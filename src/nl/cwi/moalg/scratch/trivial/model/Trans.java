package nl.cwi.moalg.scratch.trivial.model;

public class Trans {
	public String event;
	public State to;
	public State from; // owner
	public Trans(String event, State to) { this.event = event; this.to = to; }
}
