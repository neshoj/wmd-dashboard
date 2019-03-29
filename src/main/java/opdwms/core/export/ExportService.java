package opdwms.core.export;

import opdwms.core.template.datatables.DatatablesInterface;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @category    Export
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Service
public class ExportService {

    private static HttpSession setAttributes(DatatablesInterface dataTable, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> map = dataTable.getParameters();
        session.setAttribute("requestParam", map.get("params"));
        session.setAttribute("requestParamList", map.get("listParams"));
        // Save the parameters and sql used to generate the datatable
        session.setAttribute("requestSQL", dataTable.getHQL(""));
        return session;
    }

    public static void init(DatatablesInterface dataTable, HttpServletRequest request, String[] columns) {
        HttpSession session = setAttributes(dataTable, request);
        session.setAttribute("columns", columns);
    }
}
