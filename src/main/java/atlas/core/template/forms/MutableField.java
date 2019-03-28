package atlas.core.template.forms;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation identifies all entity fields that changes can be tracked
 * <p>
 * Apply this annotation for entities with maker-checker requirement
 *
 * @category    Forms
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MutableField {

    String name() default "";
    String entity() default "";
    String reference() default "";
    boolean optional() default false;
}
