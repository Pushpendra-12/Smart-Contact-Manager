package com.contactManager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfiguration  {
	
	
	
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> {
			try {
				requests
				        .requestMatchers("/admin/**")
				        .hasRole("ADMIN")
				        .requestMatchers("/user/**")
				        .hasRole("USER")
				        .requestMatchers("/**")
				        .permitAll()
				        .and()
				        .formLogin()
				        .loginPage("/signin")
				        .loginProcessingUrl("/dologin")
				        .defaultSuccessUrl("/user/index")
				        .and()
				        .csrf().disable();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		} );
                
                
		return http.build();
    }
    
    @Bean
    public UserDetailsService getUserDetailService() {
        return new UserDetailsServiceImpl();
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    
    
}
