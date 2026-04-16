package com.epam.rd.autocode.assessment.appliances.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import com.epam.rd.autocode.assessment.appliances.formbuilder.FormValuesDto;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.panel.PanelService;
import com.epam.rd.autocode.assessment.appliances.panel.forms.OrderFormResult;
import com.epam.rd.autocode.assessment.appliances.panel.table.OrderViewDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationRequestDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.TableDto;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;

@Service
public class OrderServiceImpl implements PanelService<OrderViewDto, OrderFormResult> {

  private OrdersRepository repo;
  private Locale locale;

  public OrderServiceImpl(OrdersRepository ordersRepository) {
    this.repo = ordersRepository;

    locale = LocaleContextHolder.getLocale();

  }

  @Override
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

  @Override
  public FormValuesDto getForm(@Nullable Long id, @Nullable OrderFormResult item,
      @Nullable List<FieldError> fieldErrors) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getForm'");
  }

  @Override
  public Long create(OrderFormResult item) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'create'");
  }

  @Override
  public void update(Long id, OrderFormResult item) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public Boolean delete(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }

}
