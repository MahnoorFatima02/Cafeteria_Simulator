package simu.framework;

public abstract class Engine {
    protected EventList eventList;
    private double simulationTime = 0;    // time when the simulation will be stopped
    private Clock clock;                // to simplify the code (clock.getClock() instead Clock.getInstance().getClock())


    // Flags for start,resume, stop and pause
    private volatile boolean running = false;
    private volatile boolean paused = false;
    private volatile boolean stopped = false;


    public Engine() {
        clock = Clock.getInstance();
        eventList = new EventList();

    }

    public void setSimulationTime(double time) {    // define how long we will run the simulation
        simulationTime = time;
    }


    public void run() {
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
    }

    private void runBEvents() {
        while (eventList.getNextEventTime() == clock.getClock()) {
            runEvent(eventList.remove());
        }
    }

    private double currentTime() {
        return eventList.getNextEventTime();
    }

    private boolean simulate() {
        return clock.getClock() < simulationTime;
    }

    protected abstract void runEvent(Event t);    // Defined in simu.model-package's class who is inheriting the Engine class

    protected abstract void tryCEvents();        // Defined in simu.model-package's class who is inheriting the Engine class

    protected abstract void initialize();        // Defined in simu.model-package's class who is inheriting the Engine class

    protected abstract void results();            // Defined in simu.model-package's class who is inheriting the Engine class


    public void startSimulation() {
        running = true;
        paused = false;
        stopped = false;
        run(); // Run the simulation in the current thread
    }

    public void pauseSimulation() {
        paused = true;
    }

    public void resumeSimulation() {
        paused = false;
    }

    public void stopSimulation() {
        stopped = true;
        running = false;
    }

    // Getter and Setter

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {

        this.running = running;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {

        this.paused = paused;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {

        this.stopped = stopped;
    }
}