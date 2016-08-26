package com.frameworkset.platform.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.frameworkset.util.RegexUtil;

public class DefaultSearch extends Search {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public String[] searchway(String condition,String datasource) throws IOException 
	  {
		 
		  String data = "";
		  String condition1 = condition;
		  String datasource1=datasource;
		  
			  try{
				File f = new File(datasource1);
				if(f.exists())
				{
					BufferedReader in = null;
					StringBuffer sb = new StringBuffer();
					String s = null;
					try {
						in = new BufferedReader(new FileReader(f));
						boolean flag = false;
						while ((s = in.readLine()) != null)
						{
							if(!flag)
							{
								sb.append(s);
								flag = true;
							}
							else
								sb.append("\\n").append(s);
							
						}
						data = sb.toString();
					} finally {
						if (in != null)
							in.close();
					}
					
					
				    //System.out.println("data:" + data);
				  }
			}
			catch(FileNotFoundException e){
				e.printStackTrace();
				System.out.println("File Not Found");
				return null;
			}catch(ArrayIndexOutOfBoundsException e){
				e.printStackTrace();
				System.out.println("Usage:ShowFile File");
			    return null;		   
			}////////////////////////////////////////catch2
		    String regexp = "([^;]*" + condition1 + "[^;]*);";
			String result1[] = RegexUtil.containWithPatternMatcherInput(data,regexp);
			return result1;

		}

}
