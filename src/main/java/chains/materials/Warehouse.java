package chains.materials;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class Warehouse {

    private final ConcurrentHashMap<Class<? extends Resource>, CopyOnWriteArrayList<Resource>> resources = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<Resource> resourcesToBeRemoved = new CopyOnWriteArrayList<>();

    public void addResourceToWarehouse(List<Resource> list) {

//        if (!list.isEmpty()) {
        list.stream()
                .filter(Objects::nonNull)
                .forEach(resource -> {

                    Class<? extends Resource> aClass = resource.getClass();

                    // If the resource already exists in the warehouse, increase the number of stored pieces
                    if (resources.containsKey(aClass)) {
                        resources.get(aClass).add(resource);
                    } else {
                        // Else simply add the received resource
                        CopyOnWriteArrayList<Resource> newList = new CopyOnWriteArrayList<>();
                        newList.add(resource);
                        resources.put(aClass, newList);
                    }
                });
//        }

    }

    public <T extends Resource> List<Resource> retrieveResourceAmountFromWarehouse(Class<T> requestedResource, Long amount) {

        List<Resource> retrievedResources = new ArrayList<>();
        List<Resource> itemsInMap = resources.get(requestedResource);

        if (resources.containsKey(requestedResource)) {

            retrievedResources = itemsInMap
                    .stream()
                    .unordered()
                    .limit(amount)
                    .collect(Collectors.toList());

            retrievedResources.forEach(resource -> resources.get(requestedResource).remove(resource));

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
        resources.get(resource.getClass()).remove(resource);
    }

    public void bulkRemoveResourceFromWarehouse() {
        resourcesToBeRemoved.forEach(resource -> resources.get(resource.getClass()).remove(resource));
        resourcesToBeRemoved.clear();
    }


    public <T> List<Class<T>> getSpecificTypeOfResource(Class<T> clazz) {

        return resources.keySet()
                .stream()
                .filter(clazz::isAssignableFrom)
                .map(aClass -> (Class<T>) aClass)
                .collect(Collectors.toList());
    }


    public ConcurrentHashMap<Class<? extends Resource>, CopyOnWriteArrayList<Resource>> getResources() {
        return resources;
    }

    public List<Resource> getResourcesToBeRemoved() {
        return resourcesToBeRemoved;
    }
}
