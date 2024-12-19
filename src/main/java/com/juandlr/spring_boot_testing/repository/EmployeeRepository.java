package com.juandlr.spring_boot_testing.repository;

import com.juandlr.spring_boot_testing.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    // define custom query using JPQL and index params
    @Query("select e from Employee e where e.firstName = ?1 and  e.lastName = ?2")
    Employee findByJPQL(String firstName, String lastName);

    // define custom query using JPQL and named params
    @Query("select e from Employee e where e.firstName =:firstName and  e.lastName =:lastName")
    Employee findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // define custom query using Native SQL and index params
    @Query(value = "SELECT * FROM employees e WHERE e.first_name =?1 AND e.last_name =?2", nativeQuery = true)
    Employee findByNativeSQL(String firstName, String lastName);

    // define custom query using Native SQL and named params
    @Query(value = "SELECT * FROM employees e WHERE e.first_name =:firstName AND e.last_name =:lastName", nativeQuery = true)
    Employee findByNativeSQLNamed(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
