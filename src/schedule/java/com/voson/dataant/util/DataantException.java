package com.voson.dataant.util;

public class DataantException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DataantException(){
		super();
	}
	
	public DataantException(String message){
		super(message);
	}
	
	public DataantException(Throwable e){
		super(e);
	}
	
	public DataantException(String msg,Throwable e){
		super(msg, e);
	}

}
