package simu.utility;

public class SimulationVariables {
    public static double ARRIVAL_MEAN = 5;
    public static double MEAN_VEGAN_SERVICE = 20;  // this should be 30
    public static double MEAN_NON_VEGAN_SERVICE = 25; // this should be 25
    public static double MEAN_CASHIER = 22;  // this should be 19
    public static double MEAN_SELF_CHECKOUT = 27; // this should be 25
    public static double DELAY_TIME = 0;

    public static int TOTAL_CUSTOMERS_ARRIVED = 0;
    public static int TOTAL_CUSTOMERS_SERVED = 0;
    public static int TOTAL_CUSTOMERS_NOT_SERVED = 0;
    public static double SERVE_EFFICIENCY = 0.0;
    public static double AVERAGE_TIME_SPENT = 0;
    public static double AVG_VEGAN_SERVICE_TIME = 0;
    public static double AVG_NON_VEGAN_SERVICE_TIME = 0;
    public static double AVG_CASHIER_SERVICE_TIME = 0;
    public static double AVG_SELF_CHECKOUT_SERVICE_TIME = 0;

    public static boolean veganQueueArrival;
    public static boolean nonVeganQueueArrival1;
    public static boolean nonVeganQueueArrival2;
    public static boolean veganQueueDeparture;
    public static boolean nonVeganQueueDeparture1;
    public static boolean nonVeganQueueDeparture2;
    public static boolean veganFoodServe;
    public static boolean nonVeganFoodServe1;
    public static boolean nonVeganFoodServe2;
    public static boolean veganDeparture;
    public static boolean nonVeganDeparture1;
    public static boolean nonVeganDeparture2;
    public static boolean cashierQueueArrival1;
    public static boolean cashierQueueArrival2;
    public static boolean selfCashierQueueArrival;
    public static boolean cashierQueueDeparture1;
    public static boolean cashierQueueDeparture2;
    public static boolean selfCashierQueueDeparture;
    public static boolean cashierArrival1;
    public static boolean cashierArrival2;
    public static boolean selfCashierArrival;
    public static boolean cashierDeparture1;
    public static boolean cashierDeparture2;
    public static boolean selfCashierDeparture;

    public static double getFoodServiceSpeed() {
        return MEAN_VEGAN_SERVICE; // or MEAN_NON_VEGAN_SERVICE
    }

    public static double getCashierServiceSpeed() {
        return MEAN_CASHIER;
    }

    public static double getArrivalSpeed() {
        return ARRIVAL_MEAN;
    }

    public static double getSimulationSpeed() {
        return DELAY_TIME;
    }

}