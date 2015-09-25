package nl.cwi.moalg.casestudies.activity.runtime;

public class Bool extends Value {

	private boolean value;

	public Bool(boolean value) {
		this.value = value;
  }
	
	boolean getValue() {
		return value;
	}
}
