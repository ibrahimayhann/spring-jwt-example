package com.ibrahimayhan.exception;

import lombok.Getter;

@Getter//setterea gerek yok
public enum MessageType {

	NO_RECORD_EXIST("1001","Kayıt bulunamadı."),
	GENERAL_EXCEPTION("9999","Genel bir hata oluştu.");
	
	private String code;
	
	private String message;
	
	private MessageType(String code,String message) {

		this.code=code;
		this.message=message;
		
	}
}
