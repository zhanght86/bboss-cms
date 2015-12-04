package com.frameworkset.platform.cms.templatemanager;


import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;

import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.util.ListInfo;
/**
 * 模板管理
 * @author yongjun.xiong
 * 日期:Dec 19, 2006
 * 版本:1.0
 * 版权所有:三一重工
 */

public interface TemplateManager extends java.io.Serializable 
{
  //-------增------ 
	 /**
     * 创建公共模板
     * 输入:模板对象template
     * 返回值int:成功时为模板id;失败时为:0
     * 功能:将模板基本信息写入数据库表(td_cms_template)中
     */
    public int createTemplate(Template template, int siteid) throws TemplateManagerException;
    /**
     * 创建站点下的模板
     * @since 2006.12
     * @param 输入:模板对象template,站点ID:siteid
     * 返回值int:成功时为模板id;失败时为:0
     * 功能:将模板基本信息写入数据库表(td_cms_template)中,与站点的对应关系写在td_cms_sitetemplatemap中
     */
    public int createTemplateofSite(Template template,int siteid) throws TemplateManagerException;


    /**
     * 导入模板
     * @param template 模板对象,包含站点id
     * @param sourcePackage 用zip格式打好了包的模板
     * @param overWrite 是否是重新导入
     * @throws TemplateManagerException
     */
    public void importTemplate(Template template,ZipInputStream sourcePackage,boolean reImport)throws TemplateManagerException;
//  -------删------ 
    /**
     * 功能:删除模板表中的记录
     * 输入:模板ID
     * 输出:ture:成功;false失败
     * @since 2006.12
     * @param template
     */ 
    public boolean deleteTemplate(int templateid) throws TemplateManagerException;
    
    /**
     * 功能:删除站点下的模板
     * 输入:模板ID,站点ID
     * 输出:true:成功;false:不成功         
     */ 
    public boolean deleteTemplateofSite(int templateid,int siteid) throws TemplateManagerException;
   

    /**
     * 功能:模板是否被站点、频道
     * 输入:模板ID
     * 输出:true:被引用;false:没有引用         
     */ 
    public boolean templateisUsed(int templateid) throws TemplateManagerException;
    
    /**
     * 功能:更新模板表信息
     * 输入:模板对象template
     * 输出:true:成功;false:失败    
     * @param template
     * @roseuid 45864967030D
     */
//  -------改------
    public boolean updateTemplate(Template template) throws TemplateManagerException;

    /**
     * 功能:更新模板的所有信息,如果模板存储在文件系统中,将模板的内容写入文件
     * 输入:模板对象template
     * 输出:true:成功;false:失败    
     * @param template
     * @roseuid 45864967030D
     */
    
    public boolean updateAllInfoOfTemplate(Template template) throws TemplateManagerException;
    
//  -------查------   
    /**
     * 根据站点id,模板类型查找模板
     */
    public List getTplList(int siteid,int type)throws TemplateManagerException;
   
    /**功能:取某个模板详细信息,用于模板编辑
     * 输入:模板ID
     * 输出:list对象
     * @param templateid
     * @roseuid 45864967035B
     */
    public Template getTemplateInfo(String templateid) throws TemplateManagerException;
    
    
    /**
     * 功能:获取站点的首页模版,用于发布和站点预览
     * 输入:模板ID
     * 输出:Template对象
     * @param siteid
     * 
     */
    public Template getIndexTemplateOfSite(String siteid) throws TemplateManagerException;
    
   /**获取站点需要增量发布的首页模板   * 
   * 输入:模板ID
   *  @param siteid 站点ID
   */      
	public Template getSiteHomepageTplNeedIncPub(String siteId) throws TemplateManagerException;
	
	/**取某个站点下的模板列表,用于站点模板维护
     * 输入:站点ID
     * 输出:列表对像list
     * @param 
     * @roseuid 45864967035B
     */ 
    public List getTemplateInfoListofSite(int siteid) throws TemplateManagerException;
    
    /**
     * 取某个站点下的模板列表,用于模板信息缓冲（Template中包含了模板正文信息）
     * 输入:站点ID
     * 输出:列表对像list<Template>
     * @param 
     * add by xinwang.jiao
     */ 
    public List getTemplateListOfSite(String siteid) throws TemplateManagerException;
	
	/**获取站点需要增量发布概览页面的频道及对应的模板
	 * @param siteId
	 * @return 返回的list中包含的元素还是list，子list中第一个元素为频道ID，第二个元素为模板ID
	 * @throws TemplateManagerException
	 */
	public List getChnlOutlineNeedIncPub(String siteId) throws TemplateManagerException;
    
	
	/**获取需要增量发布的细览模板列表
	 * @param siteId
	 * @return List
	 * @throws TemplateManagerException
	 */
	public List getDetailTplNeedIncPub(String siteId) throws TemplateManagerException;
	
	/**获取细览模板关联的可完全发布的文档列表
	 * @param siteId
	 * @return List
	 * @throws TemplateManagerException
	 */
	public List getAllCanPubDocsByTpl(String templateId) throws TemplateManagerException;

	
	
	/**功能:取站点下特定类型的模板详细信息,不包括其频道下的模板
     * 输入:站点ID；类型type：0 首页，1 概览，2 细览，3评论
     * 输出:列表对像list    
     */ 
    public List getTemplateInfoListofSite(int siteid,int type) throws TemplateManagerException;
    /**
     * 分页查询中取站点中模板分页列表,不包括其频道下的模板
     */
    public ListInfo getTemplateInfoListofSite(int siteid,int offset,int maxitem) throws TemplateManagerException;

    
    /**
     * 根据SQL语言传入实现搜索查询模板的列表
     * 
     * sql为传入的要执行的sql语言
     * 
     */
    public ListInfo getTemplateInfoListofSite (HttpServletRequest request,int offset,int maxitem) throws TemplateManagerException;
    /**取某一类型的模板列表
     * 输入:type 模板类型
     * 输出:列表对像list
     * @param 
     * @roseuid 45864967035B
     */ 
    public List getTemplateInfoList(int type) throws TemplateManagerException;
    /**
     * 根据站点ID取得站点下模板的创作者。
     * @param type
     */
    public List getSiteTplCreator(String siteId) throws TemplateManagerException;
    
    /**
     * 根据站点id,路径,文件名获取存储在文件系统中的模板的模板基本信息
     * @param siteId 某个站点
     * @param path 相对站点下_template目录的相对路径
     * @param name 文件名
     * @return 
     * @throws TemplateManagerException
     */
	public Template getTemplateInfo(String siteId,String path,String name)throws TemplateManagerException;


	/**
	 * 导出某个站点下的某个模板
	 * @param siteId 站点id
	 * @param templateId 模板id
	 * @param target 生成的压缩包输出流目的地
	 * @return
	 * @throws TemplateManagerException
	 */
	public void export(HttpServletRequest request,String siteId, String templateId,OutputStream target)throws TemplateManagerException;
	
	/**
	 * 判断站点的页面是否是模版
	 * @param siteid 
	 * @param pageUrl
	 * @param fileName
	 * @return
	 */
	public boolean isPageTemplate(String siteid,String pagePath,String fileName);
	
	/**
	 * 判断页面是否是模版
	 * @param pageUrl
	 * @param fileName
	 * @return
	 */
	public boolean isTemplate(String pagePath,String fileName);
	
	/**
	 * 判断模板在哪一个站点下面
	 * @param siteid 
	 * @param pageUrl
	 * @param fileName
	 * @return
	 */
	public String getTemplateofSite(String templateId);
	
	/**
	 * 获取模板被引用得次数，
	 * flag：0－被引用的总次数(包括频道引用和文档引用)；1－被频道引用的次数；2－被文档引用的次数
	 * @param templateId
	 * @param flag
	 * @return
	 */
	public int getTplCtiedCount(String templateId,int flag) throws TemplateManagerException;
	/**
	 * 获取首页被引用得次数，
	 * @param indexPagePath
	 * @param siteId
	 * @return
	 * @throws TemplateManagerException
	 */
	public int getIndexPageCtiedCount(String indexPagePath,String siteId) throws TemplateManagerException;
	/**
	 * 获取模板或首页的频道引用列表。
	 * @param templateId
	 * @param offset
	 * @param maxitem
	 * @return
	 * @throws TemplateManagerException
	 */
	public ListInfo getChannelListofTlpCited (String indexPagePath,String siteId,String templateId,int offset,int maxitem) throws TemplateManagerException;
	/**
	 * 获取模板的文档引用列表。
	 * @param templateId
	 * @param offset
	 * @param maxitem
	 * @return
	 * @throws TemplateManagerException
	 */
	public ListInfo getDocumentListofTlpCited (String templateId,int offset,int maxitem) throws TemplateManagerException;
	
	/**
	 * 导出某个站点下的多个模板
	 * @param siteId 站点id
	 * @param templateIds 模板id数组
	 * @param target 生成的压缩包输出流目的地
	 * @throws TemplateManagerException
	 * @return 返回对导出模板的描述
	 * 先建压缩包, 把文件逐一存放到压缩包, 然后, 记录文件的路径, 把描述多个模板的文件, 存放到压缩包
	 */
	public String exportTmpls(HttpServletRequest request,String siteId, String[] templateIds, OutputStream target)throws TemplateManagerException ;
	
	/**
	 * 
	 * @param xml xml路径
	 * @param fillTemplate 压缩文件路径
	 * @return 
	 * TemplateManagerImpl.java
	 * @author: ge.tao
	 * xml文件的拓扑结构
	 * <templates>
	 *     <template>
	 *         <id></id>
	 *	       <name></name>
	 *         ...
	 *     </template>
	 *     <template>
	 *         <id></id>
	 *	       <name></name>
	 *         ...
	 *     </template>
	 *     ...
	 * </templates>
	 * 分析模板描述文件,把模板相关文件,用目录结构分离
	 * 构造模板对象
	 */
	public Object fillTemplate(String xml,String zipAbsPath);
	
	/**
	 * 获取导出模板的所有记录列表 不翻页
	 * @param count
	 * @return 
	 * TemplateManager.java
	 * @author: ge.tao
	 */
	public List getExportTmplList(int count);
	
	/**
	 * 获取导出当前站点有权限查看的模板包列表 翻页
	 * @param offset
	 * @param pageitems
	 * @param siteId
	 * @return
	 * @throws DocumentManagerException 
	 * TemplateManager.java
	 * @author: ge.tao
	 */
	public ListInfo getExportTmplList(long offset, int pageitems,int siteId) ;
	/**
	 * 新增导出模板的记录
	 * @param tmplName 导出模板名称
	 * @param tmplDesc 导出模板描述
	 * @param userid 操作人ID
	 * @param expType 导出模板是共有还是私有
	 * siteid
	 * TemplateManager.java
	 * @author: ge.tao
	 */	
	public void addExportTmplRecord(String tmplName, String tmplDesc, String userid, String expType,int siteId);
	
	/**
	 * 删除导入模板的记录和导出模板
	 * @param tmplName 导出模板名称
	 * @param tmplName 导出模板路径
	 * TemplateManager.java
	 * @author: ge.tao
	 */
	public void deleteExportTmplRecord(String tmplName,String fielPath);
	
	/**
	 * 获取导出模板的所有记录列表,里面封装TmplateExport对象
	 * @param select 0: 共有; 1: 有权限的私有; 2:有权限的全部 ; 3:全部
	 * @param siteId 当前站点ID 
	 * @return 
	 * TemplateManager.java
	 * @author: ge.tao
	 */
	public List exportTmplRecordList(int select, int siteId);
	
	/**
	 * 根据模板类型查询出所有子类型列表
	 * @param type 模版类型0:站点首页模版1：频道概览模板2：文档细览模板 3：文档评论模板
	 * @return
	 * @throws TemplateManagerException
	 * @author: peng.yang
	 */
	public List getTemplateStyleList(String type);
	
	
	/**
	 * 根据所有模板风格将某个站点下的所有模板信息分组显示到《select》标签内
	 * @param siteid
	 * @return
	 * *@throws TemplateManagerException
	 * @author: peng.yang
	 */
	public List groupByTemplateStyle(int siteid, int type) throws TemplateManagerException;
	
	/**
	 * 删除文件系统中的模板
	 * @param templateid 模板id
	 * @param tsiteId 站点id
	 * @param userId 用户id
	 * TemplateManager.java
	 * @author: zhizhong.ding
	 */
	public void deleteTemplateFile(int templateid, String siteId,String userId)throws TemplateManagerException;
	
	/**
	 * 根据站点ID，原文件路径，新文件路径更新模板文件路径
	 * 
	 * @param sitId,olduri,newuri
	 *         
	 * @return  
	 * @throws TemplateManagerException
	 * @author: zhizhong.ding
	 */
	public void  updateTemplatePath(String siteId,String olduri,String newuri) throws TemplateManagerException;
}
