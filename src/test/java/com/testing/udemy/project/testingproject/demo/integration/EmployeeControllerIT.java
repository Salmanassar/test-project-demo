package com.testing.udemy.project.testingproject.demo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.udemy.project.testingproject.demo.model.Employee;
import com.testing.udemy.project.testingproject.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIT extends AbstractionBaseContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void startup() {
        employeeRepository.deleteAll();
    }

    @DisplayName("Integration test for create employee")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnCreatedEmployee() throws Exception {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Lolik")
                .lastName("Bolik")
                .email("lolicbolik@deneg.net")
                .build();

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then  - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("Integration test for get all employees")
    @Test
    public void givenEmployeeObjects_whenGetEmployeesList_thenReturnEmployees() throws Exception {
        // given - precondition on setup
        Employee employee = Employee.builder()
                .firstName("Lolik")
                .lastName("Bolik")
                .email("lolicbolik@deneg.net")
                .build();

        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Lolik2")
                .lastName("Bolik2")
                .email("lolikbolik2@deneg.net")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        List<Employee> listEmployees = List.of(employee, employee2);

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listEmployees.size())));
    }

    @DisplayName("Integration test for get employee by id")
    @Test
    public void givenEmployeeObject_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        // given - precondition on setup
        Employee employee = Employee.builder()
                .firstName("Lolik")
                .lastName("Bolik")
                .email("lolicbolik@deneg.net")
                .build();

        employeeRepository.save(employee);

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("Integration test for get employee by id (negative scenario")
    @Test
    public void givenInvalidEmployeeObject_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        // given - precondition on setup
        long employeeId = 812L;
        Employee employee = Employee.builder()
                .firstName("Lolik")
                .lastName("Bolik")
                .email("lolicbolik@deneg.net")
                .build();

        employeeRepository.save(employee);

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Integration test for updating employee rest controller")
    @Test
    public void givenEmployeeObjects_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        // given - precondition on setup
        Employee savedEmployee = Employee.builder()
                .firstName("Lolik")
                .lastName("Bolik")
                .email("lolicbolik@deneg.net")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Adam")
                .lastName("Madam")
                .email("adamadam@deneg.net")
                .build();

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @DisplayName("Integration test for updating employee rest controller(negative scenario)")
    @Test
    public void givenEmployeeObjects_whenUpdateEmployee_thenReturn404() throws Exception {
        // given - precondition on setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Lolik")
                .lastName("Bolik")
                .email("lolicbolik@deneg.net")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Adam")
                .lastName("Madam")
                .email("adamadam@deneg.net")
                .build();

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Integration test for delete employee by id Rest Api")
    @Test
    public void givenEmployeeObject_whenDeleteEmployeeById_thenReturn200() throws Exception {
        // given - precondition on setup
        Employee employee = Employee.builder()
                .firstName("Lolik")
                .lastName("Bolik")
                .email("lolicbolik@deneg.net")
                .build();

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employee.getId()));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());
    }
}
