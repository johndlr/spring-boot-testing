package com.juandlr.spring_boot_testing.testcontainers;

import com.juandlr.spring_boot_testing.entity.Employee;
import com.juandlr.spring_boot_testing.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryIntegrationTestUsingTestContainers extends AbstractContainerBaseTest{

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
         employee = Employee.builder()
                .firstName("Juan")
                .lastName("de la Rosa")
                .email("juan@example.com")
                .build();
    }

    // JUnit test for save employee operation
    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        //given - precondition or setup | using setUp method to initialize employee object

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    // JUnit test for get all employees operation
    @DisplayName("JUnit test for get all employees operation")
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeesList(){

        //given - precondition or setup | using setUp method to initialize employee object

        Employee employee1 = Employee.builder()
                .firstName("Marco")
                .lastName("Ramirez")
                .email("marco@example.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        //when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }

    // JUnit test for get employee by id operation
    @DisplayName("JUnit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){

        //given - precondition or setup | using setUp method to initialize employee object

        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();

    }

    // JUnit test for get employee by email operation
    @DisplayName("JUnit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){

        //given - precondition or setup | using setUp method to initialize employee object

        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();

    }

    // JUnit test for update employee operation
    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){

        //given - precondition or setup | using setUp method to initialize employee object

        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByEmail(employee.getEmail()).get();
        savedEmployee.setFirstName("John");
        savedEmployee.setEmail("john@example.com");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then - verify the output
        assertThat(updatedEmployee.getFirstName()).isEqualTo("John");
        assertThat(updatedEmployee.getEmail()).isEqualTo("john@example.com");

    }

    // JUnit test for delete employee operation
    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee(){

        //given - precondition or setup | using setUp method to initialize employee object

        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        //then - verify the output
        assertThat(employeeOptional).isEmpty();

    }

    // JUnit test for custom query using JPQL with index
    @DisplayName("JUnit test for custom query using JPQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject(){

        //given - precondition or setup | using setUp method to initialize employee object

        employeeRepository.save(employee);
        String firstName = "Juan";
        String lastName = "de la Rosa";

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQL(firstName,lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();

    }

    // JUnit test for custom query using JPQL with named parameters
    @DisplayName("JUnit test for custom query using JPQL with named parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParameters_thenReturnEmployeeObject(){

        //given - precondition or setup | using setUp method to initialize employee object

        employeeRepository.save(employee);
        String firstName = "Juan";
        String lastName = "de la Rosa";

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName,lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();

    }

    // JUnit test for custom query using native SQL with index
    @DisplayName("JUnit test for custom query using native SQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject(){

        //given - precondition or setup | using setUp method to initialize employee object

        employeeRepository.save(employee);
        String firstName = "Juan";
        String lastName = "de la Rosa";

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQL(firstName,lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();

    }

    // JUnit test for custom query using native SQL with named params
    @DisplayName("JUnit test for custom query using native SQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamed_thenReturnEmployeeObject(){

        //given - precondition or setup | using setUp method to initialize employee object
        
        employeeRepository.save(employee);
        String firstName = "Juan";
        String lastName = "de la Rosa";

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamed(firstName,lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();

    }


}
