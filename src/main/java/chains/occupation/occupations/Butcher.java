package chains.occupation.occupations;

import chains.occupation.type.Craft;
import chains.worker.Worker;

public class Butcher extends Craft {

    public Butcher(Worker worker) {
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
