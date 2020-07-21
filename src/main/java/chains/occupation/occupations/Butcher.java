package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.lifestock.Chicken;
import chains.materials.lifestock.Cow;
import chains.materials.lifestock.Pig;
import chains.materials.raw.Meat;
import chains.occupation.type.Craft;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Butcher extends Craft {
    private static double weight = 10.0;
    public Butcher(Worker worker) {

        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }


    @Override
    public void produce() {

        retrieveLifestock();
        store(produceMeat());

    }

    // Improvement: take random lifestock which is older than half it's life expectancy
    public void retrieveLifestock() {

        addResourceToLocalStorage(warehouse.retrieveResourceFromWarehouse(Pig.class, 10L * efficiency));
        addResourceToLocalStorage(warehouse.retrieveResourceFromWarehouse(Cow.class, 5L * efficiency));
        addResourceToLocalStorage(warehouse.retrieveResourceFromWarehouse(Chicken.class, 20L * efficiency));

    }


    public List<Resource> produceMeat() {


        List<Resource> meatList = new ArrayList<>();

        List<Cow> cowList = retrieveResourceFromLocalStorage(Cow.class, (long) localResourceStorage.getOrDefault(Cow.class, Collections.emptyList()).size())
                .stream()
                .filter(Objects::nonNull)
                .map(Cow.class::cast)
                .collect(Collectors.toList());

        cowList.forEach(animal -> {
            for (int i = 0; i < animal.getMeat(); i++) {
                meatList.add(new Meat());
            }
        });

        List<Pig> pigList = retrieveResourceFromLocalStorage(Pig.class, (long) localResourceStorage.getOrDefault(Pig.class, Collections.emptyList()).size())
                .stream()
                .filter(Objects::nonNull)
                .map(Pig.class::cast)
                .collect(Collectors.toList());


        pigList.forEach(animal -> {
            for (int i = 0; i < animal.getMeat(); i++) {
                meatList.add(new Meat());
            }
        });

        List<Chicken> chickenList = retrieveResourceFromLocalStorage(Chicken.class, (long) localResourceStorage.getOrDefault(Chicken.class, Collections.emptyList()).size())
                .stream()
                .filter(Objects::nonNull)
                .map(Chicken.class::cast)
                .collect(Collectors.toList());

        chickenList.forEach(animal -> {
            for (int i = 0; i < animal.getMeat(); i++) {
                meatList.add(new Meat());
            }
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
