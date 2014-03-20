package com.sany.mbp.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class StringUtil 
{
 /**
  * 将字符串content中的字符paramString2全都替换为paramString3
  * @param content 需要替换的字符串
  * @param paramString2 需要替换的字符
  * @param paramString3 替换后的字符
  * @return 替换后的字符串
  */
  public static final String replaceString(String content, String paramString2, String paramString3)
  {
    if ((content == null) || (content.length() == 0))
      return content;
    StringBuffer localStringBuffer = new StringBuffer();
    while ((content.indexOf(paramString2)) >= 0)
    {
      int i;
      i= content.indexOf(paramString2);
      localStringBuffer.append(content.substring(0, i));
      localStringBuffer.append(paramString3);
      content = content.substring(i + paramString2.length());
    }
    if (content.length() > 0)
      localStringBuffer.append(content);
    return localStringBuffer.toString();
  }



  /**
   * 将字符串content中的字符为"\r\n"或"\n"的全都替换为"<br>"
   * @param content 需要替换的字符串
   * @return 替换后的字符串
   */
  public static final String replaceEnter(String content)
  {
    if ((content == null) || (content.length() == 0))
      return content;
    content = replaceString(content, "\r\n", "<br>");
    content = replaceString(content, "\n", "<br>");
    return content;
  }

  public static final String ascii2Html(String content)
  {
    if ((content == null) || (content.length() == 0))
      return content;
    /**modify by biebiyuan for bug 1158 at 2009-6-18 19:46 begin */
   /* content = replaceString(content, "&", "&amp;");
    content = replaceString(content, "\"", "&quot;");
    content = replaceString(content, "<", "&lt;");
    content = replaceString(content, ">", "&gt;");
    content = replaceString(content, "'", "&#39");
    content = replaceString(content, "\n", "<br>");*/
    
    content = replaceString(content, "&", "&amp;");
    content = replaceString(content, "<", "&lt;");
    content = replaceString(content, ">", "&gt;");
    content = replaceString(content, "\n", "<br>");
    content = replaceString(content, "\"", "&quot;");
    content = replaceString(content, "'", "&#39");
    
    /**modify by biebiyuan for bug 1158 at 2009-6-18 19:46 begin */
    return content;
  }
  /**
   * 将字符串content格式由html转换成ascii(美国信息交换标准代码)
   * @param content 需要替换的字符串
   * @return 替换后的字符串
   */
  public static final String html2Ascii(String content)
  {
    if ((content == null) || (content.length() == 0))
      return content;
    content = replaceString(content, "&amp;", "&");
    content = replaceString(content, "&quot;", "\"");
    content = replaceString(content, "<br>", "\r\n");
    content = replaceString(content, "&lt;", "<");
    content = replaceString(content, "&gt;", ">");
    content = replaceString(content, "&nbsp;", " ");
    content = replaceString(content, "&#39;", "'");
    return content;
  }

  /**
   * 将字符串content编码格式由GBK转换成ISO8859_1
   * @param paramString 需要替换的字符串
   * @return 替换后的字符串
   */
  public static final String GBK2ISO(String paramString)
  {
    try
    {
      return new String(paramString.getBytes("GBK"), "ISO8859_1");
    }
    catch (Exception localException)
    {
      System.err.println("GBK转换成ISO错误!");
    }
    return null;
  }

  /**
   * 将字符串content编码格式由GB2312转换成UTF-8
   * @param paramString 需要替换的字符串
   * @return 替换后的字符串
   */
  public static final String GB23122UTF8(String paramString)
  {
    try
    {
      return new String(paramString.getBytes("GB2312"), "UTF-8");
    }
    catch (Exception localException)
    {
      System.err.println("GBK转换成ISO错误!");
    }
    return null;
  }

  /**
   * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
   * 
   * @param s
   *          原文件名
   * @return 重新编码后的文件名
   */
  public static String toUtf8String(String s) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c >= 0 && c <= 255) {
        sb.append(c);
      } else {
        byte[] b;
        try {
          b = Character.toString(c).getBytes("UTF-8");
        } catch (Exception ex) {
          b = new byte[0];
        }
        for (int j = 0; j < b.length; j++) {
          int k = b[j];
          if (k < 0)
            k += 256;
          sb.append("%" + Integer.toHexString(k).toUpperCase());
        }
      }
    }
    return sb.toString();
  }
  
  /**
   * 将字符串content编码格式由ISO8859-1转换成UTF-8
   * @param paramString 需要替换的字符串
   * @return 替换后的字符串 
   */
  public static final String ISO2UTF8(String paramString)
  {
    try
    {
      return new String(paramString.getBytes("ISO8859-1"), "UTF-8");
    }
    catch (Exception localException)
    {
      System.out.println("ISO2UTF8");
    }
    return null;
  }

  /**
   * 将字符串content编码格式由Windows-1252转换成UTF-8
   * @param paramString 需要替换的字符串
   * @return 替换后的字符串
   */
  public static final String Windows1252UTF8(String paramString)
  {
    try
    {
      return new String(paramString.getBytes("Windows-1252"), "UTF-8");
    }
    catch (Exception localException)
    {
      System.out.println("Windows1252UTF8");
    }
    return null;
  }

  /**
   * 将字符串content编码格式由UTF-8转换成ISO8859-1
   * @param paramString 需要替换的字符串
   * @return 替换后的字符串
   */
  public static final String UTF82ISO(String paramString)
  {
    try
    {
      return new String(paramString.getBytes("UTF-8"), "ISO8859-1");
    }
    catch (Exception localException)
    {
      System.err.println("GBK转换成ISO错误!");
    }
    return null;
  }

  /**
   * 将字符串content编码格式由UTF-8转换成GBK
   * @param paramString 需要替换的字符串
   * @return 替换后的字符串
   */
  public static final String UTF82GBK(String paramString)
  {
    try
    {
      return new String(paramString.getBytes("UTF-8"), "GBK");
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  /**
   * 将字符串content编码格式由UTF-8转换成gb2312
   * @param paramString 需要替换的字符串
   * @return 替换后的字符串
   */
  public static final String UTF82GB2312(String paramString)
  {
    try
    {
      return new String(paramString.getBytes("UTF-8"), "gb2312");
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  /**
   * 将字符串content编码格式由ISO8859_1转换成GBK
   * @param paramString 需要替换的字符串
   * @return 替换后的字符串
   */
  public static String ISO2GBK(String paramString)
  {
    try
    {
      return new String(paramString.getBytes("ISO8859_1"), "GBK");
    }
    catch (Exception localException)
    {
      System.err.println("ISO转换成GBK错误!");
    }
    return null;
  }

  /**
   * 对空值处理默认为""
   * @param paramString 需要转换的字符串
   * @return 转换后的字符串
   */
  public static final String dealNull(String paramString)
  {
    if (paramString == null)
      return "";
    return paramString;
  }

  /**
   * 对sql语句的处理
   * @param paramString 需要转换的sql语句
   * @return 转换后的sql语句
   */
  public static final String dealSql(String paramString)
  { 
    if ((paramString == null) || (paramString.trim().length() == 0))
      return paramString;
    String str = replaceString(paramString.trim().replaceAll("\r", ""), "'", "''");
    str = replaceString(str, "\\", "\\\\");
    if (str.charAt(0) != '\'')
      str = "'" + str;
    if (!(str.endsWith("'")))
      str = str + "'";
    return str;
  }
  
/**
 * 将字符数组各元素用","分开进行组合
 * @param paramArrayOfString 需要组合的字符数组
 * @return 组合后的字符串
 */
  public static final String getMultiString(String[] paramArrayOfString)
  {
    return getMultiString(paramArrayOfString, ",");
  }

  /**
   * 将字符数组各元素用传过来的参数分开进行组合
   * @param paramArrayOfString 需要组合的字符数组
   * @param paramString 用来进行分隔的字符
   * @return 组合后的字符串
   */
  public static final String getMultiString(String[] paramArrayOfString, String paramString)
  {
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0))
      return "";
    StringBuffer localStringBuffer = new StringBuffer();
    String str = paramString;
    String[] arrayOfString = paramArrayOfString;
    int i = 0;
    for (i = 0; i < arrayOfString.length - 1; ++i)
      localStringBuffer.append(arrayOfString[i] + str);
    localStringBuffer.append(arrayOfString[i]);
    return localStringBuffer.toString();
  }

  /**
   * 判断是传入的字符串是否为空
   * @param paramString 需要检验的字符串
   * @return 是否为空
   */
  public static final boolean isBlank(String paramString)
  {
    return ((paramString == null) || (paramString.trim().length() == 0));
  }

  /**
   * @deprecated
   * @param paramString
   * @return
   */
  public static final String getString(String paramString)
  {
    return getString(paramString, null);
  }

  /**
   * @deprecated
   * @param paramString
   * @return
   */
  public static final String getString(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      return paramString2;
    return paramString1;
  }

  /**
   * 将字符型转化为int型
   * @param paramString 需要转化的字符串
   * @return 转化后的整型数据
   * @throws Exception 
   */
  public static final int getInt(String paramString)
  {
    return getInt(paramString, 0);
  }

  /**
   * 将字符型转化为int型
   * @param paramString 需要转化的字符串
   * @param paramInt 当要转化的字符串为null值时转化成的数值
   * @return 转化后的整型数据
   * @throws Exception 
   */
  public static final int getInt(String paramString, int paramInt)
  {
    if (paramString == null)
      return paramInt;
    try
    {
      return Integer.parseInt(paramString.trim());
    }
    catch (Exception localException)
    {
    }
    return paramInt;
  }

  /**
   * 将字符型转化为long型
   * @param paramString 需要转化的字符串
   * @return 转化后的long型数据
   * @throws Exception 
   */
  public static final long getLong(String paramString)
    throws Exception, NumberFormatException
  {
    if (paramString == null)
      throw new Exception("getLong(String strName):Input value is NULL!");
    try
    {
      return Long.parseLong(paramString.trim());
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return 0L;
  }

  /**
   * 将字符型转化为Double型
   * @param paramString 需要转化的字符串
   * @return 转化后的Double型数据
   * @throws Exception 
   */
  public static final double getDouble(String paramString)
    throws Exception, NumberFormatException
  {
    if (paramString == null)
      throw new Exception("getDouble(String strName):Input value is NULL!");
    try
    {
      return Double.parseDouble(paramString);
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return 0D;
  }

  /**
   * 将字符型转化为Double型
   * @param request servlet请求
   * @param paramString request请求对象中的参数名
   * @return 转化后的Double型数据
   * @throws Exception 
   */
  public static final double getDouble(HttpServletRequest request, String paramString)
    throws Exception, NumberFormatException
  {
    String str = getString(request, paramString);
    try
    {
      return Double.parseDouble(str);
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return 0D;
  }

  /**
   * 将字符型转化为float型
   * @param paramString 需要转化的字符串
   * @return 转化后的float型数据
   * @throws Exception 
   */
  public static final float getFloat(String paramString)
    throws Exception, NumberFormatException
  {
    if (paramString == null)
      throw new Exception("Input value is NULL!");
    try
    {
      return Float.parseFloat(paramString.trim());
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw new NumberFormatException("getFloat(String) NumberFormatException for input string:" + paramString);
    }
  }

  /**
   * 将字符型转化为float型
   * @param request servlet请求
   * @param paramString request请求对象中的参数名
   * @return 转化后的float型数据
   * @throws Exception 
   */
  public static final float getFloat(HttpServletRequest request, String paramString)
    throws Exception, NumberFormatException
  {
    String str = request.getParameter(paramString);
    if (str == null)
      return 0F;
    try
    {
      return Float.parseFloat(str.trim());
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return 0F;
  }

  /**
   * 将字符型转化为float型
   * @param request servlet请求
   * @param paramString request请求对象中的参数名
   * @param paramFloat 当取得的数据为null或转化出现错误时返回该数值
   * @return 转化后的float型数据
   * @throws Exception 
   */
  public static final float getFloat(HttpServletRequest paramHttpServletRequest, String paramString, float paramFloat)
    throws Exception, NumberFormatException
  {
    String str = paramHttpServletRequest.getParameter(paramString);
    if (str == null)
    {
      System.out.println("val is null,so return default!");
      return paramFloat;
    }
    try
    {
      return Float.parseFloat(str.trim());
    }
    catch (NumberFormatException localNumberFormatException)
    {
      System.out.println("getFloat(HttpServletRequest request,String strName,float def) NumberFormatException for input string:" + str + ",so return default!");
    }
    return paramFloat;
  }

  /**
   * 将字符型转化为long型
   * @param paramString 需要转化的字符串
   * @param paramLong 当传入的字符串为null或转化出现错误时返回该数值
   * @return 转化后的long型数据
   * @throws Exception 
   */
  public static final long getLong(String paramString, long paramLong)
  {
    if (paramString == null)
      return paramLong;
    try
    {
      return Long.parseLong(paramString);
    }
    catch (Exception localException)
    {
    }
    return paramLong;
  }

  /**
   * @deprecated
   */
  public static final String getString(String paramString, int paramInt)
  {
    if (paramString == null)
      return null;
    if (stringToByteArray(paramString) <= paramInt)
      return paramString;
    byte[] arrayOfByte1 = paramString.getBytes();
    byte[] arrayOfByte2 = new byte[paramInt];
    for (int i = 0; i < paramInt; ++i)
      arrayOfByte2[i] = arrayOfByte1[i];
    return byteArrayToString(arrayOfByte2);
  }

  /**
   * 将传过来的String参数解码为字节序列,并将结果存储到一个新的字节数组中
   * @param paramString 需要转化的字符串
   * @return 转化后的字节数组
   */
  private static int stringToByteArray(String paramString)
  {
    if (paramString == null)
      return 0;
    return paramString.getBytes().length;
  }

  /**
   * @deprecated
   * @return
   */
  private static final String byteArrayToString(byte[] paramArrayOfByte)
  {
    String str = new String(paramArrayOfByte);
    if (str.length() == 0)
    {
      int i = paramArrayOfByte.length;
      if (i > 1)
      {
        byte[] arrayOfByte = new byte[i - 1];
        for (int j = 0; j < i - 1; ++j)
          arrayOfByte[j] = paramArrayOfByte[j];
        return byteArrayToString(arrayOfByte);
      }
      return "";
    }
    return str;
  }

 

  /**
   * 将servlet请求对象中的参数转换成整型数据
   * @param request servlet请求
   * @param paramString request请求对象中的参数名
   * @return 转化后的整型数据
   * @throws Exception 
   */
  public static final int getInt(HttpServletRequest request, String paramString)
  {
    return getInt(request, paramString, 0);
  }

  /**
   * 将servlet请求对象中的参数转换成整型数据
   * @param request servlet请求
   * @param paramString request请求对象中的参数名
   * @param paramInt 当取得的参数为null时返回该数值
   * @return 转化后的整型数据
   * @throws Exception 
   */
  public static final int getInt(HttpServletRequest request, String paramString, int paramInt)
  {
    if (request.getParameter(paramString) != null)
      try
      {
        int i = Integer.parseInt(request.getParameter(paramString));
        return i;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        return 0;
      }
    return paramInt;
  }

  /**
   * 将servlet请求对象中的参数转换成long型数据
   * @param request servlet请求
   * @param paramString request请求对象中的参数名
   * @return 转化后的long型数据
   * @throws Exception 
   */
  public static final long getLong(HttpServletRequest request, String paramString)
  {
    if (request.getParameter(paramString) != null)
      try
      {
        long l = Long.parseLong(request.getParameter(paramString).trim());
        return l;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        return 0L;
      }
    return 0L;
  }

  /**
   * 将servlet请求对象中的参数转换成long型数据
   * @param request servlet请求
   * @param paramString request请求对象中的参数名
   * @param paramInt 当取得的参数为null时返回该数值
   * @return 转化后的long型数据
   * @throws Exception 
   */
  public static final long getLong(HttpServletRequest request, String paramString, long paramLong)
  {
    if (request.getParameter(paramString) != null)
      try
      {
        long l = Long.parseLong(request.getParameter(paramString).trim());
        return l;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        return 0L;
      }
    return paramLong;
  }

  /**
   * 将servlet请求对象中的参数进行验证
   * @param request servlet请求
   * @param paramString request请求对象中的参数名
   * @return 验证后的数据
   * @throws Exception 
   */
  public static final String getString(HttpServletRequest request, String paramString)
  {
    if (paramString == null)
      return null;
    String str = request.getParameter(paramString);
    if ((str != null) && (!(str.equals(""))))
      return str.trim();
    return "";
  }

  /**
   * 将servlet请求对象中的参数进行验证
   * @param request servlet请求
   * @param paramString1 request请求对象中的参数名
   * @param paramString2 当取得的参数为null或空值时返回该值
   * @return 验证后的数据
   * @throws Exception 
   */
  public static final String getString(HttpServletRequest request, String paramString1, String paramString2)
  {
    String str = request.getParameter(paramString1);
    if ((str != null) && (!(str.equals(""))))
      return str.trim();
    return paramString2;
  }

  /**
   * 获取Session对象
   * @param request servlet请求
   * @param paramString1 request请求对象中的参数名
   * @param paramString2 当取得的参数为null或空值时返回该值
   * @return 验证后的数据
   * @throws Exception 
   */
  public static final HttpSession getSession(HttpServletRequest request)
  {
    return request.getSession(true);
  }

  /**
   * 将paramString1根据paramInt截取指定的字符串并与paramString2组合
   * @param paramString1 需要截取的字符串
   * @param paramInt 截取的长度
   * @param paramString2 进行组合的字符串
   * @return 截取并组合后的值
   */
  public static final String cutString(String paramString1, int paramInt, String paramString2)
  {
    int i = paramString1.length();
    if (i > paramInt)
      paramString1 = paramString1.substring(0, paramInt) + paramString2;
    return paramString1;
  }
  
  /**
   * 将paramString1根据paramInt截取指定的字符串并与paramString2组合
   * @param paramString1 需要截取的字符串
   * @param paramInt 截取的长度
   * @param paramString2 进行组合的字符串
   * @return 截取并组合后的值
   */
  public static final String splitString(String paramString1, int paramInt, String paramString2) 
  { 
		int cutLength = 0; 
		int byteNum = paramInt; 
		byte btParamString[] = paramString1.getBytes();
		int cutStringLength = btParamString.length;
		if(cutStringLength > paramInt)
		{
			if (paramInt > 1) 
			{ 
				for (int i = 0; i < byteNum; i++) 
				{ 
					if (btParamString[i] < 0) 
					{ 
						cutLength++; 
					} 
				} 

				if (cutLength % 2 == 0) 
				{ 
					cutLength /= 2; 
				}
				else 
				{ 
					cutLength=0; 
				} 
			} 
			int result=cutLength+--byteNum; 
			if(result>paramInt) 
			{ 
				result=paramInt; 
			} 
			if (paramInt == 1) 
			{ 
				if (btParamString[0] < 0) 
				{ 
					result+=2; 
				}
				else 
				{ 
					result+=1; 
				} 
			} 
			String substring = new String(btParamString, 0, result);
			return substring + paramString2;
		}
		else
		{
			return paramString1;
		}
		
	}
  /**
   * 用于格式化十进制的double型数据
   * @param paramString 格式化的格式
   * @param paramDouble 需要格式化的double型数据
   * @return 格式化后的数据
   */
  public static String formatNumber(String paramString, double paramDouble)
  {
    DecimalFormat localDecimalFormat = new DecimalFormat(paramString);//使用给定的模式和默认语言环境的符号创建一个 DecimalFormats
    return localDecimalFormat.format(paramDouble);
  }

  /**
   * 用于格式化十进制的long型数据
   * @param paramString 格式化的格式
   * @param paramLong 需要格式化的long型数据
   * @return 格式化后的数据
   */
  public static String formatNumber(String paramString, long paramLong)
  {
    DecimalFormat localDecimalFormat = new DecimalFormat(paramString);//使用给定的模式和默认语言环境的符号创建一个 DecimalFormat
    return localDecimalFormat.format(paramLong);
  }

  /**
   * 获取返回路径地址
   * @param request 请求对象
   * @return 请求对象的返回路径地址
   */
  public static final String getBackURL(HttpServletRequest request)
  {
    String str1 = getString(request, "backurl");
    str1 = replaceString(str1, "@", "");
    if (str1.equals(""))
    {
      String str2 = request.getHeader("referer");
      if (str2 == null)
        str2 = "";
      return str2;
    }
    return str1;
  }

  /**
   * 获取当前路径地址
   * @param request 请求对象
   * @return 请求对象的当前路径地址
   */
  public static String getCurrentURL(HttpServletRequest request)
  {
    String str = request.getQueryString();
    if (str == null)
      return request.getRequestURI();
    return request.getRequestURI() + "?" + str;
  }

  /**
   * 对URL进行编码（译成密码）
   * @param paramString 需要加密的URL
   * @return 处理后的URL
   */
  public static String enCodeBackURL(String paramString)
  {
    return paramString.replace('&', '!');
  }

  /**
   * 对URL进行解码
   * @param paramString 需要解码的URL
   * @return 解码后的URL
   */
  public static String deCodeBackURL(String paramString)
  {
    return paramString.replace('!', '&');
  }

  /**
   * @deprecated
   * @param paramString
   * @return
   */
  public static long bin2Dec(String paramString)
  {
    int i = paramString.length();
    long l = 0L;
    for (int k = 0; k < i; ++k)
    {
      int j = getInt(paramString.substring(i - k - 1, i - k));
      l = (long)(l + j * Math.pow(2.0D, k));
    }
    return l;
  }

  /**
   * 得到checkBox和Radio的状态
   * @param paramInt 选中状态的标志,0为未选中
   * @return 选中状态
   */
  public static String getStatusForCheckBoxAndRadio(int paramInt)
  {
    if (paramInt == 0)
      return "";
    return "checked";
  }

  /**
   * 设置localCookie(设置有效时间)
   * @param response 响应对象
   * @param cookieName Cookie名
   * @param originalCookie 原有Cookie
   * @param time Cookie的有效时间
   * @throws UnsupportedEncodingException
   */
  public static void setCookie(HttpServletResponse response, String cookieName, String originalCookie, int time)
    throws UnsupportedEncodingException
  {
    Cookie localCookie = new Cookie(cookieName, URLEncoder.encode(originalCookie, "utf-8"));
    localCookie.setMaxAge(time);
    localCookie.setPath("/");
    response.addCookie(localCookie);
  }

  /**
   * 设置session的localCookie(不设置有效时间)
   * @param response 响应对象
   * @param cookieName Cookie名
   * @param originalCookie 原有cookie
   * @throws UnsupportedEncodingException
   */
  public static void setSessionCookie(HttpServletResponse response, String cookieName, String originalCookie)
    throws UnsupportedEncodingException
  {
    Cookie localCookie = new Cookie(cookieName, URLEncoder.encode(originalCookie, "utf-8"));
    localCookie.setPath("/");
    response.addCookie(localCookie);
  }

  /**
   * 设置localCookie(设置有效时间)
   * @param response 响应对象
   * @param cookieName Cookie名
   * @param originalCookie 原有cookie
   * @param time Cookie的有效时间
   * @param domainNames  所有域名
   * @throws UnsupportedEncodingException
   */
  public static void setCookie(HttpServletResponse response, String cookieName, String originalCookie, int time, String[] domainNames)
    throws UnsupportedEncodingException
  {
    for (int i = 0; i < domainNames.length; ++i)
    {
      Cookie localCookie = new Cookie(cookieName, URLEncoder.encode(originalCookie, "utf-8"));
      localCookie.setMaxAge(time);
      localCookie.setPath("/");
      localCookie.setDomain(domainNames[i]);
      response.addCookie(localCookie);
    }
  }

  /**
   * 设置session的localCookie(设置有效时间)
   * @param response 响应对象
   * @param cookieName Cookie名
   * @param originalCookie 原有cookie
   * @param domainNames 所有域名
   * @throws UnsupportedEncodingException
   */
  public static void setSessionCookie(HttpServletResponse response, String cookieName, String originalCookie, String[] domainNames)
    throws UnsupportedEncodingException
  {
    for (int i = 0; i < domainNames.length; ++i)
    {
      Cookie localCookie = new Cookie(cookieName, URLEncoder.encode(originalCookie, "utf-8"));
      localCookie.setPath("/");
      localCookie.setDomain(domainNames[i]);
      response.addCookie(localCookie);
    }
  }

  /**
   * 根据参数得到相关指定Cookie值
   * @param request 请求对象
   * @param paramString 指定的参数
   * @return 指定的Cookie值
   * @throws UnsupportedEncodingException
   */
  public static String getCookie(HttpServletRequest request, String paramString)
    throws UnsupportedEncodingException
  {
    Cookie[] arrayOfCookie = request.getCookies();
    if (arrayOfCookie == null)
      return null;
    for (int i = 0; i < arrayOfCookie.length; ++i)
      if ((arrayOfCookie[i] != null) && (arrayOfCookie[i].getName().equals(paramString)))
        return URLDecoder.decode(arrayOfCookie[i].getValue(), "utf-8");
    return "SANY06001"; //星沙园区
  }

  /**
   * 得到所有Cookie值
   * @param request 请求对象
   * @return 所有Cookie值
   * @throws UnsupportedEncodingException
   */
  public static String[] getCookie(HttpServletRequest request)
    throws UnsupportedEncodingException
  {
    Cookie[] arrayOfCookie = request.getCookies();
    ArrayList localArrayList = new ArrayList();
    if (arrayOfCookie == null)
      return null;
    for (int i = 0; i < arrayOfCookie.length; ++i)
      localArrayList.add(arrayOfCookie[i].getName() + " = " + URLDecoder.decode(arrayOfCookie[i].getValue(), "utf-8"));
    return ((String[])(String[])localArrayList.toArray(new String[0]));
  }

  /**
   * 根据参数删除相关指定Cookie值
   * @param response 响应对象
   * @param request 请求对象
   * @param paramString 指定参数
   * @throws UnsupportedEncodingException
   */
  public static void delCookie(HttpServletResponse response, HttpServletRequest request, String paramString)
    throws UnsupportedEncodingException
  {
    Cookie[] arrayOfCookie = request.getCookies();
    if (arrayOfCookie != null)
      for (int i = 0; i < arrayOfCookie.length; ++i)
      {
        String str = arrayOfCookie[i].getName();
        if (str.equals(paramString))
        {
          arrayOfCookie[i].setMaxAge(0);
          response.addCookie(arrayOfCookie[i]);
        }
      }
  }

  /**
   * 根据参数Cookie名和域名删除相关指定Cookie值
   * @param response 响应对象
   * @param cookieName Cookie名
   * @param domainName 域名
   * @throws UnsupportedEncodingException
   */
  public static void delCookie(HttpServletResponse response, String cookieName, String[] domainName)
    throws UnsupportedEncodingException
  {
    for (int i = 0; i < domainName.length; ++i)
    {
      Cookie localCookie = new Cookie(cookieName, null);
      localCookie.setMaxAge(0);
      localCookie.setPath("/");
      localCookie.setDomain(domainName[i]);
      response.addCookie(localCookie);
    }
  }

  /**
   * 去掉字符串中的回车符
   * @param paramString 需要处理的字符串
   * @return 去掉回车符后的字符串
   * @throws UnsupportedEncodingException
   */
  public static String removeHH(String paramString)
  {
    return replaceString(replaceString(paramString, "\n", ""), "\r", "");
  }

 
  /**
   * 设置指定字符串内容的颜色
   * @param content 字符串内容
   * @param color 颜色
   * @param paramBoolean 是否设置颜色的判断标志
   * @return 指定颜色后的字符串内容
   */
  public static String withColor(String content, String color, boolean paramBoolean)
  {
    if (paramBoolean)
      return "<font color='".concat(color).concat("'>").concat(content).concat("</font>");
    return content;
  }

  /**
   * 从session中取得属性值
   * @param session Session对象
   * @param paramString 属性名
   * @return session中取得的String型属性值
   */
  public static String getStringFromSession(HttpSession session, String paramString)
  {
    if (session.getAttribute(paramString) != null)
      return session.getAttribute(paramString).toString();
    return null;
  }

  /**
   * 从session中取得属性值
   * @param session Session对象
   * @param paramString 属性名
   * @return session中取得的int型属性值
   */
  public static int getIntFromSession(HttpSession session, String paramString)
  {
    if (getStringFromSession(session, paramString) != null)
      return getInt(getStringFromSession(session, paramString));
    return 0;
  }

  /**
   * 从session中取得属性值
   * @param session Session对象
   * @param paramString 属性名
   * @return session中取得的float型属性值
   */
  public static float getFloatFromSession(HttpSession session, String paramString)
  {
    if (getStringFromSession(session, paramString) != null)
      try
      {
        return getFloat(getStringFromSession(session, paramString));
      }
      catch (NumberFormatException localNumberFormatException)
      {
        return 0F;
      }
      catch (Exception localException)
      {
        return 0F;
      }
    return 0F;
  }
/**
 * 获取请求的所有参数信息
 * @param request 请求对象
 * @return 请求的所有参数信息
 */
  public static String getAllParameters(HttpServletRequest request)
  {
    Enumeration localEnumeration = request.getParameterNames();
    StringBuffer localStringBuffer = new StringBuffer("");
    while (localEnumeration.hasMoreElements())
    {
      String str1 = (String)localEnumeration.nextElement();
      String str2 = request.getParameter(str1);
      localStringBuffer.append(str1);
      localStringBuffer.append("=");
      localStringBuffer.append(str2);
      localStringBuffer.append("&");
    }
    return localStringBuffer.toString();
  }
 
 

  /**
   * 通过对正则表达式与传过来的字符串内容进行匹配，来对字符串内容进行追加和替换操作
   * @param content 字符串内容
   * @param regularExpression 正则表达式
   * @param replacement  替换成的内容
   * @return 处理后的字符串内容
   */
  public static String regReplace(String content, String regularExpression, String replacement)
  {
    String str = "";
    str = content;
    if ((content != null) && (!(content.equals(""))))
    {
      str = content;
      Pattern localPattern = Pattern.compile(regularExpression, 2);//正则表达式的编译表示形式  

      Matcher localMatcher = localPattern.matcher(str);//通过解释 Pattern 对字符序列执行匹配操作的引擎
      StringBuffer localStringBuffer = new StringBuffer();
      int i = 0;
      for (boolean bool = localMatcher.find(); bool; bool = localMatcher.find())
      {
        ++i;
        localMatcher.appendReplacement(localStringBuffer, replacement);//实现非终端追加和替换步骤
      }
      localMatcher.appendTail(localStringBuffer);//实现终端追加和替换步骤
      str = localStringBuffer.toString();
    }
    else
    {
      str = "";
    }
    return str;
  }

 

  /**
   * 将字符串信息中带\n 和\r都置空
   * @param paramString 需求进行回车符替换的字符串
   * @return 替换后的字符串
   */
  public static final String cuteNR(String paramString)
  {
    paramString = replaceString(paramString, "\n", "");
    paramString = replaceString(paramString, "\r", "");
    return paramString;
  }
 

  /**
   * 取得Double型的初始值
   * @return 0D
   */
  public static final double getDoubleType()
  {
    return 0D;
  }

  /**
   * 取得float型的初始值
   * @return 0F
   */
  public static final float getFloatType()
  {
    return 0F;
  }

  /**
   * 取得long型的初始值
   * @return 0L
   */
  public static final long getLongType()
  {
    return 0L;
  }

  /**
   * 将string型转换成long型
   * @param paramString 需要转换的字符串
   * @return 转换后的long型数据
   */
  public static final long getLongType(String paramString)
  {
    return Long.parseLong(paramString);
  }

  /**
   * 取得boolean值
   * @return 返回true
   */
  public static final boolean getTrueType()
  {
    return true;
  }

  /**
   * 取得boolean值
   * @return 返回false
   */
  public static final boolean getFalseType()
  {
    return false;
  }

  /**
   * 创建一个Object对象
   * @return Object对象
   */
  public static final Object getObjectType()
  {
    return new Object();
  }

  /**
   * 进行验证
   * @param paramInt 验证标志
   * @return 当传入的验证标志为1时返回true,否则返回false
   */
  public static final boolean getBooleanType(int paramInt)
  {
    return (paramInt == 1);
  }

  /**
   * 获取null对象
   * @return null对象
   */
  public static final Object getNullType()
  {
    return null;
  }
 
/**
 * 设置select默认的选中值
 * @param paramLong1 类型编号1
 * @param paramLong2 类型编号2
 * @return 当输入参数相等时,返回选中
 */
  public static final String selectIt(long paramLong1, long paramLong2)
  {
    return ((paramLong1 == paramLong2) ? "selected" : "");
  }

  /**
   * 设置radio默认的选中值
   * @param paramLong1 类型编号1
   * @param paramLong2 类型编号2
   * @return 当输入参数相等时,返回选中
   */
  public static final String checkedIt(long paramLong1, long paramLong2)
  {
    return ((paramLong1 == paramLong2) ? "checked" : "");
  }

  /**
   * 设置radio默认的选中值
   * @param paramLong1 参数1
   * @param paramLong2 参数2
   * @return 当输入参数相等时,返回选中
   */
  public static final String checkedIt(String paramString1, String paramString2)
  {
    return ((paramString1.equals(paramString2)) ? "checked" : "");
  }

  /**
   * 取得手续费的百分比
   * @param paramLong1 手续费
   * @return 手续费的百分比
   */
  public float getPayCostPercent(float paramFloat)
  {
    return (paramFloat * 100.0F);
  }

  /**
   * 判断对像是否为null值
   * @param paramObject 需要检验的对象
   * @return 检验后的结果:1表示为null,0不为null
   */
  public static final int isNull(Object paramObject)
  {
    if (paramObject == null)
      return 1;
    return 0;
  }
  public static final String parseStringFromFCK(String fck)
  {
	  //fck = fck.replaceAll(" src=\"/", " src=\""+ConfigBean.getStringValue("server_bpo_root")+"/");
	  //fck = fck.replaceAll(" href=\"/", " href=\""+ConfigBean.getStringValue("server_bpo_root")+"/");
	  return fck;
  }
  
  //ADD START BY CAIQI 2009-05-13
  /**
   * 判断字符串是不是日期时间
   * @param String类型的日期
   * @return 是/否
   */
   public static final boolean toOracleDate(String todate){
	  Pattern pattern = Pattern.compile("^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))(\\s(((0?[0-9])|([1-2][0-9]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
	  Matcher matcher = pattern.matcher(todate);
	  return matcher.matches();
  }
  /**
   * 判断字符串是不是日期
   * @param String类型的日期
   * @return 是/否
   */
  public static final boolean isDate(String todate){
	  Pattern pattern = Pattern.compile("^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$");
	  Matcher matcher = pattern.matcher(todate);
	  return matcher.matches();
  }
  /**
   * 转换字符串日期为Oracle的日期
   * @param String类型的日期
   * @return Oracle的日期函数
   */
  public static final String toDate(String todate){
	  return " to_date('"+todate+"', 'yyyy-mm-dd hh24:mi:ss')";
  }
  
  public static String DateformatString(Date date , String formateString){
	  SimpleDateFormat sdf = new SimpleDateFormat(formateString);//"yyyy/MM/dd HH:mm:ss"
		if(date!=null){
			return sdf.format(date);
		}
		return "";
  }
  
  /**
   * 当前时间添加天数
   * @param num
   * @return
   */
  public static Date DateAddNowdate(String num){
	  if(num == null)num="0";
	  int numInt = Integer.valueOf(num);
	  Calendar s = Calendar.getInstance();
	  s.add(Calendar.DATE, numInt);
	  return s.getTime();
  }
  
  /**
   * 指定时间添加天数
   * @param num
   * @return
   */
  public static Date DateAdddate(Date currentdate,String num){
	  if(num == null)num="0";
	  int numInt = Integer.valueOf(num);
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	  
	  Date newDate = new Date(sdf.format(currentdate));
	  
	  newDate.setDate(newDate.getDate()+numInt);
	  return newDate;
  }
  
  /**
   * 根据source规则，生成父编号
   * @param from
   * @return
   */
  public static String getProductTypeFatherId(String from){
	  String [] source = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	  String to = "";
	  for(int i=0;i<source.length;i++){
		 String tmp = source[i];
		 if(tmp.equals(from)){
			 i++;
			 to= source[i];
			 break;
		 }
	  }
	  return to;
  }
  
  /**
   * 根据source规则，子编号
   * @param from
   * @return
   */
  public static String getProductTypeChildId(String fatherid,String from){
	  String to = "";
	  int fromInt = Integer.valueOf(from);
	  fromInt = fromInt+1;
	  String fromStr = String.valueOf(fromInt);
	  int len = fromStr.length();
	  if(len==1){
		  fromStr = "000"+fromStr;
	  }
	  else if(len==2){
		  fromStr = "00"+fromStr;
	  }
	  else if(len==3){
		  fromStr = "0"+fromStr;
	  }
	  to = fromStr;
	  
	  return to;
  }
  
  /**
   * 将指定字符串进行MD5加密后返回的字符串结果
   * @param paramString 需要加密的字符串
   * @return 加密后的字符串
   */
  public static String getMD5(String paramString)
  {
    if (paramString == null)
      return null;
    if (paramString.trim().length() == 0)
      return paramString;
    Md5 localMd5 = new Md5();
    return localMd5.getMD5ofStr(paramString);
  }
  
  public static void main(String [] args){
	  //StringUtil.getProductTypeChildId("A","0012");
	 System.out.println( StringUtil.getMD5("888"));
  }
  
  //ADD END BY CAIQI 2009-05-13
//  public static void main(String[] paramArrayOfString)
//    throws Exception
//  {
//    int i = 0;
//    String str = "3";
//    String[] arrayOfString = str.split(",");
//    StringBuffer localStringBuffer = new StringBuffer("");
//    while (i < arrayOfString.length - 1)
//    {
//      localStringBuffer.append(arrayOfString[i]);
//      localStringBuffer.append("-");
//      ++i;
//    }
//    localStringBuffer.append(arrayOfString[i]);
//    System.out.println(localStringBuffer.toString());  
//  }
}