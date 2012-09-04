<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.net.URLDecoder"%>
<%
    //可见关系维护-----------------------------------------------    
    AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	DictManager dictManager = new DictManagerImpl();
    
    String dicttypeId = request.getParameter("dicttypeId");
    //用户授权的下级或本级机构ID
    String orgId = request.getParameter("orgId");
    String state = request.getParameter("state");
    String data_org = request.getParameter("data_org");
    //用户所属机构ID
    String curOrg = accesscontroler.getChargeOrgId();
    String hintStr = "";
    if(!data_org.equals("") && data_org != null){//如果字典设置了机构字段
    	if(state.equals("read")){
	    	String dictdataValues = request.getParameter("dictdataValues");
	    	String[] dictdataValue = dictdataValues.split(",");
	    	if(accesscontroler.isAdmin()){
	    		dictManager.storeReadOrgTaxcodeorAdmin(orgId,dicttypeId,dictdataValue);
	    	}else{
	    		dictManager.storeReadOrgTaxcode(curOrg,orgId,dicttypeId,dictdataValue);
	    	}
	    	hintStr = "saveRead";
	    }
	    if(state.equals("allRead")){
	    	String identifier = request.getParameter("identifier");
	    	String[] dictAlldataValue = null;
	    	if(accesscontroler.isAdmin()){
	    		dictAlldataValue = dictManager.getDictdataValues(dicttypeId,orgId,identifier,true,"");
	    		dictManager.storeReadOrgTaxcodeorAdmin(orgId,dicttypeId,dictAlldataValue);
	    	}else{
	    		//根据用户ID得到getCurOrgSelfGatherDictDataValues
	    		if(orgId.equals(curOrg)){
	    			dictAlldataValue = dictManager.getCurOrgSelfGatherDictDataValues(curOrg,dicttypeId,identifier);
	    		}else{
	    			dictAlldataValue = dictManager.getDictdataValues(dicttypeId,orgId,identifier,false,accesscontroler.getUserID());
	    		}
	    		dictManager.storeReadOrgTaxcode(curOrg,orgId,dicttypeId,dictAlldataValue);
	    	}
	    	hintStr = "saveAllRead";
	    }
    }else{//字典没有设置机构字段
	    if(state.equals("read")){
	    	String dictdataValues = request.getParameter("dictdataValues");
	    	String[] dictdataValue = dictdataValues.split(",");
	    	dictManager.storeReadOrgTaxcode(orgId,dicttypeId,dictdataValue);
	    	hintStr = "saveRead";
	    }
	    if(state.equals("allRead")){
	    	String identifier = request.getParameter("identifier");
	    	String[] dictAlldataValue = null;
	    	if(accesscontroler.isAdmin()){
	    		dictAlldataValue = dictManager.getDictdataValues(dicttypeId,orgId,identifier,true,"");
	    	}else{
	    		//根据用户ID得到
	    		dictAlldataValue = dictManager.getDictdataValues(dicttypeId,orgId,identifier,false,accesscontroler.getUserID());
	    	}
	    	dictManager.storeReadOrgTaxcode(orgId,dicttypeId,dictAlldataValue);
	    	hintStr = "saveAllRead";
	    }
    }
%>
<script language = "javascript">
<%if(hintStr.equals("saveRead")){%>
	alert("保存未设置项成功！");
	parent.document.location.href = parent.document.location.href;
	parent.parent.refurbishIframe();  
<%}%>

<%if(hintStr.equals("saveAllRead")){%>
	alert("保存全部未设置项成功！");
	parent.document.location.href = parent.document.location.href;
	parent.parent.refurbishIframe();  
<%}%>

<%if(hintStr.equals("")){%>
	alert("操作失败！");
<%}%>
</script>
