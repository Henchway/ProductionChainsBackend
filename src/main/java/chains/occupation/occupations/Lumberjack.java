package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.raw.Tannin;
import chains.materials.raw.Wood;
import chains.occupation.Work;
import chains.occupation.type.Labour;
import chains.worker.Worker;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Lumberjack extends Labour {


    public Lumberjack(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public List<HashMap<Class<? extends Resource>, Integer>> produce() {
        return produceLumberAndTannin();
    }

    public List<HashMap<Class<? extends Resource>, Integer>> produceLumberAndTannin() {

        List<HashMap<Class<? extends Resource>, Integer>> list = Work.createMaps(2);
        Random random = new Random();
        int wood = (random.nextInt(50) + 75) * efficiency;
        list.get(0).put(Wood.class, wood);
        list.get(1).put(Tannin.class, wood / 10);
        return list;
    }

    @Override
    public void acquireTool() {

    }
}
