import java.util.*;
import java.io.*;
import java.lang.*;
/**
 * Erik Roeckel
 * Project 3
 * CS 1501
 */

public class CarTracker {

    private static Scanner fileInput; // used to read in data from cars.txt
    private static Scanner userInput;
    private static Car newCar;
    private static boolean firstTime = true;
    // price queue for unspecified make and model
    private static priorityPriceQueue priceQueue;
    private static makeModelPriceQueues priceQueues;
    //mileage queue for unspecified make and model
    private static priorityMileageQueue mileageQueue;
    private static makeModelMileageQueues mileageQueues;
    public static final String terminate = ":";
    public static void main(String[] args) throws FileNotFoundException
    {
        userInput = new Scanner(System.in);
        priceQueue = new priorityPriceQueue(50);
        mileageQueue = new priorityMileageQueue(50);
        priceQueues = new makeModelPriceQueues();
        mileageQueues = new makeModelMileageQueues();
        initTable();

        while(true)
        {
            menuOptions(); // prints menu options
            firstTime = false;
            int userChoice = userInput.nextInt(); // gets the integer corresponding to menu option they wanted
            // loops until user enters valid menu option
            while(userChoice <= 0 || userChoice > 8)
                {
                    System.out.println("You did not enter a valid menu option, select again!");
                    menuOptions();
                    userChoice = userInput.nextInt();
                }
            menuChoice(userChoice); // executes based on whatever menu option user chose;
                
                // System.out.println("Min Price Queue:");
                // priceQueue.printQueue();
                // System.out.println("--------------------------------------");
                // System.out.println("Min Mileage Queue:");
                // mileageQueue.printQueue();
                // System.out.println("--------------------------------------");
        }
        
        

    }


    /**
     * Important Note *
     * For intializing data structure I assume that the file cars.txt is presented in the exact format it was given in
     * therefore I skip the first line of the file because it has usless text
     * @return a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    //intializes priority queue if cars.txt exists
    public static void initTable() throws FileNotFoundException
    {
        String VIN;
        String make;
        String model;
        double price;
        int mileage;
        String color;
        File file = new File("cars.txt");
        if(!file.exists())
        {
            System.out.println("No file exists to intialize car application, begin adding cars");
            return;
        }
        fileInput = new Scanner(file);
        fileInput.nextLine(); // skips first line of text from file
        fileInput.useDelimiter(terminate); //uses Semicolon to read from file
        while(fileInput.hasNextLine())
        {
            VIN = fileInput.next();
            make = fileInput.next();
            model = fileInput.next();
            price = fileInput.nextDouble();
            mileage = fileInput.nextInt();
            color = fileInput.nextLine();
            color = color.substring(1); // elimates the colon from color string b/c of nextLine
            newCar = new Car(VIN, make, model, price, mileage, color);
            priceQueue.insert(newCar);
            mileageQueue.insert(newCar);
            priceQueues.insertIntoPriceQueues(newCar);
            mileageQueues.insertIntoMileageQueues(newCar);

        }

    }

    // Displays menu options for user to chose from
    public static void menuOptions()
    {
        if(firstTime == true)
            System.out.println("Welcome to my Car buying application!");
        System.out.println("Here are your options: ");
        System.out.println("-----------------------------------");
        System.out.println("(1) Add a Car's info ");
        System.out.println("(2) Update a Car's info -- > By price, mileage, or color ");
        System.out.println("(3) Remove a Car's info ");
        System.out.println("(4) Retrieve the Car which has lowest price ");
        System.out.println("(5) Retrieve the Car which has lowest mileage");
        System.out.println("(6) Retrieve the lowest price for a Car based on specified make and model");
        System.out.println("(7) Retrieve the lowest mileage Car based on specified make and model");
        System.out.println("(8) Quit application");
        System.out.println("-----------------------------------");
        System.out.println("Please choose an option (1 - 8): ");
    }
    
    // handles options 1 - 8
    public static void menuChoice(int option)
    {
        switch(option)
        {
            // Option 1: add Car
            case 1:
            {
                System.out.println("Option 1:");
                System.out.println("-----------------------------------");
                addCar();
                System.out.println("-----------------------------------");
                break;
            }
            //Option 2: Update Car
            case 2:
            {
                System.out.println("Option 2:");
                System.out.println("-----------------------------------");
                System.out.print("Enter the VIN number for the car you wish to update: ");
                String carVIN = userInput.next();
                System.out.println("-----------------------------------");
                updateCar(carVIN);
                break;
            }
            //Option 3: Remove Car
            case 3:
            {
                System.out.println("Option 3:");
                System.out.println("-----------------------------------");
                System.out.print("Enter the VIN number for the car you wish to remove: ");
                String carVIN = userInput.next();
                priceQueues.deleteFromPriceQueues(carVIN, priceQueue);
                mileageQueues.deleteFromMileageQueues(carVIN, mileageQueue);
                priceQueue.delete(carVIN);
                mileageQueue.delete(carVIN);
                break;
            }
            //Option 4: Find lowest Car by price
            case 4:
            {
                System.out.println("Option 4:");
                Car lowestPriceCar = priceQueue.minCarPrice();
                System.out.println("-----------------------------------");
                System.out.println(lowestPriceCar.toString());
                System.out.println("-----------------------------------");
                break;
            }
            //Option 5: Find lowest Car by mileage
            case 5:
            {
                System.out.println("Option 5:");
                Car lowestMileageCar = mileageQueue.minCarMileage();
                System.out.println("-----------------------------------");
                System.out.println(lowestMileageCar.toString());
                System.out.println("-----------------------------------");
                break;
            }
            //Option 6: Find lowest price Car by specified make and model
            case 6:
            {
                String make;
                String model;
                System.out.println("Option 6:");
                System.out.println("-----------------------------------");
                System.out.print("Enter car's make: ");
                make = userInput.next();
                System.out.print("Enter car's model: ");
                model = userInput.next();
                System.out.println("-----------------------------------");
                String makeModel = make.toUpperCase() + model.toUpperCase();
                Car lowestPriceByMakeModel = priceQueues.lowestPriceByMakeModel(makeModel);
                //Car lowestPriceCar = priceQueue.priceByMake(make, model);
                System.out.println(lowestPriceByMakeModel.toString());
                System.out.println("-----------------------------------");
                break;
            }
            //Options 7: Find lowest mileage Car by specified make and model
            case 7:
            {
                String make;
                String model;
                System.out.println("Option 7: ");
                System.out.println("-----------------------------------");
                System.out.print("Enter car's make: ");
                make = userInput.next();
                System.out.print("Enter car's model: ");
                model = userInput.next();
                System.out.println("-----------------------------------");
                String makeModel = make.toUpperCase() + model.toUpperCase();
                Car lowestMileageByMakeModel = mileageQueues.lowestMileageByMakeModel(makeModel);
                System.out.println(lowestMileageByMakeModel.toString());
                System.out.println("-----------------------------------");
                break;
            }
            //Option 8: Exit Application
            case 8:
            {
                System.out.println("-----------------------------------");
                System.out.println("You chose to quit, thank you for using my car application!\n");
                System.exit(0);
                break;
            }
        }
    }
    
    // Method for adding car to queue
    public static void addCar()
    {
        String VIN;
        String make;
        String model;
        double price;
        int mileage;
        String color;
        System.out.println("Adding new Car ");
        System.out.print("Enter car's VIN number: ");
        VIN = userInput.next();
        System.out.print("Enter car's make: ");
        make = userInput.next();
        System.out.print("Enter car's model: ");
        model = userInput.next();
        System.out.print("Enter car's price: $ ");
        price = userInput.nextDouble();
        System.out.print("Enter car's mileage: ");
        mileage = userInput.nextInt();
        System.out.print("Enter car's color: ");
        color = userInput.next();

        newCar = new Car(VIN, make, model, price, mileage, color);
        priceQueue.insert(newCar);
        mileageQueue.insert(newCar);
        priceQueues.insertIntoPriceQueues(newCar);
        mileageQueues.insertIntoMileageQueues(newCar);

        System.out.println("New car info just added: ");
        System.out.println("-----------------------------------");
        System.out.println(newCar.toString());
        System.out.println("-----------------------------------");

    }
    
    // Method for updating car in queue based on: price, mileage, or color
    public static void updateCar(String updateVIN)
    {
        
        System.out.println("Options to Update: ");
        System.out.println("(1) Update car price");
        System.out.println("(2) Update car mileage");
        System.out.println("(3) Update car color");
        System.out.println("Select an option (1-3)");
        int updateChoice = userInput.nextInt();
        
        // If user doesn't enter 1,2,3
        while(updateChoice != 1 && updateChoice != 2 && updateChoice != 3)
            {
                System.out.println("You did not enter a valid menu option, select again!");
                menuOptions();
                updateChoice = userInput.nextInt();
            }
        switch(updateChoice)
        {
            case 1:
            {
                System.out.println("Option 1: ");
                System.out.print("Enter the updated car price: ");
                double updatedPrice = userInput.nextDouble();
                priceQueues.updateQueuesPrice(updateVIN, updatedPrice, priceQueue);
                mileageQueues.updateMileageQueuesPrice(updateVIN, updatedPrice, mileageQueue);
                //updatePrice both finds car based on VIN in hashtable and ensures updated car is in correct spot of heap
                priceQueue.updatePrice(updateVIN, updatedPrice); // updates price in price queue
                mileageQueue.updatePrice(updateVIN, updatedPrice); // updates price in mileage queue
                break;
            }
            case 2:
            {
                System.out.println("Option 2: ");
                System.out.print("Enter the updated car mileage: ");
                int updatedMileage = userInput.nextInt();
                priceQueues.updateQueuesMileage(updateVIN, updatedMileage, priceQueue);
                mileageQueues.updateMileageQueuesMileage(updateVIN, updatedMileage, mileageQueue);
                //updateMileage both finds car based on VIN in hashtable and ensures updated car is in correct spot of queue
                priceQueue.updateMileage(updateVIN, updatedMileage); // updates price queue
                mileageQueue.updateMileage(updateVIN, updatedMileage); // updates mileage queue
                break;
            }
            case 3:
            {
                System.out.println("Option 3: ");
                System.out.print("Enter the updated car color: ");
                String updatedColor = userInput.next();
                priceQueues.updateQueuesColor(updateVIN, updatedColor, priceQueue);
                mileageQueues.updateMileageQueuesColor(updateVIN, updatedColor, mileageQueue);
                //Update color finds car based on VIN, changing color does not change queue
                priceQueue.updateColor(updateVIN, updatedColor);
                mileageQueue.updateColor(updateVIN, updatedColor);
                break;
            }
        }


    }

    
}