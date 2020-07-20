package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.raw.Tannin;
import chains.materials.raw.Wood;
import chains.occupation.Work;
import chains.occupation.type.Labour;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Lumberjack extends Labour {


    public Lumberjack(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {
        store(produceLumber());
        store(produceTannin());
    }

    public List<Resource> produceLumber() {

        List<Resource> list = new ArrayList<>();
        Random random = new Random();
        int wood = (random.nextInt(15) + 30) * efficiency;
        for (int i = 0; i < wood; i++) {
            list.add(new Wood());
        }
        return list;
    }

    public List<Resource> produceTannin() {

        List<Resource> list = new ArrayList<>();
        Random random = new Random();
        int tannin = (random.nextInt(5) + 5) * efficiency;

        for (int i = 0; i < tannin / 10; i++) {
            list.add(new Tannin());
        }
        return list;
    }


    @Override
    public void acquireTool() {

    }
}
