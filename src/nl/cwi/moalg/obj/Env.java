package nl.cwi.moalg.obj;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Env {
  private final Env parent;
  private final Set<String> namespaces = new HashSet<>();
  private final Map<String,Object> defs = new HashMap<>();

  public Env(Env parent) {
    this.parent = parent;
  }
  
  public void scope(String ns) {
    namespaces.add(ns);
  }
  
  public void define(String ns, String id, Object obj) {
    if (namespaces.contains(ns)) {
      defs.put(id, obj);
    }
    else if (parent != null) {
      parent.define(ns, id, obj);
    }
    else {
      throw new RuntimeException("No scope for " + ns);
    }
  }

  public Object lookup(String ns, String id) {
    if (namespaces.contains(ns)) {
      if (defs.containsKey(id)) {
        return defs.get(id);
      }
    }
    if (parent != null) {
      return parent.lookup(ns, id);
    }
    return null;
  }

}
