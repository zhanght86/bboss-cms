package com.frameworkset.platform.dbmanager;

/**
 * poolman.xml中连接池实例属性
 * <p>Title: ParsedDataSource.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2008-4-23
 * @author liangbing.tao
 * @version 1.0
 */
public class ParsedDataSource {
	
	private String dbName;
	private String loadMetaData ;
	private String jndiName ;
	private String driver;
	private String url ;
	private String userName ;
	private String password ;
	private String txIsolationLevel;
	private String nativeResults;
	private String poolPreparedStatements;
	private String initialConnections;
	private String minimumSize ;
	private String maximumSize ;
	private String maximumSoft ;
	private String maxWait ;
	private String removeAbandoned;
	private String userTimeout;
	private String connectionTimeout ;
	private String skimmerFrequency;
	private String shrinkBy;
	private String keygenerate;
	private String logFile;
	private String debugging;
	
	public String getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(String connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDebugging() {
		return debugging;
	}
	public void setDebugging(String debugging) {
		this.debugging = debugging;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getInitialConnections() {
		return initialConnections;
	}
	public void setInitialConnections(String initialConnections) {
		this.initialConnections = initialConnections;
	}
	public String getJndiName() {
		return jndiName;
	}
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}
	public String getKeygenerate() {
		return keygenerate;
	}
	public void setKeygenerate(String keygenerate) {
		this.keygenerate = keygenerate;
	}
	public String getLoadMetaData() {
		return loadMetaData;
	}
	public void setLoadMetaData(String loadMetaData) {
		this.loadMetaData = loadMetaData;
	}
	public String getLogFile() {
		return logFile;
	}
	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}
	public String getMaximumSize() {
		return maximumSize;
	}
	public void setMaximumSize(String maximumSize) {
		this.maximumSize = maximumSize;
	}
	public String getMaximumSoft() {
		return maximumSoft;
	}
	public void setMaximumSoft(String maximumSoft) {
		this.maximumSoft = maximumSoft;
	}
	public String getMinimumSize() {
		return minimumSize;
	}
	public void setMinimumSize(String minimumSize) {
		this.minimumSize = minimumSize;
	}
	public String getNativeResults() {
		return nativeResults;
	}
	public void setNativeResults(String nativeResults) {
		this.nativeResults = nativeResults;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPoolPreparedStatements() {
		return poolPreparedStatements;
	}
	public void setPoolPreparedStatements(String poolPreparedStatements) {
		this.poolPreparedStatements = poolPreparedStatements;
	}
	public String getRemoveAbandoned() {
		return removeAbandoned;
	}
	public void setRemoveAbandoned(String removeAbandoned) {
		this.removeAbandoned = removeAbandoned;
	}
	public String getShrinkBy() {
		return shrinkBy;
	}
	public void setShrinkBy(String shrinkBy) {
		this.shrinkBy = shrinkBy;
	}
	public String getTxIsolationLevel() {
		return txIsolationLevel;
	}
	public void setTxIsolationLevel(String txIsolationLevel) {
		this.txIsolationLevel = txIsolationLevel;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserTimeout() {
		return userTimeout;
	}
	public void setUserTimeout(String userTimeout) {
		this.userTimeout = userTimeout;
	}
	public String getSkimmerFrequency() {
		return skimmerFrequency;
	}
	public void setSkimmerFrequency(String skimmerFrequency) {
		this.skimmerFrequency = skimmerFrequency;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMaxWait() {
		return maxWait;
	}
	public void setMaxWait(String maxWait) {
		this.maxWait = maxWait;
	}
	
	
}
