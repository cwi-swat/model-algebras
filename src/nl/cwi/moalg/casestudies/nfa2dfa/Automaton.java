package nl.cwi.moalg.casestudies.nfa2dfa;

import java.util.List;

public interface Automaton<A, S, T> {
	
	A automaton(List<S> states);
	S state(String name, List<T> trans);
	
	// event = "" means epsilon
	T trans(String event, S to);
	

}
