package com.springboot.codingchallenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import jakarta.servlet.http.HttpServletResponse;


@Configuration
public class SecurityConfiguration {

	@Autowired
	private JwtFilter jwtFilter;
	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            		
            		/* User APIs */
                    .requestMatchers("/api/user/signup").permitAll()
                    .requestMatchers("/api/user/token").authenticated()
                    .requestMatchers("/api/user/getByUsername/{username}").authenticated()

                    /* Patient APIs */
                    .requestMatchers("/api/patient/register/{userId}").hasAuthority("PATIENT")
                    .requestMatchers("/api/patient/appointment/{patientId}/{doctorId}").permitAll()
                    
                    .requestMatchers("/api/patient/doctor/{doctorId}").permitAll()
                    .requestMatchers("/api/patient/getpateintbyId/{id}").authenticated()
                    .requestMatchers("/api/patient/getBySpeciality/{speciality}").permitAll()

                    /* Doctor APIs */
                    .requestMatchers("/api/doctor/add/{userid}").hasAuthority("DOCTOR")
                    .requestMatchers("/api/doctor/get/{id}").authenticated()
                    .requestMatchers("/api/doctor/getAll").authenticated()
                    .requestMatchers("/api/doctor/getBySpecialization/{speciality}").authenticated()

                    /* medicalhistory APIs */
                    .requestMatchers("/api/medicalhistory/add/{patientId}").hasAuthority("PATIENT")
                    .requestMatchers("/api/medicalhistory/getByPatientId/{patientId}").authenticated()

                    
                    
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"Msg\": \"Access Denied\"}");
                })
            ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) 
   		 .httpBasic(Customizer.withDefaults()); 
    		return http.build(); 
    }
	
	@Bean
	PasswordEncoder passwordEncoder() {     
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager getAuthManager(AuthenticationConfiguration auth) 
			throws Exception {
		  return auth.getAuthenticationManager();
	 }
}
