package com.frameworkset.platform.cms.searchmanager.bean;

public class CMSSearchIndex implements java.io.Serializable {
	private int id;				//索引id
	
//	private int siteId;			//索引站点id
	private String siteId;
	private String siteName;	//索引站点名称
	//private int chnlId;			//索引频道id
	private String chnlId;
	private String chnlName;	//索引频道名称
	private int lever;			//索引频率，0－每周一次；1－每月一次；2每天一次；
	
	//day为建索引的日期，
	//lever为0时，day值可为1－7，表示星期一到星期天
	//lever为1时，day值可为1－31，表示从1号到31号
	//lever为2时，day值可为1，表示当天
	private int day;			
	private int time;			//建索引的具体时间，值为0－23，表示一天中00:00点到23:00点
	
	private  String domainUrl;		//站外搜索域（范围），多个域用逗号隔开
	private  String startUrl;		//站外搜索起始页面，多个域用逗号隔开
	
	private	 int searchType;		//搜索类型，站内频道搜索为0；站外搜索为1；2整站搜索。0和2都属于本地搜索  
	
	private  String indexName;		//索引名，索引文件名为："in_"或"out_"或"site_" + 索引名
	
	private  String indexPath;		//索引库路径,在配置文件中配置
	
	private  String  absoluteIndexPath;  //索引文件所在目录，绝对路径
	
	private  String site_Ids;	//站群索引时存多个站点的id
	private  String siteNames;	//站群索引时存多个站点的名称
	
	//数据库索引新增字段
	private String db_name;
	private String table_name;
	private String content_fields;
	private String content_types;
	private String content_path;
	private String title_field;
	private String description_field;
	private String keyword_field;
	private String publishtime_field;
	private String access_url;	
	private String wheres;//查询数据库信息的where条件
	private String primarys;//这个数据库表的主键
	
	public String getSite_Ids() {
		return site_Ids;
	}
	public void setSite_Ids(String site_Ids) {
		this.site_Ids = site_Ids;
	}
	public String getSiteNames() {
		return siteNames;
	}
	public void setSiteNames(String siteNames) {
		this.siteNames = siteNames;
	}
	
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
//	public int getChnlId() {
//		return chnlId;
//	}
//	public void setChnlId(int chnlId) {
//		this.chnlId = chnlId;
//	}
	public String getChnlId() {
		return chnlId;
	}
	public void setChnlId(String chnlId) {
		this.chnlId = chnlId;
	}
	public String getChnlName() {
		return chnlName;
	}
	public void setChnlName(String chnlName) {
		this.chnlName = chnlName;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getLever() {
		return lever;
	}
	public void setLever(int lever) {
		this.lever = lever;
	}
//	public int getSiteId() {
//		return siteId;
//	}
//	public void setSiteId(int siteId) {
//		this.siteId = siteId;
//	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getDomainUrl() {
		return domainUrl;
	}
	public void setDomainUrl(String domainUrl) {
		this.domainUrl = domainUrl;
	}
	public int getSearchType() {
		return searchType;
	}
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}
	public String getStartUrl() {
		return startUrl;
	}
	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIndexPath() {
		return indexPath;
	}
	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}
	public String getAbsoluteIndexPath() {
		return absoluteIndexPath;
	}
	public void setAbsoluteIndexPath(String absoluteIndexPath) {
		this.absoluteIndexPath = absoluteIndexPath;
	}
	public String getAccess_url() {
		return access_url;
	}
	public void setAccess_url(String access_url) {
		this.access_url = access_url;
	}
	public String getContent_fields() {
		return content_fields;
	}
	public void setContent_fields(String content_fields) {
		this.content_fields = content_fields;
	}
	public String getDb_name() {
		return db_name;
	}
	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}
	public String getDescription_field() {
		return description_field;
	}
	public void setDescription_field(String description_field) {
		this.description_field = description_field;
	}
	public String getKeyword_field() {
		return keyword_field;
	}
	public void setKeyword_field(String keyword_field) {
		this.keyword_field = keyword_field;
	}
	public String getPublishtime_field() {
		return publishtime_field;
	}
	public void setPublishtime_field(String publishtime_field) {
		this.publishtime_field = publishtime_field;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getTitle_field() {
		return title_field;
	}
	public void setTitle_field(String title_field) {
		this.title_field = title_field;
	}
	public String getPrimarys() {
		return primarys;
	}
	public void setPrimarys(String primarys) {
		this.primarys = primarys;
	}
	public String getWheres() {
		return wheres;
	}
	public void setWheres(String wheres) {
		this.wheres = wheres;
	}
	/**
	 * 如果是数据库索引的话，指定对应的内容字段所对应的类型
	 * 包括文件，文本
	 * 如果是文件，文件的存放方式有两种情况：
	 * 	1.文件的路径存放在数据库表字段中，文件存储在文件系统中
	 *  2. 文件名称信息、文件本身都存在数据库中
	 * @return
	 */
	public String getContent_types() {
		return content_types;
	}
	public void setContent_types(String content_types) {
		this.content_types = content_types;
	}
	public String getContent_path() {
		return content_path;
	}
	public void setContent_path(String content_path) {
		this.content_path = content_path;
	}
	
}
