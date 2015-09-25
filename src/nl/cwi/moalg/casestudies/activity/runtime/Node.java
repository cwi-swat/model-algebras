package nl.cwi.moalg.casestudies.activity.runtime;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private boolean isRunning;
	
	List<Token> heldTokens = new ArrayList<Token>();
	
	public Node(boolean isRunning) {
		this.isRunning = isRunning;
  }
	
	public boolean isRunning() {
	  return isRunning;
  }

}
