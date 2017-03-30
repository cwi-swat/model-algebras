package nl.cwi.moalg.scratch.trivial.print;

import java.util.IdentityHashMap;

import nl.cwi.moalg.scratch.trivial.adapt.IFactory;
import nl.cwi.moalg.scratch.trivial.model.Machine;
import nl.cwi.moalg.scratch.trivial.model.State;
import nl.cwi.moalg.scratch.trivial.model.Trans;

public class PrintFactory implements IFactory<PrintMachine, PrintState, PrintTrans> {
	static IdentityHashMap<Object, Object> memo = new IdentityHashMap<>();
	
	@SuppressWarnings("unchecked")
	static <T> T memoized(Object key, T t) {
		if (!memo.containsKey(key)) {
			memo.put(key, t);
		}
		return (T)memo.get(key);
	}
	
	@Override
	public PrintMachine Machine(Machine machine) {
		return memoized(machine, new PrintMachine() {
			@Override
			public PrintState[] states() {
				return IFactory.adaptList2Array(machine.states, PrintFactory.this::State, new PrintState[0]);
			}
		});
	}

	@Override
	public PrintState State(State state) {
		return memoized(state, new PrintState() {

			@Override
			public String name() {
				return state.name;
			}

			@Override
			public PrintTrans[] trans() {
				return IFactory.adaptList2Array(state.transitions, PrintFactory.this::Trans, new PrintTrans[0]);
			}
		});
	}

	@Override
	public PrintTrans Trans(Trans trans) {
		return memoized(trans, new PrintTrans() {

			@Override
			public PrintState target() {
				return State(trans.to); 
			}

			@Override
			public String event() {
				return trans.event;
			}
		});
	}

}
