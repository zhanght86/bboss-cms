package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.platform.cms.votemanager.VoteManager;
import com.frameworkset.platform.cms.votemanager.VoteManagerImpl;
import com.frameworkset.common.tag.BaseCellTag;

public class VoteTag extends BaseCellTag {

	public VoteTag() {
		super();

	}

	VoteManager vote = new VoteManagerImpl();

	private String voteids = "";

	private String width = "";
	
	private String trheight = "";


	public String voteTagShow() throws Exception {
		String outputString="";
//	 if (voteids == "") {
//	
//		// outputString="<link href='"+request.getContextPath()+"/cms/inc/css/cms.css' rel='stylesheet' type='text/css'>";
//           outputString+= "<form name='form1' method='post' action='"+request.getContextPath()+"/cms/voteManager/voteaction.jsp?voteids="+voteids+"'><table width="+width+"  border='1' cellpadding='0' align='center' cellspacing='0'>";
//
////		VoteManager vote = new VoteManager();
////		List QuestionItems = vote.getActiveQuestion();
////		for (int j = 0; j < QuestionItems.size(); j++) {
////			int idd = ((Question) QuestionItems.get(j)).getId();
////			String style = ((Question) QuestionItems.get(j)).getStyle();
////
////			List items = vote.getDiscriptionItems(idd);
////			if (items.size() > 0) {
////				outputString += "<tr height="+trheight+"> <td width='100%'  colspan='2'> "
////						+ "【"
////						+ ((Item) items.get(0)).getTitle() + "】</td></tr>";
////			}
////			if (style.equals("radiostyle")) {
////				for (int i = 0; i < items.size(); i++) {
////					outputString += "<tr height="+trheight+"><td><input type='radio' name='"
////							+ ((Item) items.get(i)).getQid()
////							+ "' value='"
////							+ ((Item) items.get(i)).getId()
////							+ "'>"
////							+ ((Item) items.get(i)).getOptions() + "</td></tr>";
////				}
////			} else {
////				for (int i = 0; i < items.size(); i++) {
////
////					outputString += "<tr height="+trheight+"><td><input type='checkbox' name='"
////							+ ((Item) items.get(i)).getQid()
////							+ "' value='"
////							+ ((Item) items.get(i)).getId()
////							+ "'>"
////							+ ((Item) items.get(i)).getOptions() + "</td></tr>";
////				}
////			}
//			outputString += "<tr height='"+trheight+"'><td> </td></tr>";
//		}
//		outputString += "<tr height="+trheight+"><td>"
//		+ "<P align='center'><input type='button' value='投票'  onclick='abc()' ><INPUT type='button' value='查看'  onclick='cw()'></P></td></tr></td></tr></table></form> <iframe src='' name='iframe_delete' width=0 height=0></iframe>";
//   String path=request.getContextPath();
//		//out.print(path);
//   outputString += "<script language='JavaScript'> function cw(){window.open('"+path+"/cms/voteManager/seeresult_tag.jsp?voteids="+voteids+"','mywindow','toolbar=no,left=150,top=200,width=420,scrollbars=yes,height=450,menubar=no,systemMenu=no');}";
//	outputString += " function abc(){document.frames[0].src = '"+request.getContextPath()+"/cms/voteManager/voteaction.jsp?voteids="+voteids+"';document.forms[0].target = 'iframe_delete';document.forms[0].action = '"+request.getContextPath()+"/cms/voteManager/voteaction.jsp?voteids="+voteids+"';document.forms[0].submit(); }</script>";
//	 }
//	 else
//	 {
//		 String[] voteids0=voteids.split(",");
//		// outputString="<link href='"+request.getContextPath()+"/cms/inc/css/cms.css' rel='stylesheet' type='text/css'>";
//         outputString += "<form name='form1' method='post' action='"+request.getContextPath()+"/cms/voteManager/voteaction.jsp?voteids="+voteids+"'><table width="+width+"  border='1' cellpadding='0' align='center' cellspacing='0' >";
//
//		for (int j = 0; j <voteids0.length; j++) {
//			int idd =Integer.parseInt(voteids0[j]);
//			
//			List questionItems = vote.getActiveQuestion(idd);
//			if (questionItems ==null || questionItems.size()==0)
//				continue;
//			
//			String style = ((Question) questionItems.get(0)).getStyle();
//
//			List items = vote.getDiscriptionItems(idd);
//			if (items.size() > 0) {
//				outputString += "<tr height="+trheight+"> <td width='100%'  colspan='2'> "
//						+ "【"
//						+ ((Item) items.get(0)).getTitle() + "】</td></tr>";
//			}
//			if (style.equals("radiostyle")) {
//				for (int i = 0; i < items.size(); i++) {
//					outputString += "<tr height="+trheight+"><td><input type='radio' name='"
//							+ ((Item) items.get(i)).getQid()
//							+ "' value='"
//							+ ((Item) items.get(i)).getId()
//							+ "'>"
//							+ ((Item) items.get(i)).getOptions() + "</td></tr>";
//				}
//			} else {
//				for (int i = 0; i < items.size(); i++) {
//
//					outputString += "<tr height="+trheight+"><td><input type='checkbox' name='"
//							+ ((Item) items.get(i)).getQid()
//							+ "' value='"
//							+ ((Item) items.get(i)).getId()
//							+ "'>"
//							+ ((Item) items.get(i)).getOptions() + "</td></tr>";
//				}
//			}
//			outputString += "<tr height='"+trheight+"'><td> </td></tr>";
//		}
//		outputString += "<tr height="+trheight+"><td>"
//				+ "<P align='center'><input type='button' value='投票'  onclick='abc()' ><INPUT type='button' value='查看'  onclick='cw()'></P></td></tr></td></tr></table></form> <iframe src='' name='iframe_delete' width=0 height=0></iframe>";
//		String path=request.getContextPath();
//		//out.print(path);
//		outputString += "<script language='JavaScript'> function cw(){window.open('"+path+"/cms/voteManager/seeresult_tag.jsp?voteids="+voteids+"','mywindow','toolbar=no,left=150,top=200,width=420,scrollbars=yes,height=450,menubar=no,systemMenu=no');}";
//		outputString += " function abc(){document.frames[0].src = '"+request.getContextPath()+"/cms/voteManager/voteaction.jsp?voteids="+voteids+"';document.forms[0].target = 'iframe_delete';document.forms[0].action = '"+request.getContextPath()+"/cms/voteManager/voteaction.jsp?voteids="+voteids+"';document.forms[0].submit(); }</script>";
//		 
//
//		 
//	 }
		return outputString;
	}

	
	public int doStartTag() throws JspException {
		super.doStartTag();
		try {
			out.println(voteTagShow());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	
	public String getTrheight() {
		return trheight;
	}

	public void setTrheight(String trheight) {
		this.trheight = trheight;
	}

	public VoteManager getVote() {
		return vote;
	}

	public void setVote(VoteManager vote) {
		this.vote = vote;
	}

	public String getVoteids() {
		return voteids;
	}

	public void setVoteids(String voteids) {
		this.voteids = voteids;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}


	@Override
	public void doFinally() {
		 voteids = "";

			  width = "";
			
			  trheight = "";

		super.doFinally();
	}

}
