package chains.occupation.occupations;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.lifestock.Chicken;
import chains.materials.lifestock.Cow;
import chains.materials.lifestock.Pig;
import chains.materials.lifestock.Sheep;
import chains.materials.raw.Meat;
import chains.occupation.type.Craft;
import chains.utility.Generator;
import chains.worker.Worker;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class Butcher extends Craft {
    private static double weight = Farmer.getWeight() * 1.1;

    public Butcher(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }


    @Override
    public void produce() {

        addResourceToLocalStorage(retrieveReadyForSlaughterLifestockFromWarehouse());
        storeSameTypes(produceMeat());

    }

    public List<Resource> retrieveReadyForSlaughterLifestockFromWarehouse() {
        List<Lifestock> retrievedResources = new ArrayList<>();
        retrievedResources.addAll(lifestockDbController.retrieveLifestockReadyForSlaughterByType(Sheep.class, true, 10 * efficiency));
        retrievedResources.addAll(lifestockDbController.retrieveLifestockReadyForSlaughterByType(Cow.class, true, 8 * efficiency));
        retrievedResources.addAll(lifestockDbController.retrieveLifestockReadyForSlaughterByType(Chicken.class, true, 30 * efficiency));
        retrievedResources.addAll(lifestockDbController.retrieveLifestockReadyForSlaughterByType(Pig.class, true, 15 * efficiency));
        return retrievedResources
                .stream()
                .map(Resource.class::cast)
                .collect(Collectors.toList());

//        List<Class<Lifestock>> lifestockList = warehouse.getSpecificTypeOfResource(Lifestock.class);
//        lifestockList.forEach(lifestockClass -> {
//            addResourceToLocalStorage(warehouse.retrieveReadyForSlaughterLifestock(lifestockClass, (long) Generator.nextInt(5) + 10 * efficiency));
//        });
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
