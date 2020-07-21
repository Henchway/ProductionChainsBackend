package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.raw.Hide;
import chains.materials.raw.Meat;
import chains.occupation.Work;
import chains.occupation.type.Labour;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Hunter extends Labour {
    private static double weight = 10.0;
    public Hunter(Worker worker) {

        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {
        List<Resource> list = produceHide();
        store(produceMeat(list.size()));
        store(list);
    }

    public List<Resource> produceHide() {

        List<Resource> list = new ArrayList<>();
        Random random = new Random();
        int deer = (random.nextInt(10) + 10) * efficiency;
        for (int i = 0; i < deer; i++) {
            list.add(new Hide());
        }
        return list;
    }

    public List<Resource> produceMeat(int deer) {

        List<Resource> list = new ArrayList<>();
        for (int i = 0; i < deer * 6; i++) {
            list.add(new Meat());
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
