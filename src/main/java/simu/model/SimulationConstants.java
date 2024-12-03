package simu.model;

public class SimulationConstants {
    public static double ARRIVAL_MEAN = 5;
    public static final double IS_VEGAN_PROBABILITY = 0.2;
    public static double MEAN_VEGAN_SERVICE = 20;  // this should be 30
    public static double MEAN_NON_VEGAN_SERVICE = 25; // this should be 25
    public static double MEAN_CASHIER = 22;  // this should be 19
    public static double MEAN_SELF_CHECKOUT = 27; // this should be 25
    public static final double STD_DEV_VEGAN_SERVICE = 5;
    public static final double STD_DEV_NON_VEGAN_SERVICE = 5;
    public static final double STD_DEV_CASHIER = 5;
    public static final double STD_DEV_SELF_CHECKOUT = 5;
    public static final double CASHIER_UPPER_LIMIT = 6;
    public static final double  CASHIER_LOWER_LIMIT = 4;
    public static final double  CUSTOMER_PREFERENCE = 0.3;
    public static final double  SIMULATION_UPPER_SPEED = 1.1;
    public static final double  SIMULATION_LOWER_SPEED = 0.9;
    public static double DELAY_TIME = 0;
    public static int TOTAL_CUSTOMERS_SERVED;
    public static double AVERAGE_TIME_SPENT;
    public static double AVG_VEGAN_SERVICE_TIME;
    public static double AVG_NON_VEGAN_SERVICE_TIME;
    public static double AVG_CASHIER_SERVICE_TIME;
    public static double AVG_SELF_CHECKOUT_SERVICE_TIME;

    public static double getFoodServiceSpeed() {
        return MEAN_VEGAN_SERVICE; // or MEAN_NON_VEGAN_SERVICE
    }

    public static double getCashierServiceSpeed() {
        return MEAN_CASHIER;
    }

    public static double getArrivalSpeed() {
        return ARRIVAL_MEAN;
    }

    public static double getSimulationSpeed(){
        return DELAY_TIME;
    }

}