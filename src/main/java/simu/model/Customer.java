package simu.model;

import simu.framework.*;

// TODO:
// Customer to be implemented according to the requirements of the simulation model (data!)
public class Customer {
	private double arrivalTime;
	private double removalTime;
	private int id;
	private static int i = 1;
	private static long sum = 0;
	private boolean isVegan;
	private double serviceStartTime;
	
	public Customer(boolean isVegan){
	    id = i++;
		this.isVegan = isVegan;
	    
		arrivalTime = Clock.getInstance().getClock();
		Trace.out(Trace.Level.INFO, "New customer #" + id + " arrived at  " + arrivalTime);
	}

	public boolean isVegan() {
		return isVegan;
	}

	public double getRemovalTime() {
		return removalTime;
	}

	public void setRemovalTime(double removalTime) {
		this.removalTime = removalTime;
	}

	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getId() {
		return id;
	}
	
	public void reportResults(){
		Trace.out(Trace.Level.INFO, "\nCustomer " + id + " ready! ");
		Trace.out(Trace.Level.INFO, "Customer "   + id + " arrived: " + arrivalTime);
		Trace.out(Trace.Level.INFO,"Customer "    + id + " removed: " + removalTime);
		Trace.out(Trace.Level.INFO,"Customer "    + id + " stayed: "  + (removalTime - arrivalTime));


		double timePerCustomer = removalTime - arrivalTime;
		sum += (long) timePerCustomer;
		double mean = (double) sum /id;

		System.out.println("Service time per customer " + timePerCustomer);

		System.out.println("Current mean of the customer service times " + mean);
	}

	public static int getTotalCustomers(){
		return i-1;
	}

	public double getServiceStartTime() {
		return serviceStartTime;
	}

	public void setServiceStartTime(double serviceStartTime) {
		this.serviceStartTime = serviceStartTime;
	}
}
