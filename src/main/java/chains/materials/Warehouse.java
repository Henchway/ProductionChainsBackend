package chains.materials;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class Warehouse {

    private final ConcurrentHashMap<Class<? extends Resource>, ConcurrentLinkedQueue<Resource>> warehouseStorage = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Resource> resourcesToBeRemoved = new ConcurrentLinkedQueue<>();

    public void addResourceToWarehouse(List<Resource> list) {

        if (!list.isEmpty()) {
            Class<? extends Resource> aClass = list.get(0).getClass();
            if (warehouseStorage.containsKey(aClass)) {
                warehouseStorage.get(aClass).addAll(list);
            } else {
                // Else simply add the received resource
                ConcurrentLinkedQueue<Resource> newList = new ConcurrentLinkedQueue<>(list);
                warehouseStorage.put(aClass, newList);
            }
        }

    }

    public <T extends Resource> List<Resource> retrieveResourceAmountFromWarehouse(Class<T> requestedResource, Long amount) {

        List<Resource> retrievedResources = new ArrayList<>();
        ConcurrentLinkedQueue<Resource> itemsInMap = warehouseStorage.get(requestedResource);

        if (warehouseStorage.containsKey(requestedResource)) {
            for (int i = 0; i < amount; i++) {
                retrievedResources.add(itemsInMap.poll());
            }
        }

        return retrievedResources;
    }

    public boolean retrieveFoodFromWarehouse(int amount) {

        List<Resource> retrievedResources = new ArrayList<>();
        List<Class<Food>> foodCategories = getSpecificTypeOfResource(Food.class);
        int foodPerCategory = foodCategories.size() > 0 ? amount / foodCategories.size() : 0;
        foodCategories.forEach(foodClass -> {
            retrievedResources.addAll(retrieveResourceAmountFromWarehouse(foodClass, (long) foodPerCategory));
        });
        return retrievedResources.isEmpty();

    }

    public void removeResourceFromWarehouse(Resource resource) {
        warehouseStorage.get(resource.getClass()).remove(resource);
    }

    public void bulkRemoveResourceFromWarehouse() {

        while (!resourcesToBeRemoved.isEmpty()) {
            Resource resource = resourcesToBeRemoved.poll();
            warehouseStorage.get(resource.getClass()).remove(resource);
        }

//        resourcesToBeRemoved.forEach(
//                resource -> warehouseStorage.get(resource.getClass()).removeAll(resource)
//        );
    }


    public <T> List<Class<T>> getSpecificTypeOfResource(Class<T> clazz) {

        return warehouseStorage.keySet()
                .stream()
                .filter(clazz::isAssignableFrom)
                .map(aClass -> (Class<T>) aClass)
                .collect(Collectors.toList());
    }


    public ConcurrentHashMap<Class<? extends Resource>, ConcurrentLinkedQueue<Resource>> getWarehouseStorage() {
        return warehouseStorage;
    }

    public ConcurrentLinkedQueue<Resource> getResourcesToBeRemoved() {
        return resourcesToBeRemoved;
    }
}
