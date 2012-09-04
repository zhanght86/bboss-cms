package com.frameworkset.platform.dictionary.input;

/**
 * 
 * <p>Title: InputType.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-11-29 19:52:44
 * @author ge.tao
 * @version 1.0
 */
public class InputType implements java.io.Serializable {
	private Integer inputTypeId;
	private String dataSourcePath;
	private String inputTypeDesc;
	private String inputTypeName;
	private String inputScript;
	
	private InputTypeScript inputTypeScript;
	
	
	public Integer getInputTypeId() {
		return inputTypeId;
	}
	public void setInputTypeId(Integer inputTypeId) {
		this.inputTypeId = inputTypeId;
	}
	public String getDataSourcePath() {
		return dataSourcePath;
	}
	public void setDataSourcePath(String dataSourcePath) {
		this.dataSourcePath = dataSourcePath;
	}
	
	public String getInputScript() {
		return inputScript;
	}
	public void setInputScript(String inputScript) {
		this.inputScript = inputScript;
	}
	public String getInputTypeName() {
		return inputTypeName;
	}
	public void setInputTypeName(String inputTypeName) {
		this.inputTypeName = inputTypeName;
	}
	public String getInputTypeDesc() {
		return inputTypeDesc;
	}
	public void setInputTypeDesc(String inputTypeDesc) {
		this.inputTypeDesc = inputTypeDesc;
	}
	/**
	 * 替换掉script里面的{extend} 附加代码
	 * @param inputScript
	 * @param objectName 
	 * InputType.java
	 * @author: ge.tao
	 */
	public void setInputScript(String inputScript,String extendstr) {
		int index = inputScript.indexOf("{extend}");
		StringBuffer html = new StringBuffer();
		if(index > 0){
			html.append(inputScript.substring(0,index)).append(extendstr).append(inputScript.substring(index+1));		
			this.inputScript = html.toString();
			html.setLength(0);
		}else{
			html.append("<input type='text' name='").append(extendstr.toLowerCase()).append("'>");
			this.inputScript = html.toString();
			html.setLength(0);
		}
	}
}
