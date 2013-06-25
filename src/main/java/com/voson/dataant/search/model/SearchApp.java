package com.voson.dataant.search.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SEARCH_APP")
public class SearchApp implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@Id
	@Column(name = "ID")
	private java.lang.Long id;
	/**
	 * 手机号码
	 */
	@Column(name = "USER_NO")
	private java.lang.String userNo;
	/**
	 * 小类业务ID
	 */
	@Column(name = "APP_ID")
	private java.lang.Integer appId;
	/**
	 * 分区（YYYYMMDD）
	 */
	@Column(name = "PARTITION_ID")
	private java.lang.Integer partitionId;
	/**
	 * 输出最大记录数
	 */
	@Column(name = "MAX_CNT")
	private java.lang.Integer maxCnt;
	/**
	 * 状态[1:待运行,2:运行中,3:结束,4:无效]
	 */
	@Column(name = "STATUS")
	private java.lang.Integer status;
	/**
	 * 插入时间
	 */
	@Column(name = "INSERT_TIME")
	private java.util.Date insertTime;
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.String getUserNo() {
		return userNo;
	}
	public void setUserNo(java.lang.String userNo) {
		this.userNo = userNo;
	}
	public java.lang.Integer getAppId() {
		return appId;
	}
	public void setAppId(java.lang.Integer appId) {
		this.appId = appId;
	}
	public java.lang.Integer getPartitionId() {
		return partitionId;
	}
	public void setPartitionId(java.lang.Integer partitionId) {
		this.partitionId = partitionId;
	}
	public java.lang.Integer getMaxCnt() {
		return maxCnt;
	}
	public void setMaxCnt(java.lang.Integer maxCnt) {
		this.maxCnt = maxCnt;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public java.util.Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(java.util.Date insertTime) {
		this.insertTime = insertTime;
	}
}
