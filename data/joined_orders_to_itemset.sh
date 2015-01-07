#!/bin/bash

if [ $# -lt 2 ]; then
  echo 'Please specify the files to be converted from and to'
  exit
fi

# it comes in format
# order_1 item_1
# order_1 item_2
# order_2 item_1

# It has to be ordered with order_id!!

## DB QUERIES:
# Get order items: SELECT order_id, item_id FROM orders JOIN order_items USING (order_id) WHERE order_date_time > '2013-10-01' AND order_date_time < '2014-10-01' AND shop_id = ? ORDER BY order_id;
# Get mapper: SELECT item_id, name FROM items WHERE shop_id = ?

# Only for Postgres:
# COPY (SELECT order_id, item_id FROM orders JOIN order_items USING (order_id) WHERE order_date_time > '2013-10-01' AND order_date_time < '2014-10-01' AND shop_id = ? ORDER BY order_id) TO '/tmp/order_items.csv' (format csv, delimiter ' ');
# COPY (SELECT item_id, name FROM items WHERE shop_id = ?) TO '/tmp/mapper.csv' (format csv, delimiter ';');

# Orders group by customers
# COPY (SELECT customer_id, item_id FROM orders JOIN order_items USING (order_id) WHERE order_date_time > '2013-10-01' AND order_date_time < '2014-10-01' AND shop_id = ? ORDER BY customer_id) TO '/tmp/order_items.csv' (format csv, delimiter ' ');

# Mapper with categorytext instead name

# COPY (SELECT item_id, categorytext FROM items WHERE categorytext IS NOT NULL AND categorytext not like '' AND shop_id = ?) TO '/tmp/mapper.csv' (format csv, delimiter ';');

# Removing " and adding them after ; and to end of line
# perl -pi -e 's/"//g' mapper.csv; perl -pi -e 's/\n/"\n/g' mapper.csv; perl -pi -e 's/;/;"/g' mapper.csv

lastOrderId=-1
line=''
outputFile=$2

while read p; do
  splited=( $p );
  orderId="${splited[0]}"
  itemId="${splited[1]}"
  if [ "$orderId" -ne "$lastOrderId" ]; then
    if [ "$line" != '' ]; then
      echo $line >> $outputFile;
    fi
    line="$itemId"
    lastOrderId=$orderId
  else
    line="$line, $itemId"
  fi
done <$1
if [ "$line" != '' ]; then
      echo $line >> $outputFile;
fi
