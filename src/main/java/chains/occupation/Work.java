package chains.occupation;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.Tool;
import chains.materials.Warehouse;
import chains.worker.Worker;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public abstract class Work {

    protected Worker worker;
    protected Warehouse warehouse;
    protected Set<Tool> tools = new HashSet<>();
    protected int efficiency = 1;
    protected HashMap<Class<? extends Resource>, ConcurrentLinkedQueue<Resource>> localResourceStorage = new HashMap<>();


    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     *
     */
    public abstract void produce();

    public void storeDifferentTypes(List<Resource> resource) {
        warehouse.addResourcesOfDifferentTypeToWarehouse(resource);
    }

    public void storeSameTypes(List<Resource> resource) {
        warehouse.addResourcesOfSameTypeToWarehouse(resource);
    }

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

        list.stream()
                .filter(Objects::nonNull)
                .forEach(resource -> {
                    Class<? extends Resource> aClass = resource.getClass();

                    if (localResourceStorage.containsKey(aClass)) {
                        localResourceStorage.get(aClass).offer(resource);
                    } else {
                        // Else simply add the received resource
                        ConcurrentLinkedQueue<Resource> newResourceQueue = new ConcurrentLinkedQueue<>();
                        newResourceQueue.offer(resource);
                        localResourceStorage.put(aClass, newResourceQueue);
                    }
                });

//        if (!list.isEmpty()) {
//            list.stream()
//                    .filter(Objects::nonNull)
//                    .forEach(resource -> {
//
//                        Class<? extends Resource> aClass = resource.getClass();
//
//                        // If the resource already exists in the warehouse, increase the number of stored pieces
//                        if (localResourceStorage.containsKey(aClass)) {
//                            localResourceStorage.get(aClass).add(resource);
//                        } else {
//                            // Else simply add the received resource & amount
//                            CopyOnWriteArrayList<Resource> newList = new CopyOnWriteArrayList<>();
//                            newList.add(resource);
//                            localResourceStorage.put(aClass, newList);
//                        }
//
//                    });
//
//        }
    }

    public <T extends Resource> List<Resource> retrieveResourceFromLocalStorage(Class<T> requestedResource, Long amount) {


        List<Resource> retrievedResources = new ArrayList<>();
        ConcurrentLinkedQueue<Resource> itemsInMap = localResourceStorage.get(requestedResource);

        if (localResourceStorage.containsKey(requestedResource)) {
            for (int i = 0; i < amount; i++) {
                retrievedResources.add(itemsInMap.poll());
            }
        }

//        List<Resource> retrievedResources = new ArrayList<>();
//        List<Resource> itemsInMap = localResourceStorage.get(requestedResource);
//
//        if (localResourceStorage.containsKey(requestedResource)) {
//            if (itemsInMap.size() < amount) {
//                retrievedResources.addAll(itemsInMap);
//                localResourceStorage.remove(requestedResource);
//            } else {
//                List<Resource> list = new ArrayList<>();
//                for (int i = 0; i < amount; i++) {
//                    list.add(itemsInMap.get(itemsInMap.size() - 1));
//                    itemsInMap.remove(itemsInMap.size() - 1);
//                }
//                retrievedResources.addAll(list);
//            }
//        }

        return retrievedResources;
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

        List<Class<Lifestock>> lifestock = getSpecificTypeOfResource(Lifestock.class);
        lifestock.forEach(lifestockClass -> {

            List<Lifestock> list = localResourceStorage
                    .get(lifestockClass)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(Lifestock.class::cast)
                    .collect(Collectors.toList());

            list.forEach(Lifestock::age);

            List<Resource> deadLifestock = list.stream()
                    .filter(Objects::nonNull)
                    .filter(lifestock1 -> !lifestock1.isAlive())
                    .map(Resource.class::cast)
                    .collect(Collectors.toList());

            removeResourcesFromLocalStorage(deadLifestock);

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
