package chains.occupation.occupations;

import chains.materials.Fodder;
import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.lifestock.Chicken;
import chains.materials.lifestock.Cow;
import chains.materials.lifestock.Pig;
import chains.materials.lifestock.Sheep;
import chains.materials.raw.Grain;
import chains.materials.raw.Hay;
import chains.materials.raw.Milk;
import chains.occupation.type.Labour;
import chains.utility.Generator;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Farmer extends Labour {

    private static double weight = 60.0;

    public Farmer(Worker worker) {
        super(worker);
    }


    @Override
    public void produce() {

        ageLocallyHeldLifestock();
        acquireLifestockExceptSheep();
        acquireSheepLifestock();
        addReadyToSlaughterLifestockToWarehouse();
        milkCows();
        produceGrain();
        produceHay();
        feedLifestock();
        depositFodder();

    }

    public void acquireLifestockExceptSheep() {
        List<Lifestock> list = new ArrayList<>();

        for (int i = 0; i < (Generator.nextInt(5) + 20) * efficiency; i++) {
            list.add(new Chicken());
        }

        for (int i = 0; i < (Generator.nextInt(2) + 4) * efficiency; i++) {
            list.add(new Cow());
        }

        for (int i = 0; i < (Generator.nextInt(2) + 8) * efficiency; i++) {
            list.add(new Pig());
        }

        addLifestockToLocalStorage(list);
    }

    public void acquireSheepLifestock() {
        List<Lifestock> list = new ArrayList<>();
        for (int i = 0; i < (Generator.nextInt(2) + 8) * efficiency; i++) {
            list.add(new Sheep());
        }
        warehouse.addLifestockOfSameTypeToWarehouse(list);
    }

    public void addReadyToSlaughterLifestockToWarehouse() {

        localLifestockStorage.values().forEach(lifestocks -> {

            List<Lifestock> readyForSlaughter = lifestocks
                    .stream()
                    .takeWhile(Lifestock::isReadyForSlaughter)
                    .collect(Collectors.toList());

            lifestocks.removeAll(readyForSlaughter);
            warehouse.addLifestockOfSameTypeToWarehouse(readyForSlaughter);

        });

    }

    public void milkCows() {

        List<Resource> list = new ArrayList<>();

        localLifestockStorage.getOrDefault(Cow.class, Generator.createTreeSet(Lifestock.class))
                .stream()
                .map(Cow.class::cast)
                .forEach(cow -> {
                    for (int i = 0; i < cow.getMilk(); i++) {
                        list.add(new Milk());
                    }
                });

        warehouse.addResourcesOfSameTypeToWarehouse(list);

    }

    public void produceGrain() {

        List<Resource> list = new ArrayList<>();

        for (int i = 0; i < (15 + Generator.nextInt(10)) * efficiency; i++) {
            list.add(new Grain());
        }
        addResourceToLocalStorage(list);
    }

    public void produceHay() {

        List<Resource> list = new ArrayList<>();
        long lifestockCount = localLifestockStorage.values()
                .stream()
                .mapToLong(Collection::size)
                .sum();

        for (int i = 0; i < (lifestockCount * 6 + Generator.nextInt(50)) * efficiency; i++) {
            list.add(new Hay());
        }
        addResourceToLocalStorage(list);
    }

    public void depositFodder() {

        localResourceStorage.forEach((aClass, resources) -> {

            if (Fodder.class.isAssignableFrom(aClass)) {

                List<Resource> list = resources.stream()
                        .limit(resources.size() / 4)
                        .collect(Collectors.toList());
                resources.removeAll(list);
                warehouse.addResourcesOfSameTypeToWarehouse(list);

            }

        });
    }


    @Override
    public void acquireTool() {

    }

    public static double getWeight() {
        return weight;
    }
}
