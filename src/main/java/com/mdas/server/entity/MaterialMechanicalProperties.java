package com.mdas.server.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "material_mechanical_properties")
@Data
public class MaterialMechanicalProperties implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "basic_property_id")
    private Integer basicPropertyId;

    @Column(name = "grain_size_min", length = 50)
    private String grainSizeMin;

    @Column(name = "grain_size_max", length = 50)
    private String grainSizeMax;

    @Column(name = "tensile_strength_min", precision = 8, scale = 2)
    private BigDecimal tensileStrengthMin;

    @Column(name = "tensile_strength_max", precision = 8, scale = 2)
    private BigDecimal tensileStrengthMax;

    @Column(name = "yield_strength", precision = 8, scale = 2)
    private BigDecimal yieldStrength;

    @Column(name = "transverse_elongation", precision = 6, scale = 3)
    private BigDecimal transverseElongation;

    @Column(name = "longitudinal_elongation", precision = 6, scale = 3)
    private BigDecimal longitudinalElongation;

    @Column(name = "transverse_impact_energy", precision = 8, scale = 2)
    private BigDecimal transverseImpactEnergy;

    @Column(name = "longitudinal_impact_energy", precision = 8, scale = 2)
    private BigDecimal longitudinalImpactEnergy;

    @Column(name = "hardness_hb_min", precision = 6, scale = 1)
    private BigDecimal hardnessHbMin;

    @Column(name = "hardness_hb_max", precision = 6, scale = 1)
    private BigDecimal hardnessHbMax;

    @Column(name = "hardness_hv_min", precision = 6, scale = 1)
    private BigDecimal hardnessHvMin;

    @Column(name = "hardness_hv_max", precision = 6, scale = 1)
    private BigDecimal hardnessHvMax;

    @Column(name = "hardness_hrc_min", precision = 6, scale = 1)
    private BigDecimal hardnessHrcMin;

    @Column(name = "hardness_hrc_max", precision = 6, scale = 1)
    private BigDecimal hardnessHrcMax;

    @Column(name = "grain_size_standard_id")
    private Integer grainSizeStandardId;

    @Column(name = "grain_size_standard_code", length = 100)
    private String grainSizeStandardCode;

    @Column(name = "grain_size_standard_name", length = 200)
    private String grainSizeStandardName;

    @Column(name = "grain_size_standard_year")
    private Integer grainSizeStandardYear;

    @Column(name = "tensile_strength_standard_id")
    private Integer tensileStrengthStandardId;

    @Column(name = "tensile_strength_standard_code", length = 100)
    private String tensileStrengthStandardCode;

    @Column(name = "tensile_strength_standard_name", length = 200)
    private String tensileStrengthStandardName;

    @Column(name = "tensile_strength_standard_year")
    private Integer tensileStrengthStandardYear;

    @Column(name = "yield_strength_standard_id")
    private Integer yieldStrengthStandardId;

    @Column(name = "yield_strength_standard_code", length = 100)
    private String yieldStrengthStandardCode;

    @Column(name = "yield_strength_standard_name", length = 200)
    private String yieldStrengthStandardName;

    @Column(name = "yield_strength_standard_year")
    private Integer yieldStrengthStandardYear;

    @Column(name = "elongation_standard_id")
    private Integer elongationStandardId;

    @Column(name = "elongation_standard_code", length = 100)
    private String elongationStandardCode;

    @Column(name = "elongation_standard_name", length = 200)
    private String elongationStandardName;

    @Column(name = "elongation_standard_year")
    private Integer elongationStandardYear;

    @Column(name = "impact_energy_standard_id")
    private Integer impactEnergyStandardId;

    @Column(name = "impact_energy_standard_code", length = 100)
    private String impactEnergyStandardCode;

    @Column(name = "impact_energy_standard_name", length = 200)
    private String impactEnergyStandardName;

    @Column(name = "impact_energy_standard_year")
    private Integer impactEnergyStandardYear;

    @Column(name = "hardness_standard_id")
    private Integer hardnessStandardId;

    @Column(name = "hardness_standard_code", length = 100)
    private String hardnessStandardCode;

    @Column(name = "hardness_standard_name", length = 200)
    private String hardnessStandardName;

    @Column(name = "hardness_standard_year")
    private Integer hardnessStandardYear;

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
}