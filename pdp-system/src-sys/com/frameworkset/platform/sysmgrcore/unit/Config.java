package com.frameworkset.platform.sysmgrcore.unit;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 项目：SysMgrCore <br>
 * 描述：配置管理模块 <br>
 * 版本：1.0 <br>
 * 
 * @author 吴卫雄
 */
public class Config implements Serializable{

    private static Logger logger = Logger.getLogger(Config.class.getName());

    /**
     * 访问服务器的路径
     */
    private static String path = "";

    /**
     * 是使用缓冲存储器
     */
    private static boolean isUseBufferStore = false;

    /**
     * 初始化配置项目
     */
    public static void init() {

        if (!path.endsWith("\\"))
            path += "\\";

        File file = new File(path + "WEB-INF\\config.props");
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                Properties props = new Properties();
                props.load(fis);

                // 初始化配置
                isUseBufferStore = Boolean.valueOf(
                        props.getProperty("isusebufferstore")).booleanValue();
            } catch (Exception e) {
                logger.error(e);
            }
        } else
            logger
                    .error("无法在 " + path
                            + " 目录中找到配置文件(config.props)，无法完成配置的初始化！");
    }

    /**
     * @return 返回 path。
     */
    public static String getPath() {
        return path;
    }

    /**
     * @param path
     *            要设置的 path。
     */
    public static void setPath(String path) {
        Config.path = path;
    }

    /**
     * @return 返回 isUseBufferStore。
     */
    public static boolean isUseBufferStore() {
        return isUseBufferStore;
    }
}