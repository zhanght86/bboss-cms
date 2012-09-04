package com.frameworkset.platform.dbmanager;

/**
 * TableInfo操作异常
 * @author Administrator
 *
 */
public class TableInfoManagerException extends Exception {
	
	public TableInfoManagerException(String errorMessage){
		super(errorMessage);
	}
	
	public TableInfoManagerException(){
		super();
	}
}
