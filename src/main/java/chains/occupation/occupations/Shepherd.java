package chains.occupation.occupations;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.lifestock.Sheep;
import chains.materials.raw.Meat;
import chains.materials.raw.Wool;
import chains.occupation.type.Labour;
import chains.utility.Generator;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Shepherd extends Labour {
    private static double weight = 10.0;

    public Shepherd(Worker worker) {

        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {
        ageLocallyHeldLifestock();
        addLifestockToLocalStorage(retrieveLifestock());
        warehouse.addResourcesOfSameTypeToWarehouse(produceWool());
        warehouse.addResourcesOfSameTypeToWarehouse(produceMeat());
    }

    public List<Lifestock> retrieveLifestock() {
        return warehouse.retrieveLifestockAmountFromWarehouse(Sheep.class, Generator.nextInt(5) + 5L * efficiency);
    }

    public List<Resource> produceMeat() {

        List<Resource> list = new ArrayList<>();

        List<Lifestock> sheepList = retrieveLifestockFromLocalStorage(Sheep.class, (long) localLifestockStorage.getOrDefault(Sheep.class, Generator.createTreeSet(Lifestock.class)).size())
                .stream()
                .filter(Objects::nonNull)
                .filter(Lifestock::isReadyForSlaughter)
                .collect(Collectors.toList());

        sheepList.forEach(sheep -> {
            for (int i = 0; i < sheep.getMeat(); i++) {
                list.add(new Meat());
            }
        });

        return list;
    }

    public List<Resource> produceWool() {

        List<Resource> list = new ArrayList<>();

        List<Sheep> sheepList = localLifestockStorage.getOrDefault(Sheep.class, Generator.createTreeSet(Lifestock.class))
                .stream()
                .filter(Objects::nonNull)
                .map(Sheep.class::cast)
                .collect(Collectors.toList());

        sheepList.forEach(sheep -> {
            for (int i = 0; i < sheep.getWool(); i++) {
                list.add(new Wool());
            }
        });

        return list;
    }

    @Override
    public void acquireTool() {

    }

    public static double getWeight() {
        return weight;
    }
}
