-- Create table
create table FIRST_ANALYZE_RULE_HISTORY
(
  ID           NUMBER(20) not null,
  RULE_ID      NUMBER(8),
  RULE_CODE    VARCHAR2(50),
  RUN_DATE     VARCHAR2(8),
  SEQ          NUMBER(4),
  START_TIME   DATE,
  END_TIME     DATE,
  STATUS       VARCHAR2(50),
  TRIGGER_TYPE NUMBER(1),
  WORK_DIR     VARCHAR2(1000),
  RESULT_DIR   VARCHAR2(1000),
  APP_NUM      NUMBER(12),
  URL_NUM      NUMBER(12)
);
-- Add comments to the table 
comment on table FIRST_ANALYZE_RULE_HISTORY
  is '一次分析处理历史记录';
-- Add comments to the columns 
comment on column FIRST_ANALYZE_RULE_HISTORY.ID
  is '历史记录ID';
comment on column FIRST_ANALYZE_RULE_HISTORY.RULE_ID
  is '规则ID';
comment on column FIRST_ANALYZE_RULE_HISTORY.RULE_CODE
  is '规则编码';
comment on column FIRST_ANALYZE_RULE_HISTORY.RUN_DATE
  is '运行日期YYYYMMDD';
comment on column FIRST_ANALYZE_RULE_HISTORY.SEQ
  is '序列号4位';
comment on column FIRST_ANALYZE_RULE_HISTORY.START_TIME
  is '开始时间';
comment on column FIRST_ANALYZE_RULE_HISTORY.END_TIME
  is '结束时间';
comment on column FIRST_ANALYZE_RULE_HISTORY.STATUS
  is '状态';
comment on column FIRST_ANALYZE_RULE_HISTORY.TRIGGER_TYPE
  is '触发方式';
comment on column FIRST_ANALYZE_RULE_HISTORY.WORK_DIR
  is '工作路径';
comment on column FIRST_ANALYZE_RULE_HISTORY.RESULT_DIR
  is '结果路径';
comment on column FIRST_ANALYZE_RULE_HISTORY.APP_NUM
  is 'APP结果数量';
comment on column FIRST_ANALYZE_RULE_HISTORY.URL_NUM
  is 'URL结果数量';
-- Create/Recreate primary, unique and foreign key constraints 
alter table FIRST_ANALYZE_RULE_HISTORY
  add constraint PK_FIRST_ANALYZE_RULE_HISTORY primary key (ID);
