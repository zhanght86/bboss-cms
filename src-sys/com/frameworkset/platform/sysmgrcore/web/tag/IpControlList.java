package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.sysmgrcore.entity.IpControl;
import com.frameworkset.util.ListInfo;
/**
 * Ip限制查询list
 * @author houtt2
 * 2014.03.13
 *
 */

public class IpControlList  extends DataInfoImpl implements Serializable{
	private static final long serialVersionUID = 3685038777331165348L;
	private static final Logger log = Logger.getLogger(IpControlList.class);
	@Override
	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}
    /**
     * 查询IP限制列表
     */
	@Override
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize) {
		  ListInfo listInfo = new ListInfo();
		  String controluser = request.getParameter("controluser");	//查询条件：用户登录名	
		  String filtertype = request.getParameter("filtertype");
		  StringBuffer sql = new StringBuffer()
		  .append("select t.ID,t.IP,t.CONTROLUSER,t.FILTERTYPE,t.IPDESC from TD_SM_IPCONTROL t where 1=1");
		 
		  if(controluser!=null && !controluser.equals("")){
			  
			  sql.append(" and t.CONTROLUSER like ?");
		  }
		  if(filtertype!=null && !filtertype.equals("")){
			  
			  sql.append(" and t.FILTERTYPE=?");
//			     .append(filtertype);
		  }
		  sql.append(" order by t.CONTROLUSER");
		  PreparedDBUtil db = new PreparedDBUtil();		  
		  try {
			db.preparedSelect(sql.toString(),offset,maxPagesize);
			int j = 0;
			 if(controluser!=null && !controluser.equals("")){
				 j ++;
				 db.setString(j, "%"+controluser+"%");
			 }
			 if(filtertype!=null && !filtertype.equals("")){
				  j ++;  				 
				  db.setInt(j, Integer.parseInt(filtertype));
			  }
			
			db.executePrepared();
//			db.executeSelect(sql.toString(),offset,maxPagesize);
			List datas = new ArrayList();
			for(int i=0;i<db.size();i++){
				IpControl ip = new IpControl();
				ip.setId(db.getString(i, "ID"));
				ip.setIp(db.getString(i, "IP"));
				ip.setControluser(db.getString(i, "CONTROLUSER"));
				ip.setFiltertype(db.getInt(i, "FILTERTYPE"));
				ip.setIpdesc(db.getString(i, "IPDESC"));
				datas.add(ip);
			}
			listInfo.setDatas(datas);
    		listInfo.setTotalSize(db.getTotalSize());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listInfo;
	}
	/**
	 * 新增IP控制
	 * @param bean
	 * @param request
	 * @return
	 */
	public boolean addIpControl(IpControl bean,HttpServletRequest request){
		boolean flag = false;
		DBUtil db = new DBUtil();	
		StringBuffer sql = new StringBuffer()
		                  .append("insert into TD_SM_IPCONTROL(ID,IP,CONTROLUSER,FILTERTYPE,IPDESC)values('")
		                  .append(bean.getId()).append("','")
		                  .append(bean.getIp()).append("','")
		                  .append(bean.getControluser()).append("',")
		                  .append(bean.getFiltertype()).append(",'")
		                  .append(bean.getIpdesc()).append("')");
		 try {
			db.executeInsert(sql.toString());
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 根据id查找对象
	 * @param id
	 * @return
	 */
	public IpControl getIpControlById(String id){
		IpControl bean = new IpControl();
		StringBuffer sql = new StringBuffer()
		                  .append("select t.ID,t.IP,t.CONTROLUSER,t.FILTERTYPE,t.IPDESC from TD_SM_IPCONTROL t where t.ID=")
		                  .append("'").append(id).append("'");
		DBUtil db = new DBUtil();	
		try {
			db.executeSelect(sql.toString());
			if(db.size()!=0){
				bean.setId(db.getString(0, "ID"));
				bean.setIp(db.getString(0, "IP"));
				bean.setControluser(db.getString(0, "CONTROLUSER"));
				bean.setFiltertype(db.getInt(0, "FILTERTYPE"));
				bean.setIpdesc(db.getString(0, "IPDESC"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	/**
	 * 更新
	 * @param bean
	 * @return
	 */
	public boolean updateIpControl(IpControl bean){
		boolean flag = false;
		if(bean!=null){
			StringBuffer sql = new StringBuffer()
            .append("update TD_SM_IPCONTROL t set t.IP='")
            .append(bean.getIp()).append("',t.CONTROLUSER='")
            .append(bean.getControluser()).append("',t.FILTERTYPE=")
            .append(bean.getFiltertype()).append(",t.IPDESC='")
            .append(bean.getIpdesc()).append("' where t.ID='")
            .append(bean.getId()).append("'");
			DBUtil db = new DBUtil();
			try {
				db.executeUpdate(sql.toString());
				flag = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return flag;
	}
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public boolean delteIpControl(String ids){
		boolean flag = false;
		StringBuffer sql = new StringBuffer()
		                  .append("delete TD_SM_IPCONTROL where id in(");
		DBUtil db = new DBUtil();
		if(ids!=null && !ids.equals("")){
			String[] idList = ids.split(",");
			for(int i=0;i<idList.length;i++){
				sql.append("'").append(idList[i]).append("',");
			}
			try {
				db.executeDelete(sql.substring(0, sql.length()-1)+")");
				flag = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		return flag;
	}
	
}
