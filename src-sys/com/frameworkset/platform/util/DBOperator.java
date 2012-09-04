package com.frameworkset.platform.util;

import java.sql.*;
import java.util.*;
import com.frameworkset.common.poolman.util.SQLManager;

public class DBOperator {
  
  public static DBOperator m_instance = new DBOperator();
  
  private DBOperator(){
  }

  public static DBOperator getInstance(){
  	if (m_instance == null)
  		m_instance = new DBOperator();
  	return m_instance;
  }

  /**
     * 执行数据库查询语句，返回查询结果。
     * @param s 查询语句
     * @return 查询结果
     */
    public static Vector runSelect(String sql,String defaultIfNull) {
        Connection con = null;
        Statement stmt = null;
        ResultSet resultset = null;
        try {
            con = getDBConnection();

            if(con == null) return null;
            stmt = con.createStatement();
            resultset = stmt.executeQuery(sql); /*查询结果句柄*/
            ResultSetMetaData resultsetmetadata = resultset.getMetaData();

            int i = resultsetmetadata.getColumnCount(); /*每条记录的字段数*/
            Vector allRecordVector = new Vector();
            Vector oneRecordVector = null;
            /*resultset为顺序存放，依次存放各条记录*/
           while( resultset.next()) {
                oneRecordVector = new Vector();
                for (int j = 1; j <= i; j++) {
                    String elem = resultset.getString(j);
                    if (elem == null)
                        elem = defaultIfNull;
                    oneRecordVector.addElement(elem);
                }
               allRecordVector.add(oneRecordVector);
            }
            releaseCon(resultset,stmt,con);
            return allRecordVector;
        } catch (Exception e) {
            releaseCon(resultset,stmt,con);
            //e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 执行数据库查询语句，返回结果集。
     * @param s 查询语句
     * @return 查询结果
     */
    public static ResultSet runSelect1(String sql) {
        Connection con = null;
        Statement stmt = null;
        ResultSet resultset = null;
        try {
            con = getDBConnection();

            if(con == null) return null;
            stmt = con.createStatement();
            resultset = stmt.executeQuery(sql); /*查询结果句柄*/            
        } catch (Exception e) {
            releaseCon(resultset,stmt,con);
            //e.printStackTrace();
            return null;
        }
        return resultset;
    }

    /**
     * 执行更新语句，返回更新记录条数
     * @param s 更新语句
     * @return 更新记录条数
     */

    public static boolean runUpdate(String s) {
        return executeUpdate(s);
    }

    /**
     * 执行插入语句，返回插入记录条数
     * @param s 插入语句
     * @return  更新记录条数
     */

    public static boolean runInsert(String s) {
        return executeUpdate(s);
    }

    /**
     * 执行创建表语句，返回创建表数
     * @param s 创建语句
     * @return  创建表数
     */

    public static boolean runCreate(String s) {
        return executeUpdate(s);
    }

    /**
     *
     * @param s String
     * @return boolean
     */

  public static boolean runDelete(String s) {
    return executeUpdate(s);
  }

  public static boolean executeUpdate(String updateSQL) {
        Connection con = null;
        Statement stmt = null;
        ResultSet resultset = null;
        try {
            con = getDBConnection();
            if(con == null) return false;
            stmt = con.createStatement();
            stmt.executeUpdate(updateSQL); /*执行SQL语句*/
            releaseCon(null,stmt,con);
            return true;
        }
        catch (SQLException e) {
            releaseCon(null,stmt,con);
            e.printStackTrace();
            return false;
        }
    }

    public static void releaseCon(ResultSet rs,Statement stmt,Connection con){
        try{
            if(rs != null)rs.close();
            if(stmt!=null)stmt.close();
            if(con!=null)con.close();
        }
        catch(SQLException e){
        }
    }
    
    public static void releaseCon(ResultSet rs,Statement stmt, PreparedStatement pstmt, Connection con){
        try{
            if(rs != null)rs.close();
            if(stmt!=null)stmt.close();
            if(pstmt!=null)pstmt.close();
            if(con!=null)con.close();
        }
        catch(SQLException e){
        }
    }

    public static Connection getDBConnection(){
    	Connection con=null;
    	try {
			con=SQLManager.getInstance().requestConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
    }
    
  

	/**
	 * @param sqlstr
	 * @param params
	 */
	public Vector exeuteQuery(String sqlstr, Object[] params) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getDBConnection();
            pstmt = con.prepareStatement(sqlstr);
            int paramCount = params.length;
            if (paramCount <0 || paramCount != getStrNum(sqlstr,"?"))
            	throw new IllegalArgumentException("参数个数小于0,或者是参数的个数不一致");
            
            for(int i=0;i<paramCount;i++){
           		pstmt.setObject(i+1,params[i]);
            }
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmetadata = rs.getMetaData();

            int i = rsmetadata.getColumnCount(); /*每条记录的字段数*/
            Vector allRecordVector = new Vector();
            Vector oneRecordVector = null;
            /*rs为顺序存放，依次存放各条记录*/
           while( rs.next()) {
                oneRecordVector = new Vector();
                for (int j = 1; j <= i; j++) {
                    String elem = rs.getString(j);
                     oneRecordVector.addElement(elem);
                }
               allRecordVector.add(oneRecordVector);
            }
            releaseCon(null,null,pstmt,con);
            return allRecordVector;
        }
        catch (SQLException e) {
            releaseCon(null,null,pstmt,con);
            e.printStackTrace();
            return null;
        }
		
	}
	

	/**
	 * @param sqlstr
	 * @param string
	 * @return
	 */
	private int getStrNum(String sqlstr, String str) {
		int num = 0;
		int index = sqlstr.indexOf(str);
		while (index != -1) {
			num++;
			index=sqlstr.indexOf(str,index+str.length());
		}
		return num;
	}

	/**
	 * @param sqlstr
	 * @param params
	 * @throws OptimisticLockException
	 */
	public int execute(String sqlstr, Object[] params) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = 0;
        try {
            con = getDBConnection();
            pstmt = con.prepareStatement(sqlstr);
            int paramCount = params.length;

            if (paramCount <0 || paramCount != getStrNum(sqlstr,"?"))
            	throw new IllegalArgumentException("参数个数小于0,或者是参数的个数不一致");

            for(int i=0;i<paramCount;i++){
            	pstmt.setObject(i+1,params[i]);
            }
            pstmt.executeUpdate();
            result = 0;
        }catch(SQLException e){
        	result = e.getErrorCode();
            releaseCon(null,null,pstmt,con);
            e.printStackTrace();
        }
        return result;
	}

	
//	public static void main(String[] args)
//	{
//		Connection con = null;
//		String sql = "select * from tb_netserver_class where classid=1";
//		ResultSet rs = null;
//		try{
//		//con = getDBConnection();	
//		executeUpdate(sql);
//		rs=runSelect1(sql);
//		rs.next();
//		System.out.println(rs.getString("classname"));
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		if(con!=null)
//		System.out.println("---------------------");
//	}

}
