
package com.frameworkset.platform.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * 根据远程地址和本地文件地址，将远程文件下载到本地
 * @作者 biaoping.yin 
 * @日期 2006-12-31 8:47:31
 * @版本 v1.0
 * @版权所有 三一集团
 */
public class RemoteFileHandle implements Serializable
{ 
	private String remoteUrl;
    private String parentPath;
    private String fileName;
    
    private void setPathInfo(String savepath)
    {
    	int idx = savepath.lastIndexOf('/');
    	if(idx == -1)
    		idx = savepath.lastIndexOf('\\');
    	if(idx == -1) 
    	{
    		parentPath = "";
    		this.fileName = savepath;
    	}
    	else
    	{
    		parentPath = savepath.substring(0,idx);
    		
    		this.fileName = savepath.substring(idx + 1);
    	}
    }

    public RemoteFileHandle(String remoteUrl,String savepath)
    {
    	this.remoteUrl = remoteUrl;
    	this.setPathInfo(savepath);
    }
    
    public RemoteFileHandle(String remoteUrl,String parentPath,String fileName)
    {
    	this.remoteUrl = remoteUrl;
    	this.parentPath = parentPath;
    	this.fileName = fileName;
    }
    
    public static void main(String args[])
    {
//    	RemoteFileHandle test = new RemoteFileHandle("http://202.197.40.230:6060/cruisecontrol/artifacts/frameworkset/20061231051307/frameworkset.jar","d:/frameworkset.jar");
//        RemoteFileHandle test = new RemoteFileHandle("http://202.197.40.230:6060/cruisecontrol/artifacts/frameworkset/20061231051307/frameworkset使用说明.txt","d:/frameworkset使用说明.txt");
        RemoteFileHandle test = new RemoteFileHandle("http://202.197.40.230:6060/cruisecontrol/artifacts/frameworkset/20061231051307/frameworkset-src.zip","d:/frameworkset-src.zip");
        
    	test.download();
    	
    }
    
    

    public boolean download()
    {
        
        URLConnection urlconnection;
        
        InputStream inputstream = null;
        FileOutputStream fileoutputstream = null;
        try
        {
        	URL url = new URL(this.remoteUrl);
	        urlconnection = url.openConnection();
	        urlconnection.connect();
	        HttpURLConnection httpurlconnection = (HttpURLConnection)urlconnection;
	        int i = httpurlconnection.getResponseCode();
	        
	        if(i != 200)
	        {
	            
		        System.out.println("Connect to " +this.remoteUrl + " failed,return code:" + i);
		        return false;
	        }
       
	        int j = urlconnection.getContentLength();
            String contentType = urlconnection.getContentType();
            
	        if(j < 0 && !contentType.endsWith("application/java-archive"))
	        	return false;
//	        int k = 0;
	        
	        inputstream = urlconnection.getInputStream();
//	        byte abyte0[] = new byte[1024];
	        File temp = new File(this.parentPath);
	        if(!temp.exists())
	        	temp.mkdirs();
	        File file = new File(parentPath,this.fileName);
	        if(!file.exists())
	            file.createNewFile();
	        
	        fileoutputstream = new FileOutputStream(file);
	        byte[] imgsteam = new byte[1];
	        while (inputstream.read(imgsteam, 0, 1) > 0)
	        {
	        	//System.out.println(imgsteam[0]);
	        	fileoutputstream.write(imgsteam, 0, 1);
	        }
	        fileoutputstream.flush();
	        fileoutputstream.close();
	        inputstream.close();
	        return true;
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	return false;
        }
        finally
        {
        	
        	if(inputstream != null)
				try {
					inputstream.close();
					inputstream = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(fileoutputstream != null)
				try {
					fileoutputstream.close();
					fileoutputstream = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	
        }

//        int k = 0;
//        if(j < 0)
//        {
//            while(k > -1) 
//            {
//                k = inputstream.read(abyte0);
//                if(k > 0)
//                    fileoutputstream.write(abyte0, 0, k);
//            }           
//        }
//        int l;
//        for(l = 0; l < j && k != -1;)
//        {
//            k = inputstream.read(abyte0);
//            if(k > 0)
//            {
//                fileoutputstream.write(abyte0, 0, k);
//                l += k;
//            }
//        }
//
//        if(l >= j)
//            
//        System.out.println("download error");
//        inputstream.close();
//        fileoutputstream.close();
//        file.delete();
//        return false;
//        fileoutputstream.flush();
//        fileoutputstream.close();
//        inputstream.close();
//       
//       
//       
//        return false;
//        return true;
    }

}