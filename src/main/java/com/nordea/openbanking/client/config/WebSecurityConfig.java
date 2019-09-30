package com.nordea.openbanking.client.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String HOME_URI = "/home";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/authorize/login")
                .defaultSuccessUrl(HOME_URI)
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/actuator/**",
                        "/error",
                        "/",
                        "/favicon.ico",
                        HOME_URI,
                        "/public/**",
                        "/authorize/**"
                ).permitAll()
                .anyRequest().authenticated();
        http.logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl(HOME_URI);
        http.csrf().disable(); // Dont run this in production :)
    }

}