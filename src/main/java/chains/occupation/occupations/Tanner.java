package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.intermediate.Leather;
import chains.materials.raw.Hide;
import chains.materials.raw.Tannin;
import chains.occupation.type.Craft;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tanner extends Craft {

    public Tanner(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public List<HashMap<Class<? extends Resource>, Integer>> produce() {

        ArrayList<HashMap<Class<? extends Resource>, Integer>> list = new ArrayList<>();
        list.add(produceLeather());
        return list;

    }

    public HashMap<Class<? extends Resource>, Integer> produceLeather() {

        HashMap<Class<Tannin>, Integer> tannin = warehouse.retrieveResourceFromWarehouse(Tannin.class, 1);
        addToLocalStorage(tannin);

        HashMap<Class<Hide>, Integer> rawMaterial = warehouse.retrieveResourceFromWarehouse(Hide.class, 5 * efficiency);
        addToLocalStorage(rawMaterial);

        HashMap<Class<? extends Resource>, Integer> intermediateMaterial = new HashMap<>();

        if (localResourceStorage.get(Tannin.class) != null && localResourceStorage.get(Tannin.class) > 0) {
            intermediateMaterial.put(Leather.class, localResourceStorage.get(Hide.class));
            localResourceStorage.remove(Tannin.class);
            localResourceStorage.remove(Hide.class);
        }

        return intermediateMaterial;
    }


    @Override
    public void acquireTool() {
    }
}
