package nl.cwi.moalg.casestudies.statemachine;

import java.util.List;

public class StatemachineEval implements StatemachineModel<IEval, IEval, IEval> {

  @Override
  public IEval machine(String name, List<IEval> states) {
    return states.get(0);
  }

  @Override
  public IEval state(String name, List<IEval> transitions, List<IEval> in) {
    return new IEval() {
      public IEval next(String e) {
        System.out.println("In state: " + name);
        for (IEval t: transitions) {
          IEval next = t.next(e);
          if (next != null) {
            System.out.println("Fire!");
            return next;
          }
        }
        return this;
      }
    };
  }

  @Override
  public IEval trans(String event, IEval to) {
    return e -> event.equals(e) ? to : null;
  }

}
