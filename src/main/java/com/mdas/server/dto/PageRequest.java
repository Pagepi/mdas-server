package com.mdas.server.dto;

import lombok.Data;

/**
 * 分页请求参数
 */
@Data
public class PageRequest {
    private Integer page = 1;
    private Integer size = 10;
    private String sortBy;
    private String sortOrder = "asc"; // asc or desc

    public Integer getOffset() {
        return (page - 1) * size;
    }
}