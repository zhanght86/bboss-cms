package com.frameworkset.platform.cms.channelmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.event.Event;
import org.frameworkset.event.Listener;

import com.frameworkset.platform.cms.event.CMSEventType;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
/**
 * 加载频道做缓冲管理
 * @author zhuo.wang
 *
 */
public class ChannelCacheManager implements Listener {

	private Site site;
	
	/** 频道缓冲器 */
	private Map channelMap;
	
	/** 
	 * 频道缓冲器 
	 * 按频道显示名称建立的索引
	 * */
	private Map chlOfDisplayNameMap;
	
	/** 
	 * 频道缓冲器 
	 * 按频道显示名称建立的索引（只包含所有导航频道）
	 * */
	private Map allNavigatorChlMap;
	
	Channel channelroot;
	
	//private boolean inited = false;
	
	/**
	 * 构造方法
	 */
	public ChannelCacheManager(Site site)
	{
		this.site = site;
	}
	
	/**
	 *重新加载
	 */
	public void reset()
	{
		synchronized(this)
		{
			channelroot = null;
			channelroot = new Channel();
			
			channelroot.setChannelId(Long.parseLong("0"));
			channelMap = null;
			chlOfDisplayNameMap = null;
			allNavigatorChlMap = null;
			loadChannel(channelroot);
			
		}
	}
	
	/**
	 * 初始化站点id为siteid为频道信息
	 *
	 */
	public void init()
	{
		channelroot = null;
		channelroot = new Channel();
		
		channelroot.setChannelId(Long.parseLong("0"));
		channelroot.setDisplayName("0");
		channelMap = null;
		chlOfDisplayNameMap = null;
		allNavigatorChlMap = null;
		loadChannel(channelroot);
		
	}
	
	private void loadChannel(Channel channelroot)
	{
		if(channelMap == null)
		{
			channelMap = new HashMap();
		}
		if(chlOfDisplayNameMap == null)
		{
			chlOfDisplayNameMap = new HashMap();
		}
		if(allNavigatorChlMap == null)
		{
			allNavigatorChlMap = new HashMap();
		}
		String channelId = String.valueOf(channelroot.getChannelId());
		String chlDisplayname = String.valueOf(channelroot.getDisplayName());
		String siteId = this.site.getSiteId() + "";//String.valueOf(channelroot.getSiteId());
		//System.out.println(siteId+"----siteid--------1");
		this.channelMap.put(channelId,channelroot);
		this.chlOfDisplayNameMap.put(chlDisplayname,channelroot);
		if(channelroot.isNavigator() || "0".equals(chlDisplayname))
			this.allNavigatorChlMap.put(chlDisplayname,channelroot);
		//System.out.println("------" + channelId + ":" + this.channelMap.get(channelId));
		//System.out.println("------" + chlDisplayname + ":" + this.chlOfDisplayNameMap.get(chlDisplayname));
		//System.out.println("------" + chlDisplayname + ":" + this.allNavigatorChlMap.get(chlDisplayname));
		try {
			ChannelManager cm = new ChannelManagerImpl();
			SiteManager sm = new SiteManagerImpl();
			List subChannelList ;
			if(channelId.equals("0")){
				 subChannelList = sm.getDirectChannelsOfSite(siteId);
				 //System.out.println(subChannelList.size()+"------------1");
				 //subChannelList = cm.getAllSubChannels(channelId);
			}
			else
			{
				 subChannelList = cm.getDirectSubChannels(channelId);
				 //System.out.println(subChannelList.size()+"-----------2");
			}
				
			for(int i =0; subChannelList !=null && i<subChannelList.size();i++){
				Channel channel = (Channel)subChannelList.get(i);
				channelroot.addSubChnl(channel);
				//this.channelMap.put(String.valueOf(channel.getChannelId()),channel);
				loadChannel(channel);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	private ChannelCacheManager()
	{
		
	}
	
	/**
	 * 判断频道id为channelId的频道是否有子频道
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	public boolean hasSubChannel(String channelId) throws Exception {
		 Channel channel = getChannel(channelId);
         return channel.getSubchnls()!= null && channel.getSubchnls().size() > 0;
		
	}
	
	/**
	 * 从缓冲区内获取频道id为channelId的频道的信息
	 * @param channelId
	 * @return Channel
	 * @throws Exception
	 */
	public Channel getChannel(String channelId) throws Exception {
		//System.out.println("........."+channelId);
		Channel channel = (Channel)this.channelMap.get(channelId);
		
        return channel;
      
	}
	
	/**
	 * 从缓冲区内获取频道显示名称为channelDisplayName的导航频道的信息
	 * @param channelDisplayName
	 * @return Channel
	 * @throws Exception
	 */
	public Channel getNavigatorChlByDisplayName(String channelDisplayName) throws Exception {
		Channel channel = (Channel)this.allNavigatorChlMap.get(channelDisplayName);
		
		return channel;
	}
	
	/**
	 * 从缓冲区内获取频道显示名称为channelDisplayName的频道的信息
	 * @param channelDisplayName
	 * @return Channel
	 * @throws Exception
	 */
	public Channel getChannelByDisplayName(String channelDisplayName) throws Exception {
		Channel channel = (Channel)this.chlOfDisplayNameMap.get(channelDisplayName);
		
		return channel;
	}
	
	/**
	 * 从缓冲区内获取频道id为channelId的频道的所有子频道的信息的列表
	 * @param channelId
	 * @return List<Channel>
	 * @throws Exception
	 */
	public List getSubChannels(String channelId) throws Exception {
		
		Channel channel = (Channel)this.channelMap.get(channelId);
		
		return channel.getSubchnls();//cm.getDirectSubChannels(channelId);
	}
	
	/**
	 * 从缓冲区内获取频道频道显示名称(DisplayName)为displayName的频道的所有子频道的信息的列表
	 * @param channelId
	 * @return List<Channel>
	 * @throws Exception
	 */
	public List getSubChannelsByDisplayName(String displayName) throws Exception {
		
		Channel channel = (Channel)this.chlOfDisplayNameMap.get(displayName);
		
		return channel.getSubchnls();
	}
	
	/**
	 * 从缓冲区内获取频道频道显示名称(DisplayName)为displayName的导航频道的所有子导航频道的信息的列表
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	public List getSubNavigatorChlsByDisplayName(String displayName) throws Exception {
		List list = new ArrayList();
        Channel channel = (Channel)this.allNavigatorChlMap.get(displayName);
        if(channel == null)
        	return list;
        List sonlist = channel.getSubchnls();
        Channel sonchannel;
        for(int i=0;(channel != null && sonlist != null && i < sonlist.size());i++)
        {
        	sonchannel = (Channel)sonlist.get(i);
        	if(sonchannel.isNavigator())
        		list.add(sonchannel);
        }
        
		return list;
	}

	public void handle(Event e) {
		Channel channel = null;
		try
		{
			channel = (Channel)e.getSource();
		}
		catch(Exception e1)
		{
			//System.out.println("error:" + (Object)e.getSource() + "/");
			return;
		}
		if(channel != null && !(channel.getSiteId() + "").equals(this.site.getSiteId() + ""))
			return;
		if(e.getType() .equals( CMSEventType.EVENT_CHANNEL_ADD) )
	    {		
			synchronized(this)
			{
				if(this.channelMap.get(channel.getChannelId() + "") != null)
					return;
				String parentid = channel.getParentChannelId() + "";
				Channel parent = (Channel)this.channelMap.get(parentid);
				parent.addSubChnl(channel);
				channelMap.put(channel.getChannelId() + "",channel);
				chlOfDisplayNameMap.put(channel.getDisplayName(),channel);//
				if(channel.isNavigator())
					this.allNavigatorChlMap.put(channel.getDisplayName(),channel);
				Collections.sort(parent.getSubchnls());
			}
		}
		if(e.getType() .equals(  CMSEventType.EVENT_CHANNEL_DELETE) )
	    {		
			synchronized(this)
			{
				String parentid = channel.getParentChannelId() + "";
				Channel parent = (Channel)this.channelMap.get(parentid);
				List list = parent.getSubchnls();
				list.remove(channelMap.get(channel.getChannelId() + ""));
				ChannelManager cm = new ChannelManagerImpl();
				List subChannelList = null;
				this.channelMap.remove(channel.getChannelId() + "");
				this.chlOfDisplayNameMap.remove(channel.getDisplayName());//
				if(channel.isNavigator())
					this.allNavigatorChlMap.remove(channel.getDisplayName());
				try {
					subChannelList = cm.getAllSubChannels(channel.getChannelId() + "");
				} catch (ChannelManagerException e1) {
					e1.printStackTrace();
				}
				for(int i =0; subChannelList !=null && i<subChannelList.size();i++){
					Channel chl = (Channel)subChannelList.get(i);
					this.channelMap.remove(chl.getChannelId() + "");
					this.chlOfDisplayNameMap.remove(chl.getDisplayName());//
					if(chl.isNavigator())
						this.allNavigatorChlMap.remove(chl.getDisplayName());
				}
				//System.out.println(channelMap.get(channel.getChannelId() + ""));
			}
		}
		if(e.getType() .equals(  CMSEventType.EVENT_CHANNEL_UPDATE) )
	    {		
			synchronized(this)
			{
				Channel oChannel = (Channel)this.channelMap.get(channel.getChannelId() + "");
				ChannelManager cm = new ChannelManagerImpl();
				Channel newChannel = null;
				try {
					newChannel = cm.getChannelInfo(channel.getChannelId() + "");
				} catch (ChannelManagerException e1) {
					e1.printStackTrace();
				}
				
				this.chlOfDisplayNameMap.remove(oChannel.getDisplayName());
				this.allNavigatorChlMap.remove(oChannel.getDisplayName());
				
				newChannel.setSubchnls(oChannel.getSubchnls());
				this.chlOfDisplayNameMap.put(newChannel.getDisplayName(),newChannel);
				this.channelMap.put(newChannel.getChannelId() + "",newChannel);
				
				if(newChannel.isNavigator())//更新频道为导航频道
				{
					this.allNavigatorChlMap.put(newChannel.getDisplayName(),newChannel);
				}
				
				String parentid = channel.getParentChannelId() + "";
				Channel parent = (Channel)channelMap.get(parentid);
				replaceSubChannel(parent, newChannel);
				if(oChannel.getOrderNo() != newChannel.getOrderNo())
				{
					Collections.sort(parent.getSubchnls());
				}
			}
		}
	}
	
	private void replaceSubChannel(Channel parent, Channel subChannel)
	{
		List subChannels = parent.getSubchnls();
		for(int i = 0; subChannels != null && i < subChannels.size(); i ++)
		{
			Channel temp_ = (Channel)subChannels.get(i);
			if(temp_.getChannelId() == subChannel.getChannelId())
			{
				subChannels.remove(i);
				subChannels.add(i,subChannel);
				break;
			}
			
		}
	}
}
