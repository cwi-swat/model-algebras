package nl.cwi.moalg.obj;

import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.moalg.annos.Inv;

/*
 * TODO: error handling
 * - references to undefined definitions
 * - references to undefined namespaces in current scope
 * - duplicate definitions in same scope
 * - inverse on ns that isn't a defined entity (lift this restriction?)
 */

public class Obj {
  private static int ID = 0;
  private final String klass;
  private final Map<String, Object> fields = new HashMap<>();
  private final Set<String> scopedNamespaces = new HashSet<>();
  
  // namespace * fieldname
  private final Map<String, String> defines = new HashMap<>();
  private final Map<String, Set<String>> refs = new HashMap<>();
  
  private final Map<String, Inv> inverses = new HashMap<>();
  private final int id;
  
  public Obj(String klass) {
   this.klass = klass;
   this.id = ID++;
  }

  
  @Override
  public String toString() {
    StringWriter w = new StringWriter();
    toString(w, 0);
    return w.toString();
  }
  
  private void toString(StringWriter w, int indent) {
    w.append(klass + "(");
    if (!defines.isEmpty()) {
      w.append("#" + id + "|");
    }
    int i = 0;
    for (Map.Entry<String, Object> field: fields.entrySet()) {
      String name = field.getKey();
      w.append(name + ":" );
      Object value = field.getValue();
      
      if ((isRef(name) || isInverse(name)) && value instanceof Obj) {
        // if value instanceof String, we're in unresolved state.
        w.append("#" + ((Obj)value).id);
      }
      else if (value instanceof Obj) {
        indent += 2;
        w.append("\n");
        for (int j = 0; j < indent; j++) {
          w.append(" ");
        }
        ((Obj)value).toString(w, indent);
      }
      else if (value instanceof List) {
        if (isRef(name) || isInverse(name)) {
          w.append("[");
          boolean first = true;
          for (Object o: (List)value) {
            if (!first) {
              w.append(", ");
            }
            w.append("#" + ((Obj)o).id);
            first = false;
          }
          w.append("]");
        }
        else {
          w.append("[");
          indent += 1;
          for (Obj x: (List<Obj>)value) {
            w.append("\n");
            for (int j = 0; j < indent; j++) {
              w.append(" ");
            }
            ((Obj)x).toString(w, indent);
            w.append(",");
          }
          w.append("]");
        }
      }
      else {
        w.append(value != null ? value.toString() : "null");
      }
      if (i < fields.size() - 1) {
        w.append(", ");
      }
      i++;
    }
    w.append(")");
  }


  private boolean isRef(String name) {
    for (Set<String> r: refs.values()) {
      if (r.contains(name)) {
        return true;
      }
    }
    return false;
  }


  public void set(String name, Object obj) {
    fields.put(name, obj);
  }

  public void scopes(String ns) {
    scopedNamespaces.add(ns);
  }

  public void defines(String ns, String fieldName) {
    defines.put(ns, fieldName);
  }

  public void references(String ns, String fieldName) {
    if (!refs.containsKey(ns)) {
      refs.put(ns, new HashSet<>());
    }
    refs.get(ns).add(fieldName);
  }

  public void resolve() {
    List<InvTodo> invTodos = new ArrayList<>();
    resolve(new Env(null), new ArrayList<>(), invTodos);
    fixInverses(invTodos);
  }
  
  private void fixInverses(List<InvTodo> invTodos) {
    invTodos.forEach(t -> t.apply(this));
    for (String field: fields.keySet()) {
      if (isRef(field) || isInverse(field)) {
        continue;
      }
      Object o = fields.get(field);
      if (o instanceof Obj) {
        Obj obj = (Obj)o;
        obj.fixInverses(invTodos);
      }
      if (o instanceof List) {
        List<Object> l = (List<Object>)o;
        for (Object o2: l) {
          if (o2 instanceof Obj) {
            Obj obj2 = (Obj)o2;
            obj2.fixInverses(invTodos);
          }
        }
      }
    }
  }

  static class Todo {
    private Obj owner;
    private String ns;
    private String id;
    private String field;

    Todo(Obj owner, String ns, String id, String field) {
      this.owner = owner;
      this.ns = ns;
      this.id = id;
      this.field = field;
    }
    
    void fix(String ns, String id, Object target) {
      if (ns.equals(this.ns) && id.equals(this.id)) {
        owner.set(field, target);
      }
    }
  }
  
  static class InvTodo {
    private Obj me;
    private Inv inv;
    private String mine;

    InvTodo(Obj me, Inv inv, String mine) {
      this.me = me;
      this.inv = inv;
      this.mine = mine;
    }
    
    void apply(Obj obj) {
      // TODO: for now, invs only on defined stuff, not composed stuff
      if (obj.defines.containsKey(inv.ns())) {
        for (String other: obj.fields.keySet()) {
          if (!other.equals(inv.field())) {
            continue;
          }
          Obj o = (Obj) obj.fields.get(inv.field());
          if (o != me) {
            break;
          }
          if (inv.many()) {
            ((List<Object>)me.fields.get(mine)).add(obj);
          }
          else {
            me.fields.put(mine, obj);
          }
        }
      }
    }
    
  }
  
  @SuppressWarnings("rawtypes")
  private void resolve(Env env, List<Todo> todos, List<InvTodo> invTodos) {
    
    for (Map.Entry<String, String> def: defines.entrySet()) {
      String ns = def.getKey();
      String field = def.getValue();
      String id = (String)fields.get(field);
      todos.forEach(t -> t.fix(ns, id, this));
      env.define(ns, id, this);
    }
    
    for (Map.Entry<String, Set<String>> ref: refs.entrySet()) {
      String ns = ref.getKey();
      for (String field: ref.getValue()) {
        Object id = fields.get(field);
        if (!(id instanceof String)) {
          continue; // already resolved (?)
        }
        Object target = env.lookup(ns, (String)id);
        if (target != null) {
          fields.put(field, target);
        }
        else {
          todos.add(new Todo(this, ns, (String)id, field));
        }
      }
    }
    
    if (!scopedNamespaces.isEmpty()) {
      env = new Env(env);
      for (String ns: scopedNamespaces) {
        env.scope(ns);
      }
    }
    
    for (Map.Entry<String, Object> entry: fields.entrySet()) {
      String name = entry.getKey();
      Object obj = entry.getValue();
      
      if (isInverse(name)) {
        if (inverses.get(name).many()) {
          fields.put(name, new ArrayList<>());
        }
        invTodos.add(new InvTodo(this, inverses.get(name), name));
      }
      
      if (obj instanceof Obj && !isRef(name)) {
        // Only traverse spine
        ((Obj)obj).resolve(env, todos, invTodos);
      }
      if (obj instanceof List) {
        for (Object elt: (List)obj) {
          if (elt instanceof Obj) {
            ((Obj)elt).resolve(env, todos, invTodos);
          }
        }
      }
    }
  }
  
  private Method findMethod(Class<?> cls) {
    for (Method m: cls.getMethods()) {
      if (klass.equals(m.getName()) && m.getParameterCount() == fields.size()
          // filter out the generic ones which have erased types:
            && m.getReturnType() != Object.class) {
        return m;
      }
    }
    throw new RuntimeException("No method for " + klass + " in " + cls);
  }
  
  private static class Forwarder<T> implements InvocationHandler {
    T target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    	//System.out.println("Target  = " + target);
      return method.invoke(target, args);
    }
  }
  
  private Object forwarder(Class<?> cls, Forwarder<Object> fw) {
    return Proxy.newProxyInstance(Obj.class.getClassLoader(), new Class<?>[] {cls}, fw);
  }
  
  public <Alg> Object into(Alg alg) {
    return into(alg, new IdentityHashMap<>());
  }
  
  @SuppressWarnings("rawtypes")
  private <Alg> Object into(Alg alg, IdentityHashMap<Obj, Object> done) {
    if (done.containsKey(this)) {
      return done.get(this);
    }
    Method m = findMethod(alg.getClass());
//    System.out.println("Alg class: " + alg.getClass());
    final Forwarder<Object> fw = new Forwarder<>();
    Class<?> t = m.getReturnType();
//    System.out.println("METH = " + m.getName());
//    System.out.println("CLASS = " + t);
    
    if (!defines.isEmpty()) {
    	// this scheme requires all carrier types to be interfaces,
    	// not classes.
      done.put(this, forwarder(t, fw));
    }
    
    Object[] args = new Object[fields.size()];
    Parameter[] ps = m.getParameters();
    for (int i = 0; i < m.getParameterCount(); i++) {
       Object obj = fields.get(ps[i].getName());
//       System.out.println("field " + ps[i].getName() + ": " + obj);
       if (obj instanceof Obj) {
         args[i] = ((Obj)obj).into(alg, done);
       }
       else if (obj instanceof List) {
         List<Object> elts = new ArrayList<>();
         for (Object elt: ((List)obj)) {
           if (elt instanceof Obj) {
             elts.add(((Obj)elt).into(alg, done));
           }
           else {
             elts.add(elt);
           }
         }
         args[i] = elts;
       }
       else {
         args[i] = obj;
       }
    }
    try {
    	// NB: can't print arg here, because it will force the forwarder
    	// if any, to call toString on it.
      Object result = m.invoke(alg, args);
      if (!defines.isEmpty()) {
        fw.target = result;
      }
      return result;
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      System.err.println(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  boolean isInverse(String field) {
    return inverses.containsKey(field);
  }

  public void inverse(Inv inv, String mine) {
    inverses.put(mine, inv);
  }

  
}
