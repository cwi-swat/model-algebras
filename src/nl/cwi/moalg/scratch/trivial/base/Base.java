package nl.cwi.moalg.scratch.trivial.base;

public interface Base {
	
	public interface Machine {
		State[] states();
	}
	
	public interface State {
		String name();
		Trans[] trans();
	}
	
	public interface Trans {
		String event();
		State target();
	}
}
