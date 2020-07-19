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
    protected HashMap<Class<? extends Resource>, Integer> localResourceStorage = new HashMap<>();

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     * @return Returns a list of produced resources
     * The hash map contains the resource and the produced number
     */
    public abstract List<HashMap<Class<? extends Resource>, Integer>> produce();

    public void store(List<HashMap<Class<? extends Resource>, Integer>> resource) {
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

    public <T extends Resource> void addToLocalStorage(HashMap<Class<T>, Integer> resources) {
        resources.forEach((tClass, integer) -> localResourceStorage.put(tClass, integer));
    }

    public static List<HashMap<Class<? extends Resource>, Integer>> createMaps(int amount) {

        List<HashMap<Class<? extends Resource>, Integer>> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            list.add(new HashMap<>());
        }
        return list;
    }


}
