package com.sany.workflow.job.exception;

public class ProcessParamCheckException extends Exception  {

	/**流程参数检查异常
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProcessParamCheckException() {
	}

	public ProcessParamCheckException(String message) {
		super(message);
	}

	public ProcessParamCheckException(Throwable cause) {
		super(cause);
	}

	public ProcessParamCheckException(String message, Throwable cause) {
		super(message, cause);
	}

}
