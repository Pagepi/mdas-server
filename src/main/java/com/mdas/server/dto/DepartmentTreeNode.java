package com.mdas.server.dto;
// package com.mdas.server.dto;

import lombok.Data;
import java.util.List;

/**
 * 部门树节点DTO（用于前端树形组件）
 */
@Data
public class DepartmentTreeNode {
    private String key; // departmentCode
    private String title; // departmentName
    private String value; // departmentCode
    private Integer level;
    private String departmentType;
    private Integer sortOrder;
    private Boolean isLeaf; // 修复：添加 isLeaf 字段
    private List<DepartmentTreeNode> children;

    public DepartmentTreeNode(String key, String title) {
        this.key = key;
        this.title = title;
        this.value = key;
        this.isLeaf = false; // 默认不是叶子节点
    }

    // 如果需要，可以添加便捷方法
    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
}