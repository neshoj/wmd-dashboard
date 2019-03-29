package atlas.web.clients;

import atlas.core.template.forms.BaseServiceInterface;
import atlas.web.clients.entities.Clients;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ClientsServiceInterface extends BaseServiceInterface {
    List<Clients> fetchRecords(HttpServletRequest request);
}
