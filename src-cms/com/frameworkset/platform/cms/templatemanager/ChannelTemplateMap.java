//Source file: D:\\workspace\\cms\\src\\com\\frameworkset\\platform\\cms\\channelmanager\\ChannelTemplateMap.java

package com.frameworkset.platform.cms.templatemanager;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;



public class ChannelTemplateMap implements java.io.Serializable 
{
    
    /**
     * @since 2006.12
     */
    public ChannelTemplateMap() 
    {
     
    }
    /**
     * 功能:创建频道模板映射信息
     * 输入:模板ID和频道ID
     * 输出:true:成功;false:失败    
     * @param template
     * @roseuid 45864967030D
     */
    public boolean createChannelTemplateMap(int templateid,int channelid) throws TemplateManagerException
    {
 	   //新的链接池
 	   PreparedDBUtil preDBUtil=new PreparedDBUtil();    
 	   boolean ret=false;     //返回值
       String sql="";
      
      try {          	 	
     	 	 sql="insert into td_cms_channeltemplatemap("
     	 		+"template_id,channel_id)"      	 		
     	 		+"values(?,?)";     	 		   	 
     	 		preDBUtil.preparedInsert(sql);                  	 		
     	 		preDBUtil.setInt    (1,templateid);         	   
     	 		preDBUtil.setInt    (2,channelid); 
     	 		preDBUtil.executePrepared();  	 
     	 		ret=true;   	 	
     	 	
      } catch (Exception e) {
 			   ret=false;  
 			   System.out.print("新建模板频道映射出错!"+e);
 			  throw new TemplateManagerException(e.getMessage());
 	}  		     
      return ret;       	
    }
    
    /**
     * 功能:删除频道模板映射信息
     * 输入:模板ID和频道ID
     * 输出:true:成功;false:失败    
     * @param 
     * @roseuid 45864967030D
     */  
    public boolean deleteChannelTempateMap(int templateid,int channelid) throws TemplateManagerException
    {
    	boolean ret=false;
    	String sql="";
    	DBUtil conn=new DBUtil();  
 	   try {
 		    //清除站点模板映射信息
 		    sql="delete from td_cms_channeltemplatemap where channel_id="+channelid+" and template_id="+templateid+" ";
 		    conn.executeDelete(sql);
 		    ret=true;
 		} catch (Exception e) {
 			ret=false;
 			System.out.print("删除频道模板映射信息出错!"+e);
 			throw new TemplateManagerException(e.getMessage());
 		}  
 	   
    	return ret;
    }
}
