//package com.voson.dataant.store.mysql.tool;
//
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//import org.quartz.CronTrigger;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.voson.dataant.model.JobDescriptor;
//import com.voson.dataant.model.JobDescriptor.JobRunType;
//import com.voson.dataant.model.JobDescriptor.JobScheduleType;
//import com.voson.dataant.store.GroupBean;
//import com.voson.dataant.store.JobBean;
//import com.voson.dataant.util.DataantException;
//
//public class JobValidate {
//	@Autowired
//	private ReadOnlyGroupManager readOnlyGroupManager;
//
//	public boolean valide(JobDescriptor job) throws DataantException{
//		if(job.getJobType()==null){
//			throw new DataantException("任务类型必须填写");
//		}
//		if(job.getGroupId()==null){
//			throw new DataantException("所属组必须填写");
//		}
//		if(job.getJobType()==JobRunType.MapReduce){
//			if(job.getName()==null || job.getName().trim().equals("")){
//				throw new DataantException("name字段不能为空");
//			}
//			if(job.getAuto()){
//				if(job.getProperties().get("java.main.class")==null ||
//						job.getProperties().get("java.main.class").trim().equals("")){
//					throw new DataantException("必须填写Java Main类");
//				}
//				if(job.getScheduleType()==null){
//					throw new DataantException("调度类型必须填写");
//				}
//				if(job.getScheduleType()==JobScheduleType.Independent){
//					if(job.getCronExpression()==null || job.getCronExpression().trim().equals("")){
//						throw new DataantException("独立任务的定时表达式必须填写");
//					}
//					job.setDependencies(new ArrayList<String>());
//				}
//				//如果是依赖任务
//				if(job.getScheduleType()==JobScheduleType.Dependent){
//					//必须填写依赖项
//					if(job.getDependencies()==null || job.getDependencies().isEmpty()){
//						throw new DataantException("依赖任务必须填写依赖项");
//					}
//					job.setCronExpression("");
//				}
//			}
//		}else if(job.getJobType()==JobRunType.Shell){
//			if(job.getScript()==null){
//				throw new DataantException("Shell 脚本不得为空");
//			}
//		}else if(job.getJobType()==JobRunType.Hive){
//			if(job.getScript()==null){
//				throw new DataantException("Hive 脚本不得为空");
//			}
//		}
//		
//		if(job.getCronExpression()!=null && !job.getCronExpression().trim().equals("")){
//			try {
//				new CronTrigger("test", "test", job.getCronExpression());
//			} catch (ParseException e) {
//				throw new DataantException("cronExpression表达式格式出错");
//			}
//		}
//		
//		//检查依赖的死循环问题
//		GroupBean root=readOnlyGroupManager.getGlobeGroupBean();
//		Map<String, JobBean> allJobBeans=root.getAllSubJobBeans();
//		Set<JobBean> deps=new HashSet<JobBean>();
//		if(job.getScheduleType()==JobScheduleType.Dependent){
//			for(String jobId:job.getDependencies()){
//				if(allJobBeans.get(jobId)==null){
//					throw new DataantException("依赖任务："+jobId+" 不存在");
//				}
//				deps.add(allJobBeans.get(jobId));
//			}
//			check(job.getId(), deps);
//		}
//		return true;
//	}
//	//判断死循环问题
//	private static void check(String parentJobId,Set<JobBean> deps) throws DataantException{
//		for(JobBean job:deps){
//			if(job.getJobDescriptor().getId().equals(parentJobId)){
//				throw new DataantException("存在死循环依赖，请检查!");
//			}
//			if(job.getJobDescriptor().getScheduleType()==JobScheduleType.Dependent){
//				check(parentJobId,job.getDependee());
//			}
//		}
//	}
//}