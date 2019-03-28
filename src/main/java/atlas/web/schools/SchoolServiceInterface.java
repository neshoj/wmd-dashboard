package atlas.web.schools;

import atlas.core.template.forms.BaseServiceInterface;
import atlas.web.schools.entities.Schools;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SchoolServiceInterface extends BaseServiceInterface {
    List<Schools> fetchRecords(HttpServletRequest request);
}
