package com.voson.dataant.firstanalyze.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FIRST_ANALYZE_RULE_HISTORY")
public class FirstAnalyzeRuleHistory implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 历史记录ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator = "FIRST_ANALYZE_RULE_HIS_ID_SEQ")
	@SequenceGenerator(name="FIRST_ANALYZE_RULE_HIS_ID_SEQ", sequenceName="FIRST_ANALYZE_RULE_HIS_ID_SEQ",allocationSize=1,initialValue=1)
	private java.lang.Long id;
	/**
	 * 规则ID
	 */
	@Column(name = "RULE_ID")
	private java.lang.Long ruleId;
	/**
	 * 规则编码
	 */
	@Column(name = "RULE_CODE")
	private java.lang.String ruleCode;
	/**
	 * 运行日期YYYYMMDD
	 */
	@Column(name = "RUN_DATE")
	private java.lang.String runDate;
	/**
	 * 序列号4位
	 */
	@Column(name = "SEQ")
	private java.lang.Long seq;
	/**
	 * 开始时间
	 */
	@Column(name = "START_TIME")
	private java.util.Date startTime;
	/**
	 * 结束时间
	 */
	@Column(name = "END_TIME")
	private java.util.Date endTime;
	/**
	 * 状态
	 */
	@Column(name = "STATUS")
	private java.lang.String status;
	/**
	 * 触发方式
	 */
	@Column(name = "TRIGGER_TYPE")
	private java.lang.Integer triggerType;
	/**
	 * 工作路径
	 */
	@Column(name = "WORK_DIR")
	private java.lang.String workDir;
	/**
	 * 结果路径
	 */
	@Column(name = "RESULT_DIR")
	private java.lang.String resultDir;

	/**
	 * app结果数量
	 */
	@Column(name = "APP_NUM")
	private java.lang.Long appNum;
	
	/**
	 * app结果数量
	 */
	@Column(name = "URL_NUM")
	private java.lang.Long urlNum;
	
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.Long getRuleId() {
		return ruleId;
	}
	public void setRuleId(java.lang.Long ruleId) {
		this.ruleId = ruleId;
	}
	public java.lang.String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(java.lang.String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public java.lang.String getRunDate() {
		return runDate;
	}
	public void setRunDate(java.lang.String runDate) {
		this.runDate = runDate;
	}
	public java.lang.Long getSeq() {
		return seq;
	}
	public void setSeq(java.lang.Long seq) {
		this.seq = seq;
	}
	public java.util.Date getStartTime() {
		return startTime;
	}
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}
	public java.util.Date getEndTime() {
		return endTime;
	}
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}
	public java.lang.String getStatus() {
		return status;
	}
	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	public java.lang.Integer getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(java.lang.Integer triggerType) {
		this.triggerType = triggerType;
	}
	public java.lang.String getWorkDir() {
		return workDir;
	}
	public void setWorkDir(java.lang.String workDir) {
		this.workDir = workDir;
	}
	public java.lang.String getResultDir() {
		return resultDir;
	}
	public void setResultDir(java.lang.String resultDir) {
		this.resultDir = resultDir;
	}
	public java.lang.Long getAppNum() {
		return appNum;
	}
	public void setAppNum(java.lang.Long appNum) {
		this.appNum = appNum;
	}
	public java.lang.Long getUrlNum() {
		return urlNum;
	}
	public void setUrlNum(java.lang.Long urlNum) {
		this.urlNum = urlNum;
	}
	
}
