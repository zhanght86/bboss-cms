package com.frameworkset.platform.cms.documentmanager.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManager;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.documentmanager.Attachment;
import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.poolman.DBUtil;

/**
 * 工具类
 * 用于把<Document>从数据库中导出为xml，zip文件
 * @author xinwang.jiao
 * 2007.02.2
 */

public class DocZipUtil implements java.io.Serializable {
	/**
	 * 导入的zip文件是从服务器选择
	 */
	public static final String FORM_LOCAL = "local";
	
	/**
	 * 导入的zip文件来自上传
	 */
	public static final String FORM_REMOTE = "remote";
	/**
	 * 把文档<Document>的信息导出为一个zip文件
	 * @param int[] docIds(文档id) 
	 * @param List attrpathList(通过分析)
	 * @param String path(文件保存路径,从页面传过来应用的path,再拼)<"D:\workspace\cms\creatorcms\">
	 * @param String attrpath(附件文件保存路径,从页面传过来,再拼)<"site4">
	 * @return String (zipFilename,打包后的zip文件名)
	 */
	public static String toZip(String zipname,int[] docIds,HttpServletRequest request,String path,String attrpath)
	{
		ChannelManager cm = new ChannelManagerImpl();
		DocumentManager dm = new DocumentManagerImpl();
		
		String zipFilename = "backupDocuments" + System.currentTimeMillis() + ".zip";
		if(zipname != null && !zipname.trim().equals(""))
			zipFilename = zipname.trim() + ".zip";
		List xmlPicsMedias = toXml(docIds,path);//xml,主题图片,多媒体文件
		
		try
		{
			String zippath = path + "cms\\docManage\\temp\\" + zipFilename;
			File zipfile = new File(zippath);
			if(zipfile.exists())
			{
				return "";//返回空文件名，用于判断。
			}
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zippath));
			byte[] buf = new byte[1024];
			int len;
			
			for(int a=0;a<docIds.length;a++)
			{
				List list = dm.getAllAttachmentOfDocument(request,docIds[a]);
				for(int b=0;b<list.size();b++)
				{
					String attrpathTemp = attrpath + "\\_webprj\\" + cm.getChannelInfo(dm.getDocChnlId(docIds[a])+"").getChannelPath() + "/content_files";
					String attrfilepath = (String)list.get(b);
					//System.out.println(attrfilepath);
					File parentFile = new File(path + "cms\\siteResource\\" + attrpathTemp);
					if(!parentFile.exists())
					{
						parentFile.mkdirs();
					}
					File attrFile = new File(attrfilepath);
					if(!attrFile.exists())
					{
						System.out.println("附件" + attrfilepath + "不存在");
						continue;
					}
					try
					{
						FileInputStream fin = new FileInputStream(attrfilepath);
						out.putNextEntry(new ZipEntry(attrFile.getName()));
						while ((len = fin.read(buf)) > 0) {
							out.write(buf, 0, len);
						}
						out.closeEntry();
						fin.close();
					} 
					catch (Exception e)
					{
						System.out.println("附件" + attrfilepath + "不存在");
						//e.printStackTrace();
					}
				}
			}
			//保存文档主题图片和多媒体文件（这两个文件是在模板路径里，区别对待）
			//还有new图片 2007-9-19
			if(xmlPicsMedias != null && xmlPicsMedias.size() > 1)
			{
				for(int b = 1;b < xmlPicsMedias.size(); b ++)//从1开始，0为xml文件
				{
					String attrpathTemp = CMSUtil.getPath(CMSUtil.getPath(path,"cms\\siteResource\\"),attrpath + "\\_template");
					File parentFile = new File(attrpathTemp);
					if(!parentFile.exists())
					{
						parentFile.mkdirs();
					}
					File attrFile = new File(parentFile,(String)xmlPicsMedias.get(b));
					if(!attrFile.exists())
					{
						System.out.println("附件" + attrFile.getAbsoluteFile() + "不存在");
						continue;
					}
					try
					{
						FileInputStream fin = new FileInputStream(attrFile);
						out.putNextEntry(new ZipEntry("_template/" + (String)xmlPicsMedias.get(b)));
						while ((len = fin.read(buf)) > 0) {
							out.write(buf, 0, len);
						}
						out.closeEntry();
						fin.close();
					} 
					catch (Exception e)
					{
						System.out.println("附件" + attrFile.getAbsoluteFile() + "不存在");
						//e.printStackTrace();
					}
				}
			}
			
			if(xmlPicsMedias != null && xmlPicsMedias.size() > 0)
			{
				String xmlfilename = (String)xmlPicsMedias.get(0);
				FileInputStream fin1 = new FileInputStream(path + "cms\\docManage\\temp\\" + xmlfilename);
				out.putNextEntry(new ZipEntry(xmlfilename));
				
				while ((len = fin1.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				fin1.close();
				File fff = new File(path + "cms\\docManage\\temp\\" + xmlfilename);
				fff.delete();
			}
			out.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return zipFilename;
	}
	/**
	 * 把文档<Document>的基本信息导出到一个xml文件
	 * @param int[] docIds(文档id) 
	 * @param String path(文件保存路径)
	 * @return
	 */
	public static List toXml(int[] docIds,String path)
	{
		List xmlPicsMedias = new ArrayList();
		String xmlFilename = "backupDocuments" + System.currentTimeMillis() + ".xml";
		xmlPicsMedias.add(xmlFilename);
		path = path + "cms\\docManage\\temp\\" + xmlFilename;
		File xml = new File(path);
		
		try
		{
			FileOutputStream fout = new FileOutputStream(xml);
			StringBuffer sb = new StringBuffer();//缓冲
			sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
			sb.append("\n<DOCUMENTS>");
			
			DBUtil db = new DBUtil();
			DBUtil db2 = new DBUtil();
			DBUtil db3 = new DBUtil();
			for(int a=0;a<docIds.length;a++)
			{
				String sql1 = "select * from td_cms_document t where t.document_id = " + docIds[a];
				db.executeSelect(sql1);
				if(db.size()>0)
				{
					sb.append("\n\t<DOCUMENT>");
					sb.append("\n\t\t<PROPERTIES>");//<PROPERTIES>
					
					sb.append("\n\t\t\t<DOCUMENT_ID>");
					sb.append(db.getInt(0,"DOCUMENT_ID"));
					sb.append("</DOCUMENT_ID>");
					
					sb.append("\n\t\t\t<TITLE><![CDATA[");
					sb.append(db.getString(0,"TITLE"));
					sb.append("]]></TITLE>");
					
					sb.append("\n\t\t\t<SUBTITLE><![CDATA[");
					sb.append(db.getString(0,"SUBTITLE"));
					sb.append("]]></SUBTITLE>");
					
					sb.append("\n\t\t\t<AUTHOR><![CDATA[");
					sb.append(db.getString(0,"AUTHOR"));
					sb.append("]]></AUTHOR>");
					
					sb.append("\n\t\t\t<CONTENT><![CDATA[");
					sb.append(db.getString(0,"CONTENT"));
					sb.append("]]></CONTENT>");
					
					sb.append("\n\t\t\t<CHANNEL_ID>");
					sb.append(String.valueOf(db.getInt(0,"CHANNEL_ID")));
					sb.append("</CHANNEL_ID>");
					
					sb.append("\n\t\t\t<STATUS>");
					sb.append(String.valueOf(db.getInt(0,"STATUS")));
					sb.append("</STATUS>");
					
					sb.append("\n\t\t\t<KEYWORDS><![CDATA[");
					sb.append(db.getString(0,"KEYWORDS"));
					sb.append("]]></KEYWORDS>");
					
					sb.append("\n\t\t\t<DOCABSTRACT><![CDATA[");
					sb.append(db.getString(0,"DOCABSTRACT"));
					sb.append("]]></DOCABSTRACT>");
					
					sb.append("\n\t\t\t<DOCTYPE>");
					sb.append(String.valueOf(db.getInt(0,"DOCTYPE")));
					sb.append("</DOCTYPE>");
					
					sb.append("\n\t\t\t<DOCWTIME>");
					sb.append(db.getDate(0,"DOCWTIME").toString());
					sb.append("</DOCWTIME>");
					
					sb.append("\n\t\t\t<TITLECOLOR>");
					sb.append(db.getString(0,"TITLECOLOR"));
					sb.append("</TITLECOLOR>");
					
					sb.append("\n\t\t\t<CREATETIME>");
					sb.append(db.getDate(0,"CREATETIME").toString());
					sb.append("</CREATETIME>");
					
					sb.append("\n\t\t\t<CREATEUSER>");
					sb.append(String.valueOf(db.getInt(0,"CREATEUSER")));
					sb.append("</CREATEUSER>");
					
					sb.append("\n\t\t\t<DOCSOURCE_ID>");
					sb.append(String.valueOf(db.getInt(0,"DOCSOURCE_ID")));
					sb.append("</DOCSOURCE_ID>");
					
					sb.append("\n\t\t\t<DETAILTEMPLATE_ID>");
					sb.append(String.valueOf(db.getInt(0,"DETAILTEMPLATE_ID")));
					sb.append("</DETAILTEMPLATE_ID>");
					
					sb.append("\n\t\t\t<LINKTARGET>");
					sb.append(db.getString(0,"LINKTARGET"));
					sb.append("</LINKTARGET>");
					
					sb.append("\n\t\t\t<FLOW_ID>");
					sb.append(String.valueOf(db.getInt(0,"FLOW_ID")));
					sb.append("</FLOW_ID>");
					
					sb.append("\n\t\t\t<DOC_LEVEL>");
					sb.append(String.valueOf(db.getInt(0,"DOC_LEVEL")));
					sb.append("</DOC_LEVEL>");
					
					sb.append("\n\t\t\t<DOC_KIND>");
					sb.append(String.valueOf(db.getInt(0,"DOC_KIND")));
					sb.append("</DOC_KIND>");
					
					sb.append("\n\t\t\t<PARENT_DETAIL_TPL>");
					sb.append(db.getString(0,"PARENT_DETAIL_TPL"));
					sb.append("</PARENT_DETAIL_TPL>");
					
					sb.append("\n\t\t\t<PIC_PATH>");
					sb.append(db.getString(0,"PIC_PATH"));
					sb.append("</PIC_PATH>");
					
					if(!"".equals(db.getString(0,"PIC_PATH")))//如果有主题图片
					{
						if(!xmlPicsMedias.contains(db.getString(0,"PIC_PATH")))
							xmlPicsMedias.add(db.getString(0,"PIC_PATH"));
					}	
					
					sb.append("\n\t\t\t<MEDIAPATH>");
					sb.append(db.getString(0,"MEDIAPATH"));
					sb.append("</MEDIAPATH>");
					
					if(!"".equals(db.getString(0,"MEDIAPATH")))//如果有多媒体文件
					{
						if(!xmlPicsMedias.contains(db.getString(0,"MEDIAPATH")))
						{
							xmlPicsMedias.add(db.getString(0,"MEDIAPATH"));
						}
					}
					
					//文档发布名称，add by xinwang.jiao 2007.08.23
					sb.append("\n\t\t\t<PUBLISHFILENAME>");
					sb.append(db.getString(0,"PUBLISHFILENAME"));
					sb.append("</PUBLISHFILENAME>");
					
					//文档副标题，add by xinwang.jiao 2007.08.23
					sb.append("\n\t\t\t<SECONDTITLE>");
					sb.append(db.getString(0,"SECONDTITLE"));
					sb.append("</SECONDTITLE>");
					
					//add by xinwang.jiao 2007.09.19
					//是否要标记为new文档，1：是，0：否 ，缺省为0
					sb.append("\n\t\t\t<ISNEW>");
					sb.append(db.getInt(0,"ISNEW"));
					sb.append("</ISNEW>");
					
					//add by xinwang.jiao 2007.09.19
					//new标记图片路径
					sb.append("\n\t\t\t<NEWPIC_PATH>");
					sb.append(db.getString(0,"NEWPIC_PATH"));
					sb.append("</NEWPIC_PATH>");
					
					if(!"".equals(db.getString(0,"NEWPIC_PATH")))//如果有new标记图片
					{
						if(!xmlPicsMedias.contains(db.getString(0,"NEWPIC_PATH")))
						{
							xmlPicsMedias.add(db.getString(0,"NEWPIC_PATH"));
						}
					}
					
					sb.append("\n\t\t</PROPERTIES>");//</PROPERTIES>
					sb.append("\n\t\t<APPENDIXS>");//<APPENDIXS>
					String sql2 = "select * from td_cms_doc_attach t where t.document_id = " + docIds[a] + " order by t.type";
					
					db2.executeSelect(sql2);
					if(db2.size()>0)
					{
						for(int b=0;b<db2.size();b++)
						{
							sb.append("\n\t\t\t<APPENDIX>");
							
							sb.append("\n\t\t\t\t<ID>");
							sb.append(String.valueOf(db2.getInt(0,"ID")));
							sb.append("</ID>");
							
							sb.append("\n\t\t\t\t<DOCUMENT_ID>");
							sb.append(String.valueOf(db2.getInt(0,"DOCUMENT_ID")));
							sb.append("</DOCUMENT_ID>");
							
							sb.append("\n\t\t\t\t<URL>");
							sb.append(db2.getString(b,"URL"));
							sb.append("</URL>");
							
							sb.append("\n\t\t\t\t<TYPE>");
							sb.append(String.valueOf(db2.getInt(0,"TYPE")));
							sb.append("</TYPE>");
							
							sb.append("\n\t\t\t\t<DESCRIPTION><![CDATA[");
							sb.append(db2.getString(b,"DESCRIPTION"));
							sb.append("]]></DESCRIPTION>");
							
							sb.append("\n\t\t\t\t<ORIGINAL_FILENAME><![CDATA[");
							sb.append(db2.getString(b,"ORIGINAL_FILENAME"));
							sb.append("]]></ORIGINAL_FILENAME>");
							
							sb.append("\n\t\t\t\t<VALID>");
							sb.append(db2.getString(b,"VALID"));
							sb.append("</VALID>");
							
							sb.append("\n\t\t\t</APPENDIX>");
						}
					}
					
					sb.append("\n\t\t</APPENDIXS>");//</APPENDIXS>
					
					sb.append("\n\t\t<EXTFIELDVALUES>");//<EXTFIELDVALUES>
					String sql3 = "select * from td_cms_extfieldvalue t where t.document_id = " + docIds[a];
					db3.executeSelect(sql3);
					if(db3.size() > 0)
					{
						for(int c=0;c<db3.size();c++)
						{
							sb.append("\n\t\t\t<EXTFIELDVALUE>");
							
							sb.append("\n\t\t\t\t<FIELD_ID>");
							sb.append(String.valueOf(db3.getInt(c,"FIELD_ID")));
							sb.append("</FIELD_ID>");
							
							sb.append("\n\t\t\t\t<DOCUMENT_ID>");
							sb.append(String.valueOf(db3.getInt(c,"DOCUMENT_ID")));
							sb.append("</DOCUMENT_ID>");
							
							sb.append("\n\t\t\t\t<FIELDVALUE><![CDATA[");
							sb.append(db3.getString(c,"FIELDVALUE"));
							sb.append("]]></FIELDVALUE>");
							
							sb.append("\n\t\t\t\t<NUMBERVALUE>");
							sb.append(String.valueOf(db3.getInt(c,"NUMBERVALUE")));
							sb.append("</NUMBERVALUE>");
							
							sb.append("\n\t\t\t\t<CLOBVALUE><![CDATA[");
							sb.append(db3.getString(c,"CLOBVALUE"));
							sb.append("]]></CLOBVALUE>");
							
							sb.append("\n\t\t\t\t<DATEVALUE>");
							sb.append(String.valueOf(db3.getDate(c,"DATEVALUE")));
							sb.append("</DATEVALUE>");
							
							sb.append("\n\t\t\t</EXTFIELDVALUE>");
						}
					}
					
					sb.append("\n\t\t</EXTFIELDVALUES>");//</EXTFIELDVALUES>
					
					sb.append("\n\t</DOCUMENT>");
				}
			}
			//扩展字段定义start
			sb.append("\n\t<EXTFIELDS>");
			
			if(docIds != null && docIds.length > 0)
			{
				String sql1 = "select t.* from td_cms_extfield t inner join td_cms_channelfield a " +
						"on t.field_id = a.field_id where a.channel_id = " +
						"(select b.channel_id from td_cms_document b where b.document_id = " + docIds[0] + ")";
				db.executeSelect(sql1);
				if(db.size() > 0)
				{
					for(int a = 0;a < db.size();a ++)
					{
						sb.append("\n\t\t<EXTFIELD>");
						
						//EXTFIELD info
						sb.append("\n\t\t\t<EXTFIELD_INFO>");
						
						sb.append("\n\t\t\t\t<FIELD_ID>");
						sb.append(db.getInt(a,"FIELD_ID"));
						sb.append("</FIELD_ID>");
						
						sb.append("\n\t\t\t\t<FIELDNAME><![CDATA[");
						sb.append(db.getString(a,"FIELDNAME"));
						sb.append("]]></FIELDNAME>");
						
						sb.append("\n\t\t\t\t<FIELDLABEL><![CDATA[");
						sb.append(db.getString(a,"FIELDLABEL"));
						sb.append("]]></FIELDLABEL>");
						
						sb.append("\n\t\t\t\t<FIELDDESC><![CDATA[");
						sb.append(db.getString(a,"FIELDDESC"));
						sb.append("]]></FIELDDESC>");
						
						sb.append("\n\t\t\t\t<FIELDTYPE><![CDATA[");
						sb.append(db.getString(a,"FIELDTYPE"));
						sb.append("]]></FIELDTYPE>");
						
						sb.append("\n\t\t\t\t<MAXLEN>");
						sb.append(String.valueOf(db.getInt(a,"MAXLEN")));
						sb.append("</MAXLEN>");
						
						sb.append("\n\t\t\t\t<INPUTTYPE>");
						sb.append(String.valueOf(db.getInt(a,"INPUTTYPE")));
						sb.append("</INPUTTYPE>");
						
						sb.append("\n\t\t\t</EXTFIELD_INFO>");
						
						//扩展字段取值范围
						sb.append("\n\t\t\t<EXTVALUESCOPES>");
						
						String sql2 = "select * from td_cms_extvaluescope t where t.field_id = " + db.getInt(a,"FIELD_ID");
						db2.executeSelect(sql2);
						if(db2.size() > 0)
						{
							for(int b=0;b<db2.size();b++)
							{
								sb.append("\n\t\t\t\t<EXTVALUESCOPE>");
								
								sb.append("\n\t\t\t\t\t<FIELD_ID>");
								sb.append(String.valueOf(db2.getInt(b,"FIELD_ID")));
								sb.append("</FIELD_ID>");
								
								sb.append("\n\t\t\t\t\t<VALUE><![CDATA[");
								sb.append(db2.getString(b,"VALUE"));
								sb.append("]]></VALUE>");
								
								sb.append("\n\t\t\t\t\t<ID>");
								sb.append(String.valueOf(db2.getInt(b,"ID")));
								sb.append("</ID>");
								
								sb.append("\n\t\t\t\t\t<DESCRIPTION><![CDATA[");
								sb.append(db2.getString(b,"DESCRIPTION"));
								sb.append("]]></DESCRIPTION>");   
								
								sb.append("\n\t\t\t\t\t<MINVALUE>");
								sb.append(String.valueOf(db2.getInt(b,"MINVALUE")));
								sb.append("</MINVALUE>");
								
								sb.append("\n\t\t\t\t\t<MAXVALUE>");
								sb.append(String.valueOf(db2.getInt(b,"maxvalue_")));
								sb.append("</MAXVALUE>");
								  
								sb.append("\n\t\t\t\t</EXTVALUESCOPE>");
							}
						}
						
						sb.append("\n\t\t\t</EXTVALUESCOPES>");
						
						sb.append("\n\t\t</EXTFIELD>");
					}
				}
			}
			
			sb.append("\n\t</EXTFIELDS>");
			
			sb.append("\n</DOCUMENTS>");
			//扩展字段定义end
			fout.write(sb.toString().getBytes());
			fout.close();
		}catch(Exception e)
		{
			xml.deleteOnExit();
			System.out.println(11);
			e.printStackTrace();
		}
		//return xmlFilename;
		return xmlPicsMedias;
	}
	/**
	 * 解压zip文件
	 * @param String chlId(指定目的频道)
	 * @param String path(文件保存路径,从页面传过来应用的path,再拼)<"D:\workspace\cms\creatorcms\">
	 * @param String zipfilename
	 * @param String attrpath(附件文件保存路径,从页面传过来,再拼)<"site4">
	 * @param String type
	 *  DocZipUtil.FORM_LOCAL 导入的zip文件是从服务器选择
	 *  DocZipUtil.FORM_REMOTE 导入的zip文件来自上传
	 * @return
	 */
	public static String unZip(String chlId,String path,String zipfilename,String attrpath,String type)
	{
		String xmlfile = "";
		try {
			ChannelManager cm = new ChannelManagerImpl();
			String channelPath = cm.getChannelInfo(chlId).getChannelPath();
			String zip = path + "\\cms\\docManage\\temp\\upload\\" + zipfilename;
			if(type.trim().equals(DocZipUtil.FORM_LOCAL))
			{
				zip = path + "\\cms\\docManage\\temp\\" + zipfilename;
			}
			ZipInputStream zin = new ZipInputStream(new FileInputStream(zip));
			ZipEntry ze;
			byte[] buf = new byte[1024];
			while((ze = zin.getNextEntry())!=null)
			{
				String filename = ze.getName();
				String fileExt = filename.substring(filename.indexOf(".")+1,filename.length());
				File f = new File(path + "\\cms\\siteResource\\" + attrpath + "\\_webprj\\" + channelPath + "\\content_files\\" + filename);
				
				if("xml".equals(fileExt))
				{
					f = new File(path + "\\cms\\docManage\\temp\\unzip\\" + filename);
					xmlfile = path + "\\cms\\docManage\\temp\\unzip\\" + filename;
				}
				if(filename.toLowerCase().startsWith("_template"))
				{
					filename = filename.replaceFirst("_template/","");
					String parentDir = filename;
					if(parentDir.lastIndexOf("/") != -1)
						parentDir = parentDir.substring(0,parentDir.lastIndexOf("/"));
					else
						parentDir = "";
					f = new File(path + "\\cms\\siteResource\\" + attrpath + "\\_template\\" + parentDir);
					if(!f.exists())
					{
						f.mkdirs();
					}
					f = new File(path + "\\cms\\siteResource\\" + attrpath + "\\_template\\" + filename);
				}
				
				//如果目录不存在则新建
				File parentFile = new File(path + "cms\\siteResource\\" + attrpath + "\\_webprj\\" + channelPath + "\\content_files\\");
				if(!parentFile.exists())
				{
					parentFile.mkdirs();
				}
				
				if(f.exists())
					continue;
				try
				{
					FileOutputStream out = new FileOutputStream(f);
					int len;
				    while ((len = zin.read(buf)) > 0)
				    	out.write(buf,0,len);
				    out.close();
				}
				catch(Exception e)
				{
					System.out.println("读取文件" + f.getAbsolutePath() + "时出错！");
				}
			    zin.closeEntry();
			}
			
			zin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlfile;
	}
	/**
	 * 从xml中加载数据到DB
	 * @param String chlId 指定导入的频道
	 * @param String type
	 *  DocZipUtil.FORM_LOCAL 导入的zip文件是从服务器选择
	 *  DocZipUtil.FORM_REMOTE 导入的zip文件来自上传
	 * @return
	 */
	public static int xml2DB(HttpServletRequest request,HttpServletResponse response,String chlId,String path,String zipfilename,String attrpath,String type)
	{
		int i = 0;
		try
		{
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(request, response);
			
			if(type == null || "".equals(type.trim()))
			{
				return i;
			}
			
			String xml = unZip(chlId,path,zipfilename,attrpath,type);
			Channel channel = new ChannelManagerImpl().getChannelInfo(chlId);
			File inputXml = new File(xml);
			SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputXml);
            
            List listDocs = document.selectNodes("//DOCUMENTS/DOCUMENT");
            Iterator iterDocs = listDocs.iterator();
            while (iterDocs.hasNext()) //DOCUMENT
            {
            	int docId = 0;
            	DocumentManager dm = new DocumentManagerImpl();
            	com.frameworkset.platform.cms.documentmanager.Document dc = new com.frameworkset.platform.cms.documentmanager.Document();
            	Attachment docattr = new Attachment();
            	Element doc = (Element) iterDocs.next();
            	Iterator iterDoc = doc.elementIterator();
                while(iterDoc.hasNext())//PROPERTIES or APPENDIXS
                {
                	Element element = (Element)iterDoc.next();
                	if("PROPERTIES".equals(element.getName()))
        			{
                		Iterator properties = element.elementIterator();
                    	while(properties.hasNext())//PROPERTIES
                    	{
                    		//SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    		Element property = (Element)properties.next();
                    		if("TITLE".equals(property.getName()))
                    			dc.setTitle(property.getText());
                    		if("SUBTITLE".equals(property.getName()))
                    			dc.setSubtitle(property.getText());
                    		if("AUTHOR".equals(property.getName()))
                    			dc.setAuthor(property.getText());
                    		if("CONTENT".equals(property.getName()))
                    			dc.setContent(property.getText());
                    		//if("CHANNEL_ID".equals(property.getName()))
                    			//dc.setChanel_id(Integer.parseInt(property.getText()));
                    		
//                    		if("STATUS".equals(property.getName()))
//                    			dc.setStatus(Integer.parseInt(property.getText()));
                    		if("KEYWORDS".equals(property.getName()))
                    			dc.setKeywords(property.getText());
                    		if("DOCABSTRACT".equals(property.getName()))
                    			dc.setDocabstract(property.getText());
                    		if("DOCTYPE".equals(property.getName()))
                    			dc.setDoctype(Integer.parseInt(property.getText()));
//                    		if("DOCWTIME".equals(property.getName()))
//                    		{
//                    			String docWriteTime = property.getText();
//                    			dc.setDocwtime(sf.parse(docWriteTime));
//                    		}
                    		
                    		if("TITLECOLOR".equals(property.getName()))
                    			dc.setTitlecolor(property.getText());
//                    		if("CREATETIME".equals(property.getName()))
//                    		{
//                    			String docCreateTime = property.getText();
//                    			dc.setCreateTime(sf.parse(docCreateTime));
//                    		}
                    			
//                    		if("CREATEUSER".equals(property.getName()))
//                    			dc.setCreateUser(Integer.parseInt(property.getText()));
                    		if("DOCSOURCE_ID".equals(property.getName()))
                    			dc.setDocsource_id(Integer.parseInt(property.getText()));
//                    		if("DETAILTEMPLATE_ID".equals(property.getName()))
//                    			dc.setDetailtemplate_id(Integer.parseInt(property.getText()));
                    		if("LINKTARGET".equals(property.getName()))
                    			dc.setLinktarget(property.getText());
//                    		if("FLOW_ID".equals(property.getName()))
//                    			dc.setFlowId(Integer.parseInt(property.getText()));
                    		if("DOC_LEVEL".equals(property.getName()))
                    			dc.setDoc_level(Integer.parseInt(property.getText()));
                    		if("DOC_KIND".equals(property.getName()))
                    			dc.setDoc_kind(Integer.parseInt(property.getText()));
//                    		if("PARENT_DETAIL_TPL".equals(property.getName()))
//                    			dc.setParentDetailTpl(property.getText());
                    		if("PIC_PATH".equals(property.getName()))
                    			dc.setPicPath(property.getText());
                    		if("MEDIAPATH".equals(property.getName()))
                    			dc.setMediapath(property.getText());
                    		if("PUBLISHFILENAME".equals(property.getName()))
                    			dc.setPublishfilename(property.getText());
                    		if("SECONDTITLE".equals(property.getName()))
                    			dc.setSecondtitle(property.getText());
                    		if("ISNEW".equals(property.getName()))
                    			dc.setIsNew(Integer.parseInt(property.getText()));
                    		if("NEWPIC_PATH".equals(property.getName()))
                    			dc.setNewPicPath(property.getText());
                                dc.setOrdertime(new Date());
                    	}
                    	dc.setChanel_id(Integer.parseInt(chlId));//频道由用户指定
                    	dc.setCreateUser(Integer.parseInt(accesscontroler.getUserID()));//createuser为登录用户
                    	dc.setStatus(1);//为新稿
                    	dc.setDocwtime(new Date());
                    	dc.setCreateTime(new Date());
                    	dc.setDetailtemplate_id(channel.getDetailTemplateId());//细览模板继承频道的
                    	dc.setParentDetailTpl("1");
                    	dc.setFlowId(channel.getWorkflow());//流程为频道流程
                    	
                    	docId = dm.creatorDoc(dc);
        			}
                	if("APPENDIXS".equals(element.getName()))
        			{
                		Iterator appendixs = element.elementIterator();
                    	while(appendixs.hasNext())//APPENDIXS
                    	{
                    		Element appendix = (Element)appendixs.next();
                    		Iterator appendixIter = appendix.elementIterator();
                    		while(appendixIter.hasNext())//APPENDIX
                    		{
                    			Element appele = (Element)appendixIter.next();
                    			docattr.setDocumentId(docId);
                    			if("URL".equals(appele.getName()))
                    				docattr.setUrl(appele.getText());
                    			if("TYPE".equals(appele.getName()))
                    				docattr.setType(Integer.parseInt(appele.getText()));
                    			if("DESCRIPTION".equals(appele.getName()))
                    				docattr.setDescription(appele.getText());
                    			if("ORIGINAL_FILENAME".equals(appele.getName()))
                    				docattr.setOriginalFilename(appele.getText());
                    			if("VALID".equals(appele.getName()))
                    				docattr.setValid(appele.getText());
                    		}
                    		dm.createAttachment(docattr);
                    	}
        			}
                }
                i ++;
            }
            inputXml.delete();//删除xml文件
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return i;
	}
}
