package chains.occupation.type;

import chains.occupation.Work;
import chains.worker.Worker;

public abstract class Craft extends Work {


    public Craft(Worker worker) {
        super(worker);
    }
}
