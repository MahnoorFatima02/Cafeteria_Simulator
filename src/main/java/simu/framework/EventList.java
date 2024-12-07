package simu.framework;

import java.util.PriorityQueue;

/**
 * The {@code EventList} class manages a priority queue of events in the simulation.
 * It provides methods to add, remove, and retrieve events based on their scheduled time.
 */
public class EventList {
	private PriorityQueue<Event> eventlist;

	/**
	 * Constructs an {@code EventList} and initializes the priority queue.
	 */
	public EventList() {
		eventlist = new PriorityQueue<>();
	}

	/**
	 * Removes and returns the event with the earliest scheduled time from the event list.
	 *
	 * @return the event with the earliest scheduled time
	 */
	public Event remove() {
		Trace.out(Trace.Level.INFO, "EventList: Removing from the event list " + eventlist.peek().getType() + " " + eventlist.peek().getTime());
		return eventlist.remove();
	}

	/**
	 * Adds an event to the event list.
	 *
	 * @param t the event to be added
	 */
	public void add(Event t) {
		Trace.out(Trace.Level.INFO, "EventList: Event generated to the event list " + t.getType() + " " + t.getTime());
		eventlist.add(t);
	}

	/**
	 * Returns the scheduled time of the next event in the event list.
	 *
	 * @return the scheduled time of the next event
	 */
	public double getNextEventTime() {
		return eventlist.peek().getTime();
	}

	/**
	 * Clears all events from the event list.
	 */
	public void clear() {
		eventlist.clear();
	}

//	public double getNextEventTime() {
//		Event nextEvent = eventlist.peek();
//		if (nextEvent == null) {
//			// Handle the case when the queue is empty
//			return Double.MAX_VALUE; // or some other appropriate value
//		}
//		return nextEvent.getTime();
//	}
}
