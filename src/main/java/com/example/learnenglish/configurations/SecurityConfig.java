package com.example.learnenglish.configurations;

import com.example.learnenglish.service.CustomUserDetailsService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
//    @Autowired
//    private FilterRegistrationBean<CustomRequestLoggingFilter> customRequestLoggingFilter;


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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    public static final String[] ENDPOINTS_WHITELIST = {
            "/css/**",
            "/js/**",
            "/images/**",
            "/",
            "/login",
//            "/tiny",
//            "/lessons/**",
            "/about-the-app",
            "/registration"

    };
    public static final String LOGIN_URL = "/login";
    //    public static final String LOGOUT_URL = "/logout";
    public static final String LOGIN_FAIL_URL = LOGIN_URL + "?error";
    public static final String DEFAULT_SUCCESS_URL = "/about-the-app";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse
        //        http.csrf().disable();
        http.authorizeRequests(request ->
                        request.requestMatchers(ENDPOINTS_WHITELIST).permitAll()
//                                .requestMatchers("/admin/**").hasRole("ADMIN")
//                                .requestMatchers("/user/**").hasRole("USER")
                                .anyRequest()
                                .authenticated()
                                .and()
                                .addFilterBefore(customRequestLoggingFilter(), UsernamePasswordAuthenticationFilter.class)
//                                .antMatchers("/api/test/**").addFilterBefore(customRequestLoggingFilter.getFilter(), BasicAuthenticationFilter.class)
                )
                .formLogin(form -> form
                                .loginPage(LOGIN_URL)
                                .loginProcessingUrl(LOGIN_URL)
                                .failureUrl(LOGIN_FAIL_URL)
                                .usernameParameter(USERNAME)
                                .passwordParameter(PASSWORD)
//                        .defaultSuccessUrl(DEFAULT_SUCCESS_URL)
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
//    @Lazy
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

//    @Bean
//    public FilterRegistrationBean<CustomRequestLoggingFilter> customLoggingFilter() {
//        FilterRegistrationBean<CustomRequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new CustomRequestLoggingFilter());
//        registrationBean.setUrlPatterns(Arrays.asList("/user/*/lesson/*"));
//        return registrationBean;
//    }
//    @Bean
//    public CommonsRequestLoggingFilter requestLoggingFilter() {
//        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
//        filter.setIncludeQueryString(true);
//        filter.setIncludePayload(true);
//        filter.setMaxPayloadLength(10000);
//        filter.setIncludeHeaders(false);
//        filter.setBeforeMessagePrefix("REQUEST : [");
//        filter.setAfterMessagePrefix("RESPONSE : [");
//        return filter;
//    }


}