package nl.cwi.moalg.casestudies.statemachine;
import java.util.List;

import nl.cwi.moalg.annos.Def;
import nl.cwi.moalg.annos.Inv;
import nl.cwi.moalg.annos.Ref;
import nl.cwi.moalg.annos.Scopes;


public interface StatemachineSyntax<M,S,T> {
	
	// within a scope, definitions within a namespace
	// are assumed to be unique. 
	// The only error we generate is unresolved reference;
	// not duplicate definition etc. 
	// So scopes only affects shadowing and whether
	// references can be found (refs need to be in same scope)
	// Scopes are letrec like, so a definition of a name can occur after
	// a reference to it. 
	// Scopes extend downwards.
	
	
	@Scopes({"State"})
	M machine(String name, List<S> states);
	
	// Defines unique within scope and namspace.
	@Scopes("Transition")
	S state(@Def("State") String name, List<T> transitions,
			@Inv(ns="Transition", field="to", many=true) Void in);
	
	T trans(@Def("Transition") String event, @Ref("State") String to);
	
	
}
