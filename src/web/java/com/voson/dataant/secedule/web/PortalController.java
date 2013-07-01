/**
 * This file created at 2013-7-1.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.secedule.web;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voson.dataant.store.GroupBean;
import com.voson.dataant.store.JobBean;
import com.voson.dataant.store.mysql.MysqlGroupManager;

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
		String rootId = mysqlGroupManager.getRootGroupId();
		GroupBean root = mysqlGroupManager.getDownstreamGroupBean(rootId);
		
		String treeJson = this.buildGroupTree(root);
		System.out.println(treeJson);
		model.addAttribute("treeJson", treeJson);
		return "portal/index";
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
