package gui;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import timeline.GameTimeline;
import utility.Statistics;
import worker.Worker;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;


public class GuiController {

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
    private GameTimeline gameTimeline;
    private Statistics statistics;

    public void initialize() {

        lineChartSeries.setName("Population Growth");
        this.gameTimeline = new GameTimeline(1000);
        statistics = new Statistics(gameTimeline.workersList);
        gameTimeline.setStatistics(statistics);
        gameTimeline.startPopulation();
        statistics.generateWorkerStatistics();
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

                    gameTimeline.setYearsPassed(gameTimeline.getYearsPassed() + 1);
                    gameTimeline.processNewYear();

                    lineChartSeries.getData().add(new LineChart.Data<>(String.valueOf(gameTimeline.getYearsPassed()), statistics.getWorkerCount()));
                    labelYear.setText(String.valueOf(gameTimeline.getYearsPassed()));
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
        timer.scheduleAtFixedRate(task, 0, GameTimeline.getDurationOfYear());

    }


    @FXML
    void resetGraph(ActionEvent event) {

        lineChart.getData().clear();
        lineChartSeries = new LineChart.Series<>();
        lineChartSeries.setName("Population Growth");
        lineChart.getData().add(lineChartSeries);

    }


}
