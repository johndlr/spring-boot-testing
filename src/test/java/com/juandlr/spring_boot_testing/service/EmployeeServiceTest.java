package com.juandlr.spring_boot_testing.service;

import com.juandlr.spring_boot_testing.entity.Employee;
import com.juandlr.spring_boot_testing.exception.ResourceNotFoundException;
import com.juandlr.spring_boot_testing.repository.EmployeeRepository;
import static org.assertj.core.api.Assertions.*;

import com.juandlr.spring_boot_testing.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("de la Rosa")
                .email("juan@example.com")
                .build();
    }

    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeEmail_whenSaveEmployee_thenReturnEmployeeObject(){

        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();

    }

    // JUnit test for saveEmployee method which throws exception
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenEmployeeEmail_whenSaveEmployee_thenThrowsException(){

        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));

    }

    // JUnit test for getAllEmployees method
    @DisplayName("JUnit test for getAllEmployees method")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeesList(){

        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Rodrigo")
                .lastName("Sarabia")
                .email("rodrigo@example.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        //when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }

    // JUnit test for getAllEmployees method (negative scenario)
    @DisplayName("JUnit test for getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){

        //given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);

    }

    // JUnit test for getEmployeeById method
    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void given_when_then(){

        //given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeService.getEmployeeById(1L).get();

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for updateEmployee method
    @DisplayName("JUnit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){

        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setFirstName("John");
        employee.setEmail("john@example.com");

        //when - action or the behaviour that we are going test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //then - verify the output
        assertThat(updatedEmployee.getFirstName()).isEqualTo("John");
        assertThat(updatedEmployee.getEmail()).isEqualTo("john@example.com");
    }



}
