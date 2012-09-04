package com.frameworkset.platform.ca;
/** 
 * <p>类说明:</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: 三一集团</p>
 * @author  gao.tang 
 * @version V1.0  创建时间：Oct 23, 2009 1:59:21 PM 
 */
public class CaException extends Exception {
	/**
	 * 
	 */
	public CaException() {
		super();
	}

	/**
	 * @param message
	 */
	public CaException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public CaException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CaException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
}
