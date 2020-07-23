package chains.occupation.occupations;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.raw.Meat;
import chains.occupation.type.Craft;
import chains.utility.Generator;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.List;

public class Butcher extends Craft {
    private static double weight = Farmer.getWeight() * 1.1;

    public Butcher(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }


    @Override
    public void produce() {

        retrieveReadyForSlaughterLifestockFromWarehouse();
        storeSameTypes(produceMeat());

    }

    public void retrieveReadyForSlaughterLifestockFromWarehouse() {

        List<Class<Lifestock>> lifestockList = warehouse.getSpecificTypeOfResource(Lifestock.class);
        lifestockList.forEach(lifestockClass -> {
            addResourceToLocalStorage(warehouse.retrieveReadyForSlaughterLifestock(lifestockClass, (long) Generator.nextInt(5) + 10 * efficiency));
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
//
//    public List<Lifestock> getSheepFromWarehouse() {
//
//        return warehouse.retrieveReadyForSlaughterLifestock(Sheep.class, (long) 5 * efficiency);
//
//    }
//
//    public List<Lifestock> getPigsFromWarehouse() {
//
//        return warehouse.retrieveReadyForSlaughterLifestock(Pig.class, (long) 5 * efficiency);
//    }
//
//    public List<Lifestock> getChickensFromWarehouse() {
//
//        return warehouse.retrieveReadyForSlaughterLifestock(Chicken.class, (long) 5 * efficiency);
//
//    }
//
//    public List<Lifestock> getCowsFromWarehouse() {
//
//        return warehouse.retrieveReadyForSlaughterLifestock(Cow.class, (long) 5 * efficiency);
//
//    }


    public List<Resource> produceMeat() {

        List<Resource> meatList = new ArrayList<>();
        List<Class<Lifestock>> lifestock = getSpecificTypeOfResource(Lifestock.class);

        lifestock.forEach(lifestockClass -> {
            retrieveResourceFromLocalStorage(lifestockClass, (long) localResourceStorage
                    .getOrDefault(lifestockClass, Generator.createConcurrentLinkedQueue(Resource.class)).size())
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
