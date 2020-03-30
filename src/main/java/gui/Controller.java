package gui;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import utility.Statistics;
import worker.Worker;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;


public class Controller {

    @FXML
    private Label labelYear;

    @FXML
    private Label labelPopulationSize;

    @FXML
    private Label labelFemaleWorkers;

    @FXML
    private Label labelMaleWorkers;

    @FXML
    private Label labelHunters;

    @FXML
    private Label labelLumberjacks;

    @FXML
    private Label labelMiners;

    @FXML
    private Label labelShepherds;

    @FXML
    private Label labelTanners;

    @FXML
    private LineChart<Integer, Integer> lineChart;

    @FXML
    private Label labelMigratedWorkers;


    private static XYChart.Series lineChartSeries = new XYChart.Series();

    private static ReentrantLock mutex = new ReentrantLock();

    public void initialize() {

        lineChartSeries.setName("Population Growth");
        Worker.startPopulation();
        Statistics statistics = Statistics.createStatistics();
        statistics.start();
        lineChart.getData().add(lineChartSeries);
        createTimer(statistics);

    }

    void createTimer(Statistics statistics) {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                /* Note to self: https://stackoverflow.com/questions/13784333/platform-runlater-and-task-in-javafx
                runLater übergibt den Task an den JavaFX Thread um Concurrency Issues zu verhindern.
                Weiterer Vortei: Beim Animation Timer kann die Tick-Rate nicht beinflusst werden, bei gewöhnlichen Threads schon, bzw. speziell beim Timer ist das einfach.
                 */

                Platform.runLater(() -> {

                    Worker.workerMigrates();
                    lineChartSeries.getData().add(new LineChart.Data<>(String.valueOf(Worker.getYearsPassed()), statistics.getWorkerCount()));
                    labelYear.setText(String.valueOf(Worker.getYearsPassed()));
                    labelPopulationSize.setText(String.valueOf(statistics.getWorkerCount()));
                    labelFemaleWorkers.setText(String.valueOf(statistics.getFemaleWorkerCount()));
                    labelMaleWorkers.setText(String.valueOf(statistics.getMaleWorkerCount()));
                    labelMigratedWorkers.setText(String.valueOf(statistics.getMigratedWorkerCount()));
                    labelHunters.setText(String.valueOf(statistics.getVocationMap().get("Hunter")));
                    labelLumberjacks.setText(String.valueOf(statistics.getVocationMap().get("Lumberjack")));
                    labelMiners.setText(String.valueOf(statistics.getVocationMap().get("Miner")));
                    labelTanners.setText(String.valueOf(statistics.getVocationMap().get("Tanner")));
                    labelShepherds.setText(String.valueOf(statistics.getVocationMap().get("Shepherd")));

                });

            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, Worker.getDurationOfYear());

    }


    @FXML
    void resetGraph(ActionEvent event) {

        lineChart.getData().clear();
        lineChartSeries = new LineChart.Series<>();
        lineChartSeries.setName("Population Growth");
        lineChart.getData().add(lineChartSeries);

    }


}
