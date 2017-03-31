package nl.cwi.moalg.scratch.trivial.adapt;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import nl.cwi.moalg.scratch.trivial.model.Machine;
import nl.cwi.moalg.scratch.trivial.model.State;
import nl.cwi.moalg.scratch.trivial.model.Trans;

// Generated
public interface IFactory<M, S, T> {

	static <T, U> T[] adaptList2Array(List<U> lst, Function<U, T> f, T[] x) {
		return lst.stream().map(f).collect(Collectors.toList()).toArray(x);
	}

	M Machine(Machine m);
	S State(State m);
	T Trans(Trans m);
}
