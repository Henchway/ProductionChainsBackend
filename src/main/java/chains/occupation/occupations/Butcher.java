package chains.occupation.occupations;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.raw.Meat;
import chains.occupation.type.Craft;
import chains.utility.Generator;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Butcher extends Craft {
    private static double weight = 15.0;

    public Butcher(Worker worker) {

        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }


    @Override
    public void produce() {

        addResourceToLocalStorage(retrieveReadyForSlaughterLifestockFromWarehouse());
        store(produceMeat());

    }

    public List<Resource> retrieveReadyForSlaughterLifestockFromWarehouse() {

        List<Class<Lifestock>> lifestockList = warehouse.getSpecificTypeOfResource(Lifestock.class);
        ConcurrentLinkedQueue<Resource> retrievedLifestock = new ConcurrentLinkedQueue<>();
        List<Resource> lifestockToSlaughter = new ArrayList<>();
        List<Resource> lifestockToReturn = new ArrayList<>();

        lifestockList.forEach(aClass -> {

            for (int i = 0; i < 10 + Generator.nextInt(5); i++) {
                Resource resource = warehouse.getWarehouseStorage().get(aClass).poll();
                if (resource != null) {
                    retrievedLifestock.offer(resource);
                }
            }

        });

        retrievedLifestock.stream()
                .filter(Objects::nonNull)
                .map(Lifestock.class::cast)
                .forEachOrdered(lifestock -> {
                    if (lifestock.isReadyForSlaughter()) {
                        lifestockToSlaughter.add(retrievedLifestock.poll());
                    } else {
                        lifestockToReturn.add(retrievedLifestock.poll());
                    }
                });

        warehouse.addResourceToWarehouse(lifestockToReturn);
        return lifestockToSlaughter;

    }


    public List<Resource> produceMeat() {

        List<Resource> meatList = new ArrayList<>();
        List<Class<Lifestock>> lifestock = getSpecificTypeOfResource(Lifestock.class);

        lifestock.forEach(lifestockClass -> {
            retrieveResourceFromLocalStorage(lifestockClass, (long) localResourceStorage
                    .getOrDefault(lifestockClass, Generator.createEmptyCopyOnWriteList(Resource.class)).size())
                    .stream()
                    .map(Lifestock.class::cast)
                    .forEach(lifestock1 -> {
                        for (int i = 0; i < lifestock1.getMeat(); i++) {
                            meatList.add(new Meat());
                        }
                    });
        });

        return meatList;

    }


    @Override
    public void acquireTool() {

    }

    public static double getWeight() {
        return weight;
    }
}
