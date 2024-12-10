package simu.framework;

/**
 * The {@code Event} class represents an event in the simulation.
 * It implements the {@code Comparable} interface to allow events to be sorted by time.
 */
public class Event implements Comparable<Event> {
    private IEventType type;
    private double time;

    public Event(IEventType type, double time) {
        this.type = type;
        this.time = time;
    }

    public IEventType getType() {
        return type;
    }

    public void setType(IEventType type) {
        this.type = type;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public int compareTo(Event arg) {
        if (this.time < arg.time) return -1;
        else if (this.time > arg.time) return 1;
        return 0;
    }
}
