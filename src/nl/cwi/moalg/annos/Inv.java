package nl.cwi.moalg.annos;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Inv {
	String ns();
	String field();
	boolean many();
}
