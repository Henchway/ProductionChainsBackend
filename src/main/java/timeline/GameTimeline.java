package timeline;

import utility.Statistics;
import worker.Worker;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class GameTimeline {

    public final List<Worker> workersList;
    private int yearsPassed = 0;
    private final static int durationOfYear = 100;
    private final int population;
    private Statistics statistics;

    public GameTimeline(int population) {
        this.workersList = new CopyOnWriteArrayList<>();
        this.population = population;
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

        Worker.workerMigrates(workersList);
        statistics.generateWorkerStatistics();


    }

    public void startPopulation() {

        for (int i = 0; i < population; i++) {
            new Worker(workersList);
        }
    }


    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public int getYearsPassed() {
        return yearsPassed;
    }

    public void setYearsPassed(int yearsPassed) {
        this.yearsPassed = yearsPassed;
    }

    public static int getDurationOfYear() {
        return durationOfYear;
    }
}
