package nl.cwi.moalg.casestudies.statemachine;

import java.util.Arrays;

import nl.cwi.moalg.obj.Obj;
import nl.cwi.moalg.obj.ToObj;

public class Main {

	public static <M, S, T> M doors(StatemachineSyntax<M, S, T> alg) {
		return alg.machine("doors", Arrays.asList(
				alg.state("closed", Arrays.asList(alg.trans("open", "opened"), alg.trans("lock", "locked"))),
				alg.state("opened", Arrays.asList(alg.trans("close", "closed"))),
				alg.state("locked", Arrays.asList(alg.trans("unlock", "closed")))
				));
	}
	
	@SuppressWarnings("unchecked")
  public static void main(String[] args) {
	  Obj doors = doors((StatemachineSyntax<Obj,Obj,Obj>)ToObj.make(StatemachineSyntax.class));
	  System.out.println(doors);
	  doors.resolve();
	  System.out.println(doors);
	  
	  IEval eval = (IEval)doors.into(new StatemachineEval());
	  
	  eval = eval.next("open");
	  eval = eval.next("close");
	  eval = eval.next("bla");
	  eval = eval.next("lock");
	  eval = eval.next("unlock");
  }
	
	
}
