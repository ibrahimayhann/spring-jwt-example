package com.ibrahimayhan.service;

import com.ibrahimayhan.dto.DtoEmployee;

public interface IEmployeeService {
	
	public DtoEmployee findEmployeeById(Long id);

}
