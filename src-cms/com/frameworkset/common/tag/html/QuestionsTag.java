package com.frameworkset.common.tag.html;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.ecs.Input;
import com.frameworkset.common.ecs.LI;
import com.frameworkset.common.tag.BaseCellTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.votemanager.Item;
import com.frameworkset.platform.cms.votemanager.Question;
import com.frameworkset.platform.cms.votemanager.VoteManager;
import com.frameworkset.platform.cms.votemanager.VoteManagerImpl;

/**
 *  
 * <p>Title: QuestionsTag.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007 3:25:14 PM
 * @author da.wei
 * @version 1.0
 */
public class QuestionsTag extends BaseCellTag {

	protected String optionstyle = "";
	
	protected String textareastyle = "";
	
	protected String channel = "";

	protected int count = -1;

	protected String id = "";

	public int doStartTag() throws JspException {
		super.doStartTag();

		StringBuffer str = new StringBuffer();
		try {

		
			
			if (super.dataSet != null && super.dataSet.size()>0) {//有dataSet，从dataSet得到id
				
				
				
				str.append("<script language='javascript'>questionids+=','+"+String.valueOf(dataSet.getInt("id"))+";</script>");
				str.append("<strong>"+dataSet.getString("title")+"</strong>");
				Input checkbox = new Input();
				/* gw_tanx 20150204 增加单选和多选的预览情况
				 * if (dataSet.getInt("style") == 1
						|| dataSet.getInt("style") == 0 ) {*/
				if (dataSet.getInt("style") == 1
						|| dataSet.getInt("style") == 0 || dataSet.getInt("style") == 3|| dataSet.getInt("style") == 4) {
					List options = (ArrayList) dataSet.getValue("items");
					LI li = new LI();
					for (int i = 0; i < options.size(); i++) {
						checkbox = new Input();
						Item item = (Item) options.get(i);
						
						/*
						 * gw_tanx 20150204 增加单选和多选的预览情况
						 * if (dataSet.getInt("style") == 1 )
							checkbox.setType("checkbox");
						if (dataSet.getInt("style") == 0 )
							checkbox.setType("radio");*/
						
						if (dataSet.getInt("style") == 1 || dataSet.getInt("style") == 4)
							checkbox.setType("checkbox");
						if (dataSet.getInt("style") == 0 || dataSet.getInt("style") == 3)
							checkbox.setType("radio");

						checkbox.setTagText(item.getOptions());
						checkbox.setValue(item.getId());
						checkbox.setID("chckbox"
								+ String.valueOf(item.getQid()));
						checkbox.setName("chckbox"
								+ String.valueOf(item.getQid()));

						li = new LI();
						li.setStyle(" " + optionstyle);
						li.setTagText(checkbox.toString());
						
						Input hidden = new Input();
						hidden.setType("hidden");
						hidden.setID("itemids");
						hidden.setName("itemids");
						hidden.setValue("chckbox"
								+ String.valueOf(item.getQid()));
						str.append(li.toString()+hidden.toString());
					}

				} else if (dataSet.getInt("style") == 5) {
					
				}else {
					Input hidden = new Input();
					hidden.setType("hidden");
					hidden.setID("textareaids");
					hidden.setName("textareaids");
					hidden.setValue("textarea"+ String.valueOf(dataSet.getInt("id")));
					
					str.append("<br><textarea id='textarea"
							+ String.valueOf(dataSet.getInt("id"))
							+ "' name='textarea"
							+ String.valueOf(dataSet.getInt("id"))
							+ "' cols='30' rows='5' style='"+textareastyle+"'></textarea>"+hidden.toString());
				}

				out.print(str.toString());
			} 
			else if (!"".equals(id)) 
			{//id不为空
				String[] ids = id.split(",");
				str.append("<script language='javascript'>questionids='"+id+"';</script>");
				str.append("<table width='100%'>");
				for (int t = 0; t < ids.length; t++) {
					str.append("<tr><td>");
					VoteManager voteMgr = new VoteManagerImpl();
					Question question = voteMgr.getQuestionBy(Integer.parseInt(ids[t]));
					str.append(String.valueOf(t+1)+". "+question.getTitle());

					Input checkbox = new Input();
					if (question.getStyle() == 1 || question.getStyle() == 0) {
						List options = question.getItems();
						LI li = new LI();
						for (int i = 0; i < options.size(); i++) {
							checkbox = new Input();
							Item item = (Item) options.get(i);
							if (question.getStyle() == 1)
								checkbox.setType("checkbox");
							if (question.getStyle() == 0)
								checkbox.setType("radio");

							checkbox.setTagText(item.getOptions());
							checkbox.setValue(item.getId());
							checkbox.setID("chckbox"
									+ String.valueOf(item.getQid()));
							checkbox.setName("chckbox"
									+ String.valueOf(item.getQid()));

							li = new LI();
							li.setStyle(" " + optionstyle);
							li.setTagText(checkbox.toString());

							Input hidden = new Input();
							hidden.setType("hidden");
							hidden.setID("itemids");
							hidden.setName("itemids");
							hidden.setValue("chckbox"
									+ String.valueOf(item.getQid()));
							str.append(li.toString()+hidden.toString());
						}

					} else {
						Input hidden = new Input();
						hidden.setType("hidden");
						hidden.setID("textareaids");
						hidden.setName("textareaids");
						hidden.setValue("textarea"+ String.valueOf(question.getId()));
						
						str.append("<br><textarea id='textarea"
								+ String.valueOf(question.getId())
								+ "' name='textarea"
								+ String.valueOf(question.getId())
								+ "' cols='30' rows='5' style='"+textareastyle+"'></textarea>"+hidden.toString());
					}
					str.append("<p></td></tr>");
				}
				str.append("</table>");
				out.print(str.toString());
			} 
			else if ((id==null||"".equals(id))&& channel!=null && !"".equals(channel)) {//id为空，但是有频道
				
				ChannelManagerImpl chnlMgr = new ChannelManagerImpl();
				Channel chnl = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannelByDisplayName(this.getChannel());
				VoteManager voteMgr = new VoteManagerImpl();
				List qlist = voteMgr.getActiveQstOf(String.valueOf(chnl.getChannelId()),count);
				
				str.append("<table width='100%'>");
				for (int t = 0; t < qlist.size(); t++) {
					str.append("<tr><td>");
					Question question = (Question)qlist.get(t);
					str.append("<strong>"+String.valueOf(t+1)+". "+question.getTitle()+"</strong>");
					str.append("<script language='javascript'>questionids+=',"+question.getId()+"';</script>");

					Input checkbox = new Input();
					if (question.getStyle() == 1 || question.getStyle() == 0) {
						List options = question.getItems();
						LI li = new LI();
						for (int i = 0; i < options.size(); i++) {
							checkbox = new Input();
							Item item = (Item) options.get(i);
							if (question.getStyle() == 1)
								checkbox.setType("checkbox");
							if (question.getStyle() == 0)
								checkbox.setType("radio");

							checkbox.setTagText(item.getOptions());
							checkbox.setValue(item.getId());
							checkbox.setID("chckbox"
									+ String.valueOf(item.getQid()));
							checkbox.setName("chckbox"
									+ String.valueOf(item.getQid()));

							li = new LI();
							li.setStyle(" " + optionstyle);
							li.setTagText(checkbox.toString());

							Input hidden = new Input();
							hidden.setType("hidden");
							hidden.setID("itemids");
							hidden.setName("itemids");
							hidden.setValue("chckbox"
									+ String.valueOf(item.getQid()));
							str.append(li.toString()+hidden.toString());
						}

					} else {
						Input hidden = new Input();
						hidden.setType("hidden");
						hidden.setID("textareaids");
						hidden.setName("textareaids");
						hidden.setValue("textarea"+ String.valueOf(question.getId()));
						
						str.append("<br><textarea id='textarea"
								+ String.valueOf(question.getId())
								+ "' name='textarea"
								+ String.valueOf(question.getId())
								+ "' cols='30' rows='5' style='"+textareastyle+"'></textarea>"+hidden.toString());
					}
					str.append("<p></td></tr>");
				}
				str.append("</table>");
				out.print(str.toString());
			}else if ((id==null||"".equals(id))&& CMSTagUtil.getCurrentChannel(context)!=null) {//没有id，没有频道，从上下文得到当前频道
				
				Channel chnl = CMSTagUtil.getCurrentChannel(context);
				VoteManager voteMgr = new VoteManagerImpl();
				List qlist = voteMgr.getActiveQstOf(String.valueOf(chnl.getChannelId()),count);
				
				str.append("<table width='100%'>");
				for (int t = 0; t < qlist.size(); t++) {
					str.append("<tr><td>");
					Question question = (Question)qlist.get(t);
					str.append("<strong>"+String.valueOf(t+1)+". "+question.getTitle()+"</strong>");
					str.append("<script language='javascript'>questionids+=',"+question.getId()+"';</script>");

					Input checkbox = new Input();
					if (question.getStyle() == 1 || question.getStyle() == 0) {
						List options = question.getItems();
						LI li = new LI();
						for (int i = 0; i < options.size(); i++) {
							checkbox = new Input();
							Item item = (Item) options.get(i);
							if (question.getStyle() == 1)
								checkbox.setType("checkbox");
							if (question.getStyle() == 0)
								checkbox.setType("radio");

							checkbox.setTagText(item.getOptions());
							checkbox.setValue(item.getId());
							checkbox.setID("chckbox"
									+ String.valueOf(item.getQid()));
							checkbox.setName("chckbox"
									+ String.valueOf(item.getQid()));

							li = new LI();
							li.setStyle(" " + optionstyle);
							li.setTagText(checkbox.toString());

							Input hidden = new Input();
							hidden.setType("hidden");
							hidden.setID("itemids");
							hidden.setName("itemids");
							hidden.setValue("chckbox"
									+ String.valueOf(item.getQid()));
							str.append(li.toString()+hidden.toString());
						}

					} else {
						Input hidden = new Input();
						hidden.setType("hidden");
						hidden.setID("textareaids");
						hidden.setName("textareaids");
						hidden.setValue("textarea"+ String.valueOf(question.getId()));
						
						str.append("<br><textarea id='textarea"
								+ String.valueOf(question.getId())
								+ "' name='textarea"
								+ String.valueOf(question.getId())
								+ "' cols='30' rows='5' style='"+textareastyle+"'></textarea>"+hidden.toString());
					}
					str.append("<p></td></tr>");
				}
				str.append("</table>");
				out.print(str.toString());
			}else  {//没有id和频道，不能从上下文得到频道，则使用置顶的问卷
				VoteManager voteMgr = new VoteManagerImpl();
				Question question = voteMgr.getQuestionBy(voteMgr.getTopQuestionID(Integer.parseInt(context.getSiteID())));
				
				str.append("<table width='100%'>");
				if (question!=null) {
					str.append("<tr><td>");
					str.append("<strong>"+question.getTitle()+"</strong>");
					str.append("<script language='javascript'>questionids+=',"+question.getId()+"';</script>");

					Input checkbox = new Input();
					if (question.getStyle() == 1 || question.getStyle() == 0) {
						List options = question.getItems();
						LI li = new LI();
						for (int i = 0; i < options.size(); i++) {
							checkbox = new Input();
							Item item = (Item) options.get(i);
							if (question.getStyle() == 1)
								checkbox.setType("checkbox");
							if (question.getStyle() == 0)
								checkbox.setType("radio");

							checkbox.setTagText(item.getOptions());
							checkbox.setValue(item.getId());
							checkbox.setID("chckbox"
									+ String.valueOf(item.getQid()));
							checkbox.setName("chckbox"
									+ String.valueOf(item.getQid()));

							li = new LI();
							li.setStyle(" " + optionstyle);
							li.setTagText(checkbox.toString());

							Input hidden = new Input();
							hidden.setType("hidden");
							hidden.setID("itemids");
							hidden.setName("itemids");
							hidden.setValue("chckbox"
									+ String.valueOf(item.getQid()));
							str.append(li.toString()+hidden.toString());
						}

					} else {
						Input hidden = new Input();
						hidden.setType("hidden");
						hidden.setID("textareaids");
						hidden.setName("textareaids");
						hidden.setValue("textarea"+ String.valueOf(question.getId()));
						
						str.append("<br><textarea id='textarea"
								+ String.valueOf(question.getId())
								+ "' name='textarea"
								+ String.valueOf(question.getId())
								+ "' cols='30' rows='5' style='"+textareastyle+"'></textarea>"+hidden.toString());
					}
					str.append("<p></td></tr>");
				}
				str.append("</table>");
				out.print(str.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return this.SKIP_BODY;

	}

	public String getOptionstyle() {
		return optionstyle;
	}

	public void setOptionstyle(String optionstyle) {
		this.optionstyle = optionstyle;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTextareastyle() {
		return textareastyle;
	}

	public void setTextareastyle(String textareastyle) {
		this.textareastyle = textareastyle;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public void doFinally() {
		optionstyle = "";
		
		  textareastyle = "";
		
		  channel = "";

		  count = -1;

		  id = "";
		super.doFinally();
	}

}

/**/
