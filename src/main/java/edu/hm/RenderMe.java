package edu.hm;

import java.lang.annotation.*;

/**
 * @author Yo Havlik
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })

public @interface RenderMe {
	String with() default "edu.hm.Renderer";
}
