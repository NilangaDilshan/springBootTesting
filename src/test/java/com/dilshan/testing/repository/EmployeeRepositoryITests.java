package com.dilshan.testing.repository;

import com.dilshan.testing.testcontainer.AbstractContainerBaseTest;
import com.dilshan.testing.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)//Only when mysql as database
@AutoConfigureTestEntityManager//Only when mysql as database
public class EmployeeRepositoryITests extends AbstractContainerBaseTest {

    private Employee employee;

    @BeforeEach
    public void setup() {
        this.employee = Employee.builder()
                .firstName("Dilshan")
                .lastName("Wijetunga")
                .email("test@gmail.com")
                .build();
        //this.employeeRepository.deleteAll();//Only when mysql as database
    }

    @Autowired
    private EmployeeRepository employeeRepository;

    //JUnit test for save employee operation
    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        //given - precondition or setup
        //Employee object from the private variable

        //when - action or the behaviour to be tested
        Employee savedEmployee = this.employeeRepository.save(employee);

        //then -verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);

    }

    //JUnit test for get all employees operation
    @Test
    @DisplayName("JUnit test for get all employees operation")
    public void givenEmployeesList_whenFindAllMethod_thenEmployeesList() {

        //given - precondition or setup
        //Employee object from the private variable

        Employee employee2 = Employee.builder()
                .firstName("Slayer")
                .lastName("Araya")
                .email("666@gmail.com")
                .build();
        this.employeeRepository.save(employee);
        this.employeeRepository.save(employee2);

        //when - action or the behaviour to be tested
        List<Employee> employeeList = this.employeeRepository.findAll();

        //then -verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //JUnit test for get employee by id operation
    @DisplayName("JUnit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {

        //given - precondition or setup
        //Employee object from the private variable

        this.employeeRepository.save(employee);

        //when - action or the behaviour to be tested
        Employee retrievedEmployee = this.employeeRepository.findById(employee.getId()).get();

        //then -verify the output
        assertThat(retrievedEmployee).isNotNull();
        assertThat(retrievedEmployee.getId()).isEqualTo(employee.getId());
    }

    //JUnit test for get employee by email operation
    @DisplayName("JUnit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {

        //given - precondition or setup
        //Employee object from the private variable

        this.employeeRepository.save(employee);

        //when - action or the behaviour to be tested
        Employee employeeByEmail = this.employeeRepository.findByEmail(employee.getEmail()).get();

        //then -verify the output
        assertThat(employeeByEmail).isNotNull();
        assertThat(employeeByEmail.getEmail()).isEqualTo(employee.getEmail());
    }

    //JUnit test for update employee operation
    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        //given - precondition or setup
        //Employee object from the private variable

        this.employeeRepository.save(employee);

        //when - action or the behaviour to be tested
        Employee savedEmployee = this.employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("satan@gmail.com");
        savedEmployee.setFirstName("Nilanga");
        Employee updatedEmployee = this.employeeRepository.save(savedEmployee);

        //then -verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo(savedEmployee.getEmail());
        assertThat(updatedEmployee.getFirstName()).isEqualTo(savedEmployee.getFirstName());
    }

    //JUnit test for delete employee operation
    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {

        //given - precondition or setup
        //Employee object from the private variable

        this.employeeRepository.save(employee);

        //when - action or the behaviour to be tested
        this.employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = this.employeeRepository.findById(employee.getId());

        //then -verify the output
        assertThat(employeeOptional.isPresent()).isFalse();
        assertThat(employeeOptional.isEmpty()).isTrue();
    }

    //JUnit test for custom query using JPQL with index params
    @DisplayName("JUnit test for custom query using JPQL with index params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLIndexParams_thenReturnEmployeeObject() {

        //given - precondition or setup
        //Employee object from the private variable

        this.employeeRepository.save(employee);
        //when - action or the behaviour to be tested
        Employee savedEmployee = this.employeeRepository.findByJPQLIndexParams(employee.getFirstName(), employee.getLastName());

        //then -verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for custom query using JPQL with named params
    @DisplayName("JUnit test for custom query using JPQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {

        //given - precondition or setup
        //Employee object from the private variable

        this.employeeRepository.save(employee);
        //when - action or the behaviour to be tested
        Employee savedEmployee = this.employeeRepository.findByJPQLNamedParams(employee.getFirstName(), employee.getLastName());

        //then -verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for custom query using Native Query with index params
    @DisplayName("JUnit test for custom query using Native Query with index params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeQueryIndexParams_thenReturnEmployeeObject() {

        //given - precondition or setup
        //Employee object from the private variable

        this.employeeRepository.save(employee);

        //when - action or the behaviour to be tested
        Employee savedEmployee = this.employeeRepository.findByNativeSqlWithIndexParams(employee.getFirstName(), employee.getLastName());

        //then -verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for custom query using Native Query with named params
    @DisplayName("JUnit test for custom query using Native Query with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeQueryNamedParams_thenReturnEmployeeObject() {

        //given - precondition or setup
        //Employee object from the private variable

        this.employeeRepository.save(employee);

        //when - action or the behaviour to be tested
        Employee savedEmployee = this.employeeRepository.findByNativeSqlWithNamedParams(employee.getFirstName(), employee.getLastName());

        //then -verify the output
        assertThat(savedEmployee).isNotNull();
    }
}
