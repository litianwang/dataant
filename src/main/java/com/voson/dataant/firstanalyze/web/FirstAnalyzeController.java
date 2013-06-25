package com.voson.dataant.firstanalyze.web;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Maps;
import com.voson.dataant.firstanalyze.model.FirstAnalyzeRule;
import com.voson.dataant.firstanalyze.service.FirstAnalyzeService;

/**
 * FirstAnalyzeRule管理的Controller, 使用Restful风格的Urls:
 * 
 * List page     : GET /firstanalyze/
 * Create page   : GET /firstanalyze/create
 * Create action : POST /firstanalyze/create
 * Update page   : GET /firstanalyze/update/{id}
 * Update action : POST /firstanalyze/update
 * Delete action : GET /firstanalyze/delete/{id}
 * 
 * @author litianwang
 */
@Controller
@RequestMapping(value = "/firstanalyze")
public class FirstAnalyzeController {

	private static final int PAGE_SIZE = 3;

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("ruleName", "规则名");
	}

	@Autowired
	private FirstAnalyzeService firstanalyzeService;

	@RequestMapping(value = "")
	public String list(@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<FirstAnalyzeRule> firstanalyzes = firstanalyzeService.getFirstAnalyzeRule(searchParams, pageNumber, PAGE_SIZE, sortType);

		model.addAttribute("firstanalyzes", firstanalyzes);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "firstanalyze/firstanalyzeList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("firstanalyze", new FirstAnalyzeRule());
		model.addAttribute("action", "create");
		return "firstanalyze/firstanalyzeForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid FirstAnalyzeRule newFirstAnalyzeRule, RedirectAttributes redirectAttributes) {
		newFirstAnalyzeRule.setInsertTime(new Date());
		firstanalyzeService.saveFirstAnalyzeRule(newFirstAnalyzeRule);
		redirectAttributes.addFlashAttribute("message", "创建任务成功");
		return "redirect:/firstanalyze/";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("firstanalyze", firstanalyzeService.getFirstAnalyzeRule(id));
		model.addAttribute("action", "update");
		return "firstanalyze/firstanalyzeForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("preloadFirstAnalyzeRule") FirstAnalyzeRule firstanalyze, RedirectAttributes redirectAttributes) {
		firstanalyzeService.saveFirstAnalyzeRule(firstanalyze);
		redirectAttributes.addFlashAttribute("message", "更新任务成功");
		return "redirect:/firstanalyze/";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		firstanalyzeService.deleteFirstAnalyzeRule(id);
		redirectAttributes.addFlashAttribute("message", "删除任务成功");
		return "redirect:/firstanalyze/";
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出FirstAnalyzeRule对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadFirstAnalyzeRule")
	public FirstAnalyzeRule getFirstAnalyzeRule(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return firstanalyzeService.getFirstAnalyzeRule(id);
		}
		return null;
	}
	
}
