package chains.occupation.occupations;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.lifestock.Sheep;
import chains.materials.raw.Meat;
import chains.occupation.type.Craft;
import chains.utility.Generator;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Butcher extends Craft {
    private static double weight = Farmer.getWeight() * 0.5;

    public Butcher(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }


    @Override
    public void produce() {

        retrieveLifestockFromWarehouse();
        warehouse.addResourcesOfSameTypeToWarehouse(produceMeat());

    }

    public void retrieveLifestockFromWarehouse() {

        List<Class<Lifestock>> lifestockList = warehouse.getTypesOfLifestock(Lifestock.class);
        lifestockList.forEach(lifestockClass -> {
            if (!lifestockClass.equals(Sheep.class)) {
                addLifestockToLocalStorage(warehouse.retrieveLifestockAmountFromWarehouse(lifestockClass, (long) Generator.nextInt(5) + 10 * efficiency));
            }
        });

//
//
//        ConcurrentLinkedQueue<Resource> retrievedLifestock = new ConcurrentLinkedQueue<>();
//        List<Resource> lifestockToSlaughter = new ArrayList<>();
//        List<Resource> lifestockToReturn = new ArrayList<>();
//
//        lifestockList.forEach(aClass -> {
//
//
//            for (int i = 0; i < 10 + Generator.nextInt(5); i++) {
//                Resource resource = warehouse.getWarehouseStorage().get(aClass).poll();
//                if (resource != null) {
//                    retrievedLifestock.offer(resource);
//                }
//            }
//
//        });
//
//        retrievedLifestock.stream()
//                .filter(Objects::nonNull)
//                .map(Lifestock.class::cast)
//                .forEachOrdered(lifestock -> {
//                    if (lifestock.isReadyForSlaughter()) {
//                        lifestockToSlaughter.add(retrievedLifestock.poll());
//                    } else {
//                        lifestockToReturn.add(retrievedLifestock.poll());
//                    }
//                });
//
//        warehouse.addResourcesOfDifferentTypeToWarehouse(lifestockToReturn);
//        return lifestockToSlaughter;

    }



    public List<Resource> produceMeat() {

        List<Resource> meatList = new ArrayList<>();

        localLifestockStorage.values().forEach(lifestocks -> {
            lifestocks.forEach(lifestock -> {
                for (int i = 0; i < lifestock.getMeat(); i++) {
                    meatList.add(new Meat());
                }
            });
            lifestocks.clear();

        });

        return meatList;

//        lifestock.forEach(lifestockClass -> {
//            retrieveResourceFromLocalStorage(lifestockClass, (long) localResourceStorage
//                    .getOrDefault(lifestockClass, Generator.createConcurrentLinkedQueue(Resource.class)).size())
//                    .stream()
//                    .map(Lifestock.class::cast)
//                    .forEach(lifestock1 -> {
//                        for (int i = 0; i < lifestock1.getMeat(); i++) {
//                            meatList.add(new Meat());
//                        }
//                    });
//        });


    }


    @Override
    public void acquireTool() {

    }

    public static double getWeight() {
        return weight;
    }
}
