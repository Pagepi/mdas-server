package com.mdas.server.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "material_chemical_composition_history")
@Data
public class MaterialChemicalCompositionHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "basic_property_id", nullable = false)
    private Integer basicPropertyId;

    @Column(name = "carbon_min", precision = 8, scale = 5)
    private BigDecimal carbonMin;

    @Column(name = "carbon_max", precision = 8, scale = 5)
    private BigDecimal carbonMax;

    @Column(name = "silicon_min", precision = 8, scale = 5)
    private BigDecimal siliconMin;

    @Column(name = "silicon_max", precision = 8, scale = 5)
    private BigDecimal siliconMax;

    @Column(name = "manganese_min", precision = 8, scale = 5)
    private BigDecimal manganeseMin;

    @Column(name = "manganese_max", precision = 8, scale = 5)
    private BigDecimal manganeseMax;

    @Column(name = "chromium_min", precision = 8, scale = 5)
    private BigDecimal chromiumMin;

    @Column(name = "chromium_max", precision = 8, scale = 5)
    private BigDecimal chromiumMax;

    @Column(name = "molybdenum_min", precision = 8, scale = 5)
    private BigDecimal molybdenumMin;

    @Column(name = "molybdenum_max", precision = 8, scale = 5)
    private BigDecimal molybdenumMax;

    @Column(name = "vanadium_min", precision = 8, scale = 5)
    private BigDecimal vanadiumMin;

    @Column(name = "vanadium_max", precision = 8, scale = 5)
    private BigDecimal vanadiumMax;

    @Column(name = "titanium_min", precision = 8, scale = 5)
    private BigDecimal titaniumMin;

    @Column(name = "titanium_max", precision = 8, scale = 5)
    private BigDecimal titaniumMax;

    @Column(name = "boron_min", precision = 8, scale = 5)
    private BigDecimal boronMin;

    @Column(name = "boron_max", precision = 8, scale = 5)
    private BigDecimal boronMax;

    @Column(name = "nickel_min", precision = 8, scale = 5)
    private BigDecimal nickelMin;

    @Column(name = "nickel_max", precision = 8, scale = 5)
    private BigDecimal nickelMax;

    @Column(name = "aluminum_min", precision = 8, scale = 5)
    private BigDecimal aluminumMin;

    @Column(name = "aluminum_max", precision = 8, scale = 5)
    private BigDecimal aluminumMax;

    @Column(name = "copper_min", precision = 8, scale = 5)
    private BigDecimal copperMin;

    @Column(name = "copper_max", precision = 8, scale = 5)
    private BigDecimal copperMax;

    @Column(name = "niobium_min", precision = 8, scale = 5)
    private BigDecimal niobiumMin;

    @Column(name = "niobium_max", precision = 8, scale = 5)
    private BigDecimal niobiumMax;

    @Column(name = "nitrogen_min", precision = 8, scale = 5)
    private BigDecimal nitrogenMin;

    @Column(name = "nitrogen_max", precision = 8, scale = 5)
    private BigDecimal nitrogenMax;

    @Column(name = "tungsten_min", precision = 8, scale = 5)
    private BigDecimal tungstenMin;

    @Column(name = "tungsten_max", precision = 8, scale = 5)
    private BigDecimal tungstenMax;

    @Column(name = "phosphorus_min", precision = 8, scale = 5)
    private BigDecimal phosphorusMin;

    @Column(name = "phosphorus_max", precision = 8, scale = 5)
    private BigDecimal phosphorusMax;

    @Column(name = "sulfur_min", precision = 8, scale = 5)
    private BigDecimal sulfurMin;

    @Column(name = "sulfur_max", precision = 8, scale = 5)
    private BigDecimal sulfurMax;

    @Column(name = "iron_min", precision = 8, scale = 5)
    private BigDecimal ironMin;

    @Column(name = "iron_max", precision = 8, scale = 5)
    private BigDecimal ironMax;

    @Column(name = "magnesium_min", precision = 8, scale = 5)
    private BigDecimal magnesiumMin;

    @Column(name = "magnesium_max", precision = 8, scale = 5)
    private BigDecimal magnesiumMax;

    @Column(name = "tin_min", precision = 8, scale = 5)
    private BigDecimal tinMin;

    @Column(name = "tin_max", precision = 8, scale = 5)
    private BigDecimal tinMax;

    @Column(name = "lead_min", precision = 8, scale = 5)
    private BigDecimal leadMin;

    @Column(name = "lead_max", precision = 8, scale = 5)
    private BigDecimal leadMax;

    @Column(name = "other_min", precision = 8, scale = 5)
    private BigDecimal otherMin;

    @Column(name = "other_max", precision = 8, scale = 5)
    private BigDecimal otherMax;

    @Column(name = "composition_standard_id")
    private Integer compositionStandardId;

    @Column(name = "composition_standard_code", length = 100)
    private String compositionStandardCode;

    @Column(name = "composition_standard_name", length = 200)
    private String compositionStandardName;

    @Column(name = "composition_standard_year")
    private Integer compositionStandardYear;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_by_account", length = 50)
    private String createdByAccount;

    @Column(name = "created_by_name", length = 50)
    private String createdByName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_by_account", length = 50)
    private String updatedByAccount;

    @Column(name = "updated_by_name", length = 50)
    private String updatedByName;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber = 1;

    @Column(name = "version_remark", length = 500)
    private String versionRemark;

    @Column(name = "original_record_id", nullable = false)
    private Integer originalRecordId;
}