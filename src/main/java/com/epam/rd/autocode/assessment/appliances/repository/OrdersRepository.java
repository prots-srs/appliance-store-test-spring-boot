package com.epam.rd.autocode.assessment.appliances.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epam.rd.autocode.assessment.appliances.model.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

}
