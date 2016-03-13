package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.ecs.Form;
import com.frameworkset.common.ecs.IMG;
import com.frameworkset.common.ecs.Input;
import com.frameworkset.common.ecs.Option;
import com.frameworkset.common.ecs.Select;
import com.frameworkset.common.ecs.TD;
import com.frameworkset.common.ecs.TR;
import com.frameworkset.common.ecs.Table;
import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.driver.context.CMSContext;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.context.PageContext;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.util.CMSUtil;

/**
 * 
 * <p>Title: SearchTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-7-14 12:07:50
 * @author huiqiong.zeng
 * @version 1.0
 */
public class SearchTag extends CMSBaseTag {
	
	String textStyle = "";
	String searchType = "2";
	String textSize = "12";
	String buttonStyle = "";
	String buttonValue = "";
	String img = "";
	String imgHeigth = "";
	String imgWidth = "";
	String imgStyle = "";
	
	String hasFiledSelect = "0";
	String selectSize;
	String field = "查询所有字段,按内容查询,按标题查询,按描述查询,按关键字查询";				//内容,标题,描述,关键字(必须包含这些字样)
	
	String hasAdvancedSearch = "0";
	String advancedButtonValue = "高级";
	String advancedImg = "";
	
	String brFlag = "0";			//换行标志,将“搜索”和“高级”按钮换到另一行显示
	
	String tableBgcolor = "#FFFFFF";		//表格背景颜色
	String tableBorder = "0";				//表格线宽
	private String target = null;
	
	String indexName;		//索引名
							//缺陷381:全文检索标签增加索引名属性设置，直接指定索引库名称作为标签对应的库的标识?
	
	public String getIndexName() {
		return indexName;
	}
	
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	
	public String getHasFiledSelect() {
		return hasFiledSelect;
	}



	public void setHasFiledSelect(String hasFiledSelect) {
		this.hasFiledSelect = hasFiledSelect;
	}



	public String getField() {
		return field;
	}



	public void setField(String field) {
		this.field = field;
	}



	public String getButtonValue() {
		return buttonValue;
	}



	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}



	public String getImg() {
		return img;
	}



	public void setImg(String img) {
		this.img = img;
	}



	public String getImgHeigth() {
		return imgHeigth;
	}



	public void setImgHeigth(String imgHeigth) {
		this.imgHeigth = imgHeigth;
	}



	public String getImgWidth() {
		return imgWidth;
	}



	public void setImgWidth(String imgWidth) {
		this.imgWidth = imgWidth; 
	}
  


	public int doStartTag() throws JspException
	{	
		super.doStartTag();
		String path = request.getContextPath();
		try{
			//获取站点id
			String siteId = context.getSiteID();
			String chnlId = "";
			
			System.out.println("indexName:"+indexName+"-------指定的索引名称");
			//通过索引名称来检索
			
			if(this.searchType.equals("0") )
			{
				if(channel == null || channel.equals(""))
				{
					if(context instanceof ChannelContext)
					{
						ChannelContext channelctx = (ChannelContext)context;
						this.channel = channelctx.getChannel().getDisplayName();
						chnlId = channelctx.getChannelID() ;
					}
					else if(context instanceof ContentContext){
						chnlId = ((ContentContext)context).getChannelid();
						this.channel = ((ContentContext)context).getChannelid();
					}
					else if(context instanceof PageContext || 
							context instanceof CMSContext || 
							context instanceof DefaultContextImpl)
					{						
						throw new JspException("本检索入口被设置为频道检索入口，但是没有指定频道名称。");						
					}
				}
				else
				{
//					String[] chnlNames = channel.split(",");
//					if(chnlNames.length>1)
//					{
//						if( !(chnlNames[0].equals("")) && !(chnlNames[0]==null) )
//						{
//							chnlId = CMSUtil.getChannelCacheManager(siteId).getChannelByDisplayName(chnlNames[0]).getChannelId() + "";
//						}
//					}
//					for(int r=1; r<chnlNames.length; r++)
//					{
//						chnlId = chnlId + "," + CMSUtil.getChannelCacheManager(siteId).getChannelByDisplayName(chnlNames[r]).getChannelId();
//					}
					chnlId = CMSUtil.getChannelCacheManager(siteId).getChannelByDisplayName(channel).getChannelId() + "";
					//多频道修改,weida
				}
			}
			
			
			Form myForm = new Form();
			myForm.setName("searchForm");
			myForm.setMethod("post");
			//myForm.setAction(path + "/cms/searchManage/search_process.jsp?siteId="+siteId+"&chnlId="+chnlId+"&searchType="+searchType+"&tagFlag=1"+"&indexName="+indexName);        //entry为标签页面入口标志
			myForm.setAction(path + "/cms/searchManage/search_process.jsp?siteId="+siteId+"&searchType="+searchType+"&tagFlag=1"+"&chnlId="+chnlId+"&indexName="+indexName);        //entry为标签页面入口标志
			//增加索引名称参数,da.wei
			
			if(this.getTarget() != null && !this.getTarget().equals(""))
				myForm.setTarget(this.getTarget());
			
			Table table = new Table();
			table.setBorder(tableBorder);
			table.setBgColor(tableBgcolor);
			TR  tr = new TR();
			TR  tr2 = new TR();
			
			Input searchStr = new Input();
			searchStr.setType("text");
			searchStr.setStyle(textStyle);
			searchStr.setSize(textSize);
			searchStr.setName("queryString");
			TD tdSearchStr = new TD();
			tdSearchStr.setAlign("center");
			tdSearchStr.addElement(searchStr);
			tr.addElement(tdSearchStr);
			
			
			if("1".equals(hasFiledSelect))
			{
				Select fieldSelect = new Select();
				fieldSelect.setName("field");
				if(selectSize != null && selectSize.length()>0)
					fieldSelect.setSize(selectSize);
				if(field != null && field.length() > 0)
				{
					String[] fields = field.split(",");
					for(int i=0;i<fields.length;i++)
					{
						Option op = new Option();
						op.setTagText(fields[i]);
						if(fields[i].indexOf("所有") != -1)
							op.setValue("all");
						else if(fields[i].indexOf("内容") != -1)
							op.setValue("content");
						else if(fields[i].indexOf("标题") != -1)
							op.setValue("title");
						else if(fields[i].indexOf("描述") != -1)
							op.setValue("description");
						else if(fields[i].indexOf("关键字") != -1)
							op.setValue("keyword");
						fieldSelect.addElement(op);
					}
				}
				TD tdFieldSelect = new TD();
				tdFieldSelect.setAlign("center");
				tdFieldSelect.addElement(fieldSelect);
				tr.addElement(tdFieldSelect);
			}
			
			if(img == null || img.equals(""))
			{
				Input searchButton = new Input();
				searchButton.setType("button");
				
				
				if(buttonValue == null || buttonValue.equals(""))
				{
					if("0".equals(searchType))
						buttonValue = "频道内搜索";
					else if("1".equals(searchType))
						buttonValue = "站外搜索";
					else if("2".equals(searchType)) 
						buttonValue = "站内搜索";
					else if("3".equals(searchType))
						buttonValue = "站群搜索";
					else if("4".equals(searchType))
						buttonValue = "库表搜索";
				}
//				searchButton.setStyle(buttonStyle);
//				searchButton.setStyle("cursor:hand")	;
				if(buttonStyle != null )
					searchButton.setStyle(buttonStyle);
				else
					searchButton.setStyle("cursor:hand");
				searchButton.setValue(buttonValue);
				searchButton.setName("searchButton");
				searchButton.setOnClick("searchForm.submit();");
				TD tdSearchButton = new TD();
				tdSearchButton.setAlign("center");
				tdSearchButton.addElement(searchButton);
				if(brFlag.equals("0"))
					tr.addElement(tdSearchButton);
				else
					tr2.addElement(tdSearchButton);
			}
			else
			{
				IMG image = new IMG();
				image.setStyle("cursor:hand");
				String img_ = CMSTagUtil.getPublishedLinkPath(context,img);
				image.setSrc(img_);
				image.setOnClick("searchForm.submit();");
				if(!(imgHeigth == null || imgHeigth.equals("")))
				{
					image.setHeight(imgHeigth);
				}
				
				if(!(imgWidth == null || imgWidth.equals("")))
				{
					image.setWidth(imgWidth);
				}
				image.setStyle(imgStyle);
				TD tdImage = new TD();
				tdImage.setAlign("center");
				tdImage.addElement(image);
				if(brFlag.equals("0"))
					tr.addElement(tdImage);
				else
					tr2.addElement(tdImage);
			}
			
			if("1".equals(hasAdvancedSearch))
			{
				if(advancedImg == null || advancedImg.equals(""))
				{
					Input advancedSearchButton = new Input();
					advancedSearchButton.setType("button");
					if(buttonStyle != null )
						advancedSearchButton.setStyle(buttonStyle);
					else
						advancedSearchButton.setStyle("cursor:hand");
					advancedSearchButton.setValue(advancedButtonValue);
					advancedSearchButton.setName("advancedSearchButton");
					advancedSearchButton.setOnClick("var trueAction=searchForm.action;searchForm.action='" + path + "/cms/searchManage/advanced_search.jsp?siteId=" + siteId + "';searchForm.submit();searchForm.action=trueAction;");
					TD tdAdvancedSearchButton = new TD();
					tdAdvancedSearchButton.setAlign("center");
					tdAdvancedSearchButton.addElement(advancedSearchButton);
					if(brFlag.equals("0"))
						tr.addElement(tdAdvancedSearchButton);
					else
						tr2.addElement(tdAdvancedSearchButton);
				}
				else
				{
					IMG advancedImage = new IMG();
					String img_ = CMSTagUtil.getPublishedLinkPath(context,advancedImg);
					advancedImage.setSrc(img_);
					advancedImage.setOnClick("var trueAction=searchForm.action;searchForm.action='" + path + "/cms/searchManage/advanced_search.jsp';searchForm.submit();searchForm.action=trueAction;");
					if(!(imgHeigth == null || imgHeigth .equals("")))
					{
						advancedImage.setHeight(imgHeigth );
					}
					
					if(!(imgWidth == null || imgWidth.equals("")))
					{
						advancedImage.setWidth(imgWidth);
					}
//					advancedImage.setStyle(imgStyle);
//					advancedImage.setStyle("cursor:hand");
					if(buttonStyle != null )
						advancedImage.setStyle(buttonStyle);
					else
						advancedImage.setStyle("cursor:hand");
					TD tdAdvancedImage = new TD();
					tdAdvancedImage.setAlign("center");
					tdAdvancedImage.addElement(advancedImage); 
					if(brFlag.equals("0"))
						tr.addElement(tdAdvancedImage);
					else
						tr2.addElement(tdAdvancedImage);
				}
			}
			
			table.addElement(tr);
			if(brFlag.equals("1"))
				table.addElement(tr2);
			myForm.addElement(table);
			out.print(myForm.toString()); 
			//System.out.println(myForm.toString());
			//StringBuffer submitJSFunction = new StringBuffer();
			//submitJSFunction.append("<script language='javascript'>function submitLocalSearch(){" +
			//		"localSearchForm.submit();" +
			//		"}</script>");
		} catch (Exception e) {  
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	

	public String getButtonStyle() {
		return buttonStyle;
	}



	public void setButtonStyle(String buttonStyle) {
		this.buttonStyle = buttonStyle;
	}



	public String getTextSize() {
		return textSize;
	}



	public void setTextSize(String textSize) {
		this.textSize = textSize;
	}



	public String getTextStyle() {
		return textStyle;
	}



	public void setTextStyle(String textStyle) {
		this.textStyle = textStyle;
	}



	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}



	public String getImgStyle() {
		return imgStyle;
	}



	public void setImgStyle(String imgStyle) {
		this.imgStyle = imgStyle;
	}



	public String getSelectSize() {
		return selectSize;
	}



	public void setSelectSize(String selectSize) {
		this.selectSize = selectSize;
	}



	public String getAdvancedButtonValue() {
		return advancedButtonValue;
	}



	public void setAdvancedButtonValue(String advancedButtonValue) {
		this.advancedButtonValue = advancedButtonValue;
	}



	public String getAdvancedImg() {
		return advancedImg;
	}



	public void setAdvancedImg(String advancedImg) {
		this.advancedImg = advancedImg;
	}



	public String getHasAdvancedSearch() {
		return hasAdvancedSearch;
	}



	public void setHasAdvancedSearch(String hasAdvancedSearch) {
		this.hasAdvancedSearch = hasAdvancedSearch;
	}



	public String getBrFlag()
	{
		return brFlag;
	}



	public void setBrFlag(String brFlag)
	{
		this.brFlag = brFlag; 
	}



	public String getTableBgcolor()
	{
		return tableBgcolor;
	}



	public void setTableBgcolor(String tableBgcolor)
	{
		this.tableBgcolor = tableBgcolor;
	}



	public String getTableBorder()
	{
		return tableBorder;
	}



	public void setTableBorder(String tableBorder)
	{
		this.tableBorder = tableBorder;
	}



	public String getTarget() {
		return target;
	}



	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public void doFinally() {
		textStyle = "";
		  searchType = "2";
		  textSize = "12";
		  buttonStyle = "";
		  buttonValue = "";
		  img = "";
		  imgHeigth = "";
		  imgWidth = "";
		  imgStyle = "";
		
		  hasFiledSelect = "0";
		 selectSize = null;
		  field = "查询所有字段,按内容查询,按标题查询,按描述查询,按关键字查询";				//内容,标题,描述,关键字(必须包含这些字样)
		
		  hasAdvancedSearch = "0";
		  advancedButtonValue = "高级";
		  advancedImg = "";
		
		  brFlag = "0";			//换行标志,将“搜索”和“高级”按钮换到另一行显示
		
		  tableBgcolor = "#FFFFFF";		//表格背景颜色
		  tableBorder = "0";				//表格线宽
		    target = null;
		
		  indexName = null;
		super.doFinally();
	}

}
