/*
 * Created on 2006-3-15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * @author ok
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BasicAction extends DispatchAction implements Serializable{
      protected static ActionForward getActionForward(ActionMapping mapping,String forward,String params)
      {
            ActionForward aforward = mapping.findForward(forward);
            StringBuffer path = new StringBuffer(aforward.getPath());
            boolean isQuery = (path.indexOf("?") >= 0);
            
            if (isQuery) {
                path.append("&" + params);
            } else {
                path.append("?" + params);
            }

            return new ActionForward(path.toString());  
      }
}
