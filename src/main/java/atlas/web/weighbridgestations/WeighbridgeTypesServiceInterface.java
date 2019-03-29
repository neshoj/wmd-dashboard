package atlas.web.weighbridgestations;


import atlas.web.weighbridgestations.entities.WeighbridgeTypes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface WeighbridgeTypesServiceInterface  {
    public List<WeighbridgeTypes> fetchRecords(HttpServletRequest request);
}
