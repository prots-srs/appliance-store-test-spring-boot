package com.epam.rd.autocode.assessment.appliances.model;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderRow {

  // @Id
  private Long id;

  @ManyToOne
  @JoinColumn(name = "appliance_id", referencedColumnName = "id")
  private Appliance appliance;

  private Long number;
  private BigDecimal amount;

}
