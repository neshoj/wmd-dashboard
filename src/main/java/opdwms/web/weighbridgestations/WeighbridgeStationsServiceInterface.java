package opdwms.web.weighbridgestations;


import opdwms.core.template.forms.BaseServiceInterface;
import opdwms.web.weighbridgestations.entities.WeighbridgeStations;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface WeighbridgeStationsServiceInterface extends BaseServiceInterface {
    public List<WeighbridgeStations> fetchRecords(HttpServletRequest request);

    public Optional<WeighbridgeStations> findByWBSCode(String request);

    public Optional<WeighbridgeStations> findByWBSId(String request);
}
