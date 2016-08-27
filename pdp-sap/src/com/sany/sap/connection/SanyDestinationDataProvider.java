/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.sany.sap.connection;

import java.util.HashMap;
import java.util.Properties;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

/**
 * <p> SanyDestinationDataProvider.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-9-24 下午6:15:19
 * @author biaoping.yin
 * @version 1.0
 */
public class SanyDestinationDataProvider  implements DestinationDataProvider {
	private DestinationDataEventListener destinationDataEventListener; 
	private HashMap<String, Properties> destinations=new HashMap<String, Properties>(); 
	
    public SanyDestinationDataProvider(){
    	
    }
    
	public Properties getDestinationProperties(String arg0) {
	    return this.destinations.get(arg0);

	}


	public void setDestinationDataEventListener(
			DestinationDataEventListener arg0) {
		destinationDataEventListener=arg0;

	}
	
	public boolean supportsEvents() {
		return true;
	}
	
	public void addDestination(String destinationName, Properties properties) {
		destinationDataEventListener.updated(destinationName);
		synchronized (destinations) {
			destinations.put(destinationName, properties);
		}
	}
	
	
	public void deleteDestination(String destinationName) {
		destinationDataEventListener.deleted(destinationName);
		synchronized (destinations) {
			destinations.remove(destinationName);
		}
	}

}
