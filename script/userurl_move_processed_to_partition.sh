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
echo >> $LOG_FILE
chmod 666 $LOG_FILE

echo Script $0 >> $LOG_FILE
echo ==== started on `date` ==== >> $LOG_FILE
echo >> $LOG_FILE

#==============configuration begin================#
#HIVE_HOME=/home/hadoop/
#扫描HDFS目录
SCAN_PATH=/user/hadoop/dgch/userurl/processed
#扫描文件名前缀
FILE_NAME_PREFIX="userurl_"
#上传HDFS目录
TABLE_NAME=p_first_userurl
#分区表主目录
TABLE_PATH=/user/hadoop/dgch/userurl

echo >> $LOG_FILE
echo   "HIVE_HOME: $HIVE_HOME" >> $LOG_FILE
echo   "SCAN_PATH: $SCAN_PATH" >> $LOG_FILE
echo   "FILE_NAME_PREFIX: $FILE_NAME_PREFIX" >> $LOG_FILE
echo   "TABLE_PATH: $TABLE_PATH" >> $LOG_FILE
echo  >> $LOG_FILE
#=============configuration end ================#


#扫描文件
A=`hadoop fs -lsr $SCAN_PATH`
#echo $A|awk 'trans'
if [ "$A" = "" ]; then
        echo "no file" >> $LOG_FILE
else
        hiveAddPartition=""
	mvdata=""
        for dir in ${A[*]}
                do
                #echo $dir
		cmd1="echo \$dir|awk -F \"_\" '/${FILE_NAME_PREFIX}/{print \$1}'"
		#echo "$cmd1"
		uurl=`eval $cmd1`
		cmd2="echo \$dir|awk -F \"_\" '/${FILE_NAME_PREFIX}/{print \$2 \$3}'"
		#echo "$cmd2"
		dayTime=`eval $cmd2`
                #uurl=`echo $dir|awk -F "_" '/userurl_/{print $1}'`
                #dayTime=`echo $dir|awk -F "_" '/userurl_/{print $2 $3}'`
		lth=`expr length "${dayTime}"`
		#echo "length:${lth}"
                if [ "${lth}" = "10" ];then
                        year=${dayTime:0:4}
                        month=${dayTime:4:2}
                        day=${dayTime:6:2}
                        hour=${dayTime:8:2}
                        hiveAddPartition="${hiveAddPartition}alter table ${TABLE_NAME} add IF NOT EXISTS partition (year='${year}', month='${month}', day='${day}', hour='${hour}') location '${year}/${month}/${day}/${hour}';"
			mvdata="${mvdata} dfs -mv ${uurl}_${year}${month}${day}_${hour}* ${TABLE_PATH}/${year}/${month}/${day}/${hour};"
                fi
        done
        echo "hive -e \"${hiveAddPartition} ${mvdata}\"" >> $LOG_FILE
	hive -e "${hiveAddPartition} ${mvdata}" >> $LOG_FILE 2>&1
fi
echo ==== end on `date` ==== >> $LOG_FILE
echo  >> $LOG_FILE
