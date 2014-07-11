package com.frameworkset.platform.sysmgrcore.purviewmanager;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.platform.config.ConfigManager;


/**
 * 对登录系统用户IP进行控制
 * @author houtt2
 * 2014.03.12
 *
 */
public class IpControlUtil {
	private static Logger log = Logger.getLogger(IpControlUtil.class);
	
	 /**
     * 登录用户IP控制判断
     * @param controluser 用户域账号或工号
     * @param userip 登录用户ip
     * @return true 验证通过 false 验证失败
     */
    public static boolean validateIp(String controluser,String userip){
		boolean flag = false;
		boolean enableipcontrol = ConfigManager.getInstance().getConfigBooleanValue("enableipcontrol", false);
		if(!enableipcontrol)//判断是否开启ip访问控制策略
			return true;
		StringBuffer sql =  new StringBuffer()
		.append("select t.IP,t.CONTROLUSER,t.FILTERTYPE from TD_SM_IPCONTROL t where CONTROLUSER=?");
		try {
			PreparedDBUtil db = new PreparedDBUtil();
			db.preparedSelect(sql.toString());
			db.setString(1, controluser);
			db.executePrepared();
			if(db.size()>0){//说明对该用户进行了IP控制的白名单或者黑名单
				boolean white_f = true,black_f = false;//是否在黑白名单里面
				for(int i=0;i<db.size();i++){
					String control_ip = db.getString(i, "IP");
					int filter_type = db.getInt(i, "FILTERTYPE");
					boolean re  =compareIp(userip, control_ip);
					if((filter_type ==0)){//白名单限制
						white_f = re;
					}else if(filter_type == 1){
						black_f = re ;
					}
				}
				/**
				 * 验证通过：
				 * 1、有白名单和黑名单，在白名单中，不在黑名单中
				 * 2、只有白名单 ，并且在白名单中
				 * 3、只有黑名单，并且不在黑名单中
				 */
				if((white_f && !black_f)){ 
					flag = true;
				}else{ 
					flag  = false;
				}
			}else{
				StringBuffer sql_1 = new StringBuffer()
				.append("select t.IP,t.CONTROLUSER,t.FILTERTYPE from TD_SM_IPCONTROL t where CONTROLUSER='' or CONTROLUSER is null");
				PreparedDBUtil db_1 = new PreparedDBUtil();
				db_1.preparedSelect(sql_1.toString());
				db_1.executePrepared();
				if(db_1.size()>0){ // 说明对系统所有用户进行了IP控制的白名单或者黑名单
					boolean white_f = true,black_f = false;//是否在黑白名单里面
					for(int i=0;i<db_1.size();i++){
						String control_ip = db_1.getString(i, "IP");
						int filter_type = db_1.getInt(i, "FILTERTYPE");
						boolean re  = compareIp(userip, control_ip);
						if((filter_type==0)){//白名单限制
							white_f = re;
						}else if(filter_type == 1){
							black_f = re ;
						}
					}
					/**
					 * 验证通过：
					 * 1、有白名单和黑名单，在白名单中，不在黑名单中
					 * 2、只有白名单 ，并且在白名单中
					 * 3、只有黑名单，并且不在黑名单中
					 */
					if((white_f && !black_f)){ //
						flag = true;
					}else{ 
						flag  = false;
					}
				}else{ //说明未设置用户级别、系统级别的ip控制
					flag = true;	
				}
				
			}
		} catch (Exception e) {
			log.debug("", e);
			return true;
		}
		return flag;
	}
    
	
     /**
      * 判断给定的IP是否属于控制的IP范围内
      * @param userip 用户ip
      * @param control_ip 限制的ip范围  可以有多种形式 如：192.168.*.*,192.168.142.1,192.168.142.1-192.168.142.254（以英文半角逗号分隔）
      * @return
      */
	 public static boolean compareIp(String userip,String control_ip){
	    	boolean flag = false;
	    	String[] ips = control_ip.split(",");
	    	for(int i=0;i<ips.length;i++){
	    		String ip = ips[i];
	    		String startIp="";
	    		String endIp="";
	    		if(ip.indexOf("*")>=0){
	    			StringBuffer temp_ip = new StringBuffer();
	    			String ip_ = ip;
	    			temp_ip.append(ip.replaceAll("\\*", "0"))
	    			       .append("-")
	    			       .append(ip_.replaceAll("\\*", "254"));
	    			ip = temp_ip.toString();
    			}
	    		if(ip.indexOf("-")>=0){
	    			startIp = ip.substring(0, ip.indexOf("-"));
	    			endIp = ip.substring(ip.indexOf("-")+1,ip.length());
	    		}else{
	    			startIp = ip;
	    			endIp = ip;
	    			
	    		}
	    		flag = betweenIP(startIp,endIp,userip);
	    		if(flag){
	    			return flag;
	    		}
	    	}
	    	return flag;
	    }
	 
	 /**
	  * 比较ip是否在给定的范围内
	  * @param start 开始ip
	  * @param end 结束ip
	  * @param current 给定的ip
	  * @return
	  */
	 public static boolean betweenIP(String start,String end,String current){
	     boolean result=false;

	     start = start.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
	     start = start.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
	     start = start.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
	     start = start.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");

	     end = end.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
	     end = end.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
	     end = end.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
	     end = end.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");

	     current = current.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
	     current = current.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
	     current = current.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
	     current = current.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
	     if((current.compareTo(start)>=0) && (current.compareTo(end)<=0)){
	       result=true;
	     }
	     return result;
	   }
}
