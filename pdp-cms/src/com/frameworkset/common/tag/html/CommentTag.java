package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.ecs.A;
import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.docCommentManager.DocCommentManager;
import com.frameworkset.platform.cms.docCommentManager.DocCommentManagerImpl;
import com.frameworkset.platform.cms.driver.context.ContentContext;
/**
 * 评论查看标签
 * @author Administrator
 *
 */
public class CommentTag extends CMSBaseTag {

	private String style="";

	public int doStartTag() throws JspException
	{	
		super.doStartTag();
		String path=request.getContextPath();
		try{
			if(context instanceof ContentContext){
				DocCommentManager dcmi = new DocCommentManagerImpl();
				
				ContentContext contentContext = (ContentContext)context;
				String docId = contentContext.getContentid();
				//String docId = "33430";                //测试用
				
				String currentUrl = "";
				int actionType = contentContext.getActionType();
				if(actionType == 0){			//发布
					currentUrl = contentContext.getPublishedPageUrl();
				}else if(actionType == 1){		//预览
					currentUrl = contentContext.getPreviewPageUrl();
					currentUrl = path + "/" + currentUrl;
				}
				
				Channel chnl =  contentContext.getChannel();;
				long chnlId = chnl.getChannelId();
				long siteId = chnl.getSiteId();
				String tempPath = chnl.getCommentPagePath(); 
				if(tempPath.startsWith("/") || tempPath.startsWith("\\"))
					tempPath = tempPath.substring(1);
				if(tempPath.endsWith("/") || tempPath.endsWith("\\"))
					tempPath = tempPath.substring(0,tempPath.length()-1);
				
				String linkPth = path + "/cms/siteResource/" + contentContext.getSiteDir() + "/_template/" + tempPath + "?docId=" + docId + "&returnUrl=" + currentUrl;
				
				A a = new A();					//指向每个频道固定的评论页面
				a.setHref(linkPth);
				a.setTagText("查看评论 " + dcmi.getCommentPublishedCount(Integer.parseInt(docId)) +"条");  
				a.setStyle(style);
				if(dcmi.getDocCommentSwitch(chnlId+"","chnl") 
						+ dcmi.getDocCommentSwitch(docId,"doc")==0){		// 只有频道评论、文档评论同时开通才可以查看评论
					out.println(a);
				}
				
			}
		} catch (Exception e) {  
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	@Override
	public void doFinally() {
		style="";
		super.doFinally();
	}
}
