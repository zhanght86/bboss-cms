package com.frameworkset.platform.cms.driver.dataloader;

import java.util.Map;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.ContentContext;

/**
 * 
 * <p>
 * Title: com.frameworkset.platform.cms.driver.dataloader.DefaultDetailDataLoader.java
 * </p>
 * 
 * <p>
 * Description: 缺省内容管理系统细览数据装载器
 * </p>
 * 
 * <p>
 * Copyright (c) 2007.10
 * </p>
 * 
 * <p>
 * Company: iSany
 * </p>
 * 
 * @Date 2007-1-31 18:14:06
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultDetailDataLoader extends CMSDetailDataLoader implements java.io.Serializable {

	/**
	 * 获取文档详细信息
	 */
	public Object doGetContent(ContentContext contentcontext)
			throws CMSDataLoadException {

		Document doc = null;
		TransactionManager tm = new TransactionManager(); 
		// if(doc == null)
		try {
//			doc = contentcontext.getDriverConfiguration().getCMSService()
//					.getDocumentManager().getDoc(contentcontext);
			tm.begin();
			doc = contentcontext.getDocument();
			if(doc.getDocExtField() != null)
			{
				tm.commit();
				return doc;
			}
//			/*
//			 * 处理文档内容中的链接和路径信息
//			 */
//			String content = doc.getContent();
//			CmsLinkProcessor processor = new CmsLinkProcessor(contentcontext,CmsLinkProcessor.REPLACE_LINKS,CmsEncoder.ENCODING_ISO_8859_1);
//			processor.setHandletype(CmsLinkProcessor.PROCESS_CONTENT);
//			try {
//				content = processor.process(content,CmsEncoder.ENCODING_ISO_8859_1);
//				doc.setContent(content);
//			} catch (ParserException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			/**
			 * 获取相关文档
			 */
			
			if(!contentcontext.isPreviewDocumentMode())
			{
//				List relationDocuments = contentcontext.getDriverConfiguration()
//													   .getCMSService()
//													   .getDocumentManager()
//													   .getRelatedDocsOfDoc(doc.getDocument_id() + "");
//				
//				doc.setRelationDocumemnts(relationDocuments);
				if (doc.getType() == 0)// 普通文档
				{
					//无需进行处理
				}
				else if (doc.getType() == 1) // 装载聚合文档
				{
//					List compositeDoc = contentcontext.getDriverConfiguration()
//							.getCMSService().getDocumentManager().getCompositeDocs(
//									contentcontext);
//					doc.setCompositeDocs(compositeDoc);
				}
	
				else if (doc.getType() == 2) // 装载分页文档
				{
//					List pagingDocs = contentcontext.getDriverConfiguration()
//							.getCMSService().getDocumentManager().getPagingDocs(
//									contentcontext);
//					doc.setPagingDocs(pagingDocs);
				}
			
			
			
				Map docExtField = contentcontext.getDriverConfiguration()
												   .getCMSService()
												   .getDocumentManager()
												   .getDocExtFieldMapBean(doc.getDocument_id() + "");
				doc.setDocExtField(docExtField);
			}
			doc.setPublishTime(contentcontext.getPublishTime());
			tm.commit();
//			System.out.println("doc.getPublishTime():" + doc.getPublishTime());

			// if(doc == null)
			// throw new CMSDataLoadException("装载数据出错:"
			// +contentcontext.toString() + "不存在.");
			// contents.put(contentkey,doc);
		} catch (DocumentManagerException e) {
			throw new CMSDataLoadException("装载数据出错:"
					+ contentcontext.toString() + "," + e .getMessage());
		} catch (DriverConfigurationException e) {
			throw new CMSDataLoadException("装载数据出错:"
					+ contentcontext.toString() + "," + e.getMessage());
		}
		 catch (Exception e) {
				throw new CMSDataLoadException("装载数据出错:"
						+ contentcontext.toString() + "," + e.getMessage());
		}
		finally
		{
			tm.release();
		}
			
		return doc;
	}

}
