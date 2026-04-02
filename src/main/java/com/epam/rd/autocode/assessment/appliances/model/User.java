package com.epam.rd.autocode.assessment.appliances.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class User extends BaseEntity {
  protected String name;
  protected String email;
  protected String password;
}
