import java.util.*;
import java.io.*;
import java.lang.*;

public class makeModelMileageQueues
{
    private priorityMileageQueue[] mileageQueues;
    private int numQueues;
    private Hashtable<String, Integer> queueTable; // symbol table that hashes make + model to indexes in queues array

    public makeModelMileageQueues()
    {
        numQueues = 0; // set number of mileage queue specified by make and model to 0
        queueTable = new Hashtable<String, Integer>(20);
        mileageQueues = new priorityMileageQueue[20];
    }

    // method to add a new queue to array of queues by specified make and model
    private void addMileageQueue(String makeModel)
    {
        arrayFull();
        mileageQueues[numQueues] = new priorityMileageQueue(20);
        queueTable.put(makeModel, numQueues); // puts combined make and model into symbol table
        numQueues++; // increases amount of mileage queues by specified make and model
    }

    public void resizeQueuesArray()
    {
        priorityMileageQueue[] doubleArray = new priorityMileageQueue[mileageQueues.length * 2];
        for(int i = 0; i < mileageQueues.length; i++)
            doubleArray[i] = mileageQueues[i];
        mileageQueues = doubleArray;
    }

    // method to check if array backing queues is full if so, resize array by double
    public void arrayFull()
    {
        if(numQueues >= mileageQueues.length)
        {
            resizeQueuesArray(); // doubles size of array backing queues
        }
        else // if array backing queues is not full return
            return;
    }   

    // method to add a new car into all mileage queues of specified make and model
    public void insertIntoMileageQueues(Car car)
    {
        int carIndex;
        String makeModel = car.makeModelString();
        //if make combined with model is not currently in symbol table, create a new queue for it and add to symbol table
        if(!queueTable.containsKey(makeModel))
            addMileageQueue(makeModel);
        carIndex = queueTable.get(makeModel);
        mileageQueues[carIndex].insert(car);

    }

    // updates price of a car specified by VIN number in all make and model mileage queues
    public void updateMileageQueuesPrice(String findVIN, double updatePrice, priorityMileageQueue mQueue)
    {
        int carIndex;
        Car updateCar = mQueue.getCar(findVIN);
        String makeModel = updateCar.makeModelString();
        carIndex = queueTable.get(makeModel);
        mileageQueues[carIndex].updatePrice(findVIN, updatePrice);
    }
    // updates mileage of a car specified by VIN number in all make and model mileage queues
    public void updateMileageQueuesMileage(String findVIN, int updateMileage, priorityMileageQueue mQueue)
    {
        int carIndex;
        Car updateCar = mQueue.getCar(findVIN);
        String makeModel = updateCar.makeModelString();
        carIndex = queueTable.get(makeModel);
        mileageQueues[carIndex].updateMileage(findVIN, updateMileage);

    }
    // updates color of a car specifiec by VIN number in all make and model mileage queues
    public void updateMileageQueuesColor(String findVIN, String updateColor, priorityMileageQueue mQueue)
    {
        int carIndex;
        Car updateCar = mQueue.getCar(findVIN);
        String makeModel = updateCar.makeModelString();
        carIndex = queueTable.get(makeModel);
        mileageQueues[carIndex].updateColor(findVIN, updateColor);

    }

    // deletes car from mileage queues of specified make and model
    public void deleteFromMileageQueues(String findVIN, priorityMileageQueue mQueue)
    {
        int carIndex;
        Car removeCar = mQueue.getCar(findVIN);
        String makeModel = removeCar.makeModelString();
        carIndex = queueTable.get(makeModel);
        mileageQueues[carIndex].delete(findVIN);
    }

    /**
     *  get information of car with lowest mileage, specified by make and model
     * @return information of car with lowest mileage, specified by make and model
     */

    public Car lowestMileageByMakeModel(String makeAndModel)
    {
        int carIndex = queueTable.get(makeAndModel);
        return mileageQueues[carIndex].minCarMileage();
    }

}