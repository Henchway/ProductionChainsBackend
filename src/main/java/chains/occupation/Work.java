package chains.occupation;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.Tool;
import chains.materials.Warehouse;
import chains.utility.Generator;
import chains.worker.Worker;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class Work {

    protected Worker worker;
    protected Warehouse warehouse;
    protected Set<Tool> tools = new HashSet<>();
    protected int efficiency = 1;
    protected HashMap<Class<? extends Resource>, ConcurrentLinkedQueue<Resource>> localResourceStorage = new HashMap<>();
    protected HashMap<Class<? extends Lifestock>, TreeSet<Lifestock>> localLifestockStorage = new HashMap<>();


    public Work(Worker worker) {

        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

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
            localLifestockStorage.computeIfAbsent(key, aClass -> new TreeSet<>(Comparator.comparingInt(Lifestock::getAge).reversed())).addAll(value);
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

        TreeSet<Lifestock> itemsInMap = localLifestockStorage.getOrDefault(requestedLifestock, Generator.createTreeSet(Lifestock.class));
        List<Lifestock> retrievedLifestock = itemsInMap
                .stream()
                .filter(Objects::nonNull)
                .limit(amount)
                .collect(Collectors.toList());
        itemsInMap.removeAll(retrievedLifestock);

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

        List<TreeSet<Lifestock>> list = localLifestockStorage
                .values()
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        list.forEach(lifestocks -> {
            lifestocks.forEach(Lifestock::age);
            List<Lifestock> deadLifestock = lifestocks
                    .stream()
                    .filter(lifestock -> !lifestock.isAlive())
                    .collect(Collectors.toList());
            lifestocks.removeAll(deadLifestock);

        });

    }

    /*
   Either animals are fed or they lose Meat
    */
    public void feedLifestock() {
        localLifestockStorage.values().forEach(lifestocks -> {
            Set<Lifestock> starvedLifestock = new HashSet<>();
            lifestocks.forEach(lifestock -> {
                for (int i = 0; i < lifestock.getFodderAmount(); i++) {
                    try {
                        localResourceStorage.get(lifestock.getFodder()).remove();
                    } catch (Exception e) {
                        if (lifestock.getMeat() > 0) {
                            lifestock.setMeat(lifestock.getMeat() - 1);
                        } else {
                            System.out.println("A " + lifestock.getClass().getSimpleName() + " starved to death.");
                            starvedLifestock.add(lifestock);
                        }
                    }
                }
            });
            lifestocks.removeAll(starvedLifestock);
        });

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
