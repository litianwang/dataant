LOAD DATA
CHARACTERSET UTF8
APPEND
INTO TABLE FIRST_ANALYZE_URL_CDR
FIELDS TERMINATED BY '\t' 
TRAILING NULLCOLS 
(
  TASK_ID           ,
  UTC_BEGIN_TIME    date 'YYYYMMDDHH24MISS',
  UTC_END_TIME      date 'YYYYMMDDHH24MISS',
  USER_NO           ,
  IMEI_MEIDORESN    ,
  IMSI_MSID         ,
  NSAPI             ,
  APN_NAI           ,
  RAT_SERVICEOPTION ,
  GGSN_PDSN_IP      ,
  SGSN_PCF_IP       ,
  LAC               ,
  CI                ,
  USER_IP           ,
  SERVER_IP         ,
  IP_PROTOCAL       ,
  USER_PORT         ,
  SERVER_PORT       ,
  PROTOCAL_TYPE     ,
  CONTENT_TYPE      ,
  HOST_A            ,
  X_ONLINE_HOST     ,
  URI_C             ,
  REFERENCE         ,
  USERAGENT         ,
  SUCCESS_TAG       ,
  STATUS_CODE       ,
  CONTENT_LENGTH    ,
  RESPONSE_TIME     ,
  MO_PKG_CNT        ,
  MT_PKG_CNT        ,
  MO_BYTE_CNT       ,
  MT_BYTE_CNT       ,
  URL_CLASS_ID      ,
  URL_INTEREST_ID   ,
  PARTITION_ID      ,
  KEY_WORD          ,
  RULE_CODE            
)