package atlas.web.weighbridgestations;


import atlas.core.template.forms.BaseServiceInterface;
import atlas.web.weighbridgestations.entities.WeighbridgeStations;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface WeighbridgeStationsServiceInterface extends BaseServiceInterface {
    public List<WeighbridgeStations> fetchRecords(HttpServletRequest request);
}
