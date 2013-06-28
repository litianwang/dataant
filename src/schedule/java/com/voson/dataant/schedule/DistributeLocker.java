package com.voson.dataant.schedule;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.voson.dataant.schedule.mvc.ScheduleInfoLog;
import com.voson.dataant.socket.worker.ClientWorker;
import com.voson.dataant.store.mysql.persistence.DistributeLock;
import com.voson.dataant.store.mysql.persistence.dao.DistributeLockDao;
import com.voson.dataant.util.Environment;

/**
 * 分布式服务器的检测器
 * 每隔一分钟查询一次数据库的dataant_lock表
 * @author litianwang
 *
 */
public class DistributeLocker {

	private static Logger log=LogManager.getLogger(DistributeLocker.class);
	
	@Autowired
	DistributeLockDao distributeLockDao;
	
	public static String host=UUID.randomUUID().toString();
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ClientWorker worker;
	
	private DataantSchedule dataantSchedule;
	
	private int port=9887;
	
	static{
		try {
			host=InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			//ignore
		}
	}
	
	public DistributeLocker(String port){
		try {
			this.port=Integer.valueOf(port);
		} catch (NumberFormatException e) {
			log.error("port must be a number", e);
		}
	}
	
	public void init() throws Exception{
		dataantSchedule=new DataantSchedule(applicationContext);
		ScheduledExecutorService service=Executors.newScheduledThreadPool(3);
		service.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				try {
					update();
				} catch (Exception e) {
					log.error(e);
				}
			}
		}, 20, 60, TimeUnit.SECONDS);
	}
	/**
	 * 定时扫描任务
	 * 每隔一分钟扫描一次dataant_lock表
	 * 判断ScheduleServer是否正常运行
	 * @author litianwang
	 *
	 */
	private void update(){
		Page<DistributeLock>  locks = distributeLockDao.findBySubgroupOrderByIdDesc(Environment.getScheduleGroup(), new PageRequest(0, 1));
		DistributeLock lock = null;
		if(null != locks && locks.iterator().hasNext()){
			lock = locks.iterator().next();
		}
		if(lock==null){
			lock=new DistributeLock();
			lock.setHost(host);
			lock.setServerUpdate(new Date());
			lock.setSubgroup(Environment.getScheduleGroup());
			lock = distributeLockDao.save(lock);
		}
		
		
		if(host.equals(lock.getHost())){
			log.error("hold the locker and update time");
			lock.setServerUpdate(new Date());
			distributeLockDao.save(lock);
			
			dataantSchedule.startup(port);
		}else{//其他服务器抢占了锁
			log.error("not my locker");
			//如果最近更新时间在2分钟以上，则认为抢占的Master服务器已经失去连接，本服务器主动进行抢占
			if(System.currentTimeMillis()-lock.getServerUpdate().getTime()>1000*60*2L){
				log.error("rob the locker and update");
				lock.setHost(host);
				lock.setServerUpdate(new Date());
				lock.setSubgroup(Environment.getScheduleGroup());
				distributeLockDao.save(lock);
				dataantSchedule.startup(port);
			}else{//如果Master服务器没有问题，本服务器停止server角色
				dataantSchedule.shutdown();
			}
			
		}
		
		try {
			worker.connect(lock.getHost(),port);
		} catch (Exception e) {
			ScheduleInfoLog.error("start up worker fail", e);
		}
	}
	
}
