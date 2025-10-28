package com.ibrahimayhan.controller.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibrahimayhan.controller.IRestEmployeeController;
import com.ibrahimayhan.dto.DtoEmployee;
import com.ibrahimayhan.service.IEmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee")
public class EmployeControllerImpl implements IRestEmployeeController{
	
	@Autowired
	private IEmployeeService employeeService;

	@GetMapping("/{id}")
	@Override
	public DtoEmployee findEmployeeById(@Valid @PathVariable(value = "id") Long id) {
		return employeeService.findEmployeeById(id);
	}

}
