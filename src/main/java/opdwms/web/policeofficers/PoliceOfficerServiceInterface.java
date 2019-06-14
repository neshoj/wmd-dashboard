package opdwms.web.policeofficers;

import opdwms.core.template.forms.BaseServiceInterface;
import opdwms.web.policeofficers.entities.PoliceOfficers;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PoliceOfficerServiceInterface  extends BaseServiceInterface {
    public List<PoliceOfficers> fetchRecords(HttpServletRequest request);
}
