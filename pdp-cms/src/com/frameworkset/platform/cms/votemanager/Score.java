/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.frameworkset.platform.cms.votemanager;

import java.text.MessageFormat;

/**
 * <p>Title: Score.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-9-2
 * @author biaoping.yin
 * @version 1.0
 */
public class Score {
	private double value;
	  private double minValue = 0.0D;
	  private double maxValue = 100.0D;
	  private int partialBlocks = 1;
	  private int fullBlocks = 5;
	  private boolean showEmptyBlocks = false;
	  private boolean showA = false;
	  private boolean showB = false;
	  

	  public String pic(String body) 
	  {
	  
	      double unitSize = (this.maxValue - this.minValue) / (this.fullBlocks * this.partialBlocks);

	      int fullBlockCount = (int)Math.floor(this.value / (unitSize * this.partialBlocks));
	      int partialBlockIndex = (int)Math.floor((this.value - fullBlockCount * unitSize * this.partialBlocks) / unitSize);

	      
	      StringBuffer ret = new StringBuffer();
	      //<img src="<c:url value="/css/classic/gifs/rb_{0}.gif"/>" alt="+"
          //        title="<spring:message code="probe.jsp.sysinfo.memory.usage.alt"/>"/>
	      if (this.showA) {
	    	  ret.append("<img src=\"")
	    	  .append(MessageFormat.format(body, new Object[] { (fullBlockCount > 0) || (partialBlockIndex > 0) ? "a" : "a0" }))
	    	  .append("\">");
	      }

	      String fullBody = MessageFormat.format(body, new Object[] { new Integer(this.partialBlocks) });
	      for (int i = 0; i < fullBlockCount; i++) {
	    	  ret.append("<img src=\"").append(fullBody)
	    	  .append("\">");
	      }

	      if (partialBlockIndex > 0) {
	        String partialBody = MessageFormat.format(body, new Object[] { new Integer(partialBlockIndex) });
	        ret.append("<img src=\"").append(partialBody).append("\">");
	      }

	      int emptyBlocks = this.showEmptyBlocks ? this.fullBlocks - fullBlockCount - (partialBlockIndex > 0 ? 1 : 0) : 0;

	      if (emptyBlocks > 0) {
	        String emptyBody = MessageFormat.format(body, new Object[] { new Integer(0) });
	        for (int i = 0; i < emptyBlocks; i++) {
	        	ret.append("<img src=\"").append(emptyBody) .append("\">");
	        }
	      }

	      if (this.showB) {
	    	  ret.append("<img src=\"").append(MessageFormat.format(body, new Object[] { fullBlockCount == this.fullBlocks ? "b" : "b0" })) .append("\">");
	      }

	      return ret.toString();
	   

	   
	  }

	  public double getValue() {
	    return this.value;
	  }

	  public void setValue(double value) {
	    this.value = value;
	  }

	  public double getMinValue() {
	    return this.minValue;
	  }

	  public void setMinValue(double minValue) {
	    this.minValue = minValue;
	  }

	  public double getMaxValue() {
	    return this.maxValue;
	  }

	  public void setMaxValue(double maxValue) {
	    this.maxValue = maxValue;
	  }

	  public int getPartialBlocks() {
	    return this.partialBlocks;
	  }

	  public void setPartialBlocks(int partialBlocks) {
	    this.partialBlocks = partialBlocks;
	  }

	  public int getFullBlocks() {
	    return this.fullBlocks;
	  }

	  public void setFullBlocks(int fullBlocks) {
	    this.fullBlocks = fullBlocks;
	  }

	  public boolean isShowEmptyBlocks() {
	    return this.showEmptyBlocks;
	  }

	  public void setShowEmptyBlocks(boolean showEmptyBlocks) {
	    this.showEmptyBlocks = showEmptyBlocks;
	  }

	  public boolean isShowA() {
	    return this.showA;
	  }

	  public void setShowA(boolean showA) {
	    this.showA = showA;
	  }

	  public boolean isShowB() {
	    return this.showB;
	  }

	  public void setShowB(boolean showB) {
	    this.showB = showB;
	  }

}
