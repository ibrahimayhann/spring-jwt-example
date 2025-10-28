package com.ibrahimayhan.service;

import com.ibrahimayhan.dto.DtoUser;
import com.ibrahimayhan.jwt.AuthRequest;
import com.ibrahimayhan.jwt.AuthResponse;
import com.ibrahimayhan.jwt.RefreshTokenRequest;
import com.ibrahimayhan.model.RefreshToken;
import com.ibrahimayhan.model.User;

public interface IAuthService {
	
	public DtoUser register(AuthRequest request);
	
	public AuthResponse authenticate(AuthRequest request);
	
	
	public AuthResponse refreshToken(RefreshTokenRequest request);


}
