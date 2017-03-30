package nl.cwi.moalg.scratch.trivial.run;


public interface RunMachine extends Run.Machine {
	default void run(String token, Run.State[] current) {
		for (Run.State r : states()) {
			r.run(token, current);
		}
	}
}
