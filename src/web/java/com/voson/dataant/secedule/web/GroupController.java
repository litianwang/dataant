package com.voson.dataant.secedule.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Maps;
import com.voson.dataant.common.web.DateConvertEditor;
import com.voson.dataant.secedule.service.GroupService;
import com.voson.dataant.store.mysql.MysqlGroupManager;
import com.voson.dataant.store.mysql.persistence.GroupPersistence;

/**
 * Group管理的Controller, 使用Restful风格的Urls:
 * 
 * List page     : GET /group/
 * Create page   : GET /group/create
 * Create action : POST /group/create
 * Update page   : GET /group/update/{id}
 * Update action : POST /group/update
 * Delete action : GET /group/delete/{id}
 * 
 * @author litianwang
 */
@Controller
@RequestMapping(value = "/group")
public class GroupController {


	private static final int PAGE_SIZE = 10;

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("ruleName", "规则名");
	}

	@Autowired
	private GroupService groupService;
	
	@InitBinder
	protected void initBinder(HttpServletRequest request,
	                              ServletRequestDataBinder binder) throws Exception {
	    //对于需要转换为Date类型的属性，使用DateEditor进行处理
	    binder.registerCustomEditor(Date.class, new DateConvertEditor());
	}

	@RequestMapping(value = "")
	public String list(@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<GroupPersistence> dataantGroups = groupService.getDataantGroup(searchParams, pageNumber, PAGE_SIZE, sortType);

		model.addAttribute("dataantGroups", dataantGroups);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "group/groupList";
	}

	
	@RequestMapping(value = "add/parent/{parent}", method = RequestMethod.GET)
	public String addForm(@PathVariable("parent") Integer parent,Model model) {
		GroupPersistence dataantGroup = new GroupPersistence();
		dataantGroup.setParent(parent);
		model.addAttribute("dataantGroup", dataantGroup);
		model.addAttribute("action", "add");
		return "group/add-groupForm";
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(@RequestBody GroupPersistence newDataantGroup, RedirectAttributes redirectAttributes) {
		this.buildGroupPersistence(newDataantGroup);
		groupService.saveDataantGroup(newDataantGroup);
		redirectAttributes.addFlashAttribute("message", "创建组成功");
		Map<String, Object> modelMap = new HashMap<String, Object>(3);  
	    modelMap.put("total", "1");  
	    modelMap.put("data", newDataantGroup);  
	    modelMap.put("success", "true");
		return modelMap;
	}
	

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("dataantGroup", groupService.getDataantGroup(id));
		model.addAttribute("action", "update");
		return "group/groupForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("preloadDataantGroup") GroupPersistence dataantGroup, RedirectAttributes redirectAttributes) {
		this.buildGroupPersistence(dataantGroup);
		groupService.saveDataantGroup(dataantGroup);
		redirectAttributes.addFlashAttribute("message", "更新组成功");
		return "redirect:/group/update/" + dataantGroup.getId();
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		groupService.deleteDataantGroup(id);
		redirectAttributes.addFlashAttribute("message", "删除任务成功");
		return "redirect:/group/";
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出DataantGroup对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadDataantGroup")
	public GroupPersistence getDataantGroup(@RequestParam(value = "id", required = false) Integer id) {
		if (id != null) {
			return groupService.getDataantGroup(id);
		}
		return null;
	}
	
	public GroupPersistence buildGroupPersistence(GroupPersistence newGroup){
		if(StringUtils.isBlank(newGroup.getConfigs())){
			newGroup.setConfigs("{}");
		}
		if(StringUtils.isBlank(newGroup.getResources())){
			newGroup.setResources("[]");
		}
		if(StringUtils.isBlank(newGroup.getOwner())){
			newGroup.setOwner("litianwang");
		}
		return newGroup;
	}
}
