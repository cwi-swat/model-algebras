package nl.cwi.moalg.scratch.trivial.print;

public interface PrintMachine extends Print.Machine {
	default String print() {
		String s = "";
		for (Print.State p : states()) {
			s += p.print() + "\n";
		}
		return s;
	}
}
