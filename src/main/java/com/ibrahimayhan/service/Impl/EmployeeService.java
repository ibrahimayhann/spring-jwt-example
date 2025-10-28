package com.ibrahimayhan.service.Impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibrahimayhan.dto.DtoDepartment;
import com.ibrahimayhan.dto.DtoEmployee;
import com.ibrahimayhan.model.Department;
import com.ibrahimayhan.model.Employee;
import com.ibrahimayhan.repository.EmployeeRepository;
import com.ibrahimayhan.service.IEmployeeService;

@Service
public class EmployeeService implements IEmployeeService{

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Override
	public DtoEmployee findEmployeeById(Long id) {

		Optional<Employee> optional = employeeRepository.findById(id);
		if(optional.isEmpty()) {
			//exception fırlat burada
			System.out.println(id+" id numaralı kullanıcı bulunamadı!");
			return null;
		}
		else {
			
			
			Employee employee=optional.get();
			DtoEmployee dtoEmployee=new DtoEmployee();
			BeanUtils.copyProperties(employee, dtoEmployee);

				if(employee.getDepartment()!=null) {
					DtoDepartment dtoDepartment=new DtoDepartment();
					BeanUtils.copyProperties(employee.getDepartment(), dtoDepartment);
				    dtoEmployee.setDepartment(dtoDepartment);
				}
						
				return dtoEmployee;

		}
	}

}
