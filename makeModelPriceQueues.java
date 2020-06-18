import java.util.*;
import java.io.*;
import java.lang.*;

public class makeModelPriceQueues
{
    private priorityPriceQueue[] priceQueues;
    private int numQueues;
    private Hashtable<String, Integer> queueTable; // symbol table that hashes make + model to indexs in queues array

    public makeModelPriceQueues()
    {
        numQueues = 0; // set number of price queue specified by make and model to 0
        queueTable = new Hashtable<String, Integer>(50);
        priceQueues = new priorityPriceQueue[50];
        //priceQueues[0] = new priorityPriceQueue
    }

    // method to add a new queue to array of queues by specified make and model
    private void addPriceQueue(String makeModel)
    {
        arrayFull();
        priceQueues[numQueues] = new priorityPriceQueue(20);
        queueTable.put(makeModel, numQueues); // puts combined make and model into symbol table
        numQueues++; // increases amount of prices queues by specified make and model
    }

    public void resizeQueuesArray()
    {
        priorityPriceQueue[] doubleArray = new priorityPriceQueue[priceQueues.length * 2];
        for(int i = 0; i < priceQueues.length; i++)
            doubleArray[i] = priceQueues[i];
        priceQueues = doubleArray;
    }

    // method to check if array backing queues is full if so, resize array by double
    public void arrayFull()
    {
        if(numQueues >= priceQueues.length)
        {
            resizeQueuesArray(); // doubles size of array backing queues
        }
        else // if array backing queues is not full return
            return;
    }   

    // method to add a new car into all price queues of specified make and model
    public void insertIntoPriceQueues(Car car)
    {
        int carIndex;
        String makeModel = car.makeModelString();
        //if make combined with model is not currently in symbol table, create a new queue for it and add to symbol table
        if(!queueTable.containsKey(makeModel))
            addPriceQueue(makeModel);
        carIndex = queueTable.get(makeModel);
        priceQueues[carIndex].insert(car);

    }

    // updates price of a car specified by VIN number in all make and model price queues
    public void updateQueuesPrice(String findVIN, double updatePrice, priorityPriceQueue pQueue)
    {
        int carIndex;
        Car updateCar = pQueue.getCar(findVIN);
        String makeModel = updateCar.makeModelString();
        carIndex = queueTable.get(makeModel);
        priceQueues[carIndex].updatePrice(findVIN, updatePrice);
    }
    // updates mileage of a car specified by VIN number in all make and model price queues
    public void updateQueuesMileage(String findVIN, int updateMileage, priorityPriceQueue pQueue)
    {
        int carIndex;
        Car updateCar = pQueue.getCar(findVIN);
        String makeModel = updateCar.makeModelString();
        carIndex = queueTable.get(makeModel);
        priceQueues[carIndex].updateMileage(findVIN, updateMileage);

    }
    // updates color of a car specifiec by VIN number in all make and model price queues
    public void updateQueuesColor(String findVIN, String updateColor, priorityPriceQueue pQueue)
    {
        int carIndex;
        Car updateCar = pQueue.getCar(findVIN);
        String makeModel = updateCar.makeModelString();
        carIndex = queueTable.get(makeModel);
        priceQueues[carIndex].updateColor(findVIN, updateColor);

    }

    // deletes car from queues of specified make and model
    public void deleteFromPriceQueues(String findVIN, priorityPriceQueue pQueue)
    {
        int carIndex;
        Car removeCar = pQueue.getCar(findVIN);
        String makeModel = removeCar.makeModelString();
        carIndex = queueTable.get(makeModel);
        priceQueues[carIndex].delete(findVIN);
    }

    /**
     *  get information of car with lowest price, specified by make and model
     * @return information of car with lowest price, specified by make and model
     */

    public Car lowestPriceByMakeModel(String makeAndModel)
    {
        int carIndex = queueTable.get(makeAndModel);
        return priceQueues[carIndex].minCarPrice();
    }



}