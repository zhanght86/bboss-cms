package com.frameworkset.platform.cms.sitemanager;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManager;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.event.CMSEventType;
import com.frameworkset.platform.cms.searchmanager.CMSSearchManager;
import com.frameworkset.platform.cms.templatemanager.TemplateManager;
import com.frameworkset.platform.cms.templatemanager.TemplateManagerException;
import com.frameworkset.platform.cms.templatemanager.TemplateManagerImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.platform.cms.util.FtpUpfile;
import com.frameworkset.platform.cms.util.StringOperate;
import com.frameworkset.platform.config.model.Operation;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;

/**
 * 站点管理
 */
public class SiteManagerImpl extends EventHandle implements SiteManager {
	

    public SiteManagerImpl(){
    	//list = null;
    }
	
	private Logger log = Logger.getLogger(SiteManagerImpl.class);

	/**
	 * 根据站点中文名或英文名判断站点是否存在
	 */
  
	public boolean siteIsExist(String siteName, String secondName)
			throws SiteManagerException {

		return siteIsExist(siteName, secondName, null);

	}

	/**
	 * 根据站点id判断站点是否存在
	 */
	public boolean siteIsExist(String siteId) throws SiteManagerException {

		return siteIsExist(null, null, siteId);

	}

	private boolean siteIsExist(String sitename, String secondName,
			String siteId) throws SiteManagerException {

		if ((sitename == null || sitename.trim().length() == 0)
				&& (secondName == null || secondName.trim().length() == 0)
				&& (siteId == null || siteId.trim().length() == 0)) {
			throw new SiteManagerException("参数都为空,无法判断站点是否存在.");
		}

		try {
			DBUtil conn = new DBUtil();
			String sql = "select count(*) as total from td_cms_site where 1!=1";
			if (sitename != null && sitename.trim().length() != 0) {
				sql += " or name='" + sitename + "'";
			}
			if (secondName != null && secondName.trim().length() != 0) {
				sql += " or SECOND_NAME='" + secondName + "'";
			}
			if (siteId != null && siteId.trim().length() != 0) {
				sql += " or SITE_ID='" + siteId + "'";
			}

			conn.executeSelect(sql);
			if (conn.size() > 0) {
				if (conn.getInt(0, "total") > 0) {
					return true;
				}
			}

			return false;

		} catch (Exception e) {
			e.printStackTrace();
			throw new SiteManagerException("判断站点存在出错," + e.getMessage());
		}
	}

	/**
	 * 新建站点
	 */
	public void logCreateSite(Site site,HttpServletRequest request, HttpServletResponse response) throws SiteManagerException 
	{
		try
		{	createSite(site);
		    //add by ge.tao
		    //2007-08-20
		    //新建站点设置应用
		    this.setAppInSite(site,request);
			AccessControl control = AccessControl.getInstance();
			control.checkAccess(request,response);
			String operContent="";        
	        String operSource=request.getRemoteAddr();
	        String openModle="站点管理";
	        String userName = control.getUserName();
	        String description="";
	        LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="创建站点.站点名:"+site.getName(); 
//			System.out.println(operContent);
			description="";
	        logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);    
		}
        catch(Exception e)
        {
        	e.printStackTrace();
        	throw new SiteManagerException(e.getMessage());
        }
	}
	public void createSite(Site site) throws SiteManagerException {
		if (site == null) {
			throw new SiteManagerException("site对象为null,无法根据它来创建站点.");
		}

		// (1) 根据数据库里的信息,判断站点是否存在
		if (siteIsExist(site.getName(), site.getSecondName())) {
			throw new SiteManagerException("站点已经存在,无法创建同样的站点.");
		}

		// (2) 查找出放站点的根文件夹;如果不存在,创建
		String warPath = CMSUtil.getAppRootPath();
		if (warPath == null || "".equals(warPath.trim())) {
			throw new SiteManagerException("CMSUtil.getAppRootPath()方法返回空,请提供站点的根路径!");
		}
		String p = CMSUtil.getCMSContextPath()+CMSUtil.getWebSiteRootPath();
		File allSiteRootPath = new File(warPath, p);
		if (!allSiteRootPath.exists()) {
			boolean mkdirs = allSiteRootPath.mkdirs();
			if (!mkdirs) {
				throw new SiteManagerException("无法找到存放新建站点的根文件夹.");
			}
		}

		// (3) 构建该站点相对于"存放站点的根文件夹"的相对路径
		String parentSitePath = allSiteRootPath.getAbsolutePath();
		String siteDIR = "";
		// (3.1) 是否有父站点:有父站点,查找父站点相对于"存放站点的根文件夹"的相对路径
		long parentSiteID = site.getParentSiteId();
		if (parentSiteID > 0) {
			Site parentSite = this.getSiteInfo("" + parentSiteID);
			if (parentSite == null) {
				throw new SiteManagerException(
						"该站点有父站点,但是根据父站点id无法找到它的父站点的相关信息.");
			}
			String parentSiteDir = parentSite.getSiteDir();
			if (parentSiteDir == null || parentSiteDir.trim().length() == 0) {
				throw new SiteManagerException("该站点的父站点的相对路径为空.");
			}
			siteDIR += parentSiteDir;
			if (!parentSiteDir.endsWith("/")) {
				siteDIR += "/";
			}
			File parentSiteFolder = new File(allSiteRootPath.getAbsolutePath(),
					parentSiteDir);
			parentSitePath = parentSiteFolder.getAbsolutePath();
			if (!parentSiteFolder.exists()) {
				boolean mkdirs = parentSiteFolder.mkdirs();
				if (!mkdirs) {
					throw new SiteManagerException("无法找到父站点的文件夹.");
				}
			}
		}
		PreparedDBUtil conn = null;
		try {
			 conn = new PreparedDBUtil();
			long siteId = (long) conn.getNextPrimaryKey("TD_CMS_SITE");
			site.setSiteId(siteId);
//			String sitesavepath = "site" + siteId;
			String sitesavepath = site.getSecondName();
			siteDIR += sitesavepath;
			File currSitePath = new File(parentSitePath, sitesavepath);
			if (currSitePath.exists()) {
				throw new SiteManagerException("同样名称的文件或文件夹已经存在.无法创建站点");
			}
			boolean mkdir = currSitePath.mkdir();
			if (!mkdir) {
				throw new SiteManagerException("无法创建站点的文件夹.");
			}

//			// (4) 创建站点内的子文件夹
//			mkdir = (new File(currSitePath.getAbsoluteFile(), "_imgLib"))
//					.mkdir();
//			if (!mkdir) {
//				throw new SiteManagerException("无法创建站点内的_imgLib文件夹.");
//			}
			mkdir = (new File(currSitePath.getAbsoluteFile(), "_webprj"))
					.mkdir();
			if (!mkdir) {
				throw new SiteManagerException("无法创建站点内的_webprj文件夹.");
			}

			File _template = new File(currSitePath.getAbsoluteFile(),
					"_template");
			mkdir = _template.mkdir();
			if (!mkdir) {
				throw new SiteManagerException("无法创建站点内的_template文件夹.");
			}

//			new File(_template.getAbsoluteFile(), "files").mkdir();
//			if (!mkdir) {
//				throw new SiteManagerException("无法创建站点内的_template/files文件夹.");
//			}
			
			/**
			 * added by biaoping.yin on 2007.11.12
			 * 之前没有设置siteDir到站点对象中，导致新建站点后执行发布失败
			 */
			site.setSiteDir(siteDIR);
			// (5) 插入记录
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("insert into TD_CMS_SITE(");
			sqlBuffer.append("SITE_ID,NAME,SECOND_NAME,MAINSITE_ID,");
			sqlBuffer.append("SITEDESC,DBNAME,SITEDIR,STATUS,");
			sqlBuffer.append("FTPIP,FTPPORT,FTPUSER,FTPPASSWORD,FTPFOLDER,");
			sqlBuffer.append("WEBHTTP,SITEORDER,CREATETIME,CREATEUSER,");
			sqlBuffer.append("PUBLISHDESTINATION,INDEXFILENAME");
			sqlBuffer.append(",INDEXTEMPLATE_ID,FLOW_ID,PARENT_WORKFLOW");
			sqlBuffer.append(",LOCALPUBLISHPATH,DISTRIBUTEMANNERS)values(");
			sqlBuffer.append("?,?,?,?,");
		
			sqlBuffer.append("?,?,?,?,");
			sqlBuffer.append("?,?,?,?,?,");
			sqlBuffer.append("?,?,?,?,");
			sqlBuffer.append("?,?,?,?,?,?,?)");
			
			conn.preparedInsert(sqlBuffer.toString());

			conn.setInt(1, (int)siteId);

			String name = site.getName();
			if (name == null || name.trim().length() == 0) {
				throw new SiteManagerException("站点名字为空,不允许插入数据库.");
			}
			conn.setString(2, name);

			conn.setString(3, site.getSecondName());

			long parentSiteId = site.getParentSiteId();
			if (parentSiteId == 0) {
				conn.setNull(4, java.sql.Types.INTEGER);
			} else {
				conn.setLong(4, parentSiteId);
			}

			conn.setString(5, site.getSiteDesc());

			conn.setString(6, site.getDbName());

			conn.setString(7, site.getSiteDir());

			conn.setInt(8, site.getStatus());

			conn.setString(9, site.getFtpIp());

			conn.setShort(10, site.getFtpPort());

			conn.setString(11, site.getFtpUser());

			conn.setString(12, site.getFtpPassword());

			conn.setString(13, site.getFtpFolder());

			conn.setString(14, site.getWebHttp());

			conn.setInt(15, site.getSiteOrder());

			conn.setTimestamp(16, new Timestamp(new java.util.Date()
							.getTime()));

			long createUser = site.getCreateUser();
			if (createUser <= 0) {
				throw new SiteManagerException("创建站点的用户名为空，不允许插入数据库。");
			}
			conn.setLong(17, createUser);

			conn.setInt(18, site.getPublishDestination());

			conn.setString(19, site.getIndexFileName());

			int indexTemplateId = site.getIndexTemplateId();
			if (indexTemplateId <= 0) {
				conn.setNull(20, java.sql.Types.INTEGER);
				//conn.setInt(20,1);
			} else {
				conn.setInt(20, site.getIndexTemplateId());
			}

			int flowFromParent = site.workflowIsFromParent();
			int flowId = site.getWorkflow();
			if (flowFromParent == 1 || flowId<=0) {
				conn.setNull(21, java.sql.Types.INTEGER);
				conn.setInt(22, 1);
			} else {
				conn.setInt(21, flowId);				
				conn.setInt(22, 0);
			}
			conn.setString(23,site.getLocalPublishPath());
			conn.setString(24,site.getDistributeManners());
			conn.executePrepared();
			
			Event event = new EventImpl(site,CMSEventType.EVENT_SITE_ADD);
			super.change(event,true);
			
			//新增站点的时候默认将当前用户控制站点权限加上
			String userId = String.valueOf(site.getCreateUser());
			String siteid = String.valueOf(siteId);
			ResourceManager resManager = new ResourceManager();
			RoleManager roleManager;
			
			
			if(!userId.equals("1")){
				List list = resManager.getOperations("site");
				List listsitecnl = resManager.getOperations("channel");
				List listsitetpl = resManager.getOperations("sitetpl");
				List listsiteview = resManager.getOperations("channeldoc");
				List listsitefile = resManager.getOperations("sitefile");
				
				String[] opids1 = new String[list.size()];
				String[] opids2 = new String[listsitecnl.size()];
				String[] opids3 = new String[listsitetpl.size()];
				String[] opids4 = new String[listsiteview.size()];
				String[] opids5 = new String[listsitefile.size()];
				
				Operation op = new Operation();
					
				for(int i=0;i<list.size();i++){
					op = (Operation)list.get(i);
					opids1[i] = op.getId();
				}
				try {
					roleManager = SecurityDatabase.getRoleManager();
					roleManager.storeRoleresop(opids1,siteid,userId,"site",name,"user");
				}catch (Exception e) {
					e.printStackTrace();
				}
				for(int i=0;i<listsitecnl.size();i++){
					op = (Operation)listsitecnl.get(i);
					opids2[i] = op.getId();
				}
				try {
					roleManager = SecurityDatabase.getRoleManager();
					roleManager.storeRoleresop(opids2,siteid,userId,"site.channel",name,"user");
				}catch (Exception e) {
					e.printStackTrace();
				}
				for(int i=0;i<listsitetpl.size();i++){
					op = (Operation)listsitetpl.get(i);
					opids3[i] = op.getId();
				}
				try {
					roleManager = SecurityDatabase.getRoleManager();
					roleManager.storeRoleresop(opids3,siteid,userId,"sitetpl",name,"user");
				}catch (Exception e) {
					e.printStackTrace();
				}
				for(int i=0;i<listsiteview.size();i++){
					op = (Operation)listsiteview.get(i);
					opids4[i] = op.getId();
				}
				try {
					roleManager = SecurityDatabase.getRoleManager();
					roleManager.storeRoleresop(opids4,siteid,userId,"site.doc",name,"user");
				}catch (Exception e) {
					e.printStackTrace();
				}
				for(int i=0;i<listsitefile.size();i++){
					op = (Operation)listsitefile.get(i);
					opids5[i] = op.getId();
				}
				try {
					roleManager = SecurityDatabase.getRoleManager();
					roleManager.storeRoleresop(opids5,siteid,userId,"sitefile",name,"user");
				}catch (Exception e) {
					e.printStackTrace();
				}
	        }
			//新增站点时默认新增一个该站点管理员角色，并将该站点所有控制权赋给此角色
	        try {
	        	roleManager = SecurityDatabase.getRoleManager();
	        	
				Role role = new Role();
				//role.setRoleId(roleId);
				role.setRoleType("1") ; //设置角色类型 : 通用角色
				role.setRoleDesc(name+"站点管理员");
				role.setRoleName(name+"站点管理员");
				
				//增加创建人的ID信息 2009-1-14
				role.setOwner_id((int)site.getCreateUser())  ;
				//roleManager.storeRole(role);
				roleManager.insertRole(role);
				
				//String roleId = roleManager.getRoleByName(name+"站点管理员").getRoleId();
				//gao.tang修改，调用roleManager.insertRole(role)方法时，在该方法中将role_id设置进去
				String roleId = role.getRoleId();
				
				Operation op = new Operation();
				List list1 = resManager.getOperations("site");
				List list2 = resManager.getOperations("channel");
	        	List list3 = resManager.getOperations("sitetpl");
	        	List list4 = resManager.getOperations("channeldoc");
	        	List list5 = resManager.getOperations("sitefile");
	        	
	        	String[] opids1 = new String[list1.size()];
				String[] opids2 = new String[list2.size()];
				String[] opids3 = new String[list3.size()];
				String[] opids4 = new String[list4.size()];
				String[] opids5 = new String[list5.size()];
				
				for(int k=0;k<list1.size();k++){
					op = (Operation)list1.get(k);
					opids1[k] = op.getId();
				}
				roleManager.storeRoleresop(opids1,siteid,roleId,"site",name,"role");
				for(int k=0;k<list2.size();k++){
					op = (Operation)list2.get(k);
					opids2[k] = op.getId();
				}
				roleManager.storeRoleresop(opids2,siteid,roleId,"site.channel",name,"role");
				for(int k=0;k<list3.size();k++){
					op = (Operation)list3.get(k);
					opids3[k] = op.getId();
				}
				roleManager.storeRoleresop(opids3,siteid,roleId,"sitetpl",name,"role");
				for(int k=0;k<list4.size();k++){
					op = (Operation)list4.get(k);
					opids4[k] = op.getId();
				}
				roleManager.storeRoleresop(opids4,siteid,roleId,"site.doc",name,"role");
				for(int k=0;k<list5.size();k++){
					opids5[k] = op.getId();
					op = (Operation)list5.get(k);
				}
				roleManager.storeRoleresop(opids5,siteid,roleId,"sitefile",name,"role");
				//加站点离散资源
				DBUtil db = new DBUtil();
				db.executeSelect("select title from td_sm_res where restype_id ='site'");
				if(db.size()>0){
					for(int j =0 ;j<db.size();j++)
					{
						roleManager.storeRoleresop("write",db.getString(j,"title"),roleId,"site",db.getString(j,"title"),"role");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*拷贝标签附件到tmp目录*/				
			copyTagAttachment2Tmp(currSitePath.getAbsoluteFile()+"\\_template");
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("sql异常.异常描述为:" + e.getMessage());
		}finally{
			conn.resetPrepare();
		}
		
		/**
		 * added by biaoping.yin on 2007.10.19
		 * 创建lucene索引库，如果系统启用了全文检索，搜索引擎的类型为lucene
		 */
		if(CMSUtil.enableIndex() && CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_LUCENE))
		{
			
			//默认添加新站点的整站索引
			String index_siteId = String.valueOf(site.getSiteId());
			String index_searchType = "2";
			//String index_name = site.getName();							//索引名字暂用站点名字
			String index_name = site.getSecondName() ;    //索引名字用站点英文名称
			String index_lever = site.getIndex_level();
			String index_day = site.getIndex_day();
			String index_time = site.getIndex_time();
		
			try{
				DBUtil index_db = new DBUtil();
				
				long searchId = index_db.getNextPrimaryKey("td_cms_site_search") ;
				
				String index_sql = 
						"insert into td_cms_site_search (id,site_id,search_type,name,lever,day,time) values("
						+searchId +"," 
						+ index_siteId + "," + index_searchType + ",'" + index_name + "'," + index_lever + "," + index_day + "," + index_time + ")";
				index_db.executeInsert(index_sql);
			}catch(Exception e){
				System.out.println("添加新站点时添加整站索引发生错误");
			}
		}
		
	}
    
	public void copyTagAttachment2Tmp(String destPath){
		String folderName = "\\cms\\siteResource\\publicAttachment";
		String rootPath = CMSUtil.getAppRootPath();
		String rar_src = rootPath + folderName;
		FileUtil fileUtil = new FileUtil();
		File file = null;
    	try {
    		file = new File(rar_src);    		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	File[] files = {};
    	if(file.isDirectory()){
    		files = file.listFiles();
    		for(int i=0;i<files.length;i++){
    			File rarFile = files[i];
    			try {    				
					fileUtil.unzip(rarFile.getAbsolutePath(),destPath);
				} catch (ZipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			
    		}
    	}
	}
	
	public static boolean isNull(String str){
		boolean flag = false;
		if(str == null || str.trim().length()<=0){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 删除站点(设置标志位)
	 */
	public boolean logDeleteSite(String siteId,HttpServletRequest request, HttpServletResponse response) throws SiteManagerException 
	{	
		boolean returnValue = false;
		try
		{	
			AccessControl control = AccessControl.getInstance();
			control.checkAccess(request,response);
			String operContent="";        
	        String operSource=request.getRemoteAddr();
	        String openModle="站点管理";
	        String userName = control.getUserName();
	        String description="";
	        LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="删除站点.站点ID:"+siteId; 
			//System.out.println(operContent);
			description="";
	        logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);  
	        
	        /* *********************   删除文件  start  *************************/
	        
			/* 站点发布路径 */
			String sitePubPath = CMSUtil.getSitePubDestinction(siteId);
			Site site = CMSUtil.getSiteCacheManager().getSite(siteId);
			
			boolean[] local2remote = getSitePublishDestination(site.getPublishDestination());
			
			if(local2remote[0]){
				//第一步 ，删除发布到本地的站点文件
				FileUtil.deleteFile(sitePubPath);//删除发布的站点内容
			}
			
			if(local2remote[1]){
			//第二步 ，删除发布到ftp的站点文件
			if(site != null && !isNull(site.getFtpIp()) 
					&& !isNull(site.getFtpUser()) && !isNull(site.getFtpPassword())){
				
					FtpUpfile ftpUpfile = new FtpUpfile(site.getFtpIp(),site.getFtpUser(),site.getFtpPassword());
					ftpUpfile.login();
					String relatefilepath = CMSUtil.getPath("",site.getFtpFolder());
					ftpUpfile.deleteAll(relatefilepath);
					//ftpUpfile.deleteRemoteFile(relatefilepath);
					ftpUpfile.logout();
				}
			}
			
			//第三步 ，删除文档采集时的站点文件
			String siteLocPath = CMSUtil.getPath("",getSiteAbsolutePath(siteId));
			FileUtil.deleteFile(siteLocPath);
			
			/* ****************** 删除文件  end  *******************************/
			
	        returnValue = deleteSite(siteId);
		}
        catch(Exception e)
        {
        	throw new SiteManagerException(e.getMessage());
        }
        return returnValue;
        
	}
	public boolean deleteSite(String siteId) throws SiteManagerException {
		boolean flag = false;
		try
		{
			// 一次性删除全文检索索引表中关于本站点的索引种子记录和文档种子记录,以及删除索引库目录,weida
			if(CMSUtil.enableIndex()){
				if(CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_LUCENE)){
					try{
						CMSSearchManager sm = new CMSSearchManager();
							sm.deleteSiteIndexs(siteId);
					}catch(Exception e){}
				}
			}
		
			DBUtil db = new DBUtil();
			DBUtil dbSelectSite = new DBUtil();
			//删除站点的时候，关联删除该站点对应的管理员角色
			Site site = getSiteInfo(siteId);
			String rolename = site.getName()+"站点管理员";
			String selectrole = "select role_id from td_sm_role where role_name ='"+ rolename +"'";
			db.executeSelect(selectrole);
			if(db.size()>0)
			{
				String roleid = db.getString(0,"role_id");
				RoleManager roleManager = SecurityDatabase.getRoleManager();
				Role role = new Role();
				role.setRoleId(roleid);
				try
				{
					roleManager.deleteRole(role);
				}
				catch(Exception e)
				{
					
				}
			}
			
			
			String selectSubSite = "SELECT a.site_id,nvl(a.mainsite_id,-1) as orderid " +
					"FROM td_cms_site a START WITH site_id = " + siteId + 
					"CONNECT BY PRIOR site_id = mainsite_id order by orderid desc,site_id desc";
			dbSelectSite.executeSelect(selectSubSite);//所有子站点
			
            //删除站点引用的应用记录 TD_CMS_SITEAPPS
			//add by ge.tao
			//2007-08-20
			String deleteApp = "delete from TD_CMS_SITEAPPS where site_id=" + siteId;
			
			db.addBatch(deleteApp);	
			
			for(int a = 0; a < dbSelectSite.size(); a++)
			{
				int siteIds = dbSelectSite.getInt(a,"site_id");
				
				//删除该站点下所有频道，文档相关的记录
				String selectChls = "select c.channel_id from td_cms_channel c where c.site_id = " + siteIds;
				db.executeSelect(selectChls);
				for (int i = 0; i < db.size(); i++) 
				{
					int chlIds = db.getInt(i,"channel_id");
					ChannelManager cm = new ChannelManagerImpl();
					cm.deleteChannel(String.valueOf(chlIds));
				}
				//删除该站点下所有频道，文档相关的记录 end
				
				//删除具体的子站点
				String sql = "delete from td_cms_site t where t.site_id = " + siteIds;
				String sql1 = "delete from td_cms_sitefield t where t.site_id = " + siteIds;
				String sql2 = "delete from td_cms_site_tpl t where t.site_id = "  + siteIds;
				String sql3 = "delete from tl_cms_site_flow_his t where t.site_id = "  + siteIds;
				String sql4 ="delete from td_cms_siteuser t where t.site_id = "  + siteIds;
				String sql5 = "delete from TD_SM_ROLERESOP  " +
				  " where res_id ='"+ siteIds +"' and restype_id in('site','site.channel','site.doc','sitetpl','sitefile')";
				
				
				db.addBatch(sql1);
				db.addBatch(sql2);
				db.addBatch(sql3);
				db.addBatch(sql4);
				db.addBatch(sql);
				db.addBatch(sql5);
				
			}
	
			
			
			//真正删除该站点
			String delsql = "delete from td_cms_site t where t.site_id = " + siteId;
			db.addBatch(delsql);
			
			db.executeBatch();//批处理操作数据库
			
			flag = true;
			
			Event event = new EventImpl(site,CMSEventType.EVENT_SITE_DELETE);
			super.change(event,true);
		}
		catch(Exception e)
        {
        	throw new SiteManagerException(e.getMessage());
        }
		return flag;
		//return changeStatus(-1,siteId);
	}

	/**
	 * 停止站点
	 */
	public boolean logStopSite(String siteId,HttpServletRequest request, HttpServletResponse response) throws SiteManagerException 
	{	
		boolean returnValue = false;
		try
		{	
			AccessControl control = AccessControl.getInstance();
			control.checkAccess(request,response);
			String operContent="";        
	        String operSource=request.getRemoteAddr();
	        String openModle="站点管理";
	        String userName = control.getUserName();
	        String description="";
	        LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="停止站点.站点ID:"+siteId; 
//			System.out.println(operContent);
			description="";
	        logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);  
	        returnValue = stopSite(siteId);
		}
        catch(Exception e)
        {
        	throw new SiteManagerException(e.getMessage());
        }
        return returnValue;
        
	}
	public boolean stopSite(String siteId) throws SiteManagerException {
		return changeStatus(2,siteId);
	}
	
	
	private boolean changeStatus(int status,String siteId) throws SiteManagerException{
		PreparedDBUtil conn = null;
		try {
			 conn = new PreparedDBUtil();
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("update td_cms_site set STATUS = ? ");
			sqlBuffer.append("where site_id = ?");
			conn.preparedUpdate(sqlBuffer.toString());
			conn.setInt(1,status);
			
			if (siteId==null || siteId.trim().length()==0) {
				throw new SiteManagerException("站点id为空,无法更新站点");
			}
			
			conn.setString(2, siteId);
			conn.executePrepared();
			Event event = new EventImpl(getSiteInfo(siteId),CMSEventType.EVENT_SITESTATUS_UPDATE);
			super.change(event,true);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("编辑站点报错！" + e.getMessage());
		}finally{
			conn.resetPrepare();
		}
	}
	/**
	 * 改变站点的工作流程
	 */
	public boolean changeWorkflow(String siteId,int newFlowId,int workflowIsFromParent) throws SiteManagerException {
		PreparedDBUtil conn = null;
		try {
			 conn = new PreparedDBUtil();
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("update td_cms_site set FLOW_ID = ?,PARENT_WORKFLOW = ? ");
			sqlBuffer.append("where site_id = ?");
			
			conn.preparedUpdate(sqlBuffer.toString());
			if(workflowIsFromParent == 1 || newFlowId<=0){
				conn.setNull(1,java.sql.Types.INTEGER);
				conn.setInt(2,1);
			}else{
				conn.setInt(1,newFlowId);
				conn.setInt(2,0);
			}
			if (siteId==null || siteId.trim().length()==0) {
				throw new SiteManagerException("站点id为空,无法更新站点");
			}
			conn.setString(3, siteId);
			conn.executePrepared();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("编辑站点流程报错！" + e.getMessage());
		}finally{
			conn.resetPrepare();
		}
	}
	
	private boolean canUpdate(Site site)throws SiteManagerException{
		long siteId = site.getSiteId();
		String siteName = site.getName();
		String secondName = site.getSecondName();
		long parentSiteId = site.getParentSiteId();
		
		StringBuffer sql = new StringBuffer("select count(*) as total from TD_CMS_SITE ");
		sql.append(" where SITE_ID != '"+siteId+"' ");
		if(parentSiteId>0){
			sql.append(" and MAINSITE_ID = '"+parentSiteId+"'");
		}else{
			sql.append(" and(MAINSITE_ID = '0' or mainsite_id is null)");
		}
		sql.append(" and(1!=1 ");
		if(siteName!=null && siteName.trim().length()!=0){
			sql.append(" or NAME = '"+siteName+"' ");
		}
		if(secondName!=null && secondName.trim().length()!=0){
			sql.append(" or SECOND_NAME = '"+secondName+"' ");
		}
		sql.append(")");
//		System.out.println("sql="+sql.toString());
		try {
			DBUtil db = new DBUtil();
			db.executeSelect(sql.toString());
			if (db.size() > 0) {
				if (db.getInt(0, "total") > 0) {
					return false;
				}
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("发生sql异常,无法判断站点是否可以更新.异常信息为:" + e.getMessage());
		}		
	}

	/**
	 * 更新站点
	 */
	public boolean logUpdateSite(Site site,HttpServletRequest request, HttpServletResponse response) throws SiteManagerException 
	{	
		boolean returnValue = false;
		try
		{	
			AccessControl control = AccessControl.getInstance();
			control.checkAccess(request,response);
			String operContent="";        
	        String operSource=request.getRemoteAddr();
	        String openModle="站点管理";
	        String userName = control.getUserName();
	        String description="";
	        LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="修改站点.站点名:"+site.getName(); 
//			System.out.println(operContent);
			description="";
	        logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);  
	        returnValue = updateSite(site);
	        //更新站点下的应用
	        //add by ge.tao
	        //2007-08-20
	        this.setAppInSite(site,request);
		}
        catch(Exception e)
        {
        	throw new SiteManagerException(e.getMessage());
        }
        return returnValue;
        
	}
	public boolean updateSite(Site site) throws SiteManagerException {
		if (site == null) {
			throw new SiteManagerException("site对象为空,无法更新站点");
		}
		if(!canUpdate(site)){
			throw new SiteManagerException("可能因为重名等原因,不能更新该站点");
		}
		try {
			PreparedDBUtil conn = new PreparedDBUtil();
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("update td_cms_site set ");
			sqlBuffer.append("NAME=?,");
			sqlBuffer.append("SECOND_NAME=?,");
			sqlBuffer.append("SITEDESC=?,");
			sqlBuffer.append("WEBHTTP=?,");
			sqlBuffer.append("FTPIP=?,");
			sqlBuffer.append("FTPPORT=?,");
			sqlBuffer.append("FTPUSER=?,");
			sqlBuffer.append("ftppassword=?,");
			sqlBuffer.append("FTPFOLDER=?,");
			sqlBuffer.append("PUBLISHDESTINATION = ?,");
			sqlBuffer.append("INDEXFILENAME=?,");
			sqlBuffer.append("SITEORDER=?,");
			sqlBuffer.append("dbname=?, ");
			sqlBuffer.append("indextemplate_id =?,");
			sqlBuffer.append("LOCALPUBLISHPATH =?, ");
			sqlBuffer.append("DISTRIBUTEMANNERS =? ");
			sqlBuffer.append("where site_id = ?");

			conn.preparedUpdate(sqlBuffer.toString());

			String name = site.getName();
			if (name == null || name.trim().length() == 0) {
				throw new SiteManagerException("站点名字为空,不允许插入数据库.");
			}
			conn.setString(1, name);

			conn.setString(2, site.getSecondName());

			conn.setString(3, site.getSiteDesc());

			conn.setString(4, site.getWebHttp());

			conn.setString(5, site.getFtpIp());

			conn.setShort(6, site.getFtpPort());

			conn.setString(7, site.getFtpUser());
			
			conn.setString(8, site.getFtpPassword());

			conn.setString(9, site.getFtpFolder());

			conn.setInt(10, site.getPublishDestination());

			conn.setString(11, site.getIndexFileName());

			conn.setInt(12, site.getSiteOrder());		
			
			conn.setString(13, site.getDbName());	
			conn.setInt(14, site.getIndexTemplateId());

			long siteId = site.getSiteId();
			if (siteId <= 0) {
				throw new SiteManagerException("站点id为空,无法更新站点");
			}
			conn.setString(15,site.getLocalPublishPath());
			conn.setString(16,site.getDistributeManners());
			conn.setLong(17, siteId);
			//更新站点名称时，更新该站点管理员角色名称
			String sql ="select name from td_cms_site where site_id="+ siteId ;
			DBUtil db = new DBUtil();
			db.executeSelect(sql);
			if(db.size()>0)
			{
				String oldname = db.getString(0,"name")+"站点管理员";
				String rolename = site.getName()+"站点管理员";
				String sqlrole ="update td_sm_role set role_name ='"+ rolename +"' where role_name ='"+ oldname +"'";
				db.executeUpdate(sqlrole);
			}
			//------------
			conn.executePrepared();
		
			Event event = new EventImpl(site,CMSEventType.EVENT_SITE_UPDATE);
			super.change(event,true);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("站点编辑保存报错！" + e.getMessage());
		}
	}
	/**
	 * 更新站点状态
	 */
	public void updateSiteStatus(int siteId,int siteState) throws SiteManagerException{
		DBUtil db = new DBUtil();
		String sql = "update td_cms_site set status = " + siteState + " where site_id = " + siteId;
		try{
			db.executeUpdate(sql);
			Event event = new EventImpl(getSiteInfo(siteId + ""),CMSEventType.EVENT_SITESTATUS_UPDATE);
			super.change(event,true);
		}catch(SQLException e){
			e.printStackTrace();
			new SiteManagerException("站点状态更新时出错：" +  e.getMessage());
		}
	}
	/**
	 * 根据站点id获取站点信息,如果没有找到,返回null
	 */
	public Site getSiteInfo(String siteId) throws SiteManagerException {

		if (siteId == null || siteId.trim().length() == 0) {
			throw new SiteManagerException("没有提供站点id,无法查找站点信息.");
		}

		try {
			DBUtil db = new DBUtil();
			String sql = "select a.SITE_ID, a.NAME, a.SECOND_NAME, "
					+ "a.MAINSITE_ID, a.SITEDESC, a.DBNAME, a.SITEDIR,"
					+ "a.STATUS, a.FTPIP, a.FTPPORT, a.FTPUSER, "
					+ "a.FTPPASSWORD, a.FTPFOLDER, a.WEBHTTP,"
					+ "a.SITEORDER, a.CREATETIME, a.CREATEUSER, "
					+ "a.PUBLISHDESTINATION, a.INDEXFILENAME, a.INDEXTEMPLATE_ID,"
					+ "site_flow_id(a.SITE_ID) as FLOW_ID, a.PARENT_WORKFLOW,a.LOCALPUBLISHPATH,a.DISTRIBUTEMANNERS from td_cms_site a "
//					+ "where (a.status=0 or a.status=1) and a.SITE_ID ='"
					+ "where a.SITE_ID ='"
					+ siteId + "'";

			db.executeSelect(sql);
			if (db.size() > 0) {
				Site site = new Site();
				fillSiteInfo(site, 0, db);
				return site;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("获取站点信息时发生sql异常,描述为:"
					+ e.getMessage());
		}
	}
	
	/**
	 * 根据站点名称获得完整站点信息
	 * String siteName 站点名称name
	 */
	public Site getSiteInfoBySiteName(String siteName)throws SiteManagerException{
		if (siteName == null || siteName.trim().length() == 0) {
			throw new SiteManagerException("没有提供站点名称,无法查找站点信息.");
		}
		try {
			DBUtil db = new DBUtil();
			StringBuffer sql = new StringBuffer();
			sql.append( "select a.SITE_ID, a.NAME, a.SECOND_NAME, " );
			sql.append( "a.MAINSITE_ID, a.SITEDESC, a.DBNAME, a.SITEDIR,");
			sql.append( "a.STATUS, a.FTPIP, a.FTPPORT, a.FTPUSER, ");
			sql.append( "a.FTPPASSWORD, a.FTPFOLDER, a.WEBHTTP,");
			sql.append( "a.SITEORDER, a.CREATETIME, a.CREATEUSER, ");
			sql.append( "a.PUBLISHDESTINATION, a.INDEXFILENAME, a.INDEXTEMPLATE_ID,");
			sql.append( "site_flow_id(a.SITE_ID) as FLOW_ID, a.PARENT_WORKFLOW,a.LOCALPUBLISHPATH,");
			sql.append( "a.DISTRIBUTEMANNERS from td_cms_site a ");
//					+ "where (a.status=0 or a.status=1) and a.SITE_ID ='"
			sql.append( "where a.SECOND_NAME ='"); 
		    sql.append(siteName);
			sql.append("'");
			db.executeSelect(sql.toString());
			if (db.size() > 0) {
				Site site = new Site();
				fillSiteInfo(site, 0, db);
				return site;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("获取站点信息时发生sql异常,描述为:"+ e.getMessage());
		}
	}

	/**
	 * 根据站点id查找父站点信息,如果没有找到,返回null
	 */
	public Site getParentSiteInfo(String siteId) throws SiteManagerException {
		if (siteId == null || siteId.trim().length() == 0) {
			throw new SiteManagerException("没有提供站点id,无法查找其父站点信息.");
		}

		try {
			DBUtil db = new DBUtil();
			String sql = "select a.SITE_ID, a.NAME, a.SECOND_NAME, "
					+ "a.MAINSITE_ID, a.SITEDESC, a.DBNAME, a.SITEDIR,"
					+ "a.STATUS, a.FTPIP, a.FTPPORT, a.FTPUSER, "
					+ "a.FTPPASSWORD, a.FTPFOLDER, a.WEBHTTP,"
					+ "a.SITEORDER, a.CREATETIME, a.CREATEUSER, "
					+ "a.PUBLISHDESTINATION, a.INDEXFILENAME, a.INDEXTEMPLATE_ID,"
					+ "site_flow_id(a.SITE_ID) as FLOW_ID, a.PARENT_WORKFLOW,a.LOCALPUBLISHPATH,a.DISTRIBUTEMANNERS "
					+ "from td_cms_site a inner join td_cms_site b "
					+ "on a.SITE_ID = b.MAINSITE_ID "
					+ "where (a.status=0 or a.status=1) and (b.status=0 or b.status=1) and  b.SITE_ID ='"
					+ siteId + "' order by a.SITEORDER asc";
//			System.out.println("获取父站点信息的sql=" + sql);
			db.executeSelect(sql);
			if (db.size() > 0) {
				Site site = new Site();
				fillSiteInfo(site, 0, db);
				return site;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("发生sql异常,无法查找站点的父站点信息.描述为:"
					+ e.getMessage());
		}
	}

	/**
	 * 查是否有子站点,如果传入的siteId为0的话,表示查找是否有根节点
	 */
	public boolean hasSubSite(String siteId) throws SiteManagerException {
		if (siteId == null || siteId.trim().length() == 0) {
			throw new SiteManagerException("没有提供站点id,无法判断站点下是否有子站点.");
		}
		try {
			DBUtil db = new DBUtil();
			String sql;
			if ("0".equals(siteId)) {
				sql = "select count(*) total from td_cms_site a "
						+ "where (a.status=0 or a.status=1) and (a.MAINSITE_ID is null "
						+ "or a.mainsite_id = 0)";
			} else {
				sql = "select count(*) total from td_cms_site a "
						+ "where (a.status=0 or a.status=1) and a.MAINSITE_ID is not null "
						+ "and a.mainsite_id != 0	and a.mainsite_id = '"
						+ siteId + "'";
			}

			db.executeSelect(sql);
			if (db.size() > 0) {
				if (db.getInt(0, "total") > 0) {
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("发生sql异常,无法判断站点下是否有子站点.异常信息为:"
					+ e.getMessage());
		}
	}

	public List getTopLevelSite() throws SiteManagerException {
		try {
			DBUtil db = new DBUtil();
			String sql = "select a.SITE_ID, a.NAME, a.SECOND_NAME, "
					+ "a.MAINSITE_ID, a.SITEDESC, a.DBNAME, a.SITEDIR,"
					+ "a.STATUS, a.FTPIP, a.FTPPORT, a.FTPUSER, "
					+ "a.FTPPASSWORD, a.FTPFOLDER, a.WEBHTTP,"
					+ "a.SITEORDER, a.CREATETIME, a.CREATEUSER, "
					+ "a.PUBLISHDESTINATION, a.INDEXFILENAME, a.INDEXTEMPLATE_ID,"
					+ "site_flow_id(a.SITE_ID) as FLOW_ID, a.PARENT_WORKFLOW,a.LOCALPUBLISHPATH,a.DISTRIBUTEMANNERS "
					+ "from td_cms_site a where (a.status=0 or a.status=1) and a.MAINSITE_ID is null "
					+ "order by a.SITEORDER,a.SITE_ID";
			//System.out.println("///"+sql);
			db.executeSelect(sql);
			List subsite = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Site site = new Site();
				fillSiteInfo(site, i, db);
				subsite.add(site);
			}
			return subsite;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("获取直接子站点时发生sql异常,描述为:"
					+ e.getMessage());
		}
	}
	
	/**
	 * 获取站点树中顶层站点,也就是没有父站点的站点(不受状态影响)
	 */
	public List getTopSubSiteList() throws SiteManagerException{
		try {
			DBUtil db = new DBUtil();
			String sql = "select * from td_cms_site where MAINSITE_ID is null order by SITEORDER,SITE_ID";
		
			db.executeSelect(sql);
			List subsite = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Site site = new Site();
				fillSiteInfo(site, i, db);
				subsite.add(site);
			}
			return subsite;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("获取直接子站点时发生sql异常,描述为:"
					+ e.getMessage());
		}
	}
	
	

	public List getDirectSubSiteList(String siteId) throws SiteManagerException {
		if (siteId == null || siteId.trim().length() == 0) {

			throw new SiteManagerException("没有提供站点id,无法查找直接子站点.");

		}

		try {
			DBUtil db = new DBUtil();
			String sql = "select  a.SITE_ID, a.NAME, a.SECOND_NAME, "
					+ "a.MAINSITE_ID, a.SITEDESC, a.DBNAME, a.SITEDIR,"
					+ "a.STATUS, a.FTPIP, a.FTPPORT, a.FTPUSER, "
					+ "a.FTPPASSWORD, a.FTPFOLDER, a.WEBHTTP,"
					+ "a.SITEORDER, a.CREATETIME, a.CREATEUSER, "
					+ "a.PUBLISHDESTINATION, a.INDEXFILENAME, a.INDEXTEMPLATE_ID,"
					+ "site_flow_id(a.SITE_ID) as FLOW_ID, a.PARENT_WORKFLOW,a.LOCALPUBLISHPATH,a.DISTRIBUTEMANNERS "
					+ "from td_cms_site a where (a.status=0 or a.status=1) and a.MAINSITE_ID ='"
					+ siteId + "'order by a.SITEORDER,a.SITE_ID";
//			System.out.println(sql);
			db.executeSelect(sql);
			List subsite = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Site site = new Site();
				fillSiteInfo(site, i, db);
				subsite.add(site);
			}
			return subsite;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("获取子站点时发生sql异常,描述为:"
					+ e.getMessage());
		}
	}

	/**
	 * 获取直接子站点(不受站点状态影响)
	 */
	public List getSubSiteList(String siteId) throws SiteManagerException{
		if (siteId == null || siteId.trim().length() == 0) {

			throw new SiteManagerException("没有提供站点id,无法查找直接子站点.");

		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select * from td_cms_site where MAINSITE_ID = "+ siteId +" order by SITEORDER,SITE_ID";

			db.executeSelect(sql);
			List subsite = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Site site = new Site();
				fillSiteInfo(site, i, db);
				subsite.add(site);
			}
			return subsite;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("获取子站点时发生sql异常,描述为:"
					+ e.getMessage());
		}
	}
	
	
	/**
	 * 获取站点下的所有站点
	 */
	public List getAllSubSiteList(String siteId) throws SiteManagerException {
		if (siteId == null || siteId.trim().length() == 0) {

			throw new SiteManagerException("没有提供站点id,无法查找直接子站点.");

		}

		try {
			DBUtil db = new DBUtil();
			List sites = new ArrayList();
			String sql = "select * from(SELECT  a.SITE_ID, a.NAME, a.SECOND_NAME, "
					+ "a.MAINSITE_ID, a.SITEDESC, a.DBNAME, a.SITEDIR,"
					+ "a.STATUS, a.FTPIP, a.FTPPORT, a.FTPUSER, "
					+ "a.FTPPASSWORD, a.FTPFOLDER, a.WEBHTTP,"
					+ "a.SITEORDER, a.CREATETIME, a.CREATEUSER, "
					+ "a.PUBLISHDESTINATION, a.INDEXFILENAME, a.INDEXTEMPLATE_ID,"
					+ "site_flow_id(a.SITE_ID) as FLOW_ID, a.PARENT_WORKFLOW,a.LOCALPUBLISHPATH,a.DISTRIBUTEMANNERS "
					+ "FROM td_cms_site a "
					+ "start with a.site_id= '"
					+ siteId
					+ "' and (a.status=0 or a.status=1) "
					+ "connect by prior a.site_id = a.mainsite_id  and (a.status=0 or a.status=1)"
					+ " order by a.siteorder,a.SITE_ID) b where b.site_id <> '"+siteId+"'";
			db.executeSelect(sql);
//			System.out.println("获取所有子站点的sql=" + sql);
			for (int i = 0; i < db.size(); i++) {
				Site site = new Site();
				this.fillSiteInfo(site, i, db);
				sites.add(site);
			}
			return sites;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("发生sql异常,无法获取子节点信息.详细信息为:"
					+ e.getMessage());
		}
	}

	public List getSiteList() throws SiteManagerException {
		DBUtil db = new DBUtil();
		List list = new ArrayList();
		String sql="select distinct site_id,name,DBNAME,SITEDIR from td_cms_site";
		try{
			db.executeSelect(sql);
			for(int i=0;i<db.size();i++){
				Site site = new Site();	
				String id = String.valueOf(db.getInt(i,"site_id")).toString();
				site.setSiteId(Long.parseLong(id));
				site.setName(db.getString(i,"name"));
				site.setSiteDir(db.getString(i,"SITEDIR"));
				site.setDbName(db.getString(i,"DBNAME"));
				
				list.add(site);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SiteManagerException(e.getMessage());
		}
		return list;
	}
	public List getSiteStatusList() throws SiteManagerException {
		List list = new ArrayList();
		for(int i=0;i<3;i++){
			String statusId = i + "";
			String statusName = "";
			switch(i){
			case 0: statusName = "已开通";
					break;
			case 1:	statusName = "未开通";
					break;
			case 2: statusName = "已停用";
					break;	
			}	
			List oneStatus = new ArrayList();
			oneStatus.add(statusId);
			oneStatus.add(statusName);
			list.add(oneStatus);
		}
		return list;
	}
	/**
	 * 判断站点是否是活动的，即站点的status是否为0（开通状态）
	 * @return
	 * @throws SiteManagerException
	 */
	public boolean isActive(String siteId) throws SiteManagerException
	{
		boolean flag = false;
		int status = ((Site)getSiteInfo(siteId)).getStatus();
		if(status==0)
			flag = true;
		return flag;
	}
	/**
	 * 判断站点下是否有频道
	 */
	public boolean hasChannelOfSite(String siteId) throws SiteManagerException {
		if (siteId == null || siteId.trim().length() == 0) {
			throw new SiteManagerException("没有提供站点id,无法判断站点下是否有频道.");
		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select count(*) as total from TD_CMS_CHANNEL where status=0 and site_id="
					+ siteId;
			//System.out.println("=========="+sql);
			db.executeSelect(sql);
			if (db.size() > 0) {
				if (db.getInt(0, "total") > 0) {
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("发生sql异常,无法判断站点下是否有频道.异常信息为:"
					+ e.getMessage());
		}
	}

	public List getDirectChannelsOfSite(String siteId)
			throws SiteManagerException {
		return getChannels(siteId, 1);
	}

	public List getAllChannelsOfSite(String siteId) throws SiteManagerException {
		return getChannels(siteId, 2);
	}

	/*
	 * type = 1,顶级频道; =2,站点下的所有频道
	 */
	private List getChannels(String siteId, int type)
			throws SiteManagerException {
		if (siteId == null || siteId.trim().length() == 0) {
			throw new SiteManagerException("没有提供站点id,无法返回频道信息.");
		}
		try {
			DBUtil db = new DBUtil();
			String sql;
			if (type == 1) {
				sql = "select a.CHANNEL_ID, a.NAME, a.DISPLAY_NAME, a.ISNAVIGATOR,a.PUB_FILE_NAME, "
						+ "a.PARENT_ID, a.CHNL_PATH, a.CREATEUSER, a.CREATETIME,"
						+ "a.ORDER_NO, a.SITE_ID, a.STATUS, a.OUTLINE_TPL_ID, "
						+ "a.DETAIL_TPL_ID, CHANNEL_FLOW_ID(a.WORKFLOW) as WORKFLOW, a.CHNL_OUTLINE_DYNAMIC, "
						+ "a.DOC_DYNAMIC, a.CHNL_OUTLINE_PROTECT, a.DOC_PROTECT, "
						+ "a.PARENT_WORKFLOW , OUTLINEPICTURE, PAGEFLAG, INDEXPAGEPATH, COMMENTSWITCH, " +
						" COMMENT_TEMPLATE_ID, COMMENTPAGEPATH " +
								"from TD_CMS_CHANNEL a "
						+ "where (a.PARENT_ID=0 or a.PARENT_ID is null) and a.status=0 "
						+ "and a.site_id=" + siteId + " order by order_no,channel_id";
			} else if (type == 2) {
				sql = "select a.CHANNEL_ID, a.NAME, a.DISPLAY_NAME, a.PARENT_ID,  a.ISNAVIGATOR, a.PUB_FILE_NAME,"
						+ "a.CHNL_PATH, a.CREATEUSER, a.CREATETIME, a.ORDER_NO,"
						+ "a.SITE_ID, a.STATUS, a.OUTLINE_TPL_ID, a.DETAIL_TPL_ID,"
						+ "CHANNEL_FLOW_ID(a.WORKFLOW) as WORKFLOW, a.CHNL_OUTLINE_DYNAMIC, a.DOC_DYNAMIC,"
						+ "a.CHNL_OUTLINE_PROTECT, a.DOC_PROTECT, "
						+ "a.PARENT_WORKFLOW , OUTLINEPICTURE, PAGEFLAG, INDEXPAGEPATH, COMMENTSWITCH, " +
						" COMMENT_TEMPLATE_ID, COMMENTPAGEPATH " +
								"from TD_CMS_CHANNEL a where a.status=0 "
						+ "and a.site_id=" + siteId +" order by order_no,channel_id";
			} else {
				throw new SiteManagerException("无法确认是取站点的顶级频道,还是取站点的所有频道.");
			}
//			System.out.println("获取频道时的sql语句=" + sql);
			db.executeSelect(sql);
			List channels = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(i, "CHANNEL_ID"));
				channel.setName(db.getString(i, "NAME"));
				channel.setDisplayName(db.getString(i, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(i, "PARENT_ID"));
				channel.setChannelPath(db.getString(i, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(i, "CREATEUSER"));
				channel.setCreateTime(db.getDate(i, "CREATETIME"));
				channel.setOrderNo(db.getInt(i, "ORDER_NO"));
				channel.setSiteId(db.getLong(i, "SITE_ID"));
				channel.setStaus(db.getInt(i, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(i, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(i, "DETAIL_TPL_ID"));
				channel.setWorkflow(db.getInt(i, "WORKFLOW"));
				channel.setOutlineIsDynamic(db
						.getInt(i, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(i, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db
						.getInt(i, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(i, "DOC_PROTECT"));
				channel.setNavigator(db.getInt(i,"ISNAVIGATOR")==1?true:false);
				channel
						.setWorkflowIsFromParent(db
								.getInt(i, "PARENT_WORKFLOW"));			
				String[] pubFileNames =  StringOperate.getFileNameAndExtName(db.getString(i, "pub_file_name"));
				channel.setPubFileName(db.getString(i, "pub_file_name"));
				channel.setPubFileNameSuffix(pubFileNames[1]); 
				
				channel.setOutlinepicture(db.getString(i,"OUTLINEPICTURE"));
				channel.setPageflag(db.getInt(i,"PAGEFLAG"));
				channel.setIndexpagepath(db.getString(i,"INDEXPAGEPATH"));
				channel.setCommentTemplateId(db.getInt(i,"COMMENT_TEMPLATE_ID"));
				channel.setCommentPagePath(db.getString(i,"COMMENTPAGEPATH"));
				channels.add(channel);
			}
			return channels;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("发生sql异常,无法返回频道信息.异常信息为:"
					+ e.getMessage());
		}
	}

	public List getSupportedSitePublishStatus(String siteId)
			throws SiteManagerException {
		// TODO 优先级:低
		throw new SiteManagerException(
				"getSupportedSitePublishStatus(String siteId)还没有实现");
	}

	public List getEnablePublishStatus(String siteId)
			throws SiteManagerException {
		// TODO 优先级:低
		throw new SiteManagerException(
				"getEnablePublishStatus(String siteId)还没有实现");
	}

	public Template getIndexTemplate(String siteId) throws SiteManagerException {
		// TODO 优先级:低
		try
		{
			TemplateManager templateManager = new TemplateManagerImpl();
			
			String templateid = String.valueOf(getSiteInfo(siteId).getIndexTemplateId());
			Template tpl = templateManager.getTemplateInfo(templateid);
			return tpl;
		
		}catch(TemplateManagerException e)
		{			
			throw new SiteManagerException("getIndexTemplate():"+e.getMessage());
		}		
	}

	public boolean testFtpLink(String ip, String user, String pwd, short port)
			throws SiteManagerException {
		// TODO 优先级:低

		
		throw new SiteManagerException("测试FTP功能尚未实现.");
	}

	private void fillSiteInfo(Site site, int rowNum, DBUtil db)
			throws SQLException {
		site.setSiteId(db.getLong(rowNum, "SITE_ID"));
		site.setName(db.getString(rowNum, "NAME"));
		site.setSecondName(db.getString(rowNum, "SECOND_NAME"));
		site.setParentSiteId(db.getLong(rowNum, "MAINSITE_ID"));
		site.setSiteDesc(db.getString(rowNum, "SITEDESC"));
		site.setDbName(db.getString(rowNum, "DBNAME"));
		site.setSiteDir(db.getString(rowNum, "SITEDIR"));
		site.setStatus(db.getInt(rowNum, "STATUS"));
		site.setFtpIp(db.getString(rowNum, "FTPIP"));
		site.setFtpPort(db.getShort(rowNum, "FTPPORT"));
		site.setFtpUser(db.getString(rowNum, "FTPUSER"));
		site.setFtpPassword(db.getString(rowNum, "FTPPASSWORD"));
		site.setFtpFolder(db.getString(rowNum, "FTPFOLDER"));
		site.setWebHttp(db.getString(rowNum, "WEBHTTP"));
		site.setSiteOrder(db.getInt(rowNum, "SITEORDER"));
		site.setCreateTime(db.getDate(rowNum, "CREATETIME"));
		site.setCreateUser(db.getLong(rowNum, "CREATEUSER"));
		site.setPublishDestination(db.getInt(rowNum, "PUBLISHDESTINATION"));
		site.setIndexFileName(db.getString(rowNum, "INDEXFILENAME"));
		site.setIndexTemplateId(db.getInt(rowNum, "INDEXTEMPLATE_ID"));
		site.setWorkflow(db.getInt(rowNum, "FLOW_ID"));
		site.setWorkflowIsFromParent(db.getInt(rowNum, "PARENT_WORKFLOW"));
		site.setLocalPublishPath(db.getString(rowNum, "LOCALPUBLISHPATH"));
		site.setDistributeManners(db.getString(rowNum, "DISTRIBUTEMANNERS"));
	}
	
	/**
	 * 分发方式数组
	 * 以逗号分割的数字值:0表示html,1表示rss，2表示mail,缺省为0
	 */
	public int[] getSiteDistributeManners(String siteId)
			throws SiteManagerException {
		try {
			Site site = CMSUtil.getSiteCacheManager().getSite(siteId);
		
			String[] distributeMannersStr = site.getDistributeManners().split(",");
			int[] distributeManners = new int[distributeMannersStr.length];
			for(int i=0;i<distributeMannersStr.length;i++)
			{
				distributeManners[i] = Integer.parseInt(distributeMannersStr[i]);
			}
			return distributeManners;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new int[] {0};
	}
	
	/**
	 * 定义发布目的地
	 * 0:本地1:远程2:远程本地
	 */
	public boolean[] getSitePublishDestination(String siteId) throws SiteManagerException
	{
		
		
		Site site = getSiteInfo(siteId);
		int local2ndRemote = site.getPublishDestination();
		
		
		
		return getSitePublishDestination(local2ndRemote);
	}
	
	/**
	 * 定义发布目的地
	 * 0:本地1:远程2:远程本地
	 */
	public static boolean[] getSitePublishDestination(int local2ndRemote) throws SiteManagerException
	{
		boolean[] publishDestination = new boolean[2];
		
//		Site site = getSiteInfo(siteId);
//		int local2ndRemote = site.getPublishDestination();
		
		if(local2ndRemote==0)
		{
			publishDestination[0] = true;
			publishDestination[1] = false;
		}
		if(local2ndRemote==1)
		{
			publishDestination[0] = false;
			publishDestination[1] = true;
		}
		if(local2ndRemote==2)
		{
			publishDestination[0] = true;
			publishDestination[1] = true;
		}
		
		return publishDestination;
	}

	public List getFlowInfo(String siteId) throws SiteManagerException {
		if (siteId == null || siteId.trim().length() == 0) {
			throw new SiteManagerException("没有站点id,无法站点的流程信息");
		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select ID,NAME from table(F_SITE_FLOW('" + siteId
					+ "'))";
			db.executeSelect(sql);
			if (db.size() > 0) {
				List flowInfo = new ArrayList(2);
				flowInfo.add(db.getString(0, "ID"));
				flowInfo.add(db.getString(0, "NAME"));
				return flowInfo;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("发生sql异常,无法返回流程信息.异常信息为:"
					+ e.getMessage());
		}
	}
	
	public String getSiteAbsolutePath(String siteId)throws SiteManagerException{
		if(siteId == null||siteId.trim().length()==0){
			return null;
		}
		String sitedir = null;
		try {
			DBUtil db = new DBUtil();
			String sql = "select sitedir from td_cms_site where site_id = '"+siteId+"'";
			db.executeSelect(sql);
			if (db.size() > 0) {
				sitedir = db.getString(0,0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("取站点的[sitedir]信息时发生sql异常,描述为:"+ e.getMessage());
		}
		if(sitedir==null || sitedir.trim().length()==0){
			return null;
		}
		String approot = CMSUtil.getAppRootPath();
		String contextpath = CMSUtil.getCMSContextPath();
		File f = new File(approot,contextpath);
		String siteroot = CMSUtil.getWebSiteRootPath();
		f = new File(f.getAbsolutePath(),siteroot);
		f = new File(f.getAbsolutePath(),sitedir);
		return f.getAbsolutePath();
	}
	public static void main(String[] args)throws SiteManagerException{
		SiteManager sm = new SiteManagerImpl();
		System.out.println(sm.getSiteAbsolutePath("148"));
	}
	
	public List getSiteList(String sql) throws SiteManagerException{
		DBUtil db = new DBUtil();
		List list = new ArrayList();
		
		try{
			db.executeSelect(sql);
			for(int i=0;i<db.size();i++){
				Site site = new Site();	
				String id = String.valueOf(db.getInt(i,"site_id")).toString();
				site.setSiteId(Long.parseLong(id));
				site.setName(db.getString(i,"name"));
				list.add(site);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SiteManagerException(e.getMessage());
		}
		return list;
	}
	
	/**
	 * 是否设置了首页模板
	 * @param siteId
	 * @return
	 * @throws SiteManagerException
	 */
	public boolean hasSetIndexTemplate(String siteId) throws SiteManagerException{
		String sql = "select 1 from td_cms_site a " +
					"inner join td_cms_template b "+
					"on a.indextemplate_id = b.template_id and b.type = '0' "+
					"where to_char(a.site_id)='"+siteId+"'";
	DBUtil db = new DBUtil();
	try {
		db.executeSelect(sql);
		if (db.size() > 0) {
			return true;
		}
	} catch (Exception e) {
		e.printStackTrace();
		throw new SiteManagerException(e.getMessage());
	}
	return false;
	}
	
	
	public List getSiteAllRuningList(AccessControl accessControl)
	{
		DBUtil db = new DBUtil();
		
		List list = new ArrayList();
		
		String sql ="select site_id,name,SITEDIR from td_cms_site where status=0 order by site_id,SITEORDER";
		try{
		
			
			//如果当前登陆用户是系统管理员，显示所有开通站点
			if(accessControl.isAdmin()){
				
				db.executeSelect(sql);
				if(db.size()>0)
				{
					for(int i=0;i<db.size();i++){
						Site site = new Site();	
						String id = String.valueOf(db.getInt(i,"site_id")).toString();
						site.setSiteId(Long.parseLong(id));
						site.setName(db.getString(i,"name"));
						site.setSiteDir(db.getString(i,"SITEDIR"));
						list.add(site);
					}
				}
			}
			//如果当前登陆用户不是系统管理员，显示该用户有权限并且是开通站点
			else
			{
				db.executeSelect(sql);
				for(int k=0;k<db.size();k++)
				{
					if(accessControl.checkPermission(db.getString(k,"site_id"),AccessControl.WRITE_PERMISSION,AccessControl.SITE_RESOURCE))					
					{	
							Site site = new Site();	
							String id = String.valueOf(db.getInt(k,"site_id")).toString();
							site.setSiteId(Long.parseLong(id));
							site.setName(db.getString(k,"name"));
							site.setSiteDir(db.getString(k,"SITEDIR"));
							list.add(site);
						
					}
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return list;
	}
	public List getSiteAllRuningList(boolean isAdmin,String userId) throws SiteManagerException{
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		List list = new ArrayList();
		
		String sql ="select site_id,name,SITEDIR from td_cms_site where status=0 order by site_id,SITEORDER";
		try{
		
			
			//如果当前登陆用户是系统管理员，显示所有开通站点
			if(isAdmin){
				
				db.executeSelect(sql);
				if(db.size()>0)
				{
					for(int i=0;i<db.size();i++){
						Site site = new Site();	
						String id = String.valueOf(db.getInt(i,"site_id")).toString();
						site.setSiteId(Long.parseLong(id));
						site.setName(db.getString(i,"name"));
						site.setSiteDir(db.getString(i,"SITEDIR"));
						list.add(site);
					}
				}
			}
			//如果当前登陆用户不是系统管理员，显示该用户有权限并且是开通站点
			else
			{
				db.executeSelect(sql);
				for(int k=0;k<db.size();k++)
				{
					
					String sqlselect ="select t.* from td_cms_site t where t.site_id in( "+
									  " select res_id from td_sm_roleresop where op_id='write' "+
									  " and role_id ='"+ userId +"' and types='user' "+
									  " and res_id ='"+ db.getInt(k,"site_id")+"' and restype_id ='site'"+
									  " union "+
									  " select res_id from td_sm_roleresop where role_id in "+
									  " (select role_id from td_sm_userrole where user_id ="+ userId+ ") "+
									  " and op_id='write' and types='role' and res_id ='"+ db.getInt(k,"site_id")+"'" +
									  " and restype_id ='site')";
					//System.out.println(sqlselect);
					db1.executeSelect(sqlselect);
					if(db1.size()>0)
					{
						for(int i=0;i<db1.size();i++){
							Site site = new Site();	
							String id = String.valueOf(db1.getInt(i,"site_id")).toString();
							site.setSiteId(Long.parseLong(id));
							site.setName(db1.getString(i,"name"));
							site.setSiteDir(db1.getString(i,"SITEDIR"));
							list.add(site);
						}
					}
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new SiteManagerException(e.getMessage());
		}
		return list;
		
	}
	/**
	 * 保存用户登陆默认站点
	 */
	public void defaultSite(String siteId,String userId) throws SiteManagerException{
		DBUtil db = new DBUtil();
		String sql = "insert into td_cms_siteuser(site_Id,user_Id) values("+siteId+","+userId+")";
		String sqlselect = "select count(*) from td_cms_siteuser where " +
						   " user_id="+userId+"";
		String sqldel ="delete from td_cms_siteuser where  user_id="+userId+"";
		try {
			if(siteId.equals(""))
			{
				db.executeDelete(sqldel);
			}
			else
			{
				db.executeSelect(sqlselect);
				if(db.getInt(0,0)>0){
					db.executeDelete(sqldel);
					db.executeInsert(sql);
				}
				else
				{
					db.executeInsert(sql);
				}
			}
		
		} catch (SQLException e) {
			
			e.printStackTrace();
			throw new SiteManagerException(e.getMessage());
		}
		
	}
	/**
	 * 取当前用户默认登陆站点
	 */
	public String userDefaultSite(String userId) throws SiteManagerException{
		if(userId == null || userId.equals(""))
			return "";
		String siteId ="";
		DBUtil db = new DBUtil();
		String sqlselect = "select site_id from td_cms_siteuser where " +
						   " user_id="+userId+"";
		try {
			db.executeSelect(sqlselect);
			if(db.size()>0){
				siteId = db.getInt(0,"site_id")+"";
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
			throw new SiteManagerException(e.getMessage());
		}
		return siteId;
	}
	/**
	 * 判断站点有没有关联模板
	 */
	public boolean hasChannelOfTemplate(String siteId) throws SiteManagerException{
		if (siteId == null || siteId.trim().length() == 0) {
			throw new SiteManagerException("没有提供站点id,无法判断站点下是否有频道.");
		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select count(*) from TD_CMS_TEMPLATE a,TD_CMS_SITE_TPL b where " +
					" a.TEMPLATE_ID =b.TEMPLATE_ID and b.site_id ="+ siteId;
					
//			System.out.println("=========="+sql);
			db.executeSelect(sql);
			if (db.getInt(0,0) > 0) {
				
					return true;
				
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("发生sql异常,无法判断站点下是否有模板.异常信息为:"
					+ e.getMessage());
		}
	}
	
	/**
	 * 获取站点对应的模板列表
	 */
	public List getTemplatesOfSite(String siteId) throws SiteManagerException{
		if (siteId == null || siteId.trim().length() == 0) {
			throw new SiteManagerException("没有提供站点id,无法返回模板信息.");
		}
		try {
			DBUtil db = new DBUtil();
			String sql;
		
				sql = "select a.TEMPLATE_ID,a.name from TD_CMS_TEMPLATE a,TD_CMS_SITE_TPL b where " +
				" a.TEMPLATE_ID =b.TEMPLATE_ID and b.site_id ="+ siteId;
		
		
			db.executeSelect(sql);
			List tpls = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Template template = new Template();
				int id = db.getInt(i,"TEMPLATE_ID");
				template.setTemplateId(id);
				template.setName(db.getString(i,"name"));
				tpls.add(template);
			}
			return tpls;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SiteManagerException("发生sql异常,无法返回模板信息.异常信息为:"
					+ e.getMessage());
		}
	}
	
	/**
	 * 取得该站点下所有有权限的用户id
	 * @author xinwang.jiao
	 * @param siteId
	 * @return String[] String[0] 为userids,String[1] 为usernames，String[2] 为“user”
	 * @throws ChannelManagerException
	 */
	public String[] getUsersOfSite(String siteId) throws SiteManagerException
	{
		String[] str = new String[3];
		String userIds = "";
		String userNames = "";
		String userOrRole = "";
		DBUtil db = new DBUtil();
		String sql = "select distinct a.role_id,b.user_realname from td_sm_roleresop a " +
				"inner join td_sm_user b on a.role_id = b.user_id " +
				"where (a.restype_id = 'site' or a.restype_id = 'site.channel' or a.restype_id = 'site.doc' or a.restype_id = 'sitefile' " +
				" or a.restype_id = 'sitetpl' or a.restype_id = 'siteview') " +//siteview 是否是一个资源类型，有待考证
				" and a.types='user' and a.res_id = '" + siteId + "'";
		try
		{
			db.executeSelect(sql);
			if(db.size()>0)
			{
				for(int i = 0; i < db.size(); i++)
				{
					userIds += db.getString(i,"role_id") + ",";
					userNames += db.getString(i,"user_realname") + ",";
					userOrRole += "user" + ",";
				}
			}
			str[0] = userIds;
			str[1] = userNames;
			str[2] = userOrRole;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new SiteManagerException(e.getMessage());
		}
		return str;
	}
	/**
	 * 取得该站点下所有有权限的角色id
	 * @author xinwang.jiao
	 * @param siteId
	 * @return String[] String[0] 为角色id,String[1] 为角色名称，String[2] 为“role”
	 * @throws SiteManagerException
	 */
	public String[] getRolesOfSite(String siteId) throws SiteManagerException
	{
		String[] str = new String[3];
		String userIds = "";
		String userNames = "";
		String userOrRole = "";
		DBUtil db = new DBUtil();
		String sql = "select distinct a.role_id,b.role_name from td_sm_roleresop a " +
			"inner join td_sm_role b on a.role_id = b.role_id " +
			"where (a.restype_id = 'site' or a.restype_id = 'site.channel' or a.restype_id = 'site.doc' or a.restype_id = 'sitefile' " +
			" or a.restype_id = 'sitetpl' or a.restype_id = 'siteview') " +
			" and a.types='role' and a.res_id = '" + siteId + "'";
		try
		{
			db.executeSelect(sql);
			if(db.size()>0)
			{
				for(int i = 0; i < db.size(); i++)
				{
					userIds += db.getString(i,"role_id") + ",";
					userNames += db.getString(i,"role_name") + ",";
					userOrRole += "role" + ",";
				}
			}
			str[0] = userIds;
			str[1] = userNames;
			str[2] = userOrRole;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new SiteManagerException(e.getMessage());
		}
		return str;
	}

	/**
	 * 判断当前用户是不是站点管理员
	 */
	public boolean hasSiteManager(String siteId,String userId) throws SiteManagerException{
		boolean b = false;
		String sql = "select name from td_cms_site where site_id ="+siteId;
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				String rolename = db.getString(0,"name")+"站点管理员";
				String  sqlrole ="select count(*) from td_sm_userrole where role_id in "+
					"(select role_id from td_sm_role where role_name='"+ rolename +"') and user_id ="+userId;
				db.executeSelect(sqlrole);
				if(db.getInt(0,0)>0)
				{
					b = true;
				}
			}
		} catch (Exception e) {
		e.printStackTrace();
		throw new SiteManagerException(e.getMessage());
		}
		return b;
	}
	
	/**
	 * 获取内容管理下的应用列表
	 * @param parentPath
	 * @return 
	 * SiteManagerImpl.java
	 * @author: ge.tao
	 */
	public List getSubItems(String parentPath){
        List list = new ArrayList();
        ModuleQueue modules = Framework.getInstance().getSubModules(parentPath);        
        ItemQueue items = Framework.getInstance().getSubItems(parentPath);
       
        for(int i = 0;  i < modules.size(); i++){
            Module module = modules.getModule(i);
            //递归
            //log.warn("next path:"+parentPath + "/" + module.getId() + "$module");
            list.add(module);
            getSubItems(list,module.getPath());
        }
    
        for(int j = 0;  j < items.size(); j++){
            Item item = items.getItem(j);
            //log.warn("list add item:"+item.getId());
            list.add(item);
        }
                   
        return list;
    }
	
	
	/**
	 * 获取内容管理下的应用列表
	 * @param parentPath
	 * @return 
	 * SiteManagerImpl.java
	 * @author: ge.tao
	 */
	public List getSubItems(List list,String parentPath){
        
        ModuleQueue modules = Framework.getInstance().getSubModules(parentPath);        
        ItemQueue items = Framework.getInstance().getSubItems(parentPath);
       
        for(int i = 0;  i < modules.size(); i++){
            Module module = modules.getModule(i);
            //递归
            //log.warn("next path:"+parentPath + "/" + module.getId() + "$module");
            list.add(module);
            getSubItems(list,parentPath + "/" + module.getId() + "$module");
        }
    
        for(int j = 0;  j < items.size(); j++){
            Item item = items.getItem(j);
            //log.warn("list add item:"+item.getId());
            list.add(item);
        }
                   
        return list;
    }
	
	/**
	 * 新建站点 设置站点下的应用
	 * @param site
	 * @param request 
	 * SiteManagerImpl.java
	 * @author: ge.tao
	 */
	public void setAppInSite(Site site,HttpServletRequest request){
		if(site == null) return;
		String[] params = request.getParameterValues("appId");
		long siteId = site.getSiteId();
		
		DBUtil db = new DBUtil();
		
			
		try {
			//重新设置时,删除站点引用的应用记录 TD_CMS_SITEAPPS			
			String deleteApp = "delete from TD_CMS_SITEAPPS where site_id=" + siteId;
			db.addBatch(deleteApp);
			
			if(params != null){
			    for(int i=0;i<params.length;i++){
			    	String param = params[i];
			    	String[] p = param.split("\\^");
			    	if(p.length<=1){
			    		continue;
			    	}
			    	if("null".equalsIgnoreCase(p[0]) || "null".equalsIgnoreCase(p[1])){
			    		continue;
			    	}
			    	StringBuffer sql = new StringBuffer();
					sql.append("insert into TD_CMS_SITEAPPS(SITE_ID,APP_ID,APP_PATH) (")
					.append("select ").append(siteId).append(" as SITE_ID ")
					.append(",'").append(p[1]).append("' as APP_ID ")
					.append(",'").append(p[0]).append("' as APP_PATH ")
					.append("from dual where not exists")
					.append("(select 1 from TD_CMS_SITEAPPS where SITE_ID=")
					.append(siteId)
					.append("and APP_ID='").append(p[1]).append("' )) ");
					//System.out.println("插入站点的应用sql---------------------"+sql.toString());
					db.addBatch(sql.toString());
			    }	
			}
			db.executeBatch();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取站点下已经设置了的应用列表
	 * @param siteId
	 * @return 
	 * SiteManagerImpl.java
	 * @author: ge.tao
	 */
	public List getSetedAppInSite(String siteId){
		List list = new ArrayList();
		StringBuffer sql = new StringBuffer()
		.append("select APP_PATH || '^' || APP_ID as id from TD_CMS_SITEAPPS where ")
		.append("SITE_ID=").append(siteId);
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql.toString());
			for(int i=0;i<db.size();i++){
				list.add(db.getString(i,"id"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
		
	}
	
}