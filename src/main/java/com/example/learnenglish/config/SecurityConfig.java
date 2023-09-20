package com.example.learnenglish.config;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
//         jsr250Enabled = true,
        prePostEnabled = true)
@ComponentScan(basePackages = "com.example.learnenglish")
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;


    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    public static final String[] ENDPOINTS_WHITELIST = {
            "/css/*",
            "/js/*",
            "/images/*",
            "/img/*",
            "/web-image/**",
            "favicon.ico",
            "/robots.txt",
            "/",
            "/login",
            "/activate/*",
            "/forgot-password",
            "/forgot-password/*",
            "/about-the-app",
            "/registration",
            "/registration-page",
            "/fragmentsPages/successRegistrationFragment",
            "/words-main-category",
            "/words-main-category/*",
            "/subcategory/*",
            "/word/*",
            "/phrases-categories",
            "/phrases-category/**",
            "/phrases-page**",
            "/phrases-page/**",
            "/search-word**",
            "/audio/*",
            "/word-image/*",
            "/login-page*",
            "/word/training",
            "/category-image/**",
            "/lessons",
            "/pay",
            "/start-pay*",
            "/payment-success*",
            "/api/pay-success/*"
    };
    public static final String LOGIN_URL = "/login";
    //    public static final String LOGOUT_URL = "/logout";
    public static final String LOGIN_FAIL_URL = LOGIN_URL + "?error";
    public static final String DEFAULT_SUCCESS_URL = "/about-the-app";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/api/pay-success/*")
                )

                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                                .requestMatchers(ENDPOINTS_WHITELIST).permitAll()
                                .anyRequest().authenticated()
                )

                .addFilterBefore(customRequestLoggingFilter(), UsernamePasswordAuthenticationFilter.class)

                .sessionManagement(session -> session
//                                .invalidSessionStrategy(new MyCustomInvalidSessionStrategy())
                                .maximumSessions(1)
                                .sessionRegistry(sessionRegistry()) // Додайте цей рядок
                                .expiredSessionStrategy(new MySessionInformationExpiredStrategy())
//                                .maxSessionsPreventsLogin(true)
                )



                .formLogin(form -> form
                                .loginPage(LOGIN_URL)
                                .loginProcessingUrl(LOGIN_URL)
                                .failureUrl(LOGIN_FAIL_URL)
                                .usernameParameter(USERNAME)
                                .passwordParameter(PASSWORD)
//                                .defaultSuccessUrl(DEFAULT_SUCCESS_URL)
                                .successHandler((request, response, authentication) -> {
                                    HttpSession session = request.getSession();
                                    session.setAttribute("authorities", authentication.getAuthorities());
                                    User user = (User) authentication.getPrincipal();
                                    session.setAttribute("avatarName", user.getUserAvatar().getImageName());
                                    session.setAttribute("userDateOfCreated", user.getDateOfCreated());
                                    session.setAttribute("userGender", user.getGender().toString());
                                    session.setAttribute("userId", user.getId());
                                    session.setAttribute("userTextInLesson", user.isUserPhrasesInLesson());

                                    response.sendRedirect(DEFAULT_SUCCESS_URL);
                                })
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl(LOGIN_URL + "?logout"))
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.permitAll());
        return http.build();
    }

    @Bean
    public CustomRequestLoggingFilter customRequestLoggingFilter() {
        CustomRequestLoggingFilter filter = new CustomRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setBeforeMessagePrefix("REQUEST : ");
        filter.setAfterMessagePrefix("RESPONSE : ");
        return filter;
    }


}