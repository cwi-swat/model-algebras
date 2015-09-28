package nl.cwi.moalg.casestudies.grammar;

import java.util.Arrays;
import java.util.List;

import nl.cwi.moalg.obj.Obj;
import nl.cwi.moalg.obj.ToObj;

public class Main {
	public static <G, R, P, S> G expr(GrammarSyntax<G, R, P, S> alg) {
		return alg.grammar("Expr", Arrays.asList(
				alg.rule("Expr", Arrays.asList(
						alg.prod("add", Arrays.asList(alg.sort("Expr"), alg.lit("+"), alg.sort("Expr"))),
						alg.prod("mul", Arrays.asList(alg.sort("Expr"), alg.lit("*"), alg.sort("Expr"))),
						alg.prod("var", Arrays.asList(alg.lit("x")))
						))
		));
	}
	
	@SuppressWarnings("unchecked")
  public static void main(String[] args) {
	  Obj expr = expr((GrammarSyntax<Obj,Obj,Obj,Obj>)ToObj.make(GrammarSyntax.class));
	  System.out.println(expr);
	  expr.resolve();
	  System.out.println(expr);
	  
	  IRecognize eval = (IRecognize)expr.into(new GrammarRecognizeLeftRec());
	  List<String> tokens = Arrays.asList("x", "+", "x", "*", "x");
	  eval.parse(tokens, rest -> {
	  	if (rest.isEmpty()) {
	  		System.out.println("Yes!");
	  	}
	  });
  }
	
}
