package com.springboot.assetsphere;

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
            		
            		/*user*/
            		.requestMatchers("/api/user/signup").permitAll()
                    .requestMatchers("/api/user/getBy-username/{username}").hasAnyAuthority("HR", "EMPLOYEE", "IT_SUPPORT")
                    .requestMatchers("/api/user/getBy-Id/{id}").hasAuthority("HR")
                    .requestMatchers("/api/user/all").hasAuthority("HR")
                    .requestMatchers("/api/user/userInfo").authenticated()
                    .requestMatchers("/api/user/token").authenticated()
                    
                    /*employee*/
                    .requestMatchers("/api/employee/add").hasAuthority("HR")
                    .requestMatchers("/api/employee/getAll").hasAuthority("HR")
                    .requestMatchers("/api/employee/getBy-Id/{id}").hasAnyAuthority("HR", "EMPLOYEE")
                    .requestMatchers("/api/employee/by-username/{username}").hasAuthority("HR")
                    .requestMatchers("/api/employee/by-email/{email}").hasAuthority("HR")
                    .requestMatchers("/api/employee/by-jobtitle/{jobTitle}").hasAuthority("HR")
                    
                    /* assetCategory */
                    .requestMatchers("/api/assetcategory/add").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assetcategory/getAll").hasAnyAuthority("HR", "IT_SUPPORT", "EMPLOYEE")
                    .requestMatchers("/api/assetcategory/getById/{id}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assetcategory/getByName/{name}").hasAnyAuthority("HR", "IT_SUPPORT")
                    
                    /* asset */
                    .requestMatchers("/api/asset/add/{id}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/asset/getAll").hasAnyAuthority("HR", "IT_SUPPORT", "EMPLOYEE")
                    .requestMatchers("/api/asset/getBYId/{username}").hasAnyAuthority("HR", "IT_SUPPORT", "EMPLOYEE")
                    .requestMatchers("/api/asset/getByName/{assetName}").hasAnyAuthority("HR", "IT_SUPPORT", "EMPLOYEE")
                    .requestMatchers("/api/asset/getByModel/{model}").hasAnyAuthority("HR", "IT_SUPPORT", "EMPLOYEE")
                    .requestMatchers("/api/asset/getByValue/{assetValue}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/asset/getByExpiry/{expiryDate}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/asset/getByCategory/{categoryName}").hasAnyAuthority("HR", "IT_SUPPORT", "EMPLOYEE")
                    
                    /* assetAllocation */
                    .requestMatchers("/api/assetallocation/add/{employeeId}/{assetId}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assetallocation/getAll").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assetallocation/getByEmployee/{employeeId}").hasAnyAuthority("HR", "IT_SUPPORT", "EMPLOYEE")
                    .requestMatchers("/api/assetallocation/getByAllocationId/{assetId}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assetallocation/getByStatus/{status}").hasAnyAuthority("HR", "IT_SUPPORT")
                    
                    /* assetAudit */
                    .requestMatchers("/api/assetaudit/add/{employeeId}/{assetId}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assetaudit/getAll").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assetaudit/getByEmployee/{employeeId}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assetaudit/getByAsset/{assetId}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assetaudit/getByStatus/{status}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assetaudit/getById/{id}").hasAnyAuthority("HR", "IT_SUPPORT")
                    
                    /* assetRequest */
                    .requestMatchers("/api/assetrequest/add/{employeeId}/{assetId}").hasAnyAuthority("EMPLOYEE", "HR")
                    .requestMatchers("/api/assetrequest/getAll").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assetrequest/employee/{employeeId}").hasAnyAuthority("EMPLOYEE", "HR")
                    .requestMatchers("/api/assetrequest/asset/{assetId}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assetrequest/status/{status}").hasAnyAuthority("HR", "IT_SUPPORT")
                    
                    /* assetReturnrequest */
                    .requestMatchers("/api/assetreturnrequest/add/{employeeId}/{allocationId}").hasAnyAuthority("EMPLOYEE", "HR")
                    .requestMatchers("/api/assetreturnrequest/all").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assetreturnrequest/getByEmployee/{employeeId}").hasAnyAuthority("EMPLOYEE", "HR")
                    .requestMatchers("/api/assetreturnrequest/getByStatus/{status}").hasAnyAuthority("HR", "IT_SUPPORT")
                    
                    /* assetTracking */
                    .requestMatchers("/api/assettracking/add/{employeeId}/{assetId}").hasAnyAuthority("EMPLOYEE", "HR")
                    .requestMatchers("/api/assettracking/getAll").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assettracking/employee/{employeeId}").hasAnyAuthority("EMPLOYEE", "HR")
                    .requestMatchers("/api/assettracking/asset/{assetId}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assettracking/getByStatus/{status}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/assettracking/email/{email}").hasAuthority("HR")
                    .requestMatchers("/api/assettracking/username/{username}").hasAuthority("HR")
                    
                    /* liquidAssetrequest */
                    .requestMatchers("/api/liquidassetrequest/add/{employeeId}").hasAnyAuthority("EMPLOYEE", "HR")
                    .requestMatchers("/api/liquidassetrequest/getAll").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/liquidassetrequest/getByEmployee/{employeeId}").hasAnyAuthority("EMPLOYEE", "HR")
                    .requestMatchers("/api/liquidassetrequest/getByStatus/{status}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/liquidassetrequest/getEmail/{email}").hasAuthority("HR")
                    .requestMatchers("/api/liquidassetrequest/username/{username}").hasAuthority("HR")
                    
                    /* serviceRequest */
                    .requestMatchers("/api/servicerequest/submit/{employeeId}/{assetId}").hasAnyAuthority("EMPLOYEE", "HR")
                    .requestMatchers("/api/servicerequest/getAll").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/servicerequest/getByEmployee/{employeeId}").hasAnyAuthority("EMPLOYEE", "HR")
                    .requestMatchers("/api/servicerequest/getByAsset/{assetId}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/servicerequest/getByStatus/{status}").hasAnyAuthority("HR", "IT_SUPPORT")
                    .requestMatchers("/api/servicerequest/getByEmail/{email}").hasAuthority("HR")
                    .requestMatchers("/api/servicerequest/getByUsername/{username}").hasAuthority("HR")
                    
                    /* liquidAssetTransaction */
                    .requestMatchers("/api/liquidassettransaction/add/{employeeId}/{requestId}").hasAnyAuthority("HR") 
                    .requestMatchers("/api/liquidassettransaction/getAll") .hasAnyAuthority("HR", "IT_SUPPORT") 
                    .requestMatchers("/api/liquidassettransaction/getByEmployee/{employeeId}") .hasAnyAuthority("EMPLOYEE", "HR") // Employee can view their own; HR can view all
                    .requestMatchers("/api/liquidassettransaction/getByStatus/{status}").hasAnyAuthority("HR") // Only HR filters by status


                    
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
