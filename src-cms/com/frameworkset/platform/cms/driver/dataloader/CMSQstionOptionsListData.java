package com.frameworkset.platform.cms.driver.dataloader;

import com.frameworkset.platform.cms.votemanager.VoteManager;
import com.frameworkset.platform.cms.votemanager.VoteManagerException;
import com.frameworkset.platform.cms.votemanager.VoteManagerImpl;
import com.frameworkset.util.ListInfo;

public class CMSQstionOptionsListData extends CMSBaseListData implements java.io.Serializable {

	protected ListInfo getDataList(String arg0, boolean arg1, long arg2,
			int arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		try { 
			if(this.channel != null && !this.channel.equals(""))
			{
				ListInfo listInfo = new ListInfo();
				VoteManager voteMgr  = new VoteManagerImpl();
				listInfo.setDatas(voteMgr.getItemsOfQstion(Integer.parseInt(channel)));
				return listInfo;
			}
			else
			{
				return new ListInfo();
			}
			
		} catch (VoteManagerException e) {
			
			e.printStackTrace();
		}
		
		return new ListInfo();
	}

}
