package nl.cwi.moalg.scratch.trivial.print_run;

import java.util.IdentityHashMap;

import nl.cwi.moalg.scratch.trivial.adapt.IFactory;
import nl.cwi.moalg.scratch.trivial.model.Machine;
import nl.cwi.moalg.scratch.trivial.model.State;
import nl.cwi.moalg.scratch.trivial.model.Trans;

public class PrintRunFactory implements IFactory<PrintRun.Machine, PrintRun.State, PrintRun.Trans> {

	static IdentityHashMap<Object, Object> memo = new IdentityHashMap<>();
	
	@SuppressWarnings("unchecked")
	static <T> T memoized(Object key, T t) {
		if (!memo.containsKey(key)) {
			memo.put(key, t);
		}
		return (T)memo.get(key);
	}
	
	@Override
	public PrintRun.Machine Machine(Machine machine) {
		return memoized(machine, new PrintRun.Machine() {
			@Override
			public PrintRun.State[] states() {
				return IFactory.adaptList2Array(machine.states, PrintRunFactory.this::State, new PrintRun.State[0]);
			}
		});
	}

	@Override
	public PrintRun.State State(State state) {
		return memoized(state, new PrintRun.State() {

			@Override
			public String name() {
				return state.name;
			}

			@Override
			public PrintRun.Trans[] trans() {
				return IFactory.adaptList2Array(state.transitions, PrintRunFactory.this::Trans, new PrintRun.Trans[0]);
			}
		});
	}

	@Override
	public PrintRun.Trans Trans(Trans trans) {
		return memoized(trans, new PrintRun.Trans() {

			@Override
			public PrintRun.State target() {
				return State(trans.to); 
			}

			@Override
			public String event() {
				return trans.event;
			}
		});
	}

}
