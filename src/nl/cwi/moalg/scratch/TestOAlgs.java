
package nl.cwi.moalg.scratch;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/*
 * Simple boolean expressions
 * -> simple language of expressions; trees
 */

abstract class BoolExp { }

class Lit extends BoolExp {
	boolean val;
}

class And extends BoolExp {
	BoolExp lhs, rhs;
	And(BoolExp lhs, BoolExp rhs) { this.lhs = lhs; this.rhs = rhs; }
}

class Not extends BoolExp {
	BoolExp arg;
	Not(BoolExp arg) { this.arg = arg; }
}

// Generated
interface BoolExpAlg<E> {
	E And(And and);
	E Not(Not not);
	E Lit(Lit lit);
	
	default E $(BoolExp it) {
		if (it instanceof Lit) {
			return Lit((Lit)it);
		}
		if (it instanceof Not) {
			return Not((Not)it);
		}
		if (it instanceof And) {
			return And((And)it);
		}
		throw new RuntimeException("bad object");
	}
}

/*
 * Define boolean expression evaluation
 * -> simple evaluation of tree like structures.
 *    Like traditional oalg, but lazily "semanticized".
 */

interface IEval {
	boolean eval();
}

interface EvalBoolExp extends BoolExpAlg<IEval> {
	@Override
	default IEval Lit(Lit lit) {
		return () -> lit.val;
	}
	
	@Override
	default IEval And(And and) {
		return () -> $(and.lhs).eval() ? $(and.rhs).eval() : false;
	}
	
	@Override
	default IEval Not(Not not) {
		return () -> $(not.arg).eval();
	}
}


/*
 * Extension with Or
 * -> simple language extension, similar to ordinary OAlgs
 */

class Or extends BoolExp {
	BoolExp lhs, rhs;
}

// Generated
interface OrAlg<E> extends BoolExpAlg<E> {
	
	E Or(Or or);
	
	@Override 
	default E $(BoolExp it) {
		if (it instanceof Or) {
			return Or((Or)it);
		}
		return BoolExpAlg.super.$(it);
	}
}


/*
 *  Desugar Or into not+and (shows basic, generic (!) transformation)
 *  -> simple, generic transformation
 */

interface DesugarOr<E> extends OrAlg<E>, BoolExpAlg<E> {
	@Override
	default E Or(Or or) {
		return $(new Not(new And(new Not(or.lhs), new Not(or.rhs))));
	}
}

/*
 * Finite state machines
 * -> a model with cross-refs.
 */

class FSM {
	List<State> states = new ArrayList<>();
}

class State {
	private static int id = 0;
	
	private static String newName() {
		return "s" + (id++);
	}
	
	List<Trans> transitions = new ArrayList<>();
	String name;
	
	State() { this(newName()); }
	
	State(String name) { this.name = name; }
}

class Trans {
	String event;
	State to;
	State from; // owner
	Trans(String event, State to) { this.event = event; this.to = to; }
}

// generated
interface FSMAlg<M, S, T> {
	M FSM(FSM fsm);
	S State(State s);
	T Trans(Trans t);
	
	default M $(FSM it) {
		if (it instanceof FSM) {
			return FSM((FSM)it); 
		}
		throw new RuntimeException("bad object");
	}
	
	default S $(State it) {
		if (it instanceof State) {
			return State((State)it); 
		}
		throw new RuntimeException("bad object");
	}
	
	default T $(Trans it) {
		if (it instanceof Trans) {
			return Trans((Trans)it); 
		}
		throw new RuntimeException("bad object");
	}
}

/*
 * Execution of statemachines.
 * -> lazily mapping a model into semantics
 */

// semantic type
interface INext {
	State next(String event);
}

interface ExecFSM extends FSMAlg<INext, INext, INext> {
	
	static <T> State next(List<T> lst, Function<T, State> f) {
		return lst.stream().map(f).filter(s -> s != null).findFirst().orElse(null);
	}
	
	@Override
	default INext FSM(FSM fsm) {
		return ev -> next(fsm.states, s -> $(s).next(ev));
	}

	@Override
	default INext State(State s) {
		return ev -> next(s.transitions, s1 -> $(s1).next(ev));
	}

	@Override
	default INext Trans(Trans t) {
		return ev -> ev.equals(t.event) ? t.to : null;
	}
	
}

/*
 * Transform FSM to strings
 * -> mapping into a different "semantics"
 */

interface FSM2String extends FSMAlg<String, String, String> {

	@Override
	default String FSM(FSM fsm) {
		String s = "";
		for (State st: fsm.states) {
			s += $(st) + "\n";
		}
		return s;
	}

	@Override
	default String State(State s) {
		String str = s.name + ":\n";
		for (Trans t: s.transitions) {
			str += $(t);
		}
		return str;
	}

	@Override
	default String Trans(Trans t) {
		// note: don't recurse on t.to!
		return t.event + "=>" + t.to.name;
	}

		
}

/*
 * Another metamodel: Graphs
 */

class Graph {
	Set<Node> nodes;
	Set<Edge> edges;
}

class Node {
	String id;
	Set<Edge> out;
	Set<Edge> in;
	Node(String id) { this.id = id; }
}

class Edge {
	Node from, to;
	String label;
	
	Edge(Node from, Node to, String label) {
		this.from = from;
		this.to = to;
		this.label = label;
	}
}

// generated
interface GraphAlg<G, N, E> {
	G Graph(Graph g);
	N Node(Node n);
	E Edge(Edge e);
	
	default G $(Graph it) {
		return Graph(it); 
	}
	
	default N $(Node it) {
		return Node(it);
	}

	default E $(Edge it) {
		return Edge(it);
	}
}

/*
 * Transform FSM to Graph
 * -> exogenous transformation in the presence of cross-references
 */

interface FSM2Graph extends FSMAlg<Graph, Node, Edge> {
	
	Map<State, Node> memo = new HashMap<>();
	
	@Override
	default Graph FSM(FSM fsm) {
		memo.clear();
		Graph g = new Graph();
		for (State s: fsm.states) {
			Node n = $(s);
			g.nodes.add(n);
			g.edges.addAll(n.in);
			g.edges.addAll(n.out);
		}
		return g;
	}
	
	@Override
	default Node State(State s) {
		if (memo.containsKey(s)) {
			return memo.get(s);
		}
		Node n = new Node(s.name);
		memo.put(s, n);
		for (Trans t: s.transitions) {
			Edge e = $(t);
			e.from = n;
			n.out.add(e);
		}
		return n;
	}
	
	@Override
	default Edge Trans(Trans t) {
		Edge e = new Edge(null, $(t.to), t.event);
		$(t.to).in.add(e);
		return e;
	}
}

/*
 * Guarded FSMs
 * -> extending meta model with a new class, combining with another 
 *    meta model, and combining the semantics of both
 */

class Guarded extends Trans {

	Guarded(String event, State to) {
		super(event, to);
	}

	BoolExp guard;
}

// generated  (note how BoolExpAlg is included because Guarded depends on it.)
interface GuardedFSMAlg<E, M, S, T> extends BoolExpAlg<E>, FSMAlg<M, S, T> {
	
	T Guarded(Guarded guarded);
	
	@Override
	default T $(Trans it) {
		if (it instanceof Guarded) {
			return Guarded((Guarded)it);
		}
		return FSMAlg.super.$(it);
	}
}


/*
 * Extend FSM execution with guarded transitions (including exp evaluation).
 * -> modularly extend existing semantics (ExecFSM), including another existing semantics (EvalBoolExp).
 */

interface ExecGuardedFSM extends EvalBoolExp, ExecFSM, GuardedFSMAlg<IEval, INext, INext, INext> {

	@Override
	default INext Guarded(Guarded trans) {
		return ev -> ev.equals(trans.event) && $(trans.guard).eval() ? trans.to : null;
	}

}


/*
 * Extend guarded fsm eval with Or
 * -> Desugaring definitions can be simply merged in. 
 */

interface ExecGuardedFSMWithOr extends ExecGuardedFSM, DesugarOr<IEval> {
	
}

/*
 * Another (tree-like) meta model: Regular expressions 
 */

abstract class Regexp {}

class Token extends Regexp {
	String token;
}

class Seq extends Regexp {
	Regexp lhs, rhs;
}

class Alt extends Regexp {
	Regexp lhs, rhs;
}

class Star extends Regexp {
	Regexp arg;
}

// generated
interface RegexpAlg<R> {
	
	R Token(Token t);
	R Seq(Seq s);
	R Alt(Alt a);
	R Star(Star s);
	
	default R $(Regexp it) {
		if (it instanceof Token) {
			return Token((Token)it);
		}
		if (it instanceof Seq) {
			return Seq((Seq)it);
		}
		if (it instanceof Alt) {
			return Alt((Alt)it);
		}
		if (it instanceof Star) {
			return Star((Star)it);
		}
		throw new RuntimeException("bad object");
	}
}

/*
 * More transformation: Regexp to FSM
 * -> exogenous model transformation
 */

interface Regexp2FSM extends RegexpAlg<FSM> {
	
	@Override
	default FSM Token(Token t) {
		State s1 = new State();
		State s2 = new State();
		FSM m = new FSM();
		m.states.add(s1);
		m.states.add(s2);
		s1.transitions.add(new Trans(t.token, s2));
		return m;
	}
	
	@Override
	default FSM Seq(Seq s) {
		FSM m1 = $(s.lhs);
		FSM m2 = $(s.rhs);
		FSM m = new FSM();
		m1.states.get(m1.states.size() - 1).transitions.add(new Trans("", m2.states.get(0)));
		m.states.addAll(m1.states);
		m.states.addAll(m2.states);
		return m;
	}
	
	@Override
	default FSM Alt(Alt a) {
		FSM m1 = $(a.lhs);
		FSM m2 = $(a.rhs);
		FSM m = new FSM();

		State s1 = new State();
		s1.transitions.add(new Trans("", m1.states.get(0)));
		s1.transitions.add(new Trans("", m2.states.get(0)));

		State s2 = new State();
		m1.states.get(m1.states.size() - 1).transitions.add(new Trans("", s2));
		m2.states.get(m2.states.size() - 1).transitions.add(new Trans("", s2));
		
		m.states.add(s1);
		m.states.addAll(m1.states);
		m.states.addAll(m2.states);
		m.states.add(s2);
		
		return m;
	}
	
	@Override
	default FSM Star(Star star) {
		FSM m = $(star.arg);
		State s1 = new State();
		State s2 = new State();
		State s = m.states.get(0);
		State f = m.states.get(m.states.size() - 1);

		f.transitions.add(new Trans("", s));
		s1.transitions.add(new Trans("", s));
		f.transitions.add(new Trans("", s2));
		s1.transitions.add(new Trans("", s2));
		
		FSM m2 = new FSM();
		m2.states.add(s1);
		m2.states.addAll(m.states);
		m2.states.add(s2);
		return m2;
	}
}

/*
 * Regexps2graphs via regexp2fsm and FSM to graph
 * NB: no deforestation (intermediate models *are* created).
 * -> composing transformations.
 */

class Regexp2Graph {
	static final Regexp2FSM regexp2fsm = new Regexp2FSM() {};
	static final FSM2Graph fsm2graph = new FSM2Graph() {};
	
	public static Graph regexp2graph(Regexp it) {
		return fsm2graph.$(regexp2fsm.$(it));
	}
}

/*
 * Even more: parallel merge of two state machines
 * -> endogenous model transformation showing operations with multiple model arguments
 * [This one's a little wacky...]
 */

interface MergeFSM {
	FSM merge(FSM m2);
}

interface MergeState {
	State merge(State s2);
}

interface MergeTrans {
	List<Trans> merge(State s1, State s2, Trans t2);
}

class MergeFSMs implements FSMAlg<MergeFSM, MergeState, MergeTrans> {

	Map<String, State> memo = new HashMap<>();
	
	@Override
	public MergeFSM FSM(FSM fsm1) {
		return fsm2 -> {
			memo.clear();
			FSM fsm = new FSM();
			for (State s1: fsm1.states) {
				for (State s2: fsm2.states) {
					fsm.states.add($(s1).merge(s2));
				}
			}
			return fsm;
		};
	}

	@Override
	public MergeState State(State s1) {
		return s2 -> {
			String nn = s1.name + "_" + s2.name;
			if (memo.containsKey(nn)) {
				return memo.get(nn);
			}
			State s = new State(nn);
			memo.put(nn, s);
			
			for (Trans t1: s1.transitions) {
				for (Trans t2: s2.transitions) {
					s.transitions.addAll($(t1).merge(s1, s2, t2));
				}
			}
			return s;
		};
	}

	@Override
	public MergeTrans Trans(Trans t1) {
		return (s1, s2, t2) -> {
			List<Trans> ts = new ArrayList<>();
			if (t1.event.equals(t2.event)) {
				ts.add(new Trans(t1.event, $(t1.to).merge(t2.to)));
			}
			else {
				ts.add(new Trans(t1.event, $(t1.to).merge(s2)));
				ts.add(new Trans(t2.event, $(s1).merge(t2.to)));
			}
			return ts;
		};
	}
	
}

/*
 * Trying to provide memoization as a service.
 * -> this could be generated as part of an algebra interface
 * Unfortunately, this breaks when interfaces are merged (memo becomes duplicate).
 */

interface MemoFSMAlg<M, S, T> {
	M FSM(FSM fsm);
	S State(State s);
	T Trans(Trans t);
	
	IdentityHashMap<Object,Object> memo = new IdentityHashMap<>();
	
	@SuppressWarnings("unchecked")
	default <Syn, Sem> Sem memoizing(Syn arg, Sem init, Function<Sem, Sem> f) {
		if (!memo.containsKey(arg)) {
			memo.put(arg, init);
		}
		return f.apply((Sem) memo.get(arg));
	}
	
	@SuppressWarnings("unchecked")
	default M $(FSM it) {
		if (memo.containsKey(it)) {
			return (M)memo.get(it);
		}
		if (it instanceof FSM) {
			return FSM((FSM)it); 
		}
		throw new RuntimeException("bad object");
	}
	
	@SuppressWarnings("unchecked")
	default S $(State it) {
		if (memo.containsKey(it)) {
			return (S)memo.get(it);
		}
		if (it instanceof State) {
			return State((State)it); 
		}
		throw new RuntimeException("bad object");
	}
	
	@SuppressWarnings("unchecked")
	default T $(Trans it) {
		if (memo.containsKey(it)) {
			return (T)memo.get(it);
		}
		if (it instanceof Trans) {
			return Trans((Trans)it); 
		}
		throw new RuntimeException("bad object");
	}
}

/*
 * Example of using "builtin" memoization
 */

interface FSM2GraphWithMemo extends MemoFSMAlg<Graph, Node, Edge> {
	
	@Override
	default Graph FSM(FSM fsm) {
		Graph g = new Graph();
		for (State s: fsm.states) {
			Node n = $(s);
			g.nodes.add(n);
			g.edges.addAll(n.in);
			g.edges.addAll(n.out);
		}
		return g;
	}
	
	@Override
	default Node State(State s) {
		return memoizing(s, new Node(s.name), n -> {
			for (Trans t: s.transitions) {
				Edge e = $(t);
				e.from = n;
				n.out.add(e);
			}
			return n;
		});
	}
	
	@Override
	default Edge Trans(Trans t) {
		Edge e = new Edge(null, $(t.to), t.event);
		$(t.to).in.add(e);
		return e;
	}
}

/*
 * An alternative approach to have the semantics directly available
 */

interface DirectFSMAlg<M, S, T> {
	M FSM(FSM fsm, List<S> states);
	S State(State s, List<T> transitions);
	T Trans(Trans t, S to);
}

class FSMAlgToDirect<M, S, T> implements FSMAlg<M, S, T> {
	
	private DirectFSMAlg<M, S, T> alg;

	IdentityHashMap<Object, Object> memo = new IdentityHashMap<>();

	private Class<M> m;

	private Class<S> s;

	private Class<T> t;
	
	public FSMAlgToDirect(Class<M> m, Class<S> s, Class<T> t, DirectFSMAlg<M, S, T> alg) {
		// NB: limitation: carrier types need to be interfaces.
		assert m.isInterface() && s.isInterface() && t.isInterface();
		this.m = m;
		this.s = s;
		this.t = t;
		this.alg = alg;
	}

	@Override
	public M FSM(FSM fsm) {
		List<S> states = new ArrayList<>();
		for (State s: fsm.states) {
			states.add($(s));
		}
		return alg.FSM(fsm, states);
	}

	@Override
	public S State(State s) {
		List<T> transitions = new ArrayList<>();
		for (Trans t: s.transitions) {
			transitions.add($(t));
		}
		return alg.State(s, transitions);
	}

	@Override
	public T Trans(Trans t) {
		return alg.Trans(t, $(t.to));
	}
	
	private static class Handler<T> implements InvocationHandler {
		private T it = null;
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return method.invoke(it, args);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private <To, From> To memoized(Class<To> c, From it, Function<From, To> f) {
		if (!memo.containsKey(it)) {
			Handler<To> h = new Handler<>();
			To m = (To) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] { c }, h);
			memo.put(it, m);
			h.it = f.apply(it);
		}
		return (To) memo.get(it);
	}
	
	@Override
	public M $(FSM it) {
		return memoized(this.m, it, FSMAlg.super::$);
	}
	
	@Override
	public S $(State it) {
		return memoized(this.s, it, FSMAlg.super::$);
	}
	
	@Override
	public T $(Trans it) {
		return memoized(this.t, it, FSMAlg.super::$);
	}
	
}


class ExecFSMDirect implements DirectFSMAlg<INext, INext, INext> {

	static <T> State next(List<T> lst, Function<T, State> f) {
		return lst.stream().map(f).filter(s -> s != null).findFirst().orElse(null);
	}
	
	@Override
	public INext FSM(FSM fsm, List<INext> states) {
		return ev -> next(states, s -> s.next(ev));
	}

	@Override
	public INext State(State s, List<INext> transitions) {
		return ev -> next(transitions, t -> t.next(ev));
	}

	@Override
	public INext Trans(Trans t, INext to) {
		return ev -> ev.equals(t.event) ? t.to : null;
	}
	
}

public class TestOAlgs {
	public static void main(String[] args) {
		FSM doors = new FSM();
		State closed = new State("closed");
		State opened = new State("opened");
		closed.transitions.add(new Trans("open", opened));
		opened.transitions.add(new Trans("close", closed));
		doors.states.add(closed);
		doors.states.add(opened);
		
		ExecFSM exec = new ExecFSM() { };
		INext next = exec.$(doors);
		for (String ev: Arrays.asList("open", "close", "open")) {
			System.out.println(next.next(ev).name);
		}
		
		ExecFSMDirect directExec = new ExecFSMDirect();
		FSMAlgToDirect<INext,INext,INext> convert = new FSMAlgToDirect<>(INext.class, INext.class, INext.class, directExec);
		INext next2 = convert.$(doors);
		
		for (String ev: Arrays.asList("open", "close", "open")) {
			System.out.println(next2.next(ev).name);
		}
		
	}
	
}

interface Bla {}


