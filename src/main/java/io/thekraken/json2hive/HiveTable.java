/*******************************************************************************
 * Copyright 2014 Anthony Corbacho and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.thekraken.json2hive;

/**
 * Create the hive table from the json properties
 *
 * @author anthonycorbacho
 * @since 0.0.1
 */
public class HiveTable {


  private String tableSchema = "";
  private Integer addComma = 0;

  public void resetCommaFlag(){
    addComma = 0;
  }

  public HiveTable() {
    tableSchema += HiveUtils.TABLE_CREATE + HiveUtils.HIVE_TABLE_DEFAULT_NAME + " (";
  }

  public HiveTable(String name) {
    if (name == null || name == ""){
      name = HiveUtils.HIVE_TABLE_DEFAULT_NAME;
    }

    tableSchema += HiveUtils.TABLE_CREATE + name + " (\n";
  }

  /**
   * Close hive table
   */
  public void close() {
    tableSchema += "\n)";
  }

  public void addJsonSerde(String serdeClassName){
    tableSchema += "\nROW FORMAT SERDE '"+serdeClassName+"'";
  }

  public void addJsonSerde(){
    addJsonSerde("com.cloudera.hive.serde.JSONSerDe");
  }

  public String getSchema(){
    return tableSchema;
  }

  public void addPrimitive(String name, String value) {
    if (addComma > 0) {
      tableSchema += ",\n";
    }
    addComma++;
    tableSchema += HiveUtils.fieldWrapper(name) + " " + HiveUtils.findType(value);
  }

  public void AddUnknow(String name) {
    if (addComma > 0) {
      tableSchema += ",\n";
    }
    addComma++;
    tableSchema += HiveUtils.fieldWrapper(name) + " " + HiveUtils.STRING;
  }

  public void addStructure(String name, String value) {
    if (addComma > 0) {
      tableSchema += ",\n";
    }
    addComma++;
    tableSchema += HiveUtils.fieldWrapper(name) + " " + HiveUtils.STRUCT + "< " + HiveUtils.struct(value, 0) + " >";
  }

  public void addArray(String name, String value) {
    if (addComma > 0) {
      tableSchema += ",\n";
    }
    addComma++;
    if (value.equals("[]")) {
      tableSchema += HiveUtils.fieldWrapper(name) + " " + HiveUtils.DEFAULT_ARRAY;
    } else {
      tableSchema += HiveUtils.fieldWrapper(name) + " " + HiveUtils.ARRAY + "< " + HiveUtils.array(value) + " >";
    }
  }

}
