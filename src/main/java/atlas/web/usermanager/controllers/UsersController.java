package atlas.web.usermanager.controllers;

import atlas.core.mail.MailerService;
import atlas.core.template.AjaxUtils;
import atlas.core.template.View;
import atlas.core.template.datatables.DatatablesInterface;
import atlas.web.configs.ReasonCodeServiceInterface;
import atlas.web.clients.ClientsServiceInterface;
import atlas.web.usermanager.UserGroupServiceInterface;
import atlas.web.usermanager.UserServiceInterface;
import atlas.web.usermanager.UserTypeServiceInterface;
import atlas.web.usermanager.entities.UserTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
public class UsersController {

    private UserServiceInterface entityService;
    private UserGroupServiceInterface userGroupService;
    private DatatablesInterface dataTable;
    private ReasonCodeServiceInterface reasonCodeService;
    private UserTypeServiceInterface userTypeService;
    private ClientsServiceInterface schoolServiceInterface;
    private MailerService mailService;

    @Value("${app.endpoint}")
    private String baseURL;

    @Autowired
    public UsersController(UserServiceInterface entityService,
                           UserGroupServiceInterface userGroupService,
                           DatatablesInterface dataTable,
                           ReasonCodeServiceInterface reasonCodeService,
                           UserTypeServiceInterface userTypeService,
                           ClientsServiceInterface schoolServiceInterface,
                           MailerService mailService) {
        this.entityService = entityService;
        this.userGroupService = userGroupService;
        this.dataTable = dataTable;
        this.reasonCodeService = reasonCodeService;
        this.userTypeService = userTypeService;
        this.schoolServiceInterface = schoolServiceInterface;
        this.mailService = mailService;
    }

    @RequestMapping(value = "/users")
    public ModelAndView index(HttpServletRequest request) {
        View view = new View("user-manager/users");
        String parentType = (String) request.getSession().getAttribute("_userParentType");

        if (ObjectUtils.nullSafeEquals(parentType, UserTypes.AEA_ADMIN) || ObjectUtils.nullSafeEquals(parentType, UserTypes.KENHA_ADMIN))
            view = new View("user-manager/users");

        if (AjaxUtils.isAjaxRequest(request)) {
            String action = request.getParameter("action");

            if (null != action)
                return handleRequests(request, view);

                //When fetching table data
            else
                return fetchTableInfo(request, view);

        }

        // Todo Prepare weighbridge station service
        return view
                .addAttribute("schools", schoolServiceInterface.fetchRecords(request))
                .addAttribute("userTypes", userTypeService.fetchRecords(request))
                .addAttribute("usergroups", userGroupService.fetchAllRecords(request))
                .addAttribute("reasoncodes", reasonCodeService.fetchRecords())
                .getView();
    }

    /**
     * Handle various client requests
     *
     * @param request Current Request
     * @param view    Current view
     * @return ModelAndView
     */
    private ModelAndView handleRequests(HttpServletRequest request, View view) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String action = request.getParameter("action");

            //When creating a record
            if ("new".equals(action))
                map = this.entityService.saveRecord(request);

            else if ("edit".equals(action))
                map = this.entityService.editRecord(request);

                //When fetching a record
            else if ("fetch-record".equals(action))
                map = this.entityService.fetchRecord(request);

                //View record changes
            else if ("vedit".equals(action))
                map = this.entityService.fetchRecordChanges(request);

                //When approving changes
            else if ("approve-edit".equals(action) || "decline-edit".equals(action))
                map = this.entityService.approveEditChanges(request);

                //View deactivation reasons
            else if ("vdeactivation".equals(action))
                map = this.entityService.fetchDeactivationInfo(request);

                //When deactivating an object
            else if ("deactivate".equals(action))
                map = this.entityService.deactivateRecord(request);

                //When toggling entity status
            else if ("decline-deactivation".equals(action) || "approve-deactivation".equals(action)
                    || "activate".equals(action) || "approve-new".equals(action)
                    || "delete".equals(action) || "decline-new".equals(action)) {

                map = this.entityService.flagRecords(request);

                System.err.println("sendMail : " + map.get("sendMail"));

                //When to send an email
                if (!ObjectUtils.isEmpty(map.get("sendMail")) && "approve-new".equals(action)) {
                    String email = String.valueOf(map.get("email"));
                    String token = String.valueOf(map.get("token"));
                    String names = String.valueOf(map.get("names"));
                    String lastName = String.valueOf(map.get("lastName"));

                    boolean sent = mailService.sendMail(mailService.sendGridConfig()
                            .setTo(email, names)
                            .setTemplateId("d-f88b25f094f64d6bad5a181a9283fd0a")
                            .setSubject("Account Setup")
                            .addAttribute("_lastname", lastName)
                            .addAttribute("_baseUrl", baseURL + "/setup-account/" + token)
                    );
                }
            }

            //When unlocking a user
            else if ("unlock".equals(action))
                map = this.entityService.unlockUser(request);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "01");
            map.put("message", "Internal server error: contact admin");
        }

        return view.sendJSON(map);
    }

    /**
     * Fetch table information
     *
     * @param request Current Request
     * @param view    Current View
     * @return JsonView - json structure that DataTable can consume
     */
    private ModelAndView fetchTableInfo(HttpServletRequest request, View view) {
        String state = request.getParameter("fetch-table");
        Long parentNo = (Long) request.getSession().getAttribute("_userParentNo");
        String parentType = (String) request.getSession().getAttribute("_userParentType");

        dataTable
                .select("CONCAT(a.firstName; ' '; a.surname), a.email, b.name, str(a.createdOn; 'YYYY-MM-DD HH24:MI:SS'), str(a.updatedOn; 'YYYY-MM-DD HH24:MI:SS'), a.id")
                .from("Users a LEFT JOIN a.userGroupLink b")
                .where("a.flag =:flag")
                .setParameter("flag", state)
        ;

        // Set the parent entity
        if (!StringUtils.isEmpty(parentType) && null != parentNo) {

            if (parentType.equals(UserTypes.AEA_ADMIN) || parentType.equals(UserTypes.KENHA_ADMIN)) {
                dataTable
                        .from("LEFT JOIN a.clientUsersLink c")
                        .where("c.clientNo = :parentNo")
                        .setParameter("parentNo", parentNo);
            }
        }

        return view.sendJSON(dataTable.showTable());
    }


}
