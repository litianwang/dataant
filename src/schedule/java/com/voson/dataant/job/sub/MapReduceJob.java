package com.voson.dataant.job.sub;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voson.dataant.job.JobContext;
import com.voson.dataant.job.sub.main.MapReduceMain;
import com.voson.dataant.store.HierarchyProperties;
import com.voson.dataant.util.RunningJobKeys;

public class MapReduceJob extends JavaJob{

	
	
	public MapReduceJob(JobContext jobContext) {
		super(jobContext);
		String main=getJavaClass();
		String args=getMainArguments();
		String classpath=getClassPaths();
		jobContext.getProperties().setProperty(RunningJobKeys.RUN_JAVA_MAIN_CLASS, "com.voson.dataant.job.sub.main.MapReduceMain");
		String hadoophome=System.getenv("HADOOP_HOME");
		if(hadoophome!=null && !"".equals("")){
			classpath+=File.pathSeparator+hadoophome+"/*";
		}
		jobContext.getProperties().setProperty(RunningJobKeys.RUN_CLASSPATH, classpath+
				File.pathSeparator+getSourcePathFromClass(MapReduceMain.class));
		jobContext.getProperties().setProperty(RunningJobKeys.RUN_JAVA_MAIN_ARGS, main+" "+args);
		
	}
	
	@Override
	public Integer run() throws Exception {
		List<Map<String, String>> resources=jobContext.getResources();
		if(resources!=null && !resources.isEmpty()){
			StringBuffer sb=new StringBuffer();
			for(Map<String, String> map:jobContext.getResources()){
				if(map.get("uri")!=null){
					String uri=map.get("uri");
					if(uri.startsWith("hdfs://") && uri.endsWith(".jar")){
						sb.append(uri.substring("hdfs://".length())).append(",");
					}
				}
			}
			jobContext.getProperties().setProperty("core-site.tmpjars", sb.toString().substring(0, sb.toString().length()-1));
		}
		return super.run();
	}

	public static void main(String[] args) {
		JobContext context=JobContext.getTempJobContext();
		Map<String, String> map=new HashMap<String, String>();
		map.put("hadoop.ugi.name", "uginame");
		HierarchyProperties properties=new HierarchyProperties(map);
		context.setProperties(properties);
		
		new MapReduceJob(context);
	}

}
