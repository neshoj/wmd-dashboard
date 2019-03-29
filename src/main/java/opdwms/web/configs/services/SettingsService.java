package opdwms.web.configs.services;

import opdwms.web.configs.SettingsServiceInterface;
import opdwms.web.configs.entities.AppSettings;
import opdwms.web.configs.forms.AppSettingsForm;
import opdwms.web.configs.repository.AppSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @category    Audit Logs
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Service
@Transactional
public class SettingsService implements SettingsServiceInterface {

    @Autowired
    private AppSettingsRepository appSettingsRepo;
    @Autowired
    private AppSettingsForm appSettingsForm;

    /**
     * Fetch a record information
     *
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> fetchRecord(HttpServletRequest request){
        String index = request.getParameter("index");
        return this.appSettingsForm.transformEntity( index );
    }

    /**
     * Update a record with new values
     *
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> editRecord(HttpServletRequest request){
        Map<String, Object> map = new HashMap<String, Object>();

        String index = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String value = request.getParameter("value");

        AppSettings entity = this.appSettingsRepo.findById( Long.valueOf( index ) ).get();
        entity.setName( name ).setValue( value ).setDescription( description );
        appSettingsRepo.save( entity );

        map.put("status", "00");
        map.put("message", "Request processed successfully");
        return map;
    }
}
