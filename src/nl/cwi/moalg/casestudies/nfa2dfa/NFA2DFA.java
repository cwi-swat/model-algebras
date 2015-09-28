package nl.cwi.moalg.casestudies.nfa2dfa;

import java.util.List;
import java.util.Set;

public class NFA2DFA<A, S extends IClosure<S>, T> implements Automaton<A, S, T> {
	Automaton<A, Set<S>, T> dfa;

	@Override
  public A automaton(List<S> states) {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public S state(String name, List<T> trans) {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public T trans(String event, S to) {
	  // TODO Auto-generated method stub
	  return null;
  }
	
}
