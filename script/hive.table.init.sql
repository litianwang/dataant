dfs -rmr /user/hadoop/dgch;
dfs -mkdir /user/hadoop/dgch/userurl/unprocessed;
dfs -mkdir /user/hadoop/dgch/userurl/processing/;
dfs -mkdir /user/hadoop/dgch/userurl/processed/;

dfs -mkdir /user/hadoop/dgch/userflow/unprocessed;
dfs -mkdir /user/hadoop/dgch/userflow/processing/;
dfs -mkdir /user/hadoop/dgch/userflow/processed/;
drop table first_userurl;
CREATE EXTERNAL TABLE IF NOT EXISTS first_userurl(
UTC_BEGIN_TIME String,
UTC_END_TIME String,
USER_NO String,
IMEI_MEIDORESN String,
IMSI_MSID String,
NSAPI String,
APN_NAI String,
RAT_SERVICEOPTION String,
GGSN_PDSN_IP String,
SGSN_PCF_IP String,
LAC String,
CI String,
USER_IP String,
SERVER_IP String,
IP_PROTOCAL String,
USER_PORT String,
SERVER_PORT String,
PROTOCAL_TYPE String,
CONTENT_TYPE String,
HOST_A String,
X_ONLINE_HOST String,
URI_C String,
REFERENCE String,
USERAGENT String,
SUCCESS_TAG String,
STATUS_CODE String,
CONTENT_LENGTH String,
RESPONSE_TIME String,
MO_PKG_CNT String,
MT_PKG_CNT String,
MO_BYTE_CNT String,
MT_BYTE_CNT String,
URL_CLASS_ID String,
URL_INTEREST_ID String
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY "\t"
LOCATION '/user/hadoop/dgch/userurl/processing';
drop table first_userflow;
CREATE EXTERNAL TABLE IF NOT EXISTS first_userflow(
UTC_BEGIN_TIME String,
UTC_END_TIME String,
USER_NO String,
IMEI_MEIDORESN String,
IMSI_MSID String,
NSAPI String,
APN_NAI String,
RAT_SERVICEOPTION String,
GGSN_PDSN_IP String,
SGSN_PCF_IP String,
LAC String,
CI String,
USER_IP String,
SERVER_IP String,
IP_PROTOCAL String,
USER_PORT String,
SERVER_PORT String,
APP_ID String,
MO_PKG_CNT String,
MT_PKG_CNT String,
MO_BYTE_CNT String,
MT_BYTE_CNT String,
END_TAG String,
HOST String,
URI String,
RESPONSE_CODE String,
RESPONSE_TIME String,
CONTENT_LENGTH String
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY "\t"
LOCATION '/user/hadoop/dgch/userflow/processing';

