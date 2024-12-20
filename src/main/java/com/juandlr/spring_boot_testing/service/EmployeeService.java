package com.juandlr.spring_boot_testing.service;

import com.juandlr.spring_boot_testing.entity.Employee;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
}
