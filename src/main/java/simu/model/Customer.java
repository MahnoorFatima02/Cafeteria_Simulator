package simu.model;

import simu.framework.*;

/**
 * The {@code Customer} class represents a customer in the simulation model.
 */

public class Customer {
    private static int i = 1;
    private static long sum = 0;
    private double arrivalTime;
    private double removalTime;
    private int id;
    private boolean isVegan;
    private double serviceStartTime;
    private double expectedDepartureTime;

    /**
     * Create a unique customer and check if the customer is vegan or not
     *
     * @param isVegan Indicates if the customer is vegan
     */
    public Customer(boolean isVegan) {
        id = i++;
        this.isVegan = isVegan;

        arrivalTime = Clock.getInstance().getClock();
        Trace.out(Trace.Level.INFO, "New customer #" + id + " arrived at  " + arrivalTime);
    }

    /**
     * Get the total number of customers
     *
     * @return Total number of customers
     */
    public static int getTotalCustomers() {
        return i - 1;
    }

    /**
     * Reset the total number of customers
     */
    public static void resetTotalCustomers() {
        i = 1;
    }

    /**
     * Check if the customer is vegan
     *
     * @return True if the customer is vegan, false otherwise
     */
    public boolean isVegan() {
        return isVegan;
    }

    /**
     * Get the time when the customer has been removed (from the system to be simulated)
     *
     * @return Customer removal time
     */
    public double getRemovalTime() {
        return removalTime;
    }

    /**
     * Set the time when the customer has been removed (from the system to be simulated)
     *
     * @param removalTime Customer removal time
     */
    public void setRemovalTime(double removalTime) {
        this.removalTime = removalTime;
    }

    /**
     * Get the time when the customer arrived (to the system to be simulated)
     *
     * @return Customer arrival time
     */
    public double getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Set the time when the customer arrived (to the system to be simulated)
     *
     * @param arrivalTime Customer arrival time
     */
    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * Get the (unique) customer ID
     *
     * @return Customer ID
     */
    public int getId() {
        return id;
    }

    /**
     * Report the measured variables of the customer to the diagnostic output
     */
    public void reportResults() {
        Trace.out(Trace.Level.INFO, "\nCustomer " + id + " ready! ");
        Trace.out(Trace.Level.INFO, "Customer " + id + " arrived: " + arrivalTime);
        Trace.out(Trace.Level.INFO, "Customer " + id + " removed: " + removalTime);
        Trace.out(Trace.Level.INFO, "Customer " + id + " stayed: " + (removalTime - arrivalTime));


        double timePerCustomer = removalTime - arrivalTime;
        sum += (long) timePerCustomer;
        double mean = (double) sum / id;

        System.out.println("Service time per customer " + timePerCustomer);

        System.out.println("Current mean of the customer service times " + mean);
    }

    /**
     * Get the expected departure time of the customer
     *
     * @return Expected departure time
     */
    public double getExpectedDepartureTime() {
        return expectedDepartureTime;
    }

    /**
     * Set the expected departure time of the customer
     *
     * @param expectedDepartureTime Expected departure time
     */
    public void setExpectedDepartureTime(double expectedDepartureTime) {
        this.expectedDepartureTime = expectedDepartureTime;
    }

    /**
     * Get the service start time of the customer
     *
     * @return Service start time
     */
    public double getServiceStartTime() {
        return serviceStartTime;
    }

    /**
     * Set the service start time of the customer
     *
     * @param serviceStartTime Service start time
     */
    public void setServiceStartTime(double serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }
}
