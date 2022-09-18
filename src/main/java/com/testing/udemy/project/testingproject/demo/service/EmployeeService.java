package com.testing.udemy.project.testingproject.demo.service;

import com.testing.udemy.project.testingproject.demo.model.Employee;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface EmployeeService {
    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees(PageRequest pageRequest);

    Optional<Employee> getEmployeeById(long id);

    Employee updateEmployee(Employee employee);

    void deleteEmployee(Employee employee);

    void deleteEmployeeById(long id);
}
