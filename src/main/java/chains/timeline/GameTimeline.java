package chains.timeline;

import chains.utility.Statistics;
import chains.worker.Worker;

import java.util.concurrent.CopyOnWriteArrayList;


public class GameTimeline {

    public static final CopyOnWriteArrayList<Worker> workersList = new CopyOnWriteArrayList<>();
    private static int yearsPassed = 0;
    private final static int durationOfYear = 100;
    private final int population;
    private Statistics statistics;

    public GameTimeline() {
        this.population = 100;
    }

    public void processNewYear() {

        for (Worker worker : workersList) {

            if (worker.isAlive()) {
                worker.workerAges();
                worker.checkAdulthood();
                worker.findPartner();
                worker.workerProcreates();
                worker.checkHealth();
            }

        }

        Worker.workerMigrates((int) statistics.getWorkerCount());
        statistics.generateWorkerStatistics();


    }

    public void startPopulation() {

        for (int i = 0; i < population; i++) {
            new Worker();
        }
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


}
