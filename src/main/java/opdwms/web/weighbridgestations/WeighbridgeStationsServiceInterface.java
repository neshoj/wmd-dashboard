package opdwms.web.weighbridgestations;


import opdwms.core.template.forms.BaseServiceInterface;
import opdwms.web.weighbridgestations.entities.WeighbridgeStations;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface WeighbridgeStationsServiceInterface extends BaseServiceInterface {
    public List<WeighbridgeStations> fetchRecords(HttpServletRequest request);
}
