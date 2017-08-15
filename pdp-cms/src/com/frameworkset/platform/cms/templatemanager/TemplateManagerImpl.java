package com.frameworkset.platform.cms.templatemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.DataFormatUtil;
import org.frameworkset.util.TimeUtil;
import org.htmlparser.util.ParserException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.orm.adapter.DB;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.container.TmplateExport;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.driver.htmlconverter.CMSTemplateLinkTable;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkTable;
import com.frameworkset.platform.cms.driver.i18n.CmsEncoder;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.platform.config.model.Operation;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.util.EventUtil;
import com.frameworkset.util.ListInfo;

/**
 * 模板管理
 * @author GCQ
 * 日期:Dec 19, 2006
 * 版本:1.0
 * 版权所有:三一重工
 * modify:2006.12.25
 */

public class TemplateManagerImpl implements TemplateManager{ 
	private static Logger log = LoggerFactory.getLogger(TemplateManagerImpl.class);
    
    /**
	 * 创建公共模板
	 * @param 输入:模板对象template
	 *  返回值int:成功时为模板id;失败时为:0 功能:将模板基本信息写入数据库表(td_cms_template)中
	 */
	public int createTemplate(Template template, int siteid)throws TemplateManagerException {

		PreparedDBUtil preDBUtil = new PreparedDBUtil();
		int ret = 0;
		String sql = "";
		TransactionManager tm = new TransactionManager(); 
		try {
			tm.begin();
			sql = "insert into TD_CMS_TEMPLATE("
					+ "NAME ,DESCRIPTION, HEADER, TEXT ,TYPE,CREATEUSER, CREATETIME, " +
							"INC_PUB_FLAG,PERSISTTYPE, TEMPLATEFILENAME, TEMPLATEPATH, TEMPLATE_STYLE,TEMPLATE_ID)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			
			String template_id = preDBUtil.getNextStringPrimaryKey("TD_CMS_TEMPLATE");
			
			preDBUtil.preparedInsert(sql);//模板信息入库
			String templateName = template.getName();
			if(templateName==null || templateName.trim().length()==0){
				throw new TemplateManagerException("模板名字不能为空!");
			}
			preDBUtil.setString(1, templateName);//模板名称
			String desc = template.getDescription();
			if(desc==null || desc.trim().length()==0){
				throw new TemplateManagerException("请简短描述下模板!");
			}
			preDBUtil.setString(2, desc); // 模板描述
			preDBUtil.setString(3, template.getHeader());
			preDBUtil.setClob(4, template.getText(), "TEXT"); // 模板内容
			preDBUtil.setInt(5, template.getType()); // 模板类型
			preDBUtil.setLong(6, template.getCreateUserId());
			preDBUtil.setTimestamp(7, new Timestamp(new Date().getTime())); // 模板文件名
			preDBUtil.setInt(8, template.getIncreasePublishFlag());
			preDBUtil.setInt(9, template.getPersistType());
			preDBUtil.setInt(12,template.getStyle());//模板风格编号
			
			preDBUtil.setString(13,template_id) ;
			
			String fileName=template.getTemplateFileName();
			String path=template.getTemplatePath();
			//如果是存储在文件系统中的
			if(template.getPersistType()==1){
				if(fileName==null || fileName.trim().length()==0){
					throw new TemplateManagerException("模板存储在文件系统中,但是没有提供模板的主文件名!");
				}
				if(path!=null&&path.trim().length()!=0){
					path = path.replace('\\','/');
					if(path.startsWith("/")){
						path = path.substring(1);
					}
					if(path.endsWith("/")){
						path = path.substring(0,path.length()-1);
					}
					if(path.replace('/',' ').trim().length()==0){
						path = "";
					}
				}
				
			}
			preDBUtil.setString(10,fileName);
			preDBUtil.setString(11,path);
			//ret = ((Long) preDBUtil.executePrepared()).intValue(); // 返回主键ID
			preDBUtil.executePrepared() ;
			ret = Integer.parseInt(template_id) ; //设置主键信息
			
			//新增模板的时候默认将当前用户控制站点权限加上
		      String userId = String.valueOf(template.getCreateUserId());
		      ResourceManager resManager = new ResourceManager();
			  RoleManager roleManager = SecurityDatabase.getRoleManager();
			  Operation op;
			  List list = resManager.getOperations("template");
			  boolean sendevent  = false;
			  if(!userId.equals("1"))
			  {
				  for(int k=0;k<list.size();k++){
						op = (Operation)list.get(k);
						roleManager.storeRoleresop(op.getId(),ret+"",userId,"template",templateName,"user",false);
						sendevent = true;
				  }
			  }
			
			//新建模板将模板权限赋给站点管理员
			String  siteId= template.getSiteId()+"";
			DBUtil db = new DBUtil();
			db.executeSelect("select name from td_cms_site where site_id="+siteId);
			String rolename = db.getString(0,"name") +"站点管理员";
			db.executeSelect("select role_id from td_sm_role where role_name='"+ rolename +"'");
			if(db.size()>0)
			{
				    String roleId = db.getString(0,"role_id");
					op = new Operation();
					for(int i=0;i<list.size();i++){
						op = (Operation)list.get(i);
						try {
							
							roleManager.storeRoleresop(op.getId(),ret+"",roleId,"template",templateName,"role",false);
							sendevent = true;
						}catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					
			}
			SiteTemplateMap sitetemplateM = new SiteTemplateMap();
			sitetemplateM.createSiteTempateMap(ret, siteid);
			tm.commit();
			if(sendevent)
				EventUtil.sendRESOURCE_ROLE_INFO_CHANGEEvent();
		} catch (Exception e) {
			ret = 0; // 建模板出现异常
			throw new TemplateManagerException(e.getMessage());
		}
		finally
		{
			tm.release();
		}
		return ret;
	}
    /**
	 * 创建站点下的模板
	 * @param 输入:模板对象template,站点ID:siteid
	 *            返回值int:成功时为模板id;失败时为:0
	 *            功能:将模板基本信息写入数据库表(td_cms_template)中,与站点的对应关系写在td_cms_site_tpl中
	 */
    public int createTemplateofSite(Template template, int siteid)
			throws TemplateManagerException {

		
		int ret = createTemplate(template,   siteid);
		
		return ret;
	} 
    
    /**
     * 导入模板
     * @param template 模板对象,包含站点id
     * @param packagedTemplate 用zip格式打好了包的模板
     * @param overWrite 是否是重新导入
     * @throws TemplateManagerException
     */
    public void importTemplate(Template template,ZipInputStream packagedTemplate,boolean reImport)throws TemplateManagerException{
    	if(template==null){
    		throw new TemplateManagerException("没有提供模板基本信息,无法导入模板.");
    	}
    	if(packagedTemplate==null){
    		throw new TemplateManagerException("没有上传文件,无法导入.");
    	}
    	int siteId = template.getSiteId();
    	String sitepath = null;
    	try {
			sitepath = new SiteManagerImpl().getSiteAbsolutePath(""+siteId);
		} catch (SiteManagerException e) {
			e.printStackTrace();
			throw new TemplateManagerException(e.getMessage());
		}
		File topTemplateFolder = new File(sitepath,"_template");
		String uri = template.getTemplatePath();
//		if(uri==null || uri.trim().length()==0){
//			throw new TemplateManagerException("请提供模板存储在模板根目录下的相对路径!");
//		}
		File currTemplateFolder = new File(topTemplateFolder.getAbsolutePath(),uri);
		if(!reImport && currTemplateFolder.exists() && !uri.trim().equals("")){
			throw new TemplateManagerException("文件夹已经存在,请重新指定模板所存路径!");
		}else{
			currTemplateFolder.mkdirs();
			try {
				FileUtil.upzip(packagedTemplate, currTemplateFolder.getAbsolutePath());
			} catch (ZipException e) {
				e.printStackTrace();
				throw new TemplateManagerException("解压缩模板文件包发生错误!");
			} catch (IOException e) {
				e.printStackTrace();
				throw new TemplateManagerException("解压缩模板文件包发生IO错误!!");
			}
		}
		if(!reImport){
			createTemplateofSite(template,template.getSiteId());
		}
    }
   
    /**
	 * 功能:删除模板表中的记录 输入:模板ID 输出:ture:成功;false失败
	 * 
	 * @since 2006.12
	 * @param template
	 */
    public boolean deleteTemplate(int templateid) throws TemplateManagerException
    {
    	boolean ret=false;
    	
        DBUtil conn=null;  
 	   try {
            //删除模板附件表，以及附件（模板文件及附件放在文件系统中.）
 		    //conn=new DBUtil();
 		    //conn.executeDelete("delete from td_cms_templateattach where template_id="+templateid+"");
 		    //删除模板信息表中相应记录
 		    conn=new DBUtil();
 		    conn.executeDelete("delete from td_cms_template where template_id="+templateid+" ");
 		    //删除此模板在文件系统中的模板文件
 		   // File f =new File(templatefilepath1+"/"+templateid);
            //f.delete();
 		    String sql ="delete from td_sm_roleresop where res_id='"+ templateid +"' and restype_id='template'";
 		    conn.executeDelete(sql);
 		    ret=true;
 		} catch (Exception e) {
 			ret=false;
 			System.out.print("删除模板出错!"+e);
 			throw new TemplateManagerException(e.getMessage());
 		}  
        return ret;    
    }
    /**
	 * 删除文件系统中的模板
	 * @param templateid 模板id
	 * @param tsiteId 站点id
	 * @param userId 用户id
	 * TemplateManager.java
	 * @author: zhizhong.ding
	 */
	public void deleteTemplateFile(int templateid, String siteId,String userId)
			throws TemplateManagerException {
		TemplateManager tm = new TemplateManagerImpl();
		Template template = tm.getTemplateInfo(templateid + "");

		String uri = template.getTemplatePath() + "\\"
				+ template.getTemplateFileName();
		
		FileManager fm = new FileManagerImpl();
		fm.deleteFiles(siteId, uri, userId);

	}

    
    /**
     * 功能:删除站点下的模板
     * 输入:模板ID,站点ID
     * 输出:true:成功;false:不成功         
     */ 
    public boolean deleteTemplateofSite(int templateid,int siteid) throws TemplateManagerException
    {
       boolean ret=false;
       try{ 
    	   
          TemplateManagerImpl templateM=new TemplateManagerImpl();
          SiteTemplateMap sitetemplateM=new SiteTemplateMap();
         if(templateM.templateisUsed(templateid)){//站点下的模板是否被引用
    	   ret=false; 
         }else{
            //删除站点和模板的映射关系
        	 if(sitetemplateM.deleteSiteTempateMap(templateid,siteid)){  
             // 删除模板表中的记录    	   	    
    	   	    if(templateM.deleteTemplate(templateid)){
    	            ret=true;  
    	   	    }else{
    	   	    	ret=false;
    	   	    }
    	      }else{
    	        ret=false;
    	      }
         }
       }catch (Exception e) {
 			ret=false;
 			System.out.print("删除站点下模板出错!"+e);
 			throw new TemplateManagerException(e.getMessage());
 		}  
       
       return ret;
    }
    /**
     * 功能:模板是否被站点、频道、文档引用
     * 输入:模板ID
     * 输出:true:被引用;false:没有引用         
     */ 
    public boolean templateisUsed(int templateid) throws TemplateManagerException
    {
    	boolean ret=true;
    	int templatetype=0;
    	DBUtil conn = null; 
    	String  sql = "";
  	    
    	try{
    	//根据模板ID读者模板类型
    		conn=new DBUtil();
    	sql = "select * from td_cms_template where template_id="+templateid+" ";
    	conn.executeSelect(sql);
    	if (conn.size()>0){
    		templatetype=conn.getInt(0,"TYPE");    		
    	}
    	//判断是否被引用	
    	if (templatetype==0){//为首页模板
    	  	TemplateManagerImpl templageManagerM = new TemplateManagerImpl();
    	  	Template template = templageManagerM.getIndexTemplateOfSite("1");
    		
    	  sql="select * from td_cms_site where indextemplate_id="+templateid+" ";
    	  conn=new DBUtil();
    	  conn.executeSelect(sql);
    	  if (conn.size()>0){ 
      		ret=true;    		
      	   }else{
      	    ret=false;
      	   }
    	}else if(templatetype==1){//为概览模板
    	  sql="select * from td_cms_channel where outline_tpl_id="+templateid+" ";
    	  conn=new DBUtil();
    	  conn.executeSelect(sql);
    	  if (conn.size()>0){
        		ret=true;
        		 throw new TemplateManagerException("模板被引用！");
        	   }else{
        	    ret=false;
        	   
        	   }
    	}else if(templatetype==2){//为细览模板,文档表是否引用模板不做检查。
    	  sql="select * from td_cms_channel where DETAIL_TPL_ID="+templateid+" ";
    	  conn=new DBUtil();
    	  conn.executeSelect(sql);
    	  if (conn.size()>0){
        		ret=true;  
        		throw new TemplateManagerException("模板被引用！");
        	   }else{
        	    ret=false;
        	    
        	   }
    	}else{//模板类型错误,可以删除
    		ret=false;
    		
    	}
    	}catch(Exception e){
		     ret=true;
		     System.out.print("判断模板是否被引用出错!"+e);
		     throw new TemplateManagerException(e.getMessage());		   
	   } 
    	return ret;
    }
    /**
     * 功能:更新模板表信息
     * 输入:模板对象template
     * 输出:true:成功;false:失败    
     * @param template
     * @roseuid 45864967030D
     */
    public boolean updateTemplate(Template template)throws TemplateManagerException {
		PreparedDBUtil preDBUtil = new PreparedDBUtil();
		boolean ret = false;
		try {

			String sql = "update td_cms_template set "
					+ "NAME=?,description=?,header=?,text=?,TYPE=?,PERSISTTYPE=?,TEMPLATEFILENAME=?,TEMPLATEPATH=?, TEMPLATE_STYLE=? "
					+ " where template_id=? ";
			String theURI = template.getTemplatePath();
			if(theURI!=null){
				theURI.replace('\\','/');
				if(theURI.endsWith("/")){
					theURI = theURI.substring(0,theURI.length()-1);
				}
				if(theURI.startsWith("/")){
					theURI = theURI.substring(1);
				}
				if(theURI.replace('/',' ').trim().length()==0){
					theURI = "";
				}
				template.setTemplatePath(theURI);
			}
			preDBUtil.preparedUpdate(sql); // 模板信息
			preDBUtil.setString(1, template.getName()); // 模板名称
			preDBUtil.setString(2, template.getDescription()); // 模板描述
			preDBUtil.setString(3, template.getHeader()); // 模板内容
			preDBUtil.setString(3, template.getHeader()); // 模板内容
			preDBUtil.setString(4, template.getText());// 动静态		
			preDBUtil.setInt(5, template.getType()); // 模板类型
			preDBUtil.setInt(6, template.getPersistType()); //存储类型
			preDBUtil.setString(7, template.getTemplateFileName());
			preDBUtil.setString(8,template.getTemplatePath());
			preDBUtil.setInt(9, template.getStyle()); // 模板风格
			preDBUtil.setInt(10,template.getTemplateId());//模板ID
			preDBUtil.executePrepared();

			ret = true;
		} catch (Exception e) {
			ret = false;
			System.out.print("更新模板信息出错!" + e);
			throw new TemplateManagerException(e.getMessage());
		}
		return ret;
	}
    
    
    /**
     * 功能:更新模板的所有信息,如果模板存储在文件系统中,将模板的内容写入文件
     * 输入:模板对象template
     * 输出:true:成功;false:失败    
     * @param template
     * @roseuid 45864967030D
     */
    
    public boolean updateAllInfoOfTemplate(Template template)throws TemplateManagerException {
		try {
			// 如果数据是存在文件系统
			if (template.getPersistType() == 1) {
				String sitepath;

				sitepath = new SiteManagerImpl().getSiteAbsolutePath(""	+ template.getSiteId());

				if (sitepath == null || sitepath.trim().length() == 0) {
					throw new TemplateManagerException("根据站点id["+ template.getSiteId() + "],找不到站点对应的路径!");
				}
				File templateRoot = new File(sitepath, "_template");
				
				File currTemplateFolder = null;
				String uri = template.getTemplatePath(); 
				if(uri != null && uri.trim().length()!=0){
					currTemplateFolder = new File(templateRoot.getAbsolutePath(),uri);
				}else{
					currTemplateFolder = new File(templateRoot.getAbsolutePath());
				}
				currTemplateFolder.mkdirs();
				
				//da.wei，200710191407，文件只读时不能改写文件，bug676
				File currTemplateFile = new File(currTemplateFolder.getAbsolutePath(), template.getTemplateFileName());
				if (currTemplateFile.exists()) {
					File old = new File(currTemplateFile.getParentFile().getAbsolutePath(), currTemplateFile.getName()+ System.currentTimeMillis() + ".bak");
//					currTemplateFile.renameTo(old);
					FileUtil.fileCopy(currTemplateFile.getAbsolutePath(), old.getAbsolutePath());
				}
//				currTemplateFile.createNewFile();
//				FileUtil.writeFile(currTemplateFile.getAbsolutePath(), template.getText());
				try{
					FileUtil.saveFile(currTemplateFile.getAbsolutePath(),template.getText(),CMSUtil.getCharset());
				}catch(Exception e){
					//文件只读时将不能访问					
					throw new TemplateManagerException("保存失败,请去掉文件的只读属性!");
				}
				
				template.setText("NULL");
				template.setHeader("NULL");
			}
		} catch (SiteManagerException e) {
//			e.printStackTrace();
			throw new TemplateManagerException(e.getMessage());
		} catch (IOException e) {
//			e.printStackTrace();
			throw new TemplateManagerException(e.getMessage());
		}
		this.updateTemplate(template);
		return true;
	}

    
    
   
    /**
	 * 功能:取某个模板详细信息,用于模板编辑 输入:模板ID 输出:list对象
	 * 
	 * @param templateid
	 * @roseuid 45864967035B
	 */
    
    public Template getIndexTemplateOfSite(String siteid) throws TemplateManagerException
    {
    	return null;
    }
    public Template getTemplateInfo(String templateid) throws TemplateManagerException
    {
// 	   List   Templatelist = null;
 	   PreparedDBUtil conn = new PreparedDBUtil(); 
 	   String  sql = "";	   
 	   try {
 		if(templateid == null || templateid.equals(""))
 			return null;
 		 sql = "select a.*,b.user_name from td_cms_template a inner join  td_sm_user b on a.createuser = b.user_id and template_id=?";
 		 conn.preparedSelect(sql);
 		 conn.setInt(1, Integer.parseInt(templateid));
 		 conn.executePrepared();
 		 if (conn.size()>0){
 		     Template templateobj=new Template();
   		     templateobj.setName     (conn.getString(0,"name"));       //模板名称
   		     templateobj.setDescription (conn.getString(0,"description"));    //模板描述
   		     templateobj.setText (conn.getString(0,"text"));     		    
   		     templateobj.setType     (conn.getInt   (0,"type")); 
   		     templateobj.setCreateTime(conn.getDate(0,"createtime"));
   		     templateobj.setCreateUserId(conn.getLong(0,"createuser"));	
   		     //templateobj.setCreateUserName(conn.getString(i,"user_name"));	 			 
   		     templateobj.setHeader       (conn.getString(0,"header")); 
   		     templateobj.setTemplateId  (conn.getInt(0,"template_id"));   
   		     templateobj.setIncreasePublishFlag(conn.getInt(0,"inc_pub_flag")); 	
   		     templateobj.setPersistType(conn.getInt(0,"PERSISTTYPE"));
   		     templateobj.setTemplateFileName(conn.getString(0, "TEMPLATEFILENAME"));
   		     templateobj.setTemplatePath(conn.getString(0,"TEMPLATEPATH")); 
   		     templateobj.setStyle(conn.getInt(0, "TEMPLATE_STYLE"));
 		     return templateobj;
 		  }
 		 else 
 		 {
 			 return null;
 		 } 	
 		
 		} catch (Exception e) {
 			log.error("取模板信息出错!",e);
 			return null;
 		} 
    }   
     
    /**取某个站点下的模板列表,用于站点模板维护
     * 输入:站点ID
     * 输出:列表对像list
     * @param 
     * @roseuid 45864967035B
     */ 
    public List getTemplateInfoListofSite(int siteid) throws TemplateManagerException
    {
    	List Templatelist = new ArrayList();
    	DBUtil conn = new DBUtil(); 
  	   	String  sql = "";	   
  	   	try {
  		
  			sql = "select t1.*,t3.user_name from td_cms_template t1 inner join td_cms_site_tpl t2 on t1.template_id=t2.template_id and t2.site_id="+siteid+" inner join td_sm_user t3 on t1.createuser = t3.user_id order by t1.type";
  			conn.executeSelect(sql);
  			if (conn.size()>0){
	  			for(int i=0;i<conn.size();i++)
	  			{   		
		  			Template templateobj=new Template();
		   			templateobj.setName     (conn.getString(i,"name"));       //模板名称
		   			templateobj.setDescription (conn.getString(i,"description"));    //模板描述
		   		 	templateobj.setText (conn.getString(i,"text"));     		    
		   			templateobj.setType     (conn.getInt   (i,"type")); 
		   			templateobj.setCreateTime(conn.getDate(i,"createtime"));
		   			templateobj.setCreateUserId(conn.getLong(i,"createuser"));	
		   			//templateobj.setCreateUserName(conn.getString(i,"user_name"));	 			 
		   			templateobj.setHeader       (conn.getString(i,"header")); 
		   			templateobj.setTemplateId  (conn.getInt(i,"template_id"));   
		   			templateobj.setIncreasePublishFlag(conn.getInt(i,"inc_pub_flag")); 
		   			templateobj.setPersistType(conn.getInt(i,"PERSISTTYPE"));
		   			templateobj.setTemplateFileName(conn.getString(i, "TEMPLATEFILENAME"));
		   			templateobj.setTemplatePath(conn.getString(i,"TEMPLATEPATH")); 
		   			templateobj.setStyle(conn.getInt(i,"TEMPLATE_STYLE")); 
		   			Templatelist.add(i,templateobj);
	  			}
  			}  		
  		} catch (Exception e) {
  			System.out.print("取站点模板信息出错!"+e);
  			throw new TemplateManagerException(e.getMessage());
  		}
  		return Templatelist;
    } 	

    /**
	 * 根据所有模板风格将某个站点下的所有模板信息分组显示到《select》标签内
	 * @param siteid
	 * @return
	 * *@throws TemplateManagerException
	 * @author: peng.yang
	 */
	public List groupByTemplateStyle(int siteid, int type) throws TemplateManagerException{
		String strOption = "";
		List list = new ArrayList();
		
		List templateList = getTemplateInfoListofSite(siteid,type);//获取某个站点下的所有模板信息
		TemplateStyleManager tsm = new TemplateStyleManagerImpl();//模板风格管理
		List tsList = tsm.getAllTemplateStyleIdAndName();//模板风格实体集合
		
		//开始进行分组
		for(int j=0;tsList!=null && j<tsList.size();j++){
			TemplateStyleInfo ts = (TemplateStyleInfo)tsList.get(j);
			strOption = "<OPTGROUP label='"+ts.getStyleName()+"' title='"+ts.getStyleName()+"'></OPTGROUP>";
			list.add(strOption);
			for(int i=0;templateList!=null && i<templateList.size();i++){
				Template t = (Template)templateList.get(i);
				//System.out.println("Style: "+ts.getStyleId().intValue()+"    t_Style: "+t.getStyle());
				//out.println("<option value=\""+t.getTemplateId()+"\">"+t.getName()+"</option>");
				if(ts.getStyleId().intValue()==t.getStyle()){
					strOption = "<option value=\"" + t.getTemplateId() + "\"" +
								" templatePath=\"" + t.getTemplatePath() + "\"  templateFileName=\"" + t.getTemplateFileName() + "\" >&nbsp;&nbsp;"
								+ t.getName() + "</option>";
					list.add(strOption);
				}
			}
		}
		
		return list;
	}
    /**
     * 取某个站点下的模板列表,用于模板信息缓冲（Template中包含了模板正文信息）
     * 输入:站点ID
     * 输出:列表对像list<Template>
     * @param 
     * add by xinwang.jiao
     */ 
    public List getTemplateListOfSite(String siteid) throws TemplateManagerException
    {
    	List templatelist = new ArrayList();
    	DBUtil conn = new DBUtil(); 
  	   	String  sql = "";	   
  	   	try {
  		
  			sql = "select t1.*,t3.user_name from td_cms_template t1 inner join td_cms_site_tpl t2 on t1.template_id=t2.template_id and t2.site_id="+siteid+" inner join td_sm_user t3 on t1.createuser = t3.user_id order by t1.type";
  			conn.executeSelect(sql);
  			if (conn.size()>0){
  				Template templateobj;
  				
  				String sitepath = new SiteManagerImpl().getSiteAbsolutePath(siteid);
	   			File topTemplateFolder = new File(sitepath,"_template");
	   			
	  			for(int i=0;i<conn.size();i++)
	  			{   		
		  			templateobj = new Template();
		   			templateobj.setName(conn.getString(i,"name"));       //模板名称
		   			templateobj.setDescription(conn.getString(i,"description"));    //模板描述
		   		 	templateobj.setText(conn.getString(i,"text"));     		    
		   			templateobj.setType(conn.getInt   (i,"type")); 
		   			templateobj.setCreateTime(conn.getDate(i,"createtime"));
		   			templateobj.setCreateUserId(conn.getLong(i,"createuser"));	
		   			templateobj.setHeader(conn.getString(i,"header")); 
		   			templateobj.setTemplateId(conn.getInt(i,"template_id"));   
		   			templateobj.setIncreasePublishFlag(conn.getInt(i,"inc_pub_flag")); 
		   			templateobj.setPersistType(conn.getInt(i,"PERSISTTYPE"));
		   			templateobj.setTemplateFileName(conn.getString(i, "TEMPLATEFILENAME"));
		   			templateobj.setTemplatePath(conn.getString(i,"TEMPLATEPATH")); 
		   			
		   			File currTemplateFolder = 
		   				new File(topTemplateFolder.getAbsolutePath(),
		   						templateobj.getTemplatePath()==null?"":templateobj.getTemplatePath());
		   			File currTemplateFile = 
		   				new File(currTemplateFolder.getAbsolutePath(),templateobj.getTemplateFileName());
		   			String content = "";//正文内容
		   			
		   			if(currTemplateFile.exists())
		   				content = FileUtil.getFileContent(currTemplateFile.getAbsolutePath(),CMSUtil.getCharset());
		   			templateobj.setText(content);
		   			
		   			templatelist.add(i,templateobj);
	  			}
  			}  		
  		} catch (Exception e) {
  			System.out.print("取站点模板信息出错!"+e);
  			throw new TemplateManagerException(e.getMessage());
  		}
  		return templatelist;
    }
    
    public List getTemplateInfoListofSite(int siteid,int type) throws TemplateManagerException
    {
    	List Templatelist = new ArrayList();
    	DBUtil conn = new DBUtil(); 
  	   String  sql = "";	   
  	   try {
  		
  		 sql = "select t1.* from td_cms_template t1,td_cms_site_tpl t2 where t1.template_id=t2.template_id and t2.site_id="+siteid+" and type="+type+" ";
  		 conn.executeSelect(sql);
  		
  			for(int i=0;i<conn.size();i++){
  		     Template templateobj=new Template();
   		     templateobj.setName     (conn.getString(i,"name"));       //模板名称
   		     templateobj.setDescription (conn.getString(i,"description"));    //模板描述
   		     templateobj.setText (conn.getString(i,"text"));     		    
   		     templateobj.setType     (conn.getInt   (i,"type")); 
   		     templateobj.setCreateTime(conn.getDate(i,"createtime"));
   		     templateobj.setCreateUserId(conn.getLong(i,"createuser"));	
   		     //templateobj.setCreateUserName(conn.getString(i,"user_name"));	 			 
   		     templateobj.setHeader       (conn.getString(i,"header")); 
   		     templateobj.setTemplateId  (conn.getInt(i,"template_id"));   
   		     templateobj.setIncreasePublishFlag(conn.getInt(i,"inc_pub_flag"));    
   		     templateobj.setPersistType(conn.getInt(i,"PERSISTTYPE"));
   		     templateobj.setTemplateFileName(conn.getString(i, "TEMPLATEFILENAME"));
   		     templateobj.setTemplatePath(conn.getString(i,"TEMPLATEPATH"));
   		     templateobj.setStyle(conn.getInt(i,"TEMPLATE_STYLE"));
  		     Templatelist.add(templateobj);
  		  }
  		
  		} catch (Exception e) {
  			throw new TemplateManagerException(e.getMessage());
  		}  
    	return Templatelist;
    }
    
    public List getTplList(int siteid,int type) throws TemplateManagerException
    {
    	String sql = "select t1.template_id,t1.name "+
    		" from td_cms_template t1,td_cms_site_tpl t2 "+
    		" where t1.template_id=t2.template_id and "+
    		" t2.site_id="+siteid+" and type="+type+" ";
    	
		List list = null;
		
		try{
    		DBUtil db1 = new DBUtil();			
	    	db1.executeSelect(sql);
	    	list = new ArrayList();
	    	for(int i=0;i<db1.size();i++){
	    		List oneRow = new ArrayList();
	    		oneRow.add(db1.getString(i, 0));
	    		oneRow.add(db1.getString(i, 1));
	    		list.add(oneRow);
	    	}
    	}catch(Exception e){
	    	e.printStackTrace();
	    	throw new TemplateManagerException(e.toString());
	    }
	    
    	return list;  		
    }
    
    
    public ListInfo getTemplateInfoListofSite(int siteid,int offset,int maxitem) throws TemplateManagerException
    {
    	ListInfo   Templatelist = new ListInfo();
    	List list = new ArrayList();
    	DBUtil conn = new DBUtil(); 
   	    String  sql = "";	   
   	    try {
   		
   		 sql = "select t1.* from td_cms_template t1,td_cms_site_tpl t2 where t1.template_id=t2.template_id and t2.site_id="+siteid+" order by type";
   		 
   		 conn.executeSelect(sql, offset, maxitem);
   		 if (conn.size()>0){
   			for(int i=0;i<conn.size();i++){
   		     Template templateobj=new Template();
   		     templateobj.setName     (conn.getString(i,"name"));       //模板名称
   		     templateobj.setDescription (conn.getString(i,"description"));    //模板描述
   		     templateobj.setText (conn.getString(i,"text"));     		    
   		     templateobj.setType     (conn.getInt   (i,"type")); 
   		     templateobj.setCreateTime(conn.getDate(i,"createtime"));
   		     templateobj.setCreateUserId(conn.getLong(i,"createuser"));	
   		     //templateobj.setCreateUserName(conn.getString(i,"user_name"));	 			 
   		     templateobj.setHeader       (conn.getString(i,"header")); 
   		     templateobj.setTemplateId  (conn.getInt(i,"template_id"));   
   		     templateobj.setIncreasePublishFlag(conn.getInt(i,"inc_pub_flag"));        		     
   		     list.add(i,templateobj);
   			}
   		  }
   		  Templatelist.setDatas(list);
   		  Templatelist.setTotalSize(conn.getTotalSize());
		  return Templatelist;
   		} catch (Exception e) {
   			System.out.print("dd取站点模板信息出错!"+e);
   			throw new TemplateManagerException(e.getMessage());
   		}     	
    	
    }
    
    /**
     * 根据SQL语言传入实现搜索查询模板的列表
     * 
     * sql为传入的要执行的sql语言
     * 
     */
    public ListInfo getTemplateInfoListofSite (HttpServletRequest request,int offset,int maxitem) throws TemplateManagerException
    {
    	
		String siteId=request.getParameter("siteId");
		String action = request.getParameter("action");
		String name = request.getParameter("name");
		if(null==name) name = "";
		String creatorUser = request.getParameter("creatorUser");
		if(null==creatorUser) creatorUser = "";
		String type = request.getParameter("type");
		String templateStyle = request.getParameter("templateStyle");
		DB db = DBUtil.getDBAdapter();
		
		String TimeBgin=request.getParameter("TimeBgin");
		String TimeEnd=request.getParameter("TimeEnd");
		//String channelIds=request.getParameter("channelIds");
		Map params = new HashMap();
		params.put("siteId", Integer.parseInt(siteId));
		StringBuilder sqlBuffer = new StringBuilder();
		sqlBuffer.append(" select t1.*,t3.user_name,nvl(t4.style_name,").append(db.concat("'未知风格(' "," t1.TEMPLATE_STYLE ", "')'")).append(") as style_name from td_cms_template t1 inner join td_cms_site_tpl t2 on ");
		sqlBuffer.append(" t1.template_id = t2.template_id ");
		sqlBuffer.append(" and t2.site_id = #[siteId]");
//		sqlBuffer.append(siteId);
		DateFormat format = DataFormatUtil.getSimpleDateFormat(request, "yyyy-mm-dd");
		if(action!=null && action.equals("search"))
		{
			params.put("name", "%"+name+"%");
			sqlBuffer.append(" and t1.name like #[name] ");
			if(null!=type && !type.equals(""))
			{
				params.put("type", Integer.parseInt(type));
				sqlBuffer.append(" and t1.type = #[type]");
			}
			if(null!=TimeBgin && !TimeBgin.equals(""))
			{
				try {
					params.put("TimeBgin", format.parse(TimeBgin));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sqlBuffer.append(" and t1.createtime>=#[TimeBgin]");
			}
			if(null!=TimeEnd && !TimeEnd.equals(""))
			{
				try {
					Date date = format.parse(TimeEnd);
					date = TimeUtil.addDates(date, 1);
					params.put("TimeEnd", date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sqlBuffer.append(" and t1.createtime<#[TimeEnd]");
				
			}

		}
		sqlBuffer.append(" inner join td_sm_user t3 ");
		sqlBuffer.append("on t1.createuser = t3.user_id");
		if(action!=null && action.equals("search"))
		{
			params.put("creatorUser", "%"+creatorUser+"%");
			sqlBuffer.append(" and t3.user_name like #[creatorUser]");
		}
		sqlBuffer.append(" left join td_cms_template_style t4 on t1.template_style = t4.style_id ");
		if(null!=templateStyle && !templateStyle.equals("-2")){
			int styleId = Integer.parseInt(templateStyle);
			params.put("styleId", styleId);
			sqlBuffer.append(" and t4.style_id = #[styleId]");
		}
		sqlBuffer.append(" order by t1.type,t1.template_style,t1.PERSISTTYPE,t1.createtime desc");
    	
    	
    	ListInfo   templatelist = new ListInfo();
    	
    	
   	    try {   
   	    	final List<Template> list = new ArrayList<Template>();
   	    	templatelist = SQLExecutor.queryListInfoBeanByNullRowHandler(new NullRowHandler(){
   	    		
			@Override
			public void handleRow(Record conn) throws Exception {
				Template templateobj=new Template();
	   		     templateobj.setName     (conn.getString("name"));       //模板名称
	   		     templateobj.setDescription (conn.getString("description"));    //模板描述
	   		     templateobj.setText (conn.getString("text"));     		    
	   		     templateobj.setType     (conn.getInt   ("type"));
	   		     templateobj.setStyleName(conn.getString("style_name"));	//模板风格名称
	   		     templateobj.setCreateTime(conn.getDate("createtime"));
	   		     templateobj.setCreateUserId(conn.getLong("createuser"));	
	   		     templateobj.setCreateUserName(conn.getString("user_name"));	 			 
	   		     templateobj.setHeader       (conn.getString("header")); 
	   		     templateobj.setTemplateId  (conn.getInt("template_id"));   
	   		     templateobj.setIncreasePublishFlag(conn.getInt("inc_pub_flag"));
	   		     templateobj.setTemplateFileName(conn.getString( "TEMPLATEFILENAME"));
			     templateobj.setTemplatePath(conn.getString("TEMPLATEPATH")); 
	   		     list.add(templateobj);
				
			}
   			 
   		 }, sqlBuffer.toString(), offset, maxitem, params);
//   		 conn.executeSelect(sqlBuffer.toString(), offset, maxitem);
//   		 if (conn.size()>0){
//   			for(int i=0;i<conn.size();i++){
//   		     Template templateobj=new Template();
//   		     templateobj.setName     (conn.getString(i,"name"));       //模板名称
//   		     templateobj.setDescription (conn.getString(i,"description"));    //模板描述
//   		     templateobj.setText (conn.getString(i,"text"));     		    
//   		     templateobj.setType     (conn.getInt   (i,"type"));
//   		     templateobj.setStyleName(conn.getString(i,"style_name"));	//模板风格名称
//   		     templateobj.setCreateTime(conn.getDate(i,"createtime"));
//   		     templateobj.setCreateUserId(conn.getLong(i,"createuser"));	
//   		     templateobj.setCreateUserName(conn.getString(i,"user_name"));	 			 
//   		     templateobj.setHeader       (conn.getString(i,"header")); 
//   		     templateobj.setTemplateId  (conn.getInt(i,"template_id"));   
//   		     templateobj.setIncreasePublishFlag(conn.getInt(i,"inc_pub_flag"));
//   		     templateobj.setTemplateFileName(conn.getString(i, "TEMPLATEFILENAME"));
//		     templateobj.setTemplatePath(conn.getString(i,"TEMPLATEPATH")); 
//   		     list.add(i,templateobj);
//   			}
//   		  }
	   	  templatelist.setDatas(list);
//	   	  templatelist.setTotalSize(conn.getTotalSize());
   		  return templatelist;
   		} catch (Exception e) {
   			System.out.print("取站点模板信息出错!"+e);
   			throw new TemplateManagerException(e.getMessage());
   		}     
   	
    	
    }    
   
    public Template getSiteHomepageTplNeedIncPub(String siteId) throws TemplateManagerException
    
    {
    	DBUtil conn = new DBUtil(); 
  	    String  sql = "";	   
  	    try {
  		
  		  sql = "select b.*,c.user_name from td_cms_site a inner join td_cms_template b "+
  		 		" on a.indextemplate_id = b.template_id "+
  		 		"inner join td_sm_user c on b.createuser = c.user_id "+
  		 		" where site_id="+siteId+" and inc_pub_flag = 1 ";
  		  
		  	 conn.executeSelect(sql);
		  	Template templateobj=new Template();
		  	 if (conn.size()>0){
		  	     
		  	     templateobj.setName     (conn.getString(0,"name"));       //模板名称
		  	     templateobj.setDescription (conn.getString(0,"description"));    //模板描述
		  	     templateobj.setText (conn.getString(0,"text"));     		    
		   	     templateobj.setType     (conn.getInt   (0,"type")); 
		   	     templateobj.setCreateTime(conn.getDate(0,"createtime"));
		   	     templateobj.setCreateUserId(conn.getLong(0,"createuser"));	
		   	     templateobj.setCreateUserName(conn.getString(0,"user_name"));	 			 
		   	     templateobj.setHeader       (conn.getString(0,"header")); 
		   	     templateobj.setTemplateId  (conn.getInt(0,"template_id"));   
		   	     templateobj.setIncreasePublishFlag(conn.getInt(0,"inc_pub_flag"));       

  		        
  		  }
  		 else{		
  			 //throw new TemplateManagerException("获取模版信息错误,此站点下"+siteId+"没有增量模板");
  		 }
		  	return templateobj;
  		 } catch (Exception e) {
  			System.out.print("取模板信息出错!"+e);
  			throw new TemplateManagerException(e.getMessage());
  		}    	
    }
    
    public List getChnlOutlineNeedIncPub(String siteId) throws TemplateManagerException
    {
      	 List   Templatelist = new ArrayList();
         DBUtil conn = new DBUtil(); 
    	   String  sql = "";	   
    	   try {
    		
    		 sql =" select a.channel_id,a.outline_tpl_id "+
    			  " from td_cms_channel a inner join td_cms_template b on a.outline_tpl_id = b.template_id "+
    			  " where a.SITE_ID = "+siteId+
    			  " and a.status = 0  and "+ 
    			  " b.inc_pub_flag = 1 order by a.channel_id";    		 
    		 conn.executeSelect(sql);
    		 if (conn.size()>0){
    			for(int i=0;i<conn.size();i++){
    				List lista = new ArrayList();
    				lista.add(conn.getString(i, "channel_id"));  
    				lista.add(conn.getString(i, "outline_tpl_id"));    		     		     
    		        Templatelist.add(lista);
    			}
    		  }
    		
    		} catch (Exception e) {    			
    			throw new TemplateManagerException(e.getMessage());
    		}  
     	return Templatelist;    	
    }
    
	/**获取需要增量发布的细览模板列表
	 * @param siteId
	 * @return List 列表为template_id
	 * @throws TemplateManagerException
	 */
	public List getDetailTplNeedIncPub(String siteId) throws TemplateManagerException
	{
		DBUtil db = new DBUtil();
		String sql ="select a.template_id from td_cms_template a inner join td_cms_site_tpl b "+
					" on a.template_id = b.template_id and a.inc_pub_flag=1 "+
					" and b.site_id = "+siteId+
					" order by a.template_id";
		List listTpl = new ArrayList();
		try{
		db.executeSelect(sql);
		if(db.size()>0)
		{
			for(int i = 0;i<db.size();i++)
			{
				listTpl.add(db.getString(i, "template_id"));
			}			
		}
		}catch(SQLException e)
		{
			e.printStackTrace();
			throw new TemplateManagerException(e.getMessage());
		}
		return listTpl;
	}
	
	/**获取细览模板关联的可完全发布的文档列表
	 * @param siteId
	 * @return List 
	 * @throws TemplateManagerException
	 */
	public List getAllCanPubDocsByTpl(String templateId) throws TemplateManagerException
	{
		DBUtil db = new DBUtil();
		String sql ="select b.document_id from td_cms_template a "+
					"inner join td_cms_document b "+
					"on a.template_id = b.detailtemplate_id "+
					"and a.type = 2 and a.template_id = "+templateId+
					"inner join tb_cms_doc_level c " +
					"on b.doc_level = c.id "+
                    //"and c.level = 3  "+ //这里的tb_cms_doc_level.level
					" order by b.document_id";
		List listAllCanPubDocsByTpl = new ArrayList();
		try{
		db.executeSelect(sql);
		if(db.size()>0)
		{
			for(int i = 0;i<db.size();i++)
			{
				listAllCanPubDocsByTpl.add(db.getString(i, "document_id"));
			}			
		}
		}catch(SQLException e)
		{
			e.printStackTrace();
			throw new TemplateManagerException(e.getMessage());
		}
		return listAllCanPubDocsByTpl;
	}
    
    /**取某一类型的模板列表
     * 输入:type 模板类型
     * 输出:列表对像list
     * @param 
     * @roseuid 45864967035B
     */ 
   public List getTemplateInfoList(int type) throws TemplateManagerException{
    	 List   Templatelist = new ArrayList();
         DBUtil conn = new DBUtil(); 
    	   String  sql = "";	   
    	   try {
    		
    		 sql = "select t1.*,t2.user_name from td_cms_template t1 inner join td_sm_user t2 on t1.createuser = t2.user_id and  t1.type="+ type +" order by TEMPLATE_ID";
    		 log.warn(sql);
    		 conn.executeSelect(sql);
    		 if (conn.size()>0){
    			for(int i=0;i<conn.size();i++){
    		     Template templateobj=new Template();
       		     templateobj.setName     (conn.getString(i,"name"));       //模板名称
       		     templateobj.setDescription (conn.getString(i,"description"));    //模板描述
       		     templateobj.setText (conn.getString(i,"text"));     		    
       		     templateobj.setType     (conn.getInt   (i,"type")); 
       		     templateobj.setCreateTime(conn.getDate(i,"createtime"));		        
       		     templateobj.setCreateUserId(conn.getLong(i,"createuser"));	
    		     templateobj.setCreateUserName(conn.getString(i,"user_name"));	 	 			 
       		     templateobj.setHeader       (conn.getString(i,"header")); 
       		     templateobj.setTemplateId  (conn.getInt(i,"template_id"));   
       		     templateobj.setIncreasePublishFlag(conn.getInt(i,"inc_pub_flag"));      
    		     		     
    		     Templatelist.add(templateobj);
    			}
    		  }
    		
    		} catch (Exception e) {
    			System.out.print("取模板信息出错!"+e);
    			throw new TemplateManagerException(e.getMessage());
    		}  
     	return Templatelist;
    }
    
    public boolean TemplateisUsed(int templateid) throws TemplateManagerException
    {
    	return true;
    }

	public List getSiteTplCreator(String siteId) throws TemplateManagerException
	{
		DBUtil db = new DBUtil();
		List list = new ArrayList();
		String sql="Select distinct(t3.user_name) from td_cms_template t1 inner join td_cms_site_tpl t2 on  t1.template_id = t2.template_id  and t2.site_id = "+siteId+" inner join td_sm_user t3 on t1.createuser = t3.user_id ";
		
		try{			
			db.executeSelect(sql);
		    for(int i = 0 ;i<db.size();i++)
		    {	
		    	 Template templateobj=new Template();
	   		     templateobj.setCreateUserName(db.getString(i,"user_name"));   		     		     		    
	   		     list.add(i,templateobj);		    	
		    }			
		}catch(Exception e)
		{
 			System.out.print("获取创建了模板的用户名称集失败。"+e);
 			throw new TemplateManagerException(e.getMessage());
		}
		return list;
	}

    
    /**
     * 根据站点id,路径,文件名获取存储在文件系统中的模板的模板基本信息
     * @param siteId 某个站点
     * @param path 相对站点下_template目录的相对路径
     * @param name 文件名
     * @return 
     * @throws TemplateManagerException
     */
	public Template getTemplateInfo(String siteId,String path,String name)throws TemplateManagerException{
		if(siteId==null || siteId.trim().length()==0){
			throw new TemplateManagerException("判断某个站点下的_template目录下的相对路径和文件名是否是一个模板,请提供站点id!");
		}
		if(name==null || name.trim().length()==0){
			throw new TemplateManagerException("判断是否存在文件名和路径对应的模板,请提供文件名!");
		}
		PreparedDBUtil conn = new PreparedDBUtil();
		StringBuilder sql=new StringBuilder().append(" select a.*,c.user_name from td_cms_template a inner join TD_CMS_SITE_TPL b on a.TEMPLATE_ID = b.template_id inner join TD_SM_USER c on a.CREATEUSER = c.user_id where a.PERSISTTYPE=1 and a.TEMPLATEFILENAME =? ");	
		sql.append(" and b.site_Id = ?");
		//文件是否在存放模板的根目录里
		boolean root = false; 
		boolean pp = false;
		String p = null;
		if(path!=null){
			p = path.replace('\\','/');
			if(p.startsWith("/")){
				p = p.substring(1);
			}
			if(p.endsWith("/")){
				p = p.substring(0,p.length()-1);
			}
			if(p.trim().length()!=0){
				sql.append(" and a.TEMPLATEPATH = ?");
				pp = true;
			}else{
				root = true;
			}
		}
		if(path == null || root){
			sql.append(" and (a.TEMPLATEPATH is null || a.TEMPLATEPATH='')");
		}
		
		
		
		try {
			conn.preparedSelect(sql.toString());
			conn.setString(1, name);
			conn.setInt(2, Integer.parseInt(siteId));
			if(pp)
				conn.setString(3, p);
			conn.executePrepared();
			if (conn.size() > 0) {
				Template templateobj = new Template();
				templateobj.setName(conn.getString(0, "name"));
				templateobj.setDescription(conn.getString(0, "description"));		
				templateobj.setType(conn.getInt(0, "type"));
				templateobj.setCreateTime(conn.getDate(0, "createtime"));
				templateobj.setCreateUserId(conn.getLong(0, "createuser"));
				templateobj.setCreateUserName(conn.getString(0, "user_name"));
				templateobj.setTemplateId(conn.getInt(0, "template_id"));
				templateobj.setIncreasePublishFlag(conn.getInt(0,"inc_pub_flag"));
				templateobj.setPersistType(conn.getInt(0, "PERSISTTYPE"));
				templateobj.setTemplateFileName(conn.getString(0,"TEMPLATEFILENAME"));
				templateobj.setTemplatePath(conn.getString(0, "TEMPLATEPATH"));
				
				templateobj.setText(conn.getString(0,"text"));
				templateobj.setStyle(conn.getInt(0,"TEMPLATE_STYLE"));
				templateobj.setHeader(conn.getString(0,"header"));
				return templateobj;
			}
		} catch (Exception e) {
			throw new TemplateManagerException(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 导出某个站点下的某个模板
	 * @param siteId 站点id
	 * @param templateId 模板id
	 * @param target 生成的压缩包输出流目的地
	 * @throws TemplateManagerException
	 */
	public void export(HttpServletRequest request,String siteId, String templateId, OutputStream target)throws TemplateManagerException {
		if (siteId == null || siteId.trim().length() == 0 || templateId == null	|| templateId.trim().length() == 0) {
			throw new TemplateManagerException("没有站点id或模板id,无法导出模板");
		}
		Template tplt = this.getTemplateInfo(templateId);
		if (tplt == null) {
			throw new TemplateManagerException("根据模板id没有找到模板!");
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<template>\n");
		sb.append("\t<id>" + tplt.getTemplateId() + "</id>\n");
		sb.append("\t<name>" + tplt.getName() + "</name>\n");
		sb.append("\t<description><![CDATA[" + tplt.getDescription()+ "]]></description>\n");
		sb.append("\t<header><![CDATA[" + tplt.getHeader() + "]]></header>\n");
		sb.append("\t<text><![CDATA[" + tplt.getText() + "]]></text>\n");
		sb.append("\t<type>" + tplt.getType() + "</type>\n");
		sb.append("\t<createuser>" + tplt.getCreateUserId()	+ "</createuser>\n");
		sb.append("\t<createtime>" + tplt.getCreateTime() + "</createtime>\n");
		sb.append("\t<inc_pub_flag>" + tplt.getIncreasePublishFlag()+ "</inc_pub_flag>\n");
		sb.append("\t<presisttype>" + tplt.getPersistType()	+ "</presisttype>\n");
		sb.append("\t<templatefilename>" + tplt.getTemplateFileName()+ "</templatefilename>\n");
		sb.append("\t<templatepath>" + tplt.getTemplatePath()+ "</templatepath>\n");
		sb.append("\t<siteid>" + siteId + "</siteid>\n");
		sb.append("</template>");
		String currFileContent = "";
		String fileName = "";
		String sitepath = "";
		String currTemplateFolder = "";
		String templateFolder = "";
		if (1 == tplt.getPersistType()) {
			try {
				sitepath = new SiteManagerImpl().getSiteAbsolutePath(siteId);
			} catch (SiteManagerException e) {
				e.printStackTrace();
				throw new TemplateManagerException("根据站点id获取模板所在路径发生异常!");
			}
			if (sitepath == null || sitepath.trim().length() == 0) {
				throw new TemplateManagerException("根据站点id获取模板所在路径失败!");
			}
			templateFolder = CMSUtil.getPath(sitepath, "_template");
			currTemplateFolder = CMSUtil.getPath(templateFolder, tplt.getTemplatePath());
			fileName = tplt.getTemplateFileName();
			if (fileName == null || fileName.trim().length() == 0) {
				throw new TemplateManagerException("模板内容存储在文件系统中,但是没有提供相应文件名!");
			}
			String currFile = CMSUtil.getPath(currTemplateFolder, fileName);
			try {
				currFileContent = ""+FileUtil.getFileContent(currFile,CMSUtil.getCharset());
			} catch (IOException e) {
				e.printStackTrace();
				throw new TemplateManagerException("在文件系统中读取模板内容时发生异常!");
			} 
		}
		
		ZipOutputStream zos = new ZipOutputStream(target);
		try {
			String relativePath = tplt.getTemplatePath();
			ZipEntry ze = new ZipEntry("_template.xml");
			zos.putNextEntry(ze);
			log.warn(sb.toString());
			zos.write(sb.toString().getBytes());
			zos.closeEntry();
			
			ze = new ZipEntry(CMSUtil.getPath(relativePath,fileName));
			zos.putNextEntry(ze);
			zos.write(currFileContent.getBytes());
			zos.closeEntry();
			
			//分析文档内容
			SiteManager siteManager = new SiteManagerImpl();
			
			String sitedir = "";
			try {
			sitedir = siteManager.getSiteInfo(siteId).getSiteDir();
			} catch(Exception e) {
				e.printStackTrace();
			}
			CmsLinkProcessor processor = new CmsLinkProcessor(request,relativePath,sitedir);
			processor.setHandletype(CmsLinkProcessor.PROCESS_BACKUPTEMPLATE);
			
			try {
				processor.process(currFileContent,CmsEncoder.ENCODING_UTF_8);
				
				byte[] buf = new byte[1024];
				int len;
				
				//附件信息，包括图片，附件等
				CMSTemplateLinkTable linktable = processor.getOrigineTemplateLinkTable();
				
				Iterator it = linktable.iterator();
				
				while(it.hasNext())
				{
					CMSLink link = (CMSLink)it.next();
					
					String attachmentPath = link.getHref();
					log.warn("attachmentPath"+attachmentPath);
					String localPath = sitepath + "/_template/" + attachmentPath;
					log.warn("localPath"+localPath);
					
					try 
					{
						FileInputStream fin = new FileInputStream(localPath);
						zos.putNextEntry(
								new ZipEntry(
										attachmentPath));
//						zos.putNextEntry(
//								new ZipEntry(
//										attachmentPath.substring(attachmentPath.indexOf("/")+1,attachmentPath.length())));
						while ((len = fin.read(buf)) > 0) {
							zos.write(buf, 0, len);
						}
						zos.closeEntry();
						fin.close();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
				}
				
				//文档的相关文档
				CmsLinkTable staticlinktable = processor.getOrigineStaticPageLinkTable();
				
				Iterator staticlinkit = staticlinktable.iterator();
				
				while(staticlinkit.hasNext())
				{
					CMSLink link = (CMSLink)staticlinkit.next();
					
					String attachmentPath = link.getHref();
					log.warn("attachmentPath"+attachmentPath);
					String localPath = currTemplateFolder + attachmentPath;
					log.warn("localPath"+localPath);
					
					try
					{
						FileInputStream fin = new FileInputStream(localPath);
						zos.putNextEntry(
								new ZipEntry(
										attachmentPath.substring(attachmentPath.indexOf("/")+1,attachmentPath.length())));
						while ((len = fin.read(buf)) > 0) {
							zos.write(buf, 0, len);
						}
						zos.closeEntry();
						fin.close();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					
				}
				
				//CmsLinkTable dynamiclinktable = processor.getOrigineDynamicPageLinkTable();
			} catch (ParserException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new TemplateManagerException(e.getMessage());
		}finally{
			try {
				zos.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * 导出某个站点下的多个模板
	 * @param siteId 站点id
	 * @param templateIds 模板id数组
	 * @param target 生成的压缩包输出流目的地
	 * @throws TemplateManagerException
	 * @return 返回对导出模板的描述
	 * 先建压缩包, 把文件逐一存放到压缩包, 然后, 记录文件的路径, 把描述多个模板的文件, 存放到压缩包
	 * 模板管理中导入导出的模板附件需要过滤动态页面,所有的节点值以如下形式设置:<![CDATA[xxxxx]]>
	 */
	public String exportTmpls(HttpServletRequest request,String siteId, String[] templateIds, OutputStream target)throws TemplateManagerException {
		if(templateIds==null || templateIds.length==0) return "";
		StringBuffer sb = new StringBuffer();
		String tmpldesc = "";
		sb.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>\n");
		sb.append("<templates>\n");
		ZipOutputStream zos = new ZipOutputStream(target);
		try{
			for(int i=0;i<templateIds.length;i++){
			    String templateId = templateIds[i];		
				if (siteId == null || siteId.trim().length() == 0 || templateId == null	|| templateId.trim().length() == 0) {
					throw new TemplateManagerException("没有站点id或模板id,无法导出模板");
				}
				Template tplt = this.getTemplateInfo(templateId);
				if (tplt == null) {
					throw new TemplateManagerException("根据模板id没有找到模板!");
				}
				//记录模板描述
				if(tmpldesc.trim().length()==0)	tmpldesc += tplt.getName();	
				else tmpldesc += "," + tplt.getName();	
				//结束
				sb.append("\t<template>\n");
				sb.append("\t<id><![CDATA[" + tplt.getTemplateId() + "]]></id>\n");
				sb.append("\t<name><![CDATA[" + tplt.getName() + "]]></name>\n");
				sb.append("\t<description><![CDATA[" + tplt.getDescription()+ "]]></description>\n");
				sb.append("\t<header><![CDATA[" + tplt.getHeader() + "]]></header>\n");
				sb.append("\t<text><![CDATA[" + tplt.getText() + "]]></text>\n");
				sb.append("\t<type><![CDATA[" + tplt.getType() + "]]></type>\n");
				sb.append("\t<style><![CDATA[" + tplt.getStyle()+ "]]></style>\n");//modify 2008-3-11
				sb.append("\t<createuser><![CDATA[" + tplt.getCreateUserId()	+ "]]></createuser>\n");
				sb.append("\t<createtime><![CDATA[" + tplt.getCreateTime() + "]]></createtime>\n");
				sb.append("\t<inc_pub_flag><![CDATA[" + tplt.getIncreasePublishFlag()+ "]]></inc_pub_flag>\n");
				sb.append("\t<presisttype><![CDATA[" + tplt.getPersistType()	+ "]]></presisttype>\n");
				sb.append("\t<templatefilename><![CDATA[" + tplt.getTemplateFileName()+ "]]></templatefilename>\n");
				sb.append("\t<templatepath><![CDATA[" + tplt.getTemplatePath()+ "]]></templatepath>\n");
				sb.append("\t<siteid><![CDATA[" + siteId + "]]></siteid>\n");
				sb.append("\t<attachments>\n");
				String currFileContent = "";
				String fileName = "";
				String sitepath = "";
				String currTemplateFolder = "";
				String templateFolder = "";
				if (1 == tplt.getPersistType()) {
					try {
						sitepath = new SiteManagerImpl().getSiteAbsolutePath(siteId);
					} catch (SiteManagerException e) {
						e.printStackTrace();
						throw new TemplateManagerException("根据站点id获取模板所在路径发生异常!");
					}
					if (sitepath == null || sitepath.trim().length() == 0) {
						throw new TemplateManagerException("根据站点id获取模板所在路径失败!");
					}
					templateFolder = CMSUtil.getPath(sitepath, "_template");
					currTemplateFolder = CMSUtil.getPath(templateFolder, tplt.getTemplatePath());
					fileName = tplt.getTemplateFileName();
					if (fileName == null || fileName.trim().length() == 0) {
						throw new TemplateManagerException("模板内容存储在文件系统中,但是没有提供相应文件名!");
					}
					String currFile = CMSUtil.getPath(currTemplateFolder, fileName);
					try {
						currFileContent = ""+FileUtil.getFileContent(currFile,CMSUtil.getCharset());
					} catch (IOException e) {
						e.printStackTrace();
						throw new TemplateManagerException("在文件系统中读取模板内容时发生异常!");
					} 
				}				
				
				try {
					String relativePath = tplt.getTemplatePath();
//					ZipEntry ze = new ZipEntry("_template.xml");
//					zos.putNextEntry(ze);
//					zos.write(sb.toString().getBytes());
//					zos.closeEntry();
					
					//模板文件写入ZIP文件
					ZipEntry ze = new ZipEntry(CMSUtil.getPath(relativePath,fileName));
					zos.putNextEntry(ze);
					zos.write(currFileContent.getBytes());
					zos.closeEntry();
					
					//分析文档内容
					SiteManager siteManager = new SiteManagerImpl();
					
					String sitedir = "";
					try {
					sitedir = siteManager.getSiteInfo(siteId).getSiteDir();
					} catch(Exception e) {
						e.printStackTrace();
					}
					CmsLinkProcessor processor = new CmsLinkProcessor(request,relativePath,sitedir);
					processor.setHandletype(CmsLinkProcessor.PROCESS_BACKUPTEMPLATE);
					
					try {
						processor.process(currFileContent,CmsEncoder.ENCODING_UTF_8);
						
						byte[] buf = new byte[1024];
						int len;
						
						//附件信息，包括图片，附件等
						CMSTemplateLinkTable linktable = processor.getOrigineTemplateLinkTable();
						
						Iterator it = linktable.iterator();
						
						while(it.hasNext())
						{
							CMSLink link = (CMSLink)it.next();
							
							String attachmentPath = link.getHref();
							log.warn("附件信息，包括图片，附件等attachmentPath"+attachmentPath);
							String localPath = sitepath + "/_template/" + attachmentPath;
							log.warn("附件信息，包括图片，附件等localPath"+localPath);
							//过滤掉动态的JSP页面作为
							if(attachmentPath.indexOf(".jsp")>=0) continue;
							sb.append("\t<attachment><![CDATA[");
							sb.append(attachmentPath);
							sb.append("]]></attachment>\n");
							try 
							{
								FileInputStream fin = new FileInputStream(localPath);
								zos.putNextEntry(
										new ZipEntry(
												attachmentPath));
//		//						zos.putNextEntry(
//		//								new ZipEntry(
//		//										attachmentPath.substring(attachmentPath.indexOf("/")+1,attachmentPath.length())));
								while ((len = fin.read(buf)) > 0) {
									zos.write(buf, 0, len);
								}
								zos.closeEntry();
								fin.close();
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
							
						}
						
						//文档的相关文档
						CmsLinkTable staticlinktable = processor.getOrigineStaticPageLinkTable();
						
						Iterator staticlinkit = staticlinktable.iterator();
						
						while(staticlinkit.hasNext())
						{
							CMSLink link = (CMSLink)staticlinkit.next();							
							String attachmentPath = link.getHref();
							if(attachmentPath.indexOf(".jsp")>=0) continue;
							log.warn("相关文档attachmentPath"+attachmentPath);
							String localPath = currTemplateFolder + attachmentPath;
							log.warn("相关文档localPath"+localPath);
							sb.append("\t<attachment><![CDATA[");
							sb.append(attachmentPath);
							sb.append("]]></attachment>\n");
							try
							{
								FileInputStream fin = new FileInputStream(localPath);
								zos.putNextEntry(
										new ZipEntry(
												attachmentPath.substring(attachmentPath.indexOf("/")+1,attachmentPath.length())));
								while ((len = fin.read(buf)) > 0) {
									zos.write(buf, 0, len);
								}
								zos.closeEntry();
								fin.close();
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}						
						}					
						//CmsLinkTable dynamiclinktable = processor.getOrigineDynamicPageLinkTable();
					} catch (ParserException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
					throw new TemplateManagerException(e.getMessage());
				}
				sb.append("\t</attachments>\n");
				sb.append("\t</template>\n");				
			} //end for loop\
			sb.append("</templates>");
			//描述文件写入压缩文件
			ZipEntry ze = new ZipEntry("_template.xml");
			try {
				zos.putNextEntry(ze);
				zos.write(sb.toString().getBytes());
				zos.closeEntry();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch (Exception e1) {
			e1.printStackTrace();
			throw new TemplateManagerException(e1.getMessage());
		} finally{
			try {
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tmpldesc;
	}
	
	/**
	 * 判断页面是否是模版
	 * @param pageUrl
	 * @param fileName
	 * @return
	 */
	public boolean isPageTemplate(String siteid,String pagePath,String fileName)
	{
		
		StringBuffer templateSql = new StringBuffer("select count(TEMPLATEFILENAME) from TD_CMS_TEMPLATE t1 " +
							 "inner join td_cms_site_tpl t2 ")
							 .append("on t1.template_id=t2.template_id and t2.site_id=")
							 .append(siteid);
		if(pagePath != null && !pagePath.equals(""))
			templateSql.append(" where t1.TEMPLATEPATH='").append(pagePath).append("' and t1.TEMPLATEFILENAME='").append(fileName).append("'");
		else
		{
			templateSql.append(" where (t1.TEMPLATEPATH is null or t1.templatepath='') and t1.TEMPLATEFILENAME='").append(fileName).append("'");
		}
		DBUtil dbUtil = new DBUtil();
		try {
			dbUtil.executeSelect(templateSql.toString());
			return dbUtil.getInt(0,0) > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 判断页面是否是模版
	 * @param pageUrl
	 * @param fileName
	 * @return
	 */
	public boolean isTemplate(String pagePath,String fileName)
	{
		
		StringBuffer templateSql = new StringBuffer("select count(TEMPLATEFILENAME) from TD_CMS_TEMPLATE t1 " +
							 "inner join td_cms_site_tpl t2 ")
							 .append("on t1.template_id=t2.template_id");
		if(pagePath != null && !pagePath.equals(""))
			templateSql.append(" where t1.TEMPLATEPATH='").append(pagePath).append("' and t1.TEMPLATEFILENAME='").append(fileName).append("'");
		else
		{
			templateSql.append(" where (t1.TEMPLATEPATH is null or t1.templatepath='') and t1.TEMPLATEFILENAME='").append(fileName).append("'");
		}
		DBUtil dbUtil = new DBUtil();
		try {
			dbUtil.executeSelect(templateSql.toString());
			return dbUtil.getInt(0,0) > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 判断模板在哪一个站点下面
	 * @param siteid 
	 * @param pageUrl
	 * @param fileName
	 * @return
	 */
	public String getTemplateofSite(String templateId){
		String siteId="";
		DBUtil db = new DBUtil();
		if(templateId !=null && !templateId.equals(""))
		{
			String sql="select site_id from td_cms_site_tpl where template_id ="+templateId;
			try {
				db.executeSelect(sql);
				if(db.size()>0)
				{
					siteId = db.getInt(0,"site_id") +"";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return siteId;
	}
	public int getTplCtiedCount(String templateId, int flag) throws TemplateManagerException{
		DBUtil db = new DBUtil();
		try {
			int count1 = 0;
			int count2 = 0;
			int count = 0;
			String sql1 = "select * from td_cms_channel where outline_tpl_id =" + templateId + " or detail_tpl_id =" + templateId;
			String sql2 = "select * from td_cms_document where detailtemplate_id =" + templateId;
			if(flag == 0){
				db.executeSelect(sql1);
				count1 = db.size();
				db.executeSelect(sql2);
				count2 = db.size();
				count = count1 + count2;
			}else if(flag == 1){
				db.executeSelect(sql1);
				count1 = db.size();
				return count1;
			}else if(flag == 2){
				db.executeSelect(sql2);
				count2 = db.size();
				return count2;
			}
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TemplateManagerException(e.getMessage());
		}
	}
	public int getIndexPageCtiedCount(String indexPagePath,String siteId) throws TemplateManagerException{
		DBUtil db = new DBUtil();
		try {
			int count = 0;
			String sql = "select * from td_cms_channel where  indexpagepath = '" + indexPagePath + "' and site_id = " + siteId;			
			db.executeSelect(sql);
			count = db.size();				
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TemplateManagerException(e.getMessage());
		}
	}
	public ListInfo getChannelListofTlpCited(String indexPagePath,String siteId,String templateId, int offset, int maxitem) throws TemplateManagerException {
		ListInfo   templatelist = new ListInfo();
		List list = new ArrayList();
    	PreparedDBUtil db = new PreparedDBUtil();     	
   	    try { 
   	    	String sql = "";
			if(indexPagePath == null || indexPagePath.length()==0)
			{
				sql = "select a.*,b.name as siteName from td_cms_channel a,td_cms_site b where a.site_id = b.site_id and (a.outline_tpl_id =? or a.detail_tpl_id =?)";
				db.preparedSelect(sql,offset, maxitem);
				int tpid = Integer.parseInt(templateId) ;
				db.setInt(1, tpid);
				db.setInt(2, tpid);
			}
			else
			{
				sql = "select a.*,b.name as siteName from td_cms_channel a,td_cms_site b where a.site_id = b.site_id and a.indexpagepath = ? and a.site_id =?";
				db.preparedSelect(sql,offset, maxitem);
				int siteId_ = Integer.parseInt(siteId) ;
				db.setString(1, indexPagePath);
				db.setInt(2, siteId_);
			}
   	    	db.executePrepared();
   	    	for(int i=0;i<db.size();i++){
   	    		Channel chnl = new Channel();
   	    		chnl.setDisplayName(db.getString(i,"display_name"));
   	    		chnl.setCreateTime(db.getDate(i,"createtime"));
   	    		chnl.setSiteId(db.getInt(i,"site_id"));
   	    		chnl.setSiteName(db.getString(i,"siteName"));
   	    		list.add(chnl);
   	    	}
   	    	templatelist.setDatas(list);
  	   	  	templatelist.setTotalSize(db.getTotalSize());
     		return templatelist;
   		} catch (Exception e) {
   			System.out.print("模板的频道引用列表时出错!"+e);
   			throw new TemplateManagerException(e.getMessage());
   		}     
	}
	public ListInfo getDocumentListofTlpCited(String templateId, int offset, int maxitem) throws TemplateManagerException {
		ListInfo   templatelist = new ListInfo();
		List list = new ArrayList();
    	PreparedDBUtil db = new PreparedDBUtil();     	
   	    try { 
   	    	String sql = "select a.*,b.display_name as chnlName from td_cms_document a,td_cms_channel b where a.channel_id = b.channel_id and a.detailtemplate_id =?";
   	    	db.preparedSelect(sql,offset, maxitem);
   	    	int tpid = Integer.parseInt(templateId) ;
   	    	db.setInt(1, tpid);
   	    	db.executePrepared();
   	    	for(int i=0;i<db.size();i++){
   	    		Document doc = new Document();
   	    		doc.setSubtitle(db.getString(i,"subtitle"));
   	    		doc.setCreateTime(db.getDate(i,"createtime"));
   	    		doc.setChanel_id(db.getInt(i,"channel_id"));
   	    		doc.setChannelName(db.getString(i,"chnlName"));
   	    		list.add(doc);
   	    	}
   	    	templatelist.setDatas(list);
  	   	  	templatelist.setTotalSize(db.getTotalSize());
     		return templatelist;
   		} catch (Exception e) {
   			System.out.print("模板的频道引用列表时出错!"+e);
   			throw new TemplateManagerException(e.getMessage());
   		}     
	}
	
	public Template checkZipFile(ZipInputStream zip){
		Template tplt = new Template();
		
		return tplt;
	}
	
	/**
	 * 
	 * @param xml xml路径
	 * @param fillTemplate 压缩文件路径 
	 *        例如:D:\workspace\CMS\creatorcms\cms\siteResource\siteTemplate
	 * @return 
	 * TemplateManagerImpl.java
	 * @author: ge.tao
	 * xml文件的拓扑结构
	 * <templates>
	 *     <template>
	 *         <id></id>
	 *	       <name></name>
	 *         ...
	 *     </template>
	 *     <template>
	 *         <id></id>
	 *	       <name></name>
	 *         ...
	 *     </template>
	 *     ...
	 * </templates>
	 * 分析模板描述文件,把模板相关文件,用目录结构分离
	 * 构造模板对象
	 */
	public Object fillTemplate(String xml,String zipRentPath){
		List list = new ArrayList();		
		Map map = new HashMap();
		try {
			SAXBuilder builder = new SAXBuilder();
			org.jdom.Document doc = builder.build(new File(xml));
			Element foo = doc.getRootElement();			
			List allChildren = foo.getChildren("template");	//获取template元素
			for (int i = 0; i < allChildren.size(); i++) {
				Template tplt = new Template();
				Element child = (Element) allChildren.get(i);
				List sublist = child.getChildren(); //获取template元素的子元素
				for(int j = 0;j < sublist.size(); j++){					
					Element child_son = (Element) sublist.get(j);
					if("attachments".equalsIgnoreCase(child_son.getName())){//模板附件
						//获取附件元素
						//List subsublist = child_son.getChildren();
						//this.buildTmplFiles(subsublist,child.getAttributeValue("templatefilename"),child.getAttributeValue("id"),zipRentPath);
					}else{//模板其他基本信息
					    map.put(child_son.getName(),child_son.getText());
					}
				}	
				tplt.setTemplateId(Integer.parseInt(map.get("id").toString()));
				tplt.setSiteId(Integer.parseInt(map.get("siteid").toString()));
				tplt.setTemplatePath(map.get("templatepath").toString());
				tplt.setName(map.get("name").toString());
				tplt.setDescription(map.get("description").toString());			
				tplt.setType(Integer.parseInt(map.get("type").toString()));
				tplt.setCreateUserId(Integer.parseInt(map.get("createuser").toString()));
				tplt.setPersistType(Integer.parseInt(map.get("presisttype").toString()));
				tplt.setTemplateFileName(map.get("templatefilename").toString());			
				tplt.setHeader("null");
				tplt.setText("null");
				tplt.setTemplateId(Integer.parseInt(map.get("id").toString()));
				list.add(tplt);
				
				//建立模板目录,存放模板相关文件
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 
	 * @param list 附件列表
	 * @param tmplFile 模板文件名称
	 * @param tmplId 模板ID
	 * TemplateManagerImpl.java
	 * @author: ge.tao
	 */
	public void buildTmplFiles(List list,String tmplFile,String tmplId,String zipAbsPath){
		File tmplForder = new File(CMSUtil.getPath(zipAbsPath,tmplId));
		if(!tmplForder.exists()) 
			tmplForder.mkdirs();
		for(int i=0;i<list.size();i++){
			File file = new File(CMSUtil.getPath(zipAbsPath,tmplId),(String)list.get(i));
		}
	}
	
	
	
	/**
	 * 新增导出模板的记录
	 * @param tmplName 导出模板名称
	 * @param tmplDesc 导出模板描述
	 * @param userid 操作人ID
	 * @param expType 导出模板是共有还是私有
	 * siteid
	 * TemplateManagerImpl.java
	 * @author: ge.tao
	 */	
	public void addExportTmplRecord(String tmplName, String tmplDesc, String userid, String expType,int siteId){
		PreparedDBUtil pd = new PreparedDBUtil();
		String sql = "insert into TD_CMS_TMPL_EXPORT(TMPLNAME,TMPLDESC,EXPORTERID,FLAG,SITEID,EXPORTDATE) " +
				"values (?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
		try {
			String date = CMSUtil.formatDate(new Date());
			pd.preparedInsert(sql);
			pd.setString(1,tmplName);
			pd.setString(2,tmplDesc);
			pd.setInt(3,Integer.parseInt(userid));
			pd.setString(4,expType);
			pd.setInt(5,siteId);
			pd.setString(6,date);
			pd.executePrepared();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 删除导入模板的记录和导出模板
	 * @param tmplName 导出模板名称
	 * @param tmplName 导出模板路径
	 * TemplateManagerImpl.java
	 * @author: ge.tao
	 */
	public void deleteExportTmplRecord(String tmplName, String filePath){
		//数据库删除
		String sql = "delete TD_CMS_TMPL_EXPORT where TMPLNAME='" + tmplName + "'";
		DBUtil db = new DBUtil();
		try {
			db.executeDelete(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//文件物理删除
		//String templatePath = application.getRealPath("cms/siteResource/siteTemplate");
		String path = CMSUtil.getPath(filePath,tmplName+".zip");
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
	}
	
	/**
	 * 获取导出模板列表 不翻页
	 * @param count
	 * @return
	 * @throws DocumentManagerException 
	 * TemplateManagerImpl.java
	 * @author: ge.tao
	 */
	public List getExportTmplList(int count)
	{
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		String sql = "select TMPLNAME,TMPLDESC,EXPORTERID from TD_CMS_TMPL_EXPORT where ";
		sql += count>0?" rownum<="+String.valueOf(count):"";
		try {
			db.executeSelect(sql);
			for(int i=0;i<db.size();i++){
				TmplateExport tmpl = new TmplateExport();
				tmpl.setTmplname(db.getString(i,"TMPLNAME"));
				tmpl.setTmpldesc(db.getString(i,"TMPLDESC"));
				tmpl.setExporter(db.getString(i,"EXPORTERID"));
				list.add(tmpl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取导出当前站点有权限查看的模板包列表 有权限的私有包和公共包 翻页
	 * @param offset
	 * @param pageitems
	 * @param siteId
	 * @return
	 * @throws DocumentManagerException 
	 * TemplateManagerImpl.java
	 * @author: ge.tao
	 */
	public ListInfo getExportTmplList(long offset, int pageitems, int siteId) 
	{
		ListInfo listinfo = new ListInfo();
		List list = new ArrayList();
		PreparedDBUtil db = new PreparedDBUtil();		
		String sql = "select TMPLNAME,TMPLDESC,EXPORTERID,flag,"
            + " site.name as SITENAME from TD_CMS_TMPL_EXPORT tmpl join  td_cms_site site " 
            + "on  tmpl.siteid=site.site_id and (tmpl.flag=0 or (tmpl.flag=1 and tmpl.siteid=?))";
		try
		{
			db.preparedSelect(sql,offset,pageitems);
			db.setInt(1, siteId);
			db.executePrepared();
			if(db.size()>0)
			{
				for(int i=0;i<db.size();i++){
					TmplateExport tmpl = new TmplateExport();
					tmpl.setTmplname(db.getString(i,"TMPLNAME"));
					tmpl.setTmpldesc(db.getString(i,"TMPLDESC"));
					tmpl.setExporter(db.getString(i,"EXPORTERID"));
					tmpl.setSiteid(db.getString(i,"SITENAME"));
					tmpl.setFlag(db.getString(i,"flag"));
					list.add(tmpl);
				}
			}
			listinfo.setDatas(list);
			listinfo.setTotalSize(db.getTotalSize());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return listinfo;
	}
	
	/**
	 * 获取导出模板的所有记录列表,里面封装TmplateExport对象
	 * @param select 0: 共有; 1: 有权限的私有; 2:有权限的全部 ; 3:全部
	 * @param siteId 当前站点ID 
	 * @return 
	 * TemplateManagerImpl.java
	 * @author: ge.tao
	 */
	public List exportTmplRecordList(int select,int siteId){
		List list = new ArrayList();
		//case when DOCTYPE=1 then t.content else null end linkfile
		String subsql = "";
		boolean set = false;
		switch(select){
		case 0:
			subsql = " and tmpl.flag=0 ";
			break;
		case 1:
			subsql = " and tmpl.flag=1 and tmpl.siteid=?";
			set = true;
			break;
		case 2:
			subsql = " and ( tmpl.flag=0 or (tmpl.flag=1 and tmpl.siteid=?)) ";
			set = true;
			break;
		default:
		    break;
		}
		String sql = "select TMPLNAME,TMPLDESC,EXPORTERID,flag,"
                   + " site.name as SITENAME from TD_CMS_TMPL_EXPORT tmpl join  td_cms_site site " 
                   + "on  tmpl.siteid=site.site_id ";
		sql += subsql;
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedSelect(sql);
			if(set)
				db.setInt(1, siteId);
			db.executePrepared();
			for(int i=0;i<db.size();i++){
				TmplateExport tmpl = new TmplateExport();
				tmpl.setTmplname(db.getString(i,"TMPLNAME"));
				tmpl.setTmpldesc(db.getString(i,"TMPLDESC"));
				tmpl.setExporter(db.getString(i,"EXPORTERID"));
				tmpl.setSiteid(db.getString(i,"SITENAME"));
				tmpl.setFlag(db.getString(i,"flag"));
				list.add(tmpl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 根据模板类型查询出所有子类型列表
	 * @param type 模版类型0:站点首页模版1：频道概览模板2：文档细览模板 3：文档评论模板
	 * @return
	 * @throws TemplateManagerException
	 * @author: peng.yang
	 */
	public List getTemplateStyleList(String type){
		List tmpStyles = new ArrayList();
		
		//去掉每一模板类型中子类的重复记录
		String strsql = " select * from td_cms_template t where t.TYPE = ? and t.rowid in (select max(rowid) from td_cms_template where template_style <> -1   group by template_style) ";
		
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedSelect(strsql);
			db.setInt(1, Integer.parseInt(type));
			db.executePrepared();
			for(int i=0;i<db.size();i++){
				Integer styleId = new Integer(db.getInt(i,"TEMPLATE_STYLE"));
				tmpStyles.add(styleId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return tmpStyles;
	}
	/**
	 * 根据站点ID，文件路径查询出该路径下的模板
	 * 
	 * @param sitId,uri
	 *         
	 * @return  List
	 * @throws TemplateManagerException
	 * @author: zhizhong.ding
	 */
	
	private List getTemplate(String siteId,String uri) throws TemplateManagerException{
		if (siteId == null || siteId.trim().length() == 0) {
			throw new TemplateManagerException(
					"判断某个站点下的_template目录下的相对路径和文件名是否是一个模板,请提供站点id!");
		}
		List templates=new ArrayList();
		String sql = " select a.*,c.user_name from td_cms_template a inner join TD_CMS_SITE_TPL b on a.TEMPLATE_ID = b.template_id inner join TD_SM_USER c on a.CREATEUSER = c.user_id where a.PERSISTTYPE=1 and  b.site_Id = '"
			+ siteId + "'and (a.templatepath='"+uri+"' or a.templatepath like '"+uri+"/%')";
		System.out.println("<<<<sql<<<<"+sql);
		DBUtil conn=new DBUtil();
		try {
			conn.executeSelect(sql);
			if(conn.size()>0){
				for(int i=0;i<conn.size();i++){
					Template templateobj = new Template();
					templateobj.setName(conn.getString(i, "name"));
					templateobj.setDescription(conn.getString(i, "description"));
					templateobj.setType(conn.getInt(i, "type"));
					templateobj.setCreateTime(conn.getDate(i, "createtime"));
					templateobj.setCreateUserId(conn.getLong(i, "createuser"));
					templateobj.setCreateUserName(conn.getString(i, "user_name"));
					templateobj.setTemplateId(conn.getInt(i, "template_id"));
					templateobj.setIncreasePublishFlag(conn.getInt(i,
							"inc_pub_flag"));
					templateobj.setPersistType(conn.getInt(i, "PERSISTTYPE"));
					templateobj.setTemplateFileName(conn.getString(i,
							"TEMPLATEFILENAME"));
					templateobj.setTemplatePath(conn.getString(i, "TEMPLATEPATH"));
					
					templateobj.setText(conn.getString(i,"text"));
					templateobj.setStyle(conn.getInt(i,"TEMPLATE_STYLE"));
					templateobj.setHeader(conn.getString(i,"header")); 
					templates.add(templateobj);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new TemplateManagerException(e.getMessage());
		}
		return templates;
	}
	/**
	 * 根据站点ID，原文件路径，新文件路径更新模板文件路径
	 * 
	 * @param sitId,olduri,newuri
	 *         
	 * @return  
	 * @throws TemplateManagerException
	 * @author: zhizhong.ding
	 */
	public void  updateTemplatePath(String siteId,String olduri,String newuri) throws TemplateManagerException{
		List tmpList=getTemplate(siteId,olduri);
		TemplateManager tm=new TemplateManagerImpl();
		for(int i=0;i<tmpList.size();i++){
			Template template=(Template)tmpList.get(i);
			String uri=template.getTemplatePath().replaceFirst(olduri,newuri);
			template.setTemplatePath(uri);
			tm.updateTemplate(template);
			
		}
	}
}
