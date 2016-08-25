/*
 * Created on 2005-3-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.holiday;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

//import com.frameworkset.common.hibernate.dao.DataAccessException;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class HolidayDao implements java.io.Serializable
{
    public int update(ValueObject vo)
    {
        return 0;
    }

    public ValueObject findByPrimaryKey(Object pk) //throws DataAccessException
    {
        Holiday hol = new Holiday();
        return hol;
    }

    public Collection findByYM(ValueObject vo) //throws DataAccessException
    {
        Collection spprojects = new Vector();
        Holiday hol = (Holiday) vo;
        String yholiday = hol.getYHoliday();
        String mholiday = hol.getMHoliday();
        DBUtil dbUtil = new DBUtil();
        try
        {
            String sqlstr = "select holiday from td_sp_holiday where yholiday='"
                    + yholiday + "' and mholiday='" + mholiday + "'";
            dbUtil.executeSelect(sqlstr);
            for (int i = 0; dbUtil != null && i < dbUtil.size(); i++)
            {
                hol.setHoliday(StringUtils.defaultString(dbUtil.getString(i,
                        "holiday")));
                spprojects.add(hol);
            }
        } catch (SQLException ex)
        {
        }
        return spprojects;
    }

    public String findByDay(Object pk) //throws DataAccessException
    {

        String isSet = "";
        DBUtil db = new DBUtil();
        String sqlstr = "select holiday from td_sp_holiday where holiday='"
                + pk + "'";
        try
        {
            db.executeSelect(sqlstr);
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (db.size() != 0)
            isSet = "checked";

        return isSet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.frameworkset.platform.dao.BasicDAO#insert(com.frameworkset.platform.vo.ValueObject)
     */
    public int insert(ValueObject vo) //throws DataAccessException
    {
        Holiday hol = (Holiday) vo;
        String holiday = hol.getHoliday();
        String yholiday = hol.getYHoliday();
        String mholiday = hol.getMHoliday();
        DBUtil db = new DBUtil();
        String sqlstr = "insert into td_sp_holiday(holiday,yholiday,mholiday) values('"
                + holiday + "','" + yholiday + "','" + mholiday + "')";
        try
        {
            db.executeInsert(sqlstr);
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.frameworkset.platform.dao.BasicDAO#delete(com.frameworkset.platform.vo.ValueObject)
     */
    public int delete(ValueObject vo) //throws DataAccessException
    {
        // TODO Auto-generated method stub
        Holiday hol = (Holiday) vo;
        String yholiday = hol.getYHoliday();
        String mholiday = hol.getMHoliday();
        DBUtil db = new DBUtil();
        String sqlstr = "delete from td_sp_holiday where yholiday='"+yholiday+"' and mholiday='"+mholiday+"'";
        try
        {
            db.executeDelete(sqlstr);
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.frameworkset.platform.dao.BasicDAO#createValueObject()
     */
    public ValueObject createValueObject() //throws DataAccessException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * 批量插入记录, 采用批处理的方式
     * @param values List<ValueObject>
     * @return boolean
     * @throws DataAccessException 
     * HolidayDao.java
     * @author: ge.tao
     */
    public boolean  insertValues(List values) //throws DataAccessException
    {
    	if(values == null) return false;    	
        DBUtil db = new DBUtil();
        try{
	    	for(int i=0; i<values.size(); i++){
	    		Holiday hol = (Holiday) values.get(i);
	            String holiday = hol.getHoliday();
	            String yholiday = hol.getYHoliday();
	            String mholiday = hol.getMHoliday();
	            //插入去重
	            mholiday = mholiday.trim().length()==1?"0"+mholiday:mholiday;
	            holiday = holiday.trim().length()==1?"0"+holiday:holiday;
	            holiday = yholiday + "-" + mholiday + "-" + holiday;
	    		String sqlstr = "insert into td_sp_holiday(holiday,yholiday,mholiday) " +
	    			"select '" + holiday + "' as holiday, '" + yholiday + "' as yholiday, '" + mholiday + "' "+
	    			"as mholiday from dual where not exists (select * from " +
	    			"td_sp_holiday where holiday='" + holiday + "' and  yholiday='" + yholiday +"' " + 
	    			"and mholiday='" + mholiday + "') ";
	    		
	    		db.addBatch(sqlstr);
	    	}
	    	db.executeBatch();
        }catch(Exception e){        	
        	e.printStackTrace();
        	return false; 
        }
    	return true;
    }
    
    /**
     * 对于已经初始化的年, 记录到TD_SM_INITYEAR_HOLIDAY表中
     * @param year 初始化的年
     * @return boolean
     * HolidayDao.java
     * @author: ge.tao
     */
    public boolean recordInitYear(String year){
    	if(year == null || "".equalsIgnoreCase(year) || year.trim().length()==0){
    		return false;
    	}
    	String sqlstr = "insert into TD_SM_INITYEAR_HOLIDAY(YHOLIDAY)values(?)";
    	PreparedDBUtil pd = new PreparedDBUtil();
    	try {
			pd.preparedInsert(sqlstr);
			pd.setString(1,year);
			pd.executePrepared();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	return true;
    }
    
    /**
     * 判断当前年份的节假日是否被初始化过
     * @param year
     * @return boolean true:被初始化过, 不允许再次初始化 false: 没有初始化过
     * HolidayDao.java
     * @author: ge.tao
     */
    public boolean hasInitYear(String year){
    	if(year == null || "".equalsIgnoreCase(year) || year.trim().length()==0){
    		return false;
    	}
    	String sqlstr = "select count(*) as num from TD_SM_INITYEAR_HOLIDAY where YHOLIDAY='" + year + "'";
    	DBUtil db = new DBUtil();
    	try {
			db.executeSelect(sqlstr);
			if(db.size()>0){
				return db.getInt(0,"num")>0;
			}else{
				return false;
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
    
    /**
     * 记录已经初始化节假日的年份.
     * @param year
     * @return boolean
     * HolidayManager.java
     * @author: ge.tao
     */
    public boolean insertYearHoliday(String year_){
    	int year = 2008 ;
    	try {
			year = Integer.parseInt(year_);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block			
		}  
		//初始化保存年的节假日
		List holidays = new ArrayList();
    	for(int i=0 ;i <12 ;i ++){	
    		//i+1 Date(y,m,0) m = i+1 ;
			int length = new Date(year, i+1, 0).getDate(); 
			for(int j=1; j<=length; j++){
				Date day = new Date(year-1900, i, j);
				if(day.getDay()==0 || day.getDay()==6){ //是周末
					//插入td_sp_holiday表					
					Holiday holiday = new Holiday();
					holiday.setHoliday(String.valueOf(day.getDate()));
					holiday.setMHoliday(String.valueOf(day.getMonth()+1));
					holiday.setYHoliday(String.valueOf(day.getYear()+1900));
					holidays.add(holiday);
				}
			}				
		}
    	try {
			insertValues(holidays);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//对于已经初始化的年, 记录到TD_SM_INITYEAR_HOLIDAY表中
		this.recordInitYear(year_);
    	return true;
    }
    

}
