package com.frameworkset.platform.util;

import java.io.Serializable;
import java.sql.SQLException;

public class CommonUtil implements Serializable{
  /**
    * 报表显示分页
    * @param originalSql String 要查询的SQL语句
    * @param pageNo int 要显示的页号（从0开始）
    * @param pageRows int 每页显示的行数
    * @return String 显示当前页的SQL语句
    */
   public static String getSpiltPageSql(String originalSql,int pageNo,int pageRows)
   {
     int startRow = pageNo * pageRows + 1;
     int endRow = (pageNo+1) * pageRows;
     String operSql = "select * from (select a.*,rownum hh from(" + originalSql +
         ") a where rownum<="+endRow+") where hh >= " + startRow ;
     return operSql;
   }

   /**
    * 获取分页数
    * @param originalSql String 要查询的SQL语句
    * @param pageRows int 每页显示的行数
    * @return int 报表总页数
    */
   public static int getPageNum(String rownum,int pageRows) throws SQLException
   {
     int Pages=0;
     if (rownum.equals("0")){   //报表行数为零，报表页数Pages为0
         Pages=0;
     }else if (pageRows ==0){  //报表行数不为零，且报表每页显示行数设为零，报表页数Pages为1
        Pages=1;
     }else{                   //计算报表页数
         Pages=Integer.parseInt(rownum)%pageRows==0?Integer.parseInt
             (rownum)/pageRows:Integer.parseInt(rownum)/pageRows+1;
     }
         return   Pages; //返回报表总页数
   }

   public static String replaceString(String str,String oldString,String newString)
   {
     if (str == null || str.equals(""))   return "";
     if(oldString == null || oldString.equals(""))  return str;
     if(newString == null) return str;
     int i;
     int j;
     if (newString.indexOf(oldString) != -1){
            int n=0;
            while ( (j = str.substring(n,str.length()).indexOf(oldString)) != -1) {
              j=n+j;
              str = str.substring(0, j) + newString +
              str.substring(j + oldString.length());
              n=j+newString.length();
            }
     }else{
        while ( (i = str.indexOf(oldString)) != -1) {
            str = str.substring(0, i) + newString + str.substring(i + oldString.length());
        }
     }
     return str;
   }
   public static String getStringFromNum(String str){
       System.out.println("在客户端转换成数字的字符串:"+str);
       if(str==null || str.trim().equals("")){
         return "";
       }
       String re[]=str.split("\t");
       StringBuffer sb=new StringBuffer();
       for(int i=0;i<re.length;i++){
           char t = (char)Integer.valueOf(re[i]).intValue();
           sb.append(t);
       }
       System.out.println("在服务器端又转换为了字符串:"+sb.toString());
       return sb.toString();

   }

   public static void main(String[] argc)
   {
     String a = "ab%cd";
     String x = replaceString(a,"ab","d");
     System.out.println(x);
   }
   
}
