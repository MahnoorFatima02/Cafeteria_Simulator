package simu.framework;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Negexp;

public class ArrivalProcess {
	private ContinuousGenerator generator;
	private EventList eventList;
	private IEventType type;

	public ArrivalProcess(ContinuousGenerator g, EventList tl, IEventType type) {
		this.generator = g;
		this.eventList = tl;
		this.type = type;
	}

	public void generateNextEvent() {
		System.out.println("In arrival process .. generating events");
		Event t = new Event(type, Clock.getInstance().getClock() + generator.sample());
		System.out.println("Event Type " + t.getType());
		eventList.add(t);
	}

	public void setGenerator(Negexp generator) {
		this.generator = generator;
	}
}
