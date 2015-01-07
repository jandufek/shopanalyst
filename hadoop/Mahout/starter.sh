#!/bin/bash

if [ $# -lt 3 ]; then
  echo 'Please specify the directory with .seq and fList file, without slash in the end, then specify minimum support and minimum confidence and then output file'
  exit
fi
numOfOrders=`wc -l < $1/*.dat`
rm $4
java -cp target/Mahout-1.0-SNAPSHOT-jar-with-dependencies.jar cz.cvut.dufekja1.mahout.ResultReader $numOfOrders $1/mapper.csv $1/fList.seq $1/frequentpatterns.seq $2 $3 > $4

