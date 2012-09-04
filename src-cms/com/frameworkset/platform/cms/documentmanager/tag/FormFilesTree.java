package com.frameworkset.platform.cms.documentmanager.tag;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.cms.templatemanager.FileResource;
import com.frameworkset.platform.cms.templatemanager.TemplateManagerException;
import com.frameworkset.platform.cms.templatemanager.IndexPageFilter;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class FormFilesTree extends COMTree implements java.io.Serializable{
	private String rootPath = CMSUtil.getAppRootPath();//+CMSUtil.getCMSContextPath()+"/customForm";

    
	public boolean hasSon(ITreeNode father){
		try {
			List fileresources = getFileResource(rootPath,father.getId());
			for(int i=0;fileresources!=null&&i<fileresources.size();i++){
				if(((FileResource)fileresources.get(i)).isDirectory()){
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 资源管理树id：ResourceManager
	 * 站点id=site:siteid
	 * 频道id=channelid:site:siteid
	 * 频道根id=channel:site:siteid
	 */
	public boolean setSon(ITreeNode father, int curLevel){
		List fileresources = null;
		try {
			fileresources = getFileResource(rootPath,father.getId());
		} catch (TemplateManagerException e) {
			e.printStackTrace();
		}
		
		for(int i=0;fileresources!=null&&i<fileresources.size();i++){
			FileResource fr = (FileResource)fileresources.get(i);
			if(fr.isDirectory()){
				Map params = new HashMap();
				String uri = fr.getUri();
				if(!uri.endsWith("/")&&!uri.endsWith("\\")){
					uri += "/";
				}
				//System.out.println("uri = "+uri);
				params.put("uri",uri);
				addNode(father,uri,fr.getName(),"customForm",
						true, curLevel, (String)null,(String)null,(String)null,params);
				
			}
		}
		return true;
	}
	public static List getFileResource(String rootpath,String uri) throws TemplateManagerException{
		if(rootpath==null || rootpath.trim().length()==0){
			throw new TemplateManagerException("没有提供上下文根路径,无法对模板文件进行管理");
		}
		File root = null;
		String uriPrefix = "";
		if(uri==null||uri.trim().length()==0){
			root = new File(rootpath);
		}else{
			root = new File(rootpath,uri);
			if(uri.endsWith("/")||uri.endsWith("\\")){
				uriPrefix = uri;
			}else{
				uriPrefix = uri+"/";
			}
		}
		List fileResources = new ArrayList();
		//File[] subFiles = root.listFiles(new IndexPageFilter(true,uri));
		File[] subFiles = root.listFiles();
		FileResource fr = null;
		String subFileName = null;
		for(int i=0;subFiles!=null&&i<subFiles.length;i++){
			subFileName = subFiles[i].getName();
			if(subFileName != null && 
					(subFileName.equals("WEB-INF") || subFileName.equals("sysmanager")
					|| subFileName.equals("sitepublish") || subFileName.equals("sitepreview")))
				continue;
			fr = new FileResource();
			fr.setUri(uriPrefix + subFileName + "/");
			fr.setName(subFiles[i].getName());
			fr.setDirectory(subFiles[i].isDirectory());
			fileResources.add(fr);
		}
		return fileResources;
	}
	
	public static List getFiles(String rootpath,String uri) throws TemplateManagerException{
		if(rootpath==null || rootpath.trim().length()==0){
			throw new TemplateManagerException("没有提供上下文根路径,无法对模板文件进行管理");
		}
		File root = null;
		String uriPrefix = "";
		if(uri==null||uri.trim().length()==0){
			root = new File(rootpath);
		}else{
			root = new File(rootpath,uri);
			if(uri.endsWith("/")||uri.endsWith("\\")){
				uriPrefix = uri;
			}else{
				uriPrefix = uri+"/";
			}
		}
		List fileResources = new ArrayList();
		File[] subFiles = root.listFiles(new IndexPageFilter(true,uri));
		FileResource fr = null;
		String subFileName = null;
		for(int i=0;subFiles!=null&&i<subFiles.length;i++){
			subFileName = subFiles[i].getName();
			fr = new FileResource();
			fr.setUri(uriPrefix + subFileName + "/");
			fr.setName(subFiles[i].getName());
			fr.setDirectory(subFiles[i].isDirectory());
			fileResources.add(fr);
		}
		return fileResources;
	}
}
