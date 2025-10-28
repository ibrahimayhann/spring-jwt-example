package com.ibrahimayhan.controller.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibrahimayhan.controller.IRestAuthController;
import com.ibrahimayhan.dto.DtoUser;
import com.ibrahimayhan.jwt.AuthRequest;
import com.ibrahimayhan.jwt.AuthResponse;
import com.ibrahimayhan.jwt.RefreshTokenRequest;
import com.ibrahimayhan.service.IAuthService;

import jakarta.validation.Valid;

@RestController
public class RestAuthControllerImpl implements IRestAuthController {//register servisimiz bu aslında
	
	@Autowired
	private IAuthService authService;
	
	

	@PostMapping("/register")
	@Override
	public DtoUser register(@Valid @RequestBody AuthRequest request) {

		return authService.register(request);
	}

	@PostMapping("/authenticate")
	@Override
	public AuthResponse authenticate(@Valid @RequestBody AuthRequest request) {
		// TODO Auto-generated method stub
		return authService.authenticate(request);
	}

	@PostMapping("/refreshToken")
	@Override
	public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request) {
		// TODO Auto-generated method stub
		return authService.refreshToken(request);
	} 

}
