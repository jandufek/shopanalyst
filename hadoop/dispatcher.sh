#!/bin/bash

if [ $# -lt 2 ]; then
  echo "Please specify the command ('hadoop-rm', 'hadoop-put' or 'mahout') and itemset name!"
  exit
fi

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
logFile=$DIR/dispatcher.log

case "$1" in

hadoop-rm) $DIR/hadoop-1.1.1/bin/hadoop fs -rm $2 >> dispatcher.log 2>&1
	   ;;

hadoop-put) if [ $# -lt 3 ]; then
  		echo "Like 3th parameter specify dat file!"
  		exit
	    fi
	    $DIR/hadoop-1.1.1/bin/hadoop fs -put $3 $2 >> dispatcher.log 2>&1
	    ;;

mahout)	
	export HADOOP_PREFIX=$DIR/hadoop-1.1.1;
	export HADOOP_CONF_DIR=$HADOOP_PREFIX/conf;
	export PATH=$HADOOP_PREFIX/bin:$PATH;

	$DIR/mahout-distribution-0.7/bin/mahout fpg -i $2 -o patterns -k 15 -method mapreduce -s 2 >> dispatcher.log 2>&1
	    ;;

*) echo "Use with 'hadoop-rm', 'hadoop-put' or 'mahout' option!"
   exit
   ;;
esac

