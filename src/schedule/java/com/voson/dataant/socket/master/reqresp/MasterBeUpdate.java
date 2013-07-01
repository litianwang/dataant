package com.voson.dataant.socket.master.reqresp;

import com.voson.dataant.schedule.mvc.event.Events;
import com.voson.dataant.schedule.mvc.event.JobMaintenanceEvent;
import com.voson.dataant.socket.SocketLog;
import com.voson.dataant.socket.master.MasterContext;
import com.voson.dataant.socket.protocol.Protocol.Status;
import com.voson.dataant.socket.protocol.Protocol.WebOperate;
import com.voson.dataant.socket.protocol.Protocol.WebRequest;
import com.voson.dataant.socket.protocol.Protocol.WebResponse;

public class MasterBeUpdate {
	public WebResponse beWebUpdate(MasterContext context,WebRequest req) {
		context.getDispatcher().forwardEvent(new JobMaintenanceEvent(Events.UpdateJob,req.getId()));
		WebResponse resp=WebResponse.newBuilder().setRid(req.getRid()).setOperate(WebOperate.UpdateJob)
			.setStatus(Status.OK).build();
		SocketLog.info("send web update response,rid="+req.getRid()+",jobId="+req.getId());
		return resp;
	}
}
