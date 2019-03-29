package opdwms.web.configs.controllers;

import opdwms.core.template.View;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @category    Audit Logs
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Controller
public class ChangeLogController {

    @RequestMapping("/change-log")
    public ModelAndView index(){
        View view = new View("configs/change-log");
        return view.getView();
    }

}
