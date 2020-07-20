package chains.materials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public HashMap<Class<? extends Resource>, List<Resource>> getResources() {
        return resources;
    }
}
