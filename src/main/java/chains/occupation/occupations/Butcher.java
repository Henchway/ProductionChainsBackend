package chains.occupation.occupations;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.lifestock.Chicken;
import chains.materials.lifestock.Cow;
import chains.materials.lifestock.Pig;
import chains.materials.raw.Meat;
import chains.occupation.type.Craft;
import chains.utility.Generator;
import chains.worker.Worker;

import java.util.*;
import java.util.stream.Collectors;

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
        List<Resource> retrievedLifestock = new ArrayList<>();

        lifestockList.forEach(aClass -> {

            retrievedLifestock.addAll(warehouse.getResources()
                    .get(aClass)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(Lifestock.class::cast)
                    .filter(Lifestock::isReadyForSlaughter)
                    .limit(10 + Generator.nextInt(5))
                    .map(Resource.class::cast)
                    .collect(Collectors.toList()));
        });

        retrievedLifestock.forEach(resource -> {
            warehouse.removeResourceFromWarehouse(resource);
        });

        return retrievedLifestock;

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
