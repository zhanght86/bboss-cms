package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.CMSBaseTag;

public class VoteButtonTag extends CMSBaseTag {
	
	protected String style = "";

	protected String classname = "";
	
	public int doStartTag() throws JspException {
		super.doStartTag();
		String o = "<input type='button' value='投票'  onclick='vote()' class='"+classname+"'><INPUT type='button' value='查看'  onclick='view()'  class='"+classname+"'>";
		 o += "<iframe src='' name='iframe_delete' width=0 height=0></iframe>";
		 o += "<script language='javascript'>" +
		 		"function view(){" +
		 			"window.open('"+request.getContextPath()+"/cms/voteManager/seeresult_bz.jsp?questionid='+questionids,'mywindow','toolbar=no,left=100,top=50,width=600,scrollbars=yes,height=500,menubar=no,systemMenu=no');" +
		 		"}"+
		 		""+
		 		"function vote(){" +
		 			"document.frames[0].src = '"+request.getContextPath()+"/cms/voteManager/voteaction.jsp';"+
		 			"document.forms[0].target = 'iframe_delete';"+
		 			"document.forms[0].action = '"+request.getContextPath()+"/cms/voteManager/voteaction.jsp';" +
		 			"document.forms[0].submit();" +
		 		"}"+
		 	"</script>";
		 
		 try{
			 out.print(o);
		 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		 
		return this.SKIP_BODY;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}

/*VoteManager vote = new VoteManager();

boolean flag = false;
String voteids = request.getParameter("voteids");
//System.out.print(voteids);
if ("".equals(voteids) && null!=voteids) {
	List QuestionItems = vote.getActiveQuestion();
	for (int j = 0; QuestionItems != null
			&& j < QuestionItems.size(); j++) {

		int id = ((Question) QuestionItems.get(j)).getId();
		String title = ((Question) QuestionItems.get(j)).getTitle();

		if (vote.isToDate(id)) {
			out.println("<script language='javascript'>s +='主题"
					+ title + "已经过期！！';</script>");
			continue;
		}

		String style = ((Question) QuestionItems.get(j)).getStyle();

		if (style.equals("radio")) {

			String idd = request.getParameter("" + id);

			if (idd != null) {
				vote.updateVote(Integer.parseInt(idd));
				flag = true;
			}

		} else {

			String[] idd = request.getParameterValues("" + id);

			if (idd != null) {

				for (int i = 0; i < idd.length; i++) {
					int id0 = Integer.parseInt(idd[i]);
					vote.updateVote(id0);
				}
				flag = true;
			}
		}
	}

	if (flag) {

		out.println("<script language='javascript'>alert(s+'感谢你投了宝贵的一票！！');</script>");
	} else {
		out.println("<script language='javascript'>alert(s+'请你至少选择一项！！');</script>");
	}

} else {

	List QuestionItems = vote.getActiveQuestion(voteids);
	for (int j = 0; j < QuestionItems.size(); j++) {

		int id = ((Question) QuestionItems.get(j)).getId();
		String title = ((Question) QuestionItems.get(j)).getTitle();

		if (vote.isToDate(id)) {
			out.println("<script language='javascript'>s +='主题"
					+ title + "已经过期！！';</script>");
			continue;
		}

		String style = ((Question) QuestionItems.get(j)).getStyle();

		if (style.equals("radio")) {

			String idd = request.getParameter("" + id);

			if (idd != null) {
				vote.updateVote(Integer.parseInt(idd));
				flag = true;
			}

		} else {

			String[] idd = request.getParameterValues("" + id);

			if (idd != null) {

				for (int i = 0; i < idd.length; i++) {
					int id0 = Integer.parseInt(idd[i]);
					vote.updateVote(id0);
				}
				flag = true;
			}
		}

	}
	if (flag) {

		out.println("<script language='javascript'>alert(s+'感谢你投了宝贵的一票！！');</script>");
	} else {
		out.println("<script language='javascript'>alert(s+'请你至少选择一项！！');</script>");
	}

}

*/
