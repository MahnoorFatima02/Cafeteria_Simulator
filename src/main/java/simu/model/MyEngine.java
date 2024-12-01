package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.*;
import eduni.distributions.Negexp;

import java.util.Random;

public class MyEngine extends Engine {
    private double simulationTime;
    private double delayTime;
    private double arrivalMean;
    private double meanVeganService;
    private double meanNonVeganService;
    private double meanCashier;
    private double meanSelfCheckout;
    private double isVeganProbability;
    public static final boolean TEXTDEMO = true;
    private ArrivalProcess arrivalProcess;
    private ServicePoint veganFoodStation;
    private ServicePoint[] nonVeganFoodStation;
    private ServicePoint[] cashierServicePoints;
    private ServicePoint selfCheckoutServicePoint;
    Customer customer;

    // Variables to put in database
    int cashierUpperLimit = 6;
    int cashierLowerLimit = 3;


    // Flags to indicate if adjustments are needed and their direction
    private Boolean adjustStudentArrivalFlag = null;
    private Boolean adjustFoodLineServiceSpeedFlag = null;
    private Boolean adjustCashierServiceSpeedFlag = null;
    private Boolean adjustStimulationSpeedFlag = null;



    // TODO: Average spending time for every service point including queue as well
    // TODO: Average service time should include customer remove from list or total served?
    // TODO: put variables in database
    // TODO: where to put "this" and where not?



    public MyEngine() {

        // Initialize with default values
        this.simulationTime = 1000;
        this.arrivalMean = 5;
        isVeganProbability = 0.2;


        nonVeganFoodStation = new ServicePoint[2];
        cashierServicePoints = new ServicePoint[2];
        // I am changing these because they are not making enough queue to check cashier 2 logic

        // this should be 30
        meanVeganService = 30;
        // this should be 25
        meanNonVeganService = 25;
        // this should be 19
        meanCashier = 22;
        // this should be 25
        meanSelfCheckout = 25;
        // this should be 6.5
        double stdDevVeganService = 6;
        // should be 7.62
        double stdDevNonVeganService = 7;
        double stdDevForCashier = 5;
        double stdDevForSelfCheckout = 4;



        if (TEXTDEMO) {
            Random r = new Random();

            ContinuousGenerator arrivalTime = null;
            ContinuousGenerator veganFoodServiceTime = null;
            ContinuousGenerator nonVeganFoodServiceTime = null;
            ContinuousGenerator cashierServiceTime = null;
            ContinuousGenerator selfCheckoutServiceTime = null;

            arrivalTime = new Negexp(arrivalMean, Integer.toUnsignedLong(r.nextInt()));

            veganFoodServiceTime = new Normal(meanVeganService, stdDevVeganService, Integer.toUnsignedLong(r.nextInt()));
            nonVeganFoodServiceTime = new Normal(meanNonVeganService, stdDevNonVeganService, Integer.toUnsignedLong(r.nextInt()));
            //real-world data indicates that cashiers and self-service checkouts have different service durations:
            cashierServiceTime = new Normal(meanCashier, stdDevForCashier, Integer.toUnsignedLong(r.nextInt()));
            selfCheckoutServiceTime = new Normal(meanSelfCheckout, stdDevForSelfCheckout, Integer.toUnsignedLong(r.nextInt()));
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
        System.out.println("Average efficiency: " +String.format("%.2f", averageEfficiency) + "%");


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
        boolean isVegan = Math.random() < isVeganProbability;
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
        System.out.println("Customer " + customer.getId() + " arrived: " + Clock.getInstance().getClock());
    }

    private void handleVeganDepartureEvent() {
        customer = veganFoodStation.removeQueue();
        assignToCashier(customer);
    }

    private void handleNonVeganDepartureEvent() {
        for (ServicePoint sp : nonVeganFoodStation) {
            if (!sp.isOnQueue()) continue; // Skip empty queues
            customer = sp.removeQueue();
            assignToCashier(customer);
            break; // Process only one customer per event
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
        System.out.println("The cashier queue before customer arrival : "  + firstCashierQueueSize);

        //  compare with self service queue size
        int selfServiceQueueSize = selfCheckoutServicePoint.getQueueSize();
        System.out.println("The self service cashier queue before customer arrival: "  + selfServiceQueueSize);

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
        int updatedSelfServiceQueueSize = selfCheckoutServicePoint.getQueueSize();;

        System.out.println("The first cashier queue length: " + updatedFirstCashierQueueSize);
        System.out.println("The Second cashier queue length: " + updatedSecondCashierQueueSize);
        System.out.println("The self service queue length: " + updatedSelfServiceQueueSize);


    }



    private void checkAndUpdateSecondCashierStatus() {
        int cashierQueueSize = cashierServicePoints[0].getQueueSize();
        System.out.println("Current cashier queue size: " + cashierQueueSize);
        System.out.println("Second cashier active: " + cashierServicePoints[1].isActive());


        // Check if the second cashier should be active
        if (cashierQueueSize >= cashierUpperLimit && !cashierServicePoints[1].isActive()) {
            cashierServicePoints[1].setActive(true);
            System.out.println("Second Cashier Activated");
        } else if (cashierQueueSize <= cashierLowerLimit && cashierServicePoints[1].getQueueSize() == 0) {
            cashierServicePoints[1].setActive(false);
            System.out.println("Second Cashier Deactivated");
        }
    }


    private void assignToShortestCashierQueue(Customer customer) {
        if (cashierServicePoints[1].isActive()) {
            int firstCashierQueueSize = cashierServicePoints[0].getQueueSize();
            int secondCashierQueueSize = cashierServicePoints[1].getQueueSize();
            System.out.println("The second is active: " + secondCashierQueueSize);

            var remainingCustomers = firstCashierQueueSize - cashierUpperLimit;

            for(int i=0; i < remainingCustomers; i++) {
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

        if(adjustStimulationSpeedFlag != null){
            adjustStimulationSpeed(adjustStimulationSpeedFlag);
            adjustStimulationSpeedFlag = null;
        }
    }

    public void setDelayTime(double simulationTime) {
        this.delayTime = simulationTime;
    }

    public void  adjustStimulationSpeed(boolean increase) {
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
            this.arrivalMean *= 1.1;
            System.out.println("I am here, arrival rate is " + this.arrivalMean);
        } else {
            this.arrivalMean *= 0.9;
            System.out.println("I am here, arrival rate is " + this.arrivalMean);

        }
    }

    public void adjustFoodLineServiceSpeed(boolean increase) {
        if (increase) {
            this.meanVeganService *= 1.1;
            this.meanNonVeganService *= 1.1;
            System.out.println("I am here, meanVeganService is " + this.meanVeganService);


        } else {
            this.meanVeganService *= 0.9;
            this.meanNonVeganService *= 0.9;
            System.out.println("I am here, meanVeganService is " + this.meanVeganService);


        }
    }

    public void adjustCashierServiceSpeed(boolean increase) {
        if (increase) {
            this.meanCashier *= 1.1;
            this.meanSelfCheckout *= 1.1;
            System.out.println("I am here, meanCashier is " + this.meanCashier);


        } else {
            this.meanCashier *= 0.9;
            this.meanSelfCheckout *= 0.9;
            System.out.println("I am here, meanCashier is " + this.meanCashier);

        }
    }

    public double getArrivalMean(){
        return arrivalMean;
    }

    public double getFoodLineSpeed(){
        return meanNonVeganService;
    }

    public double getCashierSpeed(){
        return meanCashier;
    }


}



