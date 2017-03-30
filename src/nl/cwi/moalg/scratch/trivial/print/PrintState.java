package nl.cwi.moalg.scratch.trivial.print;

public interface PrintState extends Print.State {
	default String print() {
		String s = "state " + name() + "\n";
		for (Print.Trans p: trans()) {
			s += " " + p.print() + "\n";
		}
		return s;
	}
}
