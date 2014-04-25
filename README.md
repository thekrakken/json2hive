Json to Hive table
==================
[![Build Status](https://secure.travis-ci.org/thekrakken/json2hive.png?branch=master)](https://travis-ci.org/thekrakken/json2hive)

json2hive is simple API that convert your Json data into Hive table.

# How to use it

    String tableName = "MY_HIVE_TABLE";
    String hiveSchema = Json.create(tableName, jsonString);

