package simu.utility;

import java.util.Map;

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

    public double getValue() {
        return value;
    }

    public static void initialize(Map<String, Double> constantsMap) {
        for (ConstantsEnum constant : ConstantsEnum.values()) {
            if (constantsMap.containsKey(constant.name())) {
                constant.value = constantsMap.get(constant.name());
            }
        }
    }
}