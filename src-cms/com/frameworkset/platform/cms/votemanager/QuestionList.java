package com.frameworkset.platform.cms.votemanager;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.CMSManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class QuestionList extends DataInfoImpl {

	protected ListInfo getDataList(String arg0, boolean arg1, long offset,
            int maxPagesize) {
		
		AccessControl accessControl = AccessControl.getInstance();
		CMSManager cmsM = new CMSManager();
		cmsM.init(request,session,accessControl);
		int siteID = Integer.parseInt(cmsM.getSiteID());

		String surveyNm = request.getParameter("surveyName");
		String active = request.getParameter("active");
		String questionTitle = request.getParameter("questionTitle");
		String style = request.getParameter("style");
		String deptName = request.getParameter("deptName");
		String deptID = request.getParameter("deptID");
		String channelName = request.getParameter("channel");
		
		if (surveyNm!=null && !"".equals(surveyNm))
			surveyNm = " and c.name like '%"+surveyNm+"%' ";
		else
			surveyNm = "";
		
		if (!"".equals(channelName) && channelName!=null && !"null".equals(channelName))
			channelName = " and f.name='"+channelName+"' ";
		else
			channelName = "";

		if (!"".equals(active) && active!=null && !"null".equals(active) && !"2".equals(active))
			active = " and a.active ="+active+" ";
		else
			active="";

		if (questionTitle!=null && !"".equals(questionTitle))
			questionTitle = " and a.title like '%"+questionTitle+"%' ";
		else
			questionTitle = "";

		if (!"".equals(style) && style!=null && !"null".equals(style) && !"3".equals(style))
			style = " and a.style ="+style+" ";
		else
			style="";
		String strWhere = "";
		String where = surveyNm + active + questionTitle + style + channelName;
		if(deptID!=null&&!deptID.equals("")&&!deptName.equals("admin"))
		{
			where += " and c.DEPART_ID=e.id and e.orgid='"+deptID+"'";
			strWhere = ",td_comm_email_disposedep e ";
		}
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		try {
			
			String sql="select a.*,c.name from td_cms_vote_questions a,td_cms_vote_tq b,td_cms_vote_title c,TD_CMS_CHANNEL_VOTE d,TD_CMS_CHANNEL f " +strWhere+
					" where c.siteid="+siteID+" and a.id=b.quesiont_id and b.title_id=c.id and d.channel_id=f.channel_id and c.id=d.VOTE_TITLE_ID "+where+" order by a.istop desc,c.ctime desc";
			

			dbUtil.executeSelect(sql,(int)offset,maxPagesize);
			List list = new ArrayList();
			Question question;
			if(dbUtil.size()>0){
				for (int i = 0; i < dbUtil.size(); i++) {
					question = new Question();
					question.setId(dbUtil.getInt(i,"id"));
					question.setActive(dbUtil.getInt(i,"active"));
					question.setStyle(dbUtil.getInt(i,"style"));
					question.setTitle(dbUtil.getString(i,"title"));
					question.setVotecount(dbUtil.getInt(i,"votecount"));
					question.setSurveyName(dbUtil.getString(i,"name"));
					question.setIsTop(dbUtil.getInt(i,"istop"));
					list.add(question);
				}
				listInfo.setDatas(list);
				listInfo.setTotalSize(dbUtil.getTotalSize());
				return listInfo;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
