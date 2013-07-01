package com.voson.dataant.socket.master.reqresp;

import com.voson.dataant.model.JobHistory;
import com.voson.dataant.socket.SocketLog;
import com.voson.dataant.socket.master.MasterContext;
import com.voson.dataant.socket.protocol.Protocol.ExecuteKind;
import com.voson.dataant.socket.protocol.Protocol.Status;
import com.voson.dataant.socket.protocol.Protocol.WebOperate;
import com.voson.dataant.socket.protocol.Protocol.WebRequest;
import com.voson.dataant.socket.protocol.Protocol.WebResponse;

public class MasterBeWebExecute {
	public WebResponse beWebExecute(MasterContext context,WebRequest req) {
		if(req.getEk()==ExecuteKind.ManualKind || req.getEk()==ExecuteKind.ScheduleKind){
			String historyId=req.getId();
			JobHistory history=context.getJobHistoryManager().findJobHistory(historyId);
			String jobId=history.getJobId();
			context.getMaster().run(history);
			
			WebResponse resp=WebResponse.newBuilder().setRid(req.getRid()).setOperate(WebOperate.ExecuteJob)
				.setStatus(Status.OK).build();
			SocketLog.info("send web execute response,rid="+req.getRid()+",jobId="+jobId);
			return resp;
		}
		return null;
	}
}
