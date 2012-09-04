package com.frameworkset.platform.sysmgrcore.exception;

import java.io.Serializable;

/**
 * 项目：SysMgrCore <br>
 * 描述：控制器异常类 <br>
 * 版本：1.0 <br>
 *
 * @author 吴卫雄
 */
public class ControlException extends Exception 
							implements Serializable {

    public ControlException(String msg) {
        super(msg);
    }
    
}
