/**
 * 
 */
package com.frameworkset.platform.cms.templatemanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.htmlparser.util.ParserException;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.container.TmplateExport;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.htmlconverter.CMSTemplateLinkTable;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkTable;
import com.frameworkset.platform.cms.driver.i18n.CmsEncoder;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.util.ListInfo;

/**
 * <p>Title: TemplatePackageManager.java</p>
 *
 * <p>Description: 内容管理系统模板包管理,管理站点模板的导入/导出功能
 * 模板包的分析功能
 * </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-10-15 11:06:19
 * @author ge.tao
 * @version 1.0
 */
public class TemplatePackageManager implements java.io.Serializable {
	private static Logger log = Logger.getLogger(TemplatePackageManager.class);
	private static final int EOF = -1;
	/**
	 * 覆盖已有模板,覆盖模板附件
	 */
	private static final int RECOVERTMPLNAME_RECOVERATT = 0;
	/**
	 * 覆盖已有模板,使用原有模板附件
	 */
	private static final int RECOVERTMPLNAME_USEOLDATT = 1;
	/**
	 * 重命名模板,覆盖模板附件
	 */
	private static final int RENAMETMPLNAME_RECOVERATT = 2;
	/**
	 * 重命名模板,使用原有模板附件
	 */
	private static final int RENAMETMPLNAME_USEOLDATT = 3;
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
		DBUtil db = new DBUtil();		
		String sql = "select TMPLNAME,TMPLDESC,EXPORTERID,flag,"
            + " site.name as SITENAME from TD_CMS_TMPL_EXPORT tmpl join  td_cms_site site " 
            + "on  tmpl.siteid=site.site_id and (tmpl.flag=0 or (tmpl.flag=1 and tmpl.siteid="+siteId+"))";
		try
		{
			db.executeSelect(sql,offset,pageitems);
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
		switch(select){
		case 0:
			subsql = " and tmpl.flag=0 ";
			break;
		case 1:
			subsql = " and tmpl.flag=1 and tmpl.siteid="+siteId;
			break;
		case 2:
			subsql = " and ( tmpl.flag=0 or (tmpl.flag=1 and tmpl.siteid="+siteId+")) ";
			break;
		default:
		    break;
		}
		String sql = "select TMPLNAME,TMPLDESC,EXPORTERID,flag,"
                   + " site.name as SITENAME from TD_CMS_TMPL_EXPORT tmpl join  td_cms_site site " 
                   + "on  tmpl.siteid=site.site_id ";
		sql += subsql;
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
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
				TemplateManager tmplImpl = new TemplateManagerImpl();
				Template tplt = tmplImpl.getTemplateInfo(templateId);
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
				sb.append("\t<createuser><![CDATA[" + tplt.getCreateUserId()	+ "]]></createuser>\n");
				sb.append("\t<createtime><![CDATA[" + tplt.getCreateTime() + "]]></createtime>\n");
				sb.append("\t<inc_pub_flag><![CDATA[" + tplt.getIncreasePublishFlag()+ "]]></inc_pub_flag>\n");
				sb.append("\t<presisttype><![CDATA[" + tplt.getPersistType()	+ "]]></presisttype>\n");
				sb.append("\t<templatefilename><![CDATA[" + tplt.getTemplateFileName()+ "]]></templatefilename>\n");
				sb.append("\t<templatepath><![CDATA[" + tplt.getTemplatePath()+ "]]></templatepath>\n");
				sb.append("\t<siteid><![CDATA[" + siteId + "]]></siteid>\n");
				sb.append("\t<attachments>\n");
//				sb.append("</template>");
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
//			log.warn("_template.xml:"+sb.toString());
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
			try {
				CMSUtil.getCMSDriverConfiguration().getCMSService().getTemplateManager()
				.createTemplateofSite(template,template.getSiteId());
			} catch (TemplateManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			} catch (DriverConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new TemplateManagerException(e.getMessage());
			}
		}
    }
    
    /**
     * 将模板包解压到临时目录下,并且分析其中包含的模板信息到list容器中,并返回
     * 如果压缩包中没有template.xml文件则返回null
     * @param temppath
     * @param packagePath
     * @return TemplatePackage 
     * @author: ge.tao
     */
    public TemplatePackage unzipTemplatePackage(String temppath,String zippackagePath)
    {    	  
    	TemplatePackage templatePackage = new TemplatePackage();
    	if(zippackagePath.endsWith(".zip")){
			ZipFile zf = null;
			Enumeration en = null;
			InputStream is = null;
			BufferedInputStream bis = null;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			try {
				zf = new ZipFile(zippackagePath);
				en = zf.entries();
				boolean isTemplatePackage = false;
				while (en.hasMoreElements()) {
					ZipEntry zipEnt = (ZipEntry) en.nextElement();
					String name = zipEnt.getName();
					if(name.endsWith("_template.xml"))
					{
						isTemplatePackage = true;
					}
					File file = new File(CMSUtil.getPath(temppath ,zipEnt.getName()));
					if (zipEnt.isDirectory()) {
						file.mkdirs();
					} else {
						is = zf.getInputStream(zipEnt);
						bis = new BufferedInputStream(is);
						File dir = new File(file.getParent());
						dir.mkdirs();
						fos = new FileOutputStream(file);
						bos = new BufferedOutputStream(fos);
						int c;
						//byte[] b = new byte[1024];
						while ((c = bis.read()) != EOF) {
							bos.write((byte)c);							
						}
						bos.flush();
					}	
				}
				if(isTemplatePackage)
				{
					templatePackage.setTemplatePackage(isTemplatePackage);
					String xmlPath = CMSUtil.getPath(temppath,"_template.xml");
					xmlPath = xmlPath.replaceAll("\\\\","/");					
					List templateInfos = this.parserTemplateinfoFromXml(xmlPath);
					templatePackage.setTemplateInfos(templateInfos);
				}else{
					templatePackage.setTemplatePackage(false);
				}
				
				templatePackage.setUnpackage_path(temppath);
				
				return templatePackage;
			}
			catch (ZipException e) {
				
			} catch (IOException e) {
//				
			}
			finally
			{   				
				try {
					if(is != null)
					    is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(bis != null)
					    bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(bos != null)
					    bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(fos != null)
					    fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				try {
					if(zf != null)
					    zf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}    	
        return templatePackage;
    }
    
    
    
   /**
    * 解析_template.xml包含的所有的模板信息,返回模板信息列表
    * @param xmlpath
    * @return List<TemplateInfo>
    * @author: ge.tao
 * @throws IOException 
    */
    public List parserTemplateinfoFromXml(String xmlpath) throws IOException
    {
    	List templateInfos = new ArrayList();    	
		try {
			SAXBuilder builder = new SAXBuilder();
			org.jdom.Document doc = builder.build(new File(xmlpath));
			Element foo = doc.getRootElement();			
			List allChildren = foo.getChildren("template");	//获取template元素
			
			for (int i = 0; i < allChildren.size(); i++) {
				TemplateInfo templateInfo = new TemplateInfo(); 				
				Template tplt = new Template();
				templateInfo.setTemplate(tplt);				
				Element child = (Element) allChildren.get(i);
				List sublist = child.getChildren(); //获取template元素的子元素
				for(int j = 0;j < sublist.size(); j++){					
					Element child_son = (Element) sublist.get(j);
					if("attachments".equalsIgnoreCase(child_son.getName())){//模板附件
						//获取附件元素
						List subsublist = child_son.getChildren();
						for(int k=0;k<subsublist.size();k++){
							Element child_son_son = (Element) subsublist.get(k);
							templateInfo.addAttachement(child_son_son.getText());
						}						
					}else{//模板其他基本信息
						String name = child_son.getName();
						String value = child_son.getText();
						if(name.equals("id"))
						{
							tplt.setTemplateId(Integer.parseInt(value));
						}
						if(name.equals("siteid"))
						{
							tplt.setSiteId(Integer.parseInt(value));
						}
						if(name.equals("templatepath"))
						{
							tplt.setTemplatePath(value);
						}
						if(name.equals("name"))
						{
							tplt.setName(value);
						}
						if(name.equals("description"))
						{
							tplt.setDescription(value);
						}
						if(name.equals("type"))
						{
							tplt.setType(Integer.parseInt(value));
						}
						if(name.equals("style")){//modify 2008-3-11
							tplt.setStyle(Integer.parseInt(value));
						}
						if(name.equals("createuser"))
						{
							tplt.setCreateUserId(Integer.parseInt(value));
						}
						if(name.equals("presisttype"))
						{
							tplt.setPersistType(Integer.parseInt(value));
						}
						if(name.equals("templatefilename"))
						{
							tplt.setTemplateFileName(value);
						}
						if(name.equals("header"))
						{
							tplt.setHeader(value);
						}
						if(name.equals("text"))
						{
							tplt.setText(value);
						}
					}
				}	
				
				templateInfos.add(templateInfo);
				
				//建立模板目录,存放模板相关文件
				
			}
			
		} catch (JDOMException e) {
			e.printStackTrace();
		}
    	return templateInfos;
    }
    
    /**
     * 导入模板,考虑去重
     * @param request
     * @param templatePackage 
     * @param siteId
     * @param type 模板文件和模板名称和模板附件的去重处理方式
     *             type=0:覆盖已有模板,覆盖模板附件
     *             type=1:覆盖已有模板,使用原有模板附件
     *             type=2:重命名模板,覆盖模板附件
     *             type=3:重命名模板,使用原有模板附件
     * @parem importType:导入方式:本地导入/远程导入
     *                   本地导入,要分析模板文件的相关附件.
     * TemplatePackageManager.java
     * @author: ge.tao
     */
    public void importTmpls(HttpServletRequest request,TemplatePackage templatePackage, String siteId,String importType, int type){
    	List tmplInfoslist = templatePackage.getSelectedTemplateInfos();    	
    	//当前模板文件的内容
    	String currFileContent = "";
    	Site site = null;
    	try {
			site = CMSUtil.getSiteCacheManager().getSite(siteId);
			String unpackage_path = templatePackage.getUnpackage_path();
			for(int i=0;i<tmplInfoslist.size();i++){
				TemplateInfo tmplInfo = (TemplateInfo)tmplInfoslist.get(i);				
				Template template = tmplInfo.getTemplate();
				TemplateManager tmplImpl = new TemplateManagerImpl();				
				
				//文件拷贝
				//构造拷贝路径
				String sitepath = null;
		    	try {
					sitepath = new SiteManagerImpl().getSiteAbsolutePath(""+siteId);
				} catch (SiteManagerException e) {
					e.printStackTrace();
					throw new TemplateManagerException(e.getMessage());
				}
				File topTemplateFolder = new File(sitepath,"_template");
				String uri = template.getTemplatePath();
				//构造出模板拷贝目的目录
				File currTemplateFolder = new File(topTemplateFolder.getAbsolutePath(),uri);
				if(currTemplateFolder.exists() && !uri.trim().equals("")){
					//throw new TemplateManagerException("文件夹已经存在,请重新指定模板所存路径!");//modify 2008-3-11
				}else{
					currTemplateFolder.mkdirs();
				}
				
				//模板文件拷贝
				String tmplFile = template.getTemplateFileName();
				String tmplFilePath=CMSUtil.getPath(uri,tmplFile);//modify 2008-3-11 获取解压缩文件的路径；
				//原始的导入模板文件名称
				//String tmplFromFile = CMSUtil.getPath(unpackage_path,tmplFile);
				String tmplFromFile = CMSUtil.getPath(unpackage_path,tmplFilePath);//modify 2008-3-11 获取解压缩文件的完整路径；
				String tmplToPath = currTemplateFolder.getAbsolutePath();
				String tmplToFile = CMSUtil.getPath(tmplToPath,tmplFile);
				File toFile = new File(tmplToFile);
				if(toFile.exists()){//判断目的地址是否有改模板文件
					switch(type){
					case RECOVERTMPLNAME_RECOVERATT:
					case RECOVERTMPLNAME_USEOLDATT:
						toFile.delete();
						//log.warn("tmplFromFile------recover-----tmplfile-----------"+tmplFromFile);
						//log.warn("tmplToPath----------recover----tmplfile--------"+tmplToPath);
						FileUtil.fileCopy(tmplFromFile,tmplToFile);
						break;
					case RENAMETMPLNAME_RECOVERATT:
					case RENAMETMPLNAME_USEOLDATT:					
						String newTmplFileName = this.getSameTmplFileName(template.getTemplateFileName());
						String newTmplName = this.getSameTmplName(template.getName());						
						//解压文件的 重命名文件
						String newTmplToFile = CMSUtil.getPath(tmplToPath,newTmplFileName);												
						template.setName(newTmplName);
						template.setTemplateFileName(newTmplFileName);
						FileUtil.fileCopy(tmplFromFile,newTmplToFile);
						//log.warn("tmplFromFile----------rename--tmplfile----------"+tmplFromFile);	
						//log.warn("newTmplToFile------rename--tmplfile--------------"+newTmplToFile);
						break;
					}
				}else{
				    FileUtil.copy(tmplFromFile,tmplToPath);
				}
				
				//本地导入,分析模板附件
				if("local".equalsIgnoreCase(importType)){	
					String sitedir = "";
					//分析文档内容
					SiteManager siteManager = new SiteManagerImpl();
					try {
						currFileContent = ""+FileUtil.getFileContent(tmplFromFile,CMSUtil.getCharset());
					} catch (IOException e) {
						e.printStackTrace();
						throw new TemplateManagerException("在文件系统中读取模板内容时发生异常!");
					}
					try {
						sitedir = siteManager.getSiteInfo(siteId).getSiteDir();
						} catch(Exception e) {
							e.printStackTrace();
						}
					String relativePath = template.getTemplatePath();
					CmsLinkProcessor processor = new CmsLinkProcessor(request,relativePath,sitedir);
					processor.setHandletype(CmsLinkProcessor.PROCESS_BACKUPTEMPLATE);
					
					try {
						processor.process(currFileContent,CmsEncoder.ENCODING_UTF_8);	
						//附件信息，包括图片，附件等
						CMSTemplateLinkTable linktable = processor.getOrigineTemplateLinkTable();						
						Iterator it = linktable.iterator();
						while(it.hasNext()){
							CMSLink link = (CMSLink)it.next();
							String attachmentPath = link.getHref();
							//分析出本地导入模板文件, 得到模板附件,开始拷贝
							String localAttFromFile = CMSUtil.getPath(unpackage_path,attachmentPath);
							log.warn("本地上传模板,分析模板附件---------------------------"+localAttFromFile);
							String localAttToFile = CMSUtil.getPath(tmplToPath,attachmentPath);
							File attFile = new File(localAttToFile);
							if(attFile.exists()){
								switch(type){
								case RECOVERTMPLNAME_RECOVERATT:
								case RENAMETMPLNAME_RECOVERATT:
									attFile.delete();
									FileUtil.fileCopy(localAttFromFile,localAttToFile);
									break;
								case RECOVERTMPLNAME_USEOLDATT:
								case RENAMETMPLNAME_USEOLDATT:
									//do nothing 
									break;
								}
							}else{
							    FileUtil.fileCopy(localAttFromFile,localAttToFile);
							}
						}
						//最后删除当前分析的模板文件
						//本地导入是单个模板处理
						File localTmplFile = new File(tmplFromFile);
						if(localTmplFile.exists())
							localTmplFile.delete();
					}catch (ParserException e) {
						e.printStackTrace();
					}
				}
				
				//附件列表
				List attachementList = tmplInfo.getAttachements();
				for(int att=0;att<attachementList.size();att++){
					String attName = (String)attachementList.get(i);
					//过滤jsp动态附件
					if(attName.indexOf(".jsp")>0) 
						continue;
					//拷贝附件
					String attFromFile = CMSUtil.getPath(unpackage_path,attName);
					String attToPath =  currTemplateFolder.getAbsolutePath();
					String attToFile = CMSUtil.getPath(attToPath,attName);
					File attFile = new File(attToFile);
					if(attFile.exists()){
						switch(type){
						case RECOVERTMPLNAME_RECOVERATT:
						case RENAMETMPLNAME_RECOVERATT:
							attFile.delete();
							FileUtil.fileCopy(attFromFile,attToFile);
							break;
						case RECOVERTMPLNAME_USEOLDATT:
						case RENAMETMPLNAME_USEOLDATT:
							//do nothing 
							break;
						}
					}else{
					    FileUtil.fileCopy(attFromFile,attToFile);
					}
				}
				
				//模板存数据库
				tmplImpl.createTemplateofSite(template,(int)site.getSiteId());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//最后删除模板包解压临时目录
		if(!"local".equalsIgnoreCase(importType)){	
		    log.warn("delete tmp file---------------------------------"+templatePackage.getUnpackage_path());
		    FileUtil.deleteFile(templatePackage.getUnpackage_path());
		}
    	
    }
    /**
     * 
     * @param repeatName 重复模板
     * @param repeatAtt 重复附件
     * @return 
     * TemplatePackageManager.java
     * @author: ge.tao
     */
    public int getFileCopyType(String repeatName,String repeatAtt){
    	if(repeatName==null || repeatAtt==null )
    		return RENAMETMPLNAME_USEOLDATT;
    	if("cover".equalsIgnoreCase(repeatName) && "cover".equalsIgnoreCase(repeatAtt)){
    		return RECOVERTMPLNAME_RECOVERATT;
    	}else if("cover".equalsIgnoreCase(repeatName) && "useold".equalsIgnoreCase(repeatAtt)){
    		return RECOVERTMPLNAME_USEOLDATT;
    	}else if("rename".equalsIgnoreCase(repeatName) && "cover".equalsIgnoreCase(repeatAtt)){
    		return RENAMETMPLNAME_RECOVERATT;
    	}else{
    		return RENAMETMPLNAME_USEOLDATT;
    	}
    }
    
    /**
     * 得到数据库表中,已有的改模板名的个数 来重新命名
     * like '%) 模板名%'
     * 构造 复件(i) 的命名方式
     * @param filePath
     * @return 
     * TemplatePackageManager.java
     * @author: ge.tao
     */
    public String getSameTmplName(String tmplName){
    	if(tmplName.indexOf(")")>0)
    		tmplName = tmplName.substring(tmplName.indexOf(")")+1,tmplName.length()).trim();
    	int count = 0;
    	String sql = "select template_id from td_cms_template where name like  '%) "+tmplName+"'";
    	log.warn(sql);
    	DBUtil db = new DBUtil();
    	try {
			db.executeSelect(sql);			
			count =  db.size() + 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "复件(" + count + ") " + tmplName ;
    	
    }
    
    /**
     * 
     * 得到数据库表中,已有的改模板名的个数 来重新命名
     * like '%) 模板文件%'
     * 构造 复件(i) 的命名方式
     * @param tmplFileName : copy(1) filname.html / filname.html
     * @return 
     * TemplatePackageManager.java
     * @author: ge.tao
     */
    public String getSameTmplFileName(String tmplFileName){
    	if(tmplFileName.indexOf(")")>0)
    	    tmplFileName = tmplFileName.substring(tmplFileName.indexOf(")")+1,tmplFileName.length()).trim();
    	int count = 0;
    	String sql = "select template_id from td_cms_template where templatefilename like  '%) "+tmplFileName+"'";
    	log.warn(sql);
    	DBUtil db = new DBUtil();
    	try {
			db.executeSelect(sql);			
			count =  db.size() + 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "copy(" + count + ") " + tmplFileName ;
    	
    }

}
