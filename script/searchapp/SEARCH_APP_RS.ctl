LOAD DATA
CHARACTERSET UTF8
APPEND
INTO TABLE SEARCH_APP_RS
FIELDS TERMINATED BY '\t' 
TRAILING NULLCOLS 
(
  ID           ,
  UTC_BEGIN_TIME    DATE 'YYYYMMDDHH24MISS',
  UTC_END_TIME      DATE 'YYYYMMDDHH24MISS',
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
  APP_ID            ,
  MO_PKG_CNT        ,
  MT_PKG_CNT        ,
  MO_BYTE_CNT       ,
  MT_BYTE_CNT       ,
  END_TAG           ,
  HOST              ,
  URI               ,
  RESPONSE_CODE     ,
  RESPONSE_TIME     ,
  CONTENT_LENGTH    ,
  PARTITION_ID              
)