package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.raw.Coal;
import chains.materials.raw.IronOre;
import chains.occupation.Work;
import chains.occupation.type.Labour;
import chains.worker.Worker;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Miner extends Labour {

    public Miner(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {
        store(produceCoalAndIronOre());
    }

    public List<HashMap<Class<? extends Resource>, Long>> produceCoalAndIronOre() {

        List<HashMap<Class<? extends Resource>, Long>> list = Work.createMaps(2);
        Random random = new Random();
        Long ironOre = (random.nextInt(15) + 20L) * efficiency;
        Long coal = (random.nextInt(30) + 20L) * efficiency;
        list.get(0).put(IronOre.class, ironOre);
        list.get(1).put(Coal.class, coal);
        return list;
    }


    @Override
    public void acquireTool() {

    }
}
