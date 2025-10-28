package com.ibrahimayhan.jwt;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {//authrequest alıp dto dönecez //dtouserla tamamen aynı bi clas normalde gereksizdir ama farklı uygulamalarda gerek olabilir diye döyle yaptık
	
	@NotEmpty
	private String username;
	
	@NotEmpty
	private String password;

}
