package gui;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
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


    private static LineChart.Series lineChartSeries = new LineChart.Series<>();


    public void initialize() {

        Worker.startPopulation();

        new AnimationTimer() {

            @Override
            public void handle(long l) {

                Statistics.generateWorkerStatistic();
                labelYear.setText(String.valueOf(Worker.getYearsPassed()));
                labelPopulationSize.setText(String.valueOf(Statistics.getWorkerCount()));
                labelFemaleWorkers.setText(String.valueOf(Statistics.getFemaleWorkersCount()));
                labelMaleWorkers.setText(String.valueOf(Statistics.getMaleWorkersCount()));

                HashMap<String, Integer> vocationStatistics = Statistics.getVocationMap();
                labelHunters.setText(String.valueOf(vocationStatistics.get("Hunter")));
                labelLumberjacks.setText(String.valueOf(vocationStatistics.get("Lumberjack")));
                labelMiners.setText(String.valueOf(vocationStatistics.get("Miner")));
                labelTanners.setText(String.valueOf(vocationStatistics.get("Tanner")));
                labelShepherds.setText(String.valueOf(vocationStatistics.get("Shepherd")));
                lineChartSeries.getData().add(new LineChart.Data<>(String.valueOf(Worker.getYearsPassed()), Statistics.getWorkerCount()));

            }
        }.start();

        lineChart.getData().add(lineChartSeries);

    }


}
