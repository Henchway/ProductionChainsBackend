package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.intermediate.Flour;
import chains.materials.raw.Grain;
import chains.occupation.type.Craft;
import chains.utility.Generator;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class Miller extends Craft {

    private static double weight = Baker.getWeight();

    public Miller(Worker worker) {
        super(worker);
    }


    @Override
    public void produce() {

        retrieveGrainFromWarehouse();
        produceFlour();

    }

    public void retrieveGrainFromWarehouse() {
        addResourceToLocalStorage(warehouse.retrieveResourceAmountFromWarehouse(Grain.class, (long) 50 * efficiency));
    }

    public void produceFlour() {

        List<Resource> flour = new ArrayList<>();

        int storage = localResourceStorage.getOrDefault(Grain.class, Generator.createConcurrentLinkedQueue(Resource.class)).size();
        ConcurrentLinkedQueue<Resource> resources = localResourceStorage.getOrDefault(Grain.class, Generator.createConcurrentLinkedQueue(Resource.class));
        for (int i = 0; i < storage; i = i + 5) {

            List<Resource> grain = resources
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            resources.removeAll(grain);
            flour.add(new Flour());

        }

        warehouse.addResourcesOfSameTypeToWarehouse(flour);

    }


    @Override
    public void acquireTool() {

    }

    public static double getWeight() {
        return weight;
    }

}
