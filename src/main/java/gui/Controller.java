package gui;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import utility.Statistics;
import worker.Worker;

import java.util.HashMap;


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


    private static LineChart.Series lineChartSeries = new LineChart.Series<>();

//    private static ReentrantLock mutex = new ReentrantLock();

    public void initialize() {

        Worker.startPopulation();
        Statistics statistics = new Statistics();
        statistics.start();

        new AnimationTimer() {

            @Override
            public void handle(long l) {

                Worker.workerMigrates();
                labelYear.setText(String.valueOf(Worker.getYearsPassed()));
                labelPopulationSize.setText(String.valueOf(statistics.getWorkerCount()));
                labelFemaleWorkers.setText(String.valueOf(statistics.getFemaleWorkersCount()));
                labelMaleWorkers.setText(String.valueOf(statistics.getMaleWorkersCount()));
                labelMigratedWorkers.setText(String.valueOf(statistics.getMigratedWorkersCount()));

                HashMap<String, Integer> vocationStatistics = statistics.getVocationMap();
                labelHunters.setText(String.valueOf(vocationStatistics.get("Hunter")));
                labelLumberjacks.setText(String.valueOf(vocationStatistics.get("Lumberjack")));
                labelMiners.setText(String.valueOf(vocationStatistics.get("Miner")));
                labelTanners.setText(String.valueOf(vocationStatistics.get("Tanner")));
                labelShepherds.setText(String.valueOf(vocationStatistics.get("Shepherd")));
                lineChartSeries.getData().add(new LineChart.Data<>(String.valueOf(Worker.getYearsPassed()), statistics.getWorkerCount()));


            }
        }.start();

        lineChart.getData().add(lineChartSeries);

    }


    @FXML
    void resetGraph(ActionEvent event) {

        lineChart.getData().clear();
        lineChartSeries = new LineChart.Series<>();
        lineChart.getData().add(lineChartSeries);

    }


}
