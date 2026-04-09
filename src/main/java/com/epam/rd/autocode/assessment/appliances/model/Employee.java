package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee")
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Employee extends User {
  private String department;

  public Employee(Long id, String name, String email, String password, String department) {
    super(id, name, email, password);
    this.department = department;
  }
}
