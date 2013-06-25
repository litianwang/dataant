package com.voson.dataant.search.web;

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
import com.voson.dataant.search.model.SearchApp;
import com.voson.dataant.search.service.SearchAppService;

/**
 * SearchApp管理的Controller, 使用Restful风格的Urls:
 * 
 * List page     : GET /searchapp/
 * Create page   : GET /searchapp/create
 * Create action : POST /searchapp/create
 * Update page   : GET /searchapp/update/{id}
 * Update action : POST /searchapp/update
 * Delete action : GET /searchapp/delete/{id}
 * 
 * @author litianwang
 */
@Controller
@RequestMapping(value = "/searchapp")
public class SearchAppController{

	private static final int PAGE_SIZE = 3;

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("ruleName", "规则名");
	}

	@Autowired
	private SearchAppService searchAppService;
	
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

		Page<SearchApp> searchApps = searchAppService.getSearchApp(searchParams, pageNumber, PAGE_SIZE, sortType);

		model.addAttribute("searchApps", searchApps);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "searchapp/searchappList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("searchApp", new SearchApp());
		model.addAttribute("action", "create");
		return "searchapp/searchappForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid SearchApp newSearchApp, RedirectAttributes redirectAttributes) {
		// newSearchApp.setInsertTime(new Date());
		searchAppService.saveSearchApp(newSearchApp);
		redirectAttributes.addFlashAttribute("message", "创建任务成功");
		return "redirect:/searchapp/";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("searchApp", searchAppService.getSearchApp(id));
		model.addAttribute("action", "update");
		return "searchapp/searchappForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("preloadSearchApp") SearchApp searchApp, RedirectAttributes redirectAttributes) {
		searchAppService.saveSearchApp(searchApp);
		redirectAttributes.addFlashAttribute("message", "更新任务成功");
		return "redirect:/searchapp/";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		searchAppService.deleteSearchApp(id);
		redirectAttributes.addFlashAttribute("message", "删除任务成功");
		return "redirect:/searchapp/";
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出SearchApp对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadSearchApp")
	public SearchApp getSearchApp(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return searchAppService.getSearchApp(id);
		}
		return null;
	}
}
