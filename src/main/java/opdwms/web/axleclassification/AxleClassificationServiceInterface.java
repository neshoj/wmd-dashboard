package opdwms.web.axleclassification;

import opdwms.core.template.forms.BaseServiceInterface;
import opdwms.web.axleclassification.entities.AxleClassification;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AxleClassificationServiceInterface extends BaseServiceInterface {
    List<AxleClassification> fetchRecords(HttpServletRequest request);
}
