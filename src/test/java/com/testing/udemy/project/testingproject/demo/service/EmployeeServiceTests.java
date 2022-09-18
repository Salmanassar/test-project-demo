package com.testing.udemy.project.testingproject.demo.service;

import com.testing.udemy.project.testingproject.demo.exception.ResourceNotFoundException;
import com.testing.udemy.project.testingproject.demo.model.Employee;
import com.testing.udemy.project.testingproject.demo.repository.EmployeeRepository;
import com.testing.udemy.project.testingproject.demo.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Sebastian")
                .lastName("Paseka")
                .email("paseka@deneg.net")
                .build();
    }

    @DisplayName("Junit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        // given - precondition on setup
        given(employeeRepository.findEmployeeByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or behavior that we are going to test
        Employee savedEmployee = employeeServiceImpl.saveEmployee(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();

    }

    @DisplayName("Junit test for saveEmployee method witch throw Custom Exception")
    @Test
    public void givenEmployeeObject_whenSaveExistingEmployee_thenThrowException() {

        // given - precondition on setup
        given(employeeRepository.findEmployeeByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        // when - action or behavior that we are going to test
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> employeeServiceImpl.saveEmployee(employee));

        // then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("Junit test for get all employees")
    @Test
    public void givenEmployeesObjects_whenGetAllEmployees_thenReturnListEmployees() {

        // given - precondition on setup
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Lukasz")
                .lastName("Paseka")
                .email("lukaszpaseka@deneg.net")
                .build();
        given(employeeRepository.findAllBy(PageRequest.of(1, 10))).willReturn(List.of(employee, employee2));

        // when - action or behavior that we are going to test
        List<Employee> employeeList = employeeServiceImpl.getAllEmployees(PageRequest.of(1, 10));
        List<Employee> compereList = List.of(employee2, employee);

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.containsAll(compereList)).isTrue();
    }

    @DisplayName("Junit test for get all employees(negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyListEmployees() {

        // given - precondition on setup
        given(employeeRepository.findAllBy(PageRequest.of(1, 10))).willReturn(Collections.emptyList());

        // when - action or behavior that we are going to test
        List<Employee> employeeList = employeeServiceImpl.getAllEmployees(PageRequest.of(1, 10));

        // then - verify the output
        assertThat(employeeList.size()).isEqualTo(0);
    }

    // Junit test for get employee by id
    @DisplayName("Junit test for get employee by id")
    @Test
    public void givenEmployeeId_whenGetEmployeeId_thenReturnEmployee() {

        // given - precondition on setup
        given(employeeRepository.getEmployeeById(1L)).willReturn(Optional.of(employee));

        // when - action or behavior that we are going to test
        Optional<Employee> getEmployeeById = employeeServiceImpl.getEmployeeById(1L);

        // then - verify the output
        assertThat(getEmployeeById).isNotEmpty();
    }

    @DisplayName("Junit test for get employee by id (negative scenario")
    @Test
    public void givenEmptyEmployee_whenGetEmployeeId_thenReturnEmpty() {

        // given - precondition on setup
        given(employeeRepository.getEmployeeById(1L)).willReturn(Optional.empty());

        // when - action or behavior that we are going to test
        Optional<Employee> getEmployeeById = employeeServiceImpl.getEmployeeById(1L);

        // then - verify the output
        assertThat(getEmployeeById).isEmpty();
    }

    // Junit test for update Employee
    @DisplayName("Junit test for update Employee")
    @Test
    public void givenEmployeeObject_whenUpdatedEmployee_thenReturnUpdatedEmployee() {

        // given - precondition on setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("rambler@rambler.com");
        employee.setFirstName("Boris");

        // when - action or behavior that we are going to test
        Employee updatedEmployee = employeeServiceImpl.updateEmployee(employee);

        // then - verify the output
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Boris");
        assertThat(updatedEmployee.getEmail()).isEqualTo("rambler@rambler.com");
    }

    // Junit test for delete employee
    @DisplayName("Junit test for delete employee")
    @Test
    public void givenEmployeeObject_whenDeleteEmployee_thenNothing() {
        // given - precondition on setup
        willDoNothing().given(employeeRepository).delete(employee);

        // when - action or behavior that we are going to test
        employeeRepository.delete(employee);

        // then - verify the output
        verify(employeeRepository, times(1)).delete(employee);
    }

    @DisplayName("Junit test for delete employee by id")
    @Test
    public void givenEmployeeObject_whenDeleteEmployeeById_thenNothing() {
        // given - precondition on setup
        willDoNothing().given(employeeRepository).deleteEmployeeById(employee.getId());

        // when - action or behavior that we are going to test
        employeeRepository.deleteEmployeeById(employee.getId());

        // then - verify the output
        verify(employeeRepository, times(1)).deleteEmployeeById(employee.getId());
    }
}
