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

@EnableWebSecurity
@Configuration
public class SecurityConfiguration  {
	
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
            "/api/usuarios/autenticar"};
	
    @Bean
    PasswordEncoder passWordEncoder() {
		return new BCryptPasswordEncoder();
	}

    
//    @Bean
//    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("Filipe")
//          .password(bCryptPasswordEncoder.encode("senha"))
//          .roles("USER")
//          .build());
//        manager.createUser(User.withUsername("Filipe")
//          .password(bCryptPasswordEncoder.encode("senha"))
//          .roles("USER", "ADMIN")
//          .build());
//        return manager;
//    }
//
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
//        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//        .logout(logout ->
//                logout.logoutUrl("/api/v1/auth/logout")
//                        .addLogoutHandler(logoutHandler)
//                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
//        )
        ;
		
		return http.build();
    }

	
	
	
	
	
	
	
	
	
	
	

//	@Bean
//    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
//        http
////	        .csrf(AbstractHttpConfigurer::disable)
//	        .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
//            .authorizeHttpRequests(authz -> authz
//            	.requestMatchers(antMatcher("/")).permitAll()
//            			.requestMatchers(antMatcher("/login")).permitAll()
//            					.requestMatchers(antMatcher("/stylesheets/**")).permitAll()
//            							.requestMatchers(antMatcher("/javascript/**")).permitAll()
//            									.requestMatchers(antMatcher("/font-awesome/**")).permitAll()
//            											.requestMatchers(antMatcher("/css/**")).permitAll()
//            													.requestMatchers(antMatcher("/img/**")).permitAll()
//            															.requestMatchers(antMatcher("/webjars/**")).permitAll()
//                .anyRequest().authenticated()                
//            ).formLogin( form -> form
//                    .loginPage("/login")
//                    .successHandler(authenticationSucessHandler())
//                    .permitAll()
//            ).logout(form -> form 
//            		.invalidateHttpSession(true)
//            		.clearAuthentication(true)
//            		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//            		.logoutSuccessUrl("/login?logout")
//    	            .permitAll()
//            ).sessionManagement(session -> session
//            		.invalidSessionUrl("/login")
//            ).addFilterBefore(targetUrlFilter(), UsernamePasswordAuthenticationFilter.class)
//            ;
////            .httpBasic(withDefaults());
//        return http.build();
//    }
	
//	 @Override
//	    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//	    	authenticationManagerBuilder.authenticationProvider(customAuthProvider);
//	    }
	
}
