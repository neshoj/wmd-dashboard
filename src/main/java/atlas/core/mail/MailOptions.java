package atlas.core.mail;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Personalization;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The parameters used to send the email to the client
 *
 * @category    Mail
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public class MailOptions {
    private Email _email;
    private final Personalization _personalization;
    private final Mail _mail;
    
     /**
     * Init default values
     */
    public MailOptions(){
        _mail = new Mail();
        _personalization = new Personalization();
        
        //Default mail originator
        _email = new Email("info@ncgafrica.com", "Boost");
        _mail.setFrom( _email );
    }
    
    /**
     * Fetch a packed Mail instance
     * 
     * @return Mail
     */
    public Mail init(){
        _mail.addPersonalization( _personalization );
        return _mail;
    }
    
    /**
     * When there is need to override the default sender
     * @param email
     * @param name
     * @return MailOptions
     */
    public MailOptions setFrom(String email, String name){
         _email = new Email( email, name);
        _mail.setFrom( _email );
        return this;
    }
    
    /**
     * Recipient's information
     * 
     * @param email
     * @return MailOptions
     */
    public MailOptions setTo(String email){
        _email = new Email( email );
        _personalization.addTo(_email);
        return this;
    }
    
    /**
     *  Recipient's information
     * 
     * @param email
     * @param name
     * @return MailOptions
     */
    public MailOptions setTo(String email, String name){
        _email = new Email(email, name);
        _personalization.addTo(_email);
        return this;
    }
    
    /**
     *  Recipients' information
     * 
     * @param emails
     * @return MailOptions
     */
    public MailOptions setTo(List<String> emails){
       emails.stream().forEach((email) -> {
            _email = new Email(email);
             _personalization.addTo(_email);
        });
        return this;
    }
    
    /**
     * Recipient's information for a carbon copy
     * 
     * @param email
     * @return MailOptions
     */
    public MailOptions setCc(String email){
        _email = new Email( email );
        _personalization.addCc(_email);
        return this;
    }
    
     /**
     * Recipient's information for a carbon copy
     * 
     * @param email
     * @param name
     * @return MailOptions
     */
    public MailOptions setCc(String email, String name){
        _email = new Email( email, name );
        _personalization.addCc(_email);
        return this;
    }
    
    /**
     *  Recipients' information for a carbon copy
     * 
     * @param emails
     * @return MailOptions
     */
    public MailOptions setCc(List<String> emails){
        emails.stream().forEach((email) -> {
            _email = new Email(email);
             _personalization.addCc(_email);
        });
        return this;
    }
    
    /**
     * Recipient information for a blind copy
     * 
     * @param email
     * @return MailOptions
     */
    public MailOptions setBcc(String email){
        _email = new Email( email );
        _personalization.addBcc(_email);
        return this;
    }
    
    /**
     *  Recipients' information for a blind copy
     * 
     * @param emails
     * @return MailOptions
     */
    public MailOptions setBcc(List<String> emails){
       emails.stream().forEach((email) -> {
            _email = new Email(email);
             _personalization.addBcc(_email);
        });
        return this;
    }
    
    /**
     * Sets the email subject
     * 
     * @param subject
     * @return MailOptions
     */
    public MailOptions setSubject(String subject){
        _personalization.setSubject( subject );
        return this;
    }
    
    /**
     * Set the template to use when sending the email
     * 
     * @param   templateId
     * @return  MailOptions
     */
    public MailOptions setTemplateId(String templateId) {
        _mail.setTemplateId( templateId );
        return this;
    }
    
    /**
     * In the event that one will specify the content directly to the service
     * 
     * @param   content
     * @return  MailOptions
     */
    public MailOptions setContent(String content) {
        Content _content;
        if( null == content )  _content = new Content("text/plain", content);
        else _content = new Content("text/html", " ");
        _mail.addContent( _content );
        return this;
    }
    
    /**
     *  Populate dynamic values when using a template
     * 
     * @param key
     * @param value
     * @return MailOptions
     */
    public MailOptions addAttribute(String key, String value) {
        _personalization.addDynamicTemplateData(key, HtmlUtils.htmlEscape(value));
        return this;
    }
    
    /**
     *  Populate dynamic values when using a template
     * 
     * @param map
     * @return MailOptions
     */
    public MailOptions addAttribute(Map<String, Object> map) {
        for( Map.Entry<String, Object> entry : map.entrySet()){
            
            if( (entry.getValue()) instanceof BigDecimal ){
                _personalization.addSubstitution(entry.getKey(), (entry.getValue()).toString()  );
            }
            else if( (entry.getValue()) instanceof Long ){
                _personalization.addSubstitution(entry.getKey(), (entry.getValue()).toString() );
            }
            else if( entry.getValue() instanceof  ArrayList ){}
            else {
                _personalization.addSubstitution(entry.getKey(), HtmlUtils.htmlEscape((String) entry.getValue()));
            }
                
        }
        return this;
    }
}
