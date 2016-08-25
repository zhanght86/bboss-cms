package com.frameworkset.platform.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.frameworkset.common.poolman.DBUtil;

public class PrimaryKeyCreator {

	private DBUtil dbUtil = new DBUtil();

	public static int getMaxId(String tbname, String keyName) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int maxid = 0;
		try {
			if (con == null) {
				con = DBUtil.getConection();
			}
			String sql = "select max(" + keyName + ") as id from " + tbname;
			pstmt = con.prepareStatement(sql);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				maxid = rs.getInt("id");
			} else {
				maxid = 0;
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			releaseCon(rs, null, pstmt, con);
		}
		return maxid;
	}
	
   public static String getInfoId() {
    
    String infoId = "";
        DBUtil dbUtil=new DBUtil();
        try {
          infoId=dbUtil.getNextStringPrimaryKey("TD_COMM_APPLICATION_PERSONINFO");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return infoId;

  }
   
   public static String getMainId() {
     
     String infoId = "";
         DBUtil dbUtil=new DBUtil();
         try {
           infoId=dbUtil.getNextStringPrimaryKey("TD_COMM_APPLICATION");
     } catch (SQLException e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
     }
     return infoId;

   }
   public static String getOrgLeaderId() {
	     
	     String infoId = "";
	         DBUtil dbUtil=new DBUtil();
	         try {
	           infoId=dbUtil.getNextStringPrimaryKey("td_sm_organization_leader");
	     } catch (SQLException e) {
	       // TODO Auto-generated catch block
	       e.printStackTrace();
	     }
	     return infoId;

	   }
   
   public static String getId() {
     
     String infoId = "";
         DBUtil dbUtil=new DBUtil();
         try {
           infoId=dbUtil.getNextStringPrimaryKey("TD_SP_TRANSACTONLINE");
     } catch (SQLException e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
     }
     return infoId;

   }
	

	public static void releaseCon(ResultSet rs, Statement stmt,
			PreparedStatement pstmt, Connection con) {
		try {
			if (rs != null)
				rs.close();

			if (stmt != null)
				stmt.close();
			if (pstmt != null)
				pstmt.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
		}
	}
public static String getOrginfoId() {
		
		String orgInfoId = "";
        DBUtil dbUtil=new DBUtil();
        try {
        	orgInfoId =dbUtil.getNextStringPrimaryKey("td_sm_organization_baseinfo");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orgInfoId;

	}
	   public static String getOrgSecondId() {
	     
	     String infoId = "";
	         DBUtil dbUtil=new DBUtil();
	         try {
	           infoId=dbUtil.getNextStringPrimaryKey("td_sm_organization_seconde");
	     } catch (SQLException e) {
	       // TODO Auto-generated catch block
	       e.printStackTrace();
	     }
	     return infoId;

	   }
	/**
	 * 锟斤拷锟絪ql锟斤拷锟斤拷锟揭伙拷锟斤拷侄锟街?
	 * 
	 * @author xingguo.yang
	 * @param select
	 * @return 锟街讹拷值
	 */
	public static String getSValueBySelect(String select) {
		String SValue = "";
		DBUtil dbUtil = new DBUtil();
		try {
			dbUtil.executeSelect(select);
			if (dbUtil != null && dbUtil.size() > 0)
				SValue = dbUtil.getString(0, 0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SValue;
	}
	
}
