package simu.utility;

public class SimulationVariables {
    public static double ARRIVAL_MEAN = 5;
//    public static final double IS_VEGAN_PROBABILITY = 0.2;
    public static double MEAN_VEGAN_SERVICE = 20;  // this should be 30
    public static double MEAN_NON_VEGAN_SERVICE = 25; // this should be 25
    public static double MEAN_CASHIER = 22;  // this should be 19
    public static double MEAN_SELF_CHECKOUT = 27; // this should be 25
    public static double DELAY_TIME = 0;
    public static int TOTAL_CUSTOMERS_SERVED;
    public static double AVERAGE_TIME_SPENT;
    public static double AVG_VEGAN_SERVICE_TIME;
    public static double AVG_NON_VEGAN_SERVICE_TIME;
    public static double AVG_CASHIER_SERVICE_TIME;
    public static double AVG_SELF_CHECKOUT_SERVICE_TIME;
    public static int VEGAN_QUEUE = 0;
    public static int NON_VEGAN_QUEUE1 = 0;
    public static int NON_VEGAN_QUEUE2 = 0;
    public static int CASHIER_QUEUE1 = 0;
    public static int CASHIER_QUEUE2 = 0;
    public static int SELF_CHECKOUT_QUEUE = 0;

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