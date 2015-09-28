package nl.cwi.moalg.casestudies.grammar;

import java.util.List;
import java.util.function.Consumer;

public interface IRecognize {

	void parse(List<String> tokens, Consumer<List<String>> k);
	
}
