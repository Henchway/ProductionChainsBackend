package chains.timeline;

import chains.db.LifestockDbController;
import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.Warehouse;
import chains.materials.raw.Meat;
import chains.materials.raw.Milk;
import chains.utility.Generator;
import chains.utility.Statistics;
import chains.worker.Worker;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class GameTimeline {

    private final Set<Worker> workersList;
    private static int yearsPassed = 0;
    private final static int durationOfYear = 50;
    private Statistics statistics;
    private int population = 1000;
    private Warehouse warehouse;
    LifestockDbController lifestockDbController;


    public GameTimeline(Warehouse warehouse) {
        this.warehouse = warehouse;
        this.workersList = ConcurrentHashMap.newKeySet();
    }

    public void processNewYear() {

        long start = System.nanoTime();

        workersList.stream()
                .filter(Worker::isAlive)
                .forEach(Worker::lifecycle);

        ageLifestock();
        workerMigrates(workersList.size());

        long end = System.nanoTime();

        long timeElapsed = end - start;

        System.out.println("Execution time in milliseconds : " +
                timeElapsed / 1000000);
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

        warehouse.addResourcesOfDifferentTypeToWarehouse(meat);
        warehouse.addResourcesOfDifferentTypeToWarehouse(milk);

    }

    public void ageLifestock() {


        List<Lifestock> allLifestock = lifestockDbController.getAllLifestock();
        allLifestock.forEach(Lifestock::age);
        List<Lifestock> deadLifestock = allLifestock.stream()
                .filter(lifestock1 -> !lifestock1.isAlive())
                .collect(Collectors.toList());
        lifestockDbController.saveToDb(allLifestock);
        lifestockDbController.removeFromDb(deadLifestock);

//        List<Class<Lifestock>> lifestock = warehouse.getSpecificTypeOfResource(Lifestock.class);
//        lifestock.forEach(lifestockClass -> {
//            List<Lifestock> list = warehouse.getWarehouseStorage()
//                    .get(lifestockClass)
//                    .stream()
//                    .filter(Objects::nonNull)
//                    .map(Lifestock.class::cast)
//                    .collect(Collectors.toList());
//
//            list.forEach(Lifestock::age);
//
//            List<Resource> deadLifestock = list.stream()
//                    .filter(Objects::nonNull)
//                    .filter(lifestock1 -> !lifestock1.isAlive())
//                    .map(Resource.class::cast)
//                    .collect(Collectors.toList());
//
//            warehouse.removeResourcesFromWarehouse(deadLifestock);

//        });

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

    @Autowired
    public void setLifestockDbController(LifestockDbController lifestockDbController) {
        this.lifestockDbController = lifestockDbController;
    }

}
