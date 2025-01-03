package com.juandlr.spring_boot_testing.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juandlr.spring_boot_testing.entity.Employee;
import com.juandlr.spring_boot_testing.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.CoreMatchers.is;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee1, employee2;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        employee1 = Employee.builder()
                .firstName("Juan")
                .lastName("de la Rosa")
                .email("juan@example.com")
                .build();

        employee2 = Employee.builder()
                .firstName("Rodrigo")
                .lastName("Sarabia")
                .email("rodrigo@example.com")
                .build();
    }

    // JUnit integration test for create employee  REST API
    @DisplayName("JUnit integration test for create employee  REST API")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        //given - precondition or setup, we use the setup method

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employee1)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$.email", is(employee1.getEmail())));
    }

    // JUnit integration test for get all employees REST API
    @DisplayName("JUnit integration test for get all employees REST API")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {

        //given - precondition or setup
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee2);
        employeeRepository.saveAll(employeeList);

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output
        response.andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(employeeList.size()))).andDo(print());
    }

    // positive scenario - valid employee id
    // JUnit integration test for get employee REST API
    @DisplayName("JUnit integration test for get employee REST API - positive scenario")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        //given - precondition or setup
        employeeRepository.save(employee1);

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee1.getId()));

        //then - verify the output
        response.andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$.email", is(employee1.getEmail())))
                .andExpect(status().isOk());
    }

    // negative scenario - invalid employee id
    // JUnit integration test for get employee by id REST API
    @DisplayName("JUnit integration test for get employee by id REST API - negative scenario")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        //given - precondition or setup
        long invalidId = 5L;

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", invalidId));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    // JUnit integration test for update employee REST API - positive scenario
    @DisplayName("JUnit integration test for update employee REST API - positive scenario")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenUpdatedEmployee() throws Exception {

        //given - precondition or setup
        employeeRepository.save(employee1);
        Employee updatedEmployee = Employee.builder().firstName("John").lastName("de la Rosa").email("john@example.com").build();

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employee1.getId())
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    // JUnit integration for update employee REST API - negative scenario
    @DisplayName("JUnit integration for update employee REST API - negative scenario")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnEmpty() throws Exception {

        //given - precondition or setup
        Employee updatedEmployee = Employee.builder().firstName("John").lastName("de la Rosa").email("john@example.com").build();

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andDo(print()).andExpect(status().isNotFound());
    }

    // JUnit integration test for delete employee REST API
    @DisplayName("JUnit integration test for delete employee REST API")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnOkStatus() throws Exception {

        //given - precondition or setup
        employeeRepository.save(employee1);
        //when - action or the behaviour that we are going test
        ResultActions response =  mockMvc.perform(delete("/api/employees/{id}", employee1.getId()));

        //then - verify the output
        response.andDo(print()).andExpect(status().isOk());

    }

}
