package nl.cwi.moalg.scratch.trivial;

import nl.cwi.moalg.scratch.trivial.base.Base;
import nl.cwi.moalg.scratch.trivial.base.Factory;
import nl.cwi.moalg.scratch.trivial.model.Machine;
import nl.cwi.moalg.scratch.trivial.model.State;
import nl.cwi.moalg.scratch.trivial.model.Trans;
import nl.cwi.moalg.scratch.trivial.print.PrintFactory;
import nl.cwi.moalg.scratch.trivial.print.PrintMachine;
import nl.cwi.moalg.scratch.trivial.print_run.PrintRun;
import nl.cwi.moalg.scratch.trivial.print_run.PrintRunFactory;
import nl.cwi.moalg.scratch.trivial.run.Run;
import nl.cwi.moalg.scratch.trivial.run.RunFactory;
import nl.cwi.moalg.scratch.trivial.run.RunMachine;
import nl.cwi.moalg.scratch.trivial.run.RunState;

public class Main {

	public static void main(String[] args) {
		Machine doors = new Machine();
		State closed = new State("closed");
		State opened = new State("opened");
		closed.transitions.add(new Trans("open", opened));
		opened.transitions.add(new Trans("close", closed));
		doors.states.add(closed);
		doors.states.add(opened);
		
		Base.Machine m1 = new Factory() { }.Machine(doors);
		PrintMachine m2 = new PrintFactory() { }.Machine(doors);
		System.out.println(m1.states());
		System.out.println(m2.print());
		
		RunMachine m3 = new RunFactory() { }.Machine(doors);
		Run.State[] current = new RunState[1];
		current[0] = m3.states()[0];
		
		m3.run("open", current);
		System.out.println(current[0]);
		m3.run("close", current);
		System.out.println(current[0]);
		m3.run("open", current);
		System.out.println(current[0]);

		
		PrintRun.Machine m4 = new PrintRunFactory() { }.Machine(doors);
		PrintRun.State[] current2 = new PrintRun.State[1];
		current2[0] = m4.states()[0];
		
		m4.run("open", current2);
		System.out.println(current2[0].print());
		
		m4.run("close", current2);
		System.out.println(current2[0].print());
		
		m4.run("open", current2);
		System.out.println(current2[0].print());

	}
}
