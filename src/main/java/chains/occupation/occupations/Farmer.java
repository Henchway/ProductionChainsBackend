package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.lifestock.Chicken;
import chains.materials.lifestock.Cow;
import chains.materials.lifestock.Pig;
import chains.materials.lifestock.Sheep;
import chains.occupation.Work;
import chains.occupation.type.Labour;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Farmer extends Labour {
    private static double weight = 10.0;
    public Farmer(Worker worker) {

        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {
        store(acquireChickenLifestock());
        store(acquireCowLifestock());
        store(acquirePigLifestock());
        store(acquireSheepLifestock());
    }

    public List<Resource> acquireChickenLifestock() {
        List<Resource> list = new ArrayList<>();
        Random random = new Random();
        int lifestock = (random.nextInt(5) + 5) * efficiency;

        for (int i = 0; i < lifestock; i++) {
            list.add(new Chicken());
        }
        return list;

    }

    public List<Resource> acquireCowLifestock() {
        List<Resource> list = new ArrayList<>();
        Random random = new Random();
        int lifestock = (random.nextInt(2) + 2) * efficiency;

        for (int i = 0; i < lifestock; i++) {
            list.add(new Cow());
        }
        return list;

    }

    public List<Resource> acquirePigLifestock() {
        List<Resource> list = new ArrayList<>();
        Random random = new Random();
        int lifestock = (random.nextInt(2) + 3) * efficiency;

        for (int i = 0; i < lifestock; i++) {
            list.add(new Pig());
        }
        return list;

    }

    public List<Resource> acquireSheepLifestock() {
        List<Resource> list = new ArrayList<>();
        Random random = new Random();
        int lifestock = (random.nextInt(2) + 3) * efficiency;

        for (int i = 0; i < lifestock; i++) {
            list.add(new Sheep());
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
