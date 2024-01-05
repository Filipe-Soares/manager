package com.manager.config;

//import static com.alibou.security.user.Permission.ADMIN_CREATE;
//import static com.alibou.security.user.Permission.ADMIN_DELETE;
//import static com.alibou.security.user.Permission.ADMIN_READ;
//import static com.alibou.security.user.Permission.ADMIN_UPDATE;
//import static com.alibou.security.user.Permission.MANAGER_CREATE;
//import static com.alibou.security.user.Permission.MANAGER_DELETE;
//import static com.alibou.security.user.Permission.MANAGER_READ;
//import static com.alibou.security.user.Permission.MANAGER_UPDATE;
//import static com.alibou.security.user.Role.ADMIN;
//import static com.alibou.security.user.Role.MANAGER;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.manager.security.JwtAuthenticationFilter;
import com.manager.services.JwtService;
import com.manager.services.impl.SecurityUserDetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration  {
	
	private final SecurityUserDetailsService userDetailsService;
	private final JwtService jwtService;
	
	@Bean
	PasswordEncoder passWordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/api/usuarios/autenticar",
            "/api/usuarios"};
	
   @Bean
   JwtAuthenticationFilter jwtAuthenticationFilter() {
	   return new JwtAuthenticationFilter(jwtService, userDetailsService);
   }
    
   @Bean
   SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(req ->
                req.requestMatchers(WHITE_LIST_URL)
                        .permitAll()
//                        .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
//                        .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
//                        .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
//                        .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
//                        .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())
                        .anyRequest()
                        .authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
//        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//        .logout(logout ->
//                logout.logoutUrl("/api/v1/auth/logout")
//                        .addLogoutHandler(logoutHandler)
//                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
//        )
        ;
		
		return http.build();
   }

	
	
	
	
}
