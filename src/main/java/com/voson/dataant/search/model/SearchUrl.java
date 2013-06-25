package com.voson.dataant.search.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SEARCH_URL")
public class SearchUrl implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@Id
	private java.lang.Long id;
	/**
	 * 手机号码
	 */
	private java.lang.String userNo;
	/**
	 * HOST = A
	 */
	@Column(name="HOST_A")
	private java.lang.String hostA;
	/**
	 * URI = C
	 */
	@Column(name="URI_C")
	private java.lang.String uriC;
	/**
	 * 一级分类
	 */
	private java.lang.Short urlClassId;
	/**
	 * 二级分类
	 */
	private java.lang.Integer urlInterestId;
	/**
	 * 分区（YYYYMMDD）
	 */
	private java.lang.Integer partitionId;
	/**
	 * 输出最大记录数
	 */
	private java.lang.Integer maxCnt;
	/**
	 * [1:待运行,2:运行中,3:结束,4:无效]
	 */
	private String status;
	/**
	 * 插入时间
	 */
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

	public java.lang.String getHostA() {
		return hostA;
	}

	public void setHostA(java.lang.String hostA) {
		this.hostA = hostA;
	}

	public java.lang.String getUriC() {
		return uriC;
	}

	public void setUriC(java.lang.String uriC) {
		this.uriC = uriC;
	}

	public java.lang.Short getUrlClassId() {
		return urlClassId;
	}

	public void setUrlClassId(java.lang.Short urlClassId) {
		this.urlClassId = urlClassId;
	}

	public java.lang.Integer getUrlInterestId() {
		return urlInterestId;
	}

	public void setUrlInterestId(java.lang.Integer urlInterestId) {
		this.urlInterestId = urlInterestId;
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

	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	public java.util.Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(java.util.Date insertTime) {
		this.insertTime = insertTime;
	}

}
