package com.testing.udemy.project.testingproject.demo.repository;

import com.testing.udemy.project.testingproject.demo.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class EmployeeRepositoryTests {
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("Sebastian")
                .lastName("Paseka")
                .email("paseka1@deneg.net")
                .build();
    }

    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSaveObject_thanReturnSavedEmployee() {

        // given - precondition on setup

        // when - action or behavior that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    // Junit test for find all employees
    @DisplayName("Junit test for find all employees operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {

        // given - precondition on setup
        PageRequest pageRequest = PageRequest.of(0, 10);
        Employee employee2 = Employee.builder()
                .firstName("Ronny")
                .lastName("Kolman")
                .email("ronny@bodybuildin.net")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when - action or behavior that we are going to test
        List<Employee> allEmployees = employeeRepository.findAllBy(pageRequest);

        // then - verify the output
        assertThat(allEmployees).isNotNull();
        assertThat(allEmployees.size()).isEqualTo(2);
    }

    // Junit test for get employee by id
    @DisplayName("Junit test for get employee by id")
    @Test
    public void givenEmployees_whenGetEmployeeById_thenReturnEmployeeObject() {

        // given - precondition on setup
        Employee employee2 = Employee.builder()
                .firstName("Ronny")
                .lastName("Kolman")
                .email("ronny@bodybuildin.net")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when - action or behavior that we are going to test
        Employee employeeDb = employeeRepository.getEmployeeById(employee.getId()).get();

        // then - verify the output
        assertThat(employeeDb).isNotNull();
        assertThat(employeeDb.getId()).isEqualTo(employee.getId());
    }

    // Junit test for find Employee by email
    @DisplayName("Junit test for finding Employee by email")
    @Test
    public void givenEmployees_whenFindEmployeeByEmail_thenReturnEmail() {

        // given - precondition on setup
        Employee employee2 = Employee.builder()
                .firstName("Ronny")
                .lastName("Kolman")
                .email("ronny@bodybuildin.net")
                .build();
        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when - action or behavior that we are going to test
        Employee employeeDb = employeeRepository.findEmployeeByEmail(employee2.getEmail()).get();

        // then - verify the output
        assertThat(employeeDb).isNotNull();
        assertThat(employeeDb.getEmail()).isEqualTo(employee2.getEmail());
    }

    // Junit test for update employee
    @DisplayName("Junit test for update employee")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        // given - precondition on setup
        employeeRepository.save(employee);

        // when - action or behavior that we are going to test
        Employee savedEmployee = employeeRepository.getEmployeeById(employee.getId()).get();
        savedEmployee.setEmail("pass@unet.com");
        savedEmployee.setFirstName("Ugrob");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("pass@unet.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Ugrob");
    }

    // Junit test for delete employee operation
    @DisplayName("Junit test for delete employee")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {

        // given - precondition on setup
        employeeRepository.save(employee);

        // when - action or behavior that we are going to test
        employeeRepository.delete(employee);
        Optional<Employee> employeeRemoved = employeeRepository.getEmployeeById(employee.getId());

        // then - verify the output
        assertThat(employeeRemoved).isEmpty();
    }

    @DisplayName("Junit test for delete employee by id")
    @Test
    public void givenEmployeeObject_whenDeleteById_thenReturnEmptyObject() {

        // given - precondition on setup
        employeeRepository.save(employee);
        long employeeId = employee.getId();

        // when - action or behavior that we are going to test
        employeeRepository.deleteEmployeeById(employeeId);
        Optional<Employee> employeeRemoved = employeeRepository.getEmployeeById(employeeId);

        // then - verify the output
        assertThat(employeeRemoved).isEmpty();
    }

    // Junit test for custom query JPQL to find Employee by first name and last name
    @DisplayName("Junit test for custom query JPQL to find Employee by first name and last name")
    @Test
    public void givenEmployeeObject_whenFindEmployeeByFistNameAndLastName_thenReturnEmployeeObject() {

        // given - precondition on setup
        employeeRepository.save(employee);

        // when - action or behavior that we are going to test
        Employee employeeFindByFistNameAndLastName = employeeRepository
                .findEmployeeByFirstNameAndLastName("Sebastian", "Paseka").get();

        // then - verify the output
        assertThat(employeeFindByFistNameAndLastName.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(employeeFindByFistNameAndLastName.getLastName()).isEqualTo(employee.getLastName());
    }

    // Junit test for custom query JPQL to find Employee by first name and last name
    @DisplayName("Junit test for custom query JPQL to find Employee by named parameters")
    @Test
    public void givenEmployeeObject_whenFindEmployeeByNamedParameters_thenReturnEmployeeObject() {

        // given - precondition on setup
        employeeRepository.save(employee);

        // when - action or behavior that we are going to test
        Optional<Employee> employeeFindByNames = employeeRepository
                .findEmployeeByNamedParameters("Sebastian", "Paseka");

        // then - verify the output
        assertThat(employeeFindByNames).isNotEmpty();
    }

    @DisplayName("Junit test for custom query with native SQL to find Employee by index parameters")
    @Test
    public void givenEmployeeObject_whenFindEmployeeByNativeSQL_thenReturnEmployeeObject() {

        // given - precondition on setup
        employeeRepository.save(employee);

        // when - action or behavior that we are going to test
        Optional<Employee> employeeFindByNames = employeeRepository
                .findEmployeeByFirstAndLastNameUsingNativeSQL("Sebastian", "Paseka");

        // then - verify the output
        assertThat(employeeFindByNames).isNotEmpty();
    }

    @DisplayName("Junit test for custom query with native SQL to find Employee by named parameters")
    @Test
    public void givenEmployeeObject_whenFindEmployeeByNativeSQLWithNamedParameters_thenReturnEmployeeObject() {

        // given - precondition on setup
        employeeRepository.save(employee);

        // when - action or behavior that we are going to test
        Optional<Employee> employeeFindByNames = employeeRepository
                .findEmployeeUsingNativeSQLNamedParameters(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        assertThat(employeeFindByNames).isNotEmpty();
    }
}
