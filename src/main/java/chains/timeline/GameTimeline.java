package chains.timeline;

import chains.materials.Resource;
import chains.materials.Warehouse;
import chains.materials.raw.Meat;
import chains.materials.raw.Milk;
import chains.utility.Generator;
import chains.utility.Statistics;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


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

        for (Worker worker : workersList) {

            if (worker.isAlive()) {
                worker.age();
                worker.checkAdulthood();
                worker.findPartner();
                worker.procreate();
                worker.work();
                worker.eat();
                worker.checkHealth();
            }

        }

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
