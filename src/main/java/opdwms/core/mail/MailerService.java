package opdwms.core.mail;

import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * This interface defines the basic utilities that will be used to send an email
 * to a respective client
 *
 * @category    Mail
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Service("sendGridMailService")
public class MailerService {

    // The object used to send the email content
    private static SendGrid sendGrid;

    @Autowired
    public MailerService(@Value("${mail.apiKey}")String mailApiKey) {
        sendGrid = new SendGrid( mailApiKey );
    }
    
    /**
     * Get the object that will be used to send email via the SendGrid API
     *
     * @return MailOptions
     */
    public MailOptions sendGridConfig() {
        return new MailOptions();
    }

    /**
     * Send the mail message.
     *
     * @return Boolean
     */
    public boolean sendMail(MailOptions options) {
        try {

            Mail mail = options.init();
//            Email email = new Email("info@quicklink.co.ke", "Quicklink");
//            mail.setFrom( email );

            Request request = new Request();
            request.setMethod( Method.POST );
            request.setEndpoint( "mail/send" );
            request.setBody( mail.build() );

            System.err.println(" body ->" + mail.build() );
            
            //Retrieve response
            Response response = sendGrid.api(request);
            
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody() );
            System.out.println(response.getHeaders() );

            if (202 != response.getStatusCode() ) {

                System.err.println("================== MAIL ERROR =====================");
                System.err.println( response.getBody() );
                System.err.println("================== MAIL ERROR =====================");
            }

            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println( ex.getMessage() );
            return false;
        }
    }




}
