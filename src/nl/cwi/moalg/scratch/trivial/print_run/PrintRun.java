package nl.cwi.moalg.scratch.trivial.print_run;

import nl.cwi.moalg.scratch.trivial.print.PrintMachine;
import nl.cwi.moalg.scratch.trivial.print.PrintState;
import nl.cwi.moalg.scratch.trivial.print.PrintTrans;
import nl.cwi.moalg.scratch.trivial.run.RunMachine;
import nl.cwi.moalg.scratch.trivial.run.RunState;
import nl.cwi.moalg.scratch.trivial.run.RunTrans;

public interface PrintRun {
	public interface Machine extends PrintMachine, RunMachine {
		State[] states();
	}
	
	public interface State extends PrintState, RunState {
		@Override
		Trans[] trans();
	}
	
	public interface Trans extends PrintTrans, RunTrans {
		@Override
		State target();
	}
}
