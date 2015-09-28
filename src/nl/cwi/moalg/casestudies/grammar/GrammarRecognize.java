package nl.cwi.moalg.casestudies.grammar;

import java.util.List;

public class GrammarRecognize implements GrammarModel<IRecognize, IRecognize, IRecognize, IRecognize> {
	@Override
	public IRecognize grammar(IRecognize start, List<IRecognize> rules) {
	  return start;
	}
	
	@Override
	public IRecognize rule(String name, List<IRecognize> alts) {
	  return (tokens, k) -> {
	  	for (IRecognize p: alts) {
	  		p.parse(tokens, k);
	  	}
	  };
	}
	
	@Override
	public IRecognize prod(String name, List<IRecognize> symbols) {
	  return (tokens, k) -> {
	  	IRecognize head = symbols.get(0);
			if (symbols.size() == 1) {
	  		head.parse(tokens, k);
	  	}
	  	else {
	  		List<IRecognize> tail = symbols.subList(1, symbols.size());
				head.parse(tokens, tokens2 -> prod(name, tail).parse(tokens2, k));
	  	}
	  };
	}
	
	public IRecognize sort(IRecognize rule) {
		return rule;
	}
	
	@Override
	public IRecognize lit(String value) {
	  return (tokens, k) -> {
	  	if (!tokens.isEmpty() && tokens.get(0).equals(value)) {
	  		k.accept(tokens.subList(1, tokens.size()));
	  	}
	  };
	}

	
	@Override
	public IRecognize empty() {
	  return (tokens, k) -> k.accept(tokens);
	}
}
