package com.antilamer.thingTracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final AccessDeniedHandler accessDeniedHandler;

    @Autowired
    public WebSecurityConfig(
            @Qualifier("customAuthenticationEntryPoint") AuthenticationEntryPoint authenticationEntryPoint,
            AccessDeniedHandler accessDeniedHandler) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, @Qualifier("userAuthService") UserDetailsService userDetailsService) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
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
                "/*.ttf", "/*.woff2", "/login", "/registration");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/authentication/**").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/user/getUser/{id}").permitAll()
                .antMatchers("/index").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        http.csrf().disable();
    }
}