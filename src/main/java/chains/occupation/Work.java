package chains.occupation;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.Tool;
import chains.materials.Warehouse;
import chains.worker.Worker;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

public abstract class Work {

    protected Worker worker;
    protected Warehouse warehouse;
    protected Set<Tool> tools = new HashSet<>();
    protected int efficiency = 1;
    protected HashMap<Class<? extends Resource>, ConcurrentLinkedQueue<Resource>> localResourceStorage = new HashMap<>();
    protected HashMap<Class<? extends Lifestock>, PriorityBlockingQueue<Lifestock>> localLifestockStorage = new HashMap<>();


    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     *
     */
    public abstract void produce();

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void setEfficiency() {

        this.efficiency = 1;
        for (int i = 0; i < tools.size(); i++) {
            efficiency = efficiency * 2;
        }
    }

    public abstract void acquireTool();


    public void addResourceToLocalStorage(List<Resource> list) {

        ConcurrentMap<Class<? extends Resource>, List<Resource>> splitResources = list.parallelStream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingByConcurrent(Resource::getClass));
        splitResources.forEach((key, value) -> {
            localResourceStorage.computeIfAbsent(key, aClass -> new ConcurrentLinkedQueue<>()).addAll(value);
        });

    }

    public void addLifestockToLocalStorage(List<Lifestock> list) {

        ConcurrentMap<Class<? extends Lifestock>, List<Lifestock>> splitResources = list.parallelStream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingByConcurrent(Lifestock::getClass));

        splitResources.forEach((key, value) -> {
            localLifestockStorage.computeIfAbsent(key, aClass -> new PriorityBlockingQueue<>(100, Comparator.comparingInt(Lifestock::getAge))).addAll(value);
        });

    }


    public <T extends Resource> List<Resource> retrieveResourceFromLocalStorage(Class<T> requestedResource, Long amount) {

        List<Resource> retrievedResources = new ArrayList<>();
        ConcurrentLinkedQueue<Resource> itemsInMap = localResourceStorage.get(requestedResource);

        if (localResourceStorage.containsKey(requestedResource)) {
            for (int i = 0; i < amount; i++) {
                retrievedResources.add(itemsInMap.poll());
            }
        }

        return retrievedResources;
    }

    public <T extends Lifestock> List<Lifestock> retrieveLifestockFromLocalStorage(Class<T> requestedLifestock, Long amount) {

        List<Lifestock> retrievedLifestock = new ArrayList<>();
        PriorityBlockingQueue<Lifestock> itemsInMap = localLifestockStorage.get(requestedLifestock);

        if (localLifestockStorage.containsKey(requestedLifestock)) {
            for (int i = 0; i < amount; i++) {
                retrievedLifestock.add(itemsInMap.poll());
            }
        }

        return retrievedLifestock;
    }


    public static List<HashMap<Class<? extends Resource>, Long>> createMaps(int amount) {

        List<HashMap<Class<? extends Resource>, Long>> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            list.add(new HashMap<>());
        }
        return list;
    }

    public <T> List<Class<T>> getSpecificTypeOfResource(Class<T> clazz) {

        return localResourceStorage.keySet()
                .stream()
                .filter(clazz::isAssignableFrom)
                .map(aClass -> (Class<T>) aClass)
                .collect(Collectors.toList());
    }

    public void ageLocallyHeldLifestock() {

        localLifestockStorage
                .values()
                .stream()
                .filter(Objects::nonNull)
                .forEach(lifestocks -> lifestocks
                        .stream()
                        .filter(Objects::nonNull)
                        .forEach(lifestock -> {
                            lifestock.age();
                            if (!lifestock.isAlive()) {
                                lifestocks.remove(lifestock);
                            }

                        }));

//        List<Class<Lifestock>> lifestock = getSpecificTypeOfResource(Lifestock.class);
//        lifestock.forEach(lifestockClass -> {
//
//            List<Lifestock> list = localResourceStorage
//                    .get(lifestockClass)
//                    .stream()
//                    .filter(Objects::nonNull)
//                    .map(Lifestock.class::cast)
//                    .collect(Collectors.toList());
//
//            list.forEach(Lifestock::age);
//
//            List<Resource> deadLifestock = list.stream()
//                    .filter(Objects::nonNull)
//                    .filter(lifestock1 -> !lifestock1.isAlive())
//                    .map(Resource.class::cast)
//                    .collect(Collectors.toList());
//
//            removeResourcesFromLocalStorage(deadLifestock);
//
//        });
    }

    public void removeResourceFromLocalStorage(Resource resource) {
        localResourceStorage.get(resource.getClass()).remove(resource);
    }

    public void removeResourcesFromLocalStorage(List<Resource> list) {
        list.forEach(this::removeResourceFromLocalStorage);
    }

    public HashMap<Class<? extends Resource>, ConcurrentLinkedQueue<Resource>> getLocalResourceStorage() {
        return localResourceStorage;
    }

}
