package classes;

public class Car {
    private String model;
    private Integer year;

    private Boolean isElectric;

    public Car() {
        model = "Default model";
        year = 2000;
        isElectric = false;
    }

    public Car(String model, Integer year, Boolean isElectric) {
        this.model = model;
        this.year = year;
        this.isElectric = isElectric;
    }

    public boolean isNewCar(){
        return year > 2020;
    }

    @Override
    public String toString() {
        return "Car{" +
                "model='" + model + '\'' +
                ", year=" + year +
                ", isElectric=" + isElectric +
                '}';
    }
}
