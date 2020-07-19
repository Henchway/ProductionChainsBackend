package chains.occupation.occupations;

import chains.materials.Resource;
import chains.occupation.type.Labour;
import chains.worker.Worker;

import java.util.HashMap;
import java.util.List;

public class Farmer extends Labour {

    public Farmer(Worker worker) {
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
