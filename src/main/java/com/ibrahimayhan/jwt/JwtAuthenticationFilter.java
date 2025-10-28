package com.ibrahimayhan.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserDetailsService userDetailsService;//bunu enjekte ettik ama beanini oluşturmamıştık ileriki derslerde oluşturcaz

	@Override// Her HTTP isteğinde bu filtre bir kez çalışır (OncePerRequestFilter sayesinde)
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		//Bize header şu şekilde döndüğü için "Bearer ejrıwreıoehrgıuehuıgherug" tokena ulaşmak istersek baştan 7 karakter silcez
		
		String header;
		String token;
		String username;
		
		header=request.getHeader("Authorization");
		
		if(header==null) {//gelen isteğin headerinde authorization yoksa direk geri gönderiyoruz 
			filterChain.doFilter(request, response);
			return;// Eğer header yoksa (yetkilendirme yoksa) filtre zincirine devam eder, kontrol etmeden geçer

		}
		token=header.substring(7);
		try {
			username= jwtService.getUsernameByToken(token);
			
			if(username!=null&&SecurityContextHolder.getContext().getAuthentication()==null) {
				//tokenda username var mı ve  contexte herhangi başka bir token var mı (null olmalı yani başka bir token olmamalı)  
				
				
				//aşağıdaki loadbyusername metodu bi üste aldığımız username i veritabanında arar var  mı diye kontrol eder
				UserDetails userDetails=userDetailsService.loadUserByUsername(username);
				if(userDetails!=null&&jwtService.isTokenExpired(token)) {//veritabanda bu username varsa ve token süresi geçmediyse bu ife gir
					// artık burada kullanıcıyı içeri alabilirim(securitycontexte koyabilirim)
					
					UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
					authentication.setDetails(userDetails);
					SecurityContextHolder.getContext().setAuthentication(authentication);// SecurityContext'e bu kullanıcıyı oturum açmış gibi yerleştiririz

					
				}
				
				
			}
			
		} catch (ExpiredJwtException e) {
			System.out.println("Token süresi dolmmuş"+e.getMessage());
			
		}
		catch(Exception e) {
			System.out.println("Genel bir hata oluştu"+e.getMessage());
		}
		
	
		filterChain.doFilter(request, response);//iflere giremezse ve bir hata da olmazsa yine içeri almıyoruz kullanıcıyı, bu kodla requesti geri çeviriyoruz 
        // Filtre zincirine devam edilir (her durumda çağrılmalı yoksa istek takılır)

	}

	
}
