package com.frameworkset.common.tag.html;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.jsp.JspException;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;

public class ChnlGuideTag extends CMSBaseTag {

	String outputStrng = "";
	String channelid = "";
	String liImgSrc = "";
	String picWidth = "";
	String picHeight = "";
	String bgcolor = "";
	String dateStyle = "";
	String liTxtStyle = "";
	String maxTxtLnth = "";
	String width = "";
	String height = "";
	String count = "";
	String chnlPath = "";
	boolean dateShow = true;
	int rowspan = 4;

	public boolean isDateShow() {
		return dateShow;
	}

	public void setDateShow(boolean dateShow) {
		this.dateShow = dateShow;
	}

	public ChnlGuideTag() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	protected void drawBord(){
		try {
			ChannelManagerImpl chnlMgr = new ChannelManagerImpl();

			Channel chnl = chnlMgr.getChannelInfo(channelid);
			
			if (chnl==null){
				outputStrng = "ERROR in channelid!";
				return;
			}
				
			String chnlHref = CMSTagUtil.getPublishedChannelPath(context,chnl);
			
			outputStrng = "<table width=\""+width+"\" height=\""+height+"\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\""+bgcolor+"\">"
				+"<TR height=\"18px\">"
				+"<TD colspan=\"3\">"
				+"<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><TR>"
				+"<TD width=\"35%\" align=\"center\" style=\"color: #FFFFFF\" bgcolor=\"#FF3300\"><STRONG>"+chnl.getName()+"</STRONG></TD>"
				+"<TD align=\"right\" style=\"font-size: small;	color: #FFFFFF;	font-family: 华文细黑;\" bgcolor=\"#CCCCFF\">"
				+"<a href=\""+chnlHref+"\" style=\"color: #FF1100\">更多&gt;&gt;</a></TD>"
				+"</TR></table></TD></TR>";
			
			DocumentManagerImpl doc = new DocumentManagerImpl();
			List docLst = doc.getTopDocOfChnl(channelid,Integer.parseInt(count));
			if (docLst.size()==0){
				outputStrng += "<tr><td></td></tr></table>";
			}

			String docHerf = CMSTagUtil.getPublishedContentPath(context,chnl.getChannelPath(),String.valueOf(((Document)docLst.get(0)).getDocument_id()));
			String picPath = CMSTagUtil.getPublishedLinkPath(context,chnl.getOutlinepicture());
			liImgSrc = CMSTagUtil.getPublishedLinkPath(context,liImgSrc);
			SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日");
			Date docDate= ((Document)docLst.get(0)).getPublishTime();
			
			outputStrng += "<TR bgcolor=\"#eeeeee\">";
			outputStrng += "<td rowspan=\""+String.valueOf(rowspan)+"\" valign=\"top\" align=\"left\">"
				+"<img src=\""+picPath+ "/"+chnl.getOutlinepicture()+"\" width=\""+picWidth+"\" height=\""+picHeight+"\"></td>"
				+"<td align=\"left\" ><li style=\"list-style: url("+liImgSrc+");\">" +
				"<A href=\""+docHerf+"\" class=\"guide_style\">"
				+(((Document)docLst.get(0)).getTitle().length()<=Integer.parseInt(maxTxtLnth)?((Document)docLst.get(0)).getTitle():(((Document)docLst.get(0)).getTitle().substring(0,Integer.parseInt(maxTxtLnth))+"..."))+"</A></li></td>"
				+"<td class=\""+dateStyle+"\" align=\"right\" >"+(dateShow&&docDate!=null?sdf.format(docDate):"")
				+"</td></TR>";
			
			for(int i=1;i<docLst.size()&&i<rowspan;i++){
				docDate= ((Document)docLst.get(i)).getPublishTime();
				docHerf = CMSTagUtil.getPublishedContentPath(context, chnl.getChannelPath(),String.valueOf(((Document)docLst.get(i)).getDocument_id()));
				outputStrng += "<TR bgcolor=\"#eeeeee\">";
				outputStrng += "<td align=\"left\" ><li style=\"list-style: url("+liImgSrc+");\">" +
				"<A href=\""+docHerf+"\" class=\"guide_style\">"
				+(((Document)docLst.get(i)).getTitle().length()<=Integer.parseInt(maxTxtLnth)?((Document)docLst.get(i)).getTitle():(((Document)docLst.get(i)).getTitle().substring(0,Integer.parseInt(maxTxtLnth))+"..."))+"</A></li></td>"
				+"<td class=\""+dateStyle+"\" align=\"right\" >"+(dateShow&&docDate!=null?sdf.format(docDate):"")
				+"</td></TR>";
			}
			
			for(int i=rowspan;i<docLst.size();i++){
				docDate= ((Document)docLst.get(i)).getPublishTime();
				docHerf = CMSTagUtil.getPublishedContentPath(context, chnl.getChannelPath(),String.valueOf(((Document)docLst.get(i)).getDocument_id()));
				outputStrng += "<td align=\"left\" colspan=\"2\"><li style=\"list-style: url("+liImgSrc+");\">" +
				"<A href=\""+docHerf+"\" class=\"guide_style\">"
				+((Document)docLst.get(i)).getTitle()+"</A></li></td>"
				+"<td class=\""+dateStyle+"\" align=\"right\" >"+(dateShow&&docDate!=null?sdf.format(docDate):"")
				+"</td></TR>";
			}
			
			outputStrng += "</table>";
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int doEndTag() throws JspException{

		try {
			drawBord(); 
			pageContext.getOut().println(outputStrng);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doEndTag();
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getDateStyle() {
		return dateStyle;
	}

	public void setDateStyle(String dateStyle) {
		this.dateStyle = dateStyle;
	}

	public String getLiImgSrc() {
		return liImgSrc;
	}

	public void setLiImgSrc(String liImgSrc) {
		this.liImgSrc = liImgSrc;
	}

	public String getLiTxtStyle() {
		return liTxtStyle;
	}

	public void setLiTxtStyle(String liTxtStyle) {
		this.liTxtStyle = liTxtStyle;
	}

	public String getMaxTxtLnth() {
		return maxTxtLnth;
	}

	public void setMaxTxtLnth(String maxTxtLnth) {
		this.maxTxtLnth = maxTxtLnth;
	}

	public String getPicHeight() {
		return picHeight;
	}

	public void setPicHeight(String picHeight) {
		this.picHeight = picHeight;
	}

	public String getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(String picWidth) {
		this.picWidth = picWidth;
	}

	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
/*
 * 	<table width="400" height="400" border="0" cellpadding="0" cellspacing="0" bordercolor="#CCCCCC" bgcolor="#FFFFFF">
	<TR height="18px">
		<TD colspan="3">
		<table border="0" cellpadding="0" cellspacing="0" width="100%"><TR>
		<TD width="35%" align="center" style="color: #FFFFFF" bgcolor="#FF3300"><STRONG>自然地理</STRONG></TD>
		<TD align="right" style="font-size: small;	color: #FFFFFF;	font-family: 华文细黑;" bgcolor="#CCCCFF">
		 <a href="#" style="color: #993300">更多&gt;&gt;</a></TD>
		 </TR></table>
		 </TD>
	</TR>
	<TR bgcolor="#eeeeee">
		<td rowspan="5" valign="top" align="left"><img src="Img.jpg" width="60px"></td>
		<td align="left" ><li style="list-style: url(Img.jpg);"><A href="#" class="guide_style">&nbsp;</A></li></td>
		<td class="date_Style" align="right" >5月2日</td>
	</TR>
	<TR bgcolor="#eeeeee">
		<td ><li>&nbsp;</li></td>
		<td class="date_Style" align="right">5月2日</td>
	</TR>
	<TR bgcolor="#eeeeee">
		<td ><li><A href="#" class="guide_style">的方各地方国法还会...</A></li></td>
		<td class="date_Style" align="right">5月2日</td>
	</TR>
	<TR bgcolor="#eeeeee">
		<td ><li>&nbsp;</li></td>
		<td class="date_Style" align="right">5月2日</td>
	</TR>
	<TR bgcolor="#eeeeee">
		<td ><li>&nbsp;</li></td>
		<td class="date_Style" align="right">12月20日</td>
	</TR>
	<TR bgcolor="#eeeeee">
		<td colspan="2"><li><A href="#" class="guide_style">形象逼真可擦洗，制作无需美术基础</a></li></td>
		<td class="date_Style" align="right">12月20日</td>
	</TR>
	<TR bgcolor="#eeeeee">
		<td colspan="2"><li><A href="#" class="guide_style">形象逼真可擦洗，制作无需美术基础</a></li></td>
		<td class="date_Style" align="right">12月20日</td>
	</TR>
	<TR bgcolor="#eeeeee">
		<td colspan="2"><li><A href="#" class="guide_style">形象逼真可擦洗，制作无需美术基础</a></li></td>
		<td class="date_Style" align="right">12月20日</td>
	</TR>
	</table>
 **/

	@Override
	public void doFinally() {
		  outputStrng = "";
		  channelid = "";
		  liImgSrc = "";
		  picWidth = "";
		  picHeight = "";
		  bgcolor = "";
		  dateStyle = "";
		  liTxtStyle = "";
		  maxTxtLnth = "";
		  width = "";
		  height = "";
		  count = "";
		  chnlPath = "";
		  dateShow = true;
		  rowspan = 4;
		super.doFinally();
	}
}
