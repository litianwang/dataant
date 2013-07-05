/**
 * This file created at 2013-7-1.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.secedule.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.voson.dataant.store.GroupBean;
import com.voson.dataant.store.JobBean;
import com.voson.dataant.store.mysql.MysqlGroupManager;
import com.voson.dataant.store.mysql.persistence.GroupPersistence;

/**
 * <code>{@link PortalController}</code>
 *
 * 门户页面
 *
 * @author litianwang
 */
@Controller
@RequestMapping(value = "/portal")
public class PortalController {
	
	@Autowired
	private MysqlGroupManager mysqlGroupManager;
	
	@RequestMapping(value = "")
	public String index(Model model, ServletRequest request) {
		return "portal/index";
	}
	
	@RequestMapping(value = "group/tree", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> tree(Model model, ServletRequest request) {
		String rootId = mysqlGroupManager.getRootGroupId();
		GroupBean root = mysqlGroupManager.getDownstreamGroupBean(rootId);
		String treeJson = this.buildGroupTree(root);
		Map<String, Object> modelMap = new HashMap<String, Object>(3);  
	    modelMap.put("total", "1");  
	    modelMap.put("data", JSONObject.fromObject(treeJson));  
	    modelMap.put("success", "true");
		return modelMap;
	}
	
	private String buildGroupTree(GroupBean group){
		String tree = "{ url:'group/update/" + group.getGroupDescriptor().getId() 
					+ "', text:'" + group.getGroupDescriptor().getName() 
					+ "',isexpand:false";
		String child = "";
		List<GroupBean> childGroups  = group.getChildrenGroupBeans();
		if(null != childGroups ){
			for (GroupBean childGroup : childGroups) {
				child += this.buildGroupTree(childGroup) + ",";
			}
		}
		if(StringUtils.isNotBlank(child)){
			child = child.substring(0,child.lastIndexOf(","));
			tree += ",children: [" + child + "]";
		}
		String job = "";
		Map<String, JobBean> jobBeanMap = group.getJobBeans();
		for (String key : jobBeanMap.keySet()) {
			JobBean jobBean = jobBeanMap.get(key);
			job += "{url:'job/update/"+ jobBean.getJobDescriptor().getId() +"',text:'"+jobBean.getJobDescriptor().getName()+"'},";
		}
		if(StringUtils.isNotBlank(job)){
			job = job.substring(0,job.lastIndexOf(","));
			tree += ",children: [" + job + "]";
		}
		tree+="}";
		return tree;
	}
}
