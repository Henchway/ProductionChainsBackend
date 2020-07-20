package chains.occupation;

import chains.materials.Resource;
import chains.materials.Tool;
import chains.materials.Warehouse;
import chains.worker.Worker;

import java.util.*;

public abstract class Work {

    protected Worker worker;
    protected Warehouse warehouse;
    protected Set<Tool> tools = new HashSet<>();
    protected int efficiency = 1;
    protected HashMap<Class<? extends Resource>, List<Resource>> localResourceStorage = new HashMap<>();

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     *
     */
    public abstract void produce();

    public void store(List<Resource> resource) {
        warehouse.addResourceToWarehouse(resource);
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

    public <T extends Resource> void addResourceToLocalStorage(List<Resource> list) {

        if (!list.isEmpty()) {
            Class<? extends Resource> resource = list.get(0).getClass();

            // If the resource already exists in the warehouse, increase the number of stored pieces
            if (localResourceStorage.containsKey(resource)) {
                localResourceStorage.get(resource).addAll(list);
            } else {
                // Else simply add the received resource & amount
                localResourceStorage.put(resource, list);
            }
        }
    }

    public <T extends Resource> List<Resource> retrieveResourceFromLocalStorage(Class<T> requestedResource, Long amount) {

        List<Resource> retrievedResources = new ArrayList<>();
        List<Resource> itemsInMap = localResourceStorage.get(requestedResource);

        if (localResourceStorage.containsKey(requestedResource)) {
            if (itemsInMap.size() < amount) {
                retrievedResources.addAll(itemsInMap);
                localResourceStorage.remove(requestedResource);
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


    public static List<HashMap<Class<? extends Resource>, Long>> createMaps(int amount) {

        List<HashMap<Class<? extends Resource>, Long>> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            list.add(new HashMap<>());
        }
        return list;
    }


}
