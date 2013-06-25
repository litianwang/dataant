//package com.voson.dataant.schedule.mvc;
//
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//
//import com.voson.dataant.mvc.DispatcherListener;
//import com.voson.dataant.mvc.MvcEvent;
//import com.voson.dataant.socket.master.MasterContext;
///**
// * 任务失败的监听
// * 当任务失败，需要发送邮件给相关人员
// * @author zhoufang
// *
// */
//public class JobSuccessListener extends DispatcherListener{
//	private static Logger log=LogManager.getLogger(JobSuccessListener.class);
//	private GroupManager groupManager;
//	
//	private JobHistoryManager jobHistoryManager;
//	public JobSuccessListener(MasterContext context){
//		groupManager=(GroupManager) context.getGroupManager();
//		jobHistoryManager=(JobHistoryManager)context.getJobHistoryManager();
//	}
//	@Override
//	public void beforeDispatch(MvcEvent mvce) {
//		try {
//			if(mvce.getAppEvent() instanceof JobSuccessEvent){
//				final JobSuccessEvent event=(JobSuccessEvent) mvce.getAppEvent();
//				if(event.getTriggerType()==TriggerType.SCHEDULE){
//					return;
//				}
//				JobHistory history=jobHistoryManager.findJobHistory(event.getHistoryId());
//				final JobDescriptor jd=groupManager.getJobDescriptor(history.getJobId()).getX();
//				if(history.getOperator()!=null){
//					//此处可以发送IM消息
//				}
//			}
//		} catch (Exception e) {
//			//处理异常，防止后续的依赖任务受此影响，无法正常执行
//			log.error("失败任务，发送通知出现异常",e);
//		}
//	}
//}
