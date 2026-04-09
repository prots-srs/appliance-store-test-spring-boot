package com.epam.rd.autocode.assessment.appliances.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epam.rd.autocode.assessment.appliances.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
