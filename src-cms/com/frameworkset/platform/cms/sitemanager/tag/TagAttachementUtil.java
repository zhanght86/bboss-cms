package com.frameworkset.platform.cms.sitemanager.tag;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jspsmart.upload.SmartUpload;
public class TagAttachementUtil implements java.io.Serializable {
	private final static String rarFold = "cms/siteResource/publicAttachment";
	
	
	public List getAttachementList(String rootPath){
		List list = new ArrayList();
		String path = rootPath + rarFold; 
		File file = null;
		try {
			file = new File(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File[] files = {};
		if (file.isDirectory()) {
			files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File tmp = files[i];
				try {
					String absolutePath = tmp.getAbsolutePath();
					String fileName = tmp.getName();
					list.add(new String[]{fileName,absolutePath});					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
		return list;
	}
	
	public void deleteTagAttachements(){
		
	}
	
	public void addTagAttachements(){
		
	}
	
	public static void main(String[] args){
		List list = new TagAttachementUtil().getAttachementList("D:/workspace/CMS/creatorcms/");
		SmartUpload up = new SmartUpload();
		for (int i=0;i<up.getFiles().getCount();i++){
		com.jspsmart.upload.File myFile = up.getFiles().getFile(i);
		myFile.getFileName();
		}
		for(int i=0;i<list.size();i++){
			String[] str = (String[])list.get(i);
			System.out.println("---------------"+str[0]+"------------------------"+str[1]);
		}
	}

	public static String getRarFold() {
		return rarFold;
	}

}
