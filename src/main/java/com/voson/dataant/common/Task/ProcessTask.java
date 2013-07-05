/**
 * This file created at 2013-6-24.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.Task;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <code>{@link ProcessTask}</code>
 *
 *
 * @author litianwang
 */
public class ProcessTask implements Task {
	
	private static Logger log = LoggerFactory.getLogger(ProcessTask.class);
	
	protected volatile Process process;
	
	protected boolean canceled=false;


	@Override
	public boolean isCanceled() {
		return canceled;
	}

	/* (non-Javadoc)
	 * @see com.voson.dataant.common.Task.Task#run()
	 */
	@Override
	public Integer run() throws Exception {
		return null;
	}

	@Override
	public void cancel() {
//		try {
//			new CancelHadoopJob(jobContext).run();
//		} catch (Exception e1) {
//			log(e1);
//		}
		//强制kill 进程
		if (process != null) {
			log.warn("WARN Attempting to kill the process ");
			try {
				process.destroy();
				int pid=getProcessId();
				Runtime.getRuntime().exec("kill "+pid);
			} catch (Exception e) {
				log.warn("", e);
			} finally{
				process=null;
			}
		}
		
	}
	
	private int getProcessId() {
		int processId = 0;

		try {
			Field f = process.getClass().getDeclaredField("pid");
			f.setAccessible(true);

			processId = f.getInt(process);
		} catch (Throwable e) {
		}

		return processId;
	}
	

	/**
	 * Splits the command into a unix like command line structure. Quotes and
	 * single quotes are treated as nested strings.
	 * 
	 * @param command
	 * @return
	 */
	public static String[] partitionCommandLine(String command) {
		
		ArrayList<String> commands = new ArrayList<String>();
		
		String os=System.getProperties().getProperty("os.name");
		if(os!=null && (os.startsWith("win") || os.startsWith("Win"))){
			commands.add("CMD.EXE");
			commands.add("/C");
			commands.add(command);
		}else{
			int index = 0;

	        StringBuffer buffer = new StringBuffer(command.length());

	        boolean isApos = false;
	        boolean isQuote = false;
	        while(index < command.length()) {
	            char c = command.charAt(index);

	            switch(c) {
	                case ' ':
	                    if(!isQuote && !isApos) {
	                        String arg = buffer.toString();
	                        buffer = new StringBuffer(command.length() - index);
	                        if(arg.length() > 0) {
	                            commands.add(arg);
	                        }
	                    } else {
	                        buffer.append(c);
	                    }
	                    break;
	                case '\'':
	                    if(!isQuote) {
	                        isApos = !isApos;
	                    } else {
	                        buffer.append(c);
	                    }
	                    break;
	                case '"':
	                    if(!isApos) {
	                        isQuote = !isQuote;
	                    } else {
	                        buffer.append(c);
	                    }
	                    break;
	                default:
	                    buffer.append(c);
	            }

	            index++;
	        }

	        if(buffer.length() > 0) {
	            String arg = buffer.toString();
	            commands.add(arg);
	        }
		}
        return commands.toArray(new String[commands.size()]);
	}
}
