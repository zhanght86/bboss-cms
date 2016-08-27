<%@page import="com.frameworkset.util.FileUtil"%>
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

static String GetRndFileName(String sFileExt, String sPath){
	int i = (int)(Math.random()*900) + 100;
	String sFileName = formatDate(new Date(), 4) + String.valueOf(i) + "." + sFileExt;

	java.io.File f = new java.io.File(sPath+sFileName);
	if (f.exists()){
		return GetRndFileName(sFileExt, sPath);
	}

	return sFileName;
}

static String getOutScript(String str){
	return ("<script language=javascript>" + str + ";history.back()</script>");
}
static String OutScriptNoBack(String str){
	return ("<script language=javascript>" + str + "</script>");
}

static boolean CheckValidExt(String s_AllowExt, String sExt){
	String[] aExt = split(s_AllowExt, "|");
	for (int i = 0; i<aExt.length; i++){
		if (aExt[i].toLowerCase().equals(sExt)) {
			return true;
		}
	}
	return false;
}

static String getNewDir(int n_AutoDir){
	switch(n_AutoDir){
	case 1:
		return formatDate(new Date(), 1) + "/";
	case 2:
		return formatDate(new Date(), 2) + "/";
	case 3:
		return formatDate(new Date(), 3) + "/";
	default:
		return "";
	}
}

static String formatDate(Date myDate, int nFlag) {
	String strFormat = "";
	switch(nFlag){
	case 1:
		strFormat = "yyyy";
		break;
	case 2:
		strFormat = "yyyyMM";
		break;
	case 3:
		strFormat = "yyyyMMdd";
		break;
	case 4:
		strFormat = "yyyyMMddHHmmss";
		break;
	}
	SimpleDateFormat formatter = new SimpleDateFormat(strFormat);
	String strDate = formatter.format(myDate);
	return strDate;
}

static void Mkdir(String path){
	java.io.File dir = new java.io.File(path);
	if (dir == null){
		return;
	}
	if (dir.isFile()){
		return;
	}
	if (!dir.exists()){
		boolean result = dir.mkdirs();
	}
}
/**
  * url "siteResource/"
  * s_RequestURI "/creatorcms/cms/editor/eWebEditor48/jsp/upload.jsp"
 */
static String RelativePath2RootPath(String url, String s_RequestURI){
	String sTempUrl = url;
	if (sTempUrl.substring(0, 1).equals("/")){
		return sTempUrl;
	}

	String sWebEditorPath = s_RequestURI;
	sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	//add by xinwang.jiao
	sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	//sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));

	while(sTempUrl.startsWith("../")){
		sTempUrl = sTempUrl.substring(3);
		sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	}
	return sWebEditorPath + "/" + sTempUrl;
}

/**
  * url "siteResource/"
  * s_ServletPath "/cms/editor/eWebEditor48/jsp/upload.jsp"
 */
static String RelativePath2RootPath2(String url, String s_ServletPath){
	String sTempUrl = url;
	if (sTempUrl.substring(0, 1).equals("/")){
		return sTempUrl;
	}

	String sWebEditorPath = s_ServletPath;
	sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	//add by xinwang.jiao
	sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	//sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));

	while(sTempUrl.startsWith("../")){
		sTempUrl = sTempUrl.substring(3);
		sWebEditorPath = sWebEditorPath.substring(0, sWebEditorPath.lastIndexOf("/"));
	}
	return sWebEditorPath + "/" + sTempUrl;
}

static String RootPath2DomainPath(String url, String s_Protocol, String s_ServerName, String s_ServerPort){
	String sHost = split(s_Protocol, "/")[0] + "://" + s_ServerName;
	String sPort = s_ServerPort;
	if (!sPort.equals("80")){
		sHost = sHost + ":" + sPort;
	}
	return sHost + url;
}

static String inHTML(int i){
	if (i=='&') return "&amp;";
	else if (i=='<') return "&lt;";
	else if (i=='>') return "&gt;";
	else if (i=='"') return "&quot;";
	else return ""+(char)i;
}
	
static String inHTML(String st){
	StringBuffer buf = new StringBuffer();
	for (int i = 0;i<st.length();i++){
		buf.append(inHTML(st.charAt(i)));
	}
	return buf.toString();
}

static boolean SaveRemoteFile(String s_LocalFileName, String s_RemoteFileUrl, String s_RealUploadPath){
	try{ 
		int httpStatusCode;
		URL url = new URL(s_RemoteFileUrl);
		URLConnection conn = url.openConnection();
		conn.connect();
		HttpURLConnection httpconn =(HttpURLConnection)conn;
		httpStatusCode =httpconn.getResponseCode();
		if(httpStatusCode!=HttpURLConnection.HTTP_OK){
			//file://HttpURLConnection return an error code
			//System.out.println("Connect to "+s_RemoteFileUrl+" failed,return code:"+httpStatusCode);
			return false;
		}
		int filelen = conn.getContentLength();
		InputStream is = conn.getInputStream();
		byte[] tmpbuf=new byte[1024];
		File savefile =new File(s_RealUploadPath + s_LocalFileName);
		if(!savefile.exists())
			savefile.createNewFile();
		FileOutputStream fos = new FileOutputStream(savefile);
		int readnum = 0;
		if(filelen<0){
			while(readnum>-1){
				readnum = is.read(tmpbuf);
				if(readnum>0)
					fos.write(tmpbuf,0,readnum);
			}
		}else{
			int readcount =0;
			while(readcount<filelen&&readnum!=-1){
				readnum=is.read(tmpbuf);
				if(readnum>0){
					fos.write(tmpbuf,0,readnum);
					readcount =readcount +readnum;
				}
			}
			if(readcount<filelen){
				System.out.println("download error");
				is.close();
				fos.close();
				savefile.delete();
				return false;
			}
		}
		fos.flush();
		fos.close();
		is.close();
	}
	catch(Exception e){
		e.printStackTrace();
		return false;
	}
	return true;
}

static String getUploadForm(String s_Type, String s_StyleName, String s_AllowExt, String s_CusDir){
	String html = "";
	html += "\n" + "<HTML>";
	html += "\n" + "<HEAD>";
	html += "\n" + "<TITLE>eWebEditor</TITLE>";
	html += "\n" + "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>";
	html += "\n" + "<script language='javascript' src='../dialog/dialog.js'></script>";
	html += "\n" + "<link href='../dialog/dialog.css' type='text/css' rel='stylesheet'>";
	html += "\n" + "</head>";
	html += "\n" + "<body class=upload>";

	html += "\n" + "<form action='?action=save&type=" + s_Type + "&style=" + s_StyleName + "&cusdir=" + s_CusDir + "' method=post name=myform enctype='multipart/form-data'>";
	html += "\n" + "<input type=file name=uploadfile size=1 style='width:100%' onchange='try{parent.doPreview();} catch(e){}'>";
	html += "\n" + "</form>";

	html += "\n" + "<script language=javascript>";

	html += "\n" + "var sAllowExt = '" + s_AllowExt + "';";

	html += "\n" + "function CheckUploadForm() {";
	html += "\n" + "	if (!IsExt(document.myform.uploadfile.value,sAllowExt)){";
	html += "\n" + "		parent.UploadError('提示：\\n\\n请选择一个有效的文件，\\n支持的格式有:'+sAllowExt);";
	html += "\n" + "		return false;";
	html += "\n" + "	}";
	html += "\n" + "	return true";
	html += "\n" + "}";

	html += "\n" + "var oForm = document.myform ;";
	html += "\n" + "oForm.attachEvent('onsubmit', CheckUploadForm) ;";
	html += "\n" + "if (! oForm.submitUpload) oForm.submitUpload = new Array() ;";
	html += "\n" + "oForm.submitUpload[oForm.submitUpload.length] = CheckUploadForm ;";
	html += "\n" + "if (! oForm.originalSubmit) {";
	html += "\n" + "	oForm.originalSubmit = oForm.submit ;";
	html += "\n" + "	oForm.submit = function() {";
	html += "\n" + "		if (this.submitUpload) {";
	html += "\n" + "			for (var i = 0 ; i < this.submitUpload.length ; i++) {";
	html += "\n" + "				this.submitUpload[i]() ;";
	html += "\n" + "			}";
	html += "\n" + "		}";
	html += "\n" + "		this.originalSubmit() ;";
	html += "\n" + "	}";
	html += "\n" + "}";

	html += "\n" + "try {";
	html += "\n" + "	parent.UploadLoaded();";
	html += "\n" + "}";
	html += "\n" + "catch(e){";
	html += "\n" + "}";

	html += "\n" + "</script>";

	html += "\n" + "</body>";
	html += "\n" + "</html>";

	return html;
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

static ArrayList getConfigArray(String s_Key, String s_Config){
	ArrayList a_Result = new ArrayList();
	Pattern p = Pattern.compile(s_Key + " = \"(.*)\";");
	Matcher m = p.matcher(s_Config);
	while (m.find()) {
		a_Result.add(m.group(1));
	}
	return a_Result;
}

static String getSmallImageFile(String s_File){
	return s_File.substring(0, s_File.lastIndexOf(".")) + "_s" + s_File.substring(s_File.lastIndexOf("."));
}

static boolean isValidSLTSYExt(String s_File, String s_AllowExt){
	String sExt = s_File.substring(s_File.lastIndexOf(".")+1).toLowerCase();
	String[] aExt = split(s_AllowExt.toLowerCase(), "|");
	for(int i=0;i<aExt.length;i++){
		if (aExt[i].equals(sExt)){
			return true;
		}
	}
	return false;
}


static boolean makeImageSY(String s_PathFile, String s_AllowExt, int n_SYWZFlag, String s_SYFontName, int n_SYFontSize, String s_SYShadowColor, int n_SYShadowOffset, String s_SYFontColor, String s_SYText, String s_SYPicPath, int n_SYWZMinWidth, int n_SYWZMinHeight, int n_SYWZPosition, int n_SYWZTextWidth, int n_SYWZTextHeight, int n_SYWZPaddingH, int n_SYWZPaddingV, int n_SYTPFlag, int n_SYTPMinWidth, int n_SYTPMinHeight, int n_SYTPPosition, int n_SYTPPaddingH, int n_SYTPPaddingV, int n_SYTPImageWidth, int n_SYTPImageHeight, float n_SYTPOpacity){
	if((n_SYWZFlag==0)&&(n_SYTPFlag==0)){ return false; }
	if(!isValidSLTSYExt(s_PathFile, s_AllowExt)){ return false; }

		ImageIcon imgIcon = new ImageIcon(s_PathFile);
		Image theImg = imgIcon.getImage(); 
		int width = theImg.getWidth(null);
		int height = theImg.getHeight(null);
		int posX, posY;
		
		BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bimage.createGraphics();
		g.drawImage(theImg, 0, 0, null);
		if(n_SYWZFlag==1){
			if((width<n_SYWZMinWidth)||(height<n_SYWZMinHeight)){return false;}
			posX = getSYPosX(n_SYWZPosition, width, n_SYWZTextWidth+n_SYShadowOffset, n_SYWZPaddingH);
			posY = getSYPosY(n_SYWZPosition, height, n_SYWZTextHeight+n_SYShadowOffset, n_SYWZPaddingV);

			Font wordFont = new Font(s_SYFontName, Font.PLAIN, n_SYFontSize);
			g.setFont(wordFont);
			FontMetrics fm = g.getFontMetrics();
			int a = fm.getAscent();

			//g.setBackground(Color.white);
			g.setColor(new Color(Integer.parseInt(s_SYShadowColor ,16)));
			g.drawString(s_SYText, posX+n_SYShadowOffset, posY+a+n_SYShadowOffset);
			g.setColor(new Color(Integer.parseInt(s_SYFontColor ,16)));
			g.drawString(s_SYText, posX, posY+a);
		}
		if(n_SYTPFlag==1){
			if((width<n_SYTPMinWidth)||(height<n_SYTPMinHeight)){return false;}
			posX = getSYPosX(n_SYTPPosition, width, n_SYTPImageWidth, n_SYTPPaddingH);
			posY = getSYPosY(n_SYTPPosition, height, n_SYTPImageHeight, n_SYTPPaddingV);

			ImageIcon waterIcon = new ImageIcon(s_SYPicPath);
			Image waterImg = waterIcon.getImage();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, n_SYTPOpacity));
			g.drawImage(waterImg, posX, posY, null );
			//g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		}
		g.dispose(); 
		try{ 
			File fo = new File(s_PathFile);
			ImageIO.write(bimage, "jpeg", fo);
		}catch(Exception e){ 
			return false; 
		}

	return true;
}

static int getSYPosX(int posFlag, int originalW, int syW, int paddingH){
	switch(posFlag){
	case 1:
	case 2:
	case 3:
		return paddingH;
	case 4:
	case 5:
	case 6:
		return (int)Math.floor((originalW - syW) / 2);
	case 7:
	case 8:
	case 9:
		return (originalW - paddingH - syW);
	default:
		return 0;
	}
}

static int getSYPosY(int posFlag, int originalH, int syH, int paddingV){
	switch(posFlag){
	case 1:
	case 4:
	case 7:
		return paddingV;
	case 2:
	case 5:
	case 8:
		return (int)Math.floor((originalH - syH) / 2);
	case 3:
	case 6:
	case 9:
		return (originalH - paddingV - syH);
	default:
		return 0;
	}
}

static boolean makeImageSLT(String s_Path, String s_File, String s_SmallFile, int n_SLTMinSize, int n_SLTOkSize, int n_SLTFlag, String s_AllowExt){
	if(n_SLTFlag==0){ return false; }
	if(!isValidSLTSYExt(s_Path+s_File, s_AllowExt)){ return false; }

	try {
		File fi = new File(s_Path + s_File);
		BufferedImage bis = ImageIO.read(fi);
		int w = bis.getWidth();
		int h = bis.getHeight();
		if((w<=n_SLTMinSize)&&(h<=n_SLTMinSize)){
			return false;
		}
		
		int nw,nh;
		double rate;
		if(w>h){
			nw = n_SLTOkSize;
			rate = (double)nw/(double)w;
			nh = (int)(rate*(double)h);
		}else{
			nh = n_SLTOkSize;
			rate = (double)nh/(double)h;
			nw = (int)(rate*(double)w);

		}

		File fo = new File(s_Path + s_SmallFile);
		BufferedImage bid = resizeImage(bis, nw, nh);
		ImageIO.write(bid,"jpeg",fo);
	} catch(Exception e){
		System.out.println(e);
		return false;
	}
	return true;
}

static BufferedImage resizeImage(BufferedImage source, int targetW, int targetH) {
    int type = source.getType();
    BufferedImage target = null;
    if (type == BufferedImage.TYPE_CUSTOM) {
        ColorModel cm = source.getColorModel();
        WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
        boolean alphaPremultiplied = cm.isAlphaPremultiplied();
        target = new BufferedImage(cm, raster, alphaPremultiplied, null);
    } else
        target = new BufferedImage(targetW, targetH, type);
    Graphics2D g = target.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    double sx = (double) targetW / source.getWidth();
    double sy = (double) targetH / source.getHeight();
    g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
    g.dispose();
    return target;
}



%>
<%
String eWebEditorPath = request.getServletPath();// "/cms/editor/eWebEditor48/jsp/upload.jsp"
eWebEditorPath = eWebEditorPath.substring(0, eWebEditorPath.lastIndexOf("/"));
eWebEditorPath = eWebEditorPath.substring(0, eWebEditorPath.lastIndexOf("/"));
// "D:\workspace\cms\creatorcms\cms\editor\eWebEditor48"
eWebEditorPath = application.getRealPath(eWebEditorPath);
String sFileSeparator = File.separator;
if (eWebEditorPath.substring(eWebEditorPath.length()-1,eWebEditorPath.length()) != sFileSeparator){
	eWebEditorPath += sFileSeparator;// "D:\workspace\cms\creatorcms\cms\editor\eWebEditor48\"
}
String sConfig = ReadFile(eWebEditorPath+"jsp"+sFileSeparator+"config.jsp");

ArrayList aStyle = getConfigArray("aStyle", sConfig);

String sAllowExt, sUploadDir, sBaseUrl, sContentPath, sNewDir;
int nAllowSize, nUploadObject, nAutoDir;

String sSYText, sSYFontColor, sSYFontName, sSYPicPath, sSLTSYExt, sSYShadowColor;
int nSLTFlag, nSLTMinSize, nSLTOkSize, nSYWZFlag, nSYFontSize, nSLTSYObject, nSYWZMinWidth, nSYShadowOffset, nSYWZMinHeight, nSYWZPosition, nSYWZTextWidth, nSYWZTextHeight, nSYWZPaddingH, nSYWZPaddingV, nSYTPFlag, nSYTPMinWidth, nSYTPMinHeight, nSYTPPosition, nSYTPPaddingH, nSYTPPaddingV, nSYTPImageWidth, nSYTPImageHeight, nCusDirFlag;
float nSYTPOpacity;

// param
String sType = dealNull(request.getParameter("type")).toUpperCase();// "IMAGE"
String sStyleName = dealNull(request.getParameter("style"));// "coolblue"
String sCusDir = dealNull(request.getParameter("cusdir"));// "site254/_webprj/chlxwj/content_files"
// interface
String sOriginalFileName = "";
String sSaveFileName = "";
String sPathFileName = "";
// InitUpload
String sRequestURI = request.getRequestURI();// "/creatorcms/cms/editor/eWebEditor48/jsp/upload.jsp"
String sServletPath = request.getServletPath();// "/cms/editor/eWebEditor48/jsp/upload.jsp"
String sProtocol = request.getProtocol();// HTTP/1.1
String sServerName = request.getServerName();// localhost
String sServerPort = String.valueOf(request.getServerPort());// 8081

sCusDir = replace(sCusDir, "\\", "/");
if ((sCusDir.startsWith("/")) || (sCusDir.startsWith(".")) || (sCusDir.endsWith(".")) || (sCusDir.indexOf("./")>=0) || (sCusDir.indexOf("/.")>=0) || (sCusDir.indexOf("//")>=0)){
	sCusDir = "";
}


String[] aStyleConfig = new String[27];
boolean bValidStyle = false;

for (int i = 0; i < aStyle.size(); i++){
	aStyleConfig = split(aStyle.get(i).toString(), "|||");
	if (sStyleName.toLowerCase().equals(aStyleConfig[0].toLowerCase())) {
		bValidStyle = true;
		break;
	}
}

if (!bValidStyle) {
	out.print(getOutScript("parent.UploadError('调用错误：请传入有效的样式名！')"));
	return;
}

sBaseUrl = aStyleConfig[19];
nUploadObject = Integer.valueOf(aStyleConfig[20]).intValue();
nAutoDir = Integer.valueOf(aStyleConfig[21]).intValue();

sUploadDir = aStyleConfig[3];// "cms/siteResource/"

//modify by bboss xinwang.jiao 2007-9-10
/*
if (!sUploadDir.substring(0, 1).equals("/")) {
	sUploadDir = "../" + sUploadDir;
}
*/

if (sBaseUrl.equals("1")){
	sContentPath = RelativePath2RootPath(sUploadDir, sRequestURI);// /creatorcms/cms/siteResource/
} else if (sBaseUrl.equals("2")){
	sContentPath = RootPath2DomainPath(RelativePath2RootPath(sUploadDir, sRequestURI), sProtocol, sServerName, sServerPort);// HTTP://localhost:8081/creatorcms/cms/siteResource/
} else {
	sContentPath = aStyleConfig[23];
}

if (sType.equals("REMOTE")){
	sAllowExt = aStyleConfig[10];
	nAllowSize = Integer.valueOf(aStyleConfig[15]).intValue();
} else if (sType.equals("FILE")){
	sAllowExt = aStyleConfig[6];
	nAllowSize = Integer.valueOf(aStyleConfig[11]).intValue();
} else if (sType.equals("MEDIA")){
	sAllowExt = aStyleConfig[9];
	nAllowSize = Integer.valueOf(aStyleConfig[14]).intValue();
} else if (sType.equals("FLASH")){
	sAllowExt = aStyleConfig[7];
	nAllowSize = Integer.valueOf(aStyleConfig[12]).intValue();
} else {
	sAllowExt = aStyleConfig[8];
	nAllowSize = Integer.valueOf(aStyleConfig[13]).intValue();
}
//System.out.println("sType = " + sType);
//System.out.println("sAllowExt = " + sAllowExt);
nSLTFlag = Integer.valueOf(aStyleConfig[29]).intValue();
nSLTMinSize = Integer.valueOf(aStyleConfig[30]).intValue();
nSLTOkSize = Integer.valueOf(aStyleConfig[31]).intValue();
nSYWZFlag = Integer.valueOf(aStyleConfig[32]).intValue();
sSYText = aStyleConfig[33];
sSYFontColor = aStyleConfig[34];
nSYFontSize = Integer.valueOf(aStyleConfig[35]).intValue();
sSYFontName = aStyleConfig[36];
sSYPicPath = aStyleConfig[37];
nSLTSYObject = Integer.valueOf(aStyleConfig[38]).intValue();
sSLTSYExt = aStyleConfig[39];
nSYWZMinWidth = Integer.valueOf(aStyleConfig[40]).intValue();
sSYShadowColor = aStyleConfig[41];
nSYShadowOffset = Integer.valueOf(aStyleConfig[42]).intValue();
nSYWZMinHeight = Integer.valueOf(aStyleConfig[46]).intValue();
nSYWZPosition = Integer.valueOf(aStyleConfig[47]).intValue();
nSYWZTextWidth = Integer.valueOf(aStyleConfig[48]).intValue();
nSYWZTextHeight = Integer.valueOf(aStyleConfig[49]).intValue();
nSYWZPaddingH = Integer.valueOf(aStyleConfig[50]).intValue();
nSYWZPaddingV = Integer.valueOf(aStyleConfig[51]).intValue();
nSYTPFlag = Integer.valueOf(aStyleConfig[52]).intValue();
nSYTPMinWidth = Integer.valueOf(aStyleConfig[53]).intValue();
nSYTPMinHeight = Integer.valueOf(aStyleConfig[54]).intValue();
nSYTPPosition = Integer.valueOf(aStyleConfig[55]).intValue();
nSYTPPaddingH = Integer.valueOf(aStyleConfig[56]).intValue();
nSYTPPaddingV = Integer.valueOf(aStyleConfig[57]).intValue();
nSYTPImageWidth = Integer.valueOf(aStyleConfig[58]).intValue();
nSYTPImageHeight = Integer.valueOf(aStyleConfig[59]).intValue();
nSYTPOpacity = Float.valueOf(aStyleConfig[60]).floatValue();
nCusDirFlag = Integer.valueOf(aStyleConfig[61]).intValue();

String sRealUploadPath = application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)) + sFileSeparator;// D:\workspace\cms\creatorcms\cms\siteResource\
sSYPicPath = application.getRealPath(sSYPicPath);

// do action
String sAction = dealNull(request.getParameter("action")).toUpperCase();
//System.out.println("sAction = " + sAction);
if (sAction.equals("REMOTE")){
	sUploadDir = sUploadDir + sCusDir + "/";
	sContentPath = sContentPath + sCusDir + "/";
	sRealUploadPath = application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)) + sFileSeparator;

	// create dir
	if ((nCusDirFlag==1)&&(!sCusDir.equals(""))){
		String[] aTmp = split(sCusDir, "/");
		for (int i = 0; i<aTmp.length; i++){
			if (!aTmp[i].equals("")){
				sUploadDir = sUploadDir + aTmp[i] + "/";
				sRealUploadPath = application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)) + sFileSeparator;
				sContentPath = sContentPath + aTmp[i] + "/";
				Mkdir(application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)));
			}
		}
	}

	sNewDir = getNewDir(nAutoDir);
	if (!sNewDir.equals("")){
		sUploadDir = sUploadDir + sNewDir;
		sRealUploadPath = application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)) + sFileSeparator;
		sContentPath = sContentPath + sNewDir;
		Mkdir(application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)));
	}

	String sRemoteContent = dealNull(request.getParameter("eWebEditor_UploadText"));
	if (!sAllowExt.equals("") && !sRemoteContent.equals("")){
		Pattern p = Pattern.compile("((http|https|ftp|rtsp|mms):(\\/\\/|\\\\\\\\){1}(([A-Za-z0-9_-])+[.]){1,}([A-Za-z0-9]{1,5})\\/(\\S+\\.(" + sAllowExt + ")))");
		Matcher m = p.matcher(sRemoteContent);
		ArrayList a_RemoteUrl = new ArrayList();
		String sRemoteurl = "";
		boolean bFind = false;
		while (m.find()) {
			sRemoteurl = sRemoteContent.substring(m.start(), m.end());
			bFind = false;
			for(int i=0; i<a_RemoteUrl.size(); i++){
				if (sRemoteurl.equals(a_RemoteUrl.get(i).toString())){
					bFind = true;
				}
			}
			if (bFind==false){
				a_RemoteUrl.add(sRemoteurl);
			}
		}

		String SaveFileType = "";
		String SaveFileName = "";
		for(int i=0; i<a_RemoteUrl.size(); i++){
			sRemoteurl = a_RemoteUrl.get(i).toString();
			SaveFileType = sRemoteurl.substring(sRemoteurl.lastIndexOf(".")+1);
			SaveFileName = GetRndFileName(SaveFileType, sRealUploadPath);
			//sRealUploadPath = config.getServletContext().getRealPath("/") + "/" + sUploadDir + "/" + sCusDir + "/";
			if (SaveRemoteFile(SaveFileName, sRemoteurl, sRealUploadPath)){
				if (!sOriginalFileName.equals("")){
					sOriginalFileName = sOriginalFileName + "|";
					sSaveFileName = sSaveFileName + "|";
					sPathFileName = sPathFileName + "|";
				}
				sOriginalFileName = sOriginalFileName + sRemoteurl.substring(sRemoteurl.lastIndexOf("/")+1);
				sSaveFileName = sSaveFileName + SaveFileName;
				sPathFileName = sPathFileName + sContentPath + SaveFileName;
				//sPathFileName = sPathFileName + sRequestURI.replaceAll("cms/editor/eWebEditor48/jsp/upload.jsp",sUploadDir + sCusDir + "/" + sSaveFileName);
				sRemoteContent = replace(sRemoteContent, sRemoteurl, sContentPath + "/" + SaveFileName);
				//sRemoteContent = replace(sRemoteContent, sRemoteurl, sRequestURI.replaceAll("cms/editor/eWebEditor48/jsp/upload.jsp",sUploadDir + sCusDir + "/" + sSaveFileName));
			}
		}
	}

	out.print("<HTML><HEAD><TITLE>eWebEditor</TITLE><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head><body><input type=hidden id=UploadText value=\"" + inHTML(sRemoteContent) + "\"></body></html>" + OutScriptNoBack("parent.setHTML(UploadText.value);try{parent.addUploadFile('" + sOriginalFileName + "', '" + sSaveFileName + "', '" + sPathFileName + "');} catch(e){} parent.remoteUploadOK();") );

} else if (sAction.equals("SAVE")){
	// show form
	out.print(getUploadForm(sType, sStyleName, sAllowExt, sCusDir));
	
	sUploadDir = sUploadDir + sCusDir + "/";
	sContentPath = sContentPath + sCusDir + "/";
	sRealUploadPath = application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)) + sFileSeparator;

	// create dir
	if ((nCusDirFlag==1)&&(!sCusDir.equals(""))){
		String[] aTmp = split(sCusDir, "/");
		for (int i = 0; i<aTmp.length; i++){
			if (!aTmp[i].equals("")){
				sUploadDir = sUploadDir + aTmp[i] + "/";
				sRealUploadPath = application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)) + sFileSeparator;
				
				sContentPath = sContentPath + aTmp[i] + "/";
				Mkdir(application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)));
			}
		}
	}

	sNewDir = getNewDir(nAutoDir);
	if (!sNewDir.equals("")){
		sUploadDir = sUploadDir + sNewDir;
		sRealUploadPath = application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)) + sFileSeparator;
		sContentPath = sContentPath + sNewDir;
		Mkdir(application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)));
	}

	// jspsmartupload begin
	mySmartUpload.initialize(pageContext);
	mySmartUpload.setMaxFileSize(nAllowSize*1024*1024);
	String sAllowedFilesList = replace(sAllowExt, "|", ",");
	//mySmartUpload.setAllowedFilesList(sAllowedFilesList);
	try {
		mySmartUpload.upload();
		com.jspsmart.upload.File oFile = mySmartUpload.getFiles().getFile(0);
		String sFileExt = oFile.getFileExt().toLowerCase();
		if (!CheckValidExt(sAllowExt, sFileExt)){
			out.print(getOutScript("parent.UploadError('提示：\\n\\n请选择一个有效的文件，\\n支持的格式有:" + sAllowExt + "')"));
			return;
		}
		sOriginalFileName = oFile.getFileName();
		sSaveFileName = GetRndFileName(sFileExt, sRealUploadPath);
		//oFile.saveAs(RelativePath2RootPath2(sUploadDir, sServletPath) + sSaveFileName,oFile.SAVEAS_VIRTUAL);
		oFile.saveAs(sRealUploadPath + sSaveFileName);
	}
	catch(Exception e){
		out.print(OutScriptNoBack("parent.UploadError('请选择有效的上传文件！\\n您上传的文件总大小超出了最大限制:" + String.valueOf(nAllowSize) + "KB')"));
		e.printStackTrace();
		return;
	}
	// jspsmartupload end


	String s_SmallImageFile, s_SmallImagePathFile, s_SmallImageScript;
	boolean b_SY;
	s_SmallImageFile = getSmallImageFile(sSaveFileName);
	s_SmallImagePathFile = "";
	s_SmallImageScript = "";
	if(makeImageSLT(sRealUploadPath, sSaveFileName, s_SmallImageFile, nSLTMinSize, nSLTOkSize, nSLTFlag, sSLTSYExt)){
		b_SY = makeImageSY(sRealUploadPath + s_SmallImageFile, sSLTSYExt, nSYWZFlag, sSYFontName, nSYFontSize, sSYShadowColor, nSYShadowOffset, sSYFontColor, sSYText, sSYPicPath, nSYWZMinWidth, nSYWZMinHeight, nSYWZPosition, nSYWZTextWidth, nSYWZTextHeight, nSYWZPaddingH, nSYWZPaddingV, nSYTPFlag, nSYTPMinWidth, nSYTPMinHeight, nSYTPPosition, nSYTPPaddingH, nSYTPPaddingV, nSYTPImageWidth, nSYTPImageHeight, nSYTPOpacity);
		b_SY = makeImageSY(sRealUploadPath + sSaveFileName, sSLTSYExt, nSYWZFlag, sSYFontName, nSYFontSize, sSYShadowColor, nSYShadowOffset, sSYFontColor, sSYText, sSYPicPath, nSYWZMinWidth, nSYWZMinHeight, nSYWZPosition, nSYWZTextWidth, nSYWZTextHeight, nSYWZPaddingH, nSYWZPaddingV, nSYTPFlag, nSYTPMinWidth, nSYTPMinHeight, nSYTPPosition, nSYTPPaddingH, nSYTPPaddingV, nSYTPImageWidth, nSYTPImageHeight, nSYTPOpacity);
		s_SmallImagePathFile = sContentPath + s_SmallImageFile;
		s_SmallImageScript = "try{obj.addUploadFile('" + sOriginalFileName + "', '" + s_SmallImageFile + "', '" + s_SmallImagePathFile + "');} catch(e){} ";
	}else{
		s_SmallImageFile = "";
		b_SY = makeImageSY(sRealUploadPath + sSaveFileName, sSLTSYExt, nSYWZFlag, sSYFontName, nSYFontSize, sSYShadowColor, nSYShadowOffset, sSYFontColor, sSYText, sSYPicPath, nSYWZMinWidth, nSYWZMinHeight, nSYWZPosition, nSYWZTextWidth, nSYWZTextHeight, nSYWZPaddingH, nSYWZPaddingV, nSYTPFlag, nSYTPMinWidth, nSYTPMinHeight, nSYTPPosition, nSYTPPaddingH, nSYTPPaddingV, nSYTPImageWidth, nSYTPImageHeight, nSYTPOpacity);
	}

	sPathFileName = sContentPath + sSaveFileName;
	out.print( getOutScript("parent.UploadSaved('" + sPathFileName + "','" + s_SmallImagePathFile + "');var obj=parent.dialogArguments;if((!obj.eWebEditor)||(!obj.eWebEditor_Temp_HTML)||(!obj.eWebEditor_UploadForm)){obj=parent.dialogArguments.dialogArguments;} try{obj.addUploadFile('" + sOriginalFileName + "', '" + sSaveFileName + "', '" + sPathFileName + "');} catch(e){} " + s_SmallImageScript) );

} else if (sAction.equals("LOCAL")){
	sUploadDir = sUploadDir + sCusDir + "/";
	sContentPath = sContentPath + sCusDir + "/";
	sRealUploadPath = application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)) + sFileSeparator;

	// create dir
	if ((nCusDirFlag==1)&&(!sCusDir.equals(""))){
		String[] aTmp = split(sCusDir, "/");
		for (int i = 0; i<aTmp.length; i++){
			if (!aTmp[i].equals("")){
				sUploadDir = sUploadDir + aTmp[i] + "/";
				sRealUploadPath = application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)) + sFileSeparator;
				sContentPath = sContentPath + aTmp[i] + "/";
				Mkdir(application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)));
			}
		}
	}

	sNewDir = getNewDir(nAutoDir);
	if (!sNewDir.equals("")){
		sUploadDir = sUploadDir + sNewDir;
		sRealUploadPath = application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)) + sFileSeparator;
		sContentPath = sContentPath + sNewDir;
		Mkdir(application.getRealPath(RelativePath2RootPath2(sUploadDir, sServletPath)));
	}

	// jspsmartupload begin
	mySmartUpload.initialize(pageContext);
	mySmartUpload.setMaxFileSize(nAllowSize*1024);
	String sAllowedFilesList = replace(sAllowExt, "|", ",");
	mySmartUpload.setAllowedFilesList(sAllowedFilesList);
	try {
		mySmartUpload.upload();
		com.jspsmart.upload.File oFile = mySmartUpload.getFiles().getFile(0);
		String sFileExt = oFile.getFileExt().toLowerCase();
		if (!CheckValidExt(sAllowExt, sFileExt)){
			return;
		}
		sOriginalFileName = oFile.getFileName();
		//System.out.println(oFile.getFilePathName() + "|" + oFile.getFileName());
		sSaveFileName = GetRndFileName(sFileExt, sRealUploadPath);
		//oFile.saveAs(RelativePath2RootPath2(sUploadDir, sServletPath) + sSaveFileName,oFile.SAVEAS_VIRTUAL);
		oFile.saveAs(sRealUploadPath + sSaveFileName);
	}
	catch(Exception e){
		e.printStackTrace();
		return;
	}
	// jspsmartupload end

	sPathFileName = sContentPath + sSaveFileName;
	//sPathFileName = sRequestURI.replaceAll("cms/editor/eWebEditor48/jsp/upload.jsp",sUploadDir + sCusDir + "/" + sSaveFileName);
	out.print(sPathFileName);

} else {
	out.print(getUploadForm(sType, sStyleName, sAllowExt, sCusDir));
}

%>