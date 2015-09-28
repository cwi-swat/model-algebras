package nl.cwi.moalg.casestudies.grammar;

import java.util.List;

import nl.cwi.moalg.annos.Def;
import nl.cwi.moalg.annos.Ref;
import nl.cwi.moalg.annos.Scopes;

public interface GrammarModel<G, R, A, S> {
	
	@Scopes({"Rule"})
	G grammar(@Ref("Rule") S start, List<R> rules);
	
	R rule(@Def("Rule") String name, List<A> alts);
	
	A prod(String name, List<S> symbols);
	
	S sort(@Ref("Rule") R rule);

	S lit(String value);
	
	S empty();

}
