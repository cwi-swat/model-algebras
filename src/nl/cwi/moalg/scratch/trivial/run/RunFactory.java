package nl.cwi.moalg.scratch.trivial.run;

import java.util.IdentityHashMap;

import nl.cwi.moalg.scratch.trivial.adapt.IFactory;
import nl.cwi.moalg.scratch.trivial.model.Machine;
import nl.cwi.moalg.scratch.trivial.model.State;
import nl.cwi.moalg.scratch.trivial.model.Trans;

public class RunFactory implements IFactory<RunMachine, RunState, RunTrans> {

	static IdentityHashMap<Object, Object> memo = new IdentityHashMap<>();
	
	@SuppressWarnings("unchecked")
	static <T> T memoized(Object key, T t) {
		if (!memo.containsKey(key)) {
			memo.put(key, t);
		}
		return (T)memo.get(key);
	}
	
	@Override
	public RunMachine Machine(Machine machine) {
		return memoized(machine, new RunMachine() {
			@Override
			public RunState[] states() {
				return IFactory.adaptList2Array(machine.states, RunFactory.this::State, new RunState[0]);
			}
		});
	}

	@Override
	public RunState State(State state) {
		return memoized(state, new RunState() {

			@Override
			public String name() {
				return state.name;
			}

			@Override
			public RunTrans[] trans() {
				return IFactory.adaptList2Array(state.transitions, RunFactory.this::Trans, new RunTrans[0]);
			}
		});
	}

	@Override
	public RunTrans Trans(Trans trans) {
		return memoized(trans, new RunTrans() {

			@Override
			public RunState target() {
				return State(trans.to); 
			}

			@Override
			public String event() {
				return trans.event;
			}
		});
	}

}
