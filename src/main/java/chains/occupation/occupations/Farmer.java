package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.lifestock.Chicken;
import chains.materials.lifestock.Cow;
import chains.materials.lifestock.Pig;
import chains.materials.lifestock.Sheep;
import chains.occupation.Work;
import chains.occupation.type.Labour;
import chains.worker.Worker;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Farmer extends Labour {

    public Farmer(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {
        store(aqcuireLifestock());
    }

    public List<HashMap<Class<? extends Resource>, Long>> aqcuireLifestock() {

        List<HashMap<Class<? extends Resource>, Long>> list = Work.createMaps(4);
        Random random = new Random();
        Long chickens = (random.nextInt(5) + 5L) * efficiency;
        Long cows = (random.nextInt(2) + 2L) * efficiency;
        Long pigs = (random.nextInt(2) + 3L) * efficiency;
        Long sheeps = (random.nextInt(2) + 3L) * efficiency;

        list.get(0).put(Chicken.class, chickens);
        list.get(1).put(Cow.class, cows);
        list.get(2).put(Pig.class, pigs);
        list.get(3).put(Sheep.class, sheeps);

        return list;
    }


    @Override
    public void acquireTool() {

    }
}
