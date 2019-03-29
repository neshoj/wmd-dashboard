package opdwms.web.weighbridgestations;


import opdwms.web.weighbridgestations.entities.WeighbridgeTypes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface WeighbridgeTypesServiceInterface  {
    public List<WeighbridgeTypes> fetchRecords(HttpServletRequest request);
}
