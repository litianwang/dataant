#!/bin/sh
#***************************************************************************
#* author litianwang                                         *
#* company voson                                              *
#***************************************************************************
#
#
HDFS_PUT_LOG_FILE=${0}.`date +%Y%m%d`.log
echo >> $HDFS_PUT_LOG_FILE
chmod 666 $HDFS_PUT_LOG_FILE

echo Script $0 >> $HDFS_PUT_LOG_FILE
echo ==== started on `date` ==== >> $HDFS_PUT_LOG_FILE
echo >> $HDFS_PUT_LOG_FILE

LOCK_FILE=${0}.lock
if [ -f ${LOCK_FILE} ];then
echo "locking!" >> $HDFS_PUT_LOG_FILE
exit 0
else
echo `date` >> $LOCK_FILE
chmod 666 $LOCK_FILE
fi
 
#==============configuration begin================#
HADOOP_HOME=/home/hadoop/apache/hadoop-1.1.2
#扫描本地文件目录
SCAN_PATH=/home/hadoop/dgch/etl/data/zip/url
#扫描文件名规则
FILE_NAME_RES="userurl*gz"
#上传HDFS目录
HDFS_LOCATION=/user/hadoop/dgch/userurl/unprocessed

echo >> $HDFS_PUT_LOG_FILE
echo   "HADOOP_HOME: $HADOOP_HOME" >> $HDFS_PUT_LOG_FILE
echo   "SCAN_PATH: $SCAN_PATH" >> $HDFS_PUT_LOG_FILE
echo  >> $HDFS_PUT_LOG_FILE
#=============configuration end ================#


#扫描文件
A=`find $SCAN_PATH -maxdepth 1 -name "$FILE_NAME_RES"`
if [ "$A" = "" ]; then
        echo "no file" >> $HDFS_PUT_LOG_FILE
else 
        echo $A   >> $HDFS_PUT_LOG_FILE
        #上传文件到HDFS
        #1、上传到临时目录
	$HADOOP_HOME/bin/hadoop fs -mkdir $HDFS_LOCATION
        $HADOOP_HOME/bin/hadoop fs -mkdir $HDFS_LOCATION.tmp
        echo "$HADOOP_HOME/bin/hadoop fs -put $A $HDFS_LOCATION.tmp"   >> $HDFS_PUT_LOG_FILE
        $HADOOP_HOME/bin/hadoop fs -put $A $HDFS_LOCATION.tmp >> $HDFS_PUT_LOG_FILE
        #2、移动到正式目录（后面需要考虑并发的问题）
        $HADOOP_HOME/bin/hadoop fs -mv $HDFS_LOCATION.tmp/*  $HDFS_LOCATION/ >> $HDFS_PUT_LOG_FILE
        #上传完成删除文件
        rm $A >> $HDFS_PUT_LOG_FILE
fi
#rm lock file
rm -f $LOCK_FILE
echo ==== end on `date` ==== >> $HDFS_PUT_LOG_FILE
echo  >> $HDFS_PUT_LOG_FILE
