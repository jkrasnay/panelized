package ca.krasnay.panelized;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for page classes that dictates the name of the page.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PageTitle {
    String value() default "";
}
