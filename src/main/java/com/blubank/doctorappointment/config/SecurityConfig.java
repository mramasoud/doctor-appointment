package com.blubank.doctorappointment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/api/v1/doctor/**")
                                .hasAuthority("doctor")
                                .antMatchers("/api/v1/patient/appointments/**")
                                .hasAuthority("patient")
                                .antMatchers("/**/")
                                .permitAll()
                                .anyRequest().authenticated()
                ).httpBasic()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable();
        return http.build();
    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication().withUser("doctor").password("{noop}masoud").authorities("doctor");
        auth.inMemoryAuthentication().withUser("patient").password("{noop}masoud").authorities("patient");
    }


}