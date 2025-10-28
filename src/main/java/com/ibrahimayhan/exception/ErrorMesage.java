package com.ibrahimayhan.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMesage {

	private MessageType messageType;
	
	private String ofStatic;
	
	public String prepareErrorMessage() {
		
		StringBuilder builder=new StringBuilder();
		builder.append(messageType.getMessage());
		if(ofStatic!=null) {
			builder.append(" : "+ ofStatic);
		}
		return builder.toString();
	}
	
	
}

