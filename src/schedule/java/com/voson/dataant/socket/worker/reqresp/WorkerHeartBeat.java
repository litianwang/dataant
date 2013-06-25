package com.voson.dataant.socket.worker.reqresp;

import java.util.Date;

import org.jboss.netty.channel.ChannelFuture;

import com.voson.dataant.job.JobContext;
import com.voson.dataant.job.sub.tool.MemUseRateJob;
import com.voson.dataant.schedule.mvc.ScheduleInfoLog;
import com.voson.dataant.socket.master.AtomicIncrease;
import com.voson.dataant.socket.protocol.Protocol.HeartBeatMessage;
import com.voson.dataant.socket.protocol.Protocol.Operate;
import com.voson.dataant.socket.protocol.Protocol.Request;
import com.voson.dataant.socket.protocol.Protocol.SocketMessage;
import com.voson.dataant.socket.protocol.Protocol.SocketMessage.Kind;
import com.voson.dataant.socket.worker.WorkerContext;

public class WorkerHeartBeat {

	public ChannelFuture execute(WorkerContext context){
		JobContext jobContext=JobContext.getTempJobContext();
		MemUseRateJob job=new MemUseRateJob(jobContext, 1);
		try {
			int exitCode = -1;
			int count = 0;
			while(count<3 && exitCode!=0){
				count++;
				exitCode=job.run();
			}
			if(exitCode!=0) {
				ScheduleInfoLog.error("HeartBeat Shell Error",new Exception(jobContext.getJobHistory().getLog().getContent()));
				// 防止后面NPE
				jobContext.putData("mem", 1.0);
			}
		} catch (Exception e) {
			ScheduleInfoLog.error("memratejob", e);
		}
		HeartBeatMessage hbm=HeartBeatMessage.newBuilder().setMemRate(((Double)jobContext.getData("mem")).floatValue())
			////.addAllDebugRunnings(context.getDebugRunnings().keySet())
			.addAllManualRunnings(context.getManualRunnings().keySet())
			.addAllRunnings(context.getRunnings().keySet())
			.setTimestamp(new Date().getTime()).build();
		Request req=Request.newBuilder().setRid(AtomicIncrease.getAndIncrement()).setOperate(Operate.HeartBeat).setBody(hbm.toByteString()).build();
		
		SocketMessage sm=SocketMessage.newBuilder().setKind(Kind.REQUEST).setBody(req.toByteString()).build();
		return context.getServerChannel().write(sm);
	}
}
