package simu.framework;

import java.util.PriorityQueue;

public class EventList {
	private PriorityQueue<Event> eventlist;
	
	public EventList() {
		eventlist = new PriorityQueue<>();
	}
	
	public Event remove() {
		Trace.out(Trace.Level.INFO,"EventList: Removing from the event list " + eventlist.peek().getType() + " " + eventlist.peek().getTime());
		return eventlist.remove();
	}
	
	public void add(Event t) {
		Trace.out(Trace.Level.INFO,"EventList: Event generated to the event list " + t.getType() + " " + t.getTime());
		eventlist.add(t);
	}
	
	public double getNextEventTime(){
		return eventlist.peek().getTime();
	}

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
