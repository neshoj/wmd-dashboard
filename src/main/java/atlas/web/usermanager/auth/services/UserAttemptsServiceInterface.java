package atlas.web.usermanager.auth.services;


import atlas.web.usermanager.entities.UserAttempts;

import java.util.List;

/**
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public interface UserAttemptsServiceInterface {

    public long updateFailedAttempts(String email);

    /**
     * Sets number of login attempts of a user to zero
     *
     * @param email Email(or username) of user accessing the system
     */
    public void resetFailedAttempts(String email);

    /**
     * Fetch the number of retries exhausted by a user
     *
     * @param email Email(or username) of user accessing the system
     * @return Login attempts
     */
    public List<UserAttempts> fetchUserAttempts(String email);

    public String processLockedAccount(String email);
}
