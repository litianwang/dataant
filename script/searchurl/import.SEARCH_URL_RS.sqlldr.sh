#!/bin/sh
#***************************************************************************
#* author litianwang                                         *
#* company voson                                              *
#***************************************************************************
#
#
SQLLDR_LOG_FILE=${0}.`date +%Y%m%d`.log
echo >> $SQLLDR_LOG_FILE
chmod 666 $SQLLDR_LOG_FILE

echo Script $0 >> $SQLLDR_LOG_FILE
echo ==== started on `date` ==== >> $SQLLDR_LOG_FILE
echo >> $SQLLDR_LOG_FILE

##Oracle
source /home/hadoop/.bash_profile

#==============configuration begin================#
#
SCAN_PATH=/home/hadoop/dgch/dataant/result/search/searchurl
#
FILE_NAME_RES="search_url*.txt"
#
SQLLDR_CTL_FILE=/home/hadoop/dgch/dataant/script/searchurl/SEARCH_URL_RS.ctl

echo  "SCAN_PATH: $SCAN_PATH">> $SQLLDR_LOG_FILE
echo  "FILE_NAME_RES: $FILE_NAME_RES">> $SQLLDR_LOG_FILE
echo  "SQLLDR_CTL_FILE : $SQLLDR_CTL_FILE">> $SQLLDR_LOG_FILE
echo  >> $SQLLDR_LOG_FILE
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
echo ==== end on `date` ==== >> $SQLLDR_LOG_FILE
echo  >> $SQLLDR_LOG_FILE