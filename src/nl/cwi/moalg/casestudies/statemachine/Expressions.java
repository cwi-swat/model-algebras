package nl.cwi.moalg.casestudies.statemachine;

import nl.cwi.moalg.annos.Ref;

public interface Expressions<E> {

	
	E add(E l, E r);
	E sub(E l, E r);
	E lt(E l, E r);
	E gt(E l, E r);
	E leq(E l, E r);
	E geq(E l, E r);
	E neq(E l, E r);
	E eq(E l, E r);
	E lit(int n);
	
	E var(@Ref("Var") String x);
	
}
