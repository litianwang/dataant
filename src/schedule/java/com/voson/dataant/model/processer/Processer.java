package com.voson.dataant.model.processer;

import java.io.Serializable;

/**
 * 
 * Processer可以提供Job运行的前置处理或者后置处理
 * 
 * 存储格式：
 * {id="",config:""}
 * 
 * @author litianwang
 *
 */
public interface Processer extends Serializable{

	String getId();
	String getConfig();
	void parse(String config);
}
