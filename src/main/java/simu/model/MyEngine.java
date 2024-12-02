package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.*;
import eduni.distributions.Negexp;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class MyEngine extends Engine {
    Customer customer;
    public int totalCustomersServed;
    public double averageTimeSpent;
    public double avgVeganServiceTime;
    public double avgNonVeganServiceTime;
    public double avgCashierServiceTime;
    public double avgSelfCheckoutServiceTime;
    private ArrivalProcess arrivalProcess;
    private ServicePoint veganFoodStation;
    private ServicePoint[] nonVeganFoodStation;
    private ServicePoint[] cashierServicePoints;
    private ServicePoint selfCheckoutServicePoint;

    private int veganCustomerId;
    private int nonVeganCustomerId;
    private int cashierCustomerId;
    private int selfCheckoutCustomerId;

    // Flag to determine assignment method
    private boolean assignByQueueLength;


    // TODO: put variables in database


    public MyEngine() {
        nonVeganFoodStation = new ServicePoint[2];
        cashierServicePoints = new ServicePoint[2];

    /*
      ======  Random Number Generator =======
    */

        Random r = new Random();
    /*
      ======  Continuous Generators =======
    */

        ContinuousGenerator arrivalTime =  new Negexp(SimulationConstants.ARRIVAL_MEAN, Integer.toUnsignedLong(r.nextInt()));
        ContinuousGenerator veganFoodServiceTime =  new Normal(SimulationConstants.MEAN_VEGAN_SERVICE, SimulationConstants.STD_DEV_VEGAN_SERVICE, Integer.toUnsignedLong(r.nextInt()));
        ContinuousGenerator nonVeganFoodServiceTime = new Normal(SimulationConstants.MEAN_NON_VEGAN_SERVICE, SimulationConstants.STD_DEV_NON_VEGAN_SERVICE, Integer.toUnsignedLong(r.nextInt()));
        ContinuousGenerator cashierServiceTime = new Normal(SimulationConstants.MEAN_CASHIER, SimulationConstants.STD_DEV_CASHIER, Integer.toUnsignedLong(r.nextInt()));
        ContinuousGenerator selfCheckoutServiceTime =  new Normal(SimulationConstants.MEAN_SELF_CHECKOUT, SimulationConstants.STD_DEV_SELF_CHECKOUT, Integer.toUnsignedLong(r.nextInt()));

    /*
      ====== Service Points =======
    */
        veganFoodStation = new ServicePoint(veganFoodServiceTime, eventList, EventType.DEP1);
        nonVeganFoodStation[0] = new ServicePoint(nonVeganFoodServiceTime, eventList, EventType.DEP2);
        nonVeganFoodStation[1] = new ServicePoint(nonVeganFoodServiceTime, eventList, EventType.DEP2);
        cashierServicePoints[0] = new ServicePoint(cashierServiceTime, eventList, EventType.DEP3);
        cashierServicePoints[1] = new ServicePoint(cashierServiceTime, eventList, EventType.DEP3, false);
        selfCheckoutServicePoint = new ServicePoint(selfCheckoutServiceTime, eventList, EventType.DEP4);
  /*
      ====== Arrival Process =======
  */

   arrivalProcess = new ArrivalProcess(arrivalTime, eventList, EventType.ARR1);
    }

    @Override
    protected void initialize() {    // First arrival in the system
        arrivalProcess.generateNextEvent();
    }

    /*
      ====== B phase events =======
   */

    @Override
    protected void runEvent(Event t) {
        System.out.println("Simulation speed" + SimulationConstants.DELAY_TIME);
        // Implement delay
        try {
            Thread.sleep((long) (SimulationConstants.DELAY_TIME * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        switch ((EventType) t.getType()) {

            // Category 1: Vegan and Non-Vegan food service
            // Arrival at first food service point
            case ARR1:
                handleArrivalEvent();
                break;

            // Departure from vegan food service point
            case DEP1:
                handleVeganDepartureEvent();
                break;

            // Departure from non-vegan service points
            case DEP2:
                handleNonVeganDepartureEvent();
                break;

            // Removes the current customer whose service is now finished and reports the results.
            case DEP3:
                handleCashierDepartureEvent();
                break;

            // should I add another check here if (servedCustomer != null) ?
            case DEP4:
                handleSelfCheckoutDepartureEvent();
                break;

        }

        // Check for any flags
        checkAdjustments();
    }

    /*
        ====== C phase events =======
    */

    @Override
    // starting service for the next customer.
    protected void tryCEvents() {

        // for vegan service point event scheduling
        if (!veganFoodStation.isReserved() && veganFoodStation.isOnQueue()) {
            System.out.println("VEGAN FOOD POINT SERVICE STARTED: ");

            Customer customer = veganFoodStation.beginService();
            veganCustomerId = customer.getId();
        }

        // for non-vegan service points event scheduling
        for (ServicePoint sp : nonVeganFoodStation) {
            if (!sp.isReserved() && sp.isOnQueue()) {
                System.out.println("NON VEGAN FOOD POINT SERVICE STARTED: ");
                Customer customer = sp.beginService();
                nonVeganCustomerId = customer.getId();
            }
        }

        // for cashier event scheduling
        for (ServicePoint p : cashierServicePoints) {
            if (p.isActive() && !p.isReserved() && p.isOnQueue()) {
                System.out.println("CASHIER POINT SERVICE STARTED: ");
                Customer customer = p.beginService();
                cashierCustomerId = customer.getId();
            }
        }

        // Handle self-service checkout independently
        if (!selfCheckoutServicePoint.isReserved() && selfCheckoutServicePoint.isOnQueue()) {
            System.out.println("SELF SERVICE POINT SERVICE STARTED: ");
            Customer customer =  selfCheckoutServicePoint.beginService(); // Start serving the next customer in the self-service line
            selfCheckoutCustomerId = customer.getId();
        }
    }
     /*
        ====== Results =======
    */

    @Override
    protected void results() {
        System.out.println();
        System.out.println("Simulation ended at " + Clock.getInstance().getClock());
        System.out.println("Results: \n");

        // Total number of customers created
        int totalCustomersCreated = Customer.getTotalCustomers();
        System.out.println("Total customers created: " + totalCustomersCreated + "\n");


        // Total number of customers served
        totalCustomersServed = cashierServicePoints[0].getTotalCustomersRemoved() + cashierServicePoints[1].getTotalCustomersRemoved() + selfCheckoutServicePoint.getTotalCustomersRemoved();
        System.out.println("Total customers served by Cashier 1: " + cashierServicePoints[0].getTotalCustomersRemoved());
        System.out.println("Total customers served by Cashier 2: " + cashierServicePoints[1].getTotalCustomersRemoved());
        System.out.println("Total customers served by Self CheckOut: " + selfCheckoutServicePoint.getTotalCustomersRemoved() + "\n");

        System.out.println("Total customers served: " + totalCustomersServed);

        // Average efficiency
        double averageEfficiency = (double) totalCustomersServed / totalCustomersCreated * 100;
        System.out.println("Average efficiency: " + String.format("%.2f", averageEfficiency) + "%" + "\n");


        // Average service times
        avgVeganServiceTime = veganFoodStation.getAverageServiceTime();
        avgNonVeganServiceTime = ServicePoint.getAverageServiceTime(nonVeganFoodStation);
        avgCashierServiceTime = ServicePoint.getAverageServiceTime(cashierServicePoints);
        avgSelfCheckoutServiceTime = selfCheckoutServicePoint.getAverageServiceTime();
        averageTimeSpent = (avgVeganServiceTime + avgNonVeganServiceTime + avgCashierServiceTime + avgSelfCheckoutServiceTime) / totalCustomersServed;

        System.out.println("Average time spent at Vegan Service Point: " + String.format("%.2f", avgVeganServiceTime));
        System.out.println("Average time spent at Non-Vegan Service Points: " + String.format("%.2f", avgNonVeganServiceTime));
        System.out.println("Average time spent at Cashier Service Points: " + String.format("%.2f", avgCashierServiceTime));
        System.out.println("Average time spent at Self-Checkout: " + String.format("%.2f", avgSelfCheckoutServiceTime) + "\n");
        System.out.println("Average Service time at for all service including vegan, non vegan, cashier and self checkout as per al customers served:  " + averageTimeSpent);

        // Average waiting times
        double avgVeganWaitingTime = veganFoodStation.getAverageWaitingTime();
        double avgNonVeganWaitingTime = ServicePoint.getAverageWaitingTime(nonVeganFoodStation);
        double avgCashierWaitingTime = ServicePoint.getAverageWaitingTime(cashierServicePoints);
        double avgSelfCheckoutWaitingTime = selfCheckoutServicePoint.getAverageWaitingTime();


        System.out.println("Average waiting time at Vegan Service Point: " + String.format("%.2f", avgVeganWaitingTime));
        System.out.println("Average waiting time at Non-Vegan Service Points: " + String.format("%.2f", avgNonVeganWaitingTime));
        System.out.println("Average waiting time at Cashier Service Points: " + String.format("%.2f", avgCashierWaitingTime));
        System.out.println("Average waiting time at Self-Checkout: " + String.format("%.2f", avgSelfCheckoutWaitingTime) + "\n");

    }


    private void handleArrivalEvent() {
        boolean isVegan = Math.random() < SimulationConstants.IS_VEGAN_PROBABILITY;
        System.out.println("Vegan " + isVegan);
        customer = new Customer(isVegan);

        if (isVegan) {
            veganFoodStation.addQueue(customer);
        } else {
            // Assign to the shorter non-vegan queue
            if (nonVeganFoodStation[0].getQueueSize() <= nonVeganFoodStation[1].getQueueSize()) {
                nonVeganFoodStation[0].addQueue(customer);
            } else {
                nonVeganFoodStation[1].addQueue(customer);
            }
        }
        arrivalProcess.generateNextEvent();
    }

    private void handleVeganDepartureEvent() {
        customer = veganFoodStation.removeQueue();
        assignToCashier(customer);
    }


    private void handleNonVeganDepartureEvent() {
        for (ServicePoint sp : nonVeganFoodStation) {
            if (sp.isOnQueue()) {
                Customer customerToRemove = sp.peekQueue(); // Peek at the customer without removing
                if (customerToRemove != null && customerToRemove.getExpectedDepartureTime() <= Clock.getInstance().getClock()) {
                    customer = sp.removeQueue();
                    System.out.println("Customer " + customer.getId() + " removed from non-vegan service point at time: " + Clock.getInstance().getClock());
                    assignToCashier(customer);
                    break; // Process only one customer per event
                }
            }
        }
    }

    private void handleCashierDepartureEvent() {
        for (ServicePoint sp : cashierServicePoints) {
            if (sp.isActive() && sp.isOnQueue()) {
                Customer servedCustomer = sp.removeQueue();
                servedCustomer.setRemovalTime(Clock.getInstance().getClock());
                servedCustomer.reportResults();
                System.out.println("Customer " + servedCustomer.getId() + " removed: " + servedCustomer.getRemovalTime());
                break;
            }
        }
    }

    private void handleSelfCheckoutDepartureEvent() {
        if (selfCheckoutServicePoint.isOnQueue()) {
            Customer servedCustomer = selfCheckoutServicePoint.removeQueue();
            if (servedCustomer != null) {
                servedCustomer.setRemovalTime(Clock.getInstance().getClock());
                servedCustomer.reportResults();
                System.out.println("Customer " + servedCustomer.getId() + " removed: " + servedCustomer.getRemovalTime());
            }
        }
    }

     /*
         ====== Assign to Cashier or Self-Service =======
     */


    private void assignToCashier(Customer customer) {
        if (assignByQueueLength) {
            System.out.println("Assigning by queue length");
            // cashiers share same waiting queue
            int firstCashierQueueSize = cashierServicePoints[0].getQueueSize();
            System.out.println("The cashier queue before customer arrival : " + firstCashierQueueSize);

            //  compare with self service queue size
            int selfServiceQueueSize = selfCheckoutServicePoint.getQueueSize();
            System.out.println("The self service cashier queue before customer arrival: " + selfServiceQueueSize);

            // Assign customer to the appropriate queue
            if (firstCashierQueueSize <= selfServiceQueueSize) {
                cashierServicePoints[0].addQueue(customer);

            } else {
                selfCheckoutServicePoint.addQueue(customer);
                System.out.println("Customer " + customer.getId() + " assigned to Self-Service Cashier");
                System.out.println("Self Service Cash Counter Activated");
            }
        } else {
            System.out.println("Assigning by customer preference");
            boolean chooseSelfCheckOut = Math.random() < SimulationConstants.CUSTOMER_PREFERENCE;
            if (chooseSelfCheckOut) {
                selfCheckoutServicePoint.addQueue(customer);
                System.out.println("Customer " + customer.getId() + " assigned to Self-Service Cashier");
                System.out.println("Self Service Cash Counter Activated");
            } else {
                cashierServicePoints[0].addQueue(customer);
            }

        }

        // Check and update the status of the second cashier
        checkAndUpdateSecondCashierStatus();

        // Assign customers to the shortest queue between the two cashiers if the second cashier is active
        assignToShortestCashierQueue(customer);

        // Update the queue sizes
        int updatedFirstCashierQueueSize = cashierServicePoints[0].getQueueSize();
        int updatedSecondCashierQueueSize = cashierServicePoints[1].getQueueSize();
        int updatedSelfServiceQueueSize = selfCheckoutServicePoint.getQueueSize();

        System.out.println("The first cashier queue length: " + updatedFirstCashierQueueSize);
        System.out.println("The Second cashier queue length: " + updatedSecondCashierQueueSize);
        System.out.println("The self service queue length: " + updatedSelfServiceQueueSize);

    }


    private void checkAndUpdateSecondCashierStatus() {
        int cashierQueueSize = cashierServicePoints[0].getQueueSize();
        System.out.println("Current cashier queue size: " + cashierQueueSize);
        System.out.println("Second cashier active: " + cashierServicePoints[1].isActive());

        // Check if the second cashier should be active
        if (cashierQueueSize >= SimulationConstants.CASHIER_UPPER_LIMIT && !cashierServicePoints[1].isActive()) {
            cashierServicePoints[1].setActive(true);
            System.out.println("Second Cashier Activated");
        } else if (cashierQueueSize <= SimulationConstants.CASHIER_LOWER_LIMIT && cashierServicePoints[1].getQueueSize() == 0) {
            cashierServicePoints[1].setActive(false);
            System.out.println("Second Cashier Deactivated");
        }
    }

    private void assignToShortestCashierQueue(Customer customer) {
        if (cashierServicePoints[1].isActive()) {
            int firstCashierQueueSize = cashierServicePoints[0].getQueueSize();
            int secondCashierQueueSize = cashierServicePoints[1].getQueueSize();
            System.out.println("The second is active: " + secondCashierQueueSize);

            var remainingCustomers = firstCashierQueueSize - SimulationConstants.CASHIER_UPPER_LIMIT;

            for (int i = 0; i < remainingCustomers; i++) {
                System.out.println("Adding a customer in second cashier queue ");
                System.out.println("Customer " + customer.getId() + " assigned to Cashier 2");
                Customer customerToBeServed = cashierServicePoints[0].removeQueue();
                cashierServicePoints[1].addQueue(customerToBeServed);
            }

        }
    }

    /*
        ====== Adjustments =======
    */
    private void checkAdjustments() {
        SimulationConstants.ARRIVAL_MEAN *= SimulationAdjustments.adjustStudentArrival();
        System.out.println("Student Arrival value coming from adjustment " + SimulationAdjustments.adjustStudentArrival());
        System.out.println("Student Arrival rate mean " + SimulationConstants.ARRIVAL_MEAN);

        SimulationConstants.MEAN_VEGAN_SERVICE  *= SimulationAdjustments.adjustFoodLineServiceSpeed();
        SimulationConstants.MEAN_NON_VEGAN_SERVICE *= SimulationAdjustments.adjustFoodLineServiceSpeed();
        System.out.println("Mean Vegan Service is " + SimulationConstants.MEAN_VEGAN_SERVICE);

        SimulationConstants.MEAN_CASHIER *= SimulationAdjustments.adjustCashierServiceSpeed();
        SimulationConstants.MEAN_SELF_CHECKOUT *= SimulationAdjustments.adjustCashierServiceSpeed();
        System.out.println("Mean cashier is " + SimulationConstants.MEAN_CASHIER);

        SimulationConstants.DELAY_TIME *= SimulationAdjustments.adjustStimulationSpeed();
        System.out.println("Delay rate is " + SimulationConstants.DELAY_TIME);
    }

    public void setDelayTime(double simulationTime) {
        SimulationConstants.DELAY_TIME = simulationTime;
    }

    // setter method
    public void setAssignByQueueLength(boolean assignByQueueLength) {
        this.assignByQueueLength = assignByQueueLength;
    }

    // Getter method
    public boolean isAssignByQueueLength() {
        return assignByQueueLength;
    }


    public int getTotalCustomersServed() {
        return totalCustomersServed;
    }

    // Getter methods for the customer IDs
    public int getVeganCustomerId() {
        return veganCustomerId;
    }

    public int getNonVeganCustomerId() {
        return nonVeganCustomerId;
    }

    public int getCashierCustomerId() {
        return cashierCustomerId;
    }

    public int getSelfCheckoutCustomerId() {
        return selfCheckoutCustomerId;
    }

    public int getVeganQueueSize() {
        return veganFoodStation.getQueueSize();
    }

    public List<Integer> getNonVeganQueueSizes() {
        return Arrays.stream(nonVeganFoodStation)
                .map(ServicePoint::getQueueSize)
                .collect(Collectors.toList());
    }

    public List<Integer> getCashierQueueSizes() {
        return Arrays.stream(cashierServicePoints)
                .map(ServicePoint::getQueueSize)
                .collect(Collectors.toList());
    }

    public int getSelfCheckoutQueueSize() {
        return selfCheckoutServicePoint.getQueueSize();
    }

}

/*
Analysis of Results

Non-Vegan Service Points:
The average time spent (348.39) and the average waiting time (318.67) are significantly higher compared to other service points.
This indicates that customers are spending a lot of time waiting in the queue before being served.
This could be due to a high arrival rate of customers at the non-vegan service points, slower service times, or both.
It suggests that the service capacity at these points is not sufficient to handle the incoming customer load efficiently.

Cashier Service Points:
The average time spent (279.28) and the average waiting time (266.47) are also quite high.
This indicates that customers are experiencing long wait times at the cashier service points.
Similar to the non-vegan service points, this could be due to a high arrival rate of customers or slower service times.

Self-Checkout:
The average time spent (217.49) and the average waiting time (186.91) are lower than the non-vegan and cashier service points
but still relatively high. This suggests that while self-checkout is more efficient, there is still a significant wait time.

Vegan Service Point:
The average time spent (60.07) and the average waiting time (35.33) are the lowest among all service points. This indicates that
the vegan service point is handling the customer load more efficiently, with shorter wait times and faster service.

 */



