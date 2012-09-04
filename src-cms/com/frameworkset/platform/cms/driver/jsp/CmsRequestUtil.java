package com.frameworkset.platform.cms.driver.jsp;

import java.util.HashMap;
import java.util.Map;

import com.frameworkset.platform.cms.driver.util.CmsStringUtil;



public class CmsRequestUtil implements java.io.Serializable {
	/**
     * Parses the parameters of the given request query part and creaes a parameter map out of them.<p>
     * 
     * Please note: This does not parse a full request URI/URL, only the query part that 
     * starts after the "?". For example, in the URI <code>/system/index.html?a=b&amp;c=d</code>,
     * the query part is <code>a=b&amp;c=d</code>.<p>
     * 
     * If the given String is empty, an empty map is returned.<p>
     * 
     * @param query the query to parse
     * @return the parameter map created from the query
     */
    public static Map createParameterMap(String query) {

        if (CmsStringUtil.isEmpty(query)) {
            // empty query
            return new HashMap();
        }
        if (query.charAt(0) == '?') {
            // remove leading '?' if required
            query = query.substring(1);
        }
        HashMap parameters = new HashMap();
        // cut along the different parameters
        String[] params = CmsStringUtil.splitAsArray(query, '&');
        for (int i = 0; i < params.length; i++) {
            String key = null;
            String value = null;
            // get key and value, separated by a '=' 
            int pos = params[i].indexOf('=');
            if (pos > 0) {
                key = params[i].substring(0, pos);
                value = params[i].substring(pos + 1);
            } else if (pos < 0) {
                key = params[i];
                value = "";
            }
            // now make sure the values are of type String[]
            if (key != null) {
                String[] values = (String[])parameters.get(key);
                if (values == null) {
                    // this is the first value, create new array
                    values = new String[] {value};
                } else {
                    // append to the existing value array
                    String[] copy = new String[values.length + 1];
                    System.arraycopy(values, 0, copy, 0, values.length);
                    copy[copy.length - 1] = value;
                    values = copy;
                }
                parameters.put(key, values);
            }
        }
        return parameters;
    }

}
