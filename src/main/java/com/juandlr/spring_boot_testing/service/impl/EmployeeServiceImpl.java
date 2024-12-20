package com.juandlr.spring_boot_testing.service.impl;

import com.juandlr.spring_boot_testing.entity.Employee;
import com.juandlr.spring_boot_testing.exception.ResourceNotFoundException;
import com.juandlr.spring_boot_testing.repository.EmployeeRepository;
import com.juandlr.spring_boot_testing.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()){
            throw new ResourceNotFoundException("Employee already exist with given email " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }


}
