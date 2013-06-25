//package com.voson.dataant.schedule.mvc.event;
//
//import com.taobao.zeus.model.DebugHistory;
//import com.voson.hornet.mvc.AppEvent;
///**
// * Job执行成功事件
// * @author zhoufang
// *
// */
//public class DebugSuccessEvent extends AppEvent{
//	private DebugHistory history;
//	private String fileId;
//	public DebugSuccessEvent(String fileId,DebugHistory history) {
//		super(Events.JobSucceed);
//		this.fileId=fileId;
//		this.history=history;
//	}
//
//	public String getFileId() {
//		return fileId;
//	}
//
//	public DebugHistory getHistory() {
//		return history;
//	}
//}
