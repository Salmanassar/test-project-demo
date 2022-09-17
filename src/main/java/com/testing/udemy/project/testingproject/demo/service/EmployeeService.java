package com.testing.udemy.project.testingproject.demo.service;

import com.testing.udemy.project.testingproject.demo.model.Employee;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees(PageRequest pageRequest);

    Optional<Employee> getEmployeeById(long id);

    Employee updateEmployee(Employee employee);

    void deleteEmployee(Employee employee);
}
