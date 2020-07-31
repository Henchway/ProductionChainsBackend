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

public class Butcher extends Craft {

    private static double weight = Farmer.getWeight() * 0.2;

    public Butcher(Worker worker) {
        super(worker);
    }


    @Override
    public void produce() {

        retrieveLifestockFromWarehouse();
        produceMeat();

    }

    public void retrieveLifestockFromWarehouse() {
        List<Class<Lifestock>> lifestockList = warehouse.getTypesOfLifestock(Lifestock.class);
        lifestockList.forEach(lifestockClass -> {
            if (!lifestockClass.equals(Sheep.class)) {
                addLifestockToLocalStorage(warehouse.retrieveLifestockAmountFromWarehouse(lifestockClass, (long) Generator.nextInt(5) + 5 * efficiency));
            }
        });
    }


    public void produceMeat() {

        List<Resource> meatList = new ArrayList<>();

        localLifestockStorage.values().forEach(lifestocks -> {
            lifestocks.forEach(lifestock -> {
                for (int i = 0; i < lifestock.getMeat(); i++) {
                    meatList.add(new Meat());
                }
            });
            lifestocks.clear();

        });

        warehouse.addResourcesOfSameTypeToWarehouse(meatList);
    }


    @Override
    public void acquireTool() {

    }

    public static double getWeight() {
        return weight;
    }
}
