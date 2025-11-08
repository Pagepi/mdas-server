package com.mdas.server.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 材料许用应力历史表
 *
 * @TableName material_allowable_stress_history
 */
@Entity
@Table(name = "material_allowable_stress_history")
@Data
public class MaterialAllowableStressHistory implements Serializable {
    /**
     * 自增主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 关联基础属性表ID
     */
    @Column(name = "basic_property_id", nullable = false)
    private Integer basicPropertyId;

    /**
     * 20度许用应力值(MPa)
     */
    @Column(name = "stress_20c", precision = 8, scale = 2)
    private BigDecimal stress20c;

    /**
     * 100度许用应力值(MPa)
     */
    @Column(name = "stress_100c", precision = 8, scale = 2)
    private BigDecimal stress100c;

    /**
     * 200度许用应力值(MPa)
     */
    @Column(name = "stress_200c", precision = 8, scale = 2)
    private BigDecimal stress200c;

    /**
     * 250度许用应力值(MPa)
     */
    @Column(name = "stress_250c", precision = 8, scale = 2)
    private BigDecimal stress250c;

    /**
     * 260度许用应力值(MPa)
     */
    @Column(name = "stress_260c", precision = 8, scale = 2)
    private BigDecimal stress260c;

    /**
     * 280度许用应力值(MPa)
     */
    @Column(name = "stress_280c", precision = 8, scale = 2)
    private BigDecimal stress280c;

    /**
     * 300度许用应力值(MPa)
     */
    @Column(name = "stress_300c", precision = 8, scale = 2)
    private BigDecimal stress300c;

    /**
     * 320度许用应力值(MPa)
     */
    @Column(name = "stress_320c", precision = 8, scale = 2)
    private BigDecimal stress320c;

    /**
     * 340度许用应力值(MPa)
     */
    @Column(name = "stress_340c", precision = 8, scale = 2)
    private BigDecimal stress340c;

    /**
     * 350度许用应力值(MPa)
     */
    @Column(name = "stress_350c", precision = 8, scale = 2)
    private BigDecimal stress350c;

    /**
     * 360度许用应力值(MPa)
     */
    @Column(name = "stress_360c", precision = 8, scale = 2)
    private BigDecimal stress360c;

    /**
     * 370度许用应力值(MPa)
     */
    @Column(name = "stress_370c", precision = 8, scale = 2)
    private BigDecimal stress370c;

    /**
     * 380度许用应力值(MPa)
     */
    @Column(name = "stress_380c", precision = 8, scale = 2)
    private BigDecimal stress380c;

    /**
     * 390度许用应力值(MPa)
     */
    @Column(name = "stress_390c", precision = 8, scale = 2)
    private BigDecimal stress390c;

    /**
     * 400度许用应力值(MPa)
     */
    @Column(name = "stress_400c", precision = 8, scale = 2)
    private BigDecimal stress400c;

    /**
     * 410度许用应力值(MPa)
     */
    @Column(name = "stress_410c", precision = 8, scale = 2)
    private BigDecimal stress410c;

    /**
     * 420度许用应力值(MPa)
     */
    @Column(name = "stress_420c", precision = 8, scale = 2)
    private BigDecimal stress420c;

    /**
     * 430度许用应力值(MPa)
     */
    @Column(name = "stress_430c", precision = 8, scale = 2)
    private BigDecimal stress430c;

    /**
     * 440度许用应力值(MPa)
     */
    @Column(name = "stress_440c", precision = 8, scale = 2)
    private BigDecimal stress440c;

    /**
     * 450度许用应力值(MPa)
     */
    @Column(name = "stress_450c", precision = 8, scale = 2)
    private BigDecimal stress450c;

    /**
     * 460度许用应力值(MPa)
     */
    @Column(name = "stress_460c", precision = 8, scale = 2)
    private BigDecimal stress460c;

    /**
     * 470度许用应力值(MPa)
     */
    @Column(name = "stress_470c", precision = 8, scale = 2)
    private BigDecimal stress470c;

    /**
     * 480度许用应力值(MPa)
     */
    @Column(name = "stress_480c", precision = 8, scale = 2)
    private BigDecimal stress480c;

    /**
     * 490度许用应力值(MPa)
     */
    @Column(name = "stress_490c", precision = 8, scale = 2)
    private BigDecimal stress490c;

    /**
     * 500度许用应力值(MPa)
     */
    @Column(name = "stress_500c", precision = 8, scale = 2)
    private BigDecimal stress500c;

    /**
     * 510度许用应力值(MPa)
     */
    @Column(name = "stress_510c", precision = 8, scale = 2)
    private BigDecimal stress510c;

    /**
     * 520度许用应力值(MPa)
     */
    @Column(name = "stress_520c", precision = 8, scale = 2)
    private BigDecimal stress520c;

    /**
     * 530度许用应力值(MPa)
     */
    @Column(name = "stress_530c", precision = 8, scale = 2)
    private BigDecimal stress530c;

    /**
     * 540度许用应力值(MPa)
     */
    @Column(name = "stress_540c", precision = 8, scale = 2)
    private BigDecimal stress540c;

    /**
     * 550度许用应力值(MPa)
     */
    @Column(name = "stress_550c", precision = 8, scale = 2)
    private BigDecimal stress550c;

    /**
     * 560度许用应力值(MPa)
     */
    @Column(name = "stress_560c", precision = 8, scale = 2)
    private BigDecimal stress560c;

    /**
     * 570度许用应力值(MPa)
     */
    @Column(name = "stress_570c", precision = 8, scale = 2)
    private BigDecimal stress570c;

    /**
     * 580度许用应力值(MPa)
     */
    @Column(name = "stress_580c", precision = 8, scale = 2)
    private BigDecimal stress580c;

    /**
     * 许用应力来源标准id
     */
    @Column(name = "allowable_stress_standard_id")
    private Integer allowableStressStandardId;

    /**
     * 许用应力来源标准代码
     */
    @Column(name = "allowable_stress_standard_code", length = 100)
    private String allowableStressStandardCode;

    /**
     * 许用应力来源标准名称
     */
    @Column(name = "allowable_stress_standard_name", length = 200)
    private String allowableStressStandardName;

    /**
     * 许用应力来源标准颁发年份
     */
    @Column(name = "allowable_stress_standard_year")
    private Integer allowableStressStandardYear;

    /**
     * 状态
     */
    @Column(name = "status", length = 20)
    private String status;

    /**
     * 描述信息
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
     * 原记录ID
     */
    @Column(name = "original_record_id", nullable = false)
    private Integer originalRecordId;

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
        MaterialAllowableStressHistory other = (MaterialAllowableStressHistory) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getBasicPropertyId() == null ? other.getBasicPropertyId() == null : this.getBasicPropertyId().equals(other.getBasicPropertyId()))
                && (this.getStress20c() == null ? other.getStress20c() == null : this.getStress20c().equals(other.getStress20c()))
                && (this.getStress100c() == null ? other.getStress100c() == null : this.getStress100c().equals(other.getStress100c()))
                && (this.getStress200c() == null ? other.getStress200c() == null : this.getStress200c().equals(other.getStress200c()))
                && (this.getStress250c() == null ? other.getStress250c() == null : this.getStress250c().equals(other.getStress250c()))
                && (this.getStress260c() == null ? other.getStress260c() == null : this.getStress260c().equals(other.getStress260c()))
                && (this.getStress280c() == null ? other.getStress280c() == null : this.getStress280c().equals(other.getStress280c()))
                && (this.getStress300c() == null ? other.getStress300c() == null : this.getStress300c().equals(other.getStress300c()))
                && (this.getStress320c() == null ? other.getStress320c() == null : this.getStress320c().equals(other.getStress320c()))
                && (this.getStress340c() == null ? other.getStress340c() == null : this.getStress340c().equals(other.getStress340c()))
                && (this.getStress350c() == null ? other.getStress350c() == null : this.getStress350c().equals(other.getStress350c()))
                && (this.getStress360c() == null ? other.getStress360c() == null : this.getStress360c().equals(other.getStress360c()))
                && (this.getStress370c() == null ? other.getStress370c() == null : this.getStress370c().equals(other.getStress370c()))
                && (this.getStress380c() == null ? other.getStress380c() == null : this.getStress380c().equals(other.getStress380c()))
                && (this.getStress390c() == null ? other.getStress390c() == null : this.getStress390c().equals(other.getStress390c()))
                && (this.getStress400c() == null ? other.getStress400c() == null : this.getStress400c().equals(other.getStress400c()))
                && (this.getStress410c() == null ? other.getStress410c() == null : this.getStress410c().equals(other.getStress410c()))
                && (this.getStress420c() == null ? other.getStress420c() == null : this.getStress420c().equals(other.getStress420c()))
                && (this.getStress430c() == null ? other.getStress430c() == null : this.getStress430c().equals(other.getStress430c()))
                && (this.getStress440c() == null ? other.getStress440c() == null : this.getStress440c().equals(other.getStress440c()))
                && (this.getStress450c() == null ? other.getStress450c() == null : this.getStress450c().equals(other.getStress450c()))
                && (this.getStress460c() == null ? other.getStress460c() == null : this.getStress460c().equals(other.getStress460c()))
                && (this.getStress470c() == null ? other.getStress470c() == null : this.getStress470c().equals(other.getStress470c()))
                && (this.getStress480c() == null ? other.getStress480c() == null : this.getStress480c().equals(other.getStress480c()))
                && (this.getStress490c() == null ? other.getStress490c() == null : this.getStress490c().equals(other.getStress490c()))
                && (this.getStress500c() == null ? other.getStress500c() == null : this.getStress500c().equals(other.getStress500c()))
                && (this.getStress510c() == null ? other.getStress510c() == null : this.getStress510c().equals(other.getStress510c()))
                && (this.getStress520c() == null ? other.getStress520c() == null : this.getStress520c().equals(other.getStress520c()))
                && (this.getStress530c() == null ? other.getStress530c() == null : this.getStress530c().equals(other.getStress530c()))
                && (this.getStress540c() == null ? other.getStress540c() == null : this.getStress540c().equals(other.getStress540c()))
                && (this.getStress550c() == null ? other.getStress550c() == null : this.getStress550c().equals(other.getStress550c()))
                && (this.getStress560c() == null ? other.getStress560c() == null : this.getStress560c().equals(other.getStress560c()))
                && (this.getStress570c() == null ? other.getStress570c() == null : this.getStress570c().equals(other.getStress570c()))
                && (this.getStress580c() == null ? other.getStress580c() == null : this.getStress580c().equals(other.getStress580c()))
                && (this.getAllowableStressStandardId() == null ? other.getAllowableStressStandardId() == null : this.getAllowableStressStandardId().equals(other.getAllowableStressStandardId()))
                && (this.getAllowableStressStandardCode() == null ? other.getAllowableStressStandardCode() == null : this.getAllowableStressStandardCode().equals(other.getAllowableStressStandardCode()))
                && (this.getAllowableStressStandardName() == null ? other.getAllowableStressStandardName() == null : this.getAllowableStressStandardName().equals(other.getAllowableStressStandardName()))
                && (this.getAllowableStressStandardYear() == null ? other.getAllowableStressStandardYear() == null : this.getAllowableStressStandardYear().equals(other.getAllowableStressStandardYear()))
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
                && (this.getOriginalRecordId() == null ? other.getOriginalRecordId() == null : this.getOriginalRecordId().equals(other.getOriginalRecordId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBasicPropertyId() == null) ? 0 : getBasicPropertyId().hashCode());
        result = prime * result + ((getStress20c() == null) ? 0 : getStress20c().hashCode());
        result = prime * result + ((getStress100c() == null) ? 0 : getStress100c().hashCode());
        result = prime * result + ((getStress200c() == null) ? 0 : getStress200c().hashCode());
        result = prime * result + ((getStress250c() == null) ? 0 : getStress250c().hashCode());
        result = prime * result + ((getStress260c() == null) ? 0 : getStress260c().hashCode());
        result = prime * result + ((getStress280c() == null) ? 0 : getStress280c().hashCode());
        result = prime * result + ((getStress300c() == null) ? 0 : getStress300c().hashCode());
        result = prime * result + ((getStress320c() == null) ? 0 : getStress320c().hashCode());
        result = prime * result + ((getStress340c() == null) ? 0 : getStress340c().hashCode());
        result = prime * result + ((getStress350c() == null) ? 0 : getStress350c().hashCode());
        result = prime * result + ((getStress360c() == null) ? 0 : getStress360c().hashCode());
        result = prime * result + ((getStress370c() == null) ? 0 : getStress370c().hashCode());
        result = prime * result + ((getStress380c() == null) ? 0 : getStress380c().hashCode());
        result = prime * result + ((getStress390c() == null) ? 0 : getStress390c().hashCode());
        result = prime * result + ((getStress400c() == null) ? 0 : getStress400c().hashCode());
        result = prime * result + ((getStress410c() == null) ? 0 : getStress410c().hashCode());
        result = prime * result + ((getStress420c() == null) ? 0 : getStress420c().hashCode());
        result = prime * result + ((getStress430c() == null) ? 0 : getStress430c().hashCode());
        result = prime * result + ((getStress440c() == null) ? 0 : getStress440c().hashCode());
        result = prime * result + ((getStress450c() == null) ? 0 : getStress450c().hashCode());
        result = prime * result + ((getStress460c() == null) ? 0 : getStress460c().hashCode());
        result = prime * result + ((getStress470c() == null) ? 0 : getStress470c().hashCode());
        result = prime * result + ((getStress480c() == null) ? 0 : getStress480c().hashCode());
        result = prime * result + ((getStress490c() == null) ? 0 : getStress490c().hashCode());
        result = prime * result + ((getStress500c() == null) ? 0 : getStress500c().hashCode());
        result = prime * result + ((getStress510c() == null) ? 0 : getStress510c().hashCode());
        result = prime * result + ((getStress520c() == null) ? 0 : getStress520c().hashCode());
        result = prime * result + ((getStress530c() == null) ? 0 : getStress530c().hashCode());
        result = prime * result + ((getStress540c() == null) ? 0 : getStress540c().hashCode());
        result = prime * result + ((getStress550c() == null) ? 0 : getStress550c().hashCode());
        result = prime * result + ((getStress560c() == null) ? 0 : getStress560c().hashCode());
        result = prime * result + ((getStress570c() == null) ? 0 : getStress570c().hashCode());
        result = prime * result + ((getStress580c() == null) ? 0 : getStress580c().hashCode());
        result = prime * result + ((getAllowableStressStandardId() == null) ? 0 : getAllowableStressStandardId().hashCode());
        result = prime * result + ((getAllowableStressStandardCode() == null) ? 0 : getAllowableStressStandardCode().hashCode());
        result = prime * result + ((getAllowableStressStandardName() == null) ? 0 : getAllowableStressStandardName().hashCode());
        result = prime * result + ((getAllowableStressStandardYear() == null) ? 0 : getAllowableStressStandardYear().hashCode());
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
        result = prime * result + ((getOriginalRecordId() == null) ? 0 : getOriginalRecordId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", basicPropertyId=").append(basicPropertyId);
        sb.append(", stress20c=").append(stress20c);
        sb.append(", stress100c=").append(stress100c);
        sb.append(", stress200c=").append(stress200c);
        sb.append(", stress250c=").append(stress250c);
        sb.append(", stress260c=").append(stress260c);
        sb.append(", stress280c=").append(stress280c);
        sb.append(", stress300c=").append(stress300c);
        sb.append(", stress320c=").append(stress320c);
        sb.append(", stress340c=").append(stress340c);
        sb.append(", stress350c=").append(stress350c);
        sb.append(", stress360c=").append(stress360c);
        sb.append(", stress370c=").append(stress370c);
        sb.append(", stress380c=").append(stress380c);
        sb.append(", stress390c=").append(stress390c);
        sb.append(", stress400c=").append(stress400c);
        sb.append(", stress410c=").append(stress410c);
        sb.append(", stress420c=").append(stress420c);
        sb.append(", stress430c=").append(stress430c);
        sb.append(", stress440c=").append(stress440c);
        sb.append(", stress450c=").append(stress450c);
        sb.append(", stress460c=").append(stress460c);
        sb.append(", stress470c=").append(stress470c);
        sb.append(", stress480c=").append(stress480c);
        sb.append(", stress490c=").append(stress490c);
        sb.append(", stress500c=").append(stress500c);
        sb.append(", stress510c=").append(stress510c);
        sb.append(", stress520c=").append(stress520c);
        sb.append(", stress530c=").append(stress530c);
        sb.append(", stress540c=").append(stress540c);
        sb.append(", stress550c=").append(stress550c);
        sb.append(", stress560c=").append(stress560c);
        sb.append(", stress570c=").append(stress570c);
        sb.append(", stress580c=").append(stress580c);
        sb.append(", allowableStressStandardId=").append(allowableStressStandardId);
        sb.append(", allowableStressStandardCode=").append(allowableStressStandardCode);
        sb.append(", allowableStressStandardName=").append(allowableStressStandardName);
        sb.append(", allowableStressStandardYear=").append(allowableStressStandardYear);
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
        sb.append(", originalRecordId=").append(originalRecordId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}