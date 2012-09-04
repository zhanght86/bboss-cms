package com.frameworkset.platform.cms.templatemanager;

import java.io.FileFilter;
import java.util.List;
import java.util.Set;

public interface FileManager extends java.io.Serializable {
	/**
	 * 根据一个站点id（一个站点对应一个绝对路径）和相对路径获取直接的子文件夹
	 * @param siteId
	 * @param uri
	 * @return List<FileResource>
	 * @throws TemplateManagerException
	 */
	public List getDirectoryResource(String siteId,String uri) throws TemplateManagerException;
	
	/**
	 * 根据一个站点id（一个站点对应一个绝对路径）和相对路径获取直接的子文件
	 * @param siteId
	 * @param uri
	 * @return List<FileResource>
	 * @throws TemplateManagerException
	 */
	public List getFileResource(String siteId,String uri,FileFilter fileFilter) throws TemplateManagerException;
	
	/**
	 * 根据一个站点id（一个站点对应一个绝对路径）和相对路径获取直接的子文件和子文件夹
	 * @param siteId
	 * @param uri
	 * @return List<FileResource>
	 * @throws TemplateManagerException
	 */
	public Set getDirectoryAndFileResource(String siteId,String uri,FileFilter fileFilter) throws TemplateManagerException;

	/**
	 * 根据一个站点id（一个站点对应一个绝对路径）和相对路径,判断模板目录下的文件(或文件夹)是否被锁定
	 * @param siteId
	 * @param uri
	 */	
	public boolean isLock(String siteId,String uri)throws TemplateManagerException;
	
	/**
	 * 锁定站点下的某个文件(或文件夹),如果对文件夹进行操作,会影响该文件夹下的所有文件，文件夹
	 * @param siteId 
	 * @param uri
	 * @param userId
	 * @throws TemplateManagerException
	 */
	public void checkOutFile(String siteId,String uri,String userId)throws TemplateManagerException;

	/**
	 * 解锁站点下的某个文件(或文件夹),如果对文件夹进行操作,会影响该文件夹下的所有文件，文件夹
	 * @param siteId 
	 * @param uri
	 * @param userId
	 * @throws TemplateManagerException
	 */
	public void checkInFile(String siteId,String uri,String userId)throws TemplateManagerException;

	
	/**
	 * 改变文件的历史记录(具体动作就是:如果没有该文件的日志,插入该文件的日志;如果有,则选择最高版本)
	 * @param siteId 站点id
	 * @param uri 文件(或文件夹的相对路径)
	 * @param userId 用户id
	 * @param bakfileName 备份文件名称
	 * @param changeRemarks 改变的一些备注信息
	 * @param changeVersion 是否改变版本号(有些情况比如说,只是checkout checkin这些动作不需要改变文件内容,也不需要改变版本)
	 */
	public void logFileChange(String siteId,String uri,String userId,String bakfileName,String changeRemarks,boolean changeVersion)throws TemplateManagerException;
	
	/**
	 * 删除站点下uri对应的文件或文件夹,如果是文件夹的话,会删除所有子文件.在删除之前,会判断这个文件是否被该用户锁定,只有被该用户锁定才能删除
	 * @param siteId
	 * @param uri
	 * @param userId
	 * @throws TemplateManagerException
	 */
	public void deleteFiles(String siteId,String uri,String userId)throws TemplateManagerException;
	
	
	public void reName(String siteId,String uri,String userId,String oldName,String newName)throws TemplateManagerException;
}
