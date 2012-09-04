//Source file: D:\\WorkSpace\\console\\src\\com\\frameworkset\\platform\\schedularmanage\\RatifyAdvice.java

package com.frameworkset.platform.schedularmanage;

import java.io.Serializable;


/**
批准建议
 */
public class RatifyAdvice implements Serializable
{
   
   /**
   批准建议
    */
   private String advice;
   
   /**
   @roseuid 449B4527036B
    */
   private int ratifierID;
   
   private int schedularID;
   
   public RatifyAdvice() 
   {
    
   }

public String getAdvice() {
	return advice;
}

public void setAdvice(String advice) {
	this.advice = advice;
}

public int getRatifierID() {
	return ratifierID;
}

public void setRatifierID(int ratifierID) {
	this.ratifierID = ratifierID;
}

public int getSchedularID() {
	return schedularID;
}

public void setSchedularID(int schedularID) {
	this.schedularID = schedularID;
}
   
   /**
   Access method for the advice property.
   
   @return   the current value of the advice property
    */
  

}
