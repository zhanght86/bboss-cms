package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.manager.db.ResManagerImpl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
public class ResSubList  extends DataInfoImpl  implements Serializable{


		    protected ListInfo getDataList(String sortKey, boolean desc, long offset,
		            int maxPagesize) {//取资源
		        ListInfo listInfo = new ListInfo();
		        String restypeId = StringUtil.replaceNull(request.getParameter("restypeId"));
		        String title = StringUtil.replaceNull(request.getParameter("title"));


		        boolean first = true;
		        try {
//		            ResManager resManager = SecurityDatabase.getResourceManager();
//		            List list = null;
//		            PageConfig pageConfig = resManager.getPageConfig();
//		            pageConfig.setPageSize(maxPagesize);
//		            pageConfig.setStartIndex((int) offset);

		            if (restypeId.equals("") && title.equals("")) {

		            	listInfo = getResList("select * from td_sm_Res r" + " order by r.restype_Id desc",offset,maxPagesize);
//		                String restypeID = null;
//		    			ResourceManager rm = new ResourceManager();
//		    			for(int i = 0; i < list.size();i ++)
//		    			{
//		    			    Res res = (Res)list.get(i);
//		    			    restypeID = res.getRestypeId();
//		    			    //System.out.println("restypeID:"+restypeID);
//		    			    try
//		    			    {
//			    			    if(rm.getResourceInfoByType(restypeID) != null)
//			    			    {
//				    			    //System.out.println("restypeName:"+rm.getResourceInfoByType(restypeID).getName());
//				    			    res.setRestypeName(rm.getResourceInfoByType(restypeID).getName());
//			    			    }
//		    			    }
//		    			    catch(Exception e)
//		    			    {
//		    			    	res.setRestypeName("未知");
//		    			    }
//
//
//		    			}
//
//		                listInfo.setTotalSize(pageConfig.getTotalSize());
//		                listInfo.setDatas(list);
		            } else {
		                String sql = "from Res r where ";
		                if (!restypeId.equals("")) {
		                    if (first) {
		                        sql += " r.restypeId like '%" + restypeId + "%'";
		                        first = false;
		                    } else {
		                        sql += " and r.restypeId like '%" + restypeId + "%'";
		                    }
		                }
		                if (!title.equals("")) {
		                    if (first) {
		                        sql += " r.title like '%" + title + "%'";
		                        first = false;
		                    } else {
		                        sql += " and r.title like '%" + title + "%'";
		                    }
		                }
		                if (!title.equals("")) {
		                    if (first) {
		                        sql += " r.title like '%" + title + "%'";
		                        first = false;
		                    } else {
		                        sql += " and r.title like '%" + title + "%'";
		                    }
		                }
		                listInfo = getResList(sql.toString()+ " order by r.restypeId desc",offset,maxPagesize);
//		                String restypeID = null;
//		    			ResourceManager rm = new ResourceManager();
//		    			for(int i = 0; i < list.size();i ++)
//		    			{
//		    			    Res res = (Res)list.get(i);
//		    			    restypeID = res.getRestypeId();
//		    			    
//		    			    res.setRestypeName(rm.getResourceInfoByType(restypeID).getName());
//
//
//		    			}
//
//		                listInfo.setTotalSize(pageConfig.getTotalSize());
//		                listInfo.setDatas(list);
		            }

		        } catch (Exception e) {
		            e.printStackTrace();
		        }

		        	 return listInfo;


		    }


		    protected ListInfo getDataList(String arg0, boolean arg1) {

		        return null;
		    }
		    
			private ListInfo getResList(String sql,long offset,
					int maxPagesize) {
				ListInfo listinfo = new ListInfo();
				DBUtil db = new DBUtil();
				List list = new ArrayList();
				try {
					db.executeSelect(sql, offset, maxPagesize);
					list = new ResManagerImpl().getResList(db);
					listinfo.setDatas(list);
					listinfo.setTotalSize(db.getTotalSize());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return listinfo;
			}

	}


