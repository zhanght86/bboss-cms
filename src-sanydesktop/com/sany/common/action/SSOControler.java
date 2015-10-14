package com.sany.common.action;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.frameworkset.util.FileCopyUtils;
import org.frameworkset.util.annotations.AssertDToken;
import org.frameworkset.util.annotations.AssertTicket;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.interceptor.AuthenticateFilter;
import org.frameworkset.web.servlet.ModelMap;
import org.frameworkset.web.servlet.support.RequestContextUtils;
import org.frameworkset.web.token.TokenStore;
import org.frameworkset.wx.common.entity.WxAccessToken;
import org.frameworkset.wx.common.entity.WxUserToken;
import org.frameworkset.wx.common.mp.aes.AesException;
import org.frameworkset.wx.common.mp.aes.WXBizMsgCrypt;
import org.frameworkset.wx.common.util.WXHelper;

import com.frameworkset.platform.ca.CAManager;
import com.frameworkset.platform.ca.CaProperties;
import com.frameworkset.platform.ca.CookieProperties;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.MenuItem;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.SubSystem;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authorization.AccessException;
import com.frameworkset.platform.sysmgrcore.authenticate.LoginUtil;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.util.StringUtil;
import com.liferay.portlet.iframe.action.SSOUserMapping;
import com.liferay.portlet.iframe.action.WebDes;
import com.sany.webseal.LoginValidate.CommonInfo;
import com.sany.webseal.LoginValidate.UimUserInfo;

public class SSOControler {

    private static Logger log = Logger.getLogger(SSOControler.class);

    public String sso() {
        return "path:sso";
    }

    private String getSuccessRedirect(String loginStyle, String subsystem) {
        StringBuilder ret = new StringBuilder();
        if (StringUtil.isEmpty(subsystem)) {

            if (loginStyle == null || loginStyle.equals("5") || loginStyle.equals("6")) {
                ret.append("sanydesktop/indexcommon.page");
            } else if (loginStyle.equals("1")) {
                ret.append("index.jsp?subsystem_id=").append(subsystem);
            } else if (loginStyle.equals("3")) {
                ret.append("sanydesktop/index.page");
            } else if (loginStyle.equals("2")) {
                ret.append("desktop/desktop1.page");
            } else if (loginStyle.equals("4")) {
                ret.append("sanydesktop/webindex.page");
            } else {
                ret.append("sanydesktop/indexcommon.page");
            }
        } else {
            if (subsystem.equals("cms")) {
                ret.append("index.jsp?subsystem_id=").append(subsystem);
                return ret.toString();
            }
            SubSystem sys = Framework.getSubSystem(subsystem);
            if (sys != null && !StringUtil.isEmpty(sys.getSuccessRedirect()))
                ret.append(sys.getSuccessRedirect());
            else {
                if (loginStyle == null || loginStyle.equals("5") || loginStyle.equals("6")) {
                    ret.append("sanydesktop/indexcommon.page");
                } else if (loginStyle.equals("1")) {
                    ret.append("index.jsp?subsystem_id=").append(subsystem);
                } else if (loginStyle.equals("3")) {
                    ret.append("sanydesktop/index.page");
                } else if (loginStyle.equals("2")) {
                    ret.append("desktop/desktop1.page");
                } else if (loginStyle.equals("4")) {
                    ret.append("sanydesktop/webindex.page");
                } else {
                    ret.append("sanydesktop/indexcommon.page");
                }
            }
        }
        return ret.toString();
    }

    public String login(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception,
            Exception {
        HttpSession session = request.getSession(false);
        // AccessControl context_ = AccessControl.getInstance();
        // boolean success = context_.checkAccess(request, response,false);
        // if(success)
        // {
        // out.println("对不起！用户"+context_.getUserAccount() +
        // "("+context_.getUserName()+")已经登录系统，请注销后再重新登录系统!");
        // return;
        // }

        String u = "", p = "", ck = "";
        boolean fromredirect = false;

        String successRedirect = request.getParameter(AuthenticateFilter.referpath_parametername);
        if (successRedirect != null) {
            if ((successRedirect.equals(request.getContextPath())
                    || successRedirect.equals(request.getContextPath() + "/") || successRedirect.equals("login.jsp") || successRedirect
                        .equals("login.page"))) {
                successRedirect = null;
            } else {
                fromredirect = true;
                model.addAttribute("successRedirect", successRedirect);
            }

        }
        String language = request.getParameter("language");
        boolean enable_login_validatecode = ConfigManager.getInstance().getConfigBooleanValue(
                "enable_login_validatecode", true);

        model.addAttribute("enable_login_validatecode", enable_login_validatecode);
        String errorMessage = null;

        String userName = request.getParameter("userName");
        int expiredays = userName != null ? SecurityDatabase.getUserManager().getUserPasswordDualTimeByUserAccount(
                userName) : 0;
        String expriedtime_ = "";
        Date expiretime = expiredays > 0 && userName != null ? SecurityDatabase.getUserManager()
                .getPasswordExpiredTimeByUserAccount(userName) : null;
        if (expiretime != null) {
            SimpleDateFormat dateformt = new SimpleDateFormat("yyyy-MM-dd");
            expriedtime_ = dateformt.format(expiretime);
            model.addAttribute("expriedtime_", expriedtime_);
            model.addAttribute("userName", userName);
            model.addAttribute("expiredays", expiredays);

        }
        String loginStyle = null;
        String system_id = null;
        if (language == null) {
            language = RequestContextUtils.getLocaleResolver(request).resolveLocaleCode(request);

        }
        model.addAttribute("language", language);

        /*
         * if(language.equals("zh_CN")){
         * request.getSession().setAttribute("languageKey",
         * java.util.Locale.CHINA); } else if(language.equals("en_US")){
         * request.getSession().setAttribute("languageKey",
         * java.util.Locale.US); }
         */
        String loginPath = request.getParameter("loginPath");
        String subsystem_id = request.getParameter("subsystem_id");

        loginStyle = StringUtil.getCookieValue(request, "loginStyle");
        system_id = StringUtil.getCookieValue(request, "subsystem_id");

        if (loginPath != null) {
            StringUtil.addCookieValue(request, response, "loginStyle", loginPath);

        }
        if (subsystem_id != null) {
            StringUtil.addCookieValue(request, response, "subsystem_id", subsystem_id);

        }
        model.addAttribute("system_id", system_id);
        model.addAttribute("loginStyle", loginStyle);
        model.addAttribute("defaultmodulename", Framework.getSystemName("module", request));
        boolean isCasServer = ConfigManager.getInstance().getConfigBooleanValue("isCasServer", false);

        boolean isWebSealServer = ConfigManager.getInstance().getConfigBooleanValue("isWebSealServer", false);

        // 是否使用数字认证中心服务
        boolean CA_LOGIN_SERVER = CaProperties.CA_LOGIN_SERVER;

        boolean isCert = true;
        String certUserName = null;
        String certUserPassword = null;
        if (CA_LOGIN_SERVER) {
            isCert = CAManager.checkCertSn(request);
            if (isCert) {
                certUserName = CAManager.getUserName(request);
                certUserPassword = CAManager.getUserPassword(request);
            }
        }
        String specialuser = LoginUtil.isSpesialUser(request);
        if (isCasServer) {
            userName = (String) session.getAttribute("edu.yale.its.tp.cas.client.filter.user");
            boolean state = false;
            if (userName != null && !"".equals(userName)) {
                state = SSOUserMapping.isIncludeUser(userName);
            }
            if (state) {
                // 系统管理版本号，2.0和2.0以上的版本,默认版本为1.0
                String systemVersion = ConfigManager.getInstance().getConfigValue("system.version", "1.0");
                String subsystem = request.getParameter("subsystem_id");
                String password = SSOUserMapping.getUserPassword(userName);
                if (subsystem == null)
                    subsystem = AccessControl.getDefaultSUBSystemID();

                if (systemVersion.compareTo("1.0") > 0) {
                    AccessControl.getInstance().login(request, response, userName, password);

                    // if(subsystem == null) subsystem = "module";
                    /**
                     * 需要全屏时，将response.sendRedirect("index.jsp");注释掉，
                     * 将response.sendRedirect("sysmanager/refactorwindow.jsp");
                     * 打开
                     */
                    if (successRedirect == null) {
                        successRedirect = getSuccessRedirect(loginPath, subsystem);

                    }
                    if (!fromredirect) {
                        AccessControl.recordIndexPage(request, successRedirect);
                        AccessControl.recordeSystemLoginPage(request, response);
                    }
                    response.sendRedirect(successRedirect);
                    return null;
                } else {
                    // 判断用户是否已经登录
                    AccessControl control = AccessControl.getInstance();
                    control.checkAccess(request, response, false);
                    String user = control.getUserAccount();
                    // 如果没有登录则进行登录
                    if (user == null || "".equals(user) || !userName.equals(user)) {
                        if (!userName.equals(user))
                            control.resetSession(session);
                        AccessControl.getInstance().login(request, response, userName, password);
                        if (subsystem == null)
                            subsystem = AccessControl.getDefaultSUBSystemID();
                        if (successRedirect == null) {
                            successRedirect = getSuccessRedirect(loginPath, subsystem);
                        }
                        if (!fromredirect) {
                            AccessControl.recordIndexPage(request, successRedirect);
                            AccessControl.recordeSystemLoginPage(request, response);
                        }
                        response.sendRedirect(successRedirect);
                        return null;
                    } else {
                        if (subsystem == null)
                            subsystem = AccessControl.getDefaultSUBSystemID();
                        if (successRedirect == null) {
                            successRedirect = getSuccessRedirect(loginPath, subsystem);

                        }
                        if (!fromredirect) {
                            AccessControl.recordIndexPage(request, successRedirect);
                            AccessControl.recordeSystemLoginPage(request, response);
                        }
                        response.sendRedirect(successRedirect);
                        return null;
                    }
                }
            } else {
                if (userName == null || userName.equals("")) {

                    model.addAttribute("errorMessage", "系统启用了cas单点登录功能，请在web.xml的CAS Filte中拦截login.jsp页面");
                } else {
                    model.addAttribute("errorMessage", "用户【" + userName + "】在此应用中没有开通！ ");
                }
            }
        } else if ((specialuser != null || isWebSealServer) && userName == null) {

            String subsystem = request.getParameter("subsystem_id");
            try// uim检测
            {
                String ip = "";
                if (specialuser != null) {
                    userName = specialuser;
                    ip = com.frameworkset.util.StringUtil.getClientIP(request);
                } else {
                    CommonInfo info = new CommonInfo();
                    UimUserInfo userinfo = null;

                    userinfo = info.validateUIM(request);
                    ip = userinfo.getUser_ip();
                    userName = userinfo.getUser_name();
                }
                AccessControl control = AccessControl.getInstance();
                control.checkAccess(request, response, false);
                String user = control.getUserAccount();
                request.setAttribute("fromsso", "true");
                if (user == null || "".equals(user) || !userName.equals(user)) {

                    try {
                        if (!userName.equals(user))
                            control.resetSession(session);
                        String password = SSOUserMapping.getUserPassword(userName);
                        control = AccessControl.getInstance();
                        control.login(request, response, userName, password);

                        if (subsystem == null)
                            subsystem = AccessControl.getDefaultSUBSystemID();
                        if (successRedirect == null) {
                            successRedirect = getSuccessRedirect(loginPath, subsystem);
                        }
                        if (!fromredirect) {
                            AccessControl.recordIndexPage(request, successRedirect);
                            AccessControl.recordeSystemLoginPage(request, response);
                        }
                        response.sendRedirect(successRedirect);
                        return null;
                    } catch (Exception e) {

                        response.sendRedirect(request.getContextPath() + "/webseal/websealloginfail.jsp?userName="
                                + userName + "&ip=" + ip);
                        return null;
                    }

                } else {

                    if (subsystem == null)
                        subsystem = AccessControl.getDefaultSUBSystemID();
                    if (successRedirect == null) {
                        successRedirect = getSuccessRedirect(loginPath, subsystem);

                    }
                    if (!fromredirect) {
                        AccessControl.recordIndexPage(request, successRedirect);
                        AccessControl.recordeSystemLoginPage(request, response);
                    }
                    response.sendRedirect(successRedirect);
                    return null;
                }

            } catch (Exception e)// 检测失败,继续平台登录
            {

            }

        } else {
            // System.out.println(session.getSessionContext());
            String flag = request.getParameter("flag"); // 是否触发提交

            // 登陆名称的长度

            if (flag == null) {
            } else {
                // String successRedirect =
                // request.getParameter("successRedirect");

                String password = request.getParameter("password");
                WebDes wd = new WebDes();
                password = wd.strDec(password, userName, "", "");

                if (userName != null) {
                    try {
                        if (enable_login_validatecode) {
                            String rand = request.getParameter("rand");
                            String session_rand = (String) session
                                    .getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
                            session.removeAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
                            if (session_rand == null || (!session_rand.equalsIgnoreCase(rand))) {
                                throw new AccessException("验证码错误!");
                            }
                        }

                        AccessControl.getInstance().login(request, response, userName, password);
                        String subsystem = request.getParameter("subsystem_id");
                        UserManager userManager = SecurityDatabase.getUserManager();
                        // System.out.println("orgName========================"+orgName);
                        // System.out.println("orgId========================"+orgId);
                        if (!isCert) {
                            CAManager.updateUserCERT_SN(request, userName);
                        }
                        if (CA_LOGIN_SERVER)
                            request.getSession().setAttribute(CookieProperties.CERT_SN,
                                    CAManager.getCookieValue(request, CookieProperties.CERT_SN));
                        if (subsystem == null)
                            subsystem = AccessControl.getDefaultSUBSystemID();
                        /**
                         * 需要全屏时，将response.sendRedirect("index.jsp");注释掉，
                         * 将response.sendRedirect(
                         * "sysmanager/refactorwindow.jsp");打开
                         */
                        if (successRedirect == null) {
                            successRedirect = getSuccessRedirect(loginPath, subsystem);

                        }
                        if (!fromredirect) {
                            AccessControl.recordIndexPage(request, successRedirect);
                            AccessControl.recordeSystemLoginPage(request, response);
                        }
                        response.sendRedirect(successRedirect);
                        return null;
                        // response.sendRedirect("sysmanager/refactorwindow.jsp?subsystem_id="
                        // + subsystem);
                    } catch (AccessException ex) {

                        errorMessage = ex.getMessage();
                        if (errorMessage != null) {
                            // errorMessage = errorMessage.replaceAll("\\n",
                            // "\\\\n");
                            // errorMessage = errorMessage.replaceAll("\\r",
                            // "\\\\r");
                        } else {
                            errorMessage = org.frameworkset.web.servlet.support.RequestContextUtils.getI18nMessage(
                                    "sany.pdp.login.failed", request);
                        }

                        // if(errorMessage==null){
                        // out.print("登陆失败，请确保输入的用户名和口令是否正确！");
                        // }
                        // else{
                        // out.print(errorMessage);
                        // }

                    } catch (Exception ex) {
                        errorMessage = ex.getMessage();

                        if (errorMessage != null) {
                            // errorMessage = errorMessage.replaceAll("\\n",
                            // "\\\\n");
                            // errorMessage = errorMessage.replaceAll("\\r",
                            // "\\\\r");
                        } else {
                            errorMessage = org.frameworkset.web.servlet.support.RequestContextUtils.getI18nMessage(
                                    "sany.pdp.login.failed", request);
                        }
                        // out.print(errorMessage+ "登陆失败，请确保输入的用户名和口令是否正确！");

                    }
                }

            }

        }
        if (errorMessage != null)
            model.addAttribute("errorMessage", errorMessage);
        List<SubSystem> subsystemList = Framework.getInstance().getSubsystemList();
        List<SysInfo> syses = new ArrayList<SysInfo>();
        SysInfo sys = new SysInfo();
        sys.setName(Framework.getSystemName("module", request));
        sys.setSysid("module");
        syses.add(sys);
        if (subsystemList != null && subsystemList.size() > 0) {
            for (SubSystem sub : subsystemList) {
                sys = new SysInfo();
                sys.setName(Framework.getSystemName(sub.getId(), request));
                sys.setSysid(sub.getId());
                if (system_id != null && system_id.equals(sys.getSysid()))
                    sys.setSelected(true);
                syses.add(sys);
            }

        }
        model.addAttribute("systemList", syses);
        return "/login.jsp";
    }

    @AssertTicket
    public void ssowithticket(HttpServletRequest request, HttpServletResponse response) {
        _ssowithtoken(request, response);
    }

    @AssertDToken
    public void ssowithtoken(HttpServletRequest request, HttpServletResponse response) {
        _ssowithtoken(request, response);
    }

    public @ResponseBody
    String recive(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("消息接收接口");
        
        String app = request.getParameter("app");
        log.info("微信系统app名称=" + app);

        // 微信加密签名
        String sVerifyMsgSig = request.getParameter("msg_signature");
        // System.out.println("微信加密签名msg_signature=" + sVerifyMsgSig);
        log.info("微信加密签名msg_signature=" + sVerifyMsgSig);

        // 时间戳
        String sVerifyTimeStamp = request.getParameter("timestamp");
        // System.out.println("时间戳sVerifyTimeStamp=" + sVerifyTimeStamp);
        log.info("时间戳sVerifyTimeStamp=" + sVerifyTimeStamp);

        // 随机数
        String sVerifyNonce = request.getParameter("nonce");
        // System.out.println("随机数sVerifyNonce=" + sVerifyNonce);
        log.info("随机数sVerifyNonce=" + sVerifyNonce);

        // 随机字符串
        String sVerifyEchoStr = request.getParameter("echostr");
        // System.out.println("随机字符串sVerifyEchoStr=" + sVerifyEchoStr);
        log.info("随机字符串sVerifyEchoStr=" + sVerifyEchoStr);

        String sEchoStr = ""; // 需要返回的明文
        if (StringUtils.isNotEmpty(sVerifyEchoStr)) {

            WXBizMsgCrypt wxcpt;
            try {
                String weixin_token = WXHelper.getEnterpriseToken(app);
                String weixin_aeskey = WXHelper.getEnterpriseAeskey(app); 
                String weixin_corpid = WXHelper.getEnterpriseCorpid(app);
                
                wxcpt = new WXBizMsgCrypt(weixin_token, weixin_aeskey, weixin_corpid);
                sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sVerifyEchoStr);
                System.out.println("需要返回的明文sEchoStr=" + sEchoStr);

                // 验证URL成功，将sEchoStr返回
            } catch (AesException e1) {
                e1.printStackTrace();
            }
        }

        return sEchoStr;

    }

    /**
     * 配置微信菜单 //redirect_uri=http://domain/contextpath/sso/wxsso.page?loginMenu=
     * appbommanager
     * 
     * redirect_uri=http://domain/contextpath/sso/wxsso.page?successRedirect=<%=
     * URLEncoder.encode("/appbom/aaa.page")
     * 
     * 微信跳转过来的地址
     * redirect_uri=http://domain/contextpath/sso/wxsso.page?loginMenu=
     * appbommanager&app=zqztpy&code=CODE&state=STATE
     * 
     * redirect_uri=http://domain/contextpath/sso/wxsso.page?successRedirect=/
     * appbom/aaa.page&code=CODE&state=STATE
     * 
     * @param request
     * @param response
     */
    public void wxsso(HttpServletRequest request, HttpServletResponse response) {

        String code = request.getParameter("code");
        System.out.println("微信code=" + code);

        String app = request.getParameter("state");
        System.out.println("微信state=" + app);

        String successRedirect = request.getParameter("successRedirect");
        System.out.println("微信successRedirect=" + successRedirect);

        String corpid = WXHelper.getEnterpriseCorpid(app), corpsecret = WXHelper.getEnterpriseCorpsecret(app);
        System.out.println("微信corpid=" + corpid);
        System.out.println("微信corpsecret=" + corpsecret);

        String userName = null;
        String loginMenu = request.getParameter("loginMenu");
        String contextpath = request.getContextPath();
        String menuid = null;
        if (loginMenu != null) {

            menuid = loginMenu;

        }
        HttpSession session = request.getSession();

        try {
            AccessControl control = AccessControl.getInstance();
            control.checkAccess(request, response, false);
            String user = control.getUserAccount();
            if (!control.isGuest()) {
                if (!WXHelper.uselocalsession()) {

                    WxAccessToken accesstoken = WXHelper.getEnterpriseWXSecurityService().getWxAccessToken(corpid,
                            corpsecret);
                    WxUserToken userToken = WXHelper.getEnterpriseWXSecurityService().getWxUserToken(
                            accesstoken.getAccess_token(), code);

                    userName = userToken.getUserId();

                } else {
                    userName = user;
                }
            } else {
                user = null;
                WxAccessToken accesstoken = WXHelper.getEnterpriseWXSecurityService().getWxAccessToken(corpid,
                        corpsecret);
                WxUserToken userToken = WXHelper.getEnterpriseWXSecurityService().getWxUserToken(
                        accesstoken.getAccess_token(), code);

                userName = userToken.getUserId();
            }

            System.out.println("微信userName=" + userName);

            boolean issameuser = false;

            if (user != null && !user.equals(""))
                issameuser = userName.equals(user);

            if (user == null || "".equals(user) || !issameuser) {

                if (!issameuser) {
                    control.resetSession(session);
                }

                try {
                    // 1-域账号登录 2-工号登录
                    String password = null;

                    password = SSOUserMapping.getUserPassword(userName);
                    if (password == null)
                        throw new AccessException("用户" + userName + "不存在。");

                    control = AccessControl.getInstance();
                    request.setAttribute("fromsso", "true");
                    // System.out.println("-----------userName="+userName+",password="+password);
                    control.login(request, response, userName, password);
                    if (StringUtil.isEmpty(successRedirect)) {
                        Framework framework = Framework.getInstance(control.getCurrentSystemID());
                        MenuItem menuitem = framework.getMenuByID(menuid);
                        if (menuitem instanceof Item) {

                            Item menu = (Item) menuitem;
                            successRedirect = MenuHelper.getRealUrl(contextpath,
                                    Framework.getWorkspaceContent(menu, control), MenuHelper.sanymenupath_menuid,
                                    menu.getId());
                        } else {

                            Module menu = (Module) menuitem;
                            String framepath = contextpath + "/sanydesktop/singleframe.page?" + MenuHelper.sanymenupath
                                    + "=" + menu.getPath();
                            successRedirect = framepath;
                        }
                        AccessControl.recordIndexPage(request, successRedirect);
                    } else {
                        successRedirect = URLDecoder.decode(successRedirect);
                    }
                    response.sendRedirect(successRedirect);
                    return;
                } catch (Exception e) {
                    log.info("", e);
                    String msg = e.getMessage();
                    if (msg == null)
                        msg = "";
                    response.sendRedirect(contextpath + "/webseal/websealloginfail.jsp?userName=" + userName
                            + "&errormsg=" + java.net.URLEncoder.encode(msg, "UTF-8"));
                    return;
                }

            } else {
                control.resetUserAttributes();
                if (StringUtil.isEmpty(successRedirect)) {
                    Framework framework = Framework.getInstance(control.getCurrentSystemID());
                    MenuItem menuitem = framework.getMenuByID(menuid);
                    if (menuitem instanceof Item) {

                        Item menu = (Item) menuitem;
                        successRedirect = MenuHelper.getRealUrl(contextpath,
                                Framework.getWorkspaceContent(menu, control), MenuHelper.sanymenupath_menuid,
                                menu.getId());
                    } else {

                        Module menu = (Module) menuitem;
                        String framepath = contextpath + "/sanydesktop/singleframe.page?" + MenuHelper.sanymenupath
                                + "=" + menu.getPath();
                        successRedirect = framepath;
                    }
                    AccessControl.recordIndexPage(request, successRedirect);
                } else {
                    successRedirect = URLDecoder.decode(successRedirect);
                }
                response.sendRedirect(successRedirect);
                return;
            }

        } catch (Throwable ex) {
            log.info("", ex);
            String errorMessage = ex.getMessage();
            if (errorMessage == null)
                errorMessage = "";

            try {
                FileCopyUtils.copy(errorMessage + "," + userName + "登陆失败，请确保输入的用户名和口令是否正确！", new OutputStreamWriter(
                        response.getOutputStream(), "UTF-8"));
            } catch (IOException e) {
                log.info("", e);
            }

        }

    }

    /**
     * 强制要求系统必须携带令牌
     * 
     * @return
     */

    public void _ssowithtoken(HttpServletRequest request, HttpServletResponse response) {
        // return "path:sso";

        String u = "", p = "", ck = "";

        String successRedirect = request.getParameter("successRedirect");
        // System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+successRedirect);
        if (!StringUtil.isEmpty(successRedirect)) {
            successRedirect = StringUtil.getRealPath(request, successRedirect, true);
        }
        String userName = (String) request.getAttribute(TokenStore.token_request_account_key);
        String worknumber = (String) request.getAttribute(TokenStore.token_request_worknumber_key);
        String loginType = "1";
        if (StringUtil.isEmpty(userName)) {
            userName = worknumber;
            loginType = "2";
        }

        String loginMenu = request.getParameter("loginMenu");
        String contextpath = request.getContextPath();
        String menuid = "newGetDoc";
        if (loginMenu != null) {

            menuid = loginMenu;

        }
        HttpSession session = request.getSession();

        boolean isWebSealServer = ConfigManager.getInstance().getConfigBooleanValue("isWebSealServer", false);

        if (isWebSealServer && userName == null) {

            String subsystem = "sany-mms";

            try// uim检测
            {
                CommonInfo info = new CommonInfo();
                UimUserInfo userinfo = null;
                String ip = "";
                userinfo = info.validateUIM(request);
                ip = userinfo.getUser_ip();
                userName = userinfo.getUser_name();
                AccessControl control = AccessControl.getInstance();
                control.checkAccess(request, response, false);
                String user = control.getUserAccount();
                request.setAttribute("fromsso", "true");

                if (user == null || "".equals(user) || !userName.equals(user)) {

                    try {
                        if (!userName.equals(user))
                            control.resetSession(session);
                        String password = SSOUserMapping.getUserPassword(userName);
                        if (password == null)
                            throw new AccessException("用户" + userName + "不存在。");
                        control = AccessControl.getInstance();
                        control.login(request, response, userName, password);

                        if (StringUtil.isEmpty(successRedirect)) {
                            Framework framework = Framework.getInstance(control.getCurrentSystemID());
                            MenuItem menuitem = framework.getMenuByID(menuid);
                            if (menuitem instanceof Item) {

                                Item menu = (Item) menuitem;
                                successRedirect = MenuHelper.getRealUrl(contextpath,
                                        Framework.getWorkspaceContent(menu, control), MenuHelper.sanymenupath_menuid,
                                        menu.getId());
                            } else {

                                Module menu = (Module) menuitem;
                                String framepath = contextpath + "/sanydesktop/singleframe.page?"
                                        + MenuHelper.sanymenupath + "=" + menu.getPath();
                                successRedirect = framepath;
                            }
                            AccessControl.recordIndexPage(request, successRedirect);
                        } else {
                            successRedirect = URLDecoder.decode(successRedirect);
                        }
                        response.sendRedirect(successRedirect);
                        return;
                    } catch (Exception e) {
                        log.info("", e);
                        String msg = e.getMessage();
                        if (msg == null)
                            msg = "";
                        response.sendRedirect(contextpath + "/webseal/websealloginfail.jsp?userName=" + userName
                                + "&ip=" + ip + "&errormsg=" + java.net.URLEncoder.encode(msg, "UTF-8"));
                        return;
                    }

                } else {
                    control.resetUserAttributes();
                    if (StringUtil.isEmpty(successRedirect)) {
                        Framework framework = Framework.getInstance(control.getCurrentSystemID());
                        MenuItem menuitem = framework.getMenuByID(menuid);
                        if (menuitem instanceof Item) {

                            Item menu = (Item) menuitem;
                            successRedirect = MenuHelper.getRealUrl(contextpath,
                                    Framework.getWorkspaceContent(menu, control), MenuHelper.sanymenupath_menuid,
                                    menu.getId());
                        } else {

                            Module menu = (Module) menuitem;
                            String framepath = contextpath + "/sanydesktop/singleframe.page?" + MenuHelper.sanymenupath
                                    + "=" + menu.getPath();
                            successRedirect = framepath;
                        }
                        AccessControl.recordIndexPage(request, successRedirect);
                    } else {
                        successRedirect = URLDecoder.decode(successRedirect);
                    }
                    response.sendRedirect(successRedirect);
                    return;
                }

            } catch (Exception e)// 检测失败,继续平台登录
            {
                log.info("", e);
            }

        }

        else {
            try {
                AccessControl control = AccessControl.getInstance();
                control.checkAccess(request, response, false);
                String user = control.getUserAccount();

                worknumber = control.getUserAttribute("userWorknumber");
                boolean issameuser = false;
                if (loginType.equals("2")) {
                    if (worknumber != null && !worknumber.equals(""))
                        issameuser = userName.equals(worknumber);
                } else {
                    if (user != null && !user.equals(""))
                        issameuser = userName.equals(user);
                }

                if (user == null || "".equals(user) || !issameuser) {

                    if (!issameuser) {
                        control.resetSession(session);
                    }

                    try {
                        // 1-域账号登录 2-工号登录
                        String password = null;
                        if (loginType.equals("1")) {

                            password = SSOUserMapping.getUserPassword(userName);
                            if (password == null)
                                throw new AccessException("用户" + userName + "不存在。");
                        } else {
                            java.util.Map data = SSOUserMapping.getUserNameAndPasswordByWorknumber(userName);
                            if (data == null)
                                throw new AccessException("工号为" + userName + "的用户不存在。");
                            userName = (String) data.get("USER_NAME");
                            password = (String) data.get("USER_PASSWORD");
                        }
                        control = AccessControl.getInstance();
                        request.setAttribute("fromsso", "true");
                        // System.out.println("-----------userName="+userName+",password="+password);
                        control.login(request, response, userName, password);
                        if (StringUtil.isEmpty(successRedirect)) {
                            Framework framework = Framework.getInstance(control.getCurrentSystemID());
                            MenuItem menuitem = framework.getMenuByID(menuid);
                            if (menuitem instanceof Item) {

                                Item menu = (Item) menuitem;
                                successRedirect = MenuHelper.getRealUrl(contextpath,
                                        Framework.getWorkspaceContent(menu, control), MenuHelper.sanymenupath_menuid,
                                        menu.getId());
                            } else {

                                Module menu = (Module) menuitem;
                                String framepath = contextpath + "/sanydesktop/singleframe.page?"
                                        + MenuHelper.sanymenupath + "=" + menu.getPath();
                                successRedirect = framepath;
                            }
                            AccessControl.recordIndexPage(request, successRedirect);
                        } else {
                            successRedirect = URLDecoder.decode(successRedirect);
                        }
                        response.sendRedirect(successRedirect);
                        return;
                    } catch (Exception e) {
                        log.info("", e);
                        String msg = e.getMessage();
                        if (msg == null)
                            msg = "";
                        response.sendRedirect(contextpath + "/webseal/websealloginfail.jsp?userName=" + userName
                                + "&errormsg=" + java.net.URLEncoder.encode(msg, "UTF-8"));
                        return;
                    }

                } else {
                    control.resetUserAttributes();
                    if (StringUtil.isEmpty(successRedirect)) {
                        Framework framework = Framework.getInstance(control.getCurrentSystemID());
                        MenuItem menuitem = framework.getMenuByID(menuid);
                        if (menuitem instanceof Item) {

                            Item menu = (Item) menuitem;
                            successRedirect = MenuHelper.getRealUrl(contextpath,
                                    Framework.getWorkspaceContent(menu, control), MenuHelper.sanymenupath_menuid,
                                    menu.getId());
                        } else {

                            Module menu = (Module) menuitem;
                            String framepath = contextpath + "/sanydesktop/singleframe.page?" + MenuHelper.sanymenupath
                                    + "=" + menu.getPath();
                            successRedirect = framepath;
                        }
                        AccessControl.recordIndexPage(request, successRedirect);
                    } else {
                        successRedirect = URLDecoder.decode(successRedirect);
                    }
                    response.sendRedirect(successRedirect);
                    return;
                }

            } catch (Throwable ex) {
                log.info("", ex);
                String errorMessage = ex.getMessage();
                if (errorMessage == null)
                    errorMessage = "";

                try {
                    FileCopyUtils.copy(errorMessage + "," + userName + "登陆失败，请确保输入的用户名和口令是否正确！",
                            new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
                } catch (IOException e) {
                    log.info("", e);
                }

            }
        }

    }

}
