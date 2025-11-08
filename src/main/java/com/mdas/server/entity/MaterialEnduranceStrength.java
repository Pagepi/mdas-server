package com.mdas.server.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "material_endurance_strength")
@Data
public class MaterialEnduranceStrength implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "basic_property_id")
    private Integer basicPropertyId;

    @Column(name = "strength_400c", precision = 8, scale = 2)
    private BigDecimal strength400c;

    @Column(name = "strength_410c", precision = 8, scale = 2)
    private BigDecimal strength410c;

    @Column(name = "strength_420c", precision = 8, scale = 2)
    private BigDecimal strength420c;

    @Column(name = "strength_430c", precision = 8, scale = 2)
    private BigDecimal strength430c;

    @Column(name = "strength_440c", precision = 8, scale = 2)
    private BigDecimal strength440c;

    @Column(name = "strength_450c", precision = 8, scale = 2)
    private BigDecimal strength450c;

    @Column(name = "strength_460c", precision = 8, scale = 2)
    private BigDecimal strength460c;

    @Column(name = "strength_470c", precision = 8, scale = 2)
    private BigDecimal strength470c;

    @Column(name = "strength_480c", precision = 8, scale = 2)
    private BigDecimal strength480c;

    @Column(name = "strength_490c", precision = 8, scale = 2)
    private BigDecimal strength490c;

    @Column(name = "strength_500c", precision = 8, scale = 2)
    private BigDecimal strength500c;

    @Column(name = "strength_510c", precision = 8, scale = 2)
    private BigDecimal strength510c;

    @Column(name = "strength_520c", precision = 8, scale = 2)
    private BigDecimal strength520c;

    @Column(name = "strength_530c", precision = 8, scale = 2)
    private BigDecimal strength530c;

    @Column(name = "strength_540c", precision = 8, scale = 2)
    private BigDecimal strength540c;

    @Column(name = "strength_550c", precision = 8, scale = 2)
    private BigDecimal strength550c;

    @Column(name = "strength_560c", precision = 8, scale = 2)
    private BigDecimal strength560c;

    @Column(name = "strength_570c", precision = 8, scale = 2)
    private BigDecimal strength570c;

    @Column(name = "strength_580c", precision = 8, scale = 2)
    private BigDecimal strength580c;

    @Column(name = "strength_590c", precision = 8, scale = 2)
    private BigDecimal strength590c;

    @Column(name = "strength_600c", precision = 8, scale = 2)
    private BigDecimal strength600c;

    @Column(name = "strength_610c", precision = 8, scale = 2)
    private BigDecimal strength610c;

    @Column(name = "strength_620c", precision = 8, scale = 2)
    private BigDecimal strength620c;

    @Column(name = "strength_630c", precision = 8, scale = 2)
    private BigDecimal strength630c;

    @Column(name = "strength_640c", precision = 8, scale = 2)
    private BigDecimal strength640c;

    @Column(name = "strength_650c", precision = 8, scale = 2)
    private BigDecimal strength650c;

    @Column(name = "strength_660c", precision = 8, scale = 2)
    private BigDecimal strength660c;

    @Column(name = "strength_670c", precision = 8, scale = 2)
    private BigDecimal strength670c;

    @Column(name = "strength_680c", precision = 8, scale = 2)
    private BigDecimal strength680c;

    @Column(name = "strength_690c", precision = 8, scale = 2)
    private BigDecimal strength690c;

    @Column(name = "strength_700c", precision = 8, scale = 2)
    private BigDecimal strength700c;

    @Column(name = "strength_710c", precision = 8, scale = 2)
    private BigDecimal strength710c;

    @Column(name = "strength_720c", precision = 8, scale = 2)
    private BigDecimal strength720c;

    @Column(name = "strength_730c", precision = 8, scale = 2)
    private BigDecimal strength730c;

    @Column(name = "strength_740c", precision = 8, scale = 2)
    private BigDecimal strength740c;

    @Column(name = "strength_750c", precision = 8, scale = 2)
    private BigDecimal strength750c;

    @Column(name = "endurance_strength_standard_id")
    private Integer enduranceStrengthStandardId;

    @Column(name = "endurance_strength_standard_code", length = 100)
    private String enduranceStrengthStandardCode;

    @Column(name = "endurance_strength_standard_name", length = 200)
    private String enduranceStrengthStandardName;

    @Column(name = "endurance_strength_standard_year")
    private Integer enduranceStrengthStandardYear;

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