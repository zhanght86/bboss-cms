package com.frameworkset.platform.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

public class FileOperate implements Serializable
{
  private static final int EOF = -1;

  public static StringBuffer getStringBufferFromFile(String fileFullName,
      int bufferSize) throws IOException
  {
    StringBuffer sb = new StringBuffer();
    File f = new File(fileFullName);
    FileInputStream fis = new FileInputStream(f);
    BufferedInputStream bis = null;
    try{
        bis = new BufferedInputStream(fis);
        byte[] b = new byte[bufferSize];
        int readedBytes = 0;
        while( (readedBytes=bis.read(b,0,bufferSize)) != EOF )
        {
          if(readedBytes==bufferSize)
          {
            sb.append(new String(b));
          }else{
            byte[] tmp = new byte[readedBytes];
            System.arraycopy(b,0,tmp,0,readedBytes);
            sb.append(new String(tmp));
          }
        }
    }finally{
      if(bis!=null) bis.close();
    }

    return sb;
  }

  public static StringBuffer getStringBufferFromFile(String fileFullName)
      throws IOException
  {
    return getStringBufferFromFile(fileFullName,8092);
  }

  public static void main(String[] args) throws IOException
  {
    StringBuffer sb = FileOperate.getStringBufferFromFile("D:\\jboss4.0\\server\\default\\deploy\\HndsReport.ear\\ReportEjb.jar\\1.txt",8092);
    System.out.println(sb);
  }
}
