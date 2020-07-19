package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.raw.Hide;
import chains.materials.raw.Meat;
import chains.occupation.Work;
import chains.occupation.type.Labour;
import chains.worker.Worker;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Hunter extends Labour {
    public Hunter(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public List<HashMap<Class<? extends Resource>, Integer>> produce() {
        return produceMeatAndHide();
    }

    public List<HashMap<Class<? extends Resource>, Integer>> produceMeatAndHide() {

        List<HashMap<Class<? extends Resource>, Integer>> list = Work.createMaps(2);
        Random random = new Random();
        int deer = (random.nextInt(20) + 15 ) * efficiency;
        list.get(0).put(Meat.class, deer * 6);
        list.get(1).put(Hide.class, deer);

        return list;
    }

    @Override
    public void acquireTool() {

    }
}
