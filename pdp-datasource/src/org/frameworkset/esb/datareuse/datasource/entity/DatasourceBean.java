package org.frameworkset.esb.datareuse.datasource.entity;

import java.io.Serializable;
import java.sql.Timestamp;


public class DatasourceBean implements Serializable {
	private String connection_type; //连接方式(Native JDBC/JNDI)
	private String db_type; //数据库类型
	private String driver; //数据库驱动
	private String ds_name; //数据源名称
	private int initial_connections; //初始连接数
	private String jdbc_url; //数据库连接串
	private String jndi_name; //JNDI名称(连接方式为JNDI时才存此值
	private int maximum_size; //最大连接数
	private int minimum_size; //最小连接数
	private String password; //密码
	private String read_only; //只读连接(1表示是，0表示否)
	private String remark; //备注
	private String show_sql; //是否调试SQL(1表示是，0表示否)
	private String use_pool; //是否使用连接池(1表示是，0表示否)
	private String username; //用户名
	private String validation_query; //连接检测SQL
	private String ext_jndi_name;//外部JNDI名字
	private String status;//状态
	private Timestamp create_time;//新建时间
	private String creator;//新建者
	private String modifier;//最新修改者
	private Timestamp modify_time;//最新修改时间
	private int maxnumactive;
	private int numactive;
	private int numidle;
	private String workStatus;//工作状态
	
	
	
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public Timestamp getModify_time() {
		return modify_time;
	}
	public void setModify_time(Timestamp modify_time) {
		this.modify_time = modify_time;
	}
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExt_jndi_name() {
		return ext_jndi_name;
	}
	public void setExt_jndi_name(String ext_jndi_name) {
		this.ext_jndi_name = ext_jndi_name;
	}
	public String getConnection_type() {
		return connection_type;
	}
	public void setConnection_type(String connectionType) {
		connection_type = connectionType;
	}
	public String getDb_type() {
		return db_type;
	}
	public void setDb_type(String dbType) {
		db_type = dbType;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getDs_name() {
		return ds_name;
	}
	public void setDs_name(String dsName) {
		ds_name = dsName;
	}
	public int getInitial_connections() {
		return initial_connections;
	}
	public void setInitial_connections(int initialConnections) {
		initial_connections = initialConnections;
	}
	public String getJdbc_url() {
		return jdbc_url;
	}
	public void setJdbc_url(String jdbcUrl) {
		jdbc_url = jdbcUrl;
	}
	public String getJndi_name() {
		return jndi_name;
	}
	public void setJndi_name(String jndiName) {
		jndi_name = jndiName;
	}
	public int getMaximum_size() {
		return maximum_size;
	}
	public void setMaximum_size(int maximumSize) {
		maximum_size = maximumSize;
	}
	public int getMinimum_size() {
		return minimum_size;
	}
	public void setMinimum_size(int minimumSize) {
		minimum_size = minimumSize;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRead_only() {
		return read_only;
	}
	public void setRead_only(String readOnly) {
		read_only = readOnly;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getShow_sql() {
		return show_sql;
	}
	public void setShow_sql(String showSql) {
		show_sql = showSql;
	}
	public String getUse_pool() {
		return use_pool;
	}
	public void setUse_pool(String usePool) {
		use_pool = usePool;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getValidation_query() {
		return validation_query;
	}
	public void setValidation_query(String validationQuery) {
		validation_query = validationQuery;
	}
	
	@Override
	public String toString() {
		return "DatasourceBean [connection_type=" + connection_type
				+ ", db_type=" + db_type + ", driver=" + driver + ", ds_name="
				+ ds_name + ", initial_connections=" + initial_connections
				+ ", jdbc_url=" + jdbc_url + ", jndi_name=" + jndi_name
				+ ", maximum_size=" + maximum_size + ", minimum_size="
				+ minimum_size + ", password=" + password + ", read_only="
				+ read_only + ", remark=" + remark + ", show_sql=" + show_sql
				+ ", use_pool=" + use_pool + ", username=" + username
				+ ", validation_query=" + validation_query + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((connection_type == null) ? 0 : connection_type.hashCode());
		result = prime * result + ((db_type == null) ? 0 : db_type.hashCode());
		result = prime * result + ((driver == null) ? 0 : driver.hashCode());
		result = prime * result + ((ds_name == null) ? 0 : ds_name.hashCode());
		result = prime * result + initial_connections;
		result = prime * result
				+ ((jdbc_url == null) ? 0 : jdbc_url.hashCode());
		result = prime * result
				+ ((jndi_name == null) ? 0 : jndi_name.hashCode());
		result = prime * result + maximum_size;
		result = prime * result + minimum_size;
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((read_only == null) ? 0 : read_only.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result
				+ ((show_sql == null) ? 0 : show_sql.hashCode());
		result = prime * result
				+ ((use_pool == null) ? 0 : use_pool.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		result = prime
				* result
				+ ((validation_query == null) ? 0 : validation_query.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatasourceBean other = (DatasourceBean) obj;
		if (connection_type == null) {
			if (other.connection_type != null)
				return false;
		} else if (!connection_type.equals(other.connection_type))
			return false;
		if (db_type == null) {
			if (other.db_type != null)
				return false;
		} else if (!db_type.equals(other.db_type))
			return false;
		if (driver == null) {
			if (other.driver != null)
				return false;
		} else if (!driver.equals(other.driver))
			return false;
		if (ds_name == null) {
			if (other.ds_name != null)
				return false;
		} else if (!ds_name.equals(other.ds_name))
			return false;
		if (initial_connections != other.initial_connections)
			return false;
		if (jdbc_url == null) {
			if (other.jdbc_url != null)
				return false;
		} else if (!jdbc_url.equals(other.jdbc_url))
			return false;
		if (jndi_name == null) {
			if (other.jndi_name != null)
				return false;
		} else if (!jndi_name.equals(other.jndi_name))
			return false;
		if (maximum_size != other.maximum_size)
			return false;
		if (minimum_size != other.minimum_size)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (read_only == null) {
			if (other.read_only != null)
				return false;
		} else if (!read_only.equals(other.read_only))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (show_sql == null) {
			if (other.show_sql != null)
				return false;
		} else if (!show_sql.equals(other.show_sql))
			return false;
		if (use_pool == null) {
			if (other.use_pool != null)
				return false;
		} else if (!use_pool.equals(other.use_pool))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (validation_query == null) {
			if (other.validation_query != null)
				return false;
		} else if (!validation_query.equals(other.validation_query))
			return false;
		return true;
	}
	public int getMaxnumactive() {
		return maxnumactive;
	}
	public void setMaxnumactive(int maxnumactive) {
		this.maxnumactive = maxnumactive;
	}
	public int getNumactive() {
		return numactive;
	}
	public void setNumactive(int numactive) {
		this.numactive = numactive;
	}
	public int getNumidle() {
		return numidle;
	}
	public void setNumidle(int numidle) {
		this.numidle = numidle;
	}
	
	
}
