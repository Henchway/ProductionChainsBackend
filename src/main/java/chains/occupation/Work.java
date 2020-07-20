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
    protected HashMap<Class<? extends Resource>, Long> localResourceStorage = new HashMap<Class<? extends Resource>, Long>();

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     *
     */
    public abstract void produce();

    public void store(List<HashMap<Class<? extends Resource>, Long>> resource) {
        resource.forEach(classIntegerHashMap -> {
            classIntegerHashMap.forEach((tClass, integer) -> {
                warehouse.addResourceToWarehouse(tClass, integer);
            });
        });
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

    public <T extends Resource> void addResourceToLocalStorage(HashMap<Class<T>, Long> resources) {
        resources.forEach((tClass, integer) -> {
            if (localResourceStorage.containsKey(tClass)) {
                localResourceStorage.put(tClass, localResourceStorage.get(tClass) + integer);
            } else {
                localResourceStorage.put(tClass, integer);
            }
        });
    }

    public <T extends Resource> HashMap<Class<? extends Resource>, Long> retrieveResourceFromLocalStorage(Class<T> resource, Long amount) {

        HashMap<Class<? extends Resource>, Long> map = new HashMap<>();

        if (localResourceStorage.containsKey(resource)) {
            if (amount > localResourceStorage.get(resource)) {
                map.put(resource, localResourceStorage.get(resource));
                localResourceStorage.remove(resource);
            } else {
                map.put(resource, amount);
                localResourceStorage.put(resource, localResourceStorage.get(resource) - amount);
            }
        }
        return map;
    }


    public static List<HashMap<Class<? extends Resource>, Long>> createMaps(int amount) {

        List<HashMap<Class<? extends Resource>, Long>> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            list.add(new HashMap<>());
        }
        return list;
    }


}
