package nl.cwi.moalg.casestudies.statemachine;
import java.util.List;

import nl.cwi.moalg.annos.Def;
import nl.cwi.moalg.annos.Inv;
import nl.cwi.moalg.annos.Ref;
import nl.cwi.moalg.annos.Scopes;


public interface StatemachineModel<M, S, T> {
	@Scopes({"State"})
	M machine(String name, List<S> states);
	S state(@Def("State") String name, List<T> transitions, List<S> in);
	T trans(String event,  @Ref("State") S to);
}
