package chains.utility;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.Warehouse;
import chains.timeline.GameTimeline;
import chains.worker.Worker;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;


public class Statistics {

    private HashSet<Worker> workersStatisticsList;
    private long workersCount;
    private long femaleWorkersCount;
    private long maleWorkersCount;
    private long migratedWorkersCount;
    private long currentYear;
    private TreeMap<String, Long> workMap;
    private final GameTimeline gameTimeline;
    private final Warehouse warehouse;
    TreeMap<String, Integer> resources;
    TreeMap<String, Integer> lifestock;
    private long locallyStoredResources;
    private long locallyStoredLifestock;
    private Map<String, Integer> deathMap;

    public Statistics(GameTimeline gameTimeline) {
        this.gameTimeline = gameTimeline;
        this.warehouse = this.gameTimeline.getWarehouse();
    }

    public void refreshStatistics() {
        generateWorkerStatistics();
    }


    public void generateWorkerStatistics() {

        workersStatisticsList = new HashSet<>(gameTimeline.getWorkersList());
        currentYear = GameTimeline.getYearsPassed();
        workersCount = workersStatisticsList.size();
        femaleWorkersCount = workersStatisticsList.stream()
                .filter(Objects::nonNull)
                .filter(worker -> worker.getGender() == 'f')
                .count();

        migratedWorkersCount = workersStatisticsList.stream()
                .filter(Objects::nonNull)
                .filter(Worker::isMigrated)
                .count();

        locallyStoredResources = workersStatisticsList.stream()
                .filter(Objects::nonNull)
                .filter(Worker::isHasWork)
                .map(worker -> worker.getWork().getLocalResourceStorage().values())
                .mapToLong(Collection::size)
                .sum();

        locallyStoredLifestock = workersStatisticsList.stream()
                .filter(Objects::nonNull)
                .filter(Worker::isHasWork)
                .map(worker -> worker.getWork().getLocalLifestockStorage().values())
                .mapToLong(Collection::size)
                .sum();

        this.deathMap = gameTimeline.getDeathMap();
        maleWorkersCount = workersCount - femaleWorkersCount;
        generateWorkMap();
        getWarehouseResources();
        getWarehouseLifestock();

    }

    /**
     * Generates a map containing the work names and count of occurrences
     */
    public void generateWorkMap() {

        Map<String, Long> workerMap = workersStatisticsList.stream()
                .filter(Objects::nonNull)
                .filter(Worker::hasVocation)
                .map(worker -> worker.getWork().toString())
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        workMap = new TreeMap<>();
        workMap.putAll(workerMap);
    }

    public void getWarehouseResources() {

        ConcurrentHashMap<Class<? extends Resource>, ConcurrentLinkedQueue<Resource>> resources = new ConcurrentHashMap<>(this.warehouse.getResourceStorage());

        this.resources = new TreeMap<>();
        resources.entrySet()
                .stream()
                .filter(Objects::nonNull)
                .forEach(classListEntry -> this.resources.put(classListEntry.getKey().getSimpleName(),
                        classListEntry.getValue().size()));

    }

    public void getWarehouseLifestock() {

        ConcurrentHashMap<Class<? extends Resource>, ConcurrentSkipListSet<Lifestock>> lifestock = new ConcurrentHashMap<>(this.warehouse.getLifestockStorage());

        this.lifestock = new TreeMap<>();
        lifestock.entrySet()
                .stream()
                .filter(Objects::nonNull)
                .forEach(classListEntry -> this.lifestock.put(classListEntry.getKey().getSimpleName(),
                        classListEntry.getValue().size()));

    }

    public long getWorkerCount() {
        return workersCount;
    }

    public long getFemaleWorkerCount() {
        return femaleWorkersCount;
    }

    public long getMaleWorkerCount() {
        return maleWorkersCount;
    }

    public long getMigratedWorkerCount() {
        return migratedWorkersCount;
    }

    public long getCurrentYear() {
        return currentYear;
    }

    public long getLocallyStoredResources() {
        return locallyStoredResources;
    }

    public TreeMap<String, Long> getWorkMap() {
        return workMap;
    }

    public Map<String, Integer> getResources() {
        return resources;
    }

    public TreeMap<String, Integer> getLifestock() {
        return lifestock;
    }

    public long getLocallyStoredLifestock() {
        return locallyStoredLifestock;
    }

    public Map<String, Integer> getDeathMap() {
        return deathMap;
    }
}
