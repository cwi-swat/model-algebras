package nl.cwi.moalg.scratch.trivial.run;

public interface RunTrans extends Run.Trans {
	default void run(String token, Run.State[] current) {
		if (event().equals(token)) {
			current[0] = target();
		}
	}
}
