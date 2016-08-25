package com.frameworkset.platform.sysmgrcore.control;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

import com.frameworkset.platform.ldap.LdapManagerFactory;
import com.frameworkset.platform.sysmgrcore.exception.ControlException;

/**
 * 项目：SysMgrCore <br>
 * 描述：LDAP控制器 <br>
 * 版本：1.0 <br>
 * 
 * @author 
 */
public class LdapControl  {

    private static Logger logger = Logger
            .getLogger(LdapControl.class.getName());

    private DirContext dc = LdapManagerFactory.getDirContext();

    private Parameter parameter = null;

    /**
     * 增加条目
     */
    private Boolean addItem() throws ControlException {
        boolean r = false;

        if (parameter.getObject() == null
                && !(parameter.getObject() instanceof Attributes))
            throw new ControlException("Parameter.object 必须为 Attributes 对象");

        if (parameter.getProperties().isEmpty()
                || parameter.getProperties().getProperty("dn") == null)
            throw new ControlException(
                    "Parameter.properties 必须包含条目的 dn 属性，属性名称请注意使用小写");

        Attributes attrs = (Attributes) parameter.getObject();

        try {
            String name = parameter.getProperties().getProperty("dn");
            dc.createSubcontext(name, attrs);
            r = true;
        } catch (NamingException e) {
            logger.error(e);
            throw new ControlException(e.getMessage());
        }

        return new Boolean(r);
    }

    /**
     * 更新条目
     * 
     * @return
     * @throws ControlException
     */
    public Boolean updateItem() throws ControlException {
        boolean r = false;

        if (parameter.getObject() == null
                && !(parameter.getObject() instanceof Attributes))
            throw new ControlException("Parameter.object 必须为 Attributes 对象");

        if (parameter.getProperties().isEmpty()
                || parameter.getProperties().getProperty("dn") == null)
            throw new ControlException(
                    "Parameter.properties 必须包含条目的 dn 属性，属性名称请注意使用小写");

        if (parameter.getProperties().isEmpty()
                || parameter.getProperties().getProperty("modifyoption") == null)
            throw new ControlException(
                    "Parameter.properties 必须包含条目的 modifyoption 属性，属性名称请注意使用小写");

        Attributes attrs = (Attributes) parameter.getObject();

        try {
            String name = parameter.getProperties().getProperty("dn");
            int modifyoption = Integer.parseInt(parameter.getProperties()
                    .getProperty("modifyoption"));
            dc.modifyAttributes(name, modifyoption, attrs);
            r = true;
        } catch (NamingException e) {
            logger.error(e);
            throw new ControlException(e.getMessage());
        }

        return new Boolean(r);
    }

    /**
     * 删除指定的条目
     * 
     * @return
     * @throws ControlException
     */
    public Boolean deleteItem() throws ControlException {
        boolean r = false;

        if (parameter.getProperties().isEmpty()
                || parameter.getProperties().getProperty("dn") == null)
            throw new ControlException(
                    "Parameter.properties 必须包含条目的 dn 属性，属性名称请注意使用小写");

        try {
            String name = parameter.getProperties().getProperty("dn");
            dc.destroySubcontext(name);
            r = true;
        } catch (NamingException e) {
            logger.error(e);
            throw new ControlException(e.getMessage());
        }

        return new Boolean(r);
    }

    /**
     * 查找指定的条目
     * 
     * @return
     * @throws ControlException
     */
    public List findItem() throws ControlException {
        List list = null;

        if (parameter.getProperties().isEmpty()
                || parameter.getProperties().getProperty("parentdn") == null)
            throw new ControlException(
                    "Parameter.properties 必须包含条目的 parentdn 属性，属性名称请注意使用小写");

        if (parameter.getProperties().isEmpty()
                || parameter.getProperties().getProperty("filter") == null)
            throw new ControlException(
                    "Parameter.properties 必须包含条目的 filter 属性，属性名称请注意使用小写");

        String parentdn = parameter.getProperties().getProperty("parentdn");
        String filter = parameter.getProperties().getProperty("filter");
        SearchControls control = new SearchControls(2, 0, 0, null, false, false);
        try {
            list = new ArrayList();
            NamingEnumeration enu = dc.search(parentdn, filter, control);

            if (enu.hasMore()) {
                while (enu.hasMoreElements()) {
                    SearchResult result = (SearchResult) enu.next();
                    list.add(result);
                }
            }
        } catch (NamingException e) {
            logger.error(e);
        }

        return list;
    }

    public Object execute(Parameter parameter) throws ControlException {
        this.parameter = parameter;

        // ********************************************************
        // 新增或更新LDAP中的数据实例

        if (parameter.getCommand() == Parameter.COMMAND_STORE) {
            if (parameter.getProperties().isEmpty()
                    || parameter.getProperties().getProperty("id") == null)
                throw new ControlException(
                        "Parameter.properties 必须包含 id 属性，属性名称请注意使用小写");

            Boolean r = null;

            if (parameter.getProperties().getProperty("id").length() == 0)
                r = addItem();
            else
                r = updateItem();

            return r;
        }

        // ********************************************************
        // 删除LDAP中的数据实例

        if (parameter.getCommand() == Parameter.COMMAND_DELETE) {
            deleteItem();
        }

        // ********************************************************
        // 查找LDAP中的数据实例

        if (parameter.getCommand() == Parameter.COMMAND_GET) {
            return findItem();
        }

        return null;
    }

    public void commit(boolean isExit) throws ControlException {
    }

    public void exit() throws ControlException {
    }

    public void rollback(boolean isExit) throws ControlException {
    }


}