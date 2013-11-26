修改机构creator字段默认值为1,初始化脚本中有问题
ALTER TABLE TD_SM_ORGANIZATION
MODIFY(CREATOR  DEFAULT '1');

update td_sm_organization set CREATOR='1' where CREATOR='1 '