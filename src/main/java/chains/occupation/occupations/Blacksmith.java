package chains.occupation.occupations;

import chains.materials.Resource;
import chains.occupation.type.Craft;
import chains.worker.Worker;

import java.util.HashMap;
import java.util.List;

public class Blacksmith extends Craft {


    public Blacksmith(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public List<HashMap<Class<? extends Resource>, Integer>> produce() {
        return null;
    }

    @Override
    public void acquireTool() {

    }
}
