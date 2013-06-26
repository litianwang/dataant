#!/bin/sh
#***************************************************************************
#* author litianwang                                         *
#* company voson                                              *
#***************************************************************************
#
#
script_dir=`dirname "$0"`
script_dir=`cd "${script_dir}"; pwd`
echo ${script_dir}
script_name=`basename "$0"`
#mkdir logs dir 
if [ ! -d "${script_dir}/logs" ]; then
mkdir ${script_dir}/logs
fi
LOG_FILE=${script_dir}/logs/${script_name}.`date +%Y%m%d`.log
#LOG_FILE=${0}.`date +%Y%m%d`.log
echo >> $LOG_FILE
chmod 666 $LOG_FILE

echo Script $0 >> $LOG_FILE
echo ==== started on `date` ==== >> $LOG_FILE
echo >> $LOG_FILE

#make lock file
LOCK_FILE=${0}.lock
if [ -f ${LOCK_FILE} ];then
echo "locking!" >> $LOG_FILE
exit 0
else
echo `date` >> $LOCK_FILE
chmod 666 $LOCK_FILE
fi
 
#==============configuration begin================#
HADOOP_HOME=/home/hadoop/hadoop-1.1.2
#扫描本地文件目录
SCAN_PATH=/home/hadoop/dgch/etl/data/zip/app
#扫描文件名规则
FILE_NAME_RES="userflow*gz"
#上传HDFS目录
HDFS_LOCATION=/user/hadoop/dgch/userflow/unprocessed

echo >> $LOG_FILE
echo   "HADOOP_HOME: $HADOOP_HOME" >> $LOG_FILE
echo   "SCAN_PATH: $SCAN_PATH" >> $LOG_FILE
echo  >> $LOG_FILE

#=============configuration end ================#


#扫描文件
A=`find $SCAN_PATH -maxdepth 1 -name "$FILE_NAME_RES"`
if [ "$A" = "" ]; then
        echo "no file" >> $LOG_FILE
else 
        echo $A   >> $LOG_FILE
        #上传文件到HDFS
        #1、上传到临时目录
	    $HADOOP_HOME/bin/hadoop fs -mkdir $HDFS_LOCATION
        $HADOOP_HOME/bin/hadoop fs -mkdir $HDFS_LOCATION.tmp
        echo "$HADOOP_HOME/bin/hadoop fs -put $A $HDFS_LOCATION.tmp"   >> $LOG_FILE
        $HADOOP_HOME/bin/hadoop fs -put $A $HDFS_LOCATION.tmp >> $LOG_FILE
        #2、移动到正式目录（后面需要考虑并发的问题）
        $HADOOP_HOME/bin/hadoop fs -mv $HDFS_LOCATION.tmp/*  $HDFS_LOCATION/ >> $LOG_FILE
        #上传完成删除文件
        returnValue=$?
        #import success
        if [ $returnValue -eq 0 ]; then
                rm $A >> $LOG_FILE
        fi
        #rm $A >> $LOG_FILE
fi
#rm lock file
rm -f $LOCK_FILE
echo ==== end on `date` ==== >> $LOG_FILE
echo  >> $LOG_FILE
