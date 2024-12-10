import eduni.distributions.ContinuousGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import simu.dao.ConstantsDao;
import simu.framework.Event;
import simu.framework.Trace;
import simu.model.*;
import simu.utility.SimulationVariables;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyEngineTest {

    @Mock
    private ConstantsDao constantsDao;

    @Mock
    private MyEngine myEngine;

    @BeforeEach
    void setUp() {
        // Initialize the trace level
        Trace.initialize(Trace.Level.INFO);

        // Mock the constants data
        Map<String, Double> constantsMap = new HashMap<>();
        constantsMap.put("IS_VEGAN_PROBABILITY", 0.2);
        constantsMap.put("STD_DEV_VEGAN_SERVICE", 5.0);
        constantsMap.put("STD_DEV_NON_VEGAN_SERVICE", 5.0);
        constantsMap.put("STD_DEV_CASHIER", 5.0);
        constantsMap.put("STD_DEV_SELF_CHECKOUT", 5.0);
        constantsMap.put("CASHIER_UPPER_LIMIT", 6.0);
        constantsMap.put("CASHIER_LOWER_LIMIT", 4.0);
        constantsMap.put("CUSTOMER_PREFERENCE", 0.3);
        constantsMap.put("SIMULATION_UPPER_SPEED", 1.1);
        constantsMap.put("SIMULATION_LOWER_SPEED", 0.9);

        when(constantsDao.loadConstants()).thenReturn(constantsMap);

        // Now create the MyEngine instance
        myEngine = new MyEngine(constantsDao);
    }

    @Test
    void testCheckAdjustments() {

        // Set initial values for the variables
        double initialArrivalMean = SimulationVariables.ARRIVAL_MEAN;
        double initialVeganServiceMean = SimulationVariables.MEAN_VEGAN_SERVICE;
        double initialNonVeganServiceMean = SimulationVariables.MEAN_NON_VEGAN_SERVICE;
        double initialCashierServiceMean = SimulationVariables.MEAN_CASHIER;
        double initialSelfCheckoutServiceMean = SimulationVariables.MEAN_SELF_CHECKOUT;

        System.out.println("Initial Arrival Mean: " + initialArrivalMean);
        System.out.println("Initial Vegan Service Mean: " + initialVeganServiceMean);
        System.out.println("Initial Non-Vegan Service Mean: " + initialNonVeganServiceMean);
        System.out.println("Initial Cashier Service Mean: " + initialCashierServiceMean);
        System.out.println("Initial Self-Checkout Service Mean: " + initialSelfCheckoutServiceMean);

        // Set adjustment flags in SimulationAdjustments
        SimulationAdjustments.setAdjustStudentArrivalFlag(true);
        SimulationAdjustments.setAdjustFoodLineServiceSpeedFlag(true);
        SimulationAdjustments.setAdjustCashierServiceSpeedFlag(true);

        // Call the checkAdjustments method
        myEngine.checkAdjustments();


        System.out.println("Adjusted Arrival Mean: " + SimulationVariables.ARRIVAL_MEAN);
        System.out.println("Adjusted Vegan Service Mean: " + SimulationVariables.MEAN_VEGAN_SERVICE);
        System.out.println("Adjusted Non-Vegan Service Mean: " + SimulationVariables.MEAN_NON_VEGAN_SERVICE);
        System.out.println("Adjusted Cashier Service Mean: " + SimulationVariables.MEAN_CASHIER);
        System.out.println("Adjusted Self-Checkout Service Mean: " + SimulationVariables.MEAN_SELF_CHECKOUT);

        // Verify the state of the queues
        assertNotNull(myEngine.getVeganQueueSize());
        assertNotNull(myEngine.getNonVeganQueueSizes());
        assertNotNull(myEngine.getCashierQueueSizes());
        assertNotNull(myEngine.getSelfCheckoutQueueSize());

        // Verify that the variables have been adjusted
        assertNotEquals(initialArrivalMean, SimulationVariables.ARRIVAL_MEAN);
        assertNotEquals(initialVeganServiceMean, SimulationVariables.MEAN_VEGAN_SERVICE);
        assertNotEquals(initialNonVeganServiceMean, SimulationVariables.MEAN_NON_VEGAN_SERVICE);
        assertNotEquals(initialCashierServiceMean, SimulationVariables.MEAN_CASHIER);
        assertNotEquals(initialSelfCheckoutServiceMean, SimulationVariables.MEAN_SELF_CHECKOUT);

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
    void testRunEvent() {
        Event arrivalEvent = new Event(EventType.ARR1, 0);
        Event departureEvent1 = new Event(EventType.DEP1, 1);
        Event departureEvent2 = new Event(EventType.DEP2, 2);

        // Run the events
        myEngine.runEvent(arrivalEvent);
        myEngine.runEvent(departureEvent1);
        myEngine.runEvent(departureEvent2);

        // Verify the state of the queues
        assertNotNull(myEngine.getVeganQueueSize());
        assertTrue(myEngine.getVeganQueueSize() >= 0);

        assertNotNull(myEngine.getNonVeganQueueSizes());
        assertTrue(myEngine.getNonVeganQueueSizes().stream().allMatch(size -> size >= 0));

        assertNotNull(myEngine.getCashierQueueSizes());
        assertNotNull(myEngine.getCashierQueueSizes());
        assertTrue(myEngine.getCashierQueueSizes().stream().allMatch(size -> size >= 0));

        assertNotNull(myEngine.getSelfCheckoutQueueSize());
        assertTrue(myEngine.getSelfCheckoutQueueSize() >= 0);
    }

    @Test
    void testTryCEvents() {
        // Create and run arrival events to populate the queues
        Event arrivalEvent1 = new Event(EventType.ARR1, 0);
        Event arrivalEvent2 = new Event(EventType.ARR1, 1);
        Event arrivalEvent3 = new Event(EventType.ARR1, 2);
        myEngine.runEvent(arrivalEvent1);
        myEngine.runEvent(arrivalEvent2);
        myEngine.runEvent(arrivalEvent3);

        // Log the state of the queues after arrivals
        System.out.println("Vegan Queue Size after arrivals: " + myEngine.getVeganQueueSize());
        System.out.println("Non-Vegan Queue Sizes after arrivals: " + myEngine.getNonVeganQueueSizes());
        System.out.println("Cashier Queue Sizes after arrivals: " + myEngine.getCashierQueueSizes());
        System.out.println("Self-Checkout Queue Size after arrivals: " + myEngine.getSelfCheckoutQueueSize());

        // Verify the state of the queues after arrivals
        assertTrue(myEngine.getVeganQueueSize() >= 0);
        assertTrue(myEngine.getNonVeganQueueSizes().stream().anyMatch(size -> size >= 0));
        assertTrue(myEngine.getCashierQueueSizes().stream().anyMatch(size -> size >= 0));
        assertTrue(myEngine.getSelfCheckoutQueueSize() >= 0);

        // Create and run departure events (C events)
        Event departureEvent1 = new Event(EventType.DEP2, 3);
        Event departureEvent2 = new Event(EventType.DEP3, 4);
        Event departureEvent3 = new Event(EventType.DEP4, 5);

        myEngine.runEvent(departureEvent1);
        myEngine.runEvent(departureEvent2);
        myEngine.runEvent(departureEvent3);

        // Process C events
        myEngine.tryCEvents();

        // Log the state of the queues after departures
        System.out.println("Vegan Queue Size after departures: " + myEngine.getVeganQueueSize());
        System.out.println("Non-Vegan Queue Sizes after departures: " + myEngine.getNonVeganQueueSizes());
        System.out.println("Cashier Queue Sizes after departures: " + myEngine.getCashierQueueSizes());
        System.out.println("Self-Checkout Queue Size after departures: " + myEngine.getSelfCheckoutQueueSize());

        // Verify the state of the queues after departures
        assertTrue(myEngine.getVeganQueueSize() >= 0);
        assertTrue(myEngine.getNonVeganQueueSizes().stream().allMatch(size -> size >= 0));
        assertTrue(myEngine.getCashierQueueSizes().stream().allMatch(size -> size >= 0));
        assertTrue(myEngine.getSelfCheckoutQueueSize() >= 0);
    }

    @Test
    void testAssignToCashier() {
        Customer customer = new Customer(false);
        myEngine.assignToCashier(customer);
        assertNotNull(myEngine.getCashierQueueSizes());
        assertNotNull(myEngine.getSelfCheckoutQueueSize());
    }


    @Test
    void testAssignToCashierWithMultipleCustomers() {
        // Create multiple customers
        Customer customer1 = new Customer(false);
        Customer customer2 = new Customer(false);
        Customer customer3 = new Customer(false);

        // Set the assignment method to queue length
        myEngine.setAssignByQueueLength(true);

        // Assign the first customer to the cashier
        myEngine.assignToCashier(customer1);
        assertEquals(1, myEngine.getCashierQueueSizes().getFirst());
        assertEquals(0, myEngine.getSelfCheckoutQueueSize());

        // Assign the second customer to self-checkout
        myEngine.assignToCashier(customer2);
        assertEquals(1, myEngine.getCashierQueueSizes().getFirst());
        assertEquals(1, myEngine.getSelfCheckoutQueueSize());

        // Assign the third customer to the cashier or self-checkout based on availability
        myEngine.assignToCashier(customer3);
        int cashierQueueSize = myEngine.getCashierQueueSizes().getFirst();
        int selfCheckoutQueueSize = myEngine.getSelfCheckoutQueueSize();
        assertTrue(cashierQueueSize == 2 || selfCheckoutQueueSize == 2);
    }


}