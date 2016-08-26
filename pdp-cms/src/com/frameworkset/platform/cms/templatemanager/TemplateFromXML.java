package com.frameworkset.platform.cms.templatemanager;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.frameworkset.platform.cms.container.Template;

public class TemplateFromXML implements java.io.Serializable{
	public Template parse(String xmlContent){
		if(xmlContent==null || xmlContent.trim().length()==0){
			return null;
		}
		try {
			javax.xml.parsers.SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser s = factory.newSAXParser();
			Hander hander = new Hander();
			s.parse(new ByteArrayInputStream(xmlContent.getBytes()),hander);
			return hander.getTemplate();
		} catch (SAXException e) {
			System.out.println("从xml文件解析成Template类时,发生解析错误!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("从xml文件解析成Template类时,发生IO异常!");
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			System.out.println("从xml文件解析成Template类时,解析器(parser)配置错误!");
			e.printStackTrace();
		}
		return null;
	}
	private class Hander extends DefaultHandler{
		public Template getTemplate(){
			return template;
		}
		private Template template = new Template();
		private String value = "";

		public void characters(char[] ch,
	            int start,
	            int length)
	            throws SAXException{
			value = new String(ch,start,length);
		}
		public void endElement(String uri, String localName, String qName)throws SAXException{
			setField(qName, value);
		}
		
		private void setField(String fieldname,String value){
			if("id".equals(fieldname)){
				if(value!=null){
					template.setTemplateId(Integer.parseInt(value));
				}
				return;
			}
			
			if("name".equals(fieldname)){
				template.setName(value);
				return;
			}
			
			if("description".equals(fieldname)){
				template.setDescription(value);
				return;
			}
			if("header".equals(fieldname)){
				template.setHeader(value);
				return;
			}
			if("text".equals(fieldname)){
				template.setText(value);
				return;
			}
			if("type".equals(fieldname)){
				if(value!=null){
					template.setType(Integer.parseInt(value));
				}
				return;
			}
			if("createuser".equals(fieldname)){
				if(value!=null){
					template.setCreateUserId(Long.parseLong(value));
				}
				return;
			}
			if("createtime".equals(fieldname)){
				if(value!=null){
					// TODO 考虑怎么从字符串变成时间
					//template.setCreateTime()
				}
				return;
			}
			if("inc_pub_flag".equals(fieldname)){
				if(value!=null){
					template.setIncreasePublishFlag(Integer.parseInt(value));
				}
				return;
			}
			if("presisttype".equals(fieldname)){
				if(value!=null){
					template.setPersistType(Integer.parseInt(value));
				}
				return;
			}
			if("templatefilename".equals(fieldname)){
				template.setTemplateFileName(value);
				return;
			}
			if("templatepath".equals(fieldname)){
				template.setTemplatePath(value);
				return;
			}
		}
	}
}
