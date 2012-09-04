/*
 * Created on 2005-3-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.holiday;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Holiday extends ValueObject implements java.io.Serializable{
   private String holiday;
   private String yholiday;
   private String mholiday;
   
   public void setHoliday(String holiday){
   	  this.holiday = holiday;
   }
   
   public String getHoliday(){
   	  return holiday;
   }
   
   public void setYHoliday(String yholiday){
   	  this.yholiday = yholiday;
   }
   
   public String getYHoliday(){
   	  return yholiday;
   }
   
   public void setMHoliday(String mholiday){
   	  this.mholiday = mholiday;
   }
   
   public String getMHoliday(){
   	  return mholiday;
   }
   
}
