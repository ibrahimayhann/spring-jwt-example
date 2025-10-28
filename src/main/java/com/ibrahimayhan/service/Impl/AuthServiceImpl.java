package com.ibrahimayhan.service.Impl;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ibrahimayhan.dto.DtoUser;
import com.ibrahimayhan.jwt.AuthRequest;
import com.ibrahimayhan.jwt.AuthResponse;
import com.ibrahimayhan.jwt.JwtService;
import com.ibrahimayhan.jwt.RefreshTokenRequest;
import com.ibrahimayhan.model.RefreshToken;
import com.ibrahimayhan.model.User;
import com.ibrahimayhan.repository.RefreshTokenRepository;
import com.ibrahimayhan.repository.UserRepository;
import com.ibrahimayhan.service.IAuthService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService{
	
	private static final Duration REFRESH_TTL = Duration.ofHours(4);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
	
	
    @Override
	public DtoUser register(AuthRequest request) {	
		
		if(userRepository.existsByUsername(request.getUsername())) {
			
			//exception fırlat böyle bir kullanıcı zateb var 
		    //throw new IllegalArgumentException("Bu kullanıcı adı zaten kullanılıyor.");//bu da patlatıyor sonra exceptionhandling yaparsın
			System.out.println("Böyle bir kullanıcı zaten var");
			return null;
			
		}else {
			
			
			User user =new User();
			user.setUsername(request.getUsername());
			user.setPassword(passwordEncoder.encode(request.getPassword()));
			User savedUser= userRepository.save(user);
			
			DtoUser dtoUser=new DtoUser();
			BeanUtils.copyProperties(savedUser, dtoUser);
			
			return dtoUser;
		}
			
	}
	
	
	
	
	
	
	@Override
	public AuthResponse authenticate(AuthRequest request) {
		
		try {
			Authentication auth =authenticationManager.authenticate(
				       new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
				    );
				User user = (User) auth.getPrincipal(); // ← sonuç token’dan çek
			
				//yukarıdaki kodun aynı işlevlisi aşağıda da var 
				//UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()); 
				//Authentication userAuth = authenticationManager.authenticate(auth);
				//User user=(User)userAuth.getPrincipal();
			 
			
				//Doğrulanan kullanıcıyı aldık (DB'ye ikinci kez gitmeye gerek yok)
				//çünkü Authentication sınıfı zaten arkaplanda db ye sorgu atıyor
			
			String accesToken=jwtService.generateToken(user);
			RefreshToken refreshToken= createRefreshToken(user);
			refreshTokenRepository.save(refreshToken);
			return new AuthResponse(accesToken,refreshToken.getRefreshToken());
			
			
		} catch (Exception e) {
			System.out.println("kullanıcı adı veya şifre hatalı");
	        throw new BadCredentialsException("Kullanıcı adı veya şifre hatalı");

		}
		
	}
	
	
	


	
	private RefreshToken createRefreshToken(User user) {
		RefreshToken rt=new RefreshToken();
		rt.setRefreshToken(UUID.randomUUID().toString());
		rt.setExpireDate(Date.from(Instant.now().plus(REFRESH_TTL)));
		rt.setUser(user);
		return rt;
	}
	
	
	private boolean isRefreshTokenExpired(Date expiredDate) {
		
		return new Date().before(expiredDate);//anlık date expired dateden önceyse süre bitmemiştir true döner tersi durumda false dner
	}
	
	
	
	
	
	// Refresh token oluştururken eski refreshtokenları hala aktif bırakmak güvenlik açığı olduğu için eski refrsh tokenları sileceğim aşağıda 
	//ama en iyi olanı refreshtoken entitysine boolean revoked değişkeni ekleyerek eski tokenların revoked değişkenini true yaparak devre dışı bırakırım 
	//böyle yaparsam ileride de mesela tüm cihazlardan çıkış yap özelliği eklersem bu revoked  çok işimi görür
	//refreshrepostorysine revokeAllByUserId diye metot yazarım logoutall diye conrtollerda endpoin açarım falan 
	
	
	@Override
	@Transactional //db de birbiriyle bağlantılı işlemler yapılırken kullanılır yarıda işlem kalırsa rolback yapıp geri alır 
	public AuthResponse refreshToken(RefreshTokenRequest request) {


		RefreshToken refreshToken=refreshTokenRepository.findByRefreshToken(request.getRefreshToken())
		.orElseThrow(() -> new IllegalArgumentException("Geçersiz refresh token."));
		
		
		if(!isRefreshTokenExpired(refreshToken.getExpireDate())) {//başına ünlem attım yani fonksiyon false dönerse ife girecek
			
			throw new IllegalArgumentException("Refresh token süresi dolmuş.");

		}
		
		String accesToken= jwtService.generateToken(refreshToken.getUser());
		
		RefreshToken savedRefreshToken= refreshTokenRepository.save(createRefreshToken(refreshToken.getUser()));
		return new AuthResponse(accesToken,savedRefreshToken.getRefreshToken());
	
	}


	
}
