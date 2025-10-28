/*package com.ibrahimayhan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ibrahimayhan.jwt.AuthEntryPoint;
import com.ibrahimayhan.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	public static final String AUTHENTICATE="/authenticate";
	public static final String REGISTER="/register";
	public static final String REFRESH_TOKEN="/refreshToken";
	
	public static final String [] SWAGGER_PATHS= {
		
			"swagger-ui/**",
			"v3/api-docs/**",
			"swagger-ui.html"
	};
	

	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private AuthEntryPoint authEntryPoint;
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http
		.csrf(csrf->csrf.disable())
		.authorizeHttpRequests(request->
		request.requestMatchers(AUTHENTICATE,REGISTER,REFRESH_TOKEN)//bu 3 isteği controllera yönlendirebilirsin
		.permitAll().requestMatchers(SWAGGER_PATHS).permitAll()							//ama geriye kalan tüm istekleri permitall sayesinde filtreye yolla
		.anyRequest()
		.authenticated())
		.exceptionHandling(exception ->exception.authenticationEntryPoint(authEntryPoint))
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authenticationProvider(authenticationProvider)
		.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
}
*/


package com.ibrahimayhan.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.ibrahimayhan.jwt.AuthEntryPoint;
import com.ibrahimayhan.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Public endpoints
    private static final String AUTHENTICATE = "/authenticate";
    private static final String REGISTER = "/register";
    private static final String REFRESH_TOKEN = "/refreshToken";

    // Swagger/OpenAPI
    private static final String[] SWAGGER = {
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-ui.html"
    };

    private final JwtAuthenticationFilter jwtFilter;
    private final AuthEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Stateless + temel güvenlik
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))

            // Yetkilendirme kuralları (okunur blok)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(AUTHENTICATE, REGISTER, REFRESH_TOKEN).permitAll()
                .requestMatchers(SWAGGER).permitAll()
                .anyRequest().authenticated()
            )

            // JWT filtresi doğru sırada
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
