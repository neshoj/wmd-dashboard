/*
 * Copyright 2016 Anthony.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package opdwms.core.sms;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @category    SMS
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public class AfricasTalkingGateway {

    private String _username;
    private String _apiKey;
    private String _environment;
    private int responseCode;

    private static final int HTTP_CODE_OK      = 200;
    private static final int HTTP_CODE_CREATED = 201;

    //Change debug flag to true to view raw server response
    private static final boolean DEBUG = false;
    private ObjectMapper mapper = new ObjectMapper();

    public AfricasTalkingGateway(String username_, String apiKey_)
    {
        _username    = username_;
        _apiKey      = apiKey_;
        _environment = "production";
    }

    public AfricasTalkingGateway(String username_, String apiKey_, String environment_)
    {
        _username    = username_;
        _apiKey      = apiKey_;
        _environment = environment_;
    }


    //Bulk messages methods
    public JsonNode sendMessage(String to_, String message_) throws Exception
    {

        HashMap<String, String> data = new HashMap<>();
        data.put("username", _username);
        data.put("to", to_);
        data.put("message", message_);

        return sendMessageImpl(to_, message_, data);
    }


    public JsonNode sendMessage(String to_, String message_, String from_) throws Exception
    {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", _username);
        data.put("to", to_);
        data.put("message", message_);

        if ( from_.length() > 0 ) data.put("from", from_);

        return sendMessageImpl(to_, message_, data);
    }


    public JsonNode sendMessage(String to_, String message_, String from_, int bulkSMSMode_) throws Exception
    {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", _username);
        data.put("to", to_);
        data.put("message", message_);

        if ( from_.length() > 0 ) data.put("from", from_);

        data.put("bulkSMSMode", Integer.toString(bulkSMSMode_));

        return sendMessageImpl(to_, message_, data);
    }


    public JsonNode sendMessage(String to_, String message_, String from_, int bulkSMSMode_, HashMap<String, String> options_) throws Exception
    {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", _username);
        data.put("to", to_);
        data.put("message", message_);

        if ( from_.length() > 0 ) data.put("from", from_);

        data.put("bulkSMSMode", Integer.toString(bulkSMSMode_));


        if (options_.containsKey("enqueue")) data.put("enqueue", options_.get("enqueue"));
        if (options_.containsKey("keyword")) data.put("keyword", options_.get("keyword"));
        if (options_.containsKey("linkId"))  data.put("linkId", options_.get("linkId"));
        if (options_.containsKey("retryDurationInHours"))  data.put("retryDurationInHours", options_.get("retryDurationInHours"));

        return sendMessageImpl(to_, message_, data);
    }


    public JsonNode fetchMessages(int lastReceivedId_) throws Exception
    {
        String requestUrl = getSmsUrl() + "?" +
                URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(_username, "UTF-8") +
                "&" + URLEncoder.encode("lastReceivedId", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(lastReceivedId_), "UTF-8");

        String response = sendGETRequest(requestUrl);
        if(responseCode == HTTP_CODE_OK) {
            JsonNode node = mapper.readTree( response );
            return node.path("SMSMessageData").path("Messages");
        }

        throw new Exception(response.toString());
    }


    //Subcscription methods
    public JsonNode createSubscription(String phoneNumber_, String shortCode_, String keyword_) throws Exception
    {
        if(phoneNumber_.length() == 0 || shortCode_.length() == 0 || keyword_.length() == 0)
            throw new Exception("Please supply phoneNumber, shortCode and keyword");

        HashMap <String, String> data_ = new HashMap<String, String>();
        data_.put("username", _username);
        data_.put("phoneNumber", phoneNumber_);
        data_.put("shortCode", shortCode_);
        data_.put("keyword", keyword_);
        String requestUrl = getSubscriptionUrl() + "/create";

        String response = sendPOSTRequest(data_, requestUrl);

        if(responseCode != HTTP_CODE_CREATED)
            throw new Exception(response.toString());

        JsonNode node =mapper.readTree(response);
        return node;
    }


    public JsonNode deleteSubscription(String phoneNumber_, String shortCode_, String keyword_) throws Exception
    {
        if(phoneNumber_.length() == 0 || shortCode_.length() == 0 || keyword_.length() == 0)
            throw new Exception("Please supply phone number, short code and keyword");

        HashMap <String, String> data_ = new HashMap<String, String>();
        data_.put("username", _username);
        data_.put("phoneNumber", phoneNumber_);
        data_.put("shortCode", shortCode_);
        data_.put("keyword", keyword_);
        String requestUrl = getSubscriptionUrl() + "/delete";

        String response = sendPOSTRequest(data_, requestUrl);

        if(responseCode != HTTP_CODE_CREATED)
            throw new Exception(response.toString());

        JsonNode node = mapper.readTree( response );
        return node;
    }


    public JsonNode fetchPremiumSubscriptions (String shortCode_, String keyword_, int lastReceivedId_) throws Exception
    {
        if(shortCode_.length() == 0 || keyword_.length() == 0)
            throw new Exception("Please supply short code and keyword");

        lastReceivedId_ = lastReceivedId_ > 0? lastReceivedId_ : 0;
        String requestUrl = getSubscriptionUrl() + "?username="+_username+"&shortCode="+shortCode_+"&keyword="+keyword_+"&lastReceivedId="+lastReceivedId_;

        String response = sendGETRequest(requestUrl);
        if(responseCode == HTTP_CODE_OK) {
            JsonNode node = mapper.readTree( response );
            return node.path("responses");
        }

        throw new Exception(response.toString());
    }


    public JsonNode call(String from_, String to_) throws Exception
    {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", _username);
        data.put("from", from_);
        data.put("to", to_);
        String urlString = getVoiceUrl() + "/call";
        String response  = sendPOSTRequest(data, urlString);

        JsonNode node = mapper.readTree( response );

        if( node.path("errorMessage").asText().equals("None"))
            return node.path("entries");
        throw new Exception( node.path("errorMessage").asText() );
    }

    //Call methods
    public JsonNode getNumQueuedCalls(String phoneNumber, String queueName) throws Exception
    {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", _username);
        data.put("phoneNumber", phoneNumber);
        data.put("queueName", queueName);

        return queuedCalls(data);
    }


    public JsonNode getNumQueuedCalls(String phoneNumber) throws Exception
    {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", _username);
        data.put("phoneNumbers", phoneNumber);

        return queuedCalls(data);
    }


    public void uploadMediaFile(String url_, String phoneNumber_) throws Exception
    {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", _username);
        data.put("url", url_);
        data.put("phoneNumber", phoneNumber_);
        String requestUrl = getVoiceUrl() + "/mediaUpload";

        sendPOSTRequest(data, requestUrl);
    }


    //Airtime methods
    public JsonNode sendAirtime(String recipients_) throws Exception
    {
        HashMap<String, String> data_ = new HashMap<String, String>();
        data_.put("username", _username);
        data_.put("recipients", recipients_);
        String urlString = getAirtimeUrl() + "/send";

        String response = sendPOSTRequest(data_, urlString);

        if(responseCode == HTTP_CODE_CREATED) {
            JsonNode node = mapper.readTree( response );
            JsonNode results = node.path("responses");
            if( results.size() > 0)
                return results;
            throw new Exception( node.path("errorMessage").asText() );
        }

        throw new Exception(response);
    }


    //User data method
    public JsonNode getUserData() throws Exception
    {
        String requestUrl = getUserDataUrl() + "?username="+_username;

        String response   = sendGETRequest(requestUrl);
        if(responseCode == HTTP_CODE_OK) {
            JsonNode node = mapper.readTree( response );
            return node.path("UserData");
        }

        throw new Exception(response);
    }

    public JsonNode initiateMobilePaymentCheckout(String productName_,
                                                  String phoneNumber_,
                                                  String currencyCode_,
                                                  Double amount_,
                                                  Map<String, String> metadata_) throws Exception
    {
        JsonNode requestBody = mapper.createObjectNode()
                .put("username", _username)
                .put("productName", productName_)
                .put("phoneNumber", phoneNumber_)
                .put("currencyCode", currencyCode_)
                .put("amount", amount_)
                .put("metadata", mapper.writeValueAsString( metadata_ ));
        String response = sendJsonPOSTRequest(requestBody.toString(), getMobilePaymentCheckoutUrl());
        return mapper.readTree( response );
    }

    public JsonNode mobilePaymentB2CRequest(String productName_,
                                            List<MobilePaymentB2CRecipient> recipients_) throws Exception
    {
        ArrayNode jsonRecipients = mapper.createArrayNode();
        for (MobilePaymentB2CRecipient recipient: recipients_) {
            jsonRecipients.add(recipient.toJSON());
        }
        JsonNode requestBody = mapper.createObjectNode()
                .put("username", _username)
                .put("productName", productName_)
                .put("recipients", mapper.convertValue( jsonRecipients, String.class ));


        if(DEBUG) System.out.println("Raw Request: " + requestBody.toString());

        String response = sendJsonPOSTRequest(requestBody.toString(), getMobilePaymentB2CUrl());
        JsonNode jsonResponse = mapper.readTree( response );
        JsonNode entries = jsonResponse.path("entries");
        if ( entries.size() > 0 ) return entries;
        throw new Exception(jsonResponse.path("errorMessage").asText() );
    }

    public JsonNode mobilePaymentB2BRequest(String productName_, HashMap<String, String> providerData_,
                                            String currencyCode_, float amount_, HashMap<String, String> metadata_) throws Exception
    {
        if(!providerData_.containsKey("provider"))
            throw new Exception("Missing field provider");

        if(!providerData_.containsKey("destinationChannel"))
            throw new Exception("Missing field destinationChannel");

        if(!providerData_.containsKey("destinationAccount"))
            throw new Exception("Missing field destinationAccount");

        if(!providerData_.containsKey("transferType"))
            throw new Exception("Missing field transferType");

        JsonNode requestBody = mapper.createObjectNode();
        ((ObjectNode)requestBody).put("username", _username);
        ((ObjectNode)requestBody).put("productName", productName_);
        ((ObjectNode)requestBody).put("currencyCode", currencyCode_);
        ((ObjectNode)requestBody).put("amount", amount_);
        ((ObjectNode)requestBody).put("provider", providerData_.get("provider"));
        ((ObjectNode)requestBody).put("destinationChannel", providerData_.get("destinationChannel"));
        ((ObjectNode)requestBody).put("destinationAccount", providerData_.get("destinationAccount"));
        ((ObjectNode)requestBody).put("transferType", providerData_.get("transferType"));
        ((ObjectNode)requestBody).put("metadata", mapper.writeValueAsString( metadata_ ) );

        if(DEBUG) System.out.println("Raw Request: " + requestBody.toString());

        String response = sendJsonPOSTRequest(requestBody.toString(), getMobilePaymentB2BUrl());
        JsonNode nodeResponse = mapper.readTree( response );
        return nodeResponse;
    }

    private JsonNode sendMessageImpl(String to_, String message_, HashMap<String, String> data_) throws Exception {
        String response = sendPOSTRequest(data_, getSmsUrl());
        if (responseCode == HTTP_CODE_CREATED) {
            JsonNode node = mapper.readTree( response );
            JsonNode recipients = node.path("SMSMessageData").path("Recipients");

            if( recipients.size() > 0) return recipients;

            throw new Exception( node.path("SMSMessageData").path("Message").asText() );
        }

        throw new Exception( response );
    }
    //
    //Private accessor methods
    private JsonNode queuedCalls(HashMap<String, String> data_) throws Exception {
        String requestUrl = getVoiceUrl() + "/queueStatus";
        String response = sendPOSTRequest(data_, requestUrl);
        JsonNode node = mapper.readTree( response );
        if( node.path("errorMessage").asText().equals("None") )
            return node.path("entries");
        throw new Exception( node.path("errorMessage").asText() );
    }

    private String sendPOSTRequest(HashMap<String, String> dataMap_, String urlString_) throws Exception {
        String data = new String();
        Iterator<Entry<String, String>> it = dataMap_.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> pairs = (Entry<String, String>)it.next();
            data += URLEncoder.encode(pairs.getKey().toString(), "UTF-8");
            data += "=" + URLEncoder.encode(pairs.getValue().toString(), "UTF-8");
            if ( it.hasNext() ) data += "&";
        }
        URL url = new URL(urlString_);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("apikey", _apiKey);
        conn.setDoOutput(true);
        return sendPOSTRequestImpl(data, conn);
    }

    private String sendJsonPOSTRequest(String data_, String urlString_) throws Exception
    {
        URL url = new URL(urlString_);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("apikey", _apiKey);
        conn.setDoOutput(true);
        return sendPOSTRequestImpl(data_, conn);
    }

    private String sendPOSTRequestImpl(String data_, URLConnection conn_) throws Exception {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(conn_.getOutputStream());
            writer.write(data_);
            writer.flush();

            HttpURLConnection http_conn = (HttpURLConnection)conn_;
            responseCode = http_conn.getResponseCode();

            BufferedReader reader;
            boolean passed = true;

            if(responseCode == HTTP_CODE_OK || responseCode == HTTP_CODE_CREATED) {
                reader = new BufferedReader(new InputStreamReader(http_conn.getInputStream()));
            }
            else {
                reader = new BufferedReader(new InputStreamReader(http_conn.getErrorStream()));
                passed = false;
            }
            String response = readResponse(reader);

            if(DEBUG) System.out.println("ResponseCode: " + responseCode + " RAW Response: " + response);

            reader.close();

            if(passed) return response;

            throw new Exception(response);

        } catch (Exception e){
            throw e;
        }
    }

    private String readResponse(BufferedReader reader) throws Exception
    {
        StringBuilder response = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            response.append(line);
        }
        return response.toString();
    }

    private String sendGETRequest(String urlString) throws Exception
    {
        try {
            URL url= new URL(urlString);
            URLConnection connection = (URLConnection)url.openConnection();
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("apikey", _apiKey);

            HttpURLConnection http_conn = (HttpURLConnection)connection;
            responseCode = http_conn.getResponseCode();

            BufferedReader reader;
            boolean passed = true;
            if(responseCode == HTTP_CODE_OK || responseCode == HTTP_CODE_CREATED) {
                reader = new BufferedReader(new InputStreamReader(http_conn.getInputStream()));
            }
            else {
                reader = new BufferedReader(new InputStreamReader(http_conn.getErrorStream()));
                passed = false;
            }
            String response = reader.readLine();

            if(DEBUG) System.out.println(response);

            reader.close();

            if(passed) return response;

            throw new Exception(response);
        }
        catch (Exception e) {throw e;}
    }

    private String getApiHost() {
        return (_environment == "sandbox") ? "https://api.sandbox.africastalking.com" : "https://api.africastalking.com";
    }

    private String getPaymentHost() {
        return (_environment == "sandbox") ? "https://payments.sandbox.africastalking.com" : "https://payments.africastalking.com";
    }

    private String getVoiceHost() {
        return (_environment == "sandbox") ? "https://voice.sandbox.africastalking.com" : "https://voice.africastalking.com";
    }

    private String getSmsUrl() {
        return getApiHost() + "/version1/messaging";
    }

    private String getVoiceUrl() {
        return getVoiceHost();
    }

    private String getSubscriptionUrl() {
        return getApiHost() + "/version1/subscription";
    }

    private String getUserDataUrl() {
        return getApiHost() + "/version1/user";
    }

    private String getAirtimeUrl() {
        return getApiHost() + "/version1/airtime";
    }

    private String getMobilePaymentCheckoutUrl() {
        return getPaymentHost() + "/mobile/checkout/request";
    }

    private String getMobilePaymentB2BUrl() {
        return getPaymentHost() + "/mobile/b2b/request";
    }

    private String getMobilePaymentB2CUrl() {
        return getPaymentHost() + "/mobile/b2c/request";
    }
}

class MobilePaymentB2CRecipient {
    private String _phoneNumber;
    private String _currencyCode;
    private Double _amount;
    private HashMap<String, String> _metadata;

    public MobilePaymentB2CRecipient(String phoneNumber_,
                                     String currencyCode_,
                                     Double amount_) {
        _phoneNumber  = phoneNumber_;
        _currencyCode = currencyCode_;
        _amount       = amount_;
        _metadata     = new HashMap<String, String>();
    }

    public void addMetadata(String key_, String  value_) {
        _metadata.put(key_, value_);
    }

    public JsonNode toJSON() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.createObjectNode();
        return  ((ObjectNode) node)

                .put("phoneNumber", _phoneNumber)
                .put("currencyCode", _currencyCode)
                .put("amount", _amount)
                .put("metadata", mapper.convertValue( _metadata, String.class ));
    }
}
