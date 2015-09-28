package nl.cwi.moalg.casestudies.statemachine;
import java.util.List;

import nl.cwi.moalg.annos.Def;
import nl.cwi.moalg.annos.Inv;
import nl.cwi.moalg.annos.Ref;
import nl.cwi.moalg.annos.Scopes;


public interface StatemachineModel<M, S, T> {
	@Scopes({"State"})
	M machine(String name, List<S> states);
	
	@Scopes("Transition")
	S state(@Def("State") String name, List<T> transitions, 
			@Inv(ns="Transition", field="to", many=true) List<T> in);
	
	T trans(@Def("Transition") String event,  @Ref("State") S to);
}
