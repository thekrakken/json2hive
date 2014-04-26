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
package io.thekrakken.json2hive;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map.Entry;

/**
 * Generates Hive schemas for use with the JSON SerDe from
 * com.cloudera.hive.serde.JSONSerDe (but can be change in the create method {@link #create(String, String, Boolean, String)})<p/>
 *
 * Pass in a valid JSON document string to {@link #create(String, String)} and it will
 * return a Hive schema for the JSON sample.<p/>
 *
 * It supports embedded JSON objects, arrays and the standard JSON scalar types: strings,
 * numbers, booleans and null.<p/>
 * You probably don't want null in the JSON document you provide
 * as Hive can't use that.<p/>
 *
 * This program uses the JSON parsing code from Gson and that code is included in this
 * library {@see build.gradle}.<p/>
 *
 * @author anthonycorbacho
 * @since 0.0.1
 */
public class Json {

  /**
   * Pass in any valid JSON object and a Hive schema will be returned for it.
   * You should avoid having null values in the JSON document, however.
   *
   * @param hiveTableName
   * @param jsonObject
   * @return hive table schema
   * @throws Exception
   * @throws JsonSyntaxException
   */
  public static String create(String hiveTableName, String jsonObject) {
    return createHiveTable(hiveTableName, jsonObject).getSchema();
  }


  /**
   * Pass in any valid JSON object and a Hive schema will be returned for it.
   * You should avoid having null values in the JSON document, however.
   *
   * @param hiveTableName
   * @param jsonObject
   * @param withSerde : True if you want to incluse SERDE
   * @param serde : Override serde classname (default: {@code com.cloudera.hive.serde.JSONSerDe})
   *
   * @return hive table schema
   * @throws Exception
   * @throws JsonSyntaxException
   */
  public static String create(String hiveTableName, String jsonObject, Boolean withSerde, String serde) {
    HiveTable hive = createHiveTable(hiveTableName, jsonObject);
    if (withSerde) {
      if (serde != null) {
        hive.addJsonSerde(serde);
      } else {
        hive.addJsonSerde();
      }
    }
    return hive.getSchema();
  }

  private static HiveTable createHiveTable(String hiveTableName, String jsonObject){
    HiveTable hive = new HiveTable(hiveTableName);

    JsonParser parser = new JsonParser();
    JsonElement jsonElement = parser.parse(jsonObject);
    JsonObject jsonObj = jsonElement.getAsJsonObject();

    for (Entry<String, JsonElement> entry : jsonObj.entrySet()) {
      String key = entry.getKey();
      JsonElement value = entry.getValue();

      /** Structure */
      if (value.isJsonObject()) {
        hive.addStructure(key, value.toString());
      }
      /** Array */
      else if (value.isJsonArray()) {
        hive.addArray(key, value.toString());
      } else {
        /** primitive */
        if (value.isJsonNull()) {
          hive.AddUnknow(value.toString());
        } else {
          hive.addPrimitive(key, value.toString());
        }
      }
    }
    hive.close();
    return hive;
  }

}
