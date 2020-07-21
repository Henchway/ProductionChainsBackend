package chains.timeline;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.Warehouse;
import chains.materials.raw.Meat;
import chains.materials.raw.Milk;
import chains.utility.Generator;
import chains.utility.Statistics;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class GameTimeline {

    private final CopyOnWriteArrayList<Worker> workersList = new CopyOnWriteArrayList<>();
    private static int yearsPassed = 0;
    private final static int durationOfYear = 300;
    private Statistics statistics;
    private int population;
    private Warehouse warehouse;


    public GameTimeline(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void processNewYear() {

        workersList.stream()
                .filter(Worker::isAlive)
                .forEach(Worker::lifecycle);

        ageLifestock();
        workerMigrates(workersList.size());
        statistics.generateWorkerStatistics();
        statistics.getWarehouseResources();

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
        for (int i = 0; i < 500; i++) {
            meat.add(new Meat());
            milk.add(new Milk());
        }

        warehouse.addResourceToWarehouse(meat);
        warehouse.addResourceToWarehouse(milk);

    }

    public void ageLifestock() {
        List<Class<? extends Resource>> list = getWarehouse().getResources()
                .keySet()
                .stream()
                .filter(Objects::nonNull)
                .filter(Lifestock.class::isAssignableFrom)
                .collect(Collectors.toList());

        list.forEach(aClass -> {
            getWarehouse().getResources().get(aClass)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(Lifestock.class::cast)
                    .forEach(lifestock -> lifestock.age(this));
        });

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

    public CopyOnWriteArrayList<Worker> getWorkersList() {
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
