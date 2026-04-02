package com.epam.rd.autocode.assessment.appliances.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public abstract class BaseEntity {
  @Id
  @GeneratedValue
  protected Long id;

  @CreationTimestamp
  @Column(updatable = false)
  protected LocalDateTime createdOn;

  @UpdateTimestamp
  protected LocalDateTime updatedOn;
}
