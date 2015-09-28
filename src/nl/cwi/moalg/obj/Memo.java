package nl.cwi.moalg.obj;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class Memo<Alg> implements InvocationHandler {

  @SuppressWarnings("unchecked")
  public static <Alg> Alg make(Class<Alg> cls, Alg alg) {
    return (Alg) Proxy.newProxyInstance(Memo.class.getClassLoader(), 
        new Class<?>[] {cls}, new Memo<Alg>(alg));
  }
  
  private final Alg alg;
  private final Map<Object[], Object> memo = new HashMap<>();
  
  private Memo(Alg alg) {
    this.alg = alg;
  }
  
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (!memo.containsKey(args)) {
      memo.put(args, method.invoke(alg, args));
    }
    return memo.get(args);
  }

}
