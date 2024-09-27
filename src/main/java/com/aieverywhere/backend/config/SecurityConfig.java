package com.aieverywhere.backend.config;

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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aieverywhere.backend.services.UsersServices;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
private final UsersServices usersServices;
private final JwtUtils jwtUtils;
    
    @Autowired
    public SecurityConfig (UsersServices usersServices, JwtUtils jwtUtils) {
    	this.usersServices = usersServices;
    	this.jwtUtils = jwtUtils;

    }

	
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(customizer -> customizer.disable())
            .authorizeHttpRequests(authorize -> authorize
            		.requestMatchers("/**").permitAll()	
//	              .requestMatchers("/pages/**","/punch_in").permitAll()	
//                .requestMatchers("/api/login").permitAll()
//                .requestMatchers("/api/users").hasRole("ADMIN")
//                .requestMatchers("/api/products", "/api/orders", "/api/users/**","/api/orderitem").hasAnyRole("SELLER", "ADMIN")
                .anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(new JwtAuthFilter(jwtUtils, usersServices), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(usersServices);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authenticationProvider;
    }

    
    

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
