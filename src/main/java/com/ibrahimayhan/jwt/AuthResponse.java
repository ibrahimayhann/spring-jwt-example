package com.ibrahimayhan.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {//oluşturduğumuz tokenu dönen servis
	
	private String accesToken;
	
	private String refreshToken;

}
