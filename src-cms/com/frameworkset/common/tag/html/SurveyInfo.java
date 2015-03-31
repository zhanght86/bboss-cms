package com.frameworkset.common.tag.html;

import java.util.List;

import javax.servlet.jsp.JspException;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.votemanager.TimeCtrl;
import com.frameworkset.platform.cms.votemanager.Title;
import com.frameworkset.platform.cms.votemanager.VoteManager;
import com.frameworkset.platform.cms.votemanager.VoteManagerImpl;
import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;

public class SurveyInfo extends CMSBaseTag {

	String type = "";

	public int doStartTag() throws JspException {
		super.doStartTag();
		
		VoteFormTag form =(VoteFormTag)findAncestorWithClass(this,VoteFormTag.class);
		String id = form.getId();
		String channel = form.getChannel();
		String type = getType();
		VoteManager voteMgr  = new VoteManagerImpl();
		
		try{
			
			if ("".equals(id)){

				if (context==null)
					return this.EVAL_BODY_AGAIN;
				
				if (!"".equals(channel)){
					ChannelManagerImpl chnlMgr = new ChannelManagerImpl();
					Channel chnl = chnlMgr.getChannelInfoByDisplayName(context.getSiteID(),channel);
					id = String.valueOf(voteMgr.getLatestActiveSurveyIDInChnl(String.valueOf(chnl.getChannelId())));
				}else if (CMSTagUtil.getCurrentChannel(context)!=null){
					Channel chnl = CMSTagUtil.getCurrentChannel(context);
					id = String.valueOf(voteMgr.getLatestActiveSurveyIDInChnl(String.valueOf(chnl.getChannelId())));
				}else
					id = String.valueOf(voteMgr.getTopSurveyID(Integer.parseInt(site)));
			}
			
			Title title = voteMgr.getSurveyBy(Integer.parseInt(id));
			
			if (type.equals("content"))
				out.print(title.getContent());
			if(type.equals("name"))
				out.print(title.getName());
			if(type.equals("ctime"))
				out.print(title.getFoundDate());
			if(type.equals("picpath"))
			{
				out.print(title.getPicpath());
			}
			List timeCtrls = title.getTimeCtrls();
			TimeCtrl time = null;
			if (timeCtrls!=null&&timeCtrls.size()>0){
				time = (TimeCtrl)timeCtrls.get(0);
			}
			if(type.equals("stime"))
			{
				out.print(time.getTimeStart());
			}
			if(type.equals("etime"))
			{
				out.print(time.getTimeEnd());
			}
		 } catch (Exception e) {
				e.printStackTrace();
		}
		 
		return this.EVAL_BODY_AGAIN;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	@Override
	public void doFinally() {
		type = "";
		super.doFinally();
	}
}
