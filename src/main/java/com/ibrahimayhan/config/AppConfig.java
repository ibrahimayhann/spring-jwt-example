package com.ibrahimayhan.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ibrahimayhan.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        // Not: User entity'n UserDetails implement ettiği için direkt dönebilirsin.
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // istersen cost parametresi verebilirsin
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager(); // Spring kendi DaoAuthenticationProvider'ını kurar
    }
    
    
    
    
}















/*package com.ibrahimayhan.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ibrahimayhan.model.User;
import com.ibrahimayhan.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class AppConfig {
	
    private final UserRepository userRepository;

	@Bean
	public AuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return authenticationProvider;
	}
	
	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			
			@Override//jwt filter sınıfınsa userdetailsi enjekte etmiştik ama o zaman demiştik ki ileriki derslerde bunun beanini yazacağız işte burada yazdık 
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
					//jwtfilter da loadbyusername kullanmıştık ama içi boştu şimdi burada doldurduk hatta sadece burada değil repositorynin içine gidip bu findbyusernamem metodunu da yazdık
				Optional<User> optional= userRepository.findByUsername(username);
				if(optional.isPresent()) {
					return optional.get();
					//not:User tipinde return ettik ama metodun dönüş tipi userdetails neden hata vermedi?
					//çünkü user sınıfımız userdetailsi implemente etmişti bu nedenle userdetails useri karşılayabilir
				}
				return null;
			}
		};
	}
	
	
	

}
*/
