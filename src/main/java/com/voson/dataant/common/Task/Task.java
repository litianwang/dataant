/**
 * This file created at 2013-6-8.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.Task;


/**
 * <code>{@link Task}</code>
 *
 * TODO : document me
 *
 * @author litianwang
 */
public interface Task {
	
	Integer run() throws Exception;
	
	void cancel();
	
	boolean isCanceled();
}
