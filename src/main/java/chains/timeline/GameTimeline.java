package chains.timeline;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.Warehouse;
import chains.materials.product.Bread;
import chains.materials.raw.Meat;
import chains.materials.raw.Milk;
import chains.utility.Generator;
import chains.worker.Worker;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

@Getter
@Setter
@Component
public class GameTimeline {

    private final Set<Worker> workersList;
    private int yearsPassed = 0;
    private int durationOfYear = 200;
    private int population = 20;
    private Warehouse warehouse;
    private final ConcurrentHashMap<String, Integer> deathMap = new ConcurrentHashMap<>();

    public GameTimeline(Warehouse warehouse) {
        this.warehouse = warehouse;
        this.workersList = ConcurrentHashMap.newKeySet();
    }

    public void processNewYear() {

        long start = System.nanoTime();

        workersList.parallelStream()
                .filter(Worker::isAlive)
                .forEach(Worker::lifecycle);

        ageLifestock();
        feedLifestock();
        workerMigrates(workersList.size());
//        printGCStats();
//        System.gc();

        long end = System.nanoTime();

        long timeElapsed = end - start;
        System.out.println("Execution time in milliseconds : " + timeElapsed / 1000000);
    }

    public void startPopulation() {

        for (int i = 0; i < population; i++) {
            new Worker(this, false);
        }
        setInitialResources();
    }

    public void setInitialResources() {


        List<Resource> meat = new ArrayList<>();
        List<Resource> milk = new ArrayList<>();
        List<Resource> bread = new ArrayList<>();
        for (int i = 0; i < 500 * population; i++) {
            meat.add(new Meat());
            milk.add(new Milk());
            bread.add(new Bread());
        }

        warehouse.addResourcesOfSameTypeToWarehouse(meat);
        warehouse.addResourcesOfSameTypeToWarehouse(milk);
        warehouse.addResourcesOfSameTypeToWarehouse(bread);

    }

    public void ageLifestock() {

        List<ConcurrentSkipListSet<Lifestock>> list = warehouse.getLifestockStorage()
                .values()
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        list.forEach(lifestocks -> {
            lifestocks.forEach(Lifestock::age);
            List<Lifestock> deadLifestock = lifestocks
                    .stream()
                    .filter(lifestock -> !lifestock.isAlive())
                    .collect(Collectors.toList());
            lifestocks.removeAll(deadLifestock);
        });

    }

    public void feedLifestock() {
        warehouse.getLifestockStorage().values().forEach(lifestocks -> {
            lifestocks.forEach(lifestock -> {
                for (int i = 0; i < lifestock.getFodderAmount(); i++) {
                    try {
                        warehouse.getResourceStorage().getOrDefault(lifestock.getFodder(), Generator.createConcurrentLinkedQueue(Resource.class)).remove();
                    } catch (NoSuchElementException e) {
                        if (lifestock.getMeat() > 0) {
                            lifestock.setMeat(lifestock.getMeat() - 1);
                        } else {
                            warehouse.getLifestockStorage().get(lifestock.getClass()).remove(lifestock);
                        }
                    }
                }
            });
        });
    }


    public void printGCStats() {
        long totalGarbageCollections = 0;
        long garbageCollectionTime = 0;

        for (GarbageCollectorMXBean gc :
                ManagementFactory.getGarbageCollectorMXBeans()) {

            long count = gc.getCollectionCount();

            if (count >= 0) {
                totalGarbageCollections += count;
            }

            long time = gc.getCollectionTime();

            if (time >= 0) {
                garbageCollectionTime += time;
            }
        }

        System.out.println("Total Garbage Collections: "
                + totalGarbageCollections);
        System.out.println("Total Garbage Collection Time (ms): "
                + garbageCollectionTime);
    }

    public void addWorkerToList(Worker worker) {
        workersList.add(worker);
    }

    public void removeWorkerFromList(Worker worker) {
        workersList.remove(worker);
    }

    public void workerMigrates(int workerCount) {

        for (int i = 0; i < Generator.randomMigrationRate(workerCount); i++) {

            if (Generator.randomMigrationChance() >= 8) {

                new Worker(this, true);

            }

        }

    }

    public void addDeathToMap(String reason) {
        deathMap.merge(reason, 1, Integer::sum);
    }


}
