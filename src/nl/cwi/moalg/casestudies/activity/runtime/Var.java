package nl.cwi.moalg.casestudies.activity.runtime;

public class Var {
	private Value value;

	public Var(Value value) {
		this.value = value;
	}

	public void setValue(Value value) {
	  this.value = value;
  }
	
	public Value getValue() {
	  return value;
  }
}
