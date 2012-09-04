package com.frameworkset.common.tag.html;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.common.tag.CMSTagUtil;
/**
 * 子频道信息标签基础上改造 父频道信息标签 可以指定跨级(level)父类
 * <p>Title: ParentChannelInfoTag.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date Jun 14, 2005 10:15:31 PM
 * @author ge.tao
 * @version 1.0
 */
public class ParentChannelInfoTag extends ChannelInfoTag {
	
	

	
	private int level = 1;

	public ParentChannelInfoTag() {
		super();
	}


	
//	protected Channel getParentChannelWithLevel(Channel current,int level)
//	{
//		try {
//			if(level == 0 || current == null)
//				return current;
//			
//			Channel current_ = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannel(current.getParentChannelId() + "");
//			if(current_ == null)
//				return current;
//			return getParentChannelWithLevel(current_,level - 1);
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return current;
//	}
	
	protected Channel getChannelInfo()
	{
		Channel chnl = null;
		try{
			if (displayname != null && !"".equals(displayname)){
				chnl = CMSUtil.getChannelCacheManager(context.getSiteID())
						.getChannelByDisplayName(displayname);
			    /*获取父频道*/
			    chnl = CMSUtil.getParentChannelWithLevel(context.getSiteID(),chnl,this.level);
			}else {
				CMSListTag parent = this.findCMSListTag();
				if (parent == null) {
					chnl = CMSTagUtil.getCurrentChannel(context);
				} else {
					chnl = CMSUtil.getChannelCacheManager(context.getSiteID())
							.getChannelByDisplayName(parent.getChannel());
				}
				/*获取父频道*/
				
				chnl = CMSUtil.getParentChannelWithLevel(context.getSiteID(),chnl,this.level);
			}
		}
		catch(Exception e )
		{
			
		}
		return chnl;

	}
	

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	
}
