package nl.cwi.moalg.casestudies.statemachine;

import java.util.List;

public interface StmWithConditionsGraph<M, T, S, D, E> {
	M machine(String name, List<D> decls, List<S> states);
	
	D decl(String x, E init);
	
	T when(E cond, String event, S to);
}
