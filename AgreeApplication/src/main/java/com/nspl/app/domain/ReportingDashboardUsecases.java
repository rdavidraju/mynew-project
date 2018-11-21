package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ReportingDashboardUsecases.
 */
@Entity
@Table(name = "t_reporting_dashboard_usecases")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reportingdashboardusecases")
public class ReportingDashboardUsecases implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dashboard_id")
    private Long dashboardId;

    @Column(name = "seq_num")
    private Integer seqNum;

    @Column(name = "usecase_name")
    private String usecaseName;

    @Column(name = "report_temp_id")
    private String reportTempId;

    @Column(name = "output_type")
    private String outputType;

    @Column(name = "groupby_col")
    private String groupbyCol;
    
    @Column(name = "x_axis")
    private Integer xAxis;
    
    @Column(name = "y_axis")
    private Integer yAxis;
    
    @Column(name = "width")
    private Integer width;
    
    @Column(name = "height")
    private Integer height;
    
    @Column(name = "colby_col")
    private String colbyCol;
    
    @Column(name = "valby_col")
    private String valbyCol;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public ReportingDashboardUsecases dashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
        return this;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public ReportingDashboardUsecases seqNum(Integer seqNum) {
        this.seqNum = seqNum;
        return this;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public String getUsecaseName() {
        return usecaseName;
    }

    public ReportingDashboardUsecases usecaseName(String usecaseName) {
        this.usecaseName = usecaseName;
        return this;
    }

    public void setUsecaseName(String usecaseName) {
        this.usecaseName = usecaseName;
    }

    public String getReportTempId() {
        return reportTempId;
    }

    public ReportingDashboardUsecases reportTempId(String reportTempId) {
        this.reportTempId = reportTempId;
        return this;
    }

    public void setReportTempId(String reportTempId) {
        this.reportTempId = reportTempId;
    }

    public String getOutputType() {
        return outputType;
    }

    public ReportingDashboardUsecases outputType(String outputType) {
        this.outputType = outputType;
        return this;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public String getGroupbyCol() {
        return groupbyCol;
    }

    public ReportingDashboardUsecases groupbyCol(String groupbyCol) {
        this.groupbyCol = groupbyCol;
        return this;
    }

    public void setGroupbyCol(String groupbyCol) {
        this.groupbyCol = groupbyCol;
    }

    public Integer getxAxis() {
		return xAxis;
	}

	public void setxAxis(Integer xAxis) {
		this.xAxis = xAxis;
	}

	public Integer getyAxis() {
		return yAxis;
	}

	public void setyAxis(Integer yAxis) {
		this.yAxis = yAxis;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getColbyCol() {
		return colbyCol;
	}

	public void setColbyCol(String colbyCol) {
		this.colbyCol = colbyCol;
	}

	public String getValbyCol() {
		return valbyCol;
	}

	public void setValbyCol(String valbyCol) {
		this.valbyCol = valbyCol;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReportingDashboardUsecases reportingDashboardUsecases = (ReportingDashboardUsecases) o;
        if (reportingDashboardUsecases.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reportingDashboardUsecases.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

	@Override
	public String toString() {
		return "ReportingDashboardUsecases [id=" + id + ", dashboardId=" + dashboardId + ", seqNum=" + seqNum
				+ ", usecaseName=" + usecaseName + ", reportTempId=" + reportTempId + ", outputType=" + outputType
				+ ", groupbyCol=" + groupbyCol + ", xAxis=" + xAxis + ", yAxis=" + yAxis + ", width=" + width
				+ ", height=" + height + ", colbyCol=" + colbyCol + ", valbyCol=" + valbyCol + "]";
	}

	
    
}
