package com.volvadvit.vaacation.repo;

import com.volvadvit.vaacation.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    @Query("from Employee e " +
            "where concat(e.firstName, ' ', e.lastName) " +
            "like concat('%', :name, '%') ")
    List<Employee> findByName(@Param("name") String name);
}
