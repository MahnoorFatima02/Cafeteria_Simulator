package simu.framework;

/**
 * The {@code Clock} class is a singleton that keeps track of the simulation time.
 * It provides methods to set and get the current simulation time.
 */
public class Clock {
	private double clock;
	private static Clock instance;
	
	private Clock(){
		clock = 0;
	}
	
	public static Clock getInstance(){
		if (instance == null){
			instance = new Clock();
		}
		return instance;
	}
	
	public void setClock(double clock){
		this.clock = clock;
	}

	public double getClock(){
		return clock;
	}
}
