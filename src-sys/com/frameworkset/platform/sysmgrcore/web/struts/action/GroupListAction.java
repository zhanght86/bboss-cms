//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.1/xslt/JavaClass.xsl

package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.web.struts.form.GroupListForm;

/**
 * MyEclipse Struts Creation date: 03-14-2006
 * 
 * XDoclet definition:
 * 
 * @struts:action scope="null" validate="true"
 */
public class GroupListAction extends Action implements Serializable{

	private static Logger logger = Logger.getLogger(GroupListAction.class
			.getName());

	/**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, GroupListForm form,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			GroupManager gm = SecurityDatabase.getGroupManager();
			GroupListForm gmf = (GroupListForm) form;

			List gl = gm.getGroupList();

			if (gl != null && !gl.isEmpty()) {
				for (int i = 0; i < gl.size(); i++) {
					Group g = (Group) gl.get(i);
					gmf.getGroupOfLabelAndValues()
							.add(
									new LabelValueBean(g.getGroupName(), g
											.getGroupId()+""));
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return mapping.findForward("groupListView");
	}

}