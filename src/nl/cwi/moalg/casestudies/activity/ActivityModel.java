package nl.cwi.moalg.casestudies.activity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import nl.cwi.moalg.annos.Def;
import nl.cwi.moalg.annos.Inv;
import nl.cwi.moalg.annos.Ref;
import nl.cwi.moalg.annos.Scopes;

public interface ActivityModel<Ac, Ed, No, NT, Var, Val, Ex, ET, BO, UO> {
  @Scopes({"Edge", "Node", "Variable"})
  Ac activity(String name, List<Var> locals, List<Var> inputs, List<No> nodes, List<Ed> edges);
  
  Ed edge(@Def("Edge") String name, 
      @Ref("Variable") Var guard, 
      @Ref("Node") No source, 
      @Ref("Node") No target);

  Ed edge(@Def("Edge") String name, 
      @Ref("Node") No source, 
      @Ref("Node") No target);

  No node(NT type, @Def("Node") String name, 
      @Inv(ns="Edge", field="source", many=true) List<Ed> out, 
      @Inv(ns="Edge", field="target", many=true) List<Ed> in);
  
  NT merge();
  NT decision();
  NT end();
  NT join();
  NT fork();
  NT initial();
  NT action(List<Ex> expressions);
  
  default NT action() {
    return action(Collections.emptyList());
  }

  default NT action(Ex exp) {
    return action(Collections.singletonList(exp));
  }
  
  Var integer(@Def("Variable") String name, Optional<Val> initialValue);
  Var bool(@Def("Variable") String name, Optional<Val> initialValue);
  
  Val integer(int value);
  Val bool(boolean value);
  
  Ex expr(@Ref("Variable") Var x, ET expr);
  
  
  ET binary(BO op,  @Ref("Variable")  Var lhs, @Ref("Variable") Var rhs);
  ET unary(UO op,  @Ref("Variable")  Var arg);
  
  UO not();

  
  BO add();
  BO sub();

  BO and();
  BO or();
  
  BO leq();
  BO geq();
  BO lt();
  BO gt();
  BO eq();
  BO neq();
}
