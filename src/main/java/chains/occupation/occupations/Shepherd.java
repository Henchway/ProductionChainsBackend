package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.lifestock.Sheep;
import chains.materials.raw.Meat;
import chains.materials.raw.Wool;
import chains.occupation.Work;
import chains.occupation.type.Labour;
import chains.worker.Worker;

import java.util.*;

public class Shepherd extends Labour {

    public Shepherd(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {
        retrieveLifestock();
        store(produceWool());
        store(produceMeat());
    }

    public void retrieveLifestock() {

        Random random = new Random();
        if (localResourceStorage.getOrDefault(Sheep.class, Collections.emptyList()).size() < 50) {
            addResourceToLocalStorage(warehouse.retrieveResourceFromWarehouse(Sheep.class, random.nextInt(5) + 5L * efficiency));
        }
    }

    public List<Resource> produceMeat() {

        List<Resource> list = new ArrayList<>();

        int availableSheeps = localResourceStorage.getOrDefault(Sheep.class, Collections.emptyList()).size() / 3;

        for (int i = 0; i < availableSheeps; i++) {
            retrieveResourceFromLocalStorage(Sheep.class, 1L);
            for (int j = 0; j < 5; j++) {
                list.add(new Meat());
            }
        }
        return list;
    }


    public List<Resource> produceWool() {

        List<Resource> list = new ArrayList<>();
        int availableSheeps = localResourceStorage.getOrDefault(Sheep.class, Collections.emptyList()).size();

        for (int i = 0; i < availableSheeps; i++) {
            for (int j = 0; j < 10; j++) {
                list.add(new Wool());
            }
        }
        return list;
    }


    @Override
    public void acquireTool() {

    }
}
