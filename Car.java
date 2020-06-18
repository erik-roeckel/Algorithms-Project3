import java.util.*;
import java.io.*;


public class Car{
    
    private String VIN;
    private String make;
    private String model;
    private double price;
    private int mileage;
    private String color;

    public Car(String carVIN, String carMake, String carModel, double carPrice, int carMileage, String carColor){
        
        VIN = carVIN;
        make = carMake;
        model = carModel;
        price = carPrice;
        mileage = carMileage;
        color = carColor;
    }
    public String getVIN(){
        return VIN;
    }
    public String getMake(){
        return make;
    }
    public String getModel(){
        return model;
    }
    public double getPrice(){
        return price;
    }
    public int getMileage(){
        return mileage;
    }
    public String getColor(){
        return color;
    }
    public void setPrice(double carPrice){
        price = carPrice;
    }
    public void setMileage(int carMileage){
        mileage = carMileage;
    }
    public void setColor(String carColor){
        color = carColor;
    }
    public String toString(){
        return "VIN: " + VIN + "\nMake: " + make + "\nModel: " + model + "\nPrice: $ " + price + "\nMileage: " + mileage + "\nColor: " + color; 
    }
    public String makeModelString(){
        return make.toUpperCase() + model.toUpperCase();
    }

}