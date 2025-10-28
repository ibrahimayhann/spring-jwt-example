package com.ibrahimayhan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibrahimayhan.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
