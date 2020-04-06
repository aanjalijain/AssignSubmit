package com.uxpsystems.assignment.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter{
	
	
	
	  @Autowired
	    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
	        auth.inMemoryAuthentication().withUser("anjali").password("abc123").roles("ADMIN");
	        auth.inMemoryAuthentication().withUser("amit").password("abc123").roles("USER");
	    }
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.authorizeRequests().antMatchers("/", "/assignment/user/")
	                .access("hasRole('USER') or hasRole('ADMIN')")
	                .antMatchers("/assignment/user/**").access("hasRole('ADMIN')").and().exceptionHandling().accessDeniedPage("/Access_Denied");
	    }
}
