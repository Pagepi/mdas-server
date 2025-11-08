package com.mdas.server.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 材料基础属性表
 *
 * @TableName material_basic_properties
 */
@Entity
@Table(name = "material_basic_properties")
@Data
public class MaterialBasicProperties implements Serializable {
    /**
     * 自增主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 统一数字代号
     */
    @Column(name = "unified_number_code", length = 50)
    private String unifiedNumberCode;

    /**
     * 原统一数字代号
     */
    @Column(name = "former_unified_number_code", length = 50)
    private String formerUnifiedNumberCode;

    /**
     * 材料牌号
     */
    @Column(name = "material_grade", nullable = false, length = 100)
    private String materialGrade;

    /**
     * 原材料牌号
     */
    @Column(name = "former_material_grade", length = 50)
    private String formerMaterialGrade;

    /**
     * 壁厚(mm)
     */
    @Column(name = "wall_thickness", precision = 10, scale = 3)
    private BigDecimal wallThickness;

    /**
     * 特性概述
     */
    @Column(name = "characteristic_summary", columnDefinition = "TEXT")
    private String characteristicSummary;

    /**
     * 相近牌号
     */
    @Column(name = "similar_grades", length = 500)
    private String similarGrades;

    /**
     * 热处理制度
     */
    @Column(name = "heat_treatment_system", columnDefinition = "TEXT")
    private String heatTreatmentSystem;

    /**
     * 材料牌号来源标准ID
     */
    @Column(name = "grade_standard_id")
    private Integer gradeStandardId;

    /**
     * 材料牌号来源标准代码
     */
    @Column(name = "grade_standard_code", length = 100)
    private String gradeStandardCode;

    /**
     * 材料牌号来源标准名称
     */
    @Column(name = "grade_standard_name", length = 200)
    private String gradeStandardName;

    /**
     * 材料牌号来源标准年份
     */
    @Column(name = "grade_standard_year")
    private Integer gradeStandardYear;

    /**
     * 特性概述来源标准ID
     */
    @Column(name = "characteristic_standard_id")
    private Integer characteristicStandardId;

    /**
     * 特性概述来源标准代码
     */
    @Column(name = "characteristic_standard_code", length = 100)
    private String characteristicStandardCode;

    /**
     * 特性概述来源标准名称
     */
    @Column(name = "characteristic_standard_name", length = 200)
    private String characteristicStandardName;

    /**
     * 特性概述来源标准年份
     */
    @Column(name = "characteristic_standard_year")
    private Integer characteristicStandardYear;

    /**
     * 相近牌号来源标准ID
     */
    @Column(name = "similar_grades_standard_id")
    private Integer similarGradesStandardId;

    /**
     * 相近牌号来源标准代码
     */
    @Column(name = "similar_grades_standard_code", length = 100)
    private String similarGradesStandardCode;

    /**
     * 相近牌号来源标准名称
     */
    @Column(name = "similar_grades_standard_name", length = 200)
    private String similarGradesStandardName;

    /**
     * 相近牌号来源标准年份
     */
    @Column(name = "similar_grades_standard_year")
    private Integer similarGradesStandardYear;

    /**
     * 热处理制度来源标准ID
     */
    @Column(name = "heat_treatment_standard_id")
    private Integer heatTreatmentStandardId;

    /**
     * 热处理制度来源标准代码
     */
    @Column(name = "heat_treatment_standard_code", length = 100)
    private String heatTreatmentStandardCode;

    /**
     * 热处理制度来源标准名称
     */
    @Column(name = "heat_treatment_standard_name", length = 200)
    private String heatTreatmentStandardName;

    /**
     * 热处理制度来源标准年份
     */
    @Column(name = "heat_treatment_standard_year")
    private Integer heatTreatmentStandardYear;

    /**
     * 状态
     */
    @Column(name = "status", length = 20)
    private String status;

    /**
     * 描述
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 创建人id
     */
    @Column(name = "created_by")
    private Integer createdBy;

    /**
     * 创建人帐号
     */
    @Column(name = "created_by_account", length = 50)
    private String createdByAccount;

    /**
     * 创建人姓名
     */
    @Column(name = "created_by_name", length = 50)
    private String createdByName;

    /**
     * 创建时间
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 最后更新人id
     */
    @Column(name = "updated_by")
    private Integer updatedBy;

    /**
     * 最后更新人帐号
     */
    @Column(name = "updated_by_account", length = 50)
    private String updatedByAccount;

    /**
     * 最后更新人姓名
     */
    @Column(name = "updated_by_name", length = 50)
    private String updatedByName;

    /**
     * 最后更新时间
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 版本号（从1开始递增）
     */
    @Column(name = "version_number", nullable = false)
    private Integer versionNumber = 1;

    /**
     * 版本备注
     */
    @Column(name = "version_remark", length = 500)
    private String versionRemark;

    /**
     * 金相组织（如：马氏体、珠光体、奥氏体等）
     */
    @Column(name = "metallographic_structure", length = 200)
    private String metallographicStructure;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        MaterialBasicProperties other = (MaterialBasicProperties) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUnifiedNumberCode() == null ? other.getUnifiedNumberCode() == null : this.getUnifiedNumberCode().equals(other.getUnifiedNumberCode()))
                && (this.getFormerUnifiedNumberCode() == null ? other.getFormerUnifiedNumberCode() == null : this.getFormerUnifiedNumberCode().equals(other.getFormerUnifiedNumberCode()))
                && (this.getMaterialGrade() == null ? other.getMaterialGrade() == null : this.getMaterialGrade().equals(other.getMaterialGrade()))
                && (this.getFormerMaterialGrade() == null ? other.getFormerMaterialGrade() == null : this.getFormerMaterialGrade().equals(other.getFormerMaterialGrade()))
                && (this.getWallThickness() == null ? other.getWallThickness() == null : this.getWallThickness().equals(other.getWallThickness()))
                && (this.getCharacteristicSummary() == null ? other.getCharacteristicSummary() == null : this.getCharacteristicSummary().equals(other.getCharacteristicSummary()))
                && (this.getSimilarGrades() == null ? other.getSimilarGrades() == null : this.getSimilarGrades().equals(other.getSimilarGrades()))
                && (this.getHeatTreatmentSystem() == null ? other.getHeatTreatmentSystem() == null : this.getHeatTreatmentSystem().equals(other.getHeatTreatmentSystem()))
                && (this.getGradeStandardId() == null ? other.getGradeStandardId() == null : this.getGradeStandardId().equals(other.getGradeStandardId()))
                && (this.getGradeStandardCode() == null ? other.getGradeStandardCode() == null : this.getGradeStandardCode().equals(other.getGradeStandardCode()))
                && (this.getGradeStandardName() == null ? other.getGradeStandardName() == null : this.getGradeStandardName().equals(other.getGradeStandardName()))
                && (this.getGradeStandardYear() == null ? other.getGradeStandardYear() == null : this.getGradeStandardYear().equals(other.getGradeStandardYear()))
                && (this.getCharacteristicStandardId() == null ? other.getCharacteristicStandardId() == null : this.getCharacteristicStandardId().equals(other.getCharacteristicStandardId()))
                && (this.getCharacteristicStandardCode() == null ? other.getCharacteristicStandardCode() == null : this.getCharacteristicStandardCode().equals(other.getCharacteristicStandardCode()))
                && (this.getCharacteristicStandardName() == null ? other.getCharacteristicStandardName() == null : this.getCharacteristicStandardName().equals(other.getCharacteristicStandardName()))
                && (this.getCharacteristicStandardYear() == null ? other.getCharacteristicStandardYear() == null : this.getCharacteristicStandardYear().equals(other.getCharacteristicStandardYear()))
                && (this.getSimilarGradesStandardId() == null ? other.getSimilarGradesStandardId() == null : this.getSimilarGradesStandardId().equals(other.getSimilarGradesStandardId()))
                && (this.getSimilarGradesStandardCode() == null ? other.getSimilarGradesStandardCode() == null : this.getSimilarGradesStandardCode().equals(other.getSimilarGradesStandardCode()))
                && (this.getSimilarGradesStandardName() == null ? other.getSimilarGradesStandardName() == null : this.getSimilarGradesStandardName().equals(other.getSimilarGradesStandardName()))
                && (this.getSimilarGradesStandardYear() == null ? other.getSimilarGradesStandardYear() == null : this.getSimilarGradesStandardYear().equals(other.getSimilarGradesStandardYear()))
                && (this.getHeatTreatmentStandardId() == null ? other.getHeatTreatmentStandardId() == null : this.getHeatTreatmentStandardId().equals(other.getHeatTreatmentStandardId()))
                && (this.getHeatTreatmentStandardCode() == null ? other.getHeatTreatmentStandardCode() == null : this.getHeatTreatmentStandardCode().equals(other.getHeatTreatmentStandardCode()))
                && (this.getHeatTreatmentStandardName() == null ? other.getHeatTreatmentStandardName() == null : this.getHeatTreatmentStandardName().equals(other.getHeatTreatmentStandardName()))
                && (this.getHeatTreatmentStandardYear() == null ? other.getHeatTreatmentStandardYear() == null : this.getHeatTreatmentStandardYear().equals(other.getHeatTreatmentStandardYear()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getCreatedBy() == null ? other.getCreatedBy() == null : this.getCreatedBy().equals(other.getCreatedBy()))
                && (this.getCreatedByAccount() == null ? other.getCreatedByAccount() == null : this.getCreatedByAccount().equals(other.getCreatedByAccount()))
                && (this.getCreatedByName() == null ? other.getCreatedByName() == null : this.getCreatedByName().equals(other.getCreatedByName()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getUpdatedBy() == null ? other.getUpdatedBy() == null : this.getUpdatedBy().equals(other.getUpdatedBy()))
                && (this.getUpdatedByAccount() == null ? other.getUpdatedByAccount() == null : this.getUpdatedByAccount().equals(other.getUpdatedByAccount()))
                && (this.getUpdatedByName() == null ? other.getUpdatedByName() == null : this.getUpdatedByName().equals(other.getUpdatedByName()))
                && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
                && (this.getVersionNumber() == null ? other.getVersionNumber() == null : this.getVersionNumber().equals(other.getVersionNumber()))
                && (this.getVersionRemark() == null ? other.getVersionRemark() == null : this.getVersionRemark().equals(other.getVersionRemark()))
                && (this.getMetallographicStructure() == null ? other.getMetallographicStructure() == null : this.getMetallographicStructure().equals(other.getMetallographicStructure()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUnifiedNumberCode() == null) ? 0 : getUnifiedNumberCode().hashCode());
        result = prime * result + ((getFormerUnifiedNumberCode() == null) ? 0 : getFormerUnifiedNumberCode().hashCode());
        result = prime * result + ((getMaterialGrade() == null) ? 0 : getMaterialGrade().hashCode());
        result = prime * result + ((getFormerMaterialGrade() == null) ? 0 : getFormerMaterialGrade().hashCode());
        result = prime * result + ((getWallThickness() == null) ? 0 : getWallThickness().hashCode());
        result = prime * result + ((getCharacteristicSummary() == null) ? 0 : getCharacteristicSummary().hashCode());
        result = prime * result + ((getSimilarGrades() == null) ? 0 : getSimilarGrades().hashCode());
        result = prime * result + ((getHeatTreatmentSystem() == null) ? 0 : getHeatTreatmentSystem().hashCode());
        result = prime * result + ((getGradeStandardId() == null) ? 0 : getGradeStandardId().hashCode());
        result = prime * result + ((getGradeStandardCode() == null) ? 0 : getGradeStandardCode().hashCode());
        result = prime * result + ((getGradeStandardName() == null) ? 0 : getGradeStandardName().hashCode());
        result = prime * result + ((getGradeStandardYear() == null) ? 0 : getGradeStandardYear().hashCode());
        result = prime * result + ((getCharacteristicStandardId() == null) ? 0 : getCharacteristicStandardId().hashCode());
        result = prime * result + ((getCharacteristicStandardCode() == null) ? 0 : getCharacteristicStandardCode().hashCode());
        result = prime * result + ((getCharacteristicStandardName() == null) ? 0 : getCharacteristicStandardName().hashCode());
        result = prime * result + ((getCharacteristicStandardYear() == null) ? 0 : getCharacteristicStandardYear().hashCode());
        result = prime * result + ((getSimilarGradesStandardId() == null) ? 0 : getSimilarGradesStandardId().hashCode());
        result = prime * result + ((getSimilarGradesStandardCode() == null) ? 0 : getSimilarGradesStandardCode().hashCode());
        result = prime * result + ((getSimilarGradesStandardName() == null) ? 0 : getSimilarGradesStandardName().hashCode());
        result = prime * result + ((getSimilarGradesStandardYear() == null) ? 0 : getSimilarGradesStandardYear().hashCode());
        result = prime * result + ((getHeatTreatmentStandardId() == null) ? 0 : getHeatTreatmentStandardId().hashCode());
        result = prime * result + ((getHeatTreatmentStandardCode() == null) ? 0 : getHeatTreatmentStandardCode().hashCode());
        result = prime * result + ((getHeatTreatmentStandardName() == null) ? 0 : getHeatTreatmentStandardName().hashCode());
        result = prime * result + ((getHeatTreatmentStandardYear() == null) ? 0 : getHeatTreatmentStandardYear().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCreatedBy() == null) ? 0 : getCreatedBy().hashCode());
        result = prime * result + ((getCreatedByAccount() == null) ? 0 : getCreatedByAccount().hashCode());
        result = prime * result + ((getCreatedByName() == null) ? 0 : getCreatedByName().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedBy() == null) ? 0 : getUpdatedBy().hashCode());
        result = prime * result + ((getUpdatedByAccount() == null) ? 0 : getUpdatedByAccount().hashCode());
        result = prime * result + ((getUpdatedByName() == null) ? 0 : getUpdatedByName().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getVersionNumber() == null) ? 0 : getVersionNumber().hashCode());
        result = prime * result + ((getVersionRemark() == null) ? 0 : getVersionRemark().hashCode());
        result = prime * result + ((getMetallographicStructure() == null) ? 0 : getMetallographicStructure().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", unifiedNumberCode=").append(unifiedNumberCode);
        sb.append(", formerUnifiedNumberCode=").append(formerUnifiedNumberCode);
        sb.append(", materialGrade=").append(materialGrade);
        sb.append(", formerMaterialGrade=").append(formerMaterialGrade);
        sb.append(", wallThickness=").append(wallThickness);
        sb.append(", characteristicSummary=").append(characteristicSummary);
        sb.append(", similarGrades=").append(similarGrades);
        sb.append(", heatTreatmentSystem=").append(heatTreatmentSystem);
        sb.append(", gradeStandardId=").append(gradeStandardId);
        sb.append(", gradeStandardCode=").append(gradeStandardCode);
        sb.append(", gradeStandardName=").append(gradeStandardName);
        sb.append(", gradeStandardYear=").append(gradeStandardYear);
        sb.append(", characteristicStandardId=").append(characteristicStandardId);
        sb.append(", characteristicStandardCode=").append(characteristicStandardCode);
        sb.append(", characteristicStandardName=").append(characteristicStandardName);
        sb.append(", characteristicStandardYear=").append(characteristicStandardYear);
        sb.append(", similarGradesStandardId=").append(similarGradesStandardId);
        sb.append(", similarGradesStandardCode=").append(similarGradesStandardCode);
        sb.append(", similarGradesStandardName=").append(similarGradesStandardName);
        sb.append(", similarGradesStandardYear=").append(similarGradesStandardYear);
        sb.append(", heatTreatmentStandardId=").append(heatTreatmentStandardId);
        sb.append(", heatTreatmentStandardCode=").append(heatTreatmentStandardCode);
        sb.append(", heatTreatmentStandardName=").append(heatTreatmentStandardName);
        sb.append(", heatTreatmentStandardYear=").append(heatTreatmentStandardYear);
        sb.append(", status=").append(status);
        sb.append(", description=").append(description);
        sb.append(", createdBy=").append(createdBy);
        sb.append(", createdByAccount=").append(createdByAccount);
        sb.append(", createdByName=").append(createdByName);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedBy=").append(updatedBy);
        sb.append(", updatedByAccount=").append(updatedByAccount);
        sb.append(", updatedByName=").append(updatedByName);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", versionNumber=").append(versionNumber);
        sb.append(", versionRemark=").append(versionRemark);
        sb.append(", metallographicStructure=").append(metallographicStructure);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}