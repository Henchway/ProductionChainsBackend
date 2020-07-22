package chains.occupation.occupations;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.lifestock.Chicken;
import chains.materials.lifestock.Cow;
import chains.materials.lifestock.Pig;
import chains.materials.lifestock.Sheep;
import chains.occupation.type.Labour;
import chains.utility.Generator;
import chains.worker.Worker;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class Farmer extends Labour {

    private static double weight = 60.0;

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

        List<Lifestock> readyForSlaughterLifestock = localResourceStorage.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(resource -> Lifestock.class.isAssignableFrom(resource.getClass()))
                .map(Lifestock.class::cast)
                .filter(Lifestock::isReadyForSlaughter)
                .collect(Collectors.toList());

        readyForSlaughterLifestock.forEach(lifestock1 -> {
                    localResourceStorage.get(lifestock1.getClass()).remove(lifestock1);
                }
        );

        lifestockDbController.saveListToDb(readyForSlaughterLifestock);
        lifestockDbController.saveListToDb(acquireSheepLifestock());


    }

    public List<Resource> acquireChickenLifestock() {
        List<Resource> list = new ArrayList<>();
        int lifestock = (Generator.nextInt(5) + 10) * efficiency;
        for (int i = 0; i < lifestock; i++) {
            list.add(new Chicken());
        }
        return list;

    }

    public List<Resource> acquireCowLifestock() {
        List<Resource> list = new ArrayList<>();
        int lifestock = (Generator.nextInt(2) + 2) * efficiency;
        for (int i = 0; i < lifestock; i++) {
            list.add(new Cow());
        }
        return list;

    }

    public List<Resource> acquirePigLifestock() {
        List<Resource> list = new ArrayList<>();
        int lifestock = (Generator.nextInt(2) + 4) * efficiency;
        for (int i = 0; i < lifestock; i++) {
            list.add(new Pig());
        }
        return list;

    }

    public List<Lifestock> acquireSheepLifestock() {
        List<Lifestock> list = new ArrayList<>();
        int lifestock = (Generator.nextInt(2) + 4) * efficiency;
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
