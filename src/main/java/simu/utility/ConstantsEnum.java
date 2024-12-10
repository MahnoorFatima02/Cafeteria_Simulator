package simu.utility;

import java.util.Map;


/**
 * The {@code ConstantsEnum} enum defines various constants used in the simulation.
 * Each constant can be initialized with a value from a provided map.
 */
public enum ConstantsEnum {
    IS_VEGAN_PROBABILITY,
    STD_DEV_VEGAN_SERVICE,
    STD_DEV_NON_VEGAN_SERVICE,
    STD_DEV_CASHIER,
    STD_DEV_SELF_CHECKOUT,
    CASHIER_UPPER_LIMIT,
    CASHIER_LOWER_LIMIT,
    SIMULATION_UPPER_SPEED,
    SIMULATION_LOWER_SPEED,
    CUSTOMER_PREFERENCE;

    private double value;


    /**
     * Gets the value of the constant.
     *
     * @return The value of the constant
     */
    public double getValue() {
        return value;
    }


    /**
     * Initializes the constants with values from the provided map.
     *
     * @param constantsMap A map containing the values for the constants
     */
    public static void initialize(Map<String, Double> constantsMap) {
        for (ConstantsEnum constant : ConstantsEnum.values()) {
            if (constantsMap.containsKey(constant.name())) {
                constant.value = constantsMap.get(constant.name());
            }
        }
    }
}