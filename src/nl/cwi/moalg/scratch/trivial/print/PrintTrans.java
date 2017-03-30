package nl.cwi.moalg.scratch.trivial.print;

public interface PrintTrans extends Print.Trans {
	default String print() {
		return event() + " => " + target().name();
	}
}
