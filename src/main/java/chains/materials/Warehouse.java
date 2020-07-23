package chains.materials;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class Warehouse {

    private final ConcurrentHashMap<Class<? extends Resource>, ConcurrentLinkedQueue<Resource>> warehouseStorage = new ConcurrentHashMap<>();


    public void addResourcesOfSameTypeToWarehouse(List<Resource> list) {
        if (!list.isEmpty()) {
            warehouseStorage.computeIfAbsent(list.get(0).getClass(), aClass -> new ConcurrentLinkedQueue<>()).addAll(list);
        }
    }


    public void addResourcesOfDifferentTypeToWarehouse(List<Resource> list) {


        ConcurrentMap<Class<? extends Resource>, List<Resource>> splitResources = list.parallelStream()
                .collect(Collectors.groupingByConcurrent(Resource::getClass));

        splitResources.forEach((key, value) -> {
            warehouseStorage.computeIfAbsent(key, aClass -> new ConcurrentLinkedQueue<>()).addAll(value);
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

        long start = System.nanoTime();

        List<Resource> list = warehouseStorage.get(requestedResource)
                .parallelStream()
                .map(Lifestock.class::cast)
                .filter(Lifestock::isReadyForSlaughter)
                .limit(amount)
                .collect(Collectors.toList());

        removeResourcesFromWarehouse(list);

        long end = System.nanoTime();
        long timeElapsed = end - start;
                if ((timeElapsed / 1000000) > 10) {
            System.out.println("Execution time (lifestock ready to slaughter) in milliseconds : " +
                    timeElapsed / 1000000);
        }

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
