<%@page import="java.net.InetAddress"%>
<%
        InetAddress bind_addr = null;
        InetAddress[] interfaces=InetAddress.getAllByName(InetAddress.getLocalHost().getHostAddress());
        if(interfaces != null && interfaces.length > 0)
        {
            for(int i = 0; i < interfaces.length; i ++)
            {
                bind_addr=interfaces[0];
                out.println("interfaces["+i + "]:"+interfaces[i].getHostAddress());
            }
        }
        
        if(bind_addr == null)
        {
            bind_addr=InetAddress.getLocalHost();
        }
        out.println("InetAddress.getLocalHost():" + InetAddress.getLocalHost().getHostAddress());
        out.println("bind_addr:" + bind_addr.getHostName());
        try
        {
             bind_addr=InetAddress.getByName("jbosscluster");
             out.println("bind_addr jbosscluster  getHostName:" + bind_addr.getHostName());
             out.println("bind_addr jbosscluster getHostAddress:" + bind_addr.getHostAddress());
         }
         catch(Exception e)
         {
            e.printStackTrace();
         }
         
         
        
%>
