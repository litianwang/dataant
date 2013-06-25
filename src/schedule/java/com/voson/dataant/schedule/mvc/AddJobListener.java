//package com.voson.dataant.schedule.mvc;
//
//import java.util.ArrayList;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.voson.dataant.mvc.AppEvent;
//import com.voson.dataant.mvc.Controller;
//import com.voson.dataant.mvc.DispatcherListener;
//import com.voson.dataant.mvc.MvcEvent;
//import com.voson.dataant.socket.master.Master;
//import com.voson.dataant.socket.master.MasterContext;
///**
// * 如果是新增操作，这里进行处理，添加controller
// * @author zhoufang
// *
// */
//public class AddJobListener extends DispatcherListener{
//
//	private static Logger log=LoggerFactory.getLogger(AddJobListener.class);
//	
//	private Master master;
//	
//	private MasterContext context;
//	
//	public AddJobListener(MasterContext context,Master master){
//		this.master=master;
//		this.context=context;
//	}
//	@Override
//	public void beforeDispatch(MvcEvent mvce) {
//		
//		if(mvce.getAppEvent() instanceof JobMaintenanceEvent){
//			JobMaintenanceEvent event=(JobMaintenanceEvent)mvce.getAppEvent();
//			String jobId=event.getJobId();
//			boolean exist=false;
//			for(Controller c:new ArrayList<Controller>(context.getDispatcher().getControllers())){
//				if(c instanceof JobController){
//					JobController jc=(JobController)c;
//					if(jc.getJobId().equals(jobId)){
//						exist=true;
//						break;
//					}
//				}
//			}
//			if(!exist){//新增操作
//				JobController controller=new JobController(context,master, jobId);
//				context.getDispatcher().addController(controller);
//				controller.handleEvent(new AppEvent(Events.Initialize));
//				mvce.setCancelled(true);
//				log.error("schedule add job with jobId:"+jobId);
//			}
//			
//		}
//	}
//}
