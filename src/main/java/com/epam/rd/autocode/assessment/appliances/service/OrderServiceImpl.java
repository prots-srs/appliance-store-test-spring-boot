package com.epam.rd.autocode.assessment.appliances.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.epam.rd.autocode.assessment.appliances.exceptions.InvalidProcessOrders;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.panel.table.OrderViewDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationRequestDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.TableDto;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;

@Service
public class OrderServiceImpl {

  private OrdersRepository repo;
  private Locale locale;
  private EmployeeRepository employeeRepository;

  public OrderServiceImpl(OrdersRepository ordersRepository,
      EmployeeRepository employeeRepository) {
    this.repo = ordersRepository;
    this.employeeRepository = employeeRepository;

    locale = LocaleContextHolder.getLocale();

  }

  public TableDto<OrderViewDto> getTable(PaginationRequestDto page) {
    int currentPage = page.page() > 0 ? page.page() - 1 : 0;
    int showSize = page.size() > 0 ? page.size() : 3;

    Pageable pageable = page.sort() != null && !page.sort().isEmpty()
        ? PageRequest.of(currentPage, showSize, Sort.by(page.sort()).ascending())
        : PageRequest.of(currentPage, showSize, Sort.by("id").descending());

    Page<Orders> resultQuery = repo.findAll(pageable);

    // map
    List<OrderViewDto> list = new ArrayList<>();
    resultQuery.getContent().forEach(e -> list.add(OrderViewDto.convertFromEntity(e, locale)));

    return new TableDto<OrderViewDto>(list,
        new PaginationDto(currentPage + 1,
            resultQuery.getTotalElements(),
            resultQuery.getTotalPages(),
            showSize));
  }

  public void approveOrder(Long orderId) {

    if (orderId == null) {
      throw new InvalidProcessOrders("Order id is wrong");
    }

    System.out.println("approve orderid:" + orderId);

    Optional<Orders> order = repo.findById(orderId);
    if (order.isPresent()) {

      var o = order.get();
      o.setApproved(true);

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      List<Employee> employee = employeeRepository.findByEmailIgnoreCase(authentication.getName());
      if (employee.size() > 0) {
        o.setEmployee(employee.get(0));
      }

      repo.saveAndFlush(o);
    }

  }
}
