package nl.cwi.moalg.scratch.trivial.run;

import nl.cwi.moalg.scratch.trivial.base.Base;

public interface Run {
	
	public interface Machine extends Base.Machine {
		@Override
		State[] states();
		
		void run(String token, State[] current);
	}
	
	public interface State extends Base.State {
		@Override
		Trans[] trans();
		
		void run(String token, State[] current);
	}
	
	public interface Trans extends Base.Trans {
		@Override
		State target();
		
		void run(String token, State[] current);
	}
	
}
