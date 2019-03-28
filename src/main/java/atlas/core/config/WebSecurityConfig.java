package atlas.core.config;

import atlas.web.usermanager.auth.AppAuthenticationProvider;
import atlas.web.usermanager.auth.handlers.AppLogoutHandler;
import atlas.web.usermanager.auth.handlers.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * This application uses this class to wrap up all security properties to be
 * used by this application.
 * <code>
 * It makes use of a custom authentication provider to achieve the following aspects according to PCI DSS:
 * a). Locks account after no more than 5 failed attempts
 * b). Sets the lockout duration to a minimum of 30 minutes( or as the system requirements detects) or
 *    until the administrator enables this account
 * c). Notifying the user during the executions of a). and b) above.
 * </code>
 *
 * @category    Security
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 *
 * Notes:
 * 1. Spring boot auto configured security policy has been dropped
 * 2. Spring boot method-level security has been turned on
 * 3. These properties should be updated every now and then to ensure security of this application
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AppAuthenticationProvider appAuthenticationProvider;

    private static final String[] SWAGGER_WHITELIST = {
            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/configuration/**",
            "/v2/api-docs",
            "/webjars/**"
    };

    /**
     * Customized security policy for this application
     * Notes:
     * 1. The paths listed in antMatchers() are granted unconditional access
     * 2. All other paths other than 1 will require authentication
     * 3. Spring security is configured to use form-based authentication
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                    .antMatchers( SWAGGER_WHITELIST ).permitAll()
                    .antMatchers("/password-reset/**", "/setup-account/*", "/img/**").permitAll()
                    .antMatchers("/resetpassword/*").permitAll()
                    .antMatchers("/plugins/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .successHandler(successHandler())
                    .failureUrl("/login?error")
                    .usernameParameter("email").passwordParameter("password").permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession( true )
                    .deleteCookies("JSESSIONID")
                    .addLogoutHandler( customLogoutHandler() )
                    .permitAll()
                    .and()
                .exceptionHandling()
//                    .accessDeniedPage("/_error/403")
                    .and()
                .csrf().and()
                .sessionManagement()
                    .invalidSessionUrl("/login").enableSessionUrlRewriting(false)
                    /*Concurrent session control*/
                    .maximumSessions(1)
                    /*Session timeout management*/
                    .expiredUrl("/login?expired");

    }

    /**
     * Serve static resources-css, js, images- without authentication
     *
     * @param websecurity
     */
    @Override
    public void configure(WebSecurity websecurity) {
//        Register application requests to be ignored by Spring Security
        websecurity.ignoring()
                .antMatchers("/css/**", "/plugins/**", "/layouts/**", "/img/**", "/js/**", "/fonts/**", "/api/**")
//                .antMatchers( SWAGGER_WHITELIST )
                .antMatchers("/theme/**");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(appAuthenticationProvider);
    }

    /**
     * Override default role prefix-ing(ROLE_somerole)
     *
     * @return DefaultRolesPrefixPostProcessor
     */
    @Bean
    public static DefaultRolesPrefixPostProcessor defaultRolesPrefixPostProcessor() {
        return new DefaultRolesPrefixPostProcessor();
    }

    /**
     * Custom handler to be called when a user has been successfully authenticated
     *
     * @return AuthenticationSuccessHandler
     */
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new LoginSuccessHandler();
    }

    /**
     *  Bean to enable auditing of user logouts events
     *
     * @return LogoutHandler
     */
    @Bean
    public LogoutHandler customLogoutHandler(){
        return new AppLogoutHandler();
    }

}
