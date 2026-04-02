package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "client")
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class Client extends User {
  private String card;
}
