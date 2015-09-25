package nl.cwi.moalg.casestudies.activity.runtime;

public class ForkedToken extends Token {
	private int remainingOffersCount;
	private final Token base;
	
	public ForkedToken(Token base) {
		this.base = base;
  }
	
}
