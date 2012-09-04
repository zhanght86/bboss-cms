package com.frameworkset.common.tag.html;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import org.apache.ecs.html.Meta;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.util.ValueObjectUtil;

/**
 * 设置文档meta元标记，包括：
 * title,keywords,author,copyright,description,doc-type
 * <p>Title: MetasTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-9-10 9:55:31
 * @author biaoping.yin
 * @version 1.0
 */

public class MetasTag extends CMSBaseTag {  
	
	/*
	 * site			站点名字
	 * channel		频道名字
	 * dockind		文档类型
	 * title		标题
	 * subtitle		显示标题
	 * secondtitle	副标题
	 * author		作者
	 * keywords		关键词
	 * abstracts	摘要内容
	 * copyright	版权
	 * docwtime		编稿时间	
	 * */
	
	private String metas;
	private String contents;
	
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getMetas() {
		return metas;
	}

	public void setMetas(String metas) {
		this.metas = metas;
	}

	public int doStartTag() throws JspException
	{
		
		super.doStartTag();		
		
		if(context instanceof ContentContext)
		{
			ContentContext contentctx = (ContentContext)context;
			Document doc = contentctx.getDocument();

			if(metas==null || metas.equals(null))
				metas = "";
			if(contents==null || contents.equals(null))
				contents = "";
			
			System.out.println("hello,metas: " + metas);
			System.out.println("hello,contents: " + contents);
			
			if(metas.equals("") && contents.equals("")){
				addMeta("site", doc.getSiteName());
				addMeta("channel", doc.getSiteName());
				addMeta("dockind", doc.getDockind());
				addMeta("title", doc.getTitle());
				addMeta("subtitle", doc.getSubtitle());
				addMeta("secondtitle", doc.getSecondtitle());
				addMeta("author", doc.getAuthor());
				addMeta("keywords", doc.getKeywords());
				addMeta("abstracts", doc.getDocabstract());
				addMeta("copyright", String.valueOf(doc.getVersion()));
				addMeta("docwtime", doc.getDocwtime().toString());
			}
			if(!metas.equals("") && contents.equals("")){
				String[] name = metas.split("\\+");
				for(int i=0;i<name.length;i++){
					String thisContent = ValueObjectUtil.getValue(doc,name[0]).toString();
					addMeta(name[0], thisContent);
				}
			}
			if(!metas.equals("") && !contents.equals("")){
				String[] name = metas.split("\\+");
				String[] content = contents.split("\\+");
				for(int i=0;i<name.length;i++){
					String thisContent = content[i].equals("") ? (ValueObjectUtil.getValue(doc,name[0]).toString()) : content[i];
					addMeta(name[0], thisContent);
				}
			}			
		}
		
		return EVAL_BODY_INCLUDE;
	}
	
	private void addMeta(String name, String content){
		if((name != null) && (content != null)){
			Meta meta = new Meta();
			meta.setName(name);
			meta.setContent(content);
			try {
				out.print(meta);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int doEndTag() throws JspException{		
		return super.doEndTag();
	}
	
}
