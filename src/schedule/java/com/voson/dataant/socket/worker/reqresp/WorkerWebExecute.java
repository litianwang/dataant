package com.voson.dataant.socket.worker.reqresp;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import com.voson.dataant.socket.SocketLog;
import com.voson.dataant.socket.master.AtomicIncrease;
import com.voson.dataant.socket.protocol.Protocol.ExecuteKind;
import com.voson.dataant.socket.protocol.Protocol.Response;
import com.voson.dataant.socket.protocol.Protocol.SocketMessage;
import com.voson.dataant.socket.protocol.Protocol.WebOperate;
import com.voson.dataant.socket.protocol.Protocol.WebRequest;
import com.voson.dataant.socket.protocol.Protocol.WebResponse;
import com.voson.dataant.socket.protocol.Protocol.SocketMessage.Kind;
import com.voson.dataant.socket.worker.WorkerContext;
import com.voson.dataant.socket.worker.WorkerHandler.ResponseListener;

public class WorkerWebExecute {
	
	public Future<WebResponse> send(final WorkerContext context,ExecuteKind kind,String id){
		
		final WebRequest req=WebRequest.newBuilder().setRid(AtomicIncrease.getAndIncrement()).setOperate(WebOperate.ExecuteJob)
			.setEk(kind).setId(id).build();
		SocketMessage sm=SocketMessage.newBuilder().setKind(Kind.WEB_REUQEST).setBody(req.toByteString()).build();
		
		Future<WebResponse> f=context.getThreadPool().submit(new Callable<WebResponse>() {
			private WebResponse response;
			public WebResponse call() throws Exception {
				final CountDownLatch latch=new CountDownLatch(1);
				context.getHandler().addListener(new ResponseListener() {
					public void onWebResponse(WebResponse resp) {
						if(resp.getRid()==req.getRid()){
							context.getHandler().removeListener(this);
							response=resp;
							latch.countDown();
						}
					}
					public void onResponse(Response resp) {}
				});
				latch.await();
				return response;
			}
		});
		context.getServerChannel().write(sm);
		SocketLog.info("send web execute request,rid="+req.getRid()+",kind="+kind+",id="+id);
		return f;
		
	}
	
}
