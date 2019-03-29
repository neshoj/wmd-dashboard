/*
 * Copyright 2015 anthony.
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
package opdwms.core.template;

/**
 * @category    Constants
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public final class AppConstants {

    /*Maximum no of records to be fetched from the db*/
    public static final int MAX_RECORDS = 100;

    /*Bean status*/
    public static final String STATUS_NEWRECORD = "0";
    public static final String STATUS_ACTIVERECORD = "1";
    public static final String STATUS_EDITEDRECORD = "2";
    public static final String STATUS_DEACTIVATED = "3";
    public static final String STATUS_INACTIVERECORD = "4";
    public static final String STATUS_DECLINED_REQUEST = "5";
    public static final String STATUS_SOFTDELETED = "6";

    /*Global CRUD processing actions*/
    public static final String ACTION_EDIT = "edit";
    public static final String ACTION_APRROVE_NEW = "approve-new";
    public static final String ACTION_DECLINE_NEW = "decline-new";
    public static final String ACTION_DEACTIVATE = "deactivate";
    public static final String ACTION_VIEW_EDITCHANGES = "vedit";
    public static final String ACTION_APRROVE_EDIT = "approve-edit";
    public static final String ACTION_DECLINE_EDIT = "decline-edit";
    public static final String ACTION_VIEW_DEACTIVATION_REASON = "vdeactivation";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_ACTIVATE = "activate";
    public static final String ACTION_APPROVE_DEACTIVATION = "approve-deactivation";
    public static final String ACTION_DECLINE_DEACTIVATION = "decline-deactivation";
    public static final String ACTION_DECLINE_REQUEST="decline-request";
    public static final String ACTION_SOFT_DELETE="soft-delete";

//    Time Period
    public static final String INTERVAL_SECONDS = "seconds";
    public static final String INTERVAL_MINUTES = "minutes";
    public static final String INTERVAL_HOURS = "hours";
    public static final String INTERVAL_DAYS = "days";

    /*Prevent the caller of this class from instantiating this class:
     This enables the caller to reference the constants using AppConstants.CONSTANT
     */
    private AppConstants() {
        /*this prevents even the native class from calling this actor as well */
        throw new AssertionError();
    }

}
