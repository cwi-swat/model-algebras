package nl.cwi.moalg.scratch.trivial.run;

public interface RunState extends Run.State {
	default void run(String token, Run.State[] current) {
		if (this == current[0]) {
			for (Run.Trans r: trans()) {
				r.run(token, current);
			}
		}
	}
}
