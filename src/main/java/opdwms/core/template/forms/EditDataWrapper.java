package opdwms.core.template.forms;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a column that contains an instance of a bean 
 * that has been serialized into a string
 * <p>
 * This implementation follows that a modified bean is not persisted as a different entity
 * but to a column.
 * 
 *
 * @category    Forms
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EditDataWrapper {}
