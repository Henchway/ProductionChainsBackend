package chains.occupation.occupations;

import chains.occupation.type.Labour;
import chains.worker.Worker;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Stonemason extends Labour {
    private static double weight = 5.0;

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
