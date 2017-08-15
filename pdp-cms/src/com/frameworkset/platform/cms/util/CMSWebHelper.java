package com.frameworkset.platform.cms.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fiyu.edit.UploadBean;

public class CMSWebHelper {
	private static Logger log = LoggerFactory.getLogger(CMSWebHelper.class);
	private Map<String,UploadBean > styles = new HashMap<String,UploadBean >();
	private String fileName;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	static String ReadFile(String s_FileName){
		try
		{
			return FileUtil.getFileContent(s_FileName, "utf-8");
		}
		catch(Exception e)
		{
			return "";
		}
		/**String s_Result = "";
		try {
			java.io.File objFile;
			java.io.FileReader objFileReader;
			char[] chrBuffer = new char[10];
			int intLength;

			objFile = new java.io.File(s_FileName);

			if(objFile.exists()){
				objFileReader = new java.io.FileReader(objFile);
				while((intLength=objFileReader.read(chrBuffer))!=-1){
					s_Result += String.valueOf(chrBuffer,0,intLength);
				}
				objFileReader.close();
			}
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
		return s_Result;
		*/
	}
	static String[] split(String source, String div) {
	    int arynum = 0, intIdx = 0, intIdex = 0, div_length = div.length();
	    if (source.compareTo("") != 0) {
	      if (source.indexOf(div) != -1) {
	        intIdx = source.indexOf(div);
	        for (int intCount = 1; ; intCount++) {
	          if (source.indexOf(div, intIdx + div_length) != -1) {
	            intIdx = source.indexOf(div, intIdx + div_length);
	            arynum = intCount;
	          }
	          else {
	            arynum += 2;
	            break;
	          }
	        }
	      }
	      else {
	        arynum = 1;
	      }
	    }
	    else {
	      arynum = 0;

	    }
	    intIdx = 0;
	    intIdex = 0;
	    String[] returnStr = new String[arynum];

	    if (source.compareTo("") != 0) {
	      if (source.indexOf(div) != -1) {
	        intIdx = (int) source.indexOf(div);
	        returnStr[0] = (String) source.substring(0, intIdx);
	        for (int intCount = 1; ; intCount++) {
	          if (source.indexOf(div, intIdx + div_length) != -1) {
	            intIdex = (int) source.indexOf(div, intIdx + div_length);
	            returnStr[intCount] = (String) source.substring(intIdx + div_length,intIdex);
	            intIdx = (int) source.indexOf(div, intIdx + div_length);
	          }
	          else {
	            returnStr[intCount] = (String) source.substring(intIdx + div_length,source.length());
	            break;
	          }
	        }
	      }
	      else {
	        returnStr[0] = (String) source.substring(0, source.length());
	        return returnStr;
	      }
	    }
	    else {
	      return returnStr;
	    }
	    return returnStr;
	}
	
	static UploadBean getUploadBean(String[] aStyleConfig)
	{
		UploadBean bean = new UploadBean();
//		 if (sType.equals("REMOTE"))
		 {
			  	String sAllowExt = aStyleConfig[10];
			  	int nAllowSize = Integer.valueOf(aStyleConfig[15]).intValue();
//			  	bean.setSremoteext(getNodeValue(list, "sremoteext"));
//			      bean.setSremotesize(getNodeValue(list, "sremotesize"));
			  	bean.setSremoteext(sAllowExt);
			      bean.setSremotesize(aStyleConfig[15]);
			  } 
//		 else if (sType.equals("FILE"))
		 {
			  	String sAllowExt = aStyleConfig[6];
			  	int nAllowSize = Integer.valueOf(aStyleConfig[11]).intValue();
//			  	bean.setSfileext(getNodeValue(list, "sfileext"));
//			      bean.setSfilesize(getNodeValue(list, "sfilesize"));
			  	bean.setSfileext(sAllowExt);
			    bean.setSfilesize(aStyleConfig[11]);
			  } 
//		 else if (sType.equals("MEDIA"))
		 {
			  	String sAllowExt = aStyleConfig[9];
			  	int nAllowSize = Integer.valueOf(aStyleConfig[14]).intValue();
//			  	 bean.setSmediaext(getNodeValue(list, "smediaext"));
//			      bean.setSmediasize(getNodeValue(list, "smediasize"));
			  	 bean.setSmediaext(sAllowExt);
			      bean.setSmediasize(aStyleConfig[14]);
			  } 
//		 else if (sType.equals("FLASH"))
		 {
			  	String sAllowExt = aStyleConfig[7];
			  	int nAllowSize = Integer.valueOf(aStyleConfig[12]).intValue();
//			    bean.setSflashext(getNodeValue(list, "sflashext"));
//			      bean.setSflashsize(getNodeValue(list, "sflashsize"));
			      
			      bean.setSflashext(sAllowExt);
			      bean.setSflashsize(aStyleConfig[12]);
			  } 
//		 else 
		 {
			  	String sAllowExt = aStyleConfig[8];
			  	int nAllowSize = Integer.valueOf(aStyleConfig[13]).intValue();
//			    bean.setSimageext(getNodeValue(list, "simageext"));
//			      bean.setSimagesize(getNodeValue(list, "simagesize"));
			  	bean.setSimageext(sAllowExt);
			      bean.setSimagesize(aStyleConfig[13]);
			  }
		 
		 String sUploadDir = aStyleConfig[3];
		 bean.setSuploaddir(sUploadDir);
//		bean.setSfileext(getNodeValue(list, "sfileext"));
//	      bean.setSfilesize(getNodeValue(list, "sfilesize"));
//	      bean.setSflashext(getNodeValue(list, "sflashext"));
//	      bean.setSflashsize(getNodeValue(list, "sflashsize"));
//	      bean.setSimageext(getNodeValue(list, "simageext"));
//	      bean.setSimagesize(getNodeValue(list, "simagesize"));
//	      bean.setSmediaext(getNodeValue(list, "smediaext"));
//	      bean.setSmediasize(getNodeValue(list, "smediasize"));
//	      bean.setSremoteext(getNodeValue(list, "sremoteext"));
//	      bean.setSremotesize(getNodeValue(list, "sremotesize"));
//	      bean.setSuploaddir(getNodeValue(list, "suploaddir"));
		return bean;
	}
	
	static ArrayList getConfigArray(String s_Key, String s_Config){
		ArrayList a_Result = new ArrayList();
		Pattern p = Pattern.compile(s_Key + " = \"(.*)\";");
		Matcher m = p.matcher(s_Config);
		while (m.find()) {
			a_Result.add(m.group(1));
		}
		return a_Result;
	}
	  public void InitPara()
	  {
		  String sConfig = null;
		  if(StringUtil.isEmpty(fileName ))
		  {
				  
			  String approot = CMSUtil.getAppRootPath();
			  String eWebEditorPath=approot + "/cms/editor/eWebEditor48/";
			  String sFileSeparator = File.separator;
			  String path = eWebEditorPath+"jsp"+sFileSeparator+"config.jsp";
			  File f = new File(path);
			  if(!f.exists())
			  {
				  log.debug("读取eWebEditor48 config.jsp文件失败：文件路径"+path+"不存在，请检查properties-sys.xml文件中的approot配置是否正确.");
				  return;
			  }
			  sConfig = ReadFile(path);
		  }
		  else
		  {
			  sConfig = ReadFile(this.fileName);
		  }
		  ArrayList aStyle = getConfigArray("aStyle", sConfig);




		  String[] aStyleConfig = new String[27];
		  boolean bValidStyle = false;

		  for (int i = 0; i < aStyle.size(); i++){
		  	aStyleConfig = split(aStyle.get(i).toString(), "|||");
		  	UploadBean b = getUploadBean(aStyleConfig);
		  	if(b != null)
		  	{
		  		this.styles.put(aStyleConfig[0].toLowerCase(), b);
		  	}
		  
		  }

		 




		
	  }

//	  public CMSUploadWebHelper getInstance()
//	  {
//		  CMSUploadWebHelper uw = new CMSUploadWebHelper();
//	    
//	    return uw;
//	  }
	  
	  public UploadBean getUploadBean(String style)
	  {
		  return this.styles.get(style);
	  }

	 

	 
}
