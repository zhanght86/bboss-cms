package com.frameworkset.platform.portal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.platform.cms.util.GetFileNamesException;
import com.frameworkset.platform.framework.MenuHelper;

/**
 * <p>
 * 类说明:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @author gao.tang
 * @version V1.0 创建时间：Oct 16, 2009 4:19:21 PM
 */
public class PortalUtil {

	public static void jar(String inputFileName, String outputFileName)
			throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				outputFileName));
		out.setEncoding("GBK"); // ###### 这句话是关键，指定输出的编码方式
		File f = new File(inputFileName);
		jar(out, f, "");
		out.close();
	}

	private static void jar(ZipOutputStream out, File f, String base)
			throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			base = base.length() == 0 ? "" : base + "/"; // 注意，这里用左斜杠
			out.putNextEntry(new ZipEntry(base));
			for (int i = 0; i < fl.length; i++) {
				jar(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			byte[] buffer = new byte[1024];
			int n = in.read(buffer);
			while (n != -1) {
				out.write(buffer, 0, n);
				n = in.read(buffer);
			}
			in.close();
		}
	}

	/**
	 * 根据menupath和区域类型生成模块地址
	 * 
	 * @param contextPath
	 *            应用上面文目录
	 * @param menuPath
	 *            模块菜单地址
	 * @param areaType
	 *            模块区域
	 * @return
	 */
	public static String getRealMenuPath(String contextPath, String menuPath,
			String areaType) {
		String url = null;
		if (areaType.equals("root")) {
			url = MenuHelper.getRootUrl(contextPath, menuPath,
					(java.util.Map) null);

		} else if (areaType.equals("main")) {
			url = MenuHelper.getMainUrl(contextPath, menuPath,
					(java.util.Map) null);
			// url = MenuHelper.getMainUrl(menu);
		} else if (areaType.equals("actionContainer")) {
			url = MenuHelper.getActionContainerUrl(contextPath, menuPath,
					(java.util.Map) null);
		} else if (areaType.equals("navigatorContainer")) {
			url = MenuHelper.getNavigatorContainerUrl(contextPath, menuPath,
					(java.util.Map) null);
		} else if (areaType.equals("status")) {
			url = MenuHelper.getStatusUrl(contextPath, menuPath,
					(java.util.Map) null);
		} else if (areaType.equals("workspace")) {
			url = MenuHelper.getWorkspaceUrl(contextPath, menuPath,
					(java.util.Map) null);
		} else if (areaType.equals("perspectiveContent")) {
			url = MenuHelper.getPerspectiveContentUrl(contextPath, menuPath,
					(java.util.Map) null);
		}
		return url;
	}

	public static String getRealMenuPath(String menuPath, String areaType) {
		return getRealMenuPath(null, menuPath, areaType);
	}

	// 转换异常信息中的 \\n,\\r
	public static String formatErrorMsg(String errorMessage) {
		if (errorMessage != null) {
			errorMessage = errorMessage.replaceAll("\\n", "\\\\n");
			errorMessage = errorMessage.replaceAll("\\r", "\\\\r");
			errorMessage = errorMessage.replaceAll("\"", "\'");
			errorMessage = errorMessage.replace('\\', '/');
		}
		return errorMessage;
	}

	/**
	 * 根据应用部署目录，应用上下目录，和文件名称，获取文件绝对地址
	 * 
	 * @param approot
	 *            应用部署的目录
	 * @param contextPath
	 *            应用上下文目录
	 * @param fileName
	 *            文件名称
	 * @return
	 */
	public static String getFileAbsPath(String approot, String contextPath,
			String fileName) {
		StringBuffer fileAbsPath = new StringBuffer();

		return null;
	}

	/**
	 * 获取以suffix后缀的文件
	 * 
	 * @param pathName
	 * @param suffix
	 * @return
	 * @throws GetFileNamesException
	 */
	public static File[] getFileNames(String pathName, final String suffix)
			throws GetFileNamesException {

		File file = new File(pathName);
		File[] files = file.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				if (suffix.equals("*")
						|| name.toLowerCase().endsWith(suffix.toLowerCase())) {
					return true;
				}

				return false;
			}

		});

		return files;
	}

	public static String charsetTrans(String name) {
		try {
			byte[] by = name.getBytes("unicode");
			for (int i = 0; i < by.length; i++) {
				System.out.println("by[" + i + "] = " + by[i]);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getBytes(String s) {
		try {
			StringBuffer out = new StringBuffer("");
			byte[] bytes = s.getBytes("unicode");
			for (int i = 2; i < bytes.length - 1; i += 2) {
				out.append("\\u");
				String str = Integer.toHexString(bytes[i + 1] & 0xff);
				for (int j = str.length(); j < 2; j++) {
					out.append("0");
				}
				String str1 = Integer.toHexString(bytes[i] & 0xff);
				if(str1.length() == 1){
					str1 = "0" + str1;
				}
				out.append(str);
				out.append(str1);
			}
			return out.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
//		formatErrorMsg("java.io.FileNotFoundException: v:\\ca\\war13\\dad.war (系统找不到指定的路径。)");
		try {

			long start = System.currentTimeMillis();
			FileUtil.fileCopy("E:\\tool\\xmgr20.exe","D:\\ca\\xmgr202.exe");
//			File file = new File("E:\\RoxTV_Setup.exe");
//            boolean state = file.renameTo(new File("D:\\ca\\RoxTV_Setup.exe"));
            long end = System.currentTimeMillis();
            System.out.println(end - start + "|" + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
