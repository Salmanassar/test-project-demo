package com.testing.udemy.project.testingproject.demo.service.impl;

import com.testing.udemy.project.testingproject.demo.exception.ResourceNotFoundException;
import com.testing.udemy.project.testingproject.demo.model.Employee;
import com.testing.udemy.project.testingproject.demo.repository.EmployeeRepository;
import com.testing.udemy.project.testingproject.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> findEmployeeByEmail = employeeRepository.findEmployeeByEmail(employee.getEmail());
        if(findEmployeeByEmail.isPresent()){
            throw new ResourceNotFoundException("The employee is presents with email: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees(PageRequest pageRequest) {
        return employeeRepository.findAllBy(pageRequest);
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        return employeeRepository.getEmployeeById(id);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Employee employee) {
        employeeRepository.delete(employee);
    }

    @Override
    public void deleteEmployeeById(long id) {
        employeeRepository.deleteEmployeeById(id);
    }
}
