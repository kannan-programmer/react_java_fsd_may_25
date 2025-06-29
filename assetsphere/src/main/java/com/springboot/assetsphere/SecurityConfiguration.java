package com.springboot.assetsphere;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    	
        http
        
        .csrf (csrf -> csrf.disable())
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        

                // User
            	
                .requestMatchers("/api/user/signup").permitAll()
                .requestMatchers("/api/user/getBy-username/{username}").hasAnyAuthority("HR", "EMPLOYEE", "IT_SUPPORT")
                .requestMatchers("/api/user/getBy-Id/{id}").hasAuthority("HR")
                .requestMatchers("/api/user/all").hasAuthority("HR")
                .requestMatchers("/api/user/userInfo").authenticated()
                .requestMatchers("/api/user/token").authenticated()
                
                // Employee
                .requestMatchers("/api/employee/add").hasAuthority("HR")
                .requestMatchers("/api/employee/getAll").hasAuthority("HR")
                .requestMatchers("/api/employee/getBy-Id/{id}").permitAll()
                .requestMatchers("/api/employee/by-username/{username}").permitAll()
                .requestMatchers("/api/employee/by-jobtitle/{jobTitle}").hasAuthority("HR")
                
                // HR
                .requestMatchers("/api/hr/add").hasAuthority("HR")
                .requestMatchers("/api/hr/all").hasAuthority("HR")
                .requestMatchers("/api/hr/username/{username}").hasAuthority("HR")
                .requestMatchers("/api/hr/id/{id}").hasAuthority("HR")
                .requestMatchers("/api/hr/upload-image/profilepic").permitAll()
                
                // IT Support
                .requestMatchers("/api/itsupport/add").hasAuthority("HR")
                .requestMatchers("/api/itsupport/all").hasAuthority("HR")
                .requestMatchers("/api/itsupport/username/{username}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/itsupport/id/{id}").hasAnyAuthority("HR", "IT_SUPPORT")
                
                // AssetCategory
                .requestMatchers("/api/assetcategory/add").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assetcategory/getAll").permitAll()
                .requestMatchers("/api/assetcategory/getById/{id}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assetcategory/getByName/{name}").hasAnyAuthority("HR", "IT_SUPPORT")
                
                // Asset
                .requestMatchers("/api/asset/add/{id}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/asset/add-batch/{categoryId}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/asset/getAll").permitAll()
                .requestMatchers("/api/asset/getBYId/{username}").hasAnyAuthority("HR", "IT_SUPPORT", "EMPLOYEE")
                .requestMatchers("/api/asset/getByName/{assetName}").permitAll()
                .requestMatchers("/api/asset/getByModel/{model}").permitAll()
                .requestMatchers("/api/asset/getByValue/{assetValue}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/asset/getByExpiry/{expiryDate}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/asset/getByExpiry/{expiryDate}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/asset/getByCategory/{categoryName}").permitAll()
                .requestMatchers("/api/asset/getByStatus/{status}").permitAll()
                // AssetAllocation
                .requestMatchers("/api/assetallocation/add/{employeeId}/{assetId}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assetallocation/getAll").permitAll()
                .requestMatchers("/api/assetallocation/getByEmployee/{employeeId}").permitAll()
                .requestMatchers("/api/assetallocation/getByAllocationId/{assetId}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assetallocation/getByStatus/{status}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assetallocation/getByAssetId/{assetId}").permitAll()
                .requestMatchers("/api/assetallocation/getByUsername/{username}").hasAnyAuthority("HR", "IT_SUPPORT", "EMPLOYEE")
                
                // AssetAudit
                .requestMatchers("/api/assetaudit/add/{employeeId}/{assetId}").permitAll()
                .requestMatchers("/api/assetaudit/getAll").permitAll()
                .requestMatchers("/api/assetaudit/getByEmployee/{employeeId}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assetaudit/getByAsset/{assetId}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assetaudit/getByStatus/{status}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assetaudit/getById/{id}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assetaudit/getByUsername/{username}").permitAll()
                .requestMatchers("/api/assetaudit/update/{id}").permitAll()
                .requestMatchers("/api/assetaudit/delete/{id}").permitAll()
                
                
                // AssetRequest
                .requestMatchers("/api/assetrequest/add/{employeeId}/{assetId}").hasAnyAuthority("EMPLOYEE", "HR")
                .requestMatchers("/api/assetrequest/getAll").permitAll()
                .requestMatchers("/api/assetrequest/employee/{employeeId}").hasAnyAuthority("EMPLOYEE", "HR")
                .requestMatchers("/api/assetrequest/asset/{assetId}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assetrequest/status/{status}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assetrequest/username/{username}").hasAnyAuthority("HR", "IT_SUPPORT", "EMPLOYEE")
                
                // AssetReturnrequest
                .requestMatchers("/api/assetreturnrequest/add/{employeeId}/{allocationId}").permitAll()
                .requestMatchers("/api/assetreturnrequest/all").permitAll()
                .requestMatchers("/api/assetreturnrequest/getByEmployee/{employeeId}").hasAnyAuthority("EMPLOYEE", "HR")
                .requestMatchers("/api/assetreturnrequest/getByStatus/{status}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assetreturnrequest/update/{requestId}").permitAll()
                
                
                // AssetTracking
                .requestMatchers("/api/assettracking/add/{employeeId}/{assetId}").permitAll()
                .requestMatchers("/api/assettracking/getAll").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assettracking/employee/{employeeId}").hasAnyAuthority("EMPLOYEE", "HR")
                .requestMatchers("/api/assettracking/asset/{assetId}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assettracking/getByStatus/{status}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/assettracking/email/{email}").hasAuthority("HR")
                .requestMatchers("/api/assettracking/username/{username}").hasAuthority("HR")
                
                // LiquidAssetrequest
                .requestMatchers("/api/liquidassetrequest/add/{employeeId}").hasAnyAuthority("EMPLOYEE", "HR")
                .requestMatchers("/api/liquidassetrequest/getAll").permitAll()
                .requestMatchers("/api/liquidassetrequest/getByEmployee/{employeeId}").hasAnyAuthority("EMPLOYEE", "HR")
                .requestMatchers("/api/liquidassetrequest/getByStatus/{status}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/liquidassetrequest/getEmail/{email}").hasAuthority("HR")
                .requestMatchers("/api/liquidassetrequest/getNmae/{name}").hasAuthority("HR")
                .requestMatchers("/api/liquidassetrequest/username/{username}").hasAnyAuthority("HR", "EMPLOYEE")
                .requestMatchers("/api/liquidassetrequest/update/{username}").permitAll()
                // ServiceRequest
                .requestMatchers("/api/servicerequest/submit/{employeeId}/{assetId}").hasAnyAuthority("EMPLOYEE", "HR")
                .requestMatchers("/api/servicerequest/getAll").permitAll()
                .requestMatchers("/api/servicerequest/getByEmployee/{employeeId}").hasAnyAuthority("EMPLOYEE", "HR")
                .requestMatchers("/api/servicerequest/getByAsset/{assetId}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/servicerequest/getByStatus/{status}").hasAnyAuthority("HR", "IT_SUPPORT")
                .requestMatchers("/api/servicerequest/getByEmail/{email}").hasAuthority("HR")
                .requestMatchers("/api/servicerequest/getByName/{name}").hasAuthority("HR")
                .requestMatchers("/api/servicerequest/getByUsername/{username}").hasAnyAuthority("HR", "EMPLOYEE")
                .requestMatchers("/api/servicerequest/update/{username}").permitAll()
                .requestMatchers("/api/hr/upload-image/profilepic").permitAll()
                
                // LiquidAssetTransaction
                .requestMatchers("/api/liquidassettransaction/add/{employeeId}/{requestId}").hasAnyAuthority("HR") 
                .requestMatchers("/api/liquidassettransaction/getAll").permitAll()
                .requestMatchers("/api/liquidassettransaction/getByEmployee/{employeeId}").hasAnyAuthority("EMPLOYEE", "HR")
                .requestMatchers("/api/liquidassettransaction/getByStatus/{status}").hasAnyAuthority("HR")
                .requestMatchers("/api/liquidassettransaction/update/{username}").permitAll()

                
                
                
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