package com.antilamer.thingTracker.config;

import com.antilamer.thingTracker.security.JwtAuthenticationEntryPoint;
import com.antilamer.thingTracker.security.JwtAuthenticationFilter;
import com.antilamer.thingTracker.security.JwtTokenProvider;
import com.antilamer.thingTracker.security.oauth2.CustomOAuth2UserService;
import com.antilamer.thingTracker.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.antilamer.thingTracker.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.antilamer.thingTracker.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.antilamer.thingTracker.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    private final JwtTokenProvider tokenProvider;

    private final UserAuthService customUserDetailsService;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, @Qualifier("userAuthService") UserDetailsService userDetailsService) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /*
      By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
      the authorization request. But, since our service is stateless, we can't save it in
      the session. We'll save the request in a Base64 encoded cookie instead.
    */
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, customUserDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().
                antMatchers(HttpMethod.OPTIONS, "/**").
                antMatchers("/").
                antMatchers("/*.{js,html}").
                antMatchers("/img/**").
                antMatchers("/fonts/**").
                antMatchers("/css/**").
                antMatchers("/404.html").
                antMatchers("/js/**").
                antMatchers("/node_modules/**").
                antMatchers("/**/*.{js,html,css}");

        web.ignoring().antMatchers( "/", "/resources/**", "/index.*", "/login.html","/favicon.ico",
                "/template/**", "/assets", "/assets/**", "/node_modules", "/node_modules/**", "/dist", "/dist/**",
                "/*.ttf", "/*.woff2", "/login", "/registration", "/main", "/main/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/oauth2/**").permitAll()
                .antMatchers("/api/authentication/**").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/user/getUser/{id}").permitAll()
                .antMatchers("/index").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/api/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/login/oauth2/code/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);
        http.csrf().disable();
        http.cors().disable();

        // Add custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}