package com.frameworkset.platform.sysmgrcore.web.javabean.tree;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * 项目：sysmgrcore <br>
 * 描述：树形结构生成器接口 <br>
 * 版本：1.0 <br>
 * 
 * @author 吴卫雄
 */
public interface TreeBuilder extends Serializable{

    public void buildTree(TreeControl treeControl, HttpServletRequest request);
}