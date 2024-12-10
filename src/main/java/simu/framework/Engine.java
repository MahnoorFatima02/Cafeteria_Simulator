package simu.framework;

/**
 * The {@code Engine} class is an abstract class that provides the core functionality for running a simulation.
 * It manages the event list, simulation time, and the state of the simulation (running, paused, stopped).
 */
public abstract class Engine {
    protected EventList eventList;
    private double simulationTime = 0;    // time when the simulation will be stopped
    private Clock clock;                // to simplify the code (clock.getClock() instead Clock.getInstance().getClock())


    // Flags for start,resume, stop and pause
    private volatile boolean running = false;
    private volatile boolean paused = false;
    private volatile boolean stopped = false;

    /**
     * Constructs an {@code Engine} and initializes the clock and event list.
     */
    public Engine() {
        clock = Clock.getInstance();
        eventList = new EventList();

    }

    /**
     * Sets the simulation time.
     *
     * @param time the simulation time
     */
    public void setSimulationTime(double time) {    // define how long we will run the simulation
        simulationTime = time;
    }


    /**
     * Runs the simulation. It resets variables, initializes the simulation, and processes events.
     * The simulation runs until it is stopped or the simulation time is reached.
     */
    public void run() {
        resetVariables(); // Reset all simulation variables
        clock.setClock(0); // Reset the clock
        initialize(); // creating, e.g., the first event

        while (running && !stopped) {
            while (paused) {
                if (stopped) {
                    break;
                }
                try {
                    Thread.sleep(100); // Sleep for a short period to avoid busy-waiting
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!simulate()) {
                break;
            }
            Trace.out(Trace.Level.INFO, "\nA-phase: time is " + currentTime());
            clock.setClock(currentTime());

            Trace.out(Trace.Level.INFO, "\nB-phase:");
            runBEvents();

            Trace.out(Trace.Level.INFO, "\nC-phase:");
            tryCEvents();
        }

        results();
        eventList.clear(); // Clear the event list
    }

    /**
     * Runs the B-phase events.
     */
    private void runBEvents() {
        while (eventList.getNextEventTime() == clock.getClock()) {
            runEvent(eventList.remove());
        }
    }

    /**
     * Returns the current simulation time.
     *
     * @return the current simulation time
     */
    private double currentTime() {
        return eventList.getNextEventTime();
    }

    /**
     * Checks if the simulation should continue running.
     *
     * @return {@code true} if the simulation should continue, {@code false} otherwise
     */
    private boolean simulate() {
        return clock.getClock() < simulationTime;
    }

    /**
     * Runs the specified event.
     *
     * @param t the event to run
     */
    protected abstract void runEvent(Event t);    // Defined in simu.model-package's class who is inheriting the Engine class

    /**
     * Tries to run C-phase events.
     */
    protected abstract void tryCEvents();        // Defined in simu.model-package's class who is inheriting the Engine class

    /**
     * Initializes the simulation.
     */
    protected abstract void initialize();        // Defined in simu.model-package's class who is inheriting the Engine class

    /**
     * Processes the results of the simulation.
     */
    protected abstract void results();            // Defined in simu.model-package's class who is inheriting the Engine class

    /**
     * Starts the simulation.
     */
    public void startSimulation() {
        running = true;
        paused = false;
        stopped = false;
        run(); // Run the simulation in the current thread
    }

    /**
     * Pauses the simulation.
     */
    public void pauseSimulation() {
        paused = true;
    }

    /**
     * Resumes the simulation.
     */
    public void resumeSimulation() {
        paused = false;
    }

    /**
     * Stops the simulation.
     */
    public void stopSimulation() {
        stopped = true;
        running = false;
        paused = false;
    }

    /**
     * Resets the simulation variables.
     */
    public abstract void resetVariables();

    // Getter and Setter

    /**
     * Returns whether the simulation is running.
     *
     * @return {@code true} if the simulation is running, {@code false} otherwise
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets whether the simulation is running.
     *
     * @param running {@code true} to set the simulation as running, {@code false} otherwise
     */
    public void setRunning(boolean running) {

        this.running = running;
    }

    /**
     * Returns whether the simulation is paused.
     *
     * @return {@code true} if the simulation is paused, {@code false} otherwise
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Sets whether the simulation is paused.
     *
     * @param paused {@code true} to set the simulation as paused, {@code false} otherwise
     */
    public void setPaused(boolean paused) {

        this.paused = paused;
    }

    /**
     * Returns whether the simulation is stopped.
     *
     * @return {@code true} if the simulation is stopped, {@code false} otherwise
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * Sets whether the simulation is stopped.
     *
     * @param stopped {@code true} to set the simulation as stopped, {@code false} otherwise
     */
    public void setStopped(boolean stopped) {

        this.stopped = stopped;
    }
}
