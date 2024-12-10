package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.*;
import eduni.distributions.Negexp;
import simu.utility.SimulationVariables;
import simu.dao.ConstantsDao;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import simu.utility.ConstantsEnum;

/**
 * The {@code MyEngine} class represents the engine of the simulation model.
 * It handles the arrival and departure events of customers and manages the service points.
 */
public class MyEngine extends Engine {
    Customer customer;
    private ConstantsDao constantsDao;
    private int totalCustomers;
    private int totalCustomersServed;
    private int totalCustomersNotServed;
    private double serveEfficiency;
    private ArrivalProcess arrivalProcess;
    public ServicePoint veganFoodStation;
    public ServicePoint[] nonVeganFoodStation;
    public ServicePoint[] cashierServicePoints;
    public ServicePoint selfCheckoutServicePoint;
    ContinuousGenerator arrivalTime;
    ContinuousGenerator veganFoodServiceTime;
    ContinuousGenerator nonVeganFoodServiceTime;
    ContinuousGenerator cashierServiceTime;
    ContinuousGenerator selfCheckoutServiceTime;


    /*
        ====== Flags for moving balls =======
        ====== These values dont necessarily affect the simulation system,
         it only helps to move the balls, so can be public values ======
    */
    public boolean veganQueueArrival;
    public boolean nonVeganQueueArrival1;
    public boolean nonVeganQueueArrival2;
    public boolean veganQueueDeparture;
    public boolean nonVeganQueueDeparture1;
    public boolean nonVeganQueueDeparture2;
    public boolean veganFoodServe;
    public boolean nonVeganFoodServe1;
    public boolean nonVeganFoodServe2;
    public boolean veganDeparture;
    public boolean nonVeganDeparture1;
    public boolean nonVeganDeparture2;
    public boolean cashierQueueArrival1;
    public boolean cashierQueueArrival2;
    public boolean selfCashierQueueArrival;
    public boolean cashierQueueDeparture1;
    public boolean cashierQueueDeparture2;
    public boolean selfCashierQueueDeparture;
    public boolean cashierArrival1;
    public boolean cashierArrival2;
    public boolean selfCashierArrival;
    public boolean cashierDeparture1;
    public boolean cashierDeparture2;
    public boolean selfCashierDeparture;
    /*
        ====== Flags for moving balls =======
        ====== These values dont necessarily affect the simulation system,
         it only helps to move the balls, so can be public values ======
    */


    private int veganCustomerId;
    private int nonVeganCustomerId;
    private int cashierCustomerId;
    private int selfCheckoutCustomerId;

    // Flag to determine assignment method
    private boolean assignByQueueLength;


    /**
     * Service Points and random number generator with different distributions are created here.
     * We use exponential distribution for customer arrival times and normal distribution for the
     * service times.
     */

    public MyEngine() {
        this(new ConstantsDao());
    }

    public MyEngine(ConstantsDao constantsDao) {
        nonVeganFoodStation = new ServicePoint[2];
        cashierServicePoints = new ServicePoint[2];
        this.constantsDao = constantsDao;
        Map<String, Double> constants = constantsDao.loadConstants();
        ConstantsEnum.initialize(constants);

    /*
      ======  Random Number Generator =======
    */

        Random r = new Random();
    /*
      ======  Continuous Generators =======
    */

        arrivalTime =  new Negexp(SimulationVariables.ARRIVAL_MEAN, Integer.toUnsignedLong(r.nextInt()));
        veganFoodServiceTime =  new Normal(SimulationVariables.MEAN_VEGAN_SERVICE, ConstantsEnum.STD_DEV_VEGAN_SERVICE.getValue(), Integer.toUnsignedLong(r.nextInt()));
        nonVeganFoodServiceTime = new Normal(SimulationVariables.MEAN_NON_VEGAN_SERVICE, ConstantsEnum.STD_DEV_NON_VEGAN_SERVICE.getValue(), Integer.toUnsignedLong(r.nextInt()));
        cashierServiceTime = new Normal(SimulationVariables.MEAN_CASHIER, ConstantsEnum.STD_DEV_CASHIER.getValue(), Integer.toUnsignedLong(r.nextInt()));
        selfCheckoutServiceTime =  new Normal(SimulationVariables.MEAN_SELF_CHECKOUT, ConstantsEnum.STD_DEV_SELF_CHECKOUT.getValue(), Integer.toUnsignedLong(r.nextInt()));

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

    /**
     * Initializes the simulation by generating the first arrival event in the system.
     */
    @Override
    protected void initialize() {    // First arrival in the system
        arrivalProcess.generateNextEvent();
    }

    /*
      ====== B phase events =======
   */

    /**
     * Handles the events in the simulation based on their type.
     * Implements a delay to simulate real-time processing.
     *
     * @param t The event to be processed
     */
    @Override
    public void runEvent(Event t) {
        checkAndUpdateSecondCashierStatus();
        System.out.println("Simulation speed" + SimulationVariables.DELAY_TIME);
        // Implement delay
        try {
            Thread.sleep((long) (SimulationVariables.DELAY_TIME * 1000));
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

    /**
     * Starts the service for the next customer at the appropriate service points.
     * Schedules events for vegan, non-vegan, cashier, and self-checkout service points.
     */
    @Override
    // starting service for the next customer.
    public void tryCEvents() {

        // for vegan service point event scheduling
        if (!veganFoodStation.isReserved() && veganFoodStation.isOnQueue()) {
            System.out.println("VEGAN FOOD POINT SERVICE STARTED: ");

            Customer customer = veganFoodStation.beginService();
            veganCustomerId = customer.getId();
            veganFoodServe = true;
        }

        // for non-vegan service points event scheduling
        for (int i = 0; i < nonVeganFoodStation.length; i++) {
            ServicePoint sp = nonVeganFoodStation[i];
            if (!sp.isReserved() && sp.isOnQueue()) {
                System.out.println("NON VEGAN FOOD POINT SERVICE STARTED: ");
                Customer customer = sp.beginService();
                nonVeganCustomerId = customer.getId();
                if (i == 0) {
                    nonVeganFoodServe1 = true;
                } else if (i == 1) {
                    nonVeganFoodServe2 = true;
                }
            }
        }

        // for cashier event scheduling
        for (int i = 0; i < cashierServicePoints.length; i++) {
            ServicePoint p = cashierServicePoints[i];
            if (p.isActive() && !p.isReserved() && p.isOnQueue()) {
                System.out.println("CASHIER POINT SERVICE STARTED: ");
                Customer customer = p.beginService();
                cashierCustomerId = customer.getId();
                if (i == 0) {
                    cashierArrival1 = true;
                } else if (i == 1) {
                    cashierArrival2 = true;
                }
            }
        }

        // Handle self-service checkout independently
        if (!selfCheckoutServicePoint.isReserved() && selfCheckoutServicePoint.isOnQueue()) {
            System.out.println("SELF SERVICE POINT SERVICE STARTED: ");
            Customer customer =  selfCheckoutServicePoint.beginService(); // Start serving the next customer in the self-service line
            selfCheckoutCustomerId = customer.getId();
            selfCashierArrival = true;
        }

        totalCustomersServed = cashierServicePoints[0].getTotalCustomersRemoved() + cashierServicePoints[1].getTotalCustomersRemoved() + selfCheckoutServicePoint.getTotalCustomersRemoved();
        SimulationVariables.AVG_VEGAN_SERVICE_TIME  = veganFoodStation.getAverageServiceTime();
        SimulationVariables.AVG_NON_VEGAN_SERVICE_TIME = ServicePoint.getAverageServiceTime(nonVeganFoodStation);
        SimulationVariables.AVG_CASHIER_SERVICE_TIME = ServicePoint.getAverageServiceTime(cashierServicePoints);
        SimulationVariables.AVG_SELF_CHECKOUT_SERVICE_TIME  = selfCheckoutServicePoint.getAverageServiceTime();
        SimulationVariables.TOTAL_CUSTOMERS_SERVED = totalCustomersServed;
        totalCustomers = Customer.getTotalCustomers();
        SimulationVariables.TOTAL_CUSTOMERS_ARRIVED = totalCustomers;
        totalCustomersNotServed = totalCustomers - totalCustomersServed;
        SimulationVariables.TOTAL_CUSTOMERS_NOT_SERVED = totalCustomersNotServed;
        serveEfficiency = (totalCustomersServed / (double) totalCustomers) * 100;
        SimulationVariables.SERVE_EFFICIENCY = serveEfficiency;
        if (totalCustomersServed > 0 && (SimulationVariables.AVG_CASHIER_SERVICE_TIME != 0 || SimulationVariables.AVG_SELF_CHECKOUT_SERVICE_TIME != 0) && (SimulationVariables.AVG_VEGAN_SERVICE_TIME != 0 || SimulationVariables.AVG_NON_VEGAN_SERVICE_TIME != 0)) {
            SimulationVariables.AVERAGE_TIME_SPENT = (SimulationVariables.AVG_VEGAN_SERVICE_TIME  + SimulationVariables.AVG_NON_VEGAN_SERVICE_TIME) / 2 + (SimulationVariables.AVG_CASHIER_SERVICE_TIME  + SimulationVariables.AVG_SELF_CHECKOUT_SERVICE_TIME ) / 2;
        }
        else{
            SimulationVariables.AVERAGE_TIME_SPENT = 0.0;
        }
    }
     /*
        ====== Results =======
    */

    /**
     * Prints the results of the simulation to the console, including total customers created, served, and average service times.
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
        SimulationVariables.AVG_VEGAN_SERVICE_TIME = veganFoodStation.getAverageServiceTime();
        SimulationVariables.AVG_NON_VEGAN_SERVICE_TIME = ServicePoint.getAverageServiceTime(nonVeganFoodStation);
        SimulationVariables.AVG_CASHIER_SERVICE_TIME = ServicePoint.getAverageServiceTime(cashierServicePoints);
        SimulationVariables.AVG_SELF_CHECKOUT_SERVICE_TIME = selfCheckoutServicePoint.getAverageServiceTime();
        if (totalCustomersServed > 0 && (SimulationVariables.AVG_CASHIER_SERVICE_TIME != 0 || SimulationVariables.AVG_SELF_CHECKOUT_SERVICE_TIME != 0) && (SimulationVariables.AVG_VEGAN_SERVICE_TIME != 0 || SimulationVariables.AVG_NON_VEGAN_SERVICE_TIME != 0)) {
            SimulationVariables.AVERAGE_TIME_SPENT = (SimulationVariables.AVG_VEGAN_SERVICE_TIME + SimulationVariables.AVG_NON_VEGAN_SERVICE_TIME)/2 + (SimulationVariables.AVG_CASHIER_SERVICE_TIME + SimulationVariables.AVG_SELF_CHECKOUT_SERVICE_TIME)/2;}
        else {
            SimulationVariables.AVERAGE_TIME_SPENT = 0.0;
        }

        System.out.println("Average time spent at Vegan Service Point: " + String.format("%.2f", SimulationVariables.AVG_VEGAN_SERVICE_TIME));
        System.out.println("Average time spent at Non-Vegan Service Points: " + String.format("%.2f", SimulationVariables.AVG_NON_VEGAN_SERVICE_TIME));
        System.out.println("Average time spent at Cashier Service Points: " + String.format("%.2f", SimulationVariables.AVG_CASHIER_SERVICE_TIME));
        System.out.println("Average time spent at Self-Checkout: " + String.format("%.2f", SimulationVariables.AVG_SELF_CHECKOUT_SERVICE_TIME) + "\n");
        System.out.println("Average Service time at for all service including vegan, non vegan, cashier and self checkout as per al customers served:  " + SimulationVariables.AVERAGE_TIME_SPENT);

        // Actual parameter of program
        System.out.println("Actual parameter of program: ");
        System.out.println("Average time spent at Vegan Service Point: " + String.format("%.2f", SimulationVariables.MEAN_VEGAN_SERVICE));
        System.out.println("Average time spent at Non-Vegan Service Points: " + String.format("%.2f", SimulationVariables.MEAN_NON_VEGAN_SERVICE));
        System.out.println("Average time spent at Cashier Service Points: " + String.format("%.2f", SimulationVariables.MEAN_CASHIER));
        System.out.println("Average time spent at Self-Checkout: " + String.format("%.2f", SimulationVariables.MEAN_SELF_CHECKOUT) + "\n");


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


    /**
     * Handles the arrival event of a customer.
     * Determines if the customer is vegan and assigns them to the appropriate queue.
     * Generates the next arrival event.
     */
    private void handleArrivalEvent() {
//        boolean isVegan = Math.random() < SimulationVariables.IS_VEGAN_PROBABILITY;
        boolean isVegan = Math.random() < ConstantsEnum.IS_VEGAN_PROBABILITY.getValue();

        System.out.println("Vegan " + isVegan);
        customer = new Customer(isVegan);

        if (isVegan) {
            veganQueueArrival = true;
            veganQueueDeparture = true;
            veganFoodStation.addQueue(customer);
        } else {
            // Assign to the shorter non-vegan queue
            if (nonVeganFoodStation[0].getQueueSize() <= nonVeganFoodStation[1].getQueueSize()) {
                nonVeganQueueArrival1 = true;
                nonVeganQueueDeparture1 = true;
                nonVeganFoodStation[0].addQueue(customer);
            } else {
                nonVeganQueueArrival2 = true;
                nonVeganQueueDeparture2 = true;
                nonVeganFoodStation[1].addQueue(customer);
            }
        }
        arrivalProcess.generateNextEvent();
    }

    /**
     * Handles the departure event of a vegan customer.
     * Removes the customer from the vegan food station queue and assigns them to the cashier.
     */
    private void handleVeganDepartureEvent() {
        customer = veganFoodStation.removeQueue();
        veganDeparture = true;
        assignToCashier(customer);
    }


    /**
     * Handles the departure event of a non-vegan customer.
     * Removes the customer from the non-vegan food station queue and assigns them to the cashier.
     */
    private void handleNonVeganDepartureEvent() {
        for (int i = 0; i < nonVeganFoodStation.length; i++) {
            ServicePoint sp = nonVeganFoodStation[i];
            if (sp.isOnQueue()) {
                Customer customerToRemove = sp.peekQueue(); // Peek at the customer without removing
                if (customerToRemove != null && customerToRemove.getExpectedDepartureTime() <= Clock.getInstance().getClock()) {
                    customer = sp.removeQueue();
                    System.out.println("Customer " + customer.getId() + " removed from non-vegan service point at time: " + Clock.getInstance().getClock());
                    assignToCashier(customer);
                    if (i == 0) {
                        nonVeganDeparture1 = true;
                    } else if (i == 1) {
                        nonVeganDeparture2 = true;
                    }
                    //break; // Process only one customer per event //This was the problem why events get skipped
                }
            }
        }
    }

    /**
     * Handles the departure event of a customer from the cashier service points.
     * Removes the customer from the cashier queue, sets their removal time, and reports the results.
     */
    private void handleCashierDepartureEvent() {
        for (int i = 0; i < cashierServicePoints.length; i++) {
            ServicePoint p = cashierServicePoints[i];
            if (p.isActive() && p.isOnQueue()) {
                System.out.println("CASHIER POINT SERVICE STARTED: ");
                Customer servedCustomer = p.removeQueue();
                servedCustomer.setRemovalTime(Clock.getInstance().getClock());
                servedCustomer.reportResults();
                if (i == 0) {
                    cashierDeparture1 = true;
                } else if (i == 1) {
                    cashierDeparture2 = true;
                }
                //break; //This was the problem that makes the dynamic cashier not work
            }
        }
    }

    /**
     * Handles the departure event of a customer from the self-checkout service point.
     * Removes the customer from the self-checkout queue, sets their removal time, and reports the results.
     */
    private void handleSelfCheckoutDepartureEvent() {
        if (selfCheckoutServicePoint.isOnQueue()) {
            Customer servedCustomer = selfCheckoutServicePoint.removeQueue();
            if (servedCustomer != null) {
                servedCustomer.setRemovalTime(Clock.getInstance().getClock());
                servedCustomer.reportResults();
                selfCashierDeparture = true;
                System.out.println("Customer " + servedCustomer.getId() + " removed: " + servedCustomer.getRemovalTime());
            }
        }
    }

     /*
         ====== Assign to Cashier or Self-Service =======
     */


    /**
     * Assigns the given customer to either the cashier or self-checkout queue based on the assignment method.
     * If assigning by queue length, the customer is assigned to the shorter queue.
     * If assigning by customer preference, the customer is assigned based on a random preference.
     * Also checks and updates the status of the second cashier and assigns customers to the shortest queue if the second cashier is active.
     *
     * @param customer The customer to be assigned
     */
    public void assignToCashier(Customer customer) {
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
                assignToCashier();
            } else {
                selfCheckoutServicePoint.addQueue(customer);
                selfCashierQueueArrival = true;
                selfCashierQueueDeparture = true;
                System.out.println("Customer " + customer.getId() + " assigned to Self-Service Cashier");
                System.out.println("Self Service Cash Counter Activated");
            }
        } else {
            System.out.println("Assigning by customer preference");
            boolean chooseSelfCheckOut = Math.random() < ConstantsEnum.CUSTOMER_PREFERENCE.getValue();
            if (chooseSelfCheckOut) {
                selfCheckoutServicePoint.addQueue(customer);
                selfCashierQueueArrival = true;
                selfCashierQueueDeparture = true;
                System.out.println("Customer " + customer.getId() + " assigned to Self-Service Cashier");
                System.out.println("Self Service Cash Counter Activated");
            } else {
                assignToCashier();
            }
        }

        // Update the queue sizes
        int updatedFirstCashierQueueSize = cashierServicePoints[0].getQueueSize();
        int updatedSecondCashierQueueSize = cashierServicePoints[1].getQueueSize();
        int updatedSelfServiceQueueSize = selfCheckoutServicePoint.getQueueSize();

        System.out.println("The first cashier queue length: " + updatedFirstCashierQueueSize);
        System.out.println("The Second cashier queue length: " + updatedSecondCashierQueueSize);
        System.out.println("The self service queue length: " + updatedSelfServiceQueueSize);
    }


    /**
     * Checks the queue size of the first cashier and updates the status of the second cashier based on predefined limits.
     * If the queue size exceeds the upper limit, the second cashier is activated.
     * If the queue size falls below the lower limit and the second cashier's queue is empty, the second cashier is deactivated.
     */
    private void checkAndUpdateSecondCashierStatus() {
        int cashierQueueSize = cashierServicePoints[0].getQueueSize();
        System.out.println("Current cashier queue size: " + cashierQueueSize);
        System.out.println("Second cashier active: " + cashierServicePoints[1].isActive());

        // Check if the second cashier should be active
        if (cashierQueueSize > ConstantsEnum.CASHIER_UPPER_LIMIT.getValue() && !cashierServicePoints[1].isActive()) {
            cashierServicePoints[1].setActive(true);
            System.out.println("Second Cashier Activated");
        } else if (cashierQueueSize < ConstantsEnum.CASHIER_LOWER_LIMIT.getValue() && cashierServicePoints[1].getQueueSize() == 0) {
            cashierServicePoints[1].setActive(false);
            System.out.println("Second Cashier Deactivated");
        }
    }

      /**
     * Assigns the given customer to the shortest queue between the two cashiers if the second cashier is active.
     *
     * @param customer The customer to be assigned
     */
    public void assignToCashier() {
        if (cashierServicePoints[1].isActive()) {
            if (cashierServicePoints[1].getQueueSize() < cashierServicePoints[0].getQueueSize()) {
                cashierServicePoints[1].addQueue(customer);
                cashierQueueArrival2 = true;
                cashierQueueDeparture2 = true;
            } else {
                cashierServicePoints[0].addQueue(customer);
                cashierQueueArrival1 = true;
                cashierQueueDeparture1 = true;
            }
        } else {
            cashierServicePoints[0].addQueue(customer);
            cashierQueueArrival1 = true;
            cashierQueueDeparture1 = true;
        }
    }

    /*
        ====== Adjustments =======
    */

    /**
     * Resets the simulation variables and service points to their initial states.
     */
    @Override
    public void resetVariables() {
        totalCustomersServed = 0;

        veganFoodStation.reset();
        for (ServicePoint sp : nonVeganFoodStation) {
            sp.reset();
        }
        for (ServicePoint sp : cashierServicePoints) {
            sp.reset();
        }
        selfCheckoutServicePoint.reset();

        SimulationVariables.TOTAL_CUSTOMERS_SERVED = 0;
        SimulationVariables.AVERAGE_TIME_SPENT = 0.0;
        SimulationVariables.AVG_VEGAN_SERVICE_TIME = 0.0;
        SimulationVariables.AVG_NON_VEGAN_SERVICE_TIME = 0.0;
        SimulationVariables.AVG_CASHIER_SERVICE_TIME = 0.0;
        SimulationVariables.AVG_SELF_CHECKOUT_SERVICE_TIME = 0.0;

        // Reset the total number of customers created
        Customer.resetTotalCustomers();
    }

    /**
     * Adjusts the simulation variables based on the simulation adjustments and reinitializes the generators.
     */
    public void checkAdjustments() {
        SimulationVariables.ARRIVAL_MEAN *= SimulationAdjustments.adjustStudentArrival();
        System.out.println("Student Arrival value coming from adjustment " + SimulationAdjustments.adjustStudentArrival());
        System.out.println("Student Arrival rate mean " + SimulationVariables.ARRIVAL_MEAN);

        double rate = SimulationAdjustments.adjustFoodLineServiceSpeed();
        SimulationVariables.MEAN_VEGAN_SERVICE  *= rate;
        SimulationVariables.MEAN_NON_VEGAN_SERVICE *= rate;
        System.out.println("Mean Vegan Service is " + SimulationVariables.MEAN_VEGAN_SERVICE);
        System.out.println("Mean Non-Vegan Service is " + SimulationVariables.MEAN_NON_VEGAN_SERVICE);

        double rate2 = SimulationAdjustments.adjustCashierServiceSpeed();
        SimulationVariables.MEAN_CASHIER *= rate2;
        SimulationVariables.MEAN_SELF_CHECKOUT *= rate2;
        System.out.println("Mean cashier is " + SimulationVariables.MEAN_CASHIER);
        System.out.println("Mean self checkout is " + SimulationVariables.MEAN_SELF_CHECKOUT);

        SimulationVariables.DELAY_TIME *= SimulationAdjustments.adjustStimulationSpeed();
        System.out.println("Delay rate is " + SimulationVariables.DELAY_TIME);

        // Reinitialize the ContinuousGenerator instances
        Random r = new Random();
        arrivalTime =  new Negexp(SimulationVariables.ARRIVAL_MEAN, Integer.toUnsignedLong(r.nextInt()));
        veganFoodServiceTime =  new Normal(SimulationVariables.MEAN_VEGAN_SERVICE, ConstantsEnum.STD_DEV_VEGAN_SERVICE.getValue(), Integer.toUnsignedLong(r.nextInt()));
        nonVeganFoodServiceTime = new Normal(SimulationVariables.MEAN_NON_VEGAN_SERVICE, ConstantsEnum.STD_DEV_NON_VEGAN_SERVICE.getValue(), Integer.toUnsignedLong(r.nextInt()));
        cashierServiceTime = new Normal(SimulationVariables.MEAN_CASHIER, ConstantsEnum.STD_DEV_CASHIER.getValue(), Integer.toUnsignedLong(r.nextInt()));
        selfCheckoutServiceTime =  new Normal(SimulationVariables.MEAN_SELF_CHECKOUT, ConstantsEnum.STD_DEV_SELF_CHECKOUT.getValue(), Integer.toUnsignedLong(r.nextInt()));

        // Set the updated generators in the respective service points and processes
        arrivalProcess.setGenerator((Negexp) arrivalTime);
        veganFoodStation.setGenerator(veganFoodServiceTime);
        for (ServicePoint sp : nonVeganFoodStation) {
            sp.setGenerator(nonVeganFoodServiceTime);
        }
        for (ServicePoint sp : cashierServicePoints) {
            sp.setGenerator(cashierServiceTime);
        }
        selfCheckoutServicePoint.setGenerator(selfCheckoutServiceTime);
    }

    /**
     * Sets the delay time for the simulation.
     *
     * @param simulationTime The delay time in seconds
     */
    public void setDelayTime(double simulationTime) {
        SimulationVariables.DELAY_TIME = simulationTime;
    }

    // setter method
    /**
     * Sets the flag to determine the assignment method for customers.
     *
     * @param assignByQueueLength True to assign customers by queue length, false to assign by customer preference
     */
    public void setAssignByQueueLength(boolean assignByQueueLength) {
        this.assignByQueueLength = assignByQueueLength;
    }

    // Getter method
    /**
     * Checks if customers are assigned by queue length.
     *
     * @return True if customers are assigned by queue length, false otherwise
     */
    public boolean isAssignByQueueLength() {
        return assignByQueueLength;
    }


    /**
     * Gets the total number of customers served.
     *
     * @return Total number of customers served
     */
    public int getTotalCustomersServed() {
        return totalCustomersServed;
    }

    // Getter methods for the customer IDs
    /**
     * Gets the ID of the last vegan customer served.
     *
     * @return ID of the last vegan customer served
     */
    public int getVeganCustomerId() {
        return veganCustomerId;
    }


    /**
     * Gets the ID of the last non-vegan customer served.
     *
     * @return ID of the last non-vegan customer served
     */
    public int getNonVeganCustomerId() {
        return nonVeganCustomerId;
    }


    /**
     * Gets the ID of the last customer served by the cashier.
     *
     * @return ID of the last customer served by the cashier
     */
    public int getCashierCustomerId() {
        return cashierCustomerId;
    }


    /**
     * Gets the ID of the last customer served by the self-checkout.
     *
     * @return ID of the last customer served by the self-checkout
     */
    public int getSelfCheckoutCustomerId() {
        return selfCheckoutCustomerId;
    }


    /**
     * Gets the size of the queue at the vegan service point.
     *
     * @return Size of the vegan service point queue
     */
    public int getVeganQueueSize() {
        return veganFoodStation.getQueueSize();
    }


    /**
     * Gets the sizes of the queues at the non-vegan service points.
     *
     * @return List of sizes of the non-vegan service point queues
     */
    public List<Integer> getNonVeganQueueSizes() {
        return Arrays.stream(nonVeganFoodStation)
                .map(ServicePoint::getQueueSize)
                .collect(Collectors.toList());
    }


    /**
     * Gets the sizes of the queues at the cashier service points.
     *
     * @return List of sizes of the cashier service point queues
     */
    public List<Integer> getCashierQueueSizes() {
        return Arrays.stream(cashierServicePoints)
                .map(ServicePoint::getQueueSize)
                .collect(Collectors.toList());
    }

    /**
     * Gets the size of the queue at the self-checkout service point.
     *
     * @return Size of the self-checkout service point queue
     */
    public int getSelfCheckoutQueueSize() {
        return selfCheckoutServicePoint.getQueueSize();
    }

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public int getTotalCustomersNotServed() {
        return totalCustomersNotServed;
    }

    public void setTotalCustomersNotServed(int totalCustomersNotServed) {
        this.totalCustomersNotServed = totalCustomersNotServed;
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


