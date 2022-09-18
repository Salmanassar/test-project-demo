package com.testing.udemy.project.testingproject.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.udemy.project.testingproject.demo.model.Employee;
import com.testing.udemy.project.testingproject.demo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Lolik")
                .lastName("Bolik")
                .email("lolikbolik@deneg.net")
                .build();
    }

    @DisplayName("Junit test for create Employee")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnCreatedEmployee() throws Exception {
        // given - precondition or setup
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

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

    @DisplayName("Junit test for get all employees")
    @Test
    public void givenEmployeeObjects_whenGetEmployeesList_thenReturnEmployees() throws Exception {
        // given - precondition on setup
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Lolik2")
                .lastName("Bolik2")
                .email("lolikbolik2@deneg.net")
                .build();

        List<Employee> listEmployees = new ArrayList<>();
        listEmployees.add(employee);
        listEmployees.add(employee2);
        PageRequest pageRequest = PageRequest.of(0, 10);
        given(employeeService.getAllEmployees(pageRequest)).willReturn(listEmployees);

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listEmployees.size())));
    }

    @DisplayName("Junit test for get employee by id")
    @Test
    public void givenEmployeeObject_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        // given - precondition on setup
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("Junit test for get employee by id (negative scenario")
    @Test
    public void givenInvalidEmployeeObject_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        // given - precondition on setup
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Junit test for updating employee rest controller")
    @Test
    public void givenEmployeeObjects_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        // given - precondition on setup
        long employeeId = 1L;
        Employee updatedEmployee = Employee.builder()
                .firstName("Adam")
                .lastName("Madam")
                .email("adamadam@deneg.net")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @DisplayName("Junit test for updating employee rest controller(negative scenario)")
    @Test
    public void givenEmployeeObjects_whenUpdateEmployee_thenReturn404() throws Exception {
        // given - precondition on setup
        long employeeId = 1L;
        Employee updatedEmployee = Employee.builder()
                .firstName("Adam")
                .lastName("Madam")
                .email("adamadam@deneg.net")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Junit test for delete employee by id Rest Api")
    @Test
    public void givenEmployeeObject_whenDeleteEmployeeById_thenReturn200() throws Exception {
        // given - precondition on setup
        long employeeId = employee.getId();
        willDoNothing().given(employeeService).deleteEmployeeById(employeeId);

        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());
    }
}
