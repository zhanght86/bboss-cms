package com.frameworkset.platform.cms.searchmanager.handler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.parser.ParserDelegator;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.text.html.HTML;

import com.frameworkset.platform.cms.searchmanager.bean.CMSSearchIndex;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.sql.ColumnMetaData;
import com.frameworkset.common.poolman.sql.TableMetaData;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

/**
 * <p><code>DBHandler</code>
 *  Content handler for DB index.
 * </p>
 *
 * @author da.wei</a>
 * @version 1.0
 */

public final class DBHandler extends ContentHandlerBase implements java.io.Serializable {
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");

	public DBHandler() {
    }

    public void parse(Map map) {
        try {
            reset();
            
            //解析
            this.fileFormat = ContentHandler.DBT_FILEFOMAT;
            
            this.contents = new StringBuffer().append(map.get("content_fields"));
            this.title = (String) map.get("title_field");
            this.description = (String) map.get("description_field");
            this.keywords = (String) map.get("keyword_field");
            Date date = (Date) map.get("publishtime_field");
            if(date.equals(null) || date==null)
            	date = new Date();
            Calendar calendar = new GregorianCalendar();;
            calendar.setTime(date);
            this.published = calendar.getTimeInMillis();
            
        } catch (Exception e) {e.printStackTrace();}
    }
}
