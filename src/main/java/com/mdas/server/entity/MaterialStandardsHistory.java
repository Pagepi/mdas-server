package com.mdas.server.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "material_standards_history")
@Data
public class MaterialStandardsHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "standard_code", length = 100)
    private String standardCode;

    @Column(name = "standard_name", length = 200)
    private String standardName;

    @Column(name = "revision_year")
    private Integer revisionYear;

    @Column(name = "issuing_body", length = 100)
    private String issuingBody;

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