package nl.cwi.moalg.casestudies.activity;

import java.util.Arrays;
import java.util.Optional;

import nl.cwi.moalg.obj.Obj;
import nl.cwi.moalg.obj.ToObj;

public class ActivityTest {

	static <Ac, Ed, No, NT, Var, Val, Ex, ET, BO, UO> Ac diagram(ActivitySyntax<Ac, Ed, No, NT, Var, Val, Ex, ET, BO, UO> alg) {
		return
		alg.activity("example", 
				Arrays.asList(
						alg.bool("notinternal", Optional.of(alg.bool(false)))),
				Arrays.asList(
						alg.bool("internal", Optional.empty())),
				Arrays.asList(
						// nodes
						alg.node(alg.initial(), "initial", null, null),
						alg.node(alg.action(alg.expr("notinternal", alg.unary(alg.not(), "internal"))), "register", null, null),
						alg.node(alg.decision(), "decision", null, null),
						alg.node(alg.action(), "assignToProjectExternal", null, null),
						alg.node(alg.action(), "getWelcomePackage", null, null),
						alg.node(alg.fork(), "fork", null, null),
						alg.node(alg.join(), "join", null, null),
						alg.node(alg.action(),"addToWebsite", null, null),
						alg.node(alg.action(), "assignToProject", null, null),
						alg.node(alg.action(), "managerInterview", null, null),
						alg.node(alg.action(), "managerReport", null, null),
						alg.node(alg.merge(), "merge", null, null),
						alg.node(alg.action(), "authorizePayment", null, null),
						alg.node(alg.end(), "final", null, null)
						),
				Arrays.asList(
						// edges
						alg.edge("1", "initial", "register"),
						alg.edge("2", "register", "decision"),
						alg.edge("3", "notinternal", "decision", "assignToProjectExternal"),
						alg.edge("4", "internal", "decision", "getWelcomePackage"),
						alg.edge("5", "getWelcomePackage", "fork"),
						alg.edge("6", "fork", "addToWebsite"),
						alg.edge("7", "fork", "assignToProject"),
						alg.edge("8", "addToWebsite", "join"),
						alg.edge("9", "assignToProject", "join"),
						alg.edge("10", "join", "managerInterview"),
						alg.edge("11", "managerInterview", "managerReport"),
						alg.edge("12", "managerReport", "merge"),
						alg.edge("13", "merge", "authorizePayment"),
						alg.edge("14", "authorizePayment", "final"),
						alg.edge("15", "assignToProjectExternal", "merge")
						));
	}
	
	@SuppressWarnings("unchecked")
  public static void main(String[] args) {
	  Obj activity = diagram((ActivitySyntax<Obj, Obj, Obj, Obj, Obj, Obj, Obj, Obj, Obj, Obj>)ToObj.make(ActivitySyntax.class));
	  System.out.println(activity);
	  
	  activity.resolve();
	  System.out.println(activity);
	  
	  
//	  IEval eval = (IEval)doors.into(new StatemachineEval());
//	  
//	  eval = eval.next("open");
//	  eval = eval.next("close");
//	  eval = eval.next("bla");
//	  eval = eval.next("lock");
//	  eval = eval.next("unlock");
  }
}
