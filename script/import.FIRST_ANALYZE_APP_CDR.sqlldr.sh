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
SQLLDR_LOG_FILE=${script_dir}/logs/${script_name}.`date +%Y%m%d`.log
echo >> $SQLLDR_LOG_FILE
chmod 666 $SQLLDR_LOG_FILE

echo Script $0 >> $SQLLDR_LOG_FILE
echo ==== started on `date` ==== >> $SQLLDR_LOG_FILE
echo >> $SQLLDR_LOG_FILE

##Oracle
source /home/hadoop/.bash_profile

#==============configuration begin================#
#
SCAN_PATH=/home/hadoop/dgch/dataant/result/first_analyze
#
FILE_NAME_RES="*_app.txt"
#
SQLLDR_CTL_FILE=/home/hadoop/dgch/dataant/script/FIRST_ANALYZE_APP_CDR.ctl

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
 #echo "sqlldr userid=mia/mia2013@dgch control=$SQLLDR_CTL_FILE data=$dir log=$dir.sqlldr.log bad=$dir.sqlldr.bad direct=true errors=0"
 #sqlldr userid=mia/mia2013@dgch control=$SQLLDR_CTL_FILE data=$dir log=$dir.sqlldr.log bad=$dir.sqlldr.bad direct=true errors=0
 #rm -f $dir
 #mv  $dir $dir.bak
done

echo ==== end on `date` ==== >> $SQLLDR_LOG_FILE
echo  >> $SQLLDR_LOG_FILE