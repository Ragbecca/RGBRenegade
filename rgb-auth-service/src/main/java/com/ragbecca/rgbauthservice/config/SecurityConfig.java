package com.ragbecca.rgbauthservice.config;

import com.ragbecca.rgbauthservice.security.RGBUserDetailsService;
import com.ragbecca.rgbauthservice.security.TokenAuthenticationFilter;
import com.ragbecca.rgbauthservice.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.ragbecca.rgbauthservice.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.ragbecca.rgbauthservice.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.ragbecca.rgbauthservice.security.oauth2.RGBOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private RGBUserDetailsService rgbUserDetailsService;
    @Autowired
    private RGBOAuth2UserService rgbOAuth2UserService;
    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(rgbUserDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(daoAuthenticationProvider());

        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
        http.formLogin().disable();
        http.httpBasic().disable();
        http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        http.authorizeHttpRequests()
                .requestMatchers("/", "/error", "/img/*.png", "/favicon.ico").permitAll()
                .requestMatchers("/auth/**", "/oauth2/**").permitAll()
                .requestMatchers("/test/**").permitAll()
                .anyRequest().authenticated();
        http.oauth2Login().authorizationEndpoint()
                .baseUri("/oauth2/authorize").authorizationRequestRepository(cookieAuthorizationRequestRepository()).and()
                .redirectionEndpoint().baseUri("/oauth2/callback/*")
                .and().userInfoEndpoint().userService(rgbOAuth2UserService)
                .and().successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
