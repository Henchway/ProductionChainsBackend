package chains.materials;

import java.util.HashMap;

public class Warehouse {

    private final HashMap<Class<? extends Resource>, Long> resources = new HashMap<>();

    public <T extends Resource> void addResourceToWarehouse(Class<T> resource, Long amount) {

        // If the resource already exists in the warehouse, increase the number of stored pieces
        if (resources.containsKey(resource)) {
            resources.put(resource, resources.get(resource) + amount);
        } else {
            // Else simply add the received resource & amount
            resources.put(resource, amount);
        }

    }

    public <T extends Resource> HashMap<Class<T>, Long> retrieveResourceFromWarehouse(Class<T> requestedResource, Long amount) {

        HashMap<Class<T>, Long> retrievedResources = new HashMap<Class<T>, Long>();

        if (resources.containsKey(requestedResource)) {
            if (amount > resources.get(requestedResource)) {
                retrievedResources.put(requestedResource, resources.get(requestedResource));
                resources.remove(requestedResource);
            } else {
                retrievedResources.put(requestedResource, amount);
                resources.put(requestedResource, resources.get(requestedResource) - amount);
            }
        }

        return retrievedResources;
    }

    public HashMap<Class<? extends Resource>, Long> getResources() {
        return resources;
    }
}
