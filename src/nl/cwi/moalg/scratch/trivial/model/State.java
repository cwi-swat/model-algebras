package nl.cwi.moalg.scratch.trivial.model;

import java.util.ArrayList;
import java.util.List;

public class State {
	private static int id = 0;
	
	private static String newName() {
		return "s" + (id++);
	}
	
	public List<Trans> transitions = new ArrayList<>();
	public String name;
	
	public State() { this(newName()); }
	
	public State(String name) { this.name = name; }
}

