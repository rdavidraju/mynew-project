package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FavouriteReports.
 */
@Entity
@Table(name = "t_favourite_reports")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FavouriteReports implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "tenant_id")
    private Long tenantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportId() {
        return reportId;
    }

    public FavouriteReports reportId(Long reportId) {
        this.reportId = reportId;
        return this;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getUserId() {
        return userId;
    }

    public FavouriteReports userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public FavouriteReports tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FavouriteReports favouriteReports = (FavouriteReports) o;
        if (favouriteReports.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), favouriteReports.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FavouriteReports{" +
            "id=" + getId() +
            ", reportId='" + getReportId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            "}";
    }
}
