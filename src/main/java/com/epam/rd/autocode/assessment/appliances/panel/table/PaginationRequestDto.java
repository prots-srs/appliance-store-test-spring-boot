package com.epam.rd.autocode.assessment.appliances.panel.table;

public record PaginationRequestDto(
    Integer page,
    Integer size,
    String sort) {
}
