package com.voson.dataant.search.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MIA_URL_CLASS")
public class MiaUrlClass implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 类别id
	 */
	@Column(name = "ID")
	@Id
	private java.math.BigDecimal id;
	/**
	 * 名称
	 */
	@Column(name = "NAME")
	private java.lang.String name;
	/**
	 * 时间
	 */
	@Column(name = "INSERT_TIME")
	private java.util.Date insertTime;
	public java.math.BigDecimal getId() {
		return id;
	}
	public void setId(java.math.BigDecimal id) {
		this.id = id;
	}
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}
	public java.util.Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(java.util.Date insertTime) {
		this.insertTime = insertTime;
	}
	

}
