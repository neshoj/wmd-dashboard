package opdwms.web.usermanager.controllers;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import opdwms.web.usermanager.UserProfileServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ignatius
 * @version 1.0.0
 * @category User Manager
 * @package Dev
 * @since Nov 05, 2018
 */
@Controller
public class UserProfileController {

    @Autowired
    private DatatablesInterface datatable;
    @Autowired
    private UserProfileServiceInterface userProfileService;
    @Autowired
    private DatatablesInterface dataTable;

    @RequestMapping(value = "/profile")
    public ModelAndView index(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Get the view
        View view = new View("user-manager/profile");
        Long userId = (Long) request.getSession().getAttribute("_userNo");

        // If we have posted the data
        if (request.getMethod().equals("POST")) {
            return handleRequests(request, redirectAttributes, view);
        }

        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {

            //Set-up data
            datatable
                    .select("str(a.createdOn; 'YYYY-MM-DD HH24:MI'), a.activity, a.status ")
                    .from("AuditTrail a LEFT JOIN a.userLink b ")
                    .where("b.id=:flag")
                    .setParameter("flag", userId);

            return view.sendJSON(datatable.showTable());
        }

        // Show the profile page
        return view
                .addAttribute("userDetails", userProfileService.fetchUserDetails(userId))
                .getView();
    }

    private ModelAndView handleRequests(HttpServletRequest request, RedirectAttributes redirectAttributes, View view) {
        String action = request.getParameter("action");
        Map<String, Object> map = new HashMap<>();

        try {
            switch (action) {
                //Update profile
                case "profile":
                    map = userProfileService.updateProfile(request);
                    break;
                //Change password
                case "password":
                    map = userProfileService.changePassword(request);
                    break;

                //Update profile image
                default:
                    map = userProfileService.updatePhoto(request);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Internal server error: try again later");
            return view.redirect("profile");
        }

        redirectAttributes.addFlashAttribute((String) map.get("status"), map.get("message"));
        return view.redirect("profile");
    }

    private ModelAndView fetchTableInfo(HttpServletRequest request, View view) {
        Long userId = (Long) request.getSession().getAttribute("_userNo");
        dataTable
                .select("str(a.createdOn; 'YYYY-MM-DD HH24:MI'), a.activity, a.status, a.oldValues, a.newValues ")
                .from("AuditTrail a LEFT JOIN a.userLink b")
                .where("a.userNo = :userNo ")
                .setParameter("userNo", userId)
        ;
        return view.sendJSON(dataTable.showTable());
    }
}
