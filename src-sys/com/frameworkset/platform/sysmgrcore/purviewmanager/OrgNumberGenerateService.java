package com.frameworkset.platform.sysmgrcore.purviewmanager;

import com.frameworkset.platform.sysmgrcore.entity.Organization;

/**
 * 
 * 为机构编码提供层次结构 
 * 例： 父机构编码 43，直接子机构要增加两位长度 4301，4311..... 
 * 注意：子机构编码的前面几位一定要跟父机构编码相同“43”
 * 
 * <p>Title: OrgNumberGenerateService.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date  Sep 5, 2008 1:08:53 PM
 * @author gao.tang
 * @version 1.0
 */
public interface OrgNumberGenerateService {
	
	/**
	 * 判断机构编码是否需要层次规则
	 * @return
	 */
	public boolean enableOrgNumberGenerate();
	
	/**
	 * 根据父机构ID得到直接子机构的最大orgnumber值
	 * @param org
	 * @return
	 */
	public String generateOrgNumber(Organization parentOrg);
	
	/**
	 * 层次每次比父机构编码增加的长度  在父机构编码长度的基础上增加位数  
	 * @return
	 */
	public int getOrgNumberLen();
	

}
