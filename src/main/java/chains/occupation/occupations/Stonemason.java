package chains.occupation.occupations;

import chains.occupation.type.Labour;
import chains.worker.Worker;

public class Stonemason extends Labour {
    private static double weight = 10.0;

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

    public static double getWeight() {
        return weight;
    }
}
