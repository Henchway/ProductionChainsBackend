package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.intermediate.Leather;
import chains.materials.raw.Hide;
import chains.materials.raw.Tannin;
import chains.occupation.type.Craft;
import chains.worker.Worker;

import java.util.HashMap;
import java.util.List;

public class Tanner extends Craft {

    public Tanner(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {

        store(produceLeather());

    }

    public List<HashMap<Class<? extends Resource>, Long>> produceLeather() {

        addResourceToLocalStorage(warehouse.retrieveResourceFromWarehouse(Tannin.class, 2L + efficiency));
        addResourceToLocalStorage(warehouse.retrieveResourceFromWarehouse(Hide.class, 10L * efficiency));

        List<HashMap<Class<? extends Resource>, Long>> list = createMaps(1);

        /**
         * To create 5 Leather, it required 1 tannin & 5 hide
         * Efficiency raises the amount of leather retrieved and produced at once
         */
        for (int i = 0; i < localResourceStorage.getOrDefault(Tannin.class, 0L); i++) {
            for (int j = 0; j < localResourceStorage.getOrDefault(Hide.class, 0L); j = j + 5) {
                retrieveResourceFromLocalStorage(Tannin.class, 1L);
                retrieveResourceFromLocalStorage(Hide.class, 5L);
                list.get(0).put(Leather.class, 5L);
            }
        }

        return list;
    }


    @Override
    public void acquireTool() {
    }
}
