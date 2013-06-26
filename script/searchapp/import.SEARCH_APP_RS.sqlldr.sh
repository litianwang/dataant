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

##Oracle
source /home/hadoop/.bash_profile

#==============configuration begin================#
#
SCAN_PATH=/home/hadoop/dgch/dataant/result/search/searchapp
#
FILE_NAME_RES="search_app*.txt"
#
SQLLDR_CTL_FILE=/home/hadoop/dgch/dataant/script/searchapp/SEARCH_APP_RS.ctl

echo  "SCAN_PATH: $SCAN_PATH">> $LOG_FILE
echo  "FILE_NAME_RES: $FILE_NAME_RES">> $LOG_FILE
echo  "SQLLDR_CTL_FILE : $SQLLDR_CTL_FILE">> $LOG_FILE
echo  >> $LOG_FILE
#=============configuration end ================#

#
A=`find $SCAN_PATH -maxdepth 1 -name "$FILE_NAME_RES"`
for dir in ${A[*]}
do
 #echo $dir
 if [ -f "$dir" ]; then
 	rm -f $dir.dat
	mv $dir $dir.dat
	echo "sqlldr userid=mia/mia2013@dgch control=$SQLLDR_CTL_FILE data=$dir.dat log=$dir.sqlldr.log bad=$dir.sqlldr.bad direct=true errors=0"
	sqlldr userid=mia/mia2013@dgch control=$SQLLDR_CTL_FILE data=$dir.dat log=$dir.sqlldr.log bad=$dir.sqlldr.bad direct=true errors=0
	returnValue=$?
	#import success 
	if [ $returnValue -eq 0 ]; then
		rm -f $dir.dat
	fi
 fi
done
echo ==== end on `date` ==== >> $LOG_FILE
echo  >> $LOG_FILE