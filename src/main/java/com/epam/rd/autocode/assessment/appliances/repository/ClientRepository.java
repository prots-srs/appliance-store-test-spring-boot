package com.epam.rd.autocode.assessment.appliances.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.epam.rd.autocode.assessment.appliances.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
  List<Client> findByEmailIgnoreCase(String email);
}
