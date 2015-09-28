package nl.cwi.moalg.casestudies.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class GrammarRecognizeLeftRec extends GrammarRecognize {

	private static class Memo implements IRecognize {
		private static class Entry {
			List<Consumer<List<String>>> cnts = new ArrayList<>();
			List<List<String>> results = new ArrayList<>();
			
			boolean isSubsumed(List<String> input) {
				return results.contains(input);
			}
		}

		private Map<List<String>, Entry> table = new HashMap<>();
		private IRecognize recognize; 
		
		public Memo(IRecognize r) {
			this.recognize = r;
	  }
		
		
		@Override
	  public void parse(List<String> tokens, Consumer<List<String>> k) {
			if (!table.containsKey(tokens)) {
				table.put(tokens, new Entry());
			}
			Entry entry = table.get(tokens);
			if (entry.cnts.isEmpty()) {
				entry.cnts.add(k);
				recognize.parse(tokens, tokens2 -> {
						if (!entry.isSubsumed(tokens2)) {
							entry.results.add(tokens2);
							entry.cnts.forEach(c -> c.accept(tokens2));
						}
				});
			}
			else {
				entry.cnts.add(k);
				entry.results.forEach(k);
			}
	  }
		
	}

	
	@Override
	public IRecognize rule(String name, List<IRecognize> alts) {
	  return new Memo(super.rule(name, alts));
	}
}
