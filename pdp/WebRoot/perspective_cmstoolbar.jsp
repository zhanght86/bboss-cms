<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
   accesscontroler.checkAccess(request,response); 
   String userId = accesscontroler.getUserID();

    String menuid = StringTool.getParameter(request, "menuid", "sitemenu");
    String sysid = StringTool.getParameter(request, "sysid");
    String show = WebberResources.getString("show");
    int    firmlevel = sys.getFirmLevel();
    String level;
    if(firmlevel == 0)
        level = "超级网站";
    else if(firmlevel == 1)
        level = "高级网站";
    else
        level = "初级网站";

    //读取cookie
    String    showindex = "";
    Cookie [] allname = request.getCookies();
    if(allname != null)
    {
        for(int i = 0; i < allname.length; i++)
        {
            if(showindex.length() == 0 && allname[i].getName().equals("wbshow"))
            {
                showindex = allname[i].getValue();
            }
        }
    }
%>
<%
	String adminname = StringTool.escapeHTMLTags(sys.getUserName());
    /**if(menuid.equals("sysmenu") && sys.isAdmin())	//后台管理
    {
        if(sys.getAdmintype() == 0)
            adminname += "(超级管理员)";
        else
            adminname += "(普通管理员)";
    }*/
%>
<html>
<head>
    <style>
        A.menu
        {
            text-decoration: none;
            color:           black
        }
        A.menu:link
        {
            text-decoration: none;
            color:           black
        }
        A.menu:visited
        {
            text-decoration: none;
            color:           black
        }
        A.menu:hover
        {
            text-decoration: underline;
            color:           red
        }
        A.cur
        {
            text-decoration: none;
            color:           red
        }
        A.cur:link
        {
            text-decoration: none;
            color:           red
        }
        A.cur:visited
        {
            text-decoration: none;
            color:           red
        }
        A.cur:hover
        {
            text-decoration: underline;
            color:           red
        }
    </style>
    <script language="javascript" src="/system/_js/function.js"></script>
    <script language="javascript" src="/system/_js/openwin.js"></script>
    <script language="JavaScript" src="/system/_js/ajax.js"></script>
    <script language = "javascript">
        var curid = "<%=(sysid.length() == 0?"":sysid)%>";

        function changecur(o)
        {
            ongo();

            if(curid == o.id)
                return;

            if(curid != "")
                document.all(curid).className = 'menu';

            curid = o.id;
            o.className = 'cur';
        }

        var funstr; //执行串

        function execfun()
        {
            eval(funstr);
        }

        function dofunction(str) //执行命令
        {
            funstr = str;
            bridge.attachEvent("onclick", execfun);
            bridge.click();
            bridge.detachEvent("onclick", execfun);
        }

        var show = true;

        function switchshow(o) //切换菜单显示样式
        {
            if(show)
            {
                parent.root.rows = "20, *";
                document.all.tb1.style.display = "none";
                o.src = "../images/ico_n03.gif";
                o.title = "完整显示主菜单";
            }
            else
            {
                document.all.tb1.style.display = "inline";
                parent.root.rows = "50, *";
                o.src = "../images/ico_n01.gif";
                o.title = "精简显示主菜单";
            }

            show = !show;
        }

        function showsitetree()
        {
            document.all.showtree[0].style.display = "none";
            document.all.showtree[1].style.display = "none";
            parent.main.tab.showleft();
        }

        function onhidesitetree()
        {
            document.all.showtree[0].style.display = "inline";
            document.all.showtree[1].style.display = "inline";
        }

        function ongo()
        {
            document.all.showtree[0].style.display = "none";
            document.all.showtree[1].style.display = "none";
        }

        function  onget()  
        {
            if(xmlhttp.readystate == 4)
            {
                if(xmlhttp.status == 200)
                {
                    try
                    {
                        feedlist();
                    }
                    catch(e)
                    {
                    }
                }
                else
                {
                    
                }
                setTimeout(getNews, 60 * 60 * 1000);
            }
        }
        
        function feedlist()
        {
            var  out="";
            var  xmldoc  =  xmlhttp.responseXML;  
        
            //channel
            var xmlchannel = getXmlChild(xmldoc, "channel");
            var xmlctitle = getXmlData(xmlchannel, "title");
            var xmlcdescription = getXmlData(xmlchannel, "title", "");
            var xmlclink = getXmlData(xmlchannel, "link");
            
            //document.getElementById('title').innerHTML  =  "<a href=\"" + escapeForValue(xmlclink) + "\" title=\"" + escapeForValue(xmlcdescription) + "\"  target='_blank'>" + xmlctitle + "</a>";
            
            //img
            var xmlimage = getXmlChild(xmlchannel, "image");
            if(xmlimage != null)
            {
                var imgtitle = getXmlData(xmlimage, "title");
                var imglink = getXmlData(xmlimage, "link");
                var imgurl = getXmlData(xmlimage, "url");
                
            //document.getElementById('image').innerHTML  =  "<a href=\"" + escapeForValue(imglink) + "\" target='_blank'><img border=0 src=\"" + escapeForValue(imgurl) + "\" title=\"" + escapeForValue(imgtitle) + "\"></a>";
                
            }
            else
            {
            //document.getElementById('image').innerHTML = "";
            }
            
            var items = xmldoc.getElementsByTagName("item");
            
            for(i=0;i<items.length;i++)
            {
                var itemtitle = getXmlData(items[i], "title");
                var itemlink = getXmlData(items[i], "link");
                var itemdescription = getXmlData(items[i], "description", "");
                //var itemcategory = getXmlData(items[i], "category", null);
                var itempubDate = getXmlData(items[i], "pubDate", "");
                try
                {
                    var itemDate = new Date(itempubDate);
                    if(!isNaN(itemDate))
                        itempubDate = itemDate.toLocaleString();
                }
                catch(e)
                {
                }
                
                out = out + "<span style='color:red'>?</span>"  + 
                      "<a href=\"" + escapeForValue(itemlink) +"\"  target='_blank' style='font-size:9pt;color:blue;text-decoration:none' title=\"" + escapeForValue(itemdescription) + "\">" + 
                      itemtitle + "</a>&nbsp;";
                document.getElementById('rssnews').innerHTML  =  out;
            }  
        }
        
        function getNews()
        {
            <%
            if(VsbResources.getInt("product.vsbclubnews.enable", 1) == 1)
            {
            %>
            startRequest('/system/site/proxy.jsp?url=' + escape('<%=VsbResources.getString("product.vsbclubnews.url", "http://www.vsbclub.com/productnews.xml")%>'), onget);
            <%
            }
            %>
        }
        
        function resizenews()
        {
            var w = toptd.clientWidth - 10;
            if(w <= 0)
                w = 5
            rssnews.width = w ;
        }
        
    </script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="../_style/style.css" rel="stylesheet" type="text/css">
</head>

<body topmargin="0" leftmargin="0" onload="resizenews();getNews();">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" id=tb1>
        <font id="bridge" style="display:none"></font>
        <tr>
            <td width="200" nowrap
                background="../images/vsb2006_bg_04_4.jpg"><img src="../images/product/productlogo.jpg" width="200"
                height="30"></td>
            <td width="49" nowrap
                background="../images/vsb2006_bg_04_2.jpg"><img src="../images/vsb2006_bg_04_1.jpg" width="40"
                height="30"></td>
            <td width="1%" background="../images/vsb2006_bg_04_2.jpg" nowrap>
                欢迎您，<%=adminname%>&nbsp;&nbsp;
            <td>
            <td width="100%" background="../images/vsb2006_bg_04_2.jpg" nowrap id=toptd onresize='resizenews()'>
                <marquee width="5" height="10" direction="left" 
                        scrollamount="1" scrolldelay="50" onMouseOver="this.stop()" onMouseOut="this.start()" id="rssnews"></marquee></td>
            <td width="200" nowrap background="../images/vsb2006_bg_04_6.jpg"><%if(VsbGlobals.productvsbclublinkenable.equals("1")){%><span width=100%
                align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://www.vsbclub.com" target="_blank"
                style="font-weight:bold;">访问VSBCLUB产品网站</a></span><%}%></td>
        </tr>
    </table>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" id=tb2>
        <tr>
            <td width="228" nowrap background="../images/vsb2006_bg_04_5.jpg"><img src="../images/product/productname.jpg" width="200" height="20"></td>
            <td width="100%" nowrap background="../images/vsb2006_bg_04_3.jpg"><table height="20" border="0" cellpadding="0" cellspacing="0">
                    <tr align="center">
                        <td width="60%" nowrap><span class="style7">
                            <img src="/system/images/ico_n04.gif" id="showtree" align="absmiddle" title="显示树状目录" style="cursor:hand;display:none;position:relative;top:-1px" onclick="showsitetree();"><font id="showtree" style="display:none">&nbsp;</font>
                            <%
                                menu.init(request, menuid);
                                int inum = showindex.equals("1")?1:2;
                                if(menuid.equals("appmenu"))
                                {
                                    String sql = "select wbtype,wbfirm.wbfirmid,wblevel,wbownerpath,wbaccount,wbdbname,wbname from wbfirm,wbadmin where wbadmin.wbfirmid=wbfirm.wbfirmid and wbfirm.wbtypeid=-1 and  wbadmin.wbsysuserid=" + sys.getUserid();

                                    Vector vec = BasePeer.executeQuery(sql, "vsb");
                                    Record record = null;
                                    if(vec != null)
                                    {
                                        String dbname = "";
                                        String firmname = "";
                                        int firmlevel1 = 0;
                                        int firmid = 0;
                                        String ownername = "";
                                        String usertype = "";
                                        StringBuffer term = new StringBuffer();
                                        for(int ii = 0; ii < vec.size(); ii++)
                                        {
                                            record = (Record)vec.elementAt(ii);
                                            dbname = record.getValue(6).asString();
                                            firmname = record.getValue(7).asString();
                                            firmlevel1 = record.getValue(3).asInt(1);
                                            firmid = record.getValue(2).asInt();
                                            ownername = record.getValue(5).asString();
                                            usertype = record.getValue(1).asString();
                                            term.append("&dbname=");
                                            term.append(dbname);
                                            term.append("&firmname=");
                                            term.append(firmname);
                                            term.append("&firmlevel=");
                                            term.append(firmlevel1);
                                            term.append("&firmid=");
                                            term.append(firmid);
                                            term.append("&ownername=");
                                            term.append(ownername);
                                            term.append("&usertype=");
                                            term.append(usertype);

                                            for(int i = 1; i <= menu.getSize(); i++)
                                            {
                                                String name = menu.getChildName(i - 1);

                                                if(name.equals(ownername))
                                                {
                                                    if(ii > 0)
                                                        out.print("&nbsp;|&nbsp;");
                            %><a href="<%=menu.getChild(i - 1, "HREF")%>?sysid=<%=name%><%=term.toString()%>" id="<%=name%>" class="<%=(sysid.equals(name)?"cur":"menu")%>" onclick="changecur(this)" target="<%=menu.getChild(i - 1, "TARGET")%>">
                                                        <%=menu.getChild(i - 1, "TITLE")%> </a>
                                                        <%
                                                            break;
                                                }
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    for(int i = 1; i <= menu.getSize(); i++)
                                    {
                                        String name = menu.getChildName(i - 1);

                                        if(i > 1)
                                            out.print("&nbsp;|&nbsp;");
                                                        %><a href="<%=menu.getChild(i - 1, "HREF")%>?sysid=<%=name%>" id="<%=name%>" class="<%=(sysid.equals(name)?"cur":"menu")%>" onclick="changecur(this)" target="<%=menu.getChild(i - 1, "TARGET")%>"><%=menu.getChild(i - 1, "TITLE")%></a>
                                            <%
                                    }
                                                        %>
                            <%
                                }
                            %></span></td>
                    </tr>
                </table></td>
            <td nowrap width="76" align="right" valign="top" background="../images/vsb2006_bg_04_3.jpg"><img src="../images/ico_n01.gif" width="16" height="16" border="0" align="absmiddle" style="cursor:hand" title="精简显示主菜单" onclick="switchshow(this)">&nbsp;<span style="position:relative;top:1px ">|</span>&nbsp;<a href="<%=sys.getLoginPage()%>" class="menu" target="_top" title="退出系统"><span style="position:relative;top:1px ">退出</span></a>&nbsp;</td>
        </tr>
    </table>

    <iframe style="display:none" src='noop.jsp' width='0' height='0'>
    </iframe>
</body>
</html>

