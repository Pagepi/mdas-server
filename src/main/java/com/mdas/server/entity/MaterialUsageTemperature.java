package com.mdas.server.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "material_usage_temperature")
@Data
public class MaterialUsageTemperature implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "basic_property_id")
    private Integer basicPropertyId;

    @Column(name = "component_category", length = 50)
    private String componentCategory;

    @Column(name = "recommended_component", length = 100)
    private String recommendedComponent;

    @Column(name = "recommended_temperature_range", length = 500)
    private String recommendedTemperatureRange;

    @Column(name = "temperature_min", precision = 6, scale = 2)
    private BigDecimal temperatureMin;

    @Column(name = "temperature_max", precision = 6, scale = 2)
    private BigDecimal temperatureMax;

    @Column(name = "component_standard_id")
    private Integer componentStandardId;

    @Column(name = "component_standard_code", length = 100)
    private String componentStandardCode;

    @Column(name = "component_standard_name", length = 200)
    private String componentStandardName;

    @Column(name = "component_standard_year")
    private Integer componentStandardYear;

    @Column(name = "temperature_standard_id")
    private Integer temperatureStandardId;

    @Column(name = "temperature_standard_code", length = 100)
    private String temperatureStandardCode;

    @Column(name = "temperature_standard_name", length = 200)
    private String temperatureStandardName;

    @Column(name = "temperature_standard_year")
    private Integer temperatureStandardYear;

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