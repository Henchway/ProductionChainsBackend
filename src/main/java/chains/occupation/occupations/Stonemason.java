package chains.occupation.occupations;

import chains.occupation.type.Labour;
import chains.worker.Worker;

public class Stonemason extends Labour {
    private static double weight = 5.0;

    public Stonemason(Worker worker) {
        super(worker);
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
