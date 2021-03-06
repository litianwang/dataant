package com.voson.dataant.socket.master;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.Channel;

public class MasterWorkerHolder{
	public class HeartBeatInfo{
		public Float memRate;
		public List<String> runnings;
		public List<String> manualRunnings;
		public List<String> debugRunnings;
		public Date timestamp;
		public HeartBeatInfo(){}
	}
	private final Channel channel;
	/** 布尔值标记是不是已经发送过超时报警 */
	private ConcurrentHashMap<String, Boolean> runnings=new ConcurrentHashMap<String,Boolean>();
	/** 布尔值标记是不是已经发送过超时报警 */
	private ConcurrentHashMap<String, Boolean> manualRunnings=new ConcurrentHashMap<String,Boolean>();
	public HeartBeatInfo heart;
	public MasterWorkerHolder(Channel channel){
		this.channel=channel;
	}
	public Channel getChannel() {
		return channel;
	}

	public HeartBeatInfo getHeart() {
		return heart;
	}
	public ConcurrentHashMap<String, Boolean> getRunnings() {
		return runnings;
	}
	public ConcurrentHashMap<String, Boolean> getManualRunnings() {
		return manualRunnings;
	}
}
