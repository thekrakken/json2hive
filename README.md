Json to Hive table [![Build Status](https://secure.travis-ci.org/thekrakken/json2hive.png?branch=master)](https://travis-ci.org/thekrakken/json2hive)
==================


json2hive is simple API that convert your Json data into Hive table.

This API will take a curated JSON document and generate the Hive schema (CREATE TABLE statement) for use with the _any Json serDe_. I say "curated" because you should ensure that every possible key is present (with some arbitrary value of the right data type) and that all arrays have at least one entry. Otherwide json2Hive will assume that the default value is STRING.

If the curated JSON example you provide has more than one entry in an array, only the first one will be examined, so you should ensure that it has all the field.

## Build

    gradle build
    
gradle will create `jsonToHive-${version}.jar` in the `build/libs/` folder.

## Maven repository

    <dependency>
      <groupId>io.thekraken</groupId>
      <artifactId>json2hive</artifactId>
      <version>0.1.0</version>
   </dependency>

Or with gradle
=======
   	
	compile "io.thekraken:json2hive:0.1.0"

## Usage

### As API in your application

    String tableName = "MY_HIVE_TABLE";
    String json = ...;
    /** Create a default hive table */
    String hiveSchema = Json.create(tableName, json);
    
    /** Create hive table with ROW FORMAT */
    String hiveSchemaWithRow = Json.create(tableName, json, true, [OPTIONAL] "classOfTheSerde");


### Example:

Json document:

    {
        "value" : "Some text in here",
        "value2" : 5645454
    }
    
Generate the Hive table from the Json sample

    String hiveSchema = Json.create("my_table", json, [true]);
    
The resultat will be

    CREATE EXTERNAL TABLE my_table (
    value STRING,
    value2 BIGINT
    )
    [ IN CASE YOU WANTED THE ROW SERDE:]
    ROW FORMAT SERDE 'com.cloudera.hive.serde.JSONSerDe'
