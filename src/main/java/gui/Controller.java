package gui;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
                labelYear.setText(String.valueOf(Worker.getYearsPassed()));
                
                labelPopulationSize.setText(String.valueOf(Worker.getWorkersList().size()));

                labelFemaleWorkers.setText(String.valueOf(Worker.getWorkersList().stream()
                        .filter(worker -> worker.getGender() == 'f')
                        .count()));

                labelMaleWorkers.setText(String.valueOf(Worker.getWorkersList().stream()
                        .filter(worker -> worker.getGender() == 'm')
                        .count()));
            }
        }.start();


    }


}
