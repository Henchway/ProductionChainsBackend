package gui;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import utility.Statistics;
import worker.Worker;


public class Controller {

    @FXML
    private Label labelPopulationSize;

    @FXML
    private Label labelYear;

    @FXML
    private Label labelFemaleWorkers;

    @FXML
    private Label labelMaleWorkers;

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

            }
        }.start();


    }


}
