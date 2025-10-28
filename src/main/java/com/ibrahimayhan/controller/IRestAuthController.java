package com.ibrahimayhan.controller;

import com.ibrahimayhan.dto.DtoUser;
import com.ibrahimayhan.jwt.AuthRequest;
import com.ibrahimayhan.jwt.AuthResponse;
import com.ibrahimayhan.jwt.RefreshTokenRequest;

public interface IRestAuthController {

	public DtoUser register(AuthRequest request);
	
	public  AuthResponse authenticate(AuthRequest request);
	
	public AuthResponse refreshToken(RefreshTokenRequest request);
}
