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
package io.thekrakken.tohive;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.Map.Entry;

/**
 * Create a Hive table from a JSON file
 *
 * @author anthonycorbacho
 * @since 0.0.1
 */
public class Json {

  public static Json EMPTY = new Json();

  public Json() {}


  /**
   * Create Hive table from the json file
   *
   * @param hiveTableName
   * @param jsonObject
   * @return
   */
  public static String create(String hiveTableName, String jsonObject) throws Exception,
      JsonSyntaxException {
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
    return hive.getSchema();
  }

  /**
   * Create Hive table from the json file
   *
   * @param hiveTableName
   * @param jsonObject
   * @return
   */
  public static String create(String hiveTableName, String jsonObject, Boolean withSerde, String serde) throws Exception,
      JsonSyntaxException {
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
    if (withSerde) {
      if (serde != null) {
        hive.addJsonSerde(serde);
      } else {
        hive.addJsonSerde();
      }
    }
    return hive.getSchema();
  }

}
