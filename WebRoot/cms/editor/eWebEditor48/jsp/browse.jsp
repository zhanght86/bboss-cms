<%@ page contentType="text/html;charset=UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%@ page import="java.util.*,java.util.regex.*,java.text.*,java.io.*,java.net.*,javax.imageio.*,java.awt.*,java.awt.image.*,java.awt.geom.*,javax.swing.*" %>
<jsp:useBean id="mySmartUpload" class="com.jspsmart.upload.SmartUpload" scope="page"/>
<%!
/*
*######################################
* eWebEditor v4.00 - Advanced online web based WYSIWYG HTML editor.
* Copyright (c) 2003-2007 eWebSoft.com
*
* For further information go to http://www.ewebsoft.com/
* This copyright notice MUST stay intact for use.
*######################################
*/


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

static String dealNull(String str) {
	String returnstr = null;
	if (str == null) {
		returnstr = "";
	} else {
		returnstr = str;
	}
	return returnstr;
}

static Object dealNull(Object obj) {
	Object returnstr = null;
	if (obj == null){
		returnstr = (Object) ("");
	}else{
		returnstr = obj;
    }
	return returnstr;
}

static String replace(String str, String substr, String restr) {
	String[] tmp = split(str, substr);
	String returnstr = null;
	if (tmp.length != 0) {
		returnstr = tmp[0];
		for (int i = 0; i < tmp.length - 1; i++) {
			returnstr = dealNull(returnstr) + restr + tmp[i + 1];
		}
	}
	return dealNull(returnstr);
}

static String getOutScript(String str){
	String html = "";
	html += "<HTML><HEAD><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'><TITLE>eWebEditor</TITLE></head><body>";
	html += "<script language=javascript>" + str + "</script>";
	html += "</body></html>";
	return html;
}

static boolean CheckValidExt(String s_AllowExt, String sExt){
	if(s_AllowExt.equals("")){
		return true;
	}
	String[] aExt = split(s_AllowExt, "|");
	for (int i = 0; i<aExt.length; i++){
		if (aExt[i].toLowerCase().equals(sExt)) {
			return true;
		}
	}
	return false;
}

static String ReadFile(String s_FileName){
	String s_Result = "";
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

static ArrayList GetFolderTree(String s_Dir, String s_Flag, int n_Indent, int n_TreeIndex, String s_FileSeparator){
	String s_List = "";
	ArrayList aSubFolders = new ArrayList();

	File file = new File(s_Dir);
	File[] filelist = file.listFiles();

	if (filelist != null && filelist.length > 0){
		for (int i = 0; i < filelist.length; i++) {
			if (filelist[i].isDirectory()) {
				aSubFolders.add(filelist[i].getName());
			}
		}

		int n_Count = aSubFolders.size();
		String s_LastFlag = "";
		String s_Folder = "";
		for (int i=1; i<=n_Count; i++){
			if (i < n_Count){
				s_LastFlag = "0";
			}else{
				s_LastFlag = "1";
			}

			s_Folder = aSubFolders.get(i-1).toString();
			s_List = s_List + "arr" + s_Flag + "[" + String.valueOf(n_TreeIndex) + "]=new Array(\"" + s_Folder + "\"," + String.valueOf(n_Indent) + ", " + s_LastFlag + ");\n";
			n_TreeIndex = n_TreeIndex + 1;
			ArrayList a_Temp = GetFolderTree(s_Dir + s_Folder + s_FileSeparator, s_Flag, n_Indent+1, n_TreeIndex, s_FileSeparator);
			s_List = s_List + a_Temp.get(0).toString();
			n_TreeIndex = Integer.valueOf(a_Temp.get(1).toString()).intValue();
		}
	}

	ArrayList a_Return = new ArrayList();
	a_Return.add(s_List);
	a_Return.add(String.valueOf(n_TreeIndex));
	return a_Return;
}

static boolean CheckValidDir(String path){
	java.io.File dir = new java.io.File(path);
	if (dir == null){
		return false;
	}
	if (dir.isFile()){
		return false;
	}
	if (!dir.exists()){
		return false;
	}
	return true;
}

/**
  *
  * modify by xinwang.jiao 2007-9-12
 */
static String RelativePath2RootPath(String url, String s_ServletPath){
	String sTempUrl = url;
	if (sTempUrl.substring(0, 1).equals("/")){
		return sTempUrl;
	}

	String sWebEditorPath = s_ServletPath;
	//
	sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	//
	sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));

	while(sTempUrl.startsWith("../")){//
		sTempUrl = sTempUrl.substring(3);
		sWebEditorPath = s_ServletPath;
		sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
		sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	}
	return sWebEditorPath + "/" + sTempUrl;
}

/*
//modify by xinwang.jiao 2007-9-12
static String RelativePath2RootPath2(String url, String s_ServletPath){
	String sTempUrl = url;
	if (sTempUrl.substring(0, 1).equals("/")){
		return sTempUrl;
	}

	String sWebEditorPath = s_ServletPath;
	sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));

	while(sTempUrl.startsWith("../")){
		sTempUrl = sTempUrl.substring(3);
		sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	}
	return sWebEditorPath + "/" + sTempUrl;
}
*/

static String convertFileSize (long size){
	int divisor = 1024;
	String unit = "K";
	if (divisor ==1) return size /divisor + " "+unit;
	String aftercomma = ""+100*(size % divisor)/divisor;
	if (aftercomma.length() == 1) aftercomma="0"+aftercomma;
	return size /divisor + "."+aftercomma+" "+unit;
}

static String formatDate(Date myDate) {
	String strFormat = "yyyy-MM-dd HH:mm:ss";
	SimpleDateFormat formatter = new SimpleDateFormat(strFormat);
	String strDate = formatter.format(myDate);
	return strDate;
}

%>
<%
String eWebEditorPath = request.getServletPath();
eWebEditorPath = eWebEditorPath.substring(0, eWebEditorPath.lastIndexOf("/"));
eWebEditorPath = eWebEditorPath.substring(0, eWebEditorPath.lastIndexOf("/"));
eWebEditorPath = application.getRealPath(eWebEditorPath);
String sFileSeparator = File.separator;
if (eWebEditorPath.substring(eWebEditorPath.length()-1,eWebEditorPath.length()) != sFileSeparator){
	eWebEditorPath += sFileSeparator;
}
String sConfig = ReadFile(eWebEditorPath+"jsp"+sFileSeparator+"config.jsp");


ArrayList aStyle = getConfigArray("aStyle", sConfig);

String sAllowExt, sUploadDir, sBaseUrl, sContentPath;
String sCurrDir, sDir;
int nAllowBrowse, nCusDirFlag;

// param
String sType = dealNull(request.getParameter("type")).toUpperCase();
String sStyleName = dealNull(request.getParameter("style"));
String sCusDir = dealNull(request.getParameter("cusdir"));
String sAction = dealNull(request.getParameter("action")).toUpperCase();
// InitUpload
String sRequestURI = request.getRequestURI();
String sServletPath = request.getServletPath();
String sProtocol = request.getProtocol();
String sServerName = request.getServerName();
String sServerPort = String.valueOf(request.getServerPort());

String[] aStyleConfig = new String[43];
boolean bValidStyle = false;

for (int i = 0; i < aStyle.size(); i++){
	aStyleConfig = split(aStyle.get(i).toString(), "|||");
	if (sStyleName.toLowerCase().equals(aStyleConfig[0].toLowerCase())) {
		bValidStyle = true;
		break;
	}
}

if (!bValidStyle) {
	out.print(getOutScript("alert('Invalid Style!')"));
	out.close();
}

sBaseUrl = aStyleConfig[19];
nAllowBrowse = Integer.valueOf(aStyleConfig[43]).intValue();
nCusDirFlag = Integer.valueOf(aStyleConfig[61]).intValue();

if (nAllowBrowse!=1){
	out.print(getOutScript("alert('Do not allow browse!')"));
	out.close();
}

//modify by xinwang.jiao 2007-9-12
/*
if (nCusDirFlag!=1){
	sCusDir = "";
}else{
	sCusDir = replace(sCusDir, "\\", "/");
	if ((sCusDir.startsWith("/")) || (sCusDir.startsWith(".")) || (sCusDir.endsWith(".")) || (sCusDir.indexOf("./")>=0) || (sCusDir.indexOf("/.")>=0) || (sCusDir.indexOf("//")>=0)){
		sCusDir = "";
	}else{
		if (!sCusDir.endsWith("/")){
			sCusDir = sCusDir + "/";
		}
	}
}
*/

sUploadDir = aStyleConfig[3];
//modify by xinwang.jiao 2007-9-12
/*
if (!sUploadDir.substring(0, 1).equals("/")) {
	sUploadDir = "../" + sUploadDir;
}
*/
sUploadDir = sUploadDir + sCusDir;

if (sType.equals("FILE")){
	sAllowExt = "";
} else if (sType.equals("MEDIA")){
	sAllowExt = "rm|mp3|wav|mid|midi|ra|avi|mpg|mpeg|asf|asx|wma|mov";
} else if (sType.equals("FLASH")){
	sAllowExt = "swf";
} else {
	sAllowExt = "bmp|jpg|jpeg|png|gif";
}



String s_Out = "";
String s_RealPath = "";
System.out.println("sAction = " + sAction);
if (sAction.equals("FILE")) {

	String s_ReturnFlag = dealNull(request.getParameter("returnflag"));
	String s_FolderType = dealNull(request.getParameter("foldertype"));
	String s_Dir = dealNull(request.getParameter("dir"));

	String s_CurrDir = "";
	if (s_FolderType.equals("upload")){
		s_CurrDir = sUploadDir;
	} else if (s_FolderType.equals("shareimage")){
		sAllowExt = "";
		s_CurrDir = "../sharefile/image/";
	} else if (s_FolderType.equals("shareflash")){
		sAllowExt = "";
		s_CurrDir = "../sharefile/flash/";
	} else if (s_FolderType.equals("sharemedia")){
		sAllowExt = "";
		s_CurrDir = "../sharefile/media/";
	} else {
		s_FolderType = "shareother";
		sAllowExt = "";
		s_CurrDir = "../sharefile/other/";
	}

	if (!s_Dir.equals("")) {
		if (CheckValidDir(application.getRealPath(RelativePath2RootPath(s_CurrDir+s_Dir, sServletPath)))){
			s_CurrDir = s_CurrDir + s_Dir + "/";
		}else{
			s_Dir = "";
		}
	}
//System.out.println(application.getRealPath(RelativePath2RootPath(s_CurrDir, sServletPath)));
	if (CheckValidDir(application.getRealPath(RelativePath2RootPath(s_CurrDir, sServletPath)))){
		File file = new File(application.getRealPath(RelativePath2RootPath(s_CurrDir, sServletPath)));
		File[] filelist = file.listFiles();
		if (filelist != null && filelist.length > 0){
			int n = -1;
			for (int i = 0; i < filelist.length; i++) {
				if (filelist[i].isFile()) {
					String s_FileName = filelist[i].getName();
					String s_FileExt = s_FileName.substring(s_FileName.lastIndexOf(".")+1);
					s_FileExt = s_FileExt.toLowerCase();
					if (CheckValidExt(sAllowExt, s_FileExt)){
						n++;
						s_Out = s_Out + "arr[" + String.valueOf(n) + "]=new Array(\"" + s_FileName + "\", \"" + String.valueOf(convertFileSize(filelist[i].length())) + "\",\"" + formatDate(new Date(filelist[i].lastModified())) + "\");\n";
					}
				}
			}
		}
	}

	s_Out = "var arr = new Array();\n" + s_Out + "parent.setFileList('" + s_ReturnFlag + "', '" + s_FolderType + "', '" + s_Dir + "', arr);";
	out.print(getOutScript(s_Out));

}else{

	s_Out = "var arrUpload = new Array();\n";
	s_Out = s_Out + "var arrShareImage = new Array();\n";
	s_Out = s_Out + "var arrShareFlash = new Array();\n";
	s_Out = s_Out + "var arrShareMedia = new Array();\n";
	s_Out = s_Out + "var arrShareOther = new Array();\n";
	
	s_RealPath = application.getRealPath(RelativePath2RootPath(sUploadDir, sServletPath)) + sFileSeparator;
	s_Out = s_Out + GetFolderTree(s_RealPath, "Upload", 1, 0, sFileSeparator).get(0).toString();

	sAllowExt = "";
	if (sType.equals("FILE")){
		s_RealPath = application.getRealPath(RelativePath2RootPath("../sharefile/image/", sServletPath)) + sFileSeparator;
		s_Out = s_Out + GetFolderTree(s_RealPath, "ShareImage", 1, 0, sFileSeparator).get(0).toString();
		s_RealPath = application.getRealPath(RelativePath2RootPath("../sharefile/flash/", sServletPath)) + sFileSeparator;
		s_Out = s_Out + GetFolderTree(s_RealPath, "ShareFlash", 1, 0, sFileSeparator).get(0).toString();
		s_RealPath = application.getRealPath(RelativePath2RootPath("../sharefile/media/", sServletPath)) + sFileSeparator;
		s_Out = s_Out + GetFolderTree(s_RealPath, "ShareMedia", 1, 0, sFileSeparator).get(0).toString();
		s_RealPath = application.getRealPath(RelativePath2RootPath("../sharefile/other/", sServletPath)) + sFileSeparator;
		s_Out = s_Out + GetFolderTree(s_RealPath, "ShareOther", 1, 0, sFileSeparator).get(0).toString();
	} else if (sType.equals("MEDIA")){
		s_RealPath = application.getRealPath(RelativePath2RootPath("../sharefile/media/", sServletPath)) + sFileSeparator;
		s_Out = s_Out + GetFolderTree(s_RealPath, "ShareMedia", 1, 0, sFileSeparator).get(0).toString();
	} else if (sType.equals("FLASH")){
		s_RealPath = application.getRealPath(RelativePath2RootPath("../sharefile/flash/", sServletPath)) + sFileSeparator;
		s_Out = s_Out + GetFolderTree(s_RealPath, "ShareFlash", 1, 0, sFileSeparator).get(0).toString();
	} else {
		s_RealPath = application.getRealPath(RelativePath2RootPath("../sharefile/image/", sServletPath)) + sFileSeparator;
		s_Out = s_Out + GetFolderTree(s_RealPath, "ShareImage", 1, 0, sFileSeparator).get(0).toString();
	}

	s_Out = s_Out + "parent.setFolderList(arrUpload, arrShareImage, arrShareFlash, arrShareMedia, arrShareOther);";
	out.print(getOutScript(s_Out));System.out.println(getOutScript(s_Out));
}

%>