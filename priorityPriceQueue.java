
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Hashtable;

/**
 *  This implementation uses a binary heap along with an array to associate
 *  keys with integers in the given range.
 *  The <em>insert</em>, <em>delete-the-minimum</em>, <em>delete</em>,
 *  <em>change-key</em>, <em>decrease-key</em>, and <em>increase-key</em>
 *  operations take logarithmic time.
 *  The <em>is-empty</em>, <em>size</em>, <em>min-index</em>, <em>min-key</em>,
 *  <em>contains</em>, and <em>key-of</em> operations take constant time.
 *  Construction takes time proportional to the specified capacity.
 */
public class priorityPriceQueue {
    private int maxN;        // maximum number of elements on PQ
    private int numCars;  // number of elements on PQ
    private Car[] carHeap; // carHeap[i] = priority of i
    private Hashtable<String, Integer> indexTable;
    
    /**
     * Initializes an empty indexed priority queue with indices between {@code 1}
     * and {@code maxN - 1}.
     * @param  maxN the keys on this priority queue are index from {@code 1}
     *         {@code maxN - 1}
     * @throws IllegalArgumentException if {@code maxN < 0}
     */
    public priorityPriceQueue(int maxN) 
    {
        if (maxN < 0) throw new IllegalArgumentException();
        this.maxN = maxN;
        carHeap = new Car[maxN + 1];    // maxN + 1 because skipping index 0 in array
        numCars = 1; // start heap at index 1 for easier heap management
        indexTable = new Hashtable<String, Integer>(maxN); // instantiates new hashtable which used to to hash VINS to indexs in array
    }

    /**
     * Returns true if this priority queue is empty.
     *
     * @return {@code true} if this priority queue is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return numCars == 1;
    }

    /**
     * Returns the number of cars on this priority queue.
     *
     * @return the number of cars on this priority queue
     */
    public int size() {
        return numCars - 1;
    }

    // insert a new car into priority queue
    public void insert(Car car)
    {
        heapFull(); //checks if array backing heap is full, if so doubles size of array
        carHeap[numCars] = car; // puts new Car at final leaf in Heap
        indexTable.put(car.getVIN(), numCars); // puts this new Car into hashtable and computes its index based on VIN
        swim(numCars); // Makes sure new Car satisfies heap properties, if not swim up till it does
        numCars++; // increase amount of cars in queue
    }

    //double size of array backing heap if array is full
    public void resizeHeap()
    {
        Car[] doubleHeap = new Car[(maxN * 2) + 1];
        maxN *= 2; // double max number of cars
        for(int i = 1; i < carHeap.length; i++)
            doubleHeap[i] = carHeap[i];
        carHeap = doubleHeap;
    }
    // method to check if array backing heap is full is so, resize heap
    public void heapFull()
    {
        if(numCars >= maxN)
        {
            resizeHeap(); // doubles size of array backing heap
        }
        else // if array backing heap is not full return
            return;
    }

    // updates price of car based on VIN specified by usr
    public void updatePrice(String findVIN, double updatePrice)
    {
        int carIndex = indexTable.get(findVIN); // gets the index of car based on its VIN
        carHeap[carIndex].setPrice(updatePrice); // updates the price of car that was found at carIndex
        swim(carIndex); // first see if updated price should promote car to lower index in queue
        sink(carIndex); // then see if updated price should demote car to higher index in queue
    }
    // updates mileage of car based on VIN specified by user
    public void updateMileage(String findVIN, int updateMileage)
    {
        int carIndex = indexTable.get(findVIN); //gets the index of car based on its VIN
        carHeap[carIndex].setMileage(updateMileage); // updates the mileage of car that was found at carIndex
        swim(carIndex); // first see if updated mileage should promote car to lower index in queue
        sink(carIndex); // then see if updated mileage should demote car to higher index in queue
    }
    //update color of car based on VIN specified by user
    public void updateColor(String findVIN, String updateColor)
    {
        int carIndex = indexTable.get(findVIN); // gets the index of car based on its VIN
        carHeap[carIndex].setColor(updateColor); // updates the color of car that was found at carIndex
    }

    /**
     * Remove the Car associated with VIN
     *
     * @param  findVIN the index of the car to remove
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     */
    public void delete(String findVIN) {
        int carIndex = indexTable.get(findVIN); // gets the index of car based on VIN
        if (carIndex < 0 || carIndex >= maxN) throw new IllegalArgumentException();

        if(carIndex == numCars - 1) //item to remove is last item queue
        {
            String removeCar = carHeap[numCars-1].getVIN(); // gets VIN number
            indexTable.remove(removeCar); // removes car from symbol table
            carHeap[numCars - 1] = null;
            numCars--; //decrease amount of items in heap
        }
        else
        {
            exch(carIndex, numCars - 1); //exchanges Car to be removed with last leaf node
            String removeCar = carHeap[numCars - 1].getVIN();
            indexTable.remove(removeCar);
            carHeap[numCars -1] = null;
            numCars--; // decrease amount of cars in heap
            swim(carIndex);
            sink(carIndex);
            
        }
    }
    
    /**
     * Returns a minimum car based on price.
     *
     * @return a minimum car based on lowest price
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Car minCarPrice() {
        if (numCars == 1) throw new NoSuchElementException("Priority queue underflow");
        return carHeap[1];
    }

    /** 
     * gets information of car based on VIN number
     * @return Car based on VIN
    */
    public Car getCar(String findVIN)
    {
        int index = indexTable.get(findVIN);
        if (index < 0 || index >= maxN) throw new IllegalArgumentException();
        Car carInfo = carHeap[index];
        return carInfo;
    }

   /***************************************************************************
    * General helper functions. ---> taken from indexMinPQ but modified
    ***************************************************************************/
    private boolean greater(int indexOne, int indexTwo) {
        return carHeap[indexOne].getPrice() > carHeap[indexTwo].getPrice();
    }

    private void exch(int indexOne, int indexTwo) {
        Car oldCar;
        String oneVIN = carHeap[indexOne].getVIN();
        String twoVIN = carHeap[indexTwo].getVIN();
        //swap elements in hashTable
        indexTable.put(oneVIN, indexTwo);
        indexTable.put(twoVIN, indexOne);
        oldCar = carHeap[indexTwo];
        carHeap[indexTwo] = carHeap[indexOne];
        carHeap[indexOne] = oldCar;
    }


   /***************************************************************************
    * Heap helper functions. ---> taken from indexMinPQ
    ***************************************************************************/
    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= numCars - 1) {
            int j = 2*k;
            if(carHeap[j + 1] != null)
                if (j < numCars && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    // *************************************************************************

    public void printQueue()
    {
        for(int i = 1; i < carHeap.length; i++)
        {
             if(carHeap[i] != null) 
                System.out.println("Index: " + i + "\nVIN: " + carHeap[i].getVIN() + "\nPrice: $" + carHeap[i].getPrice());
        }
        
        System.out.println(indexTable.toString()); // prints how indexes are stored in symbol table to ensure they align
    }

}
