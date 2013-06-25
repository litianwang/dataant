package com.voson.dataant.firstanalyze.web;

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
import com.voson.dataant.firstanalyze.model.FirstAnalyzeRuleHistory;
import com.voson.dataant.firstanalyze.service.FirstAnalyzeRuleHistoryService;

/**
 * FirstAnalyzeRuleHistory管理的Controller, 使用Restful风格的Urls:
 * 
 * List page     : GET /firstanalyzerulehistory/
 * Update page   : GET /firstanalyzerulehistory/update/{id}
 * Update action : POST /firstanalyzerulehistory/update
 * 
 * @author litianwang
 */
@Controller
@RequestMapping(value = "/firstanalyzerulehistory")
public class FirstAnalyzeRuleHistoryController {

	private static final int PAGE_SIZE = 10;

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("runDate", "运行日期");
	}

	@Autowired
	private FirstAnalyzeRuleHistoryService firstAnalyzeRuleHistoryService;
	
	@InitBinder
	protected void initBinder(HttpServletRequest request,
	                              ServletRequestDataBinder binder) throws Exception {
	    //对于需要转换为Date类型的属性，使用DateEditor进行处理
	    binder.registerCustomEditor(Date.class, new DateConvertEditor());
	}

	@RequestMapping(value = "{rule_id}")
	public String list(@PathVariable("rule_id") Long ruleId,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		if(null != ruleId){
			searchParams.put("EQ_ruleId", String.valueOf(ruleId));
		}
		Page<FirstAnalyzeRuleHistory> firstAnalyzeRuleHistorys = firstAnalyzeRuleHistoryService.getFirstAnalyzeRuleHistory(searchParams, pageNumber, PAGE_SIZE, sortType);

		model.addAttribute("firstAnalyzeRuleHistorys", firstAnalyzeRuleHistorys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "firstanalyzerulehistory/firstanalyzerulehistoryList";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("firstAnalyzeRuleHistory", firstAnalyzeRuleHistoryService.getFirstAnalyzeRuleHistory(id));
		model.addAttribute("action", "update");
		return "firstanalyzerulehistory/firstanalyzerulehistoryForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("preloadFirstAnalyzeRuleHistory") FirstAnalyzeRuleHistory firstAnalyzeRuleHistory, RedirectAttributes redirectAttributes) {
		firstAnalyzeRuleHistoryService.saveFirstAnalyzeRuleHistory(firstAnalyzeRuleHistory);
		redirectAttributes.addFlashAttribute("message", "更新任务成功");
		return "redirect:/firstanalyzerulehistory/" + firstAnalyzeRuleHistory.getRuleId();
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出FirstAnalyzeRuleHistory对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadFirstAnalyzeRuleHistory")
	public FirstAnalyzeRuleHistory getFirstAnalyzeRuleHistory(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return firstAnalyzeRuleHistoryService.getFirstAnalyzeRuleHistory(id);
		}
		return null;
	}
}
