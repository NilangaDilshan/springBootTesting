package com.dilshan.testing.repository;

import com.dilshan.testing.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * @param email string
     * @return Optional Employee
     */
    Optional<Employee> findByEmail(String email);

    /**
     * @param firstName string
     * @param lastName  string
     * @return Employee
     * Define custom query using JPQL with index parameters
     */
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByJPQLIndexParams(String firstName, String lastName);

    /**
     * @param firstName string
     * @param lastName  string
     * @return Employee
     * Define custom query using JPQL with named parameters
     */
    @Query("select e from Employee e where e.firstName = :firstName and e.lastName = :lastName")
    Employee findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);


    /**
     * @param firstName string
     * @param lastName  string
     * @return Employee
     * Define custom query using native sql with index params
     */
    @Query(value = "select * from employees e where e.first_name = ?1 and e.last_name = ?2", nativeQuery = true)
    Employee findByNativeSqlWithIndexParams(String firstName, String lastName);

    /**
     * @param firstName string
     * @param lastName  string
     * @return Employee
     * Define custom query using native sql with named params
     */
    @Query(value = "select * from employees e where e.first_name = :firstName and e.last_name = :lastName", nativeQuery = true)
    Employee findByNativeSqlWithNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
