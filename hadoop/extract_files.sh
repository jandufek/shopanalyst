#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [ $# -eq 0 ]; then
  echo 'Please specify the path where load files'
  exit
fi

rm $1/frequentpatterns.seq
rm $1/fList.seq

$DIR/hadoop-1.1.1/bin/hadoop fs -getmerge patterns/frequentpatterns/ $1/frequentpatterns.seq
$DIR/hadoop-1.1.1/bin/hadoop fs -get patterns/fList $1/fList.seq
