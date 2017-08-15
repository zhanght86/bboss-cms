package com.frameworkset.platform.resource;

import java.io.Serializable;

import com.frameworkset.platform.config.model.ResourceInfo;

/**
 * <p>Title: </p>
 *
 * <p>Description:资源识别器 </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public interface ResourceIndentity extends Serializable{
    public void setResourceInfo(ResourceInfo resourceInfo);
}
