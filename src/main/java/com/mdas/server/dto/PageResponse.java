package com.mdas.server.dto;

import lombok.Data;
import java.util.List;

/**
 * 分页响应结果
 */
@Data
public class PageResponse<T> {
    private List<T> content;
    private Integer totalPages;
    private Long totalElements;
    private Integer currentPage;
    private Integer pageSize;
    private Boolean first;
    private Boolean last;

    public static <T> PageResponse<T> of(List<T> content, Long totalElements, Integer page, Integer size) {
        PageResponse<T> response = new PageResponse<>();
        response.setContent(content);
        response.setTotalElements(totalElements);
        response.setCurrentPage(page);
        response.setPageSize(size);
        response.setTotalPages((int) Math.ceil((double) totalElements / size));
        response.setFirst(page == 1);
        response.setLast(page >= response.getTotalPages());
        return response;
    }
}