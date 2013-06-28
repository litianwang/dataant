package com.voson.dataant.job.sub.conf;

import org.apache.hadoop.conf.Configuration;

public class OnlineConf extends Configuration{
	public OnlineConf(){
		set("hadoop.job.ugi", "hadoop,hadoop,#hadoop");
		set("mapred.job.queue.name","default");
		//set("proxy.hosts","172.24.160.65:1080");
		//set("hadoop.rpc.socket.factory.class.default","HadoopProxy");
		set("fs.default.name", "hdfs://hadoop:9000");
		set("mapred.job.tracker","hadoop:9001");
		set("mapred.working.dir", "/group/tbdataapplication/tbresys"); 
	}
}
