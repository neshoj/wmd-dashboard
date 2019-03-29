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
package opdwms.web.usermanager.auth.exceptions;

/**
 * This class will handle all exceptions resulting from inactive accounts
 *
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public class InactiveAccountException extends UserAccountStatusException{
    
    public InactiveAccountException(String msg) {
        super(msg);
    }
    
    public InactiveAccountException(String msg, Throwable t) {
        super(msg, t);
    }
    
}
