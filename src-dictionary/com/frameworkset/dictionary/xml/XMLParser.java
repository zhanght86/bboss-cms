/*
 * Title: The ERP System of kelamayi Downhole Company [PMIP]
 *
 * Copyright: Copyright (c) 2000-2004 westerasoft Co., Ltd All right reserved.
 *
 * Company: westerasoft Co., Ltd
 *
 * All right reserved.
 *
 * Created on 2004-7-29
 *
 * JDK version used		:1.4.1
 *
 * Modification history:
 *
 *
 */
package com.frameworkset.dictionary.xml;

/**
 * @author biaoping.yin
 * 2004-7-29
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 封装对应节点信息包括结点名称,属性名称,属性值
 */
final class Attribute {
	String nodeName;

	String name;

	String value;
}

public abstract class XMLParser implements Serializable {
	public abstract java.util.Map load();

	protected Document document = null;

	EntityResolver resolver;

	boolean validateDTD = true;

	public XMLParser() {

	}

	public void init(String dataPath) throws XMLParserException {
		String fileName = dataPath;
		InputSource source = null;
		try {
			source = new InputSource(new FileInputStream(new File(dataPath)));
		} catch (FileNotFoundException e) {
			throw new XMLParserException(
				"文件不存在:[" + dataPath + "]," + e.getMessage());
		}
		PrivilegedExceptionAction action = new PrivilegedExceptionAction() {

			public Object run() throws FactoryConfigurationError {
				return DocumentBuilderFactory.newInstance();
			}

		};
		DocumentBuilderFactory docBuilderFactory = null;
		try {
			docBuilderFactory =
				(DocumentBuilderFactory) AccessController.doPrivileged(action);
		} catch (PrivilegedActionException e) {
			throw new XMLParserException(e.getMessage());
		}

		docBuilderFactory.setValidating(validateDTD);
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			if (resolver != null)
				docBuilder.setEntityResolver(resolver);
			document = docBuilder.parse(source);
		} catch (ParserConfigurationException e1) {

			e1.printStackTrace();
			throw new XMLParserException(
				"ParserConfigurationException:["
					+ dataPath
					+ "],"
					+ e1.getMessage());
		} catch (SAXException e2) {

			throw new XMLParserException(
				"ParserConfigurationException:["
					+ dataPath
					+ "],"
					+ e2.getMessage());
		} catch (IOException e2) {

			throw new XMLParserException(
				"ParserConfigurationException:["
					+ dataPath
					+ "],"
					+ e2.getMessage());
		}
	}

	public void init(InputStream ios) throws XMLParserException {
		InputSource source = null;
		source = new InputSource(ios);
		PrivilegedExceptionAction action = new PrivilegedExceptionAction() {

			public Object run() throws FactoryConfigurationError {
				return DocumentBuilderFactory.newInstance();
			}

		};
		DocumentBuilderFactory docBuilderFactory = null;
		try {
			docBuilderFactory =
				(DocumentBuilderFactory) AccessController.doPrivileged(action);
		} catch (PrivilegedActionException e) {
			throw new XMLParserException(e.getMessage());
		}

		docBuilderFactory.setValidating(validateDTD);
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			if (resolver != null)
				docBuilder.setEntityResolver(resolver);
			document = docBuilder.parse(source);
		} catch (ParserConfigurationException e1) {

			e1.printStackTrace();
			throw new XMLParserException(
				"ParserConfigurationException:["

					+ "],"
					+ e1.getMessage());
		} catch (SAXException e2) {

			throw new XMLParserException(
				"ParserConfigurationException:["

					+ "],"
					+ e2.getMessage());
		} catch (IOException e2) {

			throw new XMLParserException(
				"ParserConfigurationException:["

					+ "],"
					+ e2.getMessage());
		}
	}

	public NodeList foundNodeByTagName(Node doc, String tagName)
		throws XMLParserException {
		if (doc == null || tagName == null)
			throw new XMLParserException(
				"foundNodeByTagName(Node doc,String tagName) parameter exception:[Node="
					+ doc
					+ "] or [tagName="
					+ tagName
					+ "]!");
		if (doc instanceof Document)
			return ((Document) doc).getElementsByTagName(tagName);
		if (doc instanceof Element)
			return ((Element) doc).getElementsByTagName(tagName);
		throw new XMLParserException(
			"foundNodeByTagName(Node doc,String tagName) parameter exception:[Node="
				+ doc
				+ "] the node type must be Document or Element");
	}

	public String getAttribute(Element doc, String attr)
		throws XMLParserException {
		if (doc == null || attr == null)
			throw new XMLParserException(
				"getAttribute(Node doc,String attr) parameter exception:[Node="
					+ doc
					+ "] or [attr="
					+ attr
					+ "]!");
		return doc.getAttribute(attr);
		//	    NamedNodeMap attrs = doc.getAttributes();
		//	    Attr at = (Attr)attrs.getNamedItem(attr);
		//	    if(at == null)
		//	        //throw new XMLParserException("attribute [" + attr + "] not found on
		// ["+ doc.getNodeName()+"]!");
		//	        return null;
		//	    return at.getValue();

	}

	public Node foundNode(Node doc, Attribute attr) throws XMLParserException {
		if (doc == null || attr == null)
			throw new XMLParserException(
				"foundNode(Node doc,Attribute attr) parameter exception:[document="
					+ doc
					+ "] or [attribute="
					+ attr
					+ "]!");
		if (attr.nodeName == null || attr.name == null || attr.value == null)
			throw new XMLParserException(
				"foundNode(Node doc,Attribute attr) parameter exception:[attr.nodeName="
					+ attr.nodeName
					+ ",attr.name="
					+ attr.name
					+ ",attr.value="
					+ attr.value
					+ "]");
		if (doc.getNodeType() == Node.DOCUMENT_NODE) {
			NodeList subNodes = doc.getChildNodes();
			for (int i = 0;
				subNodes != null && i < subNodes.getLength();
				i++) {
				Node temp = foundNode(subNodes.item(i), attr);
				if (temp != null) {
					return temp;
				}
			}
		}
		if (doc.getNodeType() == Node.ELEMENT_NODE) {
			if (doc.getNodeName().equals(attr.nodeName)) {
				String value = getAttribute((Element) doc, attr.name);
				if (value != null && value.equals(attr.value))
					return doc;
			}
		}
		return null;
	}

	/**
	 * 在doc及其子节点中查找包含attr的节点,如果找到则
	 */
	public Node foundNodeByAttr(Node doc, Attribute attr)
		throws XMLParserException {
		if (doc == null || attr == null)
			throw new XMLParserException(
				"foundNodeByAttr(Node doc,Attribute attr) parameter exception:[document="
					+ doc
					+ "] or [attribute="
					+ attr
					+ "]!");
		if (attr.name == null || attr.value == null)
			throw new XMLParserException(
				"foundNode(Node doc,Attribute attr) parameter exception:[attr.name="
					+ attr.name
					+ ",attr.value="
					+ attr.value
					+ "]");
		if (doc.getNodeType() == Node.DOCUMENT_NODE) {
			NodeList subNodes = doc.getChildNodes();
			for (int i = 0;
				subNodes != null && i < subNodes.getLength();
				i++) {
				Node temp = foundNodeByAttr(subNodes.item(i), attr);
				if (temp != null) {
					return temp;
				}
			}
		}
		if (doc.getNodeType() == Node.ELEMENT_NODE) {
			String value = getAttribute((Element) doc, attr.name);
			if (value != null && value.equals(attr.value))
				return doc;
		}
		return null;
	}

	/**
	 * @return Returns the resolver.
	 */
	public EntityResolver getResolver() {
		return resolver;
	}

	/**
	 * @param resolver
	 *            The resolver to set.
	 */
	public void setResolver(EntityResolver resolver) {
		this.resolver = resolver;
	}
	/**
	 * @return Returns the validateDTD.
	 */
	public boolean isValidateDTD() {
		return validateDTD;
	}
	/**
	 * @param validateDTD The validateDTD to set.
	 */
	public void setValidateDTD(boolean validateDTD) {
		this.validateDTD = validateDTD;
	}
}
