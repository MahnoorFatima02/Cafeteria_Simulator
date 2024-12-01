package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.*;
import eduni.distributions.Negexp;

import java.util.Random;

public class MyEngine extends Engine {
    public static final boolean TEXTDEMO = true;
    Customer customer;
    private double simulationTime;
    private double delayTime;
    private ArrivalProcess arrivalProcess;
    private ServicePoint veganFoodStation;
    private ServicePoint[] nonVeganFoodStation;
    private ServicePoint[] cashierServicePoints;
    private ServicePoint selfCheckoutServicePoint;


    // Flags to indicate if adjustments are needed and their direction
    private Boolean adjustStudentArrivalFlag = null;
    private Boolean adjustFoodLineServiceSpeedFlag = null;
    private Boolean adjustCashierServiceSpeedFlag = null;
    private Boolean adjustStimulationSpeedFlag = null;


    // TODO: Average service time should include customer remove from list or total served?
    // TODO: put variables in database
    // TODO: where to put "this" and where not?


    public MyEngine() {
        nonVeganFoodStation = new ServicePoint[2];
        cashierServicePoints = new ServicePoint[2];

        if (TEXTDEMO) {
            Random r = new Random();

            ContinuousGenerator arrivalTime = null;
            ContinuousGenerator veganFoodServiceTime = null;
            ContinuousGenerator nonVeganFoodServiceTime = null;
            ContinuousGenerator cashierServiceTime = null;
            ContinuousGenerator selfCheckoutServiceTime = null;

            arrivalTime = new Negexp(SimulationConstants.ARRIVAL_MEAN, Integer.toUnsignedLong(r.nextInt()));

            veganFoodServiceTime = new Normal(SimulationConstants.MEAN_VEGAN_SERVICE, SimulationConstants.STD_DEV_VEGAN_SERVICE, Integer.toUnsignedLong(r.nextInt()));
            nonVeganFoodServiceTime = new Normal(SimulationConstants.MEAN_NON_VEGAN_SERVICE, SimulationConstants.STD_DEV_NON_VEGAN_SERVICE, Integer.toUnsignedLong(r.nextInt()));
            cashierServiceTime = new Normal(SimulationConstants.MEAN_CASHIER, SimulationConstants.STD_DEV_CASHIER, Integer.toUnsignedLong(r.nextInt()));
            selfCheckoutServiceTime = new Normal(SimulationConstants.MEAN_SELF_CHECKOUT, SimulationConstants.STD_DEV_SELF_CHECKOUT, Integer.toUnsignedLong(r.nextInt()));

            // vegan food service point
            veganFoodStation = new ServicePoint(veganFoodServiceTime, eventList, EventType.DEP1);

            // nonVeganServicePoints = new ServicePoint[2];
            nonVeganFoodStation[0] = new ServicePoint(nonVeganFoodServiceTime, eventList, EventType.DEP2);
            nonVeganFoodStation[1] = new ServicePoint(nonVeganFoodServiceTime, eventList, EventType.DEP2);

            // cashier service point
            cashierServicePoints[0] = new ServicePoint(cashierServiceTime, eventList, EventType.DEP3);
            cashierServicePoints[1] = new ServicePoint(cashierServiceTime, eventList, EventType.DEP3, false);

            // self-service cashier service point
            selfCheckoutServicePoint = new ServicePoint(selfCheckoutServiceTime, eventList, EventType.DEP4);


            arrivalProcess = new ArrivalProcess(arrivalTime, eventList, EventType.ARR1);

        }
    }

    @Override
    protected void initialize() {    // First arrival in the system
        arrivalProcess.generateNextEvent();
    }

    @Override
    protected void runEvent(Event t) {  // B phase events
        System.out.println("Simulation speed" + delayTime);
        // Implement delay
        try {
            Thread.sleep((long) (delayTime * 1000));
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


    @Override
    // It ensures that available service points continue to serve customers in their queues by
    // starting service for the next customer.
    protected void tryCEvents() {

        // for vegan service point event scheduling
        if (!veganFoodStation.isReserved() && veganFoodStation.isOnQueue()) {
            System.out.println("VEGAN FOOD POINT SERVICE STARTED: ");

            veganFoodStation.beginService();
        }

        // for non-vegan service points event scheduling
        for (ServicePoint sp : nonVeganFoodStation) {
            if (!sp.isReserved() && sp.isOnQueue()) {
                System.out.println("NON VEGAN FOOD POINT SERVICE STARTED: ");
                sp.beginService();
            }
        }

        // for cashier event scheduling
        for (ServicePoint p : cashierServicePoints) {
            if (p.isActive() && !p.isReserved() && p.isOnQueue()) {
                System.out.println("CASHIER POINT SERVICE STARTED: ");
                p.beginService();
            }
        }

        // Handle self-service checkout independently
        if (!selfCheckoutServicePoint.isReserved() && selfCheckoutServicePoint.isOnQueue()) {
            System.out.println("SELF SERVICE POINT SERVICE STARTED: ");
            selfCheckoutServicePoint.beginService(); // Start serving the next customer in the self-service line
        }
    }

    @Override
    protected void results() {
        System.out.println("Simulation ended at " + Clock.getInstance().getClock());

        // Total number of customers created
        int totalCustomersCreated = Customer.getTotalCustomers();
        System.out.println("Total customers created: " + totalCustomersCreated);


        // Total number of customers served
        int totalCustomersServed = cashierServicePoints[0].getTotalCustomersRemoved() + cashierServicePoints[1].getTotalCustomersRemoved() + selfCheckoutServicePoint.getTotalCustomersRemoved();
        System.out.println("Total customers served by Cashier 1: " + cashierServicePoints[0].getTotalCustomersRemoved());
        System.out.println("Total customers served by Cashier 2: " + cashierServicePoints[1].getTotalCustomersRemoved());
        System.out.println("Total customers served by Self CheckOut: " + selfCheckoutServicePoint.getTotalCustomersRemoved());

        System.out.println("Total customers served: " + totalCustomersServed);

        // Average efficiency
        double averageEfficiency = (double) totalCustomersServed / totalCustomersCreated * 100;
        System.out.println("Average efficiency: " + String.format("%.2f", averageEfficiency) + "%");


        // Average service times
        double avgVeganServiceTime = veganFoodStation.getAverageServiceTime();
        double avgNonVeganServiceTime = ServicePoint.getAverageServiceTime(nonVeganFoodStation);
        double avgCashierServiceTime = ServicePoint.getAverageServiceTime(cashierServicePoints);
        double avgSelfCheckoutServiceTime = selfCheckoutServicePoint.getAverageServiceTime();

        System.out.println("Average time spent at Vegan Service Point: " + String.format("%.2f", avgVeganServiceTime));
        System.out.println("Average time spent at Non-Vegan Service Points: " + String.format("%.2f", avgNonVeganServiceTime));
        System.out.println("Average time spent at Cashier Service Points: " + String.format("%.2f", avgCashierServiceTime));
        System.out.println("Average time spent at Self-Checkout: " + String.format("%.2f", avgSelfCheckoutServiceTime));

        // Average waiting times
        double avgVeganWaitingTime = veganFoodStation.getAverageWaitingTime();
        double avgNonVeganWaitingTime = ServicePoint.getAverageWaitingTime(nonVeganFoodStation);
        double avgCashierWaitingTime = ServicePoint.getAverageWaitingTime(cashierServicePoints);
        double avgSelfCheckoutWaitingTime = selfCheckoutServicePoint.getAverageWaitingTime();


        System.out.println("Average waiting time at Vegan Service Point: " + String.format("%.2f", avgVeganWaitingTime));
        System.out.println("Average waiting time at Non-Vegan Service Points: " + String.format("%.2f", avgNonVeganWaitingTime));
        System.out.println("Average waiting time at Cashier Service Points: " + String.format("%.2f", avgCashierWaitingTime));
        System.out.println("Average waiting time at Self-Checkout: " + String.format("%.2f", avgSelfCheckoutWaitingTime));

    }


    private void handleArrivalEvent() {
        boolean isVegan = Math.random() < SimulationConstants.IS_VEGAN_PROBABILITY;
        System.out.println("Vegan " + isVegan);
        customer = new Customer(isVegan);

        if (isVegan) {
            veganFoodStation.addQueue(customer);
        } else {
            // Assign to the shorter non-vegan queue
            // TODO: Make names simpler like nonVeganServicePoint1.addQueue() something like this
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


    //     Helper method to assign customer to cashier or self-service
    private void assignToCashier(Customer customer) {
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
            System.out.println("Self Service Cash Counter Activated");
        }

        // Check and update the status of the second cashier
        checkAndUpdateSecondCashierStatus();

        // Assign customers to the shortest queue between the two cashiers if the second cashier is active
        assignToShortestCashierQueue(customer);
        int updatedFirstCashierQueueSize = cashierServicePoints[0].getQueueSize();
        int updatedSecondCashierQueueSize = cashierServicePoints[1].getQueueSize();
        int updatedSelfServiceQueueSize = selfCheckoutServicePoint.getQueueSize();
        ;

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
                Customer customerToBeServed = cashierServicePoints[0].removeQueue();
                cashierServicePoints[1].addQueue(customerToBeServed);
            }

        }
    }

    private void checkAdjustments() {
        if (adjustStudentArrivalFlag != null) {
            adjustStudentArrival(adjustStudentArrivalFlag);
            adjustStudentArrivalFlag = null;
        }
        if (adjustFoodLineServiceSpeedFlag != null) {
            adjustFoodLineServiceSpeed(adjustFoodLineServiceSpeedFlag);
            adjustFoodLineServiceSpeedFlag = null;
        }
        if (adjustCashierServiceSpeedFlag != null) {
            adjustCashierServiceSpeed(adjustCashierServiceSpeedFlag);
            adjustCashierServiceSpeedFlag = null;
        }

        if (adjustStimulationSpeedFlag != null) {
            adjustStimulationSpeed(adjustStimulationSpeedFlag);
            adjustStimulationSpeedFlag = null;
        }
    }

    public void setDelayTime(double simulationTime) {
        this.delayTime = simulationTime;
    }

    public void adjustStimulationSpeed(boolean increase) {
        if (increase) {
            this.delayTime *= 1.1;
            System.out.println("I am here, delay rate is " + this.delayTime);
        } else {
            this.delayTime *= 0.9;
            System.out.println("I am here, delay rate is " + this.delayTime);

        }
    }


    public void adjustStudentArrival(boolean increase) {
        if (increase) {
            SimulationConstants.ARRIVAL_MEAN *= 1.1;
            System.out.println("I am here, arrival rate is " + SimulationConstants.ARRIVAL_MEAN);
        } else {
            SimulationConstants.ARRIVAL_MEAN *= 0.9;
            System.out.println("I am here, arrival rate is " + SimulationConstants.ARRIVAL_MEAN);

        }
    }

    public void adjustFoodLineServiceSpeed(boolean increase) {
        if (increase) {
            SimulationConstants.MEAN_VEGAN_SERVICE *= 1.1;
            SimulationConstants.MEAN_NON_VEGAN_SERVICE *= 1.1;
            System.out.println("I am here, meanVeganService is " + SimulationConstants.MEAN_VEGAN_SERVICE);


        } else {
            SimulationConstants.MEAN_VEGAN_SERVICE *= 0.9;
            SimulationConstants.MEAN_NON_VEGAN_SERVICE *= 0.9;
            System.out.println("I am here, meanVeganService is " + SimulationConstants.MEAN_VEGAN_SERVICE );


        }
    }

    public void adjustCashierServiceSpeed(boolean increase) {
        if (increase) {
            SimulationConstants.MEAN_CASHIER *= 1.1;
            SimulationConstants.MEAN_SELF_CHECKOUT *= 1.1;
            System.out.println("I am here, meanCashier is " +  SimulationConstants.MEAN_CASHIER );


        } else {
            SimulationConstants.MEAN_CASHIER  *= 0.9;
            SimulationConstants.MEAN_SELF_CHECKOUT *= 0.9;
            System.out.println("I am here, meanCashier is " +  SimulationConstants.MEAN_CASHIER );

        }
    }

    public double getArrivalMean() {
        return SimulationConstants.ARRIVAL_MEAN ;
    }

    public double getFoodLineSpeed() {
        return  SimulationConstants.MEAN_NON_VEGAN_SERVICE;
    }

    public double getCashierSpeed() {
        return SimulationConstants.MEAN_CASHIER;
    }


}



