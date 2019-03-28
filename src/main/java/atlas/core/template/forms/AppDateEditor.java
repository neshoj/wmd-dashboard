package atlas.core.template.forms;

import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.text.DateFormat;

/**
 * @category    Forms
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public class AppDateEditor extends CustomDateEditor {

    public AppDateEditor(DateFormat dateFormat, boolean allowEmpty) {
        super(dateFormat, allowEmpty);
    }

}
