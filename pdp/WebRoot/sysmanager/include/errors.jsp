<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="org.apache.struts.action.*,java.util.Iterator" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%
    ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
    StringBuffer results = new StringBuffer();
    String message = null;
%>
<% if (errors ==null || errors.isEmpty()) {%>
<script language="JavaScript">
function showErrors() {
    alert("<bean:message key='global.prompt.no_errors'/>\n<bean:message key='global.prompt.notice'/>\n<bean:message key='global.prompt.asterisk'/>");
}
<%} else {
    Iterator reports = errors.get();
    while (reports.hasNext()) {
        ActionError report = (ActionError) reports.next();
        message =
                RequestUtils.message(
                    pageContext,
                    null,
                    Globals.LOCALE_KEY,
                    report.getKey(),
                    report.getValues());

        if (message != null) {
            results.append(message);
            if (reports.hasNext()) results.append("\\n");
        }
    }
%>
<script language="JavaScript">
function showErrors() {
    alert("<%=results%>");
}
showErrors();
</script>
<%}%>
</script>
