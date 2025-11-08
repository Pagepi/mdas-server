package com.mdas.server.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 材料化学成分表
 *
 * @TableName material_chemical_composition
 */
@Entity
@Table(name = "material_chemical_composition")
@Data
public class MaterialChemicalComposition implements Serializable {
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
     * 碳含量下限(%)
     */
    @Column(name = "carbon_min", precision = 8, scale = 5)
    private BigDecimal carbonMin;

    /**
     * 碳含量上限(%)
     */
    @Column(name = "carbon_max", precision = 8, scale = 5)
    private BigDecimal carbonMax;

    /**
     * 硅含量下限(%)
     */
    @Column(name = "silicon_min", precision = 8, scale = 5)
    private BigDecimal siliconMin;

    /**
     * 硅含量上限(%)
     */
    @Column(name = "silicon_max", precision = 8, scale = 5)
    private BigDecimal siliconMax;

    /**
     * 锰含量下限(%)
     */
    @Column(name = "manganese_min", precision = 8, scale = 5)
    private BigDecimal manganeseMin;

    /**
     * 锰含量上限(%)
     */
    @Column(name = "manganese_max", precision = 8, scale = 5)
    private BigDecimal manganeseMax;

    /**
     * 铬含量下限(%)
     */
    @Column(name = "chromium_min", precision = 8, scale = 5)
    private BigDecimal chromiumMin;

    /**
     * 铬含量上限(%)
     */
    @Column(name = "chromium_max", precision = 8, scale = 5)
    private BigDecimal chromiumMax;

    /**
     * 钼含量下限(%)
     */
    @Column(name = "molybdenum_min", precision = 8, scale = 5)
    private BigDecimal molybdenumMin;

    /**
     * 钼含量上限(%)
     */
    @Column(name = "molybdenum_max", precision = 8, scale = 5)
    private BigDecimal molybdenumMax;

    /**
     * 钒含量下限(%)
     */
    @Column(name = "vanadium_min", precision = 8, scale = 5)
    private BigDecimal vanadiumMin;

    /**
     * 钒含量上限(%)
     */
    @Column(name = "vanadium_max", precision = 8, scale = 5)
    private BigDecimal vanadiumMax;

    /**
     * 钛含量下限(%)
     */
    @Column(name = "titanium_min", precision = 8, scale = 5)
    private BigDecimal titaniumMin;

    /**
     * 钛含量上限(%)
     */
    @Column(name = "titanium_max", precision = 8, scale = 5)
    private BigDecimal titaniumMax;

    /**
     * 硼含量下限(%)
     */
    @Column(name = "boron_min", precision = 8, scale = 5)
    private BigDecimal boronMin;

    /**
     * 硼含量上限(%)
     */
    @Column(name = "boron_max", precision = 8, scale = 5)
    private BigDecimal boronMax;

    /**
     * 镍含量下限(%)
     */
    @Column(name = "nickel_min", precision = 8, scale = 5)
    private BigDecimal nickelMin;

    /**
     * 镍含量上限(%)
     */
    @Column(name = "nickel_max", precision = 8, scale = 5)
    private BigDecimal nickelMax;

    /**
     * 铝含量下限(%)
     */
    @Column(name = "aluminum_min", precision = 8, scale = 5)
    private BigDecimal aluminumMin;

    /**
     * 铝含量上限(%)
     */
    @Column(name = "aluminum_max", precision = 8, scale = 5)
    private BigDecimal aluminumMax;

    /**
     * 铜含量下限(%)
     */
    @Column(name = "copper_min", precision = 8, scale = 5)
    private BigDecimal copperMin;

    /**
     * 铜含量上限(%)
     */
    @Column(name = "copper_max", precision = 8, scale = 5)
    private BigDecimal copperMax;

    /**
     * 铌含量下限(%)
     */
    @Column(name = "niobium_min", precision = 8, scale = 5)
    private BigDecimal niobiumMin;

    /**
     * 铌含量上限(%)
     */
    @Column(name = "niobium_max", precision = 8, scale = 5)
    private BigDecimal niobiumMax;

    /**
     * 氮含量下限(%)
     */
    @Column(name = "nitrogen_min", precision = 8, scale = 5)
    private BigDecimal nitrogenMin;

    /**
     * 氮含量上限(%)
     */
    @Column(name = "nitrogen_max", precision = 8, scale = 5)
    private BigDecimal nitrogenMax;

    /**
     * 钨含量下限(%)
     */
    @Column(name = "tungsten_min", precision = 8, scale = 5)
    private BigDecimal tungstenMin;

    /**
     * 钨含量上限(%)
     */
    @Column(name = "tungsten_max", precision = 8, scale = 5)
    private BigDecimal tungstenMax;

    /**
     * 磷含量下限(%)
     */
    @Column(name = "phosphorus_min", precision = 8, scale = 5)
    private BigDecimal phosphorusMin;

    /**
     * 磷含量上限(%)
     */
    @Column(name = "phosphorus_max", precision = 8, scale = 5)
    private BigDecimal phosphorusMax;

    /**
     * 硫含量下限(%)
     */
    @Column(name = "sulfur_min", precision = 8, scale = 5)
    private BigDecimal sulfurMin;

    /**
     * 硫含量上限(%)
     */
    @Column(name = "sulfur_max", precision = 8, scale = 5)
    private BigDecimal sulfurMax;

    /**
     * 铁含量下限(%)
     */
    @Column(name = "iron_min", precision = 8, scale = 5)
    private BigDecimal ironMin;

    /**
     * 铁含量上限(%)
     */
    @Column(name = "iron_max", precision = 8, scale = 5)
    private BigDecimal ironMax;

    /**
     * 镁含量下限(%)
     */
    @Column(name = "magnesium_min", precision = 8, scale = 5)
    private BigDecimal magnesiumMin;

    /**
     * 镁含量上限(%)
     */
    @Column(name = "magnesium_max", precision = 8, scale = 5)
    private BigDecimal magnesiumMax;

    /**
     * 锡含量下限(%)
     */
    @Column(name = "tin_min", precision = 8, scale = 5)
    private BigDecimal tinMin;

    /**
     * 锡含量上限(%)
     */
    @Column(name = "tin_max", precision = 8, scale = 5)
    private BigDecimal tinMax;

    /**
     * 铅含量下限(%)
     */
    @Column(name = "lead_min", precision = 8, scale = 5)
    private BigDecimal leadMin;

    /**
     * 铅含量上限(%)
     */
    @Column(name = "lead_max", precision = 8, scale = 5)
    private BigDecimal leadMax;

    /**
     * 其它含量下限(%)
     */
    @Column(name = "other_min", precision = 8, scale = 5)
    private BigDecimal otherMin;

    /**
     * 其它含量上限(%)
     */
    @Column(name = "other_max", precision = 8, scale = 5)
    private BigDecimal otherMax;

    /**
     * 化学成分来源标准ID
     */
    @Column(name = "composition_standard_id")
    private Integer compositionStandardId;

    /**
     * 化学成分来源标准代码
     */
    @Column(name = "composition_standard_code", length = 100)
    private String compositionStandardCode;

    /**
     * 化学成分来源标准名称
     */
    @Column(name = "composition_standard_name", length = 200)
    private String compositionStandardName;

    /**
     * 化学成分来源标准年份
     */
    @Column(name = "composition_standard_year")
    private Integer compositionStandardYear;

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
     * 其它要求
     */
    @Column(name = "other_requirements", length = 200)
    private String otherRequirements;

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
        MaterialChemicalComposition other = (MaterialChemicalComposition) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getBasicPropertyId() == null ? other.getBasicPropertyId() == null : this.getBasicPropertyId().equals(other.getBasicPropertyId()))
                && (this.getCarbonMin() == null ? other.getCarbonMin() == null : this.getCarbonMin().equals(other.getCarbonMin()))
                && (this.getCarbonMax() == null ? other.getCarbonMax() == null : this.getCarbonMax().equals(other.getCarbonMax()))
                && (this.getSiliconMin() == null ? other.getSiliconMin() == null : this.getSiliconMin().equals(other.getSiliconMin()))
                && (this.getSiliconMax() == null ? other.getSiliconMax() == null : this.getSiliconMax().equals(other.getSiliconMax()))
                && (this.getManganeseMin() == null ? other.getManganeseMin() == null : this.getManganeseMin().equals(other.getManganeseMin()))
                && (this.getManganeseMax() == null ? other.getManganeseMax() == null : this.getManganeseMax().equals(other.getManganeseMax()))
                && (this.getChromiumMin() == null ? other.getChromiumMin() == null : this.getChromiumMin().equals(other.getChromiumMin()))
                && (this.getChromiumMax() == null ? other.getChromiumMax() == null : this.getChromiumMax().equals(other.getChromiumMax()))
                && (this.getMolybdenumMin() == null ? other.getMolybdenumMin() == null : this.getMolybdenumMin().equals(other.getMolybdenumMin()))
                && (this.getMolybdenumMax() == null ? other.getMolybdenumMax() == null : this.getMolybdenumMax().equals(other.getMolybdenumMax()))
                && (this.getVanadiumMin() == null ? other.getVanadiumMin() == null : this.getVanadiumMin().equals(other.getVanadiumMin()))
                && (this.getVanadiumMax() == null ? other.getVanadiumMax() == null : this.getVanadiumMax().equals(other.getVanadiumMax()))
                && (this.getTitaniumMin() == null ? other.getTitaniumMin() == null : this.getTitaniumMin().equals(other.getTitaniumMin()))
                && (this.getTitaniumMax() == null ? other.getTitaniumMax() == null : this.getTitaniumMax().equals(other.getTitaniumMax()))
                && (this.getBoronMin() == null ? other.getBoronMin() == null : this.getBoronMin().equals(other.getBoronMin()))
                && (this.getBoronMax() == null ? other.getBoronMax() == null : this.getBoronMax().equals(other.getBoronMax()))
                && (this.getNickelMin() == null ? other.getNickelMin() == null : this.getNickelMin().equals(other.getNickelMin()))
                && (this.getNickelMax() == null ? other.getNickelMax() == null : this.getNickelMax().equals(other.getNickelMax()))
                && (this.getAluminumMin() == null ? other.getAluminumMin() == null : this.getAluminumMin().equals(other.getAluminumMin()))
                && (this.getAluminumMax() == null ? other.getAluminumMax() == null : this.getAluminumMax().equals(other.getAluminumMax()))
                && (this.getCopperMin() == null ? other.getCopperMin() == null : this.getCopperMin().equals(other.getCopperMin()))
                && (this.getCopperMax() == null ? other.getCopperMax() == null : this.getCopperMax().equals(other.getCopperMax()))
                && (this.getNiobiumMin() == null ? other.getNiobiumMin() == null : this.getNiobiumMin().equals(other.getNiobiumMin()))
                && (this.getNiobiumMax() == null ? other.getNiobiumMax() == null : this.getNiobiumMax().equals(other.getNiobiumMax()))
                && (this.getNitrogenMin() == null ? other.getNitrogenMin() == null : this.getNitrogenMin().equals(other.getNitrogenMin()))
                && (this.getNitrogenMax() == null ? other.getNitrogenMax() == null : this.getNitrogenMax().equals(other.getNitrogenMax()))
                && (this.getTungstenMin() == null ? other.getTungstenMin() == null : this.getTungstenMin().equals(other.getTungstenMin()))
                && (this.getTungstenMax() == null ? other.getTungstenMax() == null : this.getTungstenMax().equals(other.getTungstenMax()))
                && (this.getPhosphorusMin() == null ? other.getPhosphorusMin() == null : this.getPhosphorusMin().equals(other.getPhosphorusMin()))
                && (this.getPhosphorusMax() == null ? other.getPhosphorusMax() == null : this.getPhosphorusMax().equals(other.getPhosphorusMax()))
                && (this.getSulfurMin() == null ? other.getSulfurMin() == null : this.getSulfurMin().equals(other.getSulfurMin()))
                && (this.getSulfurMax() == null ? other.getSulfurMax() == null : this.getSulfurMax().equals(other.getSulfurMax()))
                && (this.getIronMin() == null ? other.getIronMin() == null : this.getIronMin().equals(other.getIronMin()))
                && (this.getIronMax() == null ? other.getIronMax() == null : this.getIronMax().equals(other.getIronMax()))
                && (this.getMagnesiumMin() == null ? other.getMagnesiumMin() == null : this.getMagnesiumMin().equals(other.getMagnesiumMin()))
                && (this.getMagnesiumMax() == null ? other.getMagnesiumMax() == null : this.getMagnesiumMax().equals(other.getMagnesiumMax()))
                && (this.getTinMin() == null ? other.getTinMin() == null : this.getTinMin().equals(other.getTinMin()))
                && (this.getTinMax() == null ? other.getTinMax() == null : this.getTinMax().equals(other.getTinMax()))
                && (this.getLeadMin() == null ? other.getLeadMin() == null : this.getLeadMin().equals(other.getLeadMin()))
                && (this.getLeadMax() == null ? other.getLeadMax() == null : this.getLeadMax().equals(other.getLeadMax()))
                && (this.getOtherMin() == null ? other.getOtherMin() == null : this.getOtherMin().equals(other.getOtherMin()))
                && (this.getOtherMax() == null ? other.getOtherMax() == null : this.getOtherMax().equals(other.getOtherMax()))
                && (this.getCompositionStandardId() == null ? other.getCompositionStandardId() == null : this.getCompositionStandardId().equals(other.getCompositionStandardId()))
                && (this.getCompositionStandardCode() == null ? other.getCompositionStandardCode() == null : this.getCompositionStandardCode().equals(other.getCompositionStandardCode()))
                && (this.getCompositionStandardName() == null ? other.getCompositionStandardName() == null : this.getCompositionStandardName().equals(other.getCompositionStandardName()))
                && (this.getCompositionStandardYear() == null ? other.getCompositionStandardYear() == null : this.getCompositionStandardYear().equals(other.getCompositionStandardYear()))
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
                && (this.getOtherRequirements() == null ? other.getOtherRequirements() == null : this.getOtherRequirements().equals(other.getOtherRequirements()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBasicPropertyId() == null) ? 0 : getBasicPropertyId().hashCode());
        result = prime * result + ((getCarbonMin() == null) ? 0 : getCarbonMin().hashCode());
        result = prime * result + ((getCarbonMax() == null) ? 0 : getCarbonMax().hashCode());
        result = prime * result + ((getSiliconMin() == null) ? 0 : getSiliconMin().hashCode());
        result = prime * result + ((getSiliconMax() == null) ? 0 : getSiliconMax().hashCode());
        result = prime * result + ((getManganeseMin() == null) ? 0 : getManganeseMin().hashCode());
        result = prime * result + ((getManganeseMax() == null) ? 0 : getManganeseMax().hashCode());
        result = prime * result + ((getChromiumMin() == null) ? 0 : getChromiumMin().hashCode());
        result = prime * result + ((getChromiumMax() == null) ? 0 : getChromiumMax().hashCode());
        result = prime * result + ((getMolybdenumMin() == null) ? 0 : getMolybdenumMin().hashCode());
        result = prime * result + ((getMolybdenumMax() == null) ? 0 : getMolybdenumMax().hashCode());
        result = prime * result + ((getVanadiumMin() == null) ? 0 : getVanadiumMin().hashCode());
        result = prime * result + ((getVanadiumMax() == null) ? 0 : getVanadiumMax().hashCode());
        result = prime * result + ((getTitaniumMin() == null) ? 0 : getTitaniumMin().hashCode());
        result = prime * result + ((getTitaniumMax() == null) ? 0 : getTitaniumMax().hashCode());
        result = prime * result + ((getBoronMin() == null) ? 0 : getBoronMin().hashCode());
        result = prime * result + ((getBoronMax() == null) ? 0 : getBoronMax().hashCode());
        result = prime * result + ((getNickelMin() == null) ? 0 : getNickelMin().hashCode());
        result = prime * result + ((getNickelMax() == null) ? 0 : getNickelMax().hashCode());
        result = prime * result + ((getAluminumMin() == null) ? 0 : getAluminumMin().hashCode());
        result = prime * result + ((getAluminumMax() == null) ? 0 : getAluminumMax().hashCode());
        result = prime * result + ((getCopperMin() == null) ? 0 : getCopperMin().hashCode());
        result = prime * result + ((getCopperMax() == null) ? 0 : getCopperMax().hashCode());
        result = prime * result + ((getNiobiumMin() == null) ? 0 : getNiobiumMin().hashCode());
        result = prime * result + ((getNiobiumMax() == null) ? 0 : getNiobiumMax().hashCode());
        result = prime * result + ((getNitrogenMin() == null) ? 0 : getNitrogenMin().hashCode());
        result = prime * result + ((getNitrogenMax() == null) ? 0 : getNitrogenMax().hashCode());
        result = prime * result + ((getTungstenMin() == null) ? 0 : getTungstenMin().hashCode());
        result = prime * result + ((getTungstenMax() == null) ? 0 : getTungstenMax().hashCode());
        result = prime * result + ((getPhosphorusMin() == null) ? 0 : getPhosphorusMin().hashCode());
        result = prime * result + ((getPhosphorusMax() == null) ? 0 : getPhosphorusMax().hashCode());
        result = prime * result + ((getSulfurMin() == null) ? 0 : getSulfurMin().hashCode());
        result = prime * result + ((getSulfurMax() == null) ? 0 : getSulfurMax().hashCode());
        result = prime * result + ((getIronMin() == null) ? 0 : getIronMin().hashCode());
        result = prime * result + ((getIronMax() == null) ? 0 : getIronMax().hashCode());
        result = prime * result + ((getMagnesiumMin() == null) ? 0 : getMagnesiumMin().hashCode());
        result = prime * result + ((getMagnesiumMax() == null) ? 0 : getMagnesiumMax().hashCode());
        result = prime * result + ((getTinMin() == null) ? 0 : getTinMin().hashCode());
        result = prime * result + ((getTinMax() == null) ? 0 : getTinMax().hashCode());
        result = prime * result + ((getLeadMin() == null) ? 0 : getLeadMin().hashCode());
        result = prime * result + ((getLeadMax() == null) ? 0 : getLeadMax().hashCode());
        result = prime * result + ((getOtherMin() == null) ? 0 : getOtherMin().hashCode());
        result = prime * result + ((getOtherMax() == null) ? 0 : getOtherMax().hashCode());
        result = prime * result + ((getCompositionStandardId() == null) ? 0 : getCompositionStandardId().hashCode());
        result = prime * result + ((getCompositionStandardCode() == null) ? 0 : getCompositionStandardCode().hashCode());
        result = prime * result + ((getCompositionStandardName() == null) ? 0 : getCompositionStandardName().hashCode());
        result = prime * result + ((getCompositionStandardYear() == null) ? 0 : getCompositionStandardYear().hashCode());
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
        result = prime * result + ((getOtherRequirements() == null) ? 0 : getOtherRequirements().hashCode());
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
        sb.append(", carbonMin=").append(carbonMin);
        sb.append(", carbonMax=").append(carbonMax);
        sb.append(", siliconMin=").append(siliconMin);
        sb.append(", siliconMax=").append(siliconMax);
        sb.append(", manganeseMin=").append(manganeseMin);
        sb.append(", manganeseMax=").append(manganeseMax);
        sb.append(", chromiumMin=").append(chromiumMin);
        sb.append(", chromiumMax=").append(chromiumMax);
        sb.append(", molybdenumMin=").append(molybdenumMin);
        sb.append(", molybdenumMax=").append(molybdenumMax);
        sb.append(", vanadiumMin=").append(vanadiumMin);
        sb.append(", vanadiumMax=").append(vanadiumMax);
        sb.append(", titaniumMin=").append(titaniumMin);
        sb.append(", titaniumMax=").append(titaniumMax);
        sb.append(", boronMin=").append(boronMin);
        sb.append(", boronMax=").append(boronMax);
        sb.append(", nickelMin=").append(nickelMin);
        sb.append(", nickelMax=").append(nickelMax);
        sb.append(", aluminumMin=").append(aluminumMin);
        sb.append(", aluminumMax=").append(aluminumMax);
        sb.append(", copperMin=").append(copperMin);
        sb.append(", copperMax=").append(copperMax);
        sb.append(", niobiumMin=").append(niobiumMin);
        sb.append(", niobiumMax=").append(niobiumMax);
        sb.append(", nitrogenMin=").append(nitrogenMin);
        sb.append(", nitrogenMax=").append(nitrogenMax);
        sb.append(", tungstenMin=").append(tungstenMin);
        sb.append(", tungstenMax=").append(tungstenMax);
        sb.append(", phosphorusMin=").append(phosphorusMin);
        sb.append(", phosphorusMax=").append(phosphorusMax);
        sb.append(", sulfurMin=").append(sulfurMin);
        sb.append(", sulfurMax=").append(sulfurMax);
        sb.append(", ironMin=").append(ironMin);
        sb.append(", ironMax=").append(ironMax);
        sb.append(", magnesiumMin=").append(magnesiumMin);
        sb.append(", magnesiumMax=").append(magnesiumMax);
        sb.append(", tinMin=").append(tinMin);
        sb.append(", tinMax=").append(tinMax);
        sb.append(", leadMin=").append(leadMin);
        sb.append(", leadMax=").append(leadMax);
        sb.append(", otherMin=").append(otherMin);
        sb.append(", otherMax=").append(otherMax);
        sb.append(", compositionStandardId=").append(compositionStandardId);
        sb.append(", compositionStandardCode=").append(compositionStandardCode);
        sb.append(", compositionStandardName=").append(compositionStandardName);
        sb.append(", compositionStandardYear=").append(compositionStandardYear);
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
        sb.append(", otherRequirements=").append(otherRequirements);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}