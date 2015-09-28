package nl.cwi.moalg.casestudies.nfa2dfa;

import java.util.Set;

public interface IClosure<S> {
	Set<S> closure();
}
