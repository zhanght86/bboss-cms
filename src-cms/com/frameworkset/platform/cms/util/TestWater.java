package com.frameworkset.platform.cms.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class TestWater implements java.io.Serializable {
	public static boolean genWaterImage(String   filePath,   String   watermark)
	{   
		  //package   net.wayne.images;   
		   
		 
		      //添加水印,filePath   源图片路径，   watermark   水印图片路径   
		        
		          ImageIcon   imgIcon   =   new   ImageIcon(filePath);   
		          Image   theImg   =   imgIcon.getImage();   
		          ImageIcon   waterIcon   =   new   ImageIcon(watermark);   
		          Image   waterImg   =   waterIcon.getImage();   
		          int   width   =   theImg.getWidth(null);   
		          int   height   =   theImg.getHeight(null);   
		          BufferedImage   bimage   =   new   BufferedImage(width,height,   
		                                                                                            BufferedImage.TYPE_INT_RGB);   
		          Graphics2D   g   =   bimage.createGraphics();   
		          g.setColor(Color.red);   
		          g.setBackground(Color.white);   
		          g.drawImage(theImg,   0,   0,   null);   
		          g.drawImage(waterImg,200,200,null);   
		          g.drawString("www.lighting86.com.cn",   10,   10);   //添加文字   
		          g.dispose();   
		          try   {   
		              FileOutputStream   out   =   new   FileOutputStream(filePath);   
		              JPEGImageEncoder   encoder   =   JPEGCodec.createJPEGEncoder(out);   
		              JPEGEncodeParam   param   =   encoder.getDefaultJPEGEncodeParam(bimage);   
		              param.setQuality(50f,   true);   
		              encoder.encode(bimage,   param);   
		              out.close();   
		          }   
		          catch   (Exception   e)   {   
		              return   false;   
		          }   
		          return   true;   
		         
		    
		 

	}
	
	  public   static   void   main(String[]   args)   {   
             
		  
		  TestWater.genWaterImage("D:\\workspace\\cms\\creatorcms\\sitepublish\\site200\\test\\content_files\\89e057ee-cd43-4f1a-9aff-9166a8fbe34c.jpg",
				  //"D:\\workspace\\cms\\creatorcms\\sitepublish\\site200\\test\\content_files\\54a9ae7d-0d99-4608-816b-70b2833258a7.jpg");
				  "d:/012.png");
      }   

}
