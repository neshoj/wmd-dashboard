package opdwms.web.usermanager.controllers;

import opdwms.core.template.View;
import opdwms.core.mail.MailerService;
import opdwms.web.usermanager.UserServiceInterface;
import opdwms.web.usermanager.auth.exceptions.InactiveAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;

/**
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Controller
public class AuthController {

    @Autowired
    private UserServiceInterface userService;
    @Autowired
    private MailerService mailService;

    @Value("${app.endpoint}")
    private String baseURL;

    /**
     *  When handling user-login
     *
     * @param error
     * @param logout
     * @param expired
     * @param redirectAttributes
     * @param request
     * @param model
     * @return ModelAndView
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            RedirectAttributes redirectAttributes, HttpServletRequest request, Model model) {

        boolean redirect = false;
        if (error != null) {
            redirect = true;
            redirectAttributes.addFlashAttribute("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
        }

        if (logout != null) {
            redirect = true;
            redirectAttributes.addFlashAttribute("msg", "You've been logged out successfully.");
        }

        if (expired != null) {
            redirect = true;
            redirectAttributes.addFlashAttribute("msg", "Sorry, your session has timed out.");
        }
        return (redirect) ? new ModelAndView("redirect:/login") : new ModelAndView("user-manager/login");
    }

    // customize the error message
    private String getErrorMessage(HttpServletRequest request, String key) {

        Exception exception = (Exception) request.getSession().getAttribute(key);

        String error = "";
        if (exception instanceof BadCredentialsException) {
//            error = "Sorry, you entered an incorrect email address or password";
            error = exception.getMessage();
        } else if (exception instanceof LockedException) {
            error = exception.getMessage();
        } else if (exception instanceof SessionAuthenticationException) {
            error = "Your login attempt was not successful. Seems you are logged in another device";
        } else if (exception instanceof CredentialsExpiredException) {
            error = exception.getMessage();
        }
        else if( exception instanceof InactiveAccountException){
            error = exception.getMessage();
        }
        else if (exception instanceof AccountExpiredException) {
            error = exception.getMessage();
        } else if (exception instanceof AccountStatusException) {
            error = "Sorry! Seems your account is inactive";
        } else if (exception instanceof SQLSyntaxErrorException) {
            error = "Sorry! You can't access your account at the moment; try again later";
        }
        else {
            exception.printStackTrace();
//            error = "Sorry! Your request can not be served at the moment";
            error = exception.getMessage();
        }

        return error;
    }

    /**
     * When handling password reset
     *
     * @param redirectAttributes
     * @param request
     * @return ModelAndView
     */
    @RequestMapping(value = "/password-reset")
    public ModelAndView ResetPassword(RedirectAttributes redirectAttributes, HttpServletRequest request) {

        View view = new View("user-manager/forgot-password");
        try {

            if ("POST".equals(request.getMethod())) {
                String email = request.getParameter("email");

                //When no email was supplied
                if(StringUtils.isEmpty( email )){
                    redirectAttributes.addFlashAttribute("error", "Please enter your email to complete this request.");
                }

                //When processing reset token
                else {
                    /*Check email, generate token and send email*/
                    Map<String, Object> response = userService.generateResetToken( email );
                    String status  = (String)response.get("status");

                    //When everything went well
                    if( "ok".equals( status )){
                        //Send email notification
                        userService.sendPasswordToken( (String)response.get("names"), (String)response.get("email"), (String)response.get("token"));
                        redirectAttributes.addFlashAttribute("msg", "If we found an account associated with that email address, you will find an email from us in your inbox shortly.");
                    }

                    //When the email does not exist
                    else if("not-found".equals( status )){
                        redirectAttributes.addFlashAttribute("error", "If we found an account associated with that email address, you will find an email from us in your inbox shortly.");
                    }
                }

                return view.redirect("password-reset");
            }

            //When serving a GET request, render the forgot password page
            else return view.getView();

        } catch (Exception ex) {
//            logger.error("Error while allowing user to reset password:", ex);
            redirectAttributes.addFlashAttribute("error", "Sorry! Your request couldn't be processed at the moment; try again");
            return new ModelAndView("redirect:/password-reset");
        }
    }
    
    @RequestMapping(value = "/password-reset/{code}")
    public ModelAndView passwordResetConfirmation (
            HttpServletRequest request, RedirectAttributes redirectAttributes,
            @PathVariable String code
    ) {
        // Some properties
        View view = new View("user-manager/reset");

        // If we have posted the data, redirect to the appropriate page
        if (request.getMethod().equals("POST")) {

            // The response
            String status;
            Map<String, Object> map = new HashMap<>();

            // Safely execute the below transaction
            try {
                String newpassword = request.getParameter("newpassword");
                String confpassword = request.getParameter("confpassword");

                if( StringUtils.isEmpty( newpassword ) || StringUtils.isEmpty( confpassword )){
                    status = "no-data";
                }
                else if( newpassword.equals( confpassword )) {
                    map = this.userService.setupNewPassword(code, newpassword);
                    status = String.valueOf( map.get("status") );
                }
                else{
                    status = "mismatch";
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
//                loggerService.logStackTrace(ex);
                status = "error";
            }

            // If an error occurs while processing the request
            if ( "error".equals( status ) ) {
                redirectAttributes.addFlashAttribute("error", "An error occurred while trying to complete the password reset process. Please try again.");
                return view.redirect("password-reset/"+code);
            }

            //When no data was supplied
            else if("no-data".equals( status )){
                redirectAttributes.addFlashAttribute("error", "The password and confirm password are required to complete this request.");
                return view.redirect("password-reset/"+code);
            }

            // If the passwords are different
            else if ( "mismatch".equals( status ) ) {
                redirectAttributes.addFlashAttribute("error", "The password and confirmation password do not match");
                return view.redirect("password-reset/"+code);
            }
            // If the reset process was successful
            else {
                //Send mail
                String email = String.valueOf( map.get("email") );
                String names = String.valueOf( map.get("names") );
                String lastName = String.valueOf( map.get("lastName") );

                boolean sent = mailService.sendMail ( mailService.sendGridConfig()
                        .setTo( email, names )
                        .setTemplateId("d-dbb468948b16490d97274ffa9b739ab6")
                        .setSubject("Password Reset Successful")
                        .addAttribute("_lastname", lastName )
                        .addAttribute("_baseUrl", baseURL )
                );

                redirectAttributes.addFlashAttribute("msg", "Password reset successful; login to access services");
                return view.redirect("login");
            }
        }

        // Check if the reset code is active and the user has been set
        String status = this.userService.validateResetToken( code );
        //When the token is invalid
        if ("invalid".equals( status )) {
            return view.redirect("password-reset");
        }

        //When the token has expired
        else if ("expired".equals( status )) {
            redirectAttributes.addFlashAttribute("error", "Reset password link is expired");
            return view.redirect("password-reset");
        }

        //When the token is valid
        else return view.getView();
    }

    /**
     * Setup account credentials
     *
     * @param request
     * @param redirectAttributes
     * @param key
     * @return ModelAndView
     */
    @RequestMapping(value = "/setup-account/{key}")
    public ModelAndView setupAccount(HttpServletRequest request,
                                     RedirectAttributes redirectAttributes, @PathVariable String key){
        // Some properties
        View view = new View("user-manager/setup");

        // If we have posted the data, redirect to the appropriate page
        if (request.getMethod().equals("POST")) {

            String status = "";
            Map<String, Object> map = new HashMap<>();
            String newPassword = request.getParameter("newPassword");
            String confPassword = request.getParameter("confPassword");

            if( StringUtils.isEmpty( confPassword ) || StringUtils.isEmpty( newPassword )){
                status = "no-data";
            }
            else if( confPassword.equals( newPassword )) {
                map = userService.setupNewPassword( key, newPassword );
                status = String.valueOf( map.get("status") );
            }
            else status = "mismatch";


            // If the passwords are different
            if ( "mismatch".equals( status ) ) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match");
                return view.redirect( "setup-account/" + key );
            }

            //When no data was supplied
            else if("no-data".equals( status )){
                redirectAttributes.addFlashAttribute("error", "The password and confirm password are required to complete this request.");
                return view.redirect( "setup-account/"+ key );
            }

            //When an unknown key has been used
            else if ( "invalid".equals( status ) ) {
                return view.redirect("login");
            }

            // When all went all
            else  {

                String email = String.valueOf( map.get("email") );
                String names = String.valueOf( map.get("names") );
                String lastName = String.valueOf( map.get("lastName") );

                boolean sent = mailService.sendMail ( mailService.sendGridConfig()
                        .setTo( email, names )
                        .setTemplateId("d-e16874752fa24a068c621a69e013af5a")
                        .setSubject("Account Activation")
                        .addAttribute("_lastname", lastName )
                        .addAttribute("_baseUrl", baseURL )
                );

                redirectAttributes.addFlashAttribute("msg", "Account has been successfully updated;  login to access Application services");
                return view.redirect("login");
            }
        }

        // Check if the reset code is active and the user has been set/password-reset
        String status = this.userService.validateResetToken( key );

        //When the token is invalid
        if ("invalid".equals( status )) {
            return view.redirect("login");
        }

        //When the token has expired
        else if ("expired".equals( status )) {
            redirectAttributes.addFlashAttribute("error", "Account set-up link is expired.");
            return view.redirect("login");
        }

        // Show page
        return view
                .addAttribute("email", status)
                .getView();
    }

}
