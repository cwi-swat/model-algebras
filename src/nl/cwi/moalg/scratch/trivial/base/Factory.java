package nl.cwi.moalg.scratch.trivial.base;

import java.util.IdentityHashMap;

import nl.cwi.moalg.scratch.trivial.adapt.IFactory;
import nl.cwi.moalg.scratch.trivial.model.Machine;
import nl.cwi.moalg.scratch.trivial.model.State;
import nl.cwi.moalg.scratch.trivial.model.Trans;

// Generated (?)
public interface Factory extends IFactory<Base.Machine, Base.State, Base.Trans> {
	
	static IdentityHashMap<Object, Object> memo = new IdentityHashMap<>();
	
	@SuppressWarnings("unchecked")
	static <T> T memoized(Object key, T t) {
		if (!memo.containsKey(key)) {
			memo.put(key, t);
		}
		return (T)memo.get(key);
	}
	
	default Base.Machine Machine(Machine machine) {
		return memoized(machine, new Base.Machine() {
			@Override
			public Base.State[] states() {
				return IFactory.adaptList2Array(machine.states, Factory.this::State, new Base.State[0]);
			}
		});
	}
	
	default Base.State State(State state) {
		return memoized(state, new Base.State() {

			@Override
			public String name() {
				return state.name;
			}

			@Override
			public Base.Trans[] trans() {
				return IFactory.adaptList2Array(state.transitions, Factory.this::Trans, new Base.Trans[0]);
			}
		});
	}
	
	default Base.Trans Trans(Trans trans) {
		return memoized(trans, new Base.Trans() {
			
			@Override
			public Base.State target() {
				return State(trans.to); 
			}
			
			@Override
			public String event() {
				return trans.event;
			}
		});
	}
}
