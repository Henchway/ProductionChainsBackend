package chains.materials;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class Warehouse {

    private final ConcurrentHashMap<Class<? extends Resource>, ConcurrentLinkedQueue<Resource>> warehouseStorage = new ConcurrentHashMap<>();


    public void addResourcesOfSameTypeToWarehouse(List<Resource> list) {

        if (!list.isEmpty()) {
            Class<? extends Resource> aClass = list.get(0).getClass();

            if (warehouseStorage.containsKey(aClass)) {
                warehouseStorage.get(aClass).addAll(list);
            } else {
                // Else simply add the received resource
                ConcurrentLinkedQueue<Resource> newResourceQueue = new ConcurrentLinkedQueue<>(list);
                warehouseStorage.put(aClass, newResourceQueue);
            }
        }

    }


    public void addResourcesOfDifferentTypeToWarehouse(List<Resource> list) {

        list.stream()
                .filter(Objects::nonNull)
                .forEach(resource -> {
                    Class<? extends Resource> aClass = resource.getClass();

                    if (warehouseStorage.containsKey(aClass)) {
                        warehouseStorage.get(aClass).offer(resource);
                    } else {
                        // Else simply add the received resource
                        ConcurrentLinkedQueue<Resource> newResourceQueue = new ConcurrentLinkedQueue<>();
                        newResourceQueue.offer(resource);
                        warehouseStorage.put(aClass, newResourceQueue);
                    }

                });
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

    public <T extends Lifestock> List<Resource> retrieveReadyForSlaughterLifestock(Class<T> requestedResource, Long amount) {

        List<Resource> list = warehouseStorage.get(requestedResource)
                .stream()
                .sequential()
                .map(Lifestock.class::cast)
                .filter(Lifestock::isReadyForSlaughter)
                .limit(amount)
                .collect(Collectors.toList());

        removeResourcesFromWarehouse(list);
        return list;
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

    public void removeResourcesFromWarehouse(List<Resource> list) {
        if (!list.isEmpty()) {
            warehouseStorage.get(list.get(0).getClass()).removeAll(list);
        }
//        list.forEach(this::removeResourceFromWarehouse);

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
}
