package com.nordcomet.pflio.chart;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();

        if (!Boolean.valueOf(System.getenv("SECURITY_DISABLED"))) {
            httpSecurity.authorizeRequests()
                    .anyRequest().authenticated()
                    .and().httpBasic();
        }

    }
}