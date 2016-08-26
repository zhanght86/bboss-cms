package com.frameworkset.platform.cms.docCommentManager;

/**
 * 文档评论管理异常
 * @author Administrator
 *
 */
public class DocCommentManagerException extends Exception implements java.io.Serializable{
	public DocCommentManagerException(String exceptionMessage){
		super(exceptionMessage);
	}
	public DocCommentManagerException(){
		super();
	}
	public DocCommentManagerException(String string, Exception e) {
		super(string,   e);
	}
}
