package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.intermediate.Leather;
import chains.materials.raw.Hide;
import chains.materials.raw.Tannin;
import chains.occupation.type.Craft;
import chains.utility.Generator;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tanner extends Craft {

    protected static Double weight = 5.0;

    public Tanner(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {

        retrieveResourcesforLeather();
        store(produceLeather());

    }


    public void retrieveResourcesforLeather() {
        addResourceToLocalStorage(warehouse.retrieveResourceAmountFromWarehouse(Tannin.class, 2L * efficiency));
        addResourceToLocalStorage(warehouse.retrieveResourceAmountFromWarehouse(Hide.class, 10L * efficiency));
    }


    public List<Resource> produceLeather() {

        List<Resource> list = new ArrayList<>();

        /**
         * To create 5 Leather, it required 1 tannin & 5 hide
         * Efficiency raises the amount of leather retrieved and produced at once
         */

        for (int i = 0; i < localResourceStorage.getOrDefault(Tannin.class, Generator.createEmptyCopyOnWriteList(Resource.class)).size(); i = i + efficiency) {
            for (int j = 0; j < localResourceStorage.getOrDefault(Hide.class, Generator.createEmptyCopyOnWriteList(Resource.class)).size(); j = j + 5 * efficiency) {
                retrieveResourceFromLocalStorage(Tannin.class, (long) efficiency);
                retrieveResourceFromLocalStorage(Hide.class, 5L * efficiency);
                for (int k = 0; k < 5 * efficiency; k++) {
                    list.add(new Leather());
                }
            }
        }

        return list;
    }


    @Override
    public void acquireTool() {
    }

    public static Double getWeight() {
        return weight;
    }
}
