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
import com.voson.dataant.search.model.MiaUrlClass;
import com.voson.dataant.search.service.MiaUrlClassService;

/**
 * MiaUrlClass管理的Controller, 使用Restful风格的Urls:
 * 
 * List page     : GET /miaurlclass/
 * Create page   : GET /miaurlclass/create
 * Create action : POST /miaurlclass/create
 * Update page   : GET /miaurlclass/update/{id}
 * Update action : POST /miaurlclass/update
 * Delete action : GET /miaurlclass/delete/{id}
 * 
 * @author litianwang
 */
@Controller
@RequestMapping(value = "/miaurlclass")
public class MiaUrlClassController {

	private static final int PAGE_SIZE = 3;

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("ruleName", "规则名");
	}

	@Autowired
	private MiaUrlClassService miaUrlClassService;
	
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

		Page<MiaUrlClass> miaUrlClasss = miaUrlClassService.getMiaUrlClass(searchParams, pageNumber, PAGE_SIZE, sortType);

		model.addAttribute("miaUrlClasss", miaUrlClasss);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "miaurlclass/miaurlclassList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("miaUrlClass", new MiaUrlClass());
		model.addAttribute("action", "create");
		return "miaurlclass/miaurlclassForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid MiaUrlClass newMiaUrlClass, RedirectAttributes redirectAttributes) {
		// newMiaUrlClass.setInsertTime(new Date());
		miaUrlClassService.saveMiaUrlClass(newMiaUrlClass);
		redirectAttributes.addFlashAttribute("message", "创建任务成功");
		return "redirect:/miaurlclass/";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("miaUrlClass", miaUrlClassService.getMiaUrlClass(id));
		model.addAttribute("action", "update");
		return "miaurlclass/miaurlclassForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("preloadMiaUrlClass") MiaUrlClass miaUrlClass, RedirectAttributes redirectAttributes) {
		miaUrlClassService.saveMiaUrlClass(miaUrlClass);
		redirectAttributes.addFlashAttribute("message", "更新任务成功");
		return "redirect:/miaurlclass/";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		miaUrlClassService.deleteMiaUrlClass(id);
		redirectAttributes.addFlashAttribute("message", "删除任务成功");
		return "redirect:/miaurlclass/";
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出MiaUrlClass对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadMiaUrlClass")
	public MiaUrlClass getMiaUrlClass(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return miaUrlClassService.getMiaUrlClass(id);
		}
		return null;
	}
}
