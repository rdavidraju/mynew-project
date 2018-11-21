package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Periods.
 */
@Entity
@Table(name = "t_periods")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Periods implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cal_id")
    private Long calId;

    @Column(name = "period_name")
    private String periodName;

    @Column(name = "from_date")
    private ZonedDateTime fromDate;

    @Column(name = "to_date")
    private ZonedDateTime toDate;

    @Column(name = "period_num")
    private Integer periodNum;

    @Column(name = "quarter")
    private Integer quarter;

    @Column(name = "year")
    private Integer year;

    @Column(name = "adjustment")
    private Boolean adjustment;

    @Column(name = "status")
    private String status;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    
    @Column(name = "enabled_flag")
    private Boolean enabledFlag;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCalId() {
        return calId;
    }

    public Periods calId(Long calId) {
        this.calId = calId;
        return this;
    }

    public void setCalId(Long calId) {
        this.calId = calId;
    }

    public String getPeriodName() {
        return periodName;
    }

    public Periods periodName(String periodName) {
        this.periodName = periodName;
        return this;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public ZonedDateTime getFromDate() {
        return fromDate;
    }

    public Periods fromDate(ZonedDateTime fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(ZonedDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public ZonedDateTime getToDate() {
        return toDate;
    }

    public Periods toDate(ZonedDateTime toDate) {
        this.toDate = toDate;
        return this;
    }

    public void setToDate(ZonedDateTime toDate) {
        this.toDate = toDate;
    }

    public Integer getPeriodNum() {
        return periodNum;
    }

    public Periods periodNum(Integer periodNum) {
        this.periodNum = periodNum;
        return this;
    }

    public void setPeriodNum(Integer periodNum) {
        this.periodNum = periodNum;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public Periods quarter(Integer quarter) {
        this.quarter = quarter;
        return this;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public Integer getYear() {
        return year;
    }

    public Periods year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean isAdjustment() {
        return adjustment;
    }

    public Periods adjustment(Boolean adjustment) {
        this.adjustment = adjustment;
        return this;
    }

    public void setAdjustment(Boolean adjustment) {
        this.adjustment = adjustment;
    }

   public String getStatus() {
        return status;
    }

    public Periods status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Periods createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Periods createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public Periods lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Periods lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Boolean isEnabledFlag() {
        return enabledFlag;
    }

    public Periods enabledFlag(Boolean enabledFlag) {
        this.enabledFlag = enabledFlag;
        return this;
    }

    public void setEnabledFlag(Boolean enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
    
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Periods periods = (Periods) o;
        if (periods.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, periods.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Periods{" +
            "id=" + id +
            ", calId='" + calId + "'" +
            ", periodName='" + periodName + "'" +
            ", fromDate='" + fromDate + "'" +
            ", toDate='" + toDate + "'" +
            ", periodNum='" + periodNum + "'" +
            ", quarter='" + quarter + "'" +
            ", year='" + year + "'" +
            ", adjustment='" + adjustment + "'" +
           /* ", status='" + status + "'" +*/
            ", createdBy='" + createdBy + "'" +
            ", createdDate='" + createdDate + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            '}';
    }
}
