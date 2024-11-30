package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.*;
import java.util.LinkedList;

// TODO:
// Service Point functionalities & calculations (+ variables needed) and reporting to be implemented
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


	public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType type, boolean isActive){
		this.eventList = eventList;
		this.generator = generator;
		this.eventTypeScheduled = type;
		this.isActive = isActive;
	}

	public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType type) {
		this(generator, eventList, type, true); // Default active to true
	}

	public int getQueueSize() {
		return queue.size();
	}

	public void addQueue(Customer a) {	// The first customer of the queue is always in service
		if(isActive) {
			queue.add(a);
			totalCustomersServed++;
		}
	}


	public Customer removeQueue() {		// Remove serviced customer
		reserved = false;
		Customer customer = queue.poll();
		if (customer != null) {
			double serviceTime = Clock.getInstance().getClock() - customer.getArrivalTime();
			double waitingTime = customer.getServiceStartTime() - customer.getArrivalTime();
			totalServiceTime += serviceTime;
			totalWaitingTime += waitingTime;
			totalCustomersRemoved++;
		}
		return customer;
	}


	public void beginService() { // Begins a new service, customer is on the queue during the service
		Customer customer = queue.peek();
		if (customer != null) {
			customer.setServiceStartTime(Clock.getInstance().getClock());
			Trace.out(Trace.Level.INFO, "ServicePoint: Starting a new service for the customer #" + customer.getId());
			reserved = true;
			double serviceTime = generator.sample();
			eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock() + serviceTime));
//			Trace.out(Trace.Level.INFO, "ServicePoint: Service ended for the customer #" + customer.getId());
		}
	}

	public boolean isReserved(){
		return reserved;
	}

	public boolean isOnQueue(){
		return queue.size() != 0;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		this.isActive = active;
	}

	public double getAverageServiceTime() {
		return totalCustomersRemoved == 0 ? 0 : totalServiceTime / totalCustomersRemoved;
	}

	// remove repeat
	public static double getAverageServiceTime(ServicePoint[] servicePoints) {
		double totalServiceTime = 0;
		int totalCustomersRemoved = 0;
		for (ServicePoint sp : servicePoints) {
			totalServiceTime += sp.totalServiceTime;
			totalCustomersRemoved += sp.totalCustomersRemoved;
		}
		return totalCustomersRemoved == 0 ? 0 : totalServiceTime / totalCustomersRemoved;
	}


	public double getAverageWaitingTime() {
		return totalCustomersRemoved == 0 ? 0 : totalWaitingTime / totalCustomersRemoved;
	}


	public static double getAverageWaitingTime(ServicePoint[] servicePoints) {
		double totalWaitingTime = 0;
		int totalCustomersRemoved = 0;
		for (ServicePoint sp : servicePoints) {
			totalWaitingTime += sp.totalWaitingTime;
			totalCustomersRemoved += sp.totalCustomersRemoved;
		}
		return totalCustomersRemoved == 0 ? 0 : totalWaitingTime / totalCustomersRemoved;
	}

	public int getTotalCustomersRemoved(){
		return  totalCustomersRemoved;
	}




}
