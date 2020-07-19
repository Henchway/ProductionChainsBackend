package chains.materials;

import java.util.HashMap;

public class Warehouse {

    private final HashMap<Class<? extends Resource>, Integer> resources = new HashMap<>();

    public <T extends Resource> void addResourceToWarehouse(Class<T> resource, int amount) {

        // If the resource already exists in the warehouse, increase the number
        if (resources.containsKey(resource)) {
            resources.put(resource, resources.get(resource) + amount);
        } else {
            // Else simply add the received resource & amount
            resources.put(resource, amount);
        }

    }

    public <T extends Resource> HashMap<Class<T>, Integer> retrieveResourceFromWarehouse(Class<T> requestedResource, int amount) {

        HashMap<Class<T>, Integer> retrievedResources = new HashMap<>();

        if (resources.containsKey(requestedResource) && resources.get(requestedResource) >= amount) {
            retrievedResources.put(requestedResource, amount);
            resources.put(requestedResource, resources.get(requestedResource) - amount);
        }

        return retrievedResources;
    }

    public HashMap<Class<? extends Resource>, Integer> getResources() {
        return resources;
    }
}
