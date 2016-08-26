package com.frameworkset.platform.cms.templatemanager;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.frameworkset.platform.cms.docCommentManager.DocCommentManagerException;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.util.ListInfo;

/**
 * 
 * 管理模板风格表相关操作
 * @author peng.yang
 *
 */
public class TemplateStyleManagerImpl implements TemplateStyleManager{
	
	/**
	 * 通过styleId返回模板风格名称
	 * @param styleId 模板风格表ID
	 * @return
	 */
	public String getTemplateStyleName(String styleId){
		String strsql = " select * from td_cms_template_style where STYLE_ID = " + styleId;
		String styleName = "";
		DBUtil db = new DBUtil();
		
		try{
			db.executeSelect(strsql);
			if(db.size()==0){
				return null;
			}
			styleName = db.getString(0,"STYLE_NAME");
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return styleName;
	}

	
	
	/**
	 * 查询所有模板风格
	 * @return
	 */
	public List getAllTemplateStyleIdAndName() {
		
		String strsql = "select * from td_cms_template_style order by style_order ";
		DBUtil db = new DBUtil();
		List tsList = new ArrayList();
		try {
			db.executeSelect(strsql);
			for(int i=0;i<db.size();i++){
				TemplateStyleInfo tsi = new TemplateStyleInfo();
				tsi.setStyleId(new Integer(db.getInt(i,"STYLE_ID")));
				tsi.setStyleName(db.getString(i,"STYLE_NAME"));
				tsi.setStyleDesc(db.getString(i,"STYLE_DESC"));
				tsList.add(tsi);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tsList;
	}



	/**
	 * 获取模板风格信息列表
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @return
	 */
	public ListInfo getTemplateStyleList(String sql, int offset, int maxItem){
		DBUtil db = new DBUtil();
		ListInfo listInfo = new ListInfo();
		List tempList = new ArrayList();
		try{	
			db.executeSelect(sql,offset,maxItem);
			
			for(int i=0;i<db.size();i++){
				TemplateStyleInfo tsi = new TemplateStyleInfo();
				tsi.setStyleId(new Integer(db.getInt(i,"STYLE_ID")));
				tsi.setStyleName(db.getString(i,"STYLE_NAME"));
				tsi.setStyleDesc(db.getString(i,"STYLE_DESC"));
				tsi.setStyleOrder(db.getInt(i,"STYLE_ORDER"));
				
				tempList.add(tsi);
			}
			listInfo.setDatas(tempList);
			listInfo.setTotalSize(db.getTotalSize());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listInfo;
	}
	
	
	/**
	 * 新增一条模板风格信息
	 * @param tsi
	 */
	public boolean addTemplateStyle(TemplateStyleInfo tsi){
		DBUtil db = new DBUtil();
		
		//是否出现重复的模板名称的添加
		List tsList = getAllTemplateStyleIdAndName();
		for(int i=0;null != tsList && i<tsList.size();i++){
			TemplateStyleInfo temptsi = (TemplateStyleInfo)tsList.get(i);
			if(temptsi.getStyleName().trim().equals(tsi.getStyleName().trim())){
				return false;
			}
		}
		
		int order = getMaxTemplateStyleOrder()+1;
		
		
		try {
			long sytleId = db.getNextPrimaryKey("td_cms_template_style") ;
			
			String sql = "insert into td_cms_template_style(style_id,style_name,style_desc,style_order) values("
						 + sytleId +",'"
						 +tsi.getStyleName()+"', '"+tsi.getStyleDesc()+"',"+order+")";
			db.executeInsert(sql);
			 
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true ;
	}
	
	/**
	 * 更新一条模板风格信息
	 * @param styleId
	 * @param tsi
	 * @return
	 */
	public boolean updateTemplateStyle(String styleId, TemplateStyleInfo tsi){
		String sql = " update td_cms_template_style set style_name = '" + tsi.getStyleName() +
					"', style_desc = '" + tsi.getStyleDesc() +"' where style_id = " +styleId;
		DBUtil db = new DBUtil();
		
		try {
			db.executeUpdate(sql);
		} catch (SQLException se) {
			se.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 单个删除模板信息
	 * @param styleId
	 * @return
	 */
	public boolean delTemplateStyle(String styleId) {
		DBUtil db = new DBUtil();
		String sql = " delete td_cms_template_style where style_id = "+styleId;
		
		try {
			db.executeDelete(sql);
		} catch (SQLException se) {
			se.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 批量删除模板风格信息
	 * @param styleIds
	 * @return
	 */
	public boolean delTemplateStyles(String[] styleIds) {
		DBUtil db = new DBUtil();
		
		for(int i=0;null != styleIds && i<styleIds.length;i++){
			String id = styleIds[i].toString();
			String sql = " delete td_cms_template_style where style_id = "+id;
			try {
				db.executeDelete(sql);
			} catch (SQLException se) {
				se.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 查找最大的排序号
	 * @return
	 */
	public int getMaxTemplateStyleOrder() {
		String sql = " select max(style_order) as max_order from td_cms_template_style ";
		int maxOrder = 0;
		DBUtil db = new DBUtil();
		
		try {
			db.executeSelect(sql);
			maxOrder = db.getInt(0,"max_order") ;
		} catch (SQLException e){
			e.printStackTrace();
			return -1 ;
		}
		
		return maxOrder ;
	}
	
	/**
	 * 保存排序
	 * @param styleIds
	 * @param strOrderNums
	 */
	public boolean updateTemplateStyleOrder(String styleIds,String strOrderNums){
		String[] ids = styleIds.split(":");
		String[] nums = strOrderNums.split(":");
		DBUtil db = new DBUtil();
		
		if(ids.length!=nums.length)
			return false ;
		
		try{
			for(int i=0;i<ids.length;i++){
				String sql = " update td_cms_template_style set style_order = "+nums[i]
				             +" where style_id = "+ids[i];
				db.executeUpdate(sql);
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
