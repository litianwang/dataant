package com.voson.dataant.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationContext;

import com.voson.dataant.job.Job;
import com.voson.dataant.job.JobContext;
import com.voson.dataant.job.RenderHierarchyProperties;
import com.voson.dataant.job.WithProcesserJob;
import com.voson.dataant.job.sub.HadoopShellJob;
import com.voson.dataant.job.sub.HiveJob;
import com.voson.dataant.job.sub.MapReduceJob;
import com.voson.dataant.model.JobHistory;
import com.voson.dataant.model.JobDescriptor.JobRunType;
import com.voson.dataant.store.HierarchyProperties;
import com.voson.dataant.store.JobBean;

public class JobUtils {
	
	public static final Pattern PT = Pattern.compile("download\\[(doc|hdfs|http)://.+]");
	
	public static Job createJob(JobContext jobContext,JobBean jobBean,JobHistory history,String workDir,
			ApplicationContext applicationContext){
		jobContext.setJobHistory(history);
		jobContext.setWorkDir(workDir);
		HierarchyProperties hp=jobBean.getHierarchyProperties();
		if(history.getProperties()!=null && !history.getProperties().isEmpty()){
			history.getLog().appendDataant("This job hava instance configs:");
			for(String key:history.getProperties().keySet()){
				hp.setProperty(key, history.getProperties().get(key));
				history.getLog().appendDataant(key+"="+history.getProperties().get(key));
			}
		}
		jobContext.setProperties(new RenderHierarchyProperties(hp));
		List<Map<String, String>> resources = jobBean.getHierarchyResources();
		String script = jobBean.getJobDescriptor().getScript();
		// 处理脚本中的 资源引用 语句
		if(jobBean.getJobDescriptor().getJobType().equals(JobRunType.Shell)
				||jobBean.getJobDescriptor().getJobType().equals(JobRunType.Hive)){
			//script = resolvScriptResource(resources, script, applicationContext);
			jobBean.getJobDescriptor().setScript(script);
		}
		jobContext.setResources(resources);
		script=replace(jobContext.getProperties().getAllProperties(), script);
		hp.setProperty(PropertyKeys.JOB_SCRIPT, script);
		
		//添加Dataant标记属性，提供给外部
		hp.setProperty("hadoop.mapred.job.dataant_id", "dataant_job_"+history.getJobId()+"_"+history.getId());
		
//		//前置处理Job创建
//		List<Job> pres=parseJobs(jobContext, applicationContext, jobBean,jobBean.getJobDescriptor().getPreProcessers(),history,workDir);
//		pres.add(0, new DownloadJob(jobContext));
//		//后置处理Job创建
//		List<Job> posts=parseJobs( jobContext, applicationContext, jobBean,jobBean.getJobDescriptor().getPostProcessers(),history,workDir);
//		posts.add(new ZooKeeperJob(jobContext, null, applicationContext));
		//核心处理Job创建
		Job core=null;
		if(jobBean.getJobDescriptor().getJobType()==JobRunType.MapReduce){
			core=new MapReduceJob(jobContext);
		}else if(jobBean.getJobDescriptor().getJobType()==JobRunType.Shell){
			core=new HadoopShellJob(jobContext);
		}else if(jobBean.getJobDescriptor().getJobType()==JobRunType.Hive){
			core=new HiveJob(jobContext,applicationContext);
		}
		//TODO:tivan
		List<Job> pres = new ArrayList<Job>();
		List<Job> posts = new ArrayList<Job>();
		Job job=new WithProcesserJob(jobContext, pres, posts, core,applicationContext);
		
		return job;
	}
/*
	private static String resolvScriptResource(
			List<Map<String, String>> resources, String script, ApplicationContext context) {
		Matcher m = PT.matcher(script);
		while(m.find()){
			String s = m.group();
			s = s.substring(s.indexOf('[')+1,s.indexOf(']'));
			String[] args = StringUtils.split(s,' ');
			String uri = args[0];
			String name = "";
			String referScript = null;
			String path = uri.substring(uri.lastIndexOf('/')+1);
			Map<String, String> map = new HashMap<String, String>(2);
			if(uri.startsWith("doc://")){
				FileManager manager = (FileManager) context.getBean("fileManager");
				FileDescriptor fd = manager.getFile(path);
				name = fd.getName();
				// 把脚本放到map里，减少后面一次getFile调用
				referScript = fd.getContent();
			}
			if(args.length>1){
				name = "";
				for(int i=1;i<args.length;i++) {
					if(i>1) {
						name += "_";
					}
					name += args[i];
				}
			}else if(args.length==1){
				//没有指定文件名
				if (uri.startsWith("hdfs://")) {
					if(uri.endsWith("/")) {
						continue;
					}
					name = path;
				}
			}
			boolean exist = false;
			for(Map<String, String> ent : resources){
				if(ent.get("name").equals(name)){
					exist = true;
					break;
				}
			}
			if(!exist){
				map.put("uri", uri);
				map.put("name", name);
				resources.add(map);
				// 把脚本放到map里，减少后面一次getFile调用
				if(uri.startsWith("doc://") && referScript!=null) {
					map.put("dataant-doc-"+path, resolvScriptResource(resources,referScript,context));
				}
			}
		}
		script = m.replaceAll("");
		return script;
	}*/
	private static String replace(Map<String, String> map,String content){
		if(content==null){
			return null;
		}
		Map<String, String> newmap=new HashMap<String, String>();
		for(String key:map.keySet()){
			if(map.get(key)!=null){
				newmap.put("${"+key+"}", map.get(key));
			}
		}
		for(String key:newmap.keySet()){
			String old="";
			do{
				old=content;
				content=content.replace(key, newmap.get(key));
			}while(!old.equals(content));
		}
		return content;
	}
	/*
	private static List<Job> parseJobs(JobContext jobContext,ApplicationContext applicationContext,JobBean jobBean,
			List<Processer> ps,JobHistory history,String workDir){
		List<Job> jobs=new ArrayList<Job>();
		Map<String, String> map=jobContext.getProperties().getAllProperties();
		Map<String, String> newmap=new HashMap<String, String>();
		for(String key:map.keySet()){
			if(map.get(key)!=null){
				newmap.put("${"+key+"}", map.get(key));
			}
		}
		for(Processer p:ps){
			String config=p.getConfig();
			if(config!=null && !"".equals(config.trim())){
				for(String key:newmap.keySet()){
					String old="";
					do{
						old=config;
						String value=newmap.get(key).replace("\"", "\\\"");
						config=config.replace(key, value);
					}while(!old.equals(config));
				}
				p.parse(config);
			}
			if(p instanceof DownloadProcesser){
				jobs.add(new DownloadJob(jobContext));
			}else if(p instanceof ZooKeeperProcesser){
				ZooKeeperProcesser zkp=(ZooKeeperProcesser)p;
				if(!zkp.getUseDefault()){
					jobs.add(new ZooKeeperJob(jobContext, (ZooKeeperProcesser) p,applicationContext));
				}
			}else if(p instanceof MailProcesser){
				jobs.add(new MailJob(jobContext, (MailProcesser)p, applicationContext));
			}else if(p instanceof WangWangProcesser){
				jobs.add(new WangWangJob(jobContext));
			}else if(p instanceof OutputCheckProcesser){
				jobs.add(new OutputCheckJob(jobContext, (OutputCheckProcesser)p, applicationContext));
			}else if(p instanceof OutputCleanProcesser){
				jobs.add(new OutputCleanJob(jobContext, (OutputCleanProcesser)p, applicationContext));
			}else if(p instanceof HiveProcesser){
				jobs.add(new HiveProcesserJob(jobContext, (HiveProcesser) p, applicationContext));
			}else if(p instanceof JobProcesser){
				Integer depth=(Integer) jobContext.getData("depth");
				if(depth==null){
					depth=0;
				}
				if(depth<2){//job 的递归深度控制，防止无限递归
					JobProcesser jobProcesser=(JobProcesser) p;
					GroupManager groupManager=(GroupManager) applicationContext.getBean("groupManager");
					JobBean jb=groupManager.getUpstreamJobBean(jobProcesser.getJobId());
					if(jb!=null){
						for(String key:jobProcesser.getKvConfig().keySet()){
							if(jobProcesser.getKvConfig().get(key)!=null){
								jb.getJobDescriptor().getProperties().put(key, jobProcesser.getKvConfig().get(key));
							}
						}
						File direcotry=new File(workDir+File.separator+"job-processer-"+jobProcesser.getJobId());
						if(!direcotry.exists()){
							direcotry.mkdirs();
						}
						JobContext sub=new JobContext(jobContext.getRunType());
						sub.putData("depth", ++depth);
						Job job=createJob(sub,jb, history, direcotry.getAbsolutePath(), applicationContext);
						jobs.add(job);
					}
				}else{
					jobContext.getJobHistory().getLog().appendDataant("递归的JobProcesser处理单元深度过大，停止递归");
				}
			}
		}
		return jobs;
	}
	*/
}
