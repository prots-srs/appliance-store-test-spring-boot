package com.epam.rd.autocode.assessment.appliances.panel.table;

public record PaginationDto(
    Integer currentPage,
    Long totalElements,
    Integer totalPages,
    Integer pageSize) {

}
