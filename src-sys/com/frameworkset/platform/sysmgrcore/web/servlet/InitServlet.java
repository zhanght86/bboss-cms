package com.frameworkset.platform.sysmgrcore.web.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import net.sf.hibernate.HibernateException;

import org.apache.log4j.Logger;

import com.frameworkset.platform.sysmgrcore.unit.HibernateSessionFactory;
import com.frameworkset.common.poolman.util.SQLManager;

/**
 * 项目：SysMgrCore <br>
 * 描述：初始化系统环境，其中包括加载数据到缓冲存储器中。 <br>
 * 版本：1.0 <br>
 * 
 * @author 吴卫雄
 */
public class InitServlet extends HttpServlet implements Serializable{

    private static Logger logger = Logger
            .getLogger(InitServlet.class.getName());

    /**
     * Constructor of the object.
     */
    public InitServlet() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy();
    }

    /**
     * The doGet method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to get.
     * 
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * The doPost method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to
     * post.
     * 
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * 初始化环境
     */
    public void init(ServletConfig config) throws ServletException {

//        try {
//            logger.debug("完成系统初始化...");
//            SQLManager.getInstance().requestConnection().close();
//            
//            HibernateSessionFactory.currentSession();
//            
//        } catch (HibernateException e) {
//            logger.error(e);
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

    }
}