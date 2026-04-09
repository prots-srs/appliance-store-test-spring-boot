package com.epam.rd.autocode.assessment.appliances.panel.table;

import java.util.List;

public record TableDto<T>(
    List<T> list,
    PaginationDto pagination) {
}
