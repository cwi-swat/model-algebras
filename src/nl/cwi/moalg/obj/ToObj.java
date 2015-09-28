package nl.cwi.moalg.obj;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;

import nl.cwi.moalg.annos.Def;
import nl.cwi.moalg.annos.Inv;
import nl.cwi.moalg.annos.Ref;
import nl.cwi.moalg.annos.Scopes;


// Implements Alg over Repr
public class ToObj implements InvocationHandler {

    @SuppressWarnings("unchecked")
    public static <Alg> Alg make(Class<Alg> alg) {
      return (Alg) Proxy.newProxyInstance(Memo.class.getClassLoader(), 
          new Class<?>[] {alg}, new ToObj());
    }
    
    
    private ToObj() {
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      Obj r = new Obj(method.getName());
      
      Annotation[] methodAnnos = method.getAnnotations();
      for (Annotation anno: methodAnnos) {
        if (anno instanceof Scopes) {
          Scopes scopes = (Scopes)anno;
          for (String ns: scopes.value()) {
            r.scopes(ns);
          }
        }
      }
      
      Parameter[] ps = method.getParameters();
      for (int i = 0; i < method.getParameterCount(); i++) {
        Parameter p = ps[i];
        r.set(p.getName(), args[i]);
        
        Annotation[] annos = p.getAnnotations();
        for (Annotation anno: annos) {
          if (anno instanceof Def) {
            Def def = (Def)anno;
            r.defines(def.value(), p.getName());
          }
          
          if (anno instanceof Ref) {
            Ref ref = (Ref)anno;
            r.references(ref.value(), p.getName());
          }
          
          if (anno instanceof Inv) {
            r.inverse((Inv)anno, p.getName());
          }
        }
      }
      return r;
    }

}
