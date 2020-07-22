package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.raw.Coal;
import chains.materials.raw.IronOre;
import chains.occupation.type.Labour;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Miner extends Labour {
    private static double weight = 5.0;

    public Miner(Worker worker) {

        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {
        storeSameTypes(produceCoal());
        storeSameTypes(produceIronOre());
    }

    public List<Resource> produceCoal() {

        List<Resource> list = new ArrayList<>();
        Random random = new Random();

        int ironOre = (random.nextInt(10) + 15) * efficiency;
        for (int i = 0; i < ironOre; i++) {
            list.add(new IronOre());
        }
        return list;
    }

    public List<Resource> produceIronOre() {

        List<Resource> list = new ArrayList<>();
        Random random = new Random();

        int coal = (random.nextInt(10) + 20) * efficiency;
        for (int i = 0; i < coal; i++) {
            list.add(new Coal());
        }

        return list;

    }


    @Override
    public void acquireTool() {

    }

    public static double getWeight() {
        return weight;
    }
}
