package chains.occupation.occupations;

import chains.occupation.type.Labour;
import chains.worker.Worker;

public class Stonemason extends Labour {

    public Stonemason(Worker worker) {
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
