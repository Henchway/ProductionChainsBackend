package chains.timeline;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.Warehouse;
import chains.materials.raw.Meat;
import chains.materials.raw.Milk;
import chains.utility.Generator;
import chains.utility.Statistics;
import chains.worker.Worker;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class GameTimeline {

    private final Set<Worker> workersList;
    private static int yearsPassed = 0;
    private final static int durationOfYear = 50;
    private Statistics statistics;
    private int population = 1000;
    private Warehouse warehouse;


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
        workerMigrates(workersList.size());
        printGCStats();
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
        for (int i = 0; i < 500 * population; i++) {
            meat.add(new Meat());
            milk.add(new Milk());
        }

        warehouse.addResourcesOfSameTypeToWarehouse(meat);
        warehouse.addResourcesOfSameTypeToWarehouse(milk);

    }

    public void ageLifestock() {

        List<Lifestock> deadLifestock = new ArrayList<>();

        warehouse.getLifestockStorage().values().forEach(lifestocks -> {
            lifestocks.forEach(
                    lifestock1 -> {
                        lifestock1.age();
                        if (!lifestock1.isAlive()) {
                            warehouse.removeLifestockFromLifestockStorage(lifestock1);
                        }
                    });
        });

//        List<Class<Lifestock>> lifestock = warehouse.getTypesOfLifestock(Lifestock.class);
//        lifestock.forEach(lifestockClass -> {
//            List<Lifestock> list = warehouse.getLifestockStorage()
//                    .get(lifestockClass)
//                    .parallelStream()
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//
//            List<Resource> deadLifestock = list
//                    .parallelStream()
//                    .filter(Objects::nonNull)
//                    .filter(lifestock1 -> !lifestock1.isAlive())
//                    .map(Resource.class::cast)
//                    .collect(Collectors.toList());
//
//            warehouse.removeResourcesFromWarehouse(deadLifestock);
//
//        });

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


    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public static int getYearsPassed() {
        return yearsPassed;
    }

    public static void setYearsPassed(int years) {
        yearsPassed = years;
    }

    public static int getDurationOfYear() {
        return durationOfYear;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void addWorkerToList(Worker worker) {
        workersList.add(worker);
    }

    public void removeWorkerFromList(Worker worker) {
        workersList.remove(worker);
    }

    public Set<Worker> getWorkersList() {
        return workersList;
    }

    public void workerMigrates(int workerCount) {

        for (int i = 0; i < Generator.randomMigrationRate(workerCount); i++) {

            if (Generator.randomMigrationChance() >= 8) {

                new Worker(this, true);

            }

        }

    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }


}
