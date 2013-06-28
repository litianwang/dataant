package com.voson.dataant.secedule.web;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Maps;
import com.voson.dataant.common.web.DateConvertEditor;
import com.voson.dataant.secedule.service.JobHistoryService;
import com.voson.dataant.store.mysql.persistence.JobHistoryPersistence;

/**
 * JobHistory管理的Controller, 使用Restful风格的Urls:
 * 
 * List page     : GET /jobhistory/
 * Create page   : GET /jobhistory/create
 * Create action : POST /jobhistory/create
 * Update page   : GET /jobhistory/update/{id}
 * Update action : POST /jobhistory/update
 * Delete action : GET /jobhistory/delete/{id}
 * 
 * @author litianwang
 */
@Controller
@RequestMapping(value = "/jobhistory")
public class JobHistoryController {

	private static final int PAGE_SIZE = 10;

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("ruleName", "规则名");
	}

	@Autowired
	private JobHistoryService jobHistoryService;
	
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

		Page<JobHistoryPersistence> dataantJobHistorys = jobHistoryService.getDataantJobHistory(searchParams, pageNumber, PAGE_SIZE, sortType);

		model.addAttribute("dataantJobHistorys", dataantJobHistorys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "jobhistory/jobhistoryList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("dataantJobHistory", new JobHistoryPersistence());
		model.addAttribute("action", "create");
		return "jobhistory/jobhistoryForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid JobHistoryPersistence newDataantJobHistory, RedirectAttributes redirectAttributes) {
		// newDataantJobHistory.setInsertTime(new Date());
		jobHistoryService.saveDataantJobHistory(newDataantJobHistory);
		redirectAttributes.addFlashAttribute("message", "创建任务成功");
		return "redirect:/jobhistory/";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("dataantJobHistory", jobHistoryService.getDataantJobHistory(id));
		model.addAttribute("action", "update");
		return "jobhistory/jobhistoryForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("preloadDataantJobHistory") JobHistoryPersistence dataantJobHistory, RedirectAttributes redirectAttributes) {
		jobHistoryService.saveDataantJobHistory(dataantJobHistory);
		redirectAttributes.addFlashAttribute("message", "更新任务成功");
		return "redirect:/jobhistory/";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		jobHistoryService.deleteDataantJobHistory(id);
		redirectAttributes.addFlashAttribute("message", "删除任务成功");
		return "redirect:/jobhistory/";
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出DataantJobHistory对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadDataantJobHistory")
	public JobHistoryPersistence getDataantJobHistory(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return jobHistoryService.getDataantJobHistory(id);
		}
		return null;
	}
}
