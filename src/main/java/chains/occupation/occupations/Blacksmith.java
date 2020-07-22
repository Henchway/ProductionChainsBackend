package chains.occupation.occupations;

import chains.occupation.type.Craft;
import chains.worker.Worker;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Blacksmith extends Craft {

private static double weight = 0.0;

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

    public static double getWeight() {
        return weight;
    }



}
