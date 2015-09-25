package nl.cwi.moalg.casestudies.statemachine;

import java.util.List;


public class StatemachineEvalCond extends StatemachineEval implements Expressions<IEvalExpr>,
	StmWithConditions<IEval, IEval, IEvalExpr, IEvalExpr, IEval>
{

	@Override
  public IEval machine(String name, List<IEvalExpr> decls,
      List<IEvalExpr> states) {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public IEvalExpr decl(String x, IEval init) {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public IEval when(IEval cond, String event, String to) {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public IEvalExpr add(IEvalExpr l, IEvalExpr r) {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public IEvalExpr sub(IEvalExpr l, IEvalExpr r) {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public IEvalExpr lt(IEvalExpr l, IEvalExpr r) {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public IEvalExpr gt(IEvalExpr l, IEvalExpr r) {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public IEvalExpr leq(IEvalExpr l, IEvalExpr r) {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public IEvalExpr geq(IEvalExpr l, IEvalExpr r) {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public IEvalExpr neq(IEvalExpr l, IEvalExpr r) {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public IEvalExpr eq(IEvalExpr l, IEvalExpr r) {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public IEvalExpr lit(int n) {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public IEvalExpr var(String x) {
	  // TODO Auto-generated method stub
	  return null;
  }

	
}
