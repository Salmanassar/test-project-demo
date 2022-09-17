package com.testing.udemy.project.testingproject.demo.repository;

import com.testing.udemy.project.testingproject.demo.model.Employee;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends Repository<Employee, Long> {
    Employee save(Employee employee);

    List<Employee> findAllBy(PageRequest pageRequest);

    Optional<Employee> getEmployeeById(long id);

    Optional<Employee> findEmployeeByEmail(String email);

    void delete(Employee employee);

    // Define custom query using JPQL with index parameters
    @Query("select e from Employee e where e.firstName=?1 and e.lastName=?2")
    Optional<Employee> findEmployeeByFirstNameAndLastName(String firstName, String lastName);

    // Define custom query using JPQL with named parameters
    @Query("select e from Employee e where e.firstName=:firstName and e.lastName=:lastName")
    Optional<Employee> findEmployeeByNamedParameters(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // Define custom query using native SQL with index parameters
    @Query(value = "select * from employees e where e.first_name=?1 and e.last_name=?2", nativeQuery = true)
    Optional<Employee> findEmployeeByFirstAndLastNameUsingNativeSQL(String fistName, String lastName);

    // Define custom query using native SQL with named parameters
    @Query(value = "select * from employees e where e.first_name=:firstName and e.last_name=:lastName", nativeQuery = true)
    Optional<Employee> findEmployeeUsingNativeSQLNamedParameters(@Param("firstName") String fistName, @Param("lastName") String lastName);
}
