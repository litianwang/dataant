package com.voson.dataant.socket.master.reqresp;

import java.util.Date;

import org.jboss.netty.channel.Channel;

import com.google.protobuf.InvalidProtocolBufferException;
import com.voson.dataant.socket.master.MasterContext;
import com.voson.dataant.socket.master.MasterWorkerHolder;
import com.voson.dataant.socket.master.MasterWorkerHolder.HeartBeatInfo;
import com.voson.dataant.socket.protocol.Protocol.HeartBeatMessage;
import com.voson.dataant.socket.protocol.Protocol.Request;

public class MasterBeHeartBeat {
	public void beHeartBeat(MasterContext context,Channel channel,Request request) {
		MasterWorkerHolder worker=context.getWorkers().get(channel);
		HeartBeatInfo newbeat=worker.new HeartBeatInfo();
		HeartBeatMessage hbm;
		try {
			hbm = HeartBeatMessage.newBuilder().mergeFrom(request.getBody()).build();
			newbeat.memRate=hbm.getMemRate();
			newbeat.runnings=hbm.getRunningsList();
			newbeat.debugRunnings=hbm.getDebugRunningsList();
			newbeat.manualRunnings=hbm.getManualRunningsList();
			newbeat.timestamp=new Date(hbm.getTimestamp());
			if(worker.heart==null || newbeat.timestamp.getTime()>worker.heart.timestamp.getTime()){
				worker.heart=newbeat;
			}
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}
}
