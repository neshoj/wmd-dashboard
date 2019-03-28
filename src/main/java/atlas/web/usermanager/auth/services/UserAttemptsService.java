package atlas.web.usermanager.auth.services;

import atlas.web.configs.entities.AppSettings;
import atlas.web.configs.repository.SettingsRepository;
import atlas.web.usermanager.entities.Users;
import atlas.web.usermanager.repository.UserRepository;
import atlas.web.usermanager.entities.UserAttempts;
import atlas.web.usermanager.repository.UserAttemptsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * User login attempts service
 *
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Service
@Transactional
public class UserAttemptsService implements UserAttemptsServiceInterface{

    private static final String INTERVAL_SECONDS = "seconds";
    private static final String INTERVAL_MINUTES = "minutes";
    private static final String INTERVAL_HOURS = "hours";
    private static final String INTERVAL_DAYS = "days";

    @Autowired
    private UserAttemptsRepository userAttemptsRepository;
    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public long updateFailedAttempts(String email) {

        long remainingRetries = 0;
        List<UserAttempts> attempts = fetchUserAttempts(email);
        Optional<Users> oUser = userRepository.findByEmail( email );
        boolean isUserExists = oUser.isPresent();

        /*Fetch allowed number of login retries*/
        Optional<AppSettings> oConfig = settingsRepository.findByCode( "LOGIN_TRIALS");

        //When the config is not found(worst scenario), return a default value
        if( !oConfig.isPresent() ) return 5;
        AppSettings config = oConfig.get();

        if (attempts.size() < 1) {
            if (isUserExists) {
                /*User has no record; insert a new one*/
                UserAttempts userAttempt = new UserAttempts();
                userAttempt.setAttempts(1L);
                userAttempt.setEmail(email);
                userAttempt.setLastmodified(new Date( System.currentTimeMillis() ));
                userAttemptsRepository.save( userAttempt );

                remainingRetries = Long.valueOf(config.getValue()) - 1;
            }
        } else {
            /*User has a failed attempt record*/
            UserAttempts userAttempt = attempts.get(0);
            long user_attempts = userAttempt.getAttempts();
            /*Increment attempts by 1*/
            user_attempts++;

            /*If number of retries exceed maximum retries, lock user account */
            if (user_attempts > Long.valueOf(config.getValue())) {
                /*Lock user account - AccountNonLocked value is this case should be "false"*/
                Users currentUser = oUser.get();
                currentUser.setNonlocked( false );
                userRepository.save( currentUser );

            } else {
                remainingRetries = Long.valueOf(config.getValue()) - user_attempts;
                /*Update number of retries*/
                userAttempt.setAttempts( user_attempts );
                userAttempt.setLastmodified(new Date(System.currentTimeMillis()));
                userAttemptsRepository.save( userAttempt );
            }
        }
        return remainingRetries;
    }

    /**
     * Sets number of login attempts of a user to zero
     *
     * @param email Email(or username) of user accessing the system
     */
    @Override
    public void resetFailedAttempts(String email) {
        List<UserAttempts> attempts = fetchUserAttempts(email);
        if (attempts.size() > 0) {
            /*Update number of retries*/
            UserAttempts userAttempt = attempts.get(0);
            userAttempt.setAttempts(0L);
            userAttempt.setLastmodified(null);
            userAttemptsRepository.save( userAttempt );

            /*Always activate account*/
            Optional<Users> oUser = userRepository.findByEmail( email );

            if( oUser.isPresent() ){
                Users currentUser = oUser.get();
                currentUser.setNonlocked( true );
                userRepository.save( currentUser );
            }
        }
    }

    /**
     * Fetch the number of retries exhausted by a user
     *
     * @param email Email(or username) of user accessing the system
     * @return Login attempts
     */
    @Override
    public List<UserAttempts> fetchUserAttempts(String email) {
        return userAttemptsRepository.findByEmail( email );
    }

    @Override
    public String processLockedAccount(String email) {
        List<UserAttempts> attempts = fetchUserAttempts(email);
        StringBuilder error = new StringBuilder();

        if (attempts.size() > 0) {
            /*Get elapsed time*/
            UserAttempts UserAttempts = attempts.get(0);
            Date lastAttempts = UserAttempts.getLastmodified();
            Date timestamp = new Date(System.currentTimeMillis());

            /*Fetch account lock out duration*/
            Optional<AppSettings> oConfig = settingsRepository.findByCode( "LOCK_OUT_PERIOD");

            //When the config is not found(worst scenario), unlock account
            if( !oConfig.isPresent() ) {
                System.err.println("========================================================");
                System.err.println("Unlock account");
                System.err.println("========================================================");
            }
            AppSettings config = oConfig.get();

            int lockOutPeriod = Integer.valueOf( config.getValue() );
            int elapsedMinutes = timeIntervalInCustomPeriod(lastAttempts, timestamp, INTERVAL_MINUTES);
            int nextRetry = lockOutPeriod - elapsedMinutes;

            if (nextRetry == 0) {
                /*Difference is in seconds*/
                /*Update the last attempt with the config value*/
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(lastAttempts);
                Date updatedLastAttempt = new Date(calendar.getTimeInMillis() + (lockOutPeriod * 60000));
                nextRetry = timeIntervalInCustomPeriod(updatedLastAttempt, timestamp, INTERVAL_SECONDS);
                error.append("Sorry! You account has been locked! Please try again after ")
                        .append(nextRetry)
                        .append(" seconds");
            } else {
                /*Difference is in minutes*/
                error.append("Sorry! You account has been locked! Please try again after ")
                        .append(nextRetry)
                        .append(" minutes");
            }

        } else {
            /*Check if this account exists*/
            Optional<Users> oUser = userRepository.findByEmail( email );
            boolean userExists = oUser.isPresent();
            if ( userExists ) {
                error.append(" Sorry! Your account has been locked!");
            } else {
                error.append("Sorry! You've entered an invalid email or password");
            }
        }
        return error.toString();
    }

    public boolean isAccountNonLocked(String email) {
        System.err.println(" email ->" + email);
        //Fetch login attempts
        List<UserAttempts> attempts = fetchUserAttempts(email);
        System.err.println(" attempts =>" + attempts.size() );

        boolean isAccountNonLocked = false;
        /*Get elapsed time*/
        if (attempts.size() > 0) {
            UserAttempts userAttempts = attempts.get(0);
            if ( userAttempts.getLastmodified() != null && userAttempts.getAttempts() > 0) {

                Optional<Users> oUser = userRepository.findByEmail( email );
                if( oUser.isPresent() ){

                    Users currentUser = oUser.get();

                    if ( !currentUser.getNonlocked() ) {
                        Date lastAttempts = userAttempts.getLastmodified();
                        Date timestamp = new Date(System.currentTimeMillis());

                        /*Fetch account lock out duration*/
                        Optional<AppSettings> oConfig = settingsRepository.findByCode( "LOCK_OUT_PERIOD");

                        //When the config is not found(worst scenario), unlock account
                        if( !oConfig.isPresent() ) {
                            System.err.println("========================================================");
                            System.err.println("Do not lock account");
                            System.err.println("========================================================");
                        }
                        AppSettings config = oConfig.get();

                        int lockOutPeriod = Integer.valueOf( config.getValue() );
                        System.err.println("lockOutPeriod ->" + lockOutPeriod);

                        int elapsedDays = timeIntervalInCustomPeriod(lastAttempts, timestamp, INTERVAL_DAYS);
                        int elapsedHours = timeIntervalInCustomPeriod(lastAttempts, timestamp, INTERVAL_HOURS);
                        int elapsedMinutes = timeIntervalInCustomPeriod(lastAttempts, timestamp, INTERVAL_MINUTES);
                        //                    int elapseSeconds = QuickpayFunctions.timeIntervalInCustomPeriod(lastAttempts, timestamp, AppConstants.INTERVAL_SECONDS);

                        if (elapsedDays > 0 || elapsedHours > 0 || elapsedMinutes >= lockOutPeriod) {
                            /*Activate account: AccountNonLocked value is this case should be "true"*/
                            currentUser.setNonlocked(  true );
                            /*Update user object*/
                            userRepository.save( currentUser );

                            /*Set attempt status to default*/
                            userAttempts.setAttempts( 0L );
                            userAttempts.setLastmodified(null);
                            userAttemptsRepository.save( userAttempts );

                            isAccountNonLocked = true;
                        }
                    } else {
                        isAccountNonLocked = true;
                    }
                }

            } else {
                isAccountNonLocked = true;
            }
        } else {
            isAccountNonLocked = true;
        }

        return isAccountNonLocked;
    }

    private static int timeIntervalInCustomPeriod(Date startDate, Date endDate, String timeperiod) {
        Instant start = Instant.ofEpochMilli( startDate.getTime() );
        Instant end = Instant.ofEpochMilli( endDate.getTime() );

        long timeInterval = 0;
        switch (timeperiod) {
            case INTERVAL_MINUTES:
                timeInterval = ChronoUnit.MINUTES.between( start, end );
                break;
            case INTERVAL_SECONDS:
                timeInterval = ChronoUnit.SECONDS.between( start, end );
                break;
            case INTERVAL_HOURS:
                timeInterval = ChronoUnit.HOURS.between( start, end );
                break;
            case INTERVAL_DAYS:
                timeInterval = ChronoUnit.DAYS.between( start, end );
                break;
        }

        return Math.toIntExact( timeInterval );
    }

}
