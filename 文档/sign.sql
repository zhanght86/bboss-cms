ALTER TABLE TD_WF_APP
ADD (needsign NUMBER(1) DEFAULT 1)

COMMENT ON COLUMN 
TD_WF_APP.needsign IS 
'令牌、票据是否需要签名 1 需要 0 不需要 默认为0'


ALTER TABLE td_wf_app ADD (needsign DECIMAL(1) DEFAULT '1')