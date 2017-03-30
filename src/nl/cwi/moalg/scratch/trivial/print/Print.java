package nl.cwi.moalg.scratch.trivial.print;

import nl.cwi.moalg.scratch.trivial.base.Base;

public interface Print {
	
	public interface Machine extends Base.Machine {
		@Override
		State[] states();
		
		String print();
	}
	
	public interface State extends Base.State {
		@Override
		Trans[] trans();
		
		String print();
	}
	
	public interface Trans extends Base.Trans {
		@Override
		State target();
		
		String print();
	}
	
}
