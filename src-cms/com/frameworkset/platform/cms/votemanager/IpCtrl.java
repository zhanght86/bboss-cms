package com.frameworkset.platform.cms.votemanager;


public class IpCtrl {

	private int id;

	private int titleId;

	private String ipStart;

	private String ipEnd;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIpEnd() {
		return ipEnd;
	}

	public void setIpEnd(String ipEnd) {
		this.ipEnd = ipEnd;
	}

	public String getIpStart() {
		return ipStart;
	}

	public void setIpStart(String ipStart) {
		this.ipStart = ipStart;
	}

	public int getTitleId() {
		return titleId;
	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}

	public IpCtrl() {
		super();
		// TODO 自动生成构造函数存根
	}

	/*<tr><td>
		<input type="checkbox" name="qstionTbl1Chkbx" id="qstionTbl1Chkbx" value="checkbox"><input name="qstionTbl1Option" id="qstionTbl1Option" type="text" size="50">
		  </td></tr>
		  
		  <tr><td><input type='checkbox' name='"+tblName+"Chkbx' id='"+tblName+"Chkbx' value='checkbox'><input name='"+tblName+"Option' id='"+tblName+"Option' type='text' size='50'></td></tr>
		  */
	/*<tr>
									<td>
										*
										<input type="checkbox" name="ipCtrlChlbx" value="checkbox">
										<input type="text" name="ipCtrlStart">
										--
										<input type="text" name="ipCtrlEnd">
									</td>
								</tr>*/
	
	/*<tr>
									<td>
										*
										<input type="checkbox" name="timeCtrlChlbx" value="checkbox">
										<input name="timeCtrlStart" type="text" size="10" readonly="true" onFocus="return ">
										--
										<input name="timeCtrlEnd" type="text" size="10" readonly="ture" onFocus="return ">
									</td>
								</tr>*/
}
