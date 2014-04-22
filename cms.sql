insert into td_sm_dicttype
									  (DATA_VALIDATE_FIELD,
									   DATA_CREATE_ORGID_FIELD,
									   DICTTYPE_TYPE,
									   IS_TREE,
									   DICTTYPE_ID,
									   USER_ID,
									   DICTTYPE_NAME,
									   DICTTYPE_DESC,
									   DICTTYPE_PARENT,
									   DATA_TABLE_NAME,
									   DATA_NAME_FILED,
									   DATA_VALUE_FIELD,
									   DATA_ORDER_FIELD,
									   DATA_TYPEID_FIELD,
									   DATA_DBNAME,
									   DATA_PARENTID_FIELD,
									   DATA_NAME_CN,
									   DATA_VALUE_CN,
									   KEY_GENERAL_TYPE,
									   NAME_GENERAL_TYPE,
									   KEY_GENERAL_INFO,
									   NEEDCACHE,
									   ENABLE_VALUE_MODIFY)
									values
									  (' ',
									   ' ',
									   0,
									   0,
									   '2310',
									   null,
									   'newsservice',
									   '新闻服务配置',
									   '0',
									   ' ',
									   ' ',
									   ' ',
									   ' ',
									   ' ',
									   ' ',
									   ' ',
									   '参数项',
									   '参数值',
									   null,
									   ' ',
									   ' ',
									   0,
									   1);
insert into TD_SM_DICTDATA (DICTDATA_ID, DICTTYPE_ID, DICTDATA_NAME, DICTDATA_VALUE, DICTDATA_ORDER) values ('208', '2310', 'sitehost', 'http://bpit.sany.com.cn/SanyBPIT', 1);
insert into TD_SM_DICTDATA (DICTDATA_ID, DICTTYPE_ID, DICTDATA_NAME, DICTDATA_VALUE, DICTDATA_ORDER) values ('209', '2310', 'site', 'BPIT', 2);
insert into TD_SM_DICTDATA (DICTDATA_ID, DICTTYPE_ID, DICTDATA_NAME, DICTDATA_VALUE, DICTDATA_ORDER) values ('210', '2310', 'picnewschannel', 'importantnews', 3);
insert into TD_SM_DICTDATA (DICTDATA_ID, DICTTYPE_ID, DICTDATA_NAME, DICTDATA_VALUE, DICTDATA_ORDER) values ('211', '2310', 'picnewscount', '5', 4);
insert into TD_SM_DICTDATA (DICTDATA_ID, DICTTYPE_ID, DICTDATA_NAME, DICTDATA_VALUE, DICTDATA_ORDER) values ('212', '2310', 'newschannel', 'news', 5);
insert into TD_SM_DICTDATA (DICTDATA_ID, DICTTYPE_ID, DICTDATA_NAME, DICTDATA_VALUE, DICTDATA_ORDER) values ('213', '2310', 'newscount', '7', 6);