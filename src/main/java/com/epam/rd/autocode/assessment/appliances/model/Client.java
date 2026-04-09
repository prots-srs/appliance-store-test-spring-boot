package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "client")
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Client extends User {
  private String card;

  public Client(Long id, String name, String email, String password, String card) {
    super(id, name, email, password);
    this.card = card;
  }
}
