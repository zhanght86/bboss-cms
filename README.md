1.内容管理ie9下设置模板时，有中文乱码
2.去除内容管理模板管理列表页面存在session操作
3.内容管理文档内部分类中文乱码
4.解决模板管理上传zip包存放路径不正确问题

---------------------------oracle-------------------------
ALTER TABLE TD_WF_RUN_TASK
 DROP PRIMARY KEY CASCADE;
DROP TABLE TD_WF_RUN_TASK CASCADE CONSTRAINTS PURGE;

CREATE TABLE TD_WF_RUN_TASK
(
  BUSINESS_KEY     NVARCHAR2(255),
  PROCESS_KEY      NVARCHAR2(255),
  PROCESS_ID       NVARCHAR2(64)                NOT NULL,
  SENDER           NVARCHAR2(255),
  SENDERNAME       NVARCHAR2(255),
  TASK_ID          NVARCHAR2(64)                NOT NULL,
  TASK_NAME        NVARCHAR2(255),
  TASK_KEY         NVARCHAR2(255),
  TASK_URL         NVARCHAR2(200),
  CREATE_DATETIME  TIMESTAMP(6),
  DEALERS          NVARCHAR2(500),
  DEALERWORKNO     NVARCHAR2(500),
  DEALERNAMES      NVARCHAR2(500),
  LAST_OP          NVARCHAR2(64),
  LAST_OPER        NVARCHAR2(64)
);

COMMENT ON TABLE TD_WF_RUN_TASK IS '统一代办任务表';

COMMENT ON COLUMN TD_WF_RUN_TASK.BUSINESS_KEY IS '业务id';

COMMENT ON COLUMN TD_WF_RUN_TASK.PROCESS_KEY IS '流程key';

COMMENT ON COLUMN TD_WF_RUN_TASK.PROCESS_ID IS '流程实例id';

COMMENT ON COLUMN TD_WF_RUN_TASK.SENDER IS '流程发起人';

COMMENT ON COLUMN TD_WF_RUN_TASK.SENDERNAME IS '流程发起人姓名';

COMMENT ON COLUMN TD_WF_RUN_TASK.TASK_ID IS '任务id';

COMMENT ON COLUMN TD_WF_RUN_TASK.TASK_NAME IS '节点名称';

COMMENT ON COLUMN TD_WF_RUN_TASK.TASK_KEY IS '任务key';

COMMENT ON COLUMN TD_WF_RUN_TASK.TASK_URL IS '代办url';

COMMENT ON COLUMN TD_WF_RUN_TASK.CREATE_DATETIME IS '任务创建时间';

COMMENT ON COLUMN TD_WF_RUN_TASK.DEALERS IS '当前任务处理人';

COMMENT ON COLUMN TD_WF_RUN_TASK.DEALERWORKNO IS '当前任务处理人工号';

COMMENT ON COLUMN TD_WF_RUN_TASK.DEALERNAMES IS '当前任务处理人姓名';

COMMENT ON COLUMN TD_WF_RUN_TASK.LAST_OP IS '上一次处理动作';

COMMENT ON COLUMN TD_WF_RUN_TASK.LAST_OPER IS '上一次处理人';


CREATE UNIQUE INDEX TD_WF_RUN_TASK_PK ON TD_WF_RUN_TASK
(PROCESS_ID, TASK_ID);


ALTER TABLE TD_WF_RUN_TASK ADD (
  CONSTRAINT TD_WF_RUN_TASK_PK
 PRIMARY KEY
 (PROCESS_ID, TASK_ID));



DROP TABLE TD_COMMON_ORDER CASCADE CONSTRAINTS PURGE;

CREATE TABLE TD_COMMON_ORDER
(
 ORDER_ID                  NVARCHAR2(50),
  ORDER_NO                  NVARCHAR2(32),
  ORDER_TYPE                NVARCHAR2(20),
  ORDER_TYPE_NAME           NVARCHAR2(40),
  ORDER_STATE               NVARCHAR2(20),
  ORDER_STATE_NAME          NVARCHAR2(40),
  APPLY_PERSON              NVARCHAR2(120),
  APPLY_PERSON_NAME         NVARCHAR2(240),
  APPLY_PERSON_WORKNUMBER   NVARCHAR2(120),
  CREATE_DATETIME           TIMESTAMP(6),
  CREATE_PERSON             NVARCHAR2(40),
  CREATE_PERSON_NAME        NVARCHAR2(80),
  CREATE_PERSON_WORKNUMBER  NVARCHAR2(40),
  PROCESS_ID                NVARCHAR2(42),
  PROCESS_KEY               NVARCHAR2(20),
  TITLE                     NVARCHAR2(200),
  DEAL_TITLE                NVARCHAR2(200),
  DEAL_STATE                NVARCHAR2(20),
  DEAL_STATE_NAME           NVARCHAR2(40),
  DEAL_PERSON               NVARCHAR2(200),
  DEAL_PERSON_NAME          NVARCHAR2(400),
  DEAL_PERSON_WORKNUMBER    NVARCHAR2(200),
  APPROVE_PERSON            NVARCHAR2(1000),
  APPROVE_PERSON_NAME       NVARCHAR2(2000),
  APPROVE_PERSON_WORKNUMBER NVARCHAR2(1000),
  LIMIT_DATETIME            TIMESTAMP(6),
  MODIFY_DATETIME           TIMESTAMP(6),
  END_DATETIME              TIMESTAMP(6),
  ORDER_PARAM               NVARCHAR2(100)
);

COMMENT ON COLUMN TD_COMMON_ORDER.ORDER_ID IS '申请单ID
';

COMMENT ON COLUMN TD_COMMON_ORDER.ORDER_NO IS '申请单编码
';

COMMENT ON COLUMN TD_COMMON_ORDER.ORDER_TYPE IS '申请单类型
';

COMMENT ON COLUMN TD_COMMON_ORDER.ORDER_TYPE_NAME IS '申请单类型名称
';

COMMENT ON COLUMN TD_COMMON_ORDER.ORDER_STATE IS '申请单状态
';

COMMENT ON COLUMN TD_COMMON_ORDER.ORDER_STATE_NAME IS '申请单状态名称
';

COMMENT ON COLUMN TD_COMMON_ORDER.APPLY_PERSON IS '申请人
';

COMMENT ON COLUMN TD_COMMON_ORDER.APPLY_PERSON_NAME IS '申请人名称
';

COMMENT ON COLUMN TD_COMMON_ORDER.APPLY_PERSON_WORKNUMBER IS '申请人工号
';

COMMENT ON COLUMN TD_COMMON_ORDER.CREATE_DATETIME IS '申请日期
';

COMMENT ON COLUMN TD_COMMON_ORDER.CREATE_PERSON IS '提交人
';

COMMENT ON COLUMN TD_COMMON_ORDER.CREATE_PERSON_NAME IS '提交人名称
';

COMMENT ON COLUMN TD_COMMON_ORDER.CREATE_PERSON_WORKNUMBER IS '提交人工号
';

COMMENT ON COLUMN TD_COMMON_ORDER.PROCESS_ID IS '流程ID
';

COMMENT ON COLUMN TD_COMMON_ORDER.PROCESS_KEY IS '流程KEY
';

COMMENT ON COLUMN TD_COMMON_ORDER.TITLE IS '标题
';

COMMENT ON COLUMN TD_COMMON_ORDER.DEAL_TITLE IS '标题_DEAL
';

COMMENT ON COLUMN TD_COMMON_ORDER.DEAL_STATE IS '处理状态
';

COMMENT ON COLUMN TD_COMMON_ORDER.DEAL_STATE_NAME IS '状态_DEAl_NAME
';

COMMENT ON COLUMN TD_COMMON_ORDER.DEAL_PERSON IS '处理人
';

COMMENT ON COLUMN TD_COMMON_ORDER.DEAL_PERSON_NAME IS '处理人名称
';

COMMENT ON COLUMN TD_COMMON_ORDER.DEAL_PERSON_WORKNUMBER IS '处理人工号
';

COMMENT ON COLUMN TD_COMMON_ORDER.APPROVE_PERSON IS '审批人
';

COMMENT ON COLUMN TD_COMMON_ORDER.APPROVE_PERSON_NAME IS '审批人名称
';

COMMENT ON COLUMN TD_COMMON_ORDER.APPROVE_PERSON_WORKNUMBER IS '审批人工号
';

COMMENT ON COLUMN TD_COMMON_ORDER.LIMIT_DATETIME IS '处理期限
';

COMMENT ON COLUMN TD_COMMON_ORDER.MODIFY_DATETIME IS '修改时间
';

COMMENT ON COLUMN TD_COMMON_ORDER.END_DATETIME IS '结束时间
';

COMMENT ON COLUMN TD_COMMON_ORDER.ORDER_PARAM IS '申请单参数';



------------------------------mysql----------------------------------

==========================物资出门数据变更===========================


CREATE TABLE TD_WF_RUN_TASK
(
  BUSINESS_KEY     VARCHAR(255),
  PROCESS_KEY      VARCHAR(255),
  PROCESS_ID       VARCHAR(64)                NOT NULL,
  SENDER           VARCHAR(255),
  SENDERNAME       VARCHAR(255),
  TASK_ID          VARCHAR(64)                NOT NULL,
  TASK_NAME        VARCHAR(255),
  TASK_KEY         VARCHAR(255),
  TASK_URL         VARCHAR(200),
  CREATE_DATETIME  TIMESTAMP(6)  NULL DEFAULT NULL,
  DEALERS          VARCHAR(500),
  DEALERWORKNO     VARCHAR(500),
  DEALERNAMES      VARCHAR(500),
  LAST_OP          VARCHAR(64),
  LAST_OPER        VARCHAR(64)
);


CREATE UNIQUE INDEX TD_WF_RUN_TASK_PK ON TD_WF_RUN_TASK
(PROCESS_ID, TASK_ID);


ALTER TABLE TD_WF_RUN_TASK ADD (
  CONSTRAINT TD_WF_RUN_TASK_PK
 PRIMARY KEY
 (PROCESS_ID, TASK_ID));


CREATE TABLE TD_COMMON_ORDER
(
 ORDER_ID                  VARCHAR(50),
  ORDER_NO                  VARCHAR(32),
  ORDER_TYPE                VARCHAR(20),
  ORDER_TYPE_NAME           VARCHAR(40),
  ORDER_STATE               VARCHAR(20),
  ORDER_STATE_NAME          VARCHAR(40),
  APPLY_PERSON              VARCHAR(120),
  APPLY_PERSON_NAME         VARCHAR(240),
  APPLY_PERSON_WORKNUMBER   VARCHAR(120),
  CREATE_DATETIME           TIMESTAMP(6)  NULL DEFAULT NULL,
  CREATE_PERSON             VARCHAR(40),
  CREATE_PERSON_NAME        VARCHAR(80),
  CREATE_PERSON_WORKNUMBER  VARCHAR(40),
  PROCESS_ID                VARCHAR(42),
  PROCESS_KEY               VARCHAR(20),
  TITLE                     VARCHAR(200),
  DEAL_TITLE                VARCHAR(200),
  DEAL_STATE                VARCHAR(20),
  DEAL_STATE_NAME           VARCHAR(40),
  DEAL_PERSON               VARCHAR(200),
  DEAL_PERSON_NAME          VARCHAR(400),
  DEAL_PERSON_WORKNUMBER    VARCHAR(200),
  APPROVE_PERSON            VARCHAR(1000),
  APPROVE_PERSON_NAME       VARCHAR(2000),
  APPROVE_PERSON_WORKNUMBER VARCHAR(1000),
  LIMIT_DATETIME            TIMESTAMP(6) NULL DEFAULT NULL,
  MODIFY_DATETIME           TIMESTAMP(6) NULL DEFAULT NULL,
  END_DATETIME              TIMESTAMP(6) NULL DEFAULT NULL,
  ORDER_PARAM               VARCHAR(100)
);