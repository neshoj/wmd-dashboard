package opdwms.web.clients;

import opdwms.core.template.forms.BaseServiceInterface;
import opdwms.web.clients.entities.Clients;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ClientsServiceInterface extends BaseServiceInterface {
    List<Clients> fetchRecords(HttpServletRequest request);
}
