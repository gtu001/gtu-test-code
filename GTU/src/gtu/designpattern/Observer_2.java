package gtu.designpattern;

import java.util.ArrayList;
import java.util.List;

public class Observer_2 {

    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherData);
//        StaticsDisplay currentDisplay = new StaticsDisplay(weatherData);
//        ForecastDisplay currentDisplay = new ForecastDisplay(weatherData);
        weatherData.setMeasurements(80, 65, 30.4f);
        weatherData.setMeasurements(82, 70, 29.2f);
        weatherData.setMeasurements(78, 90, 30.1f);
        System.out.println("done...");
    }

    interface Subject {
        void registerObserver(Observer o);

        void removeObserver(Observer o);

        void notifyObserver();
    }

    interface Observer {
        void update(float temp, float humidity, float presure);
    }

    static class WeatherData implements Subject {
        List<Observer> observers = new ArrayList<Observer>();
        float temperature;
        float humidity;
        float pressure;

        @Override
        public void registerObserver(Observer o) {
            observers.add(o);
        }

        @Override
        public void removeObserver(Observer o) {
            int index = observers.indexOf(o);
            if (index >= 0) {
                observers.remove(index);
            }
        }

        @Override
        public void notifyObserver() {
            for (int ii = 0; ii < observers.size(); ii++) {
                Observer observer = (Observer) observers.get(ii);
                observer.update(temperature, humidity, pressure);
            }
        }

        public void measurementsChanged() {
            notifyObserver();
        }

        public void setMeasurements(float temperature, float humidity, float pressure) {
            this.temperature = temperature;
            this.humidity = humidity;
            this.pressure = pressure;
            measurementsChanged();
        }
    }

    static class CurrentConditionsDisplay implements Observer {
        float temperature;
        float humidity;
        float pressure;
        Subject weatherData;

        CurrentConditionsDisplay(Subject weatherData) {
            this.weatherData = weatherData;
            this.weatherData.registerObserver(this);
        }

        @Override
        public void update(float temp, float humidity, float presure) {
            this.temperature = temp;
            this.humidity = humidity;
            this.pressure = presure;
            System.out.println("Current conditions : " + temperature + "F degrees and " + humidity + "% humidity");
        }
    }
}
