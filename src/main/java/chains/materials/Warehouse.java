package chains.materials;

import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {

    private final HashMap<Class<? extends Resource>, List<Resource>> resources = new HashMap<>();

    public void addResourceToWarehouse(List<Resource> list) {

        if (!list.isEmpty()) {
            Class<? extends Resource> resource = list.get(0).getClass();

            // If the resource already exists in the warehouse, increase the number of stored pieces
            if (resources.containsKey(resource)) {
                resources.get(resource).addAll(list);
            } else {
                // Else simply add the received resource & amount
                resources.put(resource, list);
            }
        }
    }

    public <T extends Resource> List<Resource> retrieveResourceFromWarehouse(Class<T> requestedResource, Long amount) {

        List<Resource> retrievedResources = new ArrayList<>();
        List<Resource> itemsInMap = resources.get(requestedResource);

        if (resources.containsKey(requestedResource)) {
            if (itemsInMap.size() < amount) {
                retrievedResources.addAll(itemsInMap);
                resources.remove(requestedResource);
            } else {
                List<Resource> list = new ArrayList<>();
                for (int i = 0; i < amount; i++) {
                    list.add(itemsInMap.get(itemsInMap.size() - 1));
                    itemsInMap.remove(itemsInMap.size() - 1);
                }
                retrievedResources.addAll(list);
            }
        }

        return retrievedResources;
    }

    public List<Food> retrieveFoodFromWarehouse(int amount) {

        List<Food> retrievedResources = new ArrayList<>();

        List<Food> allFood = resources.values().stream()
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(resource -> resource instanceof Food)
                .map(Food.class::cast)
                .collect(Collectors.toList());

        if (!allFood.isEmpty()) {

            int boundary = Math.min(amount, allFood.size());

            for (int i = 0; i < boundary; i++) {
                Random random = new Random();
                Food food = allFood.get(random.nextInt(allFood.size()));
                retrievedResources.add(food);
                resources.get(food.getClass()).remove(food);
            }
        }

        return retrievedResources;

    }


    public HashMap<Class<? extends Resource>, List<Resource>> getResources() {
        return resources;
    }
}
