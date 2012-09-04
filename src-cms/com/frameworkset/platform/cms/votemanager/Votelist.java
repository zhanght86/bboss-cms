package com.frameworkset.platform.cms.votemanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.CMSManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class Votelist extends  DataInfoImpl {

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		DBUtil db = new DBUtil();
		List res = new ArrayList();
		
		AccessControl accessControl = AccessControl.getInstance();
		CMSManager cmsM = new CMSManager();
		cmsM.init(request,session,accessControl);
		int siteID = Integer.parseInt(cmsM.getSiteID());
		String channelID = request.getParameter("channelid");
		String channelName = request.getParameter("channel");
		String surveyTitle = request.getParameter("surveyTitle");
		String active = request.getParameter("active");
		String createDateFrom = request.getParameter("createDateFrom");
		String createDateTo = request.getParameter("createDateTo");
		String deptID = request.getParameter("deptID");//机构ID
		String deptName = request.getParameter("deptName");//机构名字
		
		if (!"".equals(channelID) && channelID!=null && !"null".equals(channelID))
			channelID = " and c.CHANNEL_ID="+channelID+" ";
		else
			channelID="";
		if (!"".equals(channelName) && channelName!=null && !"null".equals(channelName))
			channelName = " and d.name='"+channelName+"' ";
		else
			channelName = "";
		
		if (!"".equals(surveyTitle) && surveyTitle!=null && !"null".equals(surveyTitle))
			surveyTitle = " and a.NAME like '%"+surveyTitle+"%' ";
		else
			surveyTitle="";
		
		if (!"".equals(active) && active!=null && !"null".equals(active) && !"2".equals(active))
			active = " and a.active ="+active+" ";
		else
			active="";
		
		if (!"".equals(createDateFrom) && createDateFrom!=null && !"null".equals(createDateFrom))
			createDateFrom = " and a.ctime >=to_date('"+createDateFrom+"','yyyy-mm-dd') ";
		else
			createDateFrom="";
		
		if (!"".equals(createDateTo) && createDateTo!=null && !"null".equals(createDateTo))
			createDateTo = " and a.ctime <=to_date('"+createDateTo+"','yyyy-mm-dd')+1 ";
		else
			createDateTo="";
		String strWhere = "";
		String where = channelID + surveyTitle + active + createDateFrom + createDateTo + channelName;
		if(deptID!=null&&!deptID.equals("")&&!deptName.equals("admin"))
		{
			where += " and e.orgid="+deptID;
			//strWhere = ",td_comm_email_disposedep e ";
		}
		//equest.getInputStream();
		try {
			
			String sql = "select a.*,b.user_name,d.name as chname,d.CHANNEL_ID,e.DISPOSEDEP from td_cms_vote_title a,td_sm_user b,TD_CMS_CHANNEL_VOTE c,TD_CMS_CHANNEL d ,td_comm_email_disposedep e "+
					" where a.founder_id=b.user_id and a.id=c.VOTE_TITLE_ID(+) and c.CHANNEL_ID=d.CHANNEL_ID(+) and e.id=a.DEPART_ID  and  a.SITEID="+siteID+where+" order by istop desc,ctime desc ";
			
			
			db.executeSelect(sql,(int)offset,maxPagesize);	
			
			SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < db.size(); i++) {

				Title  title = new Title();

				title.setId(db.getInt(i, "id"));
				title.setName(db.getString(i, "name"));
				//QuestionItem.setStrStartime((db.getDate(i, "starttime")).toString());
				//QuestionItem.setStrEndtime((db.getDate(i, "endtime")).toString());
				title.setSiteid(db.getInt(i, "siteid"));
				if(db.getInt(i, "ip_repeat")==0)
				{
					title.setState("否");
				}
				else
				{
					title.setState("是");
				}
				title.setIpRepeat(db.getInt(i, "ip_repeat"));
				title.setActive(db.getInt(i, "active"));
				title.setChannelID(String.valueOf(db.getInt(i, "CHANNEL_ID")));
				title.setChannelName(String.valueOf(db.getString(i, "chname")));
				title.setFoundDate(bartDateFormat.format(db.getDate(i, "ctime")));
				title.setFoundername(db.getString(i,"user_name"));
				title.setIsTop(db.getInt(i,"istop"));
				title.setIslook(db.getInt(i,"islook"));
				title.setDepart_name(db.getString(i,"DISPOSEDEP"));
				res.add(title);
				
				

			}

			//System.out.println(res);
			listInfo.setDatas(res);
			listInfo.setTotalSize(db.getTotalSize());
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		//System.out.println(listInfo);
		return listInfo;
	}
	
	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}


}
