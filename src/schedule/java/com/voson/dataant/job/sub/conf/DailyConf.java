package com.voson.dataant.job.sub.conf;

import org.apache.hadoop.conf.Configuration;


public class DailyConf extends Configuration{

	public DailyConf(){
		set("hadoop.job.ugi", "hadoop,hadoop,#hadoop");
		set("mapred.job.queue.name","default");
		//set("proxy.hosts","10.232.101.170:1080");
		//set("hadoop.rpc.socket.factory.class.default","HadoopProxy");
		set("fs.default.name", "hdfs://hadoop:9000");
		set("mapred.working.dir", "/group/tbdataapplication/litianwang"); 
	}
}
