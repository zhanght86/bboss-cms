package com.sany.sap.connection;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author yinbp
 *
 */
public class SapResult {

  private Map<String, Object> resultParams;
  private Map<String,List<Map<String, Object>>> resultTables;
  Map<String, Map<String, Object>> resultStructures;
  private String exception;

  public String getException() {
	return exception;
}

public void setException(String exception) {
	this.exception = exception;
}

public Map<String, Object> getResultParams() {
    return resultParams;
  }

  public void setResultParams(Map<String, Object> resultParams) {
    this.resultParams = resultParams;
  }

  public Map<String,List<Map<String, Object>>> getResultTables() {
    return resultTables;
  }

  public void setResultTables(Map<String,List<Map<String, Object>>> resultTables) {
    this.resultTables = resultTables;
  }

	public void setResultStructures(
			Map<String, Map<String, Object>> resultStructures) {
		this.resultStructures = resultStructures;
		
	}

public Map<String, Map<String, Object>> getResultStructures() {
	return resultStructures;
}

}

