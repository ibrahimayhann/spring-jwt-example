package com.ibrahimayhan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoUser {//kullanıcıya passwordu dönmek güvenlik açığıdır burada örnek olsun diye döndük boolen veya sadece username ve date de dönebiliriz

	private String username;
	
	private String password;
}
