package com.frameworkset.platform.cms.searchmanager;

import java.util.StringTokenizer;

public class CMSKeyWord implements java.io.Serializable
{	
	private String strKey;

    public CMSKeyWord(String strKey)
    {
    	this.strKey=strKey;
    }
    
    /**
     * 显示截取的内容，加上修饰
     * @param s
     * @return
     */
    public String display(String s)
    {
    	StringTokenizer st = new StringTokenizer(strKey,"&");
        String[] keys=new String[st.countTokens()];        
            for(int n=0;st.countTokens() > 0;n++){
        	keys[n]=st.nextToken();        	
        }        
        int i = 0;
        for(int j = 0; j < keys.length; j++)
        {
            String s2 = keys[j];
            if(i < s2.length())
            {
                i = s2.length();
            }
        }

        String s3 = "";
        for(int k = 0; k < s.length() - i;)
        {
            boolean flag = false;
            String s4 = "";
            for(int l = 0; l <  keys.length; l++)
            {
                String s5 = keys[l];
                if(s.substring(k, k + s5.length()).equals(s5))
                {
                    flag = true;
                    s4 = s5;
                }
            }

            if(!flag)
            {
                s3 = s3 + s.substring(k, k + 1);
                k++;
            } else
            {
                s3 = s3 + "<FONT color=red>" + s4 + "</FONT>";
                k += s4.length();
            }
        }
        return s3;
    }   
}
