package com.frameworkset.platform.test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.frameworkset.util.VelocityUtil;

public class VelocityTest implements Serializable{
	org.apache.velocity.context.Context vcontext = new VelocityContext();
	Template template = VelocityUtil.getTemplate("publish/jsp_generator.vm");
	
	public static void main(String[] args)
	{
		try
		{
			VelocityUtil.getTemplate("publish/jsp_generator.vm");
//			Velocity.init();
			String templateFile = "d:/1.sql";
			String jspFile = "d:/3.sql";
			Reader reader =  new FileReader(templateFile);
			Writer fileWriter =  new StringWriter();
			Context vcontext = new VelocityContext();
			Velocity.evaluate(vcontext,fileWriter,"",reader);
			fileWriter.flush();
			fileWriter.close();
			reader.close();
			System.out.println(fileWriter.toString());
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ParseErrorException e)
		{
			e.printStackTrace();
		}
		catch(MethodInvocationException e)
		{
			e.printStackTrace();
		}
		catch(ResourceNotFoundException e)
		{
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
