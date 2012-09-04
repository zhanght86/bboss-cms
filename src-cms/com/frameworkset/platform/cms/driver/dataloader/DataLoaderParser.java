package com.frameworkset.platform.cms.driver.dataloader;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;

import com.frameworkset.platform.config.ConfigParser;
import com.frameworkset.util.StringUtil;

public class DataLoaderParser extends HandlerBase implements java.io.Serializable{
	private static Logger log = Logger.getLogger(ConfigParser.class) ;
//    private Stack traceStack;
    private StringBuffer currentValue;
    
    private Map dataLoaders;
    

    public DataLoaderParser() {
//        traceStack = new Stack();
    	dataLoaders = new HashMap();
        currentValue = new StringBuffer();
    }



    public void startElement(String name, AttributeList attributes) {
        currentValue.delete(0, currentValue.length());
        if(name.equals("dataloader"))
        {
        	DataLoaderInfo loader = new DataLoaderInfo();
            String _name = (String)attributes.getValue("name");
            loader.setName(_name);
            boolean _single = StringUtil.getBoolean(attributes.getValue("single"),false);
            loader.setSingle(_single);
            
            String _type = attributes.getValue("type");
            loader.setType(_type);
            boolean _recursive = StringUtil.getBoolean(attributes.getValue("recursive"),false);
            loader.setRecursive(_recursive);
            dataLoaders.put(_name,loader);
            
        }
        
        
        
        

    }
    

    public void characters(char[] ch, int start, int length) {
        currentValue.append(ch, start, length);
    }

    public void endElement(String name) {

        
        



    }



	public Map getDataLoaders() {
		return dataLoaders;
	}

    

}
