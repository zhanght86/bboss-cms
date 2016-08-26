/**
 * 
 */
package com.frameworkset.platform.cms.container;

/**
 * <p>Title: TmplateExport.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-9-27 17:33:05
 * @author ge.tao
 * @version 1.0
 */
public class TmplateExport implements java.io.Serializable {
	protected String tmplname;
	protected String tmpldesc;
	protected String exportdate;
	protected String exporter;	
	protected String flag;
	protected String siteid;
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getSiteid() {
		return siteid;
	}
	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}
	public String getExportdate() {
		return exportdate;
	}
	public void setExportdate(String exportdate) {
		this.exportdate = exportdate;
	}
	public String getExporter() {
		return exporter;
	}
	public void setExporter(String exporter) {
		this.exporter = exporter;
	}
	public String getTmpldesc() {
		return tmpldesc;
	}
	public void setTmpldesc(String tmpldesc) {
		this.tmpldesc = tmpldesc;
	}
	public String getTmplname() {
		return tmplname;
	}
	public void setTmplname(String tmplname) {
		this.tmplname = tmplname;
	}

}
