/*
 * 创建日期 2005-7-2
 *
 */
package com.frameworkset.platform.smtp.mail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.activation.DataSource;

/**
 * @author JForever
 * 主页:http://www.open-open.com
 */
public class CustomDataSource implements DataSource ,Serializable{
    
   private  byte[] data;
	private String type;
	private String name;
	
    public CustomDataSource(byte[] data,String type,String name)
    {
        this.data = data;
        this.type = type;
        this.name = name;
    }

    public String getContentType() 
    {
        return type;
    }


    public InputStream getInputStream() throws IOException {
        
        if (data == null)
			throw new IOException("No data.");

		return new ByteArrayInputStream(data);
    }


    public String getName() 
    {
        try {
            return new String(name.getBytes(),"ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public OutputStream getOutputStream() throws IOException {
        throw new IOException("Not supported.");
    }

}
