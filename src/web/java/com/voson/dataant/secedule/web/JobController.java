package com.voson.dataant.secedule.web;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Maps;
import com.voson.dataant.common.web.DateConvertEditor;
import com.voson.dataant.secedule.service.JobService;
import com.voson.dataant.store.mysql.persistence.JobPersistence;

/**
 * Job管理的Controller, 使用Restful风格的Urls:
 * 
 * List page     : GET /job/
 * Create page   : GET /job/create
 * Create action : POST /job/create
 * Update page   : GET /job/update/{id}
 * Update action : POST /job/update
 * Delete action : GET /job/delete/{id}
 * 
 * @author litianwang
 */
@Controller
@RequestMapping(value = "/job")
public class JobController {

	private static final int PAGE_SIZE = 10;

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("ruleName", "规则名");
	}

	@Autowired
	private JobService jobService;
	
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

		Page<JobPersistence> jobs = jobService.getJob(searchParams, pageNumber, PAGE_SIZE, sortType);

		model.addAttribute("jobs", jobs);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "job/jobList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("job", new JobPersistence());
		model.addAttribute("action", "create");
		return "job/jobForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid JobPersistence newJob, RedirectAttributes redirectAttributes) {
		buildJobPersistence(newJob);
		jobService.saveJob(newJob);
		redirectAttributes.addFlashAttribute("message", "创建任务成功");
		return "redirect:/job/";
	}
	
	@RequestMapping(value = "create/group/{groupId}", method = RequestMethod.GET)
	public String createInGroupFrom(@PathVariable("groupId") Integer groupId, Model model) {
		JobPersistence newJob = new JobPersistence();
		newJob.setGroupId(groupId);
		model.addAttribute("job", newJob);
		model.addAttribute("action", "create");
		return "job/jobForm";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("job", jobService.getJob(id));
		model.addAttribute("action", "update");
		return "job/jobForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("preloadJob") JobPersistence job, RedirectAttributes redirectAttributes) {
		buildJobPersistence(job);
		jobService.saveJob(job);
		redirectAttributes.addFlashAttribute("message", "更新任务成功");
		return "redirect:/job/update/"+job.getId();
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		jobService.deleteJob(id);
		redirectAttributes.addFlashAttribute("message", "删除任务成功");
		return "redirect:/job/";
	}
	
	@RequestMapping(value = "run/{id}/{type}", method = RequestMethod.GET)
	public String run(@PathVariable("id") String id, @PathVariable("type")int type, RedirectAttributes redirectAttributes) throws Exception {
		jobService.runJob(id, type);
		redirectAttributes.addFlashAttribute("message", "手动运行作业开始");
		return "redirect:/job/update/"+id;
	}
	@RequestMapping(value = "switch/{id}/{auto}", method = RequestMethod.GET)
	public String switchAuto(@PathVariable("id") String id, @PathVariable("auto")Boolean auto, RedirectAttributes redirectAttributes) throws Exception {
		jobService.swtichAuto(id, auto);
		redirectAttributes.addFlashAttribute("message", "开启/关闭  自动调度  成功");
		return "redirect:/job/update/"+id;
	}
	
	public JobPersistence buildJobPersistence(JobPersistence newJob){
		if(StringUtils.isBlank(newJob.getConfigs())){
			newJob.setConfigs("{}");
		}
		if(StringUtils.isBlank(newJob.getReadyDependency())){
			newJob.setReadyDependency("{}");
		}
		if(StringUtils.isBlank(newJob.getResources())){
			newJob.setResources("[]");
		}
		if(StringUtils.isBlank(newJob.getPreProcessers())){
			newJob.setPreProcessers("[]");
		}
		if(StringUtils.isBlank(newJob.getPostProcessers())){
			newJob.setPostProcessers("[]");
		}
		if(StringUtils.isBlank(newJob.getOwner())){
			newJob.setOwner("litianwang");
		}
		return newJob;
	}


	/**
	 * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出Job对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadJob")
	public JobPersistence getJob(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return jobService.getJob(id);
		}
		return null;
	}
}
