package nl.cwi.moalg.casestudies.statemachine;

import java.util.List;

import nl.cwi.moalg.annos.Def;
import nl.cwi.moalg.annos.Ref;
import nl.cwi.moalg.annos.Scopes;

public interface StmWithConditions<M, T, S, D, E> {
	@Scopes({"Var", "State"}) 
	M machine(String name, List<D> decls, List<S> states);
	
	D decl(@Def("Var") String x, E init);
	
	T when(E cond, String event, @Ref("State") String to);
}
