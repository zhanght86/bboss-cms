package com.frameworkset.platform.cms.searchmanager;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.platform.cms.searchmanager.bean.CMSSearchIndex;

public class CMSMultiSearcher implements java.io.Serializable {
	private static final Logger log = LoggerFactory.getLogger(CMSMultiSearcher.class);
	
	private String queryStr;	//要查询的字符串，用and或者or隔开
	
	private List indexes;		// 元素为CMSSearchIndex类型
	
	private int hitsPerSet;  	//每页显示条数
	
	private long from;			//起始时间
	private long to;			//终止时间
	 
	private String fileFormat = "all";         //默认值为所有文件格式
	
	private int searchType = 0;			//检索类型
	
	private String[] fields = {"title", "description", "keyword", "content","url"};	//缺省对所有字段进行检索

	private IndexSearcher multiSearcher;
	
	private NumericRangeFilter dateFilter;		//数据过滤器，用于过滤非指定时间内的记录
	
	private String sort;				//排序方式，按时间排序：time；按相关度排序：relevance，相关度排序为lucene缺省排序
	
	private String field;				//查询字段，若查询字段值为all，则查询fields中的所有字段	
	
	private String channel;
	
	public void close() {
		if (multiSearcher != null) {
			
	        try {
//	              multiSearcher.;
	        	multiSearcher.getIndexReader().close();
	          } catch (Exception e) {
	              e.printStackTrace();
	          }
	     }
	  }
	
	public IndexSearcher msearch() throws IOException
	{
		int size=indexes.size();	
		IndexReader reader[] = new IndexReader[size];
		 for(int j=0;j<size;j++)
         {
           String  indexPath = ((CMSSearchIndex)indexes.get(j)).getAbsoluteIndexPath();		//索引文件路径
           reader[j]  = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
         }
		
		 IndexSearcher multiSearcher = new IndexSearcher( new MultiReader( reader) );   //联合检索工具
		 return multiSearcher;
	}
	public HitResult search(){
		ScoreDoc[] hits = null;//存放利用lucene的检索工具检索出来的结果
		try{
			HitResult result = new HitResult();
//			int size=indexes.size();			//索引库数（每一个站外站点、频道都有单独的索引库）
//			Searchable[] searcher=new Searchable[size];		//每个索引库都对应一个检索工具对象
//			
//			 for(int j=0;j<size;j++)
//	         {
//	           String  indexPath = ((CMSSearchIndex)indexes.get(j)).getAbsoluteIndexPath();		//索引文件路径
//	           searcher[j] = new IndexSearcher(indexPath);
//	         }
			
			 multiSearcher =  msearch();   //联合检索工具
			 
			 Analyzer  analyzer = new SmartChineseAnalyzer(Version.LUCENE_47);	//解析器
			 
			 //查询条件
			 BooleanQuery query = new BooleanQuery();
			 
			 //按照指定字段查询,领导 一二三
			 BooleanQuery fQuery = new BooleanQuery();
			 if("all".equals(field)){
		         for (int i = 0; i < fields.length; i++) {
		        	 QueryParser parser = new QueryParser(Version.LUCENE_47, fields[i], analyzer);
		        	 
		             fQuery.add(parser.parse(queryStr), BooleanClause.Occur.SHOULD);
		         }
			 }else{
				 QueryParser parser = new QueryParser(Version.LUCENE_47, field, analyzer);
				 fQuery.add(parser.parse(queryStr), BooleanClause.Occur.SHOULD);
			 }
	         query.add(fQuery, BooleanClause.Occur.MUST);
//			 QueryParser parser = new QueryParser(Version.LUCENE_47, "content", analyzer);
////			 query.add(parser.parse(queryStr), BooleanClause.Occur.SHOULD);
//			 Query query = parser.parse(queryStr);
	         
	         //按照指定文件类型查询,"all"表示所有格式
	         if(!fileFormat.equals("all")){
	        	 QueryParser parser = new QueryParser(Version.LUCENE_47, "fileFormat", analyzer);
	              query.add(parser.parse(
	                          fileFormat),BooleanClause.Occur.MUST);
	          }
	         
	         //指定时间段过滤器
	         if (from != 0) {
	              if (to != 0) {
	                  dateFilter = NumericRangeFilter.newLongRange("published", from, to, true, true);
	                  
	              } else {
	                  dateFilter = NumericRangeFilter.newLongRange("published", from,null, true, false);
	              }
	          } else if (to != 0) {
	              dateFilter = NumericRangeFilter.newLongRange("published", null, to, false, true);;
	          }
	         //计时
	         long startTime = new Date().getTime();
	         //	 检索
	         if("time".equals(sort)){						//按时间排序
		         if (dateFilter == null) {
		        	
		              hits = multiSearcher.search(query,1000).scoreDocs;
		          } else {
		              hits = multiSearcher.search(query,dateFilter,1000,
		            		  new Sort(new SortField[]{new SortField("published", SortField.Type.LONG,true)})).scoreDocs;
		          }
	         }else{											//按相关度排序
	        	 if (dateFilter == null) {
		              hits = multiSearcher.search(query,1000).scoreDocs;
		          } else {
		              hits = multiSearcher.search(query, dateFilter,1000).scoreDocs;
		          }
	         }
	         long endTime = new Date().getTime();
	         long seconds = ((endTime - startTime)/(long)1000);
//	         CMSSearchManager.searchTime = seconds;
	         result.setHits( CMSSearchManager.getSearchHitList(multiSearcher,hits));
	         result.setSearchTime(seconds);
	          return result;
		}catch(Exception e){
			log.error("检索异常，请确认索引文件是否已经建立",e);
//			e.printStackTrace();
			
			return null;
		}
	}
	
//	public HitResult search(){
//		Hits hits;			//存放利用lucene的检索工具检索出来的结果
//		try{
//			HitResult result = new HitResult();
//			int size=indexes.size();			//索引库数（每一个站外站点、频道都有单独的索引库）
//			Searchable[] searcher=new Searchable[size];		//每个索引库都对应一个检索工具对象
//			
//			 for(int j=0;j<size;j++)
//	         {
//	           String  indexPath = ((CMSSearchIndex)indexes.get(j)).getAbsoluteIndexPath();		//索引文件路径
//	           searcher[j] = new IndexSearcher(indexPath);
//	         }
//			
//			 multiSearcher = new org.apache.lucene.search.MultiSearcher(searcher);   //联合检索工具
//			 
//			 StandardAnalyzer analyzer = new StandardAnalyzer();			//解析器
//			 
//			 //查询条件
//			 BooleanQuery query = new BooleanQuery();
//			 
//			 //按照指定字段查询,领导 一二三
//			 BooleanQuery fQuery = new BooleanQuery();
//			 if("all".equals(field)){
//		         for (int i = 0; i < fields.length; i++) {
//		             fQuery.add(QueryParser.parse(
//		            	  queryStr, fields[i], analyzer), false, false);
//		         }
//			 }else{
//				 fQuery.add(QueryParser.parse(
//		            	  queryStr, field, analyzer), false, false);
//			 }
//	         query.add(fQuery, true, false);
//	         
//	         //按照指定文件类型查询,"all"表示所有格式
//	         if(!fileFormat.equals("all")){
//	              query.add(QueryParser.parse(
//	                          fileFormat, "fileFormat", analyzer), true, false);
//	          }
//	         
//	         //指定时间段过滤器
//	         if (from != 0) {
//	              if (to != 0) {
//	                  dateFilter = new DateFilter("published", from, to);
//	              } else {
//	                  dateFilter = DateFilter.After("published", from);
//	              }
//	          } else if (to != 0) {
//	              dateFilter = DateFilter.Before("published", to);
//	          }
//	         //计时
//	         long startTime = new Date().getTime();
//	         //	 检索
//	         if("time".equals(sort)){						//按时间排序
//		         if (dateFilter == null) {
//		              hits = multiSearcher.search(query,
//		            		  new Sort(new SortField[]{new SortField("published",true)}));
//		          } else {
//		              hits = multiSearcher.search(query, dateFilter,
//		            		  new Sort(new SortField[]{new SortField("published",true)}));
//		          }
//	         }else{											//按相关度排序
//	        	 if (dateFilter == null) {
//		              hits = multiSearcher.search(query);
//		          } else {
//		              hits = multiSearcher.search(query, dateFilter);
//		          }
//	         }
//	         long endTime = new Date().getTime();
//	         long seconds = ((endTime - startTime)/(long)1000);
////	         CMSSearchManager.searchTime = seconds;
//	         result.setHits( CMSSearchManager.getSearchHitList(hits));
//	         result.setSearchTime(seconds);
//	          return result;
//		}catch(Exception e){
//			log.error("检索异常，请确认索引文件是否已经建立",e);
////			e.printStackTrace();
//			
//			return null;
//		}
//	}
	
	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public long getFrom() {
		return from;
	}

	public void setFrom(long from) {
		this.from = from;
	}

	public void setFrom(int days){
		from = System.currentTimeMillis() - (long)1000*60*60*24*days;
	}
	
	public int getHitsPerSet() {
		return hitsPerSet;
	}

	public void setHitsPerSet(int hitsPerSet) {
		this.hitsPerSet = hitsPerSet;
	}

	public List getIndexes() {
		return indexes;
	}

	public void setIndexes(List indexes) {
		this.indexes = indexes;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public long getTo() {
		return to;
	}

	public void setTo(long to) {
		this.to = to;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	
}
