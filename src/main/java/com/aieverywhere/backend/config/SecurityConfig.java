package com.aieverywhere.backend.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import static org.springframework.security.config.Customizer.withDefaults;
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

	private UsersServices usersServices;

	@Autowired
	public SecurityConfig(UsersServices usersServices) {
		this.usersServices = usersServices;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(withDefaults()).csrf(customizer -> customizer.disable())
				.authorizeHttpRequests(authorize -> authorize
						 .requestMatchers("/api/auth/login","/api/auth/signup", "/images/**").permitAll()
						// .requestMatchers("/**").permitAll()
						// .requestMatchers("/api/**").permitAll()
						// .requestMatchers("/api/login").permitAll()
						// .requestMatchers("/api/users").hasRole("ADMIN")
						// .requestMatchers("/api/products", "/api/orders",
						// "/api/users/**","/api/orderitem").hasAnyRole("SELLER", "ADMIN")
						.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

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

	@Bean
	public JwtUtils jwtUtils() {
		return new JwtUtils();
	}

	@Bean
	public JwtAuthFilter jwtAuthFilter() {
		return new JwtAuthFilter(jwtUtils(), usersServices);
	}

	

}
	


