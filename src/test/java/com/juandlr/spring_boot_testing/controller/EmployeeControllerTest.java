package com.juandlr.spring_boot_testing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juandlr.spring_boot_testing.entity.Employee;
import com.juandlr.spring_boot_testing.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee1, employee2;

    @BeforeEach
    void setUp() {
        employee1 = Employee.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("de la Rosa")
                .email("juan@example.com")
                .build();

        employee2 = Employee.builder()
                .id(2L)
                .firstName("Rodrigo")
                .lastName("Sarabia")
                .email("rodrigo@example.com")
                .build();
    }

    // JUnit test for create employee  REST API
    @DisplayName("JUnit test for create employee REST API")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        //given - precondition or setup
        given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employee1)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$.email", is(employee1.getEmail())));
    }

    // JUnit test for get all employees REST API
    @DisplayName("JUnit test for get all employees REST API")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {

        //given - precondition or setup
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee2);
        given(employeeService.getAllEmployees()).willReturn(employeeList);

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output
        response.andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(employeeList.size()))).andDo(print());
    }

    // positive scenario - valid employee id
    // JUnit test for get employee REST API
    @DisplayName("JUnit test for get employee REST API")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        //given - precondition or setup
        given(employeeService.getEmployeeById(employee1.getId())).willReturn(Optional.of(employee1));

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee1.getId()));

        //then - verify the output
        response.andDo(print())
                .andExpect(jsonPath("$.firstName", is("Juan")))
                .andExpect(jsonPath("$.lastName", is("de la Rosa")))
                .andExpect(jsonPath("$.email", is("juan@example.com")))
                .andExpect(status().isOk());
    }

    // negative scenario - invalid employee id
    // JUnit test for get employee by id REST API
    @DisplayName("JUnit test for get employee by id REST API")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        //given - precondition or setup
        long invalidId = 5L;
        given(employeeService.getEmployeeById(invalidId)).willReturn(Optional.empty());

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", invalidId));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    // JUnit test for update employee REST API - positive scenario
    @DisplayName("JUnit test for update employee REST API - positive scenario")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenUpdatedEmployee() throws Exception {

        //given - precondition or setup
        Employee updatedEmployee = Employee.builder().firstName("John").lastName("de la Rosa").email("john@example.com").build();
        given(employeeService.getEmployeeById(1L)).willReturn(Optional.of(employee1));
        given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John"))).andExpect(jsonPath("$.email", is("john@example.com")));
    }

    // JUnit test for update employee REST API - negative scenario
    @DisplayName("JUnit test for update employee REST API - negative scenario")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnEmpty() throws Exception {

        //given - precondition or setup
        Employee updatedEmployee = Employee.builder().firstName("John").lastName("de la Rosa").email("john@example.com").build();
        given(employeeService.getEmployeeById(1L)).willReturn(Optional.empty());

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andDo(print()).andExpect(status().isNotFound());
    }

    // JUnit test for delete employee REST API
    @DisplayName("JUnit test for delete employee REST API")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnOkStatus() throws Exception {

        //given - precondition or setup
        willDoNothing().given(employeeService).deleteEmployee(1L);

        //when - action or the behaviour that we are going test
        ResultActions response =  mockMvc.perform(delete("/api/employees/{id}", 1L));

        //then - verify the output
        response.andDo(print()).andExpect(status().isOk());

    }




}
