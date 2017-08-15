package com.frameworkset.platform.cms.templatemanager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.platform.cms.CMSManager;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;

/**
 * 本地上传无描述文件的模板包,解压后,临时目录的html/htm文件树形
 * <p>Title: TemplateWaterPicTree.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-10-16 16:51:16
 * @author ge.tao
 * @version 1.0
 */
public class TemplateupzipFolderTree extends COMTree implements java.io.Serializable{
	private static Logger log = LoggerFactory.getLogger(TemplateupzipFolderTree.class);
	private String rootPath;
	private String siteId;
    
		
	private void setRootPath()throws TemplateManagerException{
		if(rootPath == null || rootPath.trim().length()==0||siteId==null||siteId.trim().length()==0){
			CMSManager cmsmanager = new CMSManager();
			cmsmanager.init(request,session,response,super.accessControl);
			Site site = cmsmanager.getCurrentSite();
			siteId = ""+site.getSiteId();	
			//request.getParameter("rootPath")
			rootPath = CMSUtil.getPath(CMSUtil.getAppRootPath() , "cms/siteResource/siteTemplate");
			rootPath.replaceAll("\\\\","/");
		}
	}
    
	public boolean hasSon(ITreeNode father){
		try {
			this.setRootPath();			
			if(father.isRoot()){
				return FileUtil.hasSubDirectory(this.rootPath);					
			}else{
				return FileUtil.hasSubDirectory(this.rootPath,father.getId());
			}
			
		} catch (TemplateManagerException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 设置节点
	 */
	public boolean setSon(ITreeNode father, int curLevel) {
		//String fileFlag = (String)session.getAttribute("fileFlag");
		
		List fileresources = null;
		if(father.isRoot())
			father.setNodeLink("template_import.jsp");   
		try {
			this.setRootPath();
			String rp = request.getParameter("rootPath");
			String sourcePath = CMSUtil.getPath(rootPath,rp);
			fileresources = (new FileManagerImpl()).getTempZipDirResource(siteId,sourcePath,father.getId());
		} catch (TemplateManagerException e) {
			e.printStackTrace();
		}
		for(int i=0;fileresources!=null&&i<fileresources.size();i++){
			FileResource fr = (FileResource)fileresources.get(i);
			if(fr.isDirectory()){
				Map params = new HashMap();
				String filename = fr.getName();
				String uri = fr.getUri();
				if(!uri.endsWith("/")&&!uri.endsWith("\\")){
					uri += "/";
				}
				params.put("uri",uri);
				//传递参数 站点ID, 压缩包解压后目录 压缩包名称等等...
				params.put("nodeLink","template_import.jsp?filename="+filename+"&tmplzipname="+request.getParameter("rootPath")); 
				//params.put("fileFlag",request.getParameter("fileFlag"));
				addNode(father,uri,fr.getName(),"folder",
						true, curLevel, (String)null,(String)null,(String)null,params);
				
			}else{
				
			}
		}
		return true;
	}
}
