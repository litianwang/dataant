package com.voson.dataant.search.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.voson.dataant.search.model.SearchUrl;
import com.voson.dataant.search.service.SearchUrlService;

/**
 * SearchUrl管理的Controller, 使用Restful风格的Urls:
 * 
 * List page     : GET /searchurl/
 * Create page   : GET /searchurl/create
 * Create action : POST /searchurl/create
 * Update page   : GET /searchurl/update/{id}
 * Update action : POST /searchurl/update
 * Delete action : GET /searchurl/delete/{id}
 * 
 * @author litianwang
 */
@Controller
@RequestMapping(value = "/searchurl")
public class SearchUrlController {

	private static final int PAGE_SIZE = 3;

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("ruleName", "规则名");
	}

	@Autowired
	private SearchUrlService searchUrlService;
	
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

		Page<SearchUrl> searchUrls = searchUrlService.getSearchUrl(searchParams, pageNumber, PAGE_SIZE, sortType);

		model.addAttribute("searchUrls", searchUrls);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "searchurl/searchurlList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("searchUrl", new SearchUrl());
		model.addAttribute("action", "create");
		return "searchurl/searchurlForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid SearchUrl newSearchUrl, RedirectAttributes redirectAttributes) {
		
		searchUrlService.saveSearchUrl(newSearchUrl);
		redirectAttributes.addFlashAttribute("message", "创建任务成功");
		return "redirect:/searchurl/";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("searchUrl", searchUrlService.getSearchUrl(id));
		model.addAttribute("action", "update");
		return "searchurl/searchurlForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("preloadSearchUrl") SearchUrl searchUrl, RedirectAttributes redirectAttributes) {
		test(searchUrl.getUriC());
		searchUrlService.saveSearchUrl(searchUrl);
		redirectAttributes.addFlashAttribute("message", "更新任务成功");
		return "redirect:/searchurl/";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		searchUrlService.deleteSearchUrl(id);
		redirectAttributes.addFlashAttribute("message", "删除任务成功");
		return "redirect:/searchurl/";
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出SearchUrl对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadSearchUrl")
	public SearchUrl getSearchUrl(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return searchUrlService.getSearchUrl(id);
		}
		return null;
	}
	
	public void test(String cmds){

		/**
		 * 测试代码
		 */
		Process process;
		ProcessBuilder builder = new ProcessBuilder(partitionCommandLine(cmds));
		builder.directory(new File("/home/hadoop/dgch/dataant/work"));
		try {
			process=builder.start();
		
			final InputStream inputStream = process.getInputStream();
			final InputStream errorStream = process.getErrorStream();
			
			String threadName= "test=";
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try{
						BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
						String line;
						while((line=reader.readLine())!=null){
							System.out.println("std##" + line);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			},threadName).start();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						BufferedReader reader=new BufferedReader(new InputStreamReader(errorStream));
						String line;
						while((line=reader.readLine())!=null){
							System.out.println("err##" + line);
							
						}
					} catch (Exception e) {
							e.printStackTrace();
						}
				}
			},threadName).start();
			int exitCode = -999;
			try {
				exitCode = process.waitFor();
			} catch (InterruptedException e) {
				// log(e);
			} finally{
				process=null;
			}
			if(exitCode!=0){
				System.out.println("exitCode=" + exitCode);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static String[] partitionCommandLine(String command) {
		
		ArrayList<String> commands = new ArrayList<String>();
		
		String os=System.getProperties().getProperty("os.name");
		if(os!=null && (os.startsWith("win") || os.startsWith("Win"))){
			commands.add("CMD.EXE");
			commands.add("/C");
			commands.add(command);
		}else{
			int index = 0;

	        StringBuffer buffer = new StringBuffer(command.length());

	        boolean isApos = false;
	        boolean isQuote = false;
	        while(index < command.length()) {
	            char c = command.charAt(index);

	            switch(c) {
	                case ' ':
	                    if(!isQuote && !isApos) {
	                        String arg = buffer.toString();
	                        buffer = new StringBuffer(command.length() - index);
	                        if(arg.length() > 0) {
	                            commands.add(arg);
	                        }
	                    } else {
	                        buffer.append(c);
	                    }
	                    break;
	                case '\'':
	                    if(!isQuote) {
	                        isApos = !isApos;
	                    } else {
	                        buffer.append(c);
	                    }
	                    break;
	                case '"':
	                    if(!isApos) {
	                        isQuote = !isQuote;
	                    } else {
	                        buffer.append(c);
	                    }
	                    break;
	                default:
	                    buffer.append(c);
	            }

	            index++;
	        }

	        if(buffer.length() > 0) {
	            String arg = buffer.toString();
	            commands.add(arg);
	        }
		}
        return commands.toArray(new String[commands.size()]);
	}
	static Pattern p=Pattern.compile("^.*(userurl_([0-9]*)_([0-9]*)_.*\\.trans\\.gz)$");
	
	
	
	public static void main(String[] args) {
		// "/user/hadoop/dgch/userurl/2013/06/20/13/userurl_20130620_10_44_000.trans.gz"
		String aa = "std##-rw-r--r--   1 hadoop supergroup       1389 2013-06-20 00:00 /user/hadoop/dgch/userurl/2013/06/20/00/userurl_20130620_10_44_000.trans.gz";
		
				
		
		Matcher m=p.matcher(aa);
		int i=0;
		while(m.find()){
			System.out.println(m.group(1));
			String dateStr = m.group(2);
			if(dateStr.length() != 8){
				continue;
			}
			String hourStr = m.group(3);
			int year = Integer.valueOf(dateStr.substring(0,4));
			int month = Integer.valueOf(dateStr.substring(4,6));
			int day = Integer.valueOf(dateStr.substring(6,8));
			int hour = Integer.valueOf(hourStr);
			String cc = String.format("hadoop fs -mv /user/hadoop/dgch/userurl/processed/userurl_%1$s%2$02d%3$02d_%4$02d* /user/hadoop/dgch/userurl/%1$s/%2$02d/%3$02d/%4$02d",
					year, month, day, hour);
			System.out.println(cc);
		}
	}
}
