import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simu.model.MyEngine;
import simu.utility.SimulationVariables;

import static org.junit.jupiter.api.Assertions.*;

class MyEngineTest {

    private MyEngine myEngine;

    @BeforeEach
    void setUp() {
        myEngine = new MyEngine();
    }

    @Test
    void testResetVariables() {
        myEngine.resetVariables();
        assertEquals(0, myEngine.getTotalCustomersServed());
        assertEquals(0, SimulationVariables.TOTAL_CUSTOMERS_SERVED);
        assertEquals(0.0, SimulationVariables.AVERAGE_TIME_SPENT);
        assertEquals(0.0, SimulationVariables.AVG_VEGAN_SERVICE_TIME);
        assertEquals(0.0, SimulationVariables.AVG_NON_VEGAN_SERVICE_TIME);
        assertEquals(0.0, SimulationVariables.AVG_CASHIER_SERVICE_TIME);
        assertEquals(0.0, SimulationVariables.AVG_SELF_CHECKOUT_SERVICE_TIME);
    }

    @Test
    void testCheckAdjustments() {
        myEngine.checkAdjustments();
        assertNotNull(myEngine.getVeganQueueSize());
        assertNotNull(myEngine.getNonVeganQueueSizes());
        assertNotNull(myEngine.getCashierQueueSizes());
        assertNotNull(myEngine.getSelfCheckoutQueueSize());
    }

    @Test
    void testSetDelayTime() {
        double simulationTime = 5.0;
        myEngine.setDelayTime(simulationTime);
        assertEquals(simulationTime, SimulationVariables.DELAY_TIME);
    }

    @Test
    void testSetAssignByQueueLength() {
        myEngine.setAssignByQueueLength(true);
        assertTrue(myEngine.isAssignByQueueLength());
    }

    @Test
    void testGetTotalCustomersServed() {
        assertEquals(0, myEngine.getTotalCustomersServed());
    }

    @Test
    void testGetVeganCustomerId() {
        assertEquals(0, myEngine.getVeganCustomerId());
    }

    @Test
    void testGetNonVeganCustomerId() {
        assertEquals(0, myEngine.getNonVeganCustomerId());
    }

    @Test
    void testGetCashierCustomerId() {
        assertEquals(0, myEngine.getCashierCustomerId());
    }

    @Test
    void testGetSelfCheckoutCustomerId() {
        assertEquals(0, myEngine.getSelfCheckoutCustomerId());
    }

    @Test
    void testGetVeganQueueSize() {
        assertEquals(0, myEngine.getVeganQueueSize());
    }

    @Test
    void testGetNonVeganQueueSizes() {
        assertNotNull(myEngine.getNonVeganQueueSizes());
    }

    @Test
    void testGetCashierQueueSizes() {
        assertNotNull(myEngine.getCashierQueueSizes());
    }

    @Test
    void testGetSelfCheckoutQueueSize() {
        assertEquals(0, myEngine.getSelfCheckoutQueueSize());
    }
}

//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//class MyEngineTest {
//
//    private MyEngine myEngine;
//
//    @BeforeEach
//    void setUp() {
//        myEngine = new MyEngine();
//    }
//
//    @Test
//    void testResetVariables() {
//        myEngine.resetVariables();
//        assertEquals(0, myEngine.getTotalCustomersServed());
//        assertEquals(0, SimulationVariables.TOTAL_CUSTOMERS_SERVED);
//        assertEquals(0.0, SimulationVariables.AVERAGE_TIME_SPENT);
//        assertEquals(0.0, SimulationVariables.AVG_VEGAN_SERVICE_TIME);
//        assertEquals(0.0, SimulationVariables.AVG_NON_VEGAN_SERVICE_TIME);
//        assertEquals(0.0, SimulationVariables.AVG_CASHIER_SERVICE_TIME);
//        assertEquals(0.0, SimulationVariables.AVG_SELF_CHECKOUT_SERVICE_TIME);
//    }
//
//    @Test
//    void testCheckAdjustments() {
//        myEngine.checkAdjustments();
//        assertNotNull(myEngine.getVeganQueueSize());
//        assertNotNull(myEngine.getNonVeganQueueSizes());
//        assertNotNull(myEngine.getCashierQueueSizes());
//        assertNotNull(myEngine.getSelfCheckoutQueueSize());
//    }
//
//    @Test
//    void testSetDelayTime() {
//        double simulationTime = 5.0;
//        myEngine.setDelayTime(simulationTime);
//        assertEquals(simulationTime, SimulationVariables.DELAY_TIME);
//    }
//
//    @Test
//    void testSetAssignByQueueLength() {
//        myEngine.setAssignByQueueLength(true);
//        assertTrue(myEngine.isAssignByQueueLength());
//    }
//
//    @Test
//    void testGetTotalCustomersServed() {
//        assertEquals(0, myEngine.getTotalCustomersServed());
//    }
//
//    @Test
//    void testGetVeganCustomerId() {
//        assertEquals(0, myEngine.getVeganCustomerId());
//    }
//
//    @Test
//    void testGetNonVeganCustomerId() {
//        assertEquals(0, myEngine.getNonVeganCustomerId());
//    }
//
//    @Test
//    void testGetCashierCustomerId() {
//        assertEquals(0, myEngine.getCashierCustomerId());
//    }
//
//    @Test
//    void testGetSelfCheckoutCustomerId() {
//        assertEquals(0, myEngine.getSelfCheckoutCustomerId());
//    }
//
//    @Test
//    void testGetVeganQueueSize() {
//        assertEquals(0, myEngine.getVeganQueueSize());
//    }
//
//    @Test
//    void testGetNonVeganQueueSizes() {
//        assertNotNull(myEngine.getNonVeganQueueSizes());
//    }
//
//    @Test
//    void testGetCashierQueueSizes() {
//        assertNotNull(myEngine.getCashierQueueSizes());
//    }
//
//    @Test
//    void testGetSelfCheckoutQueueSize() {
//        assertEquals(0, myEngine.getSelfCheckoutQueueSize());
//    }
//}