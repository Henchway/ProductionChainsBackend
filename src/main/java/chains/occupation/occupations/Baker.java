package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.intermediate.Flour;
import chains.materials.product.Bread;
import chains.occupation.type.Craft;
import chains.utility.Generator;
import chains.worker.Worker;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@Setter
public class Baker extends Craft {

    private static double weight = 20.0;

    public Baker(Worker worker) {
        super(worker);
    }

    @Override
    public void produce() {
        retrieveFlourFromWarehouse();
        produceBread();
    }

    public void retrieveFlourFromWarehouse() {
        addResourceToLocalStorage(warehouse.retrieveResourceAmountFromWarehouse(Flour.class, (long) 10 * efficiency));
    }

    public void produceBread() {

        List<Resource> bread = new ArrayList<>();

        int storage = localResourceStorage.getOrDefault(Flour.class, Generator.createConcurrentLinkedQueue(Resource.class)).size();
        ConcurrentLinkedQueue<Resource> resources = localResourceStorage.getOrDefault(Flour.class, Generator.createConcurrentLinkedQueue(Resource.class));
        for (int i = 0; i < storage; i++) {
            resources.remove();
            for (int j = 0; j < 10; j++) {
                bread.add(new Bread());
            }
        }
        warehouse.addResourcesOfSameTypeToWarehouse(bread);
    }


    @Override
    public void acquireTool() {

    }

    public static double getWeight() {
        return weight;
    }
}
