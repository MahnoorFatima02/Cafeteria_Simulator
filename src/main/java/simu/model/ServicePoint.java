package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.*;

import java.util.LinkedList;

// TODO:
// Service Point functionalities & calculations (+ variables needed) and reporting to be implemented

/**
 * Represents a service point where customers are served.
 * Manages the queue of customers, service times, and related statistics.
 */
public class ServicePoint {
    private LinkedList<Customer> queue = new LinkedList<>(); // Data Structure used
    private ContinuousGenerator generator;
    private EventList eventList;
    private EventType eventTypeScheduled;
    //Queuestrategy strategy; // option: ordering of the customer
    private boolean reserved = false;
    private boolean isActive;
    private double totalServiceTime = 0;
    private int totalCustomersServed = 0;
    private int totalCustomersRemoved = 0;
    private double totalWaitingTime = 0;


    /**
     * Constructs a ServicePoint with the specified generator, event list, event type, and active status.
     *
     * @param generator The generator for service times
     * @param eventList The event list for scheduling events
     * @param type The type of event to be scheduled
     * @param isActive The initial active status of the service point
     */
    public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType type, boolean isActive) {
        this.eventList = eventList;
        this.generator = generator;
        this.eventTypeScheduled = type;
        this.isActive = isActive;
    }

    /**
     * Constructs a ServicePoint with the specified generator, event list, and event type.
     * This constructor is a simplified version of the main constructor, which assumes that the service point
     * is ACTIVE by default. It is designed to simplify the code and reduce repetition, since the default active
     * state is sufficient for most use cases.
     *
     * @param generator The generator for service times
     * @param eventList The event list for scheduling events
     * @param type The type of event to be scheduled
     */
    public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType type) {
        this(generator, eventList, type, true); // Default active to true
    }

    // remove repeat
    /**
     * Calculates the average service time for an array of service points.
     *
     * @param servicePoints The array of service points
     * @return The average service time
     */
    public static double getAverageServiceTime(ServicePoint[] servicePoints) {
        double totalServiceTime = 0;
        int totalCustomersRemoved = 0;
        for (ServicePoint sp : servicePoints) {
            totalServiceTime += sp.totalServiceTime;
            totalCustomersRemoved += sp.totalCustomersRemoved;
        }
        return totalCustomersRemoved == 0 ? 0 : totalServiceTime / totalCustomersRemoved;
    }

    /**
     * Calculates the average waiting time for an array of service points.
     *
     * @param servicePoints The array of service points
     * @return The average waiting time
     */
    public static double getAverageWaitingTime(ServicePoint[] servicePoints) {
        double totalWaitingTime = 0;
        int totalCustomersRemoved = 0;
        for (ServicePoint sp : servicePoints) {
            totalWaitingTime += sp.totalWaitingTime;
            totalCustomersRemoved += sp.totalCustomersRemoved;
        }
        return totalCustomersRemoved == 0 ? 0 : totalWaitingTime / totalCustomersRemoved;
    }

    /**
     * Gets the size of the queue at the service point.
     *
     * @return The size of the queue
     */
    public int getQueueSize() {
        return queue.size();
    }

    /**
     * Adds a customer to the queue if the service point is active.
     *
     * @param a The customer to be added
     */
    public void addQueue(Customer a) {    // The first customer of the queue is always in service
        if (isActive) {
            queue.add(a);
            totalCustomersServed++;
        }
    }

    /**
     * Removes and returns the first customer from the queue.
     * Updates the service and waiting times.
     *
     * @return The removed customer, or null if the queue is empty
     */
    public Customer removeQueue() {        // Remove serviced customer
        reserved = false;
        Customer customer = queue.poll();
        if (customer != null) {
            Trace.out(Trace.Level.INFO, "ServicePoint: Customer #" + customer.getId() + " removed from the queue");
            double serviceTime = Clock.getInstance().getClock() - customer.getArrivalTime();
            double waitingTime = customer.getServiceStartTime() - customer.getArrivalTime();
            totalServiceTime += serviceTime;
            totalWaitingTime += waitingTime;
            totalCustomersRemoved++;
        }
        return customer;
    }


    /**
     * Begins service for the first customer in the queue.
     * Schedules the departure event for the customer.
     *
     * @return The customer being served, or null if the queue is empty
     */
    public Customer beginService() {
        Customer customer = queue.peek();
        if (customer != null) {
            customer.setServiceStartTime(Clock.getInstance().getClock());
            Trace.out(Trace.Level.INFO, "ServicePoint: Starting a new service for the customer #" + customer.getId());
            reserved = true;
            double serviceTime = generator.sample();
            customer.setExpectedDepartureTime(Clock.getInstance().getClock() + serviceTime);
            eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock() + serviceTime));
        }
        return customer;
    }

    /**
     * Sets the generator for service times.
     *
     * @param generator The generator to be set
     */
    public void setGenerator(ContinuousGenerator generator) {
        this.generator = generator;
    }

    /**
     * Peeks at the first customer in the queue without removing them.
     *
     * @return The first customer in the queue, or null if the queue is empty
     */
    public Customer peekQueue() {
        return queue.peek();
    }

    /**
     * Checks if the service point is reserved.
     *
     * @return True if the service point is reserved, false otherwise
     */
    public boolean isReserved() {
        return reserved;
    }

    /**
     * Checks if there are customers in the queue.
     *
     * @return True if the queue is not empty, false otherwise
     */
    public boolean isOnQueue() {
        return queue.size() != 0;
    }

    /**
     * Checks if the service point is active.
     *
     * @return True if the service point is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active status of the service point.
     *
     * @param active The active status to be set
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }

    /**
     * Gets the average service time for this service point.
     *
     * @return The average service time
     */
    public double getAverageServiceTime() {
        return totalCustomersRemoved == 0 ? 0 : totalServiceTime / totalCustomersRemoved;
    }

    /**
     * Gets the average waiting time for this service point.
     *
     * @return The average waiting time
     */
    public double getAverageWaitingTime() {
        return totalCustomersRemoved == 0 ? 0 : totalWaitingTime / totalCustomersRemoved;
    }

    /**
     * Gets the total number of customers removed from the queue.
     *
     * @return The total number of customers removed
     */
    public int getTotalCustomersRemoved() {
        return totalCustomersRemoved;
    }

    /**
     * Resets the service point to its initial state.
     */
    public void reset() {
        // Reset all relevant variables to their initial state
        this.queue.clear();
        this.totalCustomersRemoved = 0;
        this.totalServiceTime = 0.0;
        this.totalWaitingTime = 0.0;
        this.reserved = false;
        this.isActive = true; // Assuming the service point is active by default
    }

}