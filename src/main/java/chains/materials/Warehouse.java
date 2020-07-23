package chains.materials;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class Warehouse {

    private final ConcurrentHashMap<Class<? extends Resource>, ConcurrentLinkedQueue<Resource>> resourceStorage = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<? extends Lifestock>, ConcurrentLinkedQueue<Lifestock>> lifestockStorage = new ConcurrentHashMap<>();


    public void addResourcesOfSameTypeToWarehouse(List<Resource> list) {
        if (!list.isEmpty()) {
            resourceStorage.computeIfAbsent(list.get(0).getClass(), aClass -> new ConcurrentLinkedQueue<>()).addAll(list);
        }
    }

    public void addLifestockOfSameTypeToWarehouse(List<Lifestock> list) {
        if (!list.isEmpty()) {
            lifestockStorage.computeIfAbsent(list.get(0).getClass(), aClass -> new ConcurrentLinkedQueue<>()).addAll(list);
        }
    }


    public void addResourcesOfDifferentTypeToWarehouse(List<Resource> list) {

        ConcurrentMap<Class<? extends Resource>, List<Resource>> splitResources = list.parallelStream()
                .collect(Collectors.groupingByConcurrent(Resource::getClass));

        splitResources.forEach((key, value) -> {
            resourceStorage.computeIfAbsent(key, aClass -> new ConcurrentLinkedQueue<>()).addAll(value);
        });

    }

    public void addLifestockOfDifferentTypeToWarehouse(List<Lifestock> list) {

        ConcurrentMap<Class<? extends Lifestock>, List<Lifestock>> splitResources = list.parallelStream()
                .collect(Collectors.groupingByConcurrent(Lifestock::getClass));
        splitResources.forEach((key, value) -> {
            lifestockStorage.computeIfAbsent(key, aClass -> new ConcurrentLinkedQueue<>()).addAll(value);
        });
    }

    public <T extends Resource> List<Resource> retrieveResourceAmountFromWarehouse(Class<T> requestedResource, Long amount) {

        List<Resource> retrievedResources = new ArrayList<>();
        ConcurrentLinkedQueue<Resource> itemsInMap = resourceStorage.get(requestedResource);

        if (resourceStorage.containsKey(requestedResource)) {
            for (int i = 0; i < amount; i++) {
                retrievedResources.add(itemsInMap.poll());
            }
        }

        return retrievedResources;
    }

    public <T extends Lifestock> List<Lifestock> retrieveLifestockAmountFromWarehouse(Class<T> requestedLifestock, Long amount) {

        List<Lifestock> retrievedLifestock = new ArrayList<>();
        ConcurrentLinkedQueue<Lifestock> itemsInMap = lifestockStorage.get(requestedLifestock);

        if (lifestockStorage.containsKey(requestedLifestock)) {
            for (int i = 0; i < amount; i++) {
                retrievedLifestock.add(itemsInMap.poll());
            }
        }

        return retrievedLifestock;
    }

    public <T extends Lifestock> List<Lifestock> retrieveReadyForSlaughterLifestock(Class<T> requestedResource, Long amount) {

        long start = System.nanoTime();

        List<Lifestock> list = lifestockStorage.get(requestedResource)
                .parallelStream()
                .unordered()
                .filter(Lifestock::isReadyForSlaughter)
                .limit(amount)
                .collect(Collectors.toList());

        removeLifestockFromLifestockStorage(list);

        long end = System.nanoTime();
        long timeElapsed = end - start;
        if ((timeElapsed / 1000000) > 10) {
            System.out.println("Execution time (lifestock ready to slaughter) in milliseconds : " +
                    timeElapsed / 1000000);
        }
        return list;
    }


    public boolean removeFoodFromWarehouse(int amount) {

        List<Resource> retrievedResources = new ArrayList<>();
        List<Class<Food>> foodCategories = getTypesOfResource(Food.class);
        int foodPerCategory = foodCategories.size() > 0 ? amount / foodCategories.size() : 0;
        foodCategories.forEach(foodClass -> {
            retrievedResources.addAll(retrieveResourceAmountFromWarehouse(foodClass, (long) foodPerCategory));
        });
        return retrievedResources.isEmpty();

    }

    public void removeResourceFromWarehouse(Resource resource) {

        resourceStorage.get(resource.getClass()).remove(resource);
    }

    public void removeLifestockFromLifestockStorage(Lifestock lifestock) {

        resourceStorage.get(lifestock.getClass()).remove(lifestock);
    }

    public void removeResourcesFromWarehouse(List<Resource> list) {
        if (!list.isEmpty()) {
            resourceStorage.get(list.get(0).getClass()).removeAll(list);
        }
    }

    public void removeLifestockFromLifestockStorage(List<Lifestock> list) {
        if (!list.isEmpty()) {
            lifestockStorage.get(list.get(0).getClass()).removeAll(list);
        }
    }


    public <T> List<Class<T>> getTypesOfResource(Class<T> clazz) {

        return resourceStorage.keySet()
                .stream()
                .filter(clazz::isAssignableFrom)
                .map(aClass -> (Class<T>) aClass)
                .collect(Collectors.toList());
    }

    public <T> List<Class<T>> getTypesOfLifestock(Class<T> clazz) {

        return lifestockStorage.keySet()
                .stream()
                .filter(clazz::isAssignableFrom)
                .map(aClass -> (Class<T>) aClass)
                .collect(Collectors.toList());
    }



    public ConcurrentHashMap<Class<? extends Resource>, ConcurrentLinkedQueue<Resource>> getResourceStorage() {
        return resourceStorage;
    }

    public ConcurrentHashMap<Class<? extends Lifestock>, ConcurrentLinkedQueue<Lifestock>> getLifestockStorage() {
        return lifestockStorage;
    }
}
