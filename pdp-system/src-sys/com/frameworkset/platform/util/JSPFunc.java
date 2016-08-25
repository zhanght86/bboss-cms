package com.frameworkset.platform.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.frameworkset.common.poolman.DBUtil;

public class JSPFunc implements Serializable
{
	public static String getDate()		//返回当前日期
	{
		return getFormatDate("yyyyMMdd");
	}

 	public static String getFormatDate(int year,int month,
        int date,String format)		
	{
		java.util.Calendar cal = new java.util.GregorianCalendar(year,month,date);
	    java.text.SimpleDateFormat theDate = new java.text.SimpleDateFormat(format);
		return theDate.format(cal.getTime());
	}
	
	public static String getFormatDate(String format)		//返回当前日期
	{
		java.util.Calendar cal = new java.util.GregorianCalendar();
	    java.text.SimpleDateFormat theDate = new java.text.SimpleDateFormat(format);
		return theDate.format(cal.getTime());
	}
	
    public static String getOption(List al)
    {
		return getOption(al,0,1,null);
	}

    public static String getOption(List al,String value)
    {
		return getOption(al,0,1,value);
	}

    public static String getOption(List al,int codeLocation,int nameLocation)
    {
		return getOption(al,codeLocation,nameLocation,null);
	}

    public static String getOption(List al,int codeLocation,int nameLocation,String selectedID)
    {
		if(al==null) return "";
		ArrayList alOneRow;
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i < al.size();i++)
		{
			alOneRow = (ArrayList)al.get(i);
			sb.append("<OPTION VALUE='").append(alOneRow.get(codeLocation)).append("' ")
				.append((selectedID != null && ((String)alOneRow.get(codeLocation)).trim().equals(selectedID) ? "selected" : "")).append(">")
				.append(alOneRow.get(nameLocation)).append("</OPTION>");
		}
		return sb.toString();
	}

    public static String getOption(Vector v,int codeLocation,
    		int nameLocation,String selectedID)
    {
		if(v==null) return "";
		
		ArrayList al = new ArrayList(v);
		
		return getOption(al,codeLocation,nameLocation,selectedID);
    }
    
    public static String getOption(String sql,String selectedID)
    {
    	try{
    		DBUtil db1 = new DBUtil();			
	    	db1.executeSelect(sql);
	    	ArrayList list = new ArrayList();
	    	for(int i=0;i<db1.size();i++){
	    		List oneRow = new ArrayList();
	    		oneRow.add(db1.getString(i, 0));
	    		oneRow.add(db1.getString(i, 1));
	    		list.add(oneRow);
	    	}
	    	return getOption(list,0,1,selectedID);
    	}catch(Exception e){
    		e.printStackTrace();
    		return "<option>取数据错误,请检查!</option>";
    	}
    }
    
    public static String getOption(String sql)
    {
    	return getOption(sql,null);
    }
    
 }
   

