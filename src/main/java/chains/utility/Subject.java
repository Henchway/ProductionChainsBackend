package chains.utility;

import java.util.ArrayList;

public abstract class Subject {


    ArrayList<Observer> observers = new ArrayList<Observer>();

    public void register(Observer observer) {
        observers.add(observer);
    }

    public void remove(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {

        for (Observer observer : observers) {
            observer.update();
        }

    }

}
