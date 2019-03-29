package opdwms.core.sms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @category    SMS
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 *
 */
@Service("smsService")
public class SmsService implements SmsServiceInterface{

    private final String USERNAME = "kilosahihi";
    private final String APIKEY = "5f62026be912e87930a675b30dd08a21b7a1393b652ae36108ecb05f45dac6eb";
//    private final String SENDER_ID = "Quickpay";
    
    /**
     * Get the object that will be used to send SMS via the SMS Gateway API
     *
     * @return SMS
     */
    @Override
    public SmsOptions smsInit() {
        return new SmsOptions();
    }
    
    /**
     * Send SMS to a client
     * 
     * @param smsOptions
     * @return True: if successful, else false
     */
    @Override
    public JsonNode  sendSMS(SmsOptions smsOptions){
        try
        {
            System.err.println(" =============== Sending SMS ======================");
            // Create a new instance of our awesome gateway class
            AfricasTalkingGateway gateway = new AfricasTalkingGateway(USERNAME, APIKEY);

            // Send SMS
            JsonNode results = gateway.sendMessage(smsOptions.getMobileNo(), smsOptions.getMessage());
//            JsonNode results = gateway.sendMessage(smsOptions.getMobileNo(), smsOptions.getMessage(), SENDER_ID);
            ObjectMapper mapper = new ObjectMapper();

            for (int i = 0; i < results.size(); ++i) {
                JsonNode result = results.get(i);

                System.out.print(result.path("status") + ","); // status is either "Success" or "error message"
                System.out.print(result.path("number") + ",");
                System.out.print(result.path("messageId") + ",");
                System.out.println(result.path("cost"));
            }
            System.err.println(" =============== Sending SMS ======================");
            return results.get(0);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Validate a client phone number
     * 
     * @param mobileNumber
     * @param country
     * @return True- if its valid, else false
     */
    @Override
    public boolean validatePhoneNumber(String mobileNumber, String country) {
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber numberProto = instance(mobileNumber, country, phoneUtil);
            
            return phoneUtil.isValidNumber(numberProto);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Generate an international phone number
     * 
     * @param mobileNumber
     * @param country
     * @return Mobile Phone Number in E64 format
     */
    @Override
    public String getInternationalPhoneNumber(String mobileNumber, String country) {
        try {
            
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber numberProto = instance(mobileNumber, country, phoneUtil);

            return phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            e.printStackTrace();
            return mobileNumber;
        } 
        catch (Exception e) {
            e.printStackTrace();
            return mobileNumber;
        }
    }

    private Phonenumber.PhoneNumber instance(String mobileNumber, String country, PhoneNumberUtil phoneUtil) throws Exception{
        Phonenumber.PhoneNumber numberProto = null;
        //when the country is defined
        if( null != country || "".equals( country )) {
            Map<String, String> countries = new HashMap<>();
            for (String isoCountry : Locale.getISOCountries()) {
                Locale l = new Locale("", isoCountry);
                countries.put(l.getDisplayCountry(), isoCountry);
            }
            String countryCode = countries.get(country);
            numberProto = phoneUtil.parse(mobileNumber, countryCode);
        }
        //when country is unknown
        else{
            numberProto = phoneUtil.parse(mobileNumber, "");
        }

        return numberProto;
    }
}
