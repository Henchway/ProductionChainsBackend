package chains.occupation.occupations;

import chains.occupation.type.Craft;
import chains.worker.Worker;

public class Blacksmith extends Craft {


    public Blacksmith(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {

    }

    @Override
    public void acquireTool() {

    }
}
