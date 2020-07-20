package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.lifestock.Sheep;
import chains.materials.raw.Meat;
import chains.materials.raw.Wool;
import chains.occupation.Work;
import chains.occupation.type.Labour;
import chains.worker.Worker;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Shepherd extends Labour {

    public Shepherd(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {
        store(produceMeatAndWool());
    }

    public List<HashMap<Class<? extends Resource>, Long>> produceMeatAndWool() {

        Random random = new Random();
        List<HashMap<Class<? extends Resource>, Long>> list = Work.createMaps(2);
        addResourceToLocalStorage(warehouse.retrieveResourceFromWarehouse(Sheep.class, random.nextInt(5) + 5L));


        Long meat = (random.nextInt(10) + 20L) * efficiency;
        Long wool = (random.nextInt(10) + 30L) * efficiency;
        list.get(0).put(Meat.class, meat);
        list.get(1).put(Wool.class, wool);
        return list;
    }


    @Override
    public void acquireTool() {

    }
}
