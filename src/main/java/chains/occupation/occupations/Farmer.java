package chains.occupation.occupations;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.lifestock.Chicken;
import chains.materials.lifestock.Cow;
import chains.materials.lifestock.Pig;
import chains.materials.lifestock.Sheep;
import chains.occupation.Work;
import chains.occupation.type.Labour;
import chains.utility.Generator;
import chains.worker.Worker;

import java.util.*;
import java.util.stream.Collectors;

public class Farmer extends Labour {

    private static double weight = 20.0;

    public Farmer(Worker worker) {

        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {

        ageLocallyHeldLifestock();
        addResourceToLocalStorage(acquireCowLifestock());
        addResourceToLocalStorage(acquireChickenLifestock());
        addResourceToLocalStorage(acquirePigLifestock());

        List<Resource> readyForSlaughterLifestock = localResourceStorage.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(resource -> Lifestock.class.isAssignableFrom(resource.getClass()))
                .map(Lifestock.class::cast)
                .filter(Lifestock::isReadyForSlaughter)
                .map(Resource.class::cast)
                .collect(Collectors.toList());

        readyForSlaughterLifestock.forEach(lifestock1 -> {
                    localResourceStorage.get(lifestock1.getClass()).remove(lifestock1);
                }
        );

        store(readyForSlaughterLifestock);
        store(acquireSheepLifestock());
    }

    public List<Resource> acquireChickenLifestock() {
        List<Resource> list = new ArrayList<>();
        int lifestock = (Generator.nextInt(5) + 5) * efficiency;
        for (int i = 0; i < lifestock; i++) {
            list.add(new Chicken());
        }
        return list;

    }

    public List<Resource> acquireCowLifestock() {
        List<Resource> list = new ArrayList<>();
        int lifestock = (Generator.nextInt(2) + 1) * efficiency;
        for (int i = 0; i < lifestock; i++) {
            list.add(new Cow());
        }
        return list;

    }

    public List<Resource> acquirePigLifestock() {
        List<Resource> list = new ArrayList<>();
        int lifestock = (Generator.nextInt(2) + 2) * efficiency;
        for (int i = 0; i < lifestock; i++) {
            list.add(new Pig());
        }
        return list;

    }

    public List<Resource> acquireSheepLifestock() {
        List<Resource> list = new ArrayList<>();
        int lifestock = (Generator.nextInt(2) + 2) * efficiency;
        for (int i = 0; i < lifestock; i++) {
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
