package com.voson.dataant.store.mysql.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="dataant_job_history")
public class JobHistoryPersistence {
	@Id
	@GeneratedValue
	//@GeneratedValue(strategy =GenerationType.SEQUENCE, generator = "dataant_job_history_seq")
	//@SequenceGenerator(name="dataant_job_history_seq", sequenceName="dataant_job_history_seq",allocationSize=1,initialValue=1)
	private Long id;
	@Column(name="job_id")
	private Long jobId;
	@Column(name="start_time")
	private Date startTime;
	@Column(name="end_time")
	private Date endTime;
	@Column(length=100000)
	private String log;
	@Column(name="execute_host")
	private String executeHost;
	@Column(name="operator")
	private String operator;
	@Column(name="gmt_create")
	private Date gmtCreate=new Date();
	@Column(name="gmt_modified")
	private Date gmtModified=new Date();
	@Column(name="trigger_type")
	private Integer triggerType;
	@Column
	private String status;
	@Column
	private String illustrate;
	@Column
	private String properties;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public String getExecuteHost() {
		return executeHost;
	}
	public void setExecuteHost(String executeHost) {
		this.executeHost = executeHost;
	}
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(Integer triggerType) {
		this.triggerType = triggerType;
	}
	public String getIllustrate() {
		return illustrate;
	}
	public void setIllustrate(String illustrate) {
		this.illustrate = illustrate;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
}
