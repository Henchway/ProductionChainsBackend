package chains.occupation.occupations;

import chains.materials.Lifestock;
import chains.materials.lifestock.Chicken;
import chains.materials.lifestock.Cow;
import chains.materials.lifestock.Pig;
import chains.materials.lifestock.Sheep;
import chains.occupation.type.Labour;
import chains.utility.Generator;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Farmer extends Labour {

    private static double weight = 60.0;

    public Farmer(Worker worker) {
        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {

        ageLocallyHeldLifestock();
        addLifestockToLocalStorage(acquireLifestockExceptSheep());

        List<Lifestock> readyForSlaughterLifestock = localLifestockStorage.values()
                .stream()
                .flatMap(Collection::stream)
                .takeWhile(Lifestock::isReadyForSlaughter)
                .collect(Collectors.toList());

        readyForSlaughterLifestock.forEach(lifestock1 -> {
                    localLifestockStorage.get(lifestock1.getClass()).remove(lifestock1);
                }
        );

        warehouse.addLifestockOfSameTypeToWarehouse(acquireSheepLifestock());

        if (readyForSlaughterLifestock.size() > 0) {
            warehouse.addLifestockOfDifferentTypeToWarehouse(readyForSlaughterLifestock);
        }
    }

    public List<Lifestock> acquireLifestockExceptSheep() {
        List<Lifestock> list = new ArrayList<>();

        for (int i = 0; i < (Generator.nextInt(5) + 10) * efficiency; i++) {
            list.add(new Chicken());
        }

        for (int i = 0; i < (Generator.nextInt(2) + 2) * efficiency; i++) {
            list.add(new Cow());
        }

        for (int i = 0; i < (Generator.nextInt(2) + 4) * efficiency; i++) {
            list.add(new Pig());
        }

        return list;
    }

    public List<Lifestock> acquireSheepLifestock() {
        List<Lifestock> list = new ArrayList<>();
        for (int i = 0; i < (Generator.nextInt(2) + 4) * efficiency; i++) {
            list.add(new Sheep());
        }
        return list;
    }


    @Override
    public void acquireTool() {

    }

    public static double getWeight() {
        return weight;
    }
}
