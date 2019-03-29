package opdwms.core.template.datatables;


/**
 * Datatables row format component.
 *
 * This interface defines the methods a row format class should implement when
 * defining a class that will be used to format row content
 *
 * @category    Datatables
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
        */
public interface RowFormatInterface {
    /**
     * All formatters will implement this method that will be called by the 
     * datatable class to format the content
     * 
     * @param   row
     * @return  Object[]
     */
    public Object[] formatRow(Object[] row);
}
