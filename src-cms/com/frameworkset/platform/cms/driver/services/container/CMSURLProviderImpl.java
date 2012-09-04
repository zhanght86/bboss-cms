/*
 * Copyright 2003,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.frameworkset.platform.cms.driver.services.container;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.frameworkset.platform.cms.driver.config.DocumentStatus;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestContext;
import com.frameworkset.platform.cms.driver.jsp.JspletWindow;
import com.frameworkset.platform.cms.driver.publish.PublishMode;
import com.frameworkset.platform.cms.driver.url.CMSURL;
import com.frameworkset.platform.cms.driver.url.CMSURLParameter;

/**
 * 
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 * @author <a href="mailto:ddewolf@apache.org">David H. DeWolf</a>
 */
public class CMSURLProviderImpl implements CMSURLProvider {

    private CMSURL url;
    private String window;

    public CMSURLProviderImpl(HttpServletRequest request,
                                  JspletWindow internalPortletWindow) {
        CMSRequestContext ctx = (CMSRequestContext)
            request.getAttribute(CMSRequestContext.REQUEST_KEY);
        url = ctx.createCMSURL();

        this.window = internalPortletWindow.getJspletWindowID().getStringId();
    }

    public void setPortletMode(PublishMode mode) {
        url.setPublishMode(window, mode);
    }

    public void setWindowState(DocumentStatus state) {
        url.setDocumentStatus(window, state);
    }

    public void setAction(boolean action) {
        if (action) {
            url.setActionWindow(window);
        } else {
            url.setActionWindow(null);
        }
    }

    public void setSecure() {
        //url.setSecure(true);
    }

    public void clearParameters() {
        url.clearParameters(window);
    }

    public void setParameters(Map parameters) {
        Iterator it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            CMSURLParameter param = new CMSURLParameter(
            		window,
            		(String) entry.getKey(),
            		(String[]) entry.getValue());
            url.addParameter(param);
        }
    }

    public String toString() {
        return url.toString();
    }

}
