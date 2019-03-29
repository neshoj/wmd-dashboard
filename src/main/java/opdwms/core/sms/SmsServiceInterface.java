/*
 * The MIT License
 *
 * Copyright 2017 Binary Limited.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package opdwms.core.sms;

import com.fasterxml.jackson.databind.JsonNode;

/**
 *  This interface defines the basic utilities that will be used to send SMS
 *  to a respective client
 *
 * @category    SMS
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 *
 */
public interface SmsServiceInterface {
    
    /**
     * Get the object that will be used to send SMS via the Teke SMS Gateway API
     *
     * @return SMS
     */
    public SmsOptions smsInit();
    
    /**
     * Send SMS to a client
     * 
     * @param smsOptions
     * @return True: if successful, else false
     */
    public JsonNode sendSMS(SmsOptions smsOptions);
    
    /**
     * Validate a client phone number
     * 
     * @param mobileNumber
     * @param country
     * @return True- if its valid, else false
     */
    public boolean validatePhoneNumber(String mobileNumber, String country);
    
    /**
     * Generate an international phone number
     * 
     * @param mobileNumber
     * @param country
     * @return Mobile Phone Number
     */
    public String getInternationalPhoneNumber(String mobileNumber, String country);

}
