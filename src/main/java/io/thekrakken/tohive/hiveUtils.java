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

import java.util.regex.Pattern;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map.Entry;

/**
 * Usefull tools for creation of the hive table schema
 *
 * @author anthonycorbacho
 * @since 0.0.1
 */
public class hiveUtils {

  public final static String TABLE_CREATE = "CREATE EXTERNAL TABLE ";
  public final static String HIVE_TABLE_DEFAULT_NAME = "MY_TABLE";

  private static Integer COMMA = 0;

  public static final Pattern DATE_PATTERN = Pattern
      .compile("^[\"]?([0-9]{4}[-/][0-9]{2}[-/][0-9]{2})[T ]"+
              "([0-9]{2}:[0-9]{2}:[0-9]{2})"+
              "([ ]?([-+][0-9]{2}[:]?[0-9]{2})|Z)[\"]?$");

  public static final Pattern HEX_PATTERN = Pattern.compile("^([0-9a-fA-F][0-9a-fA-F])*$");

  // primitive
  public final static String STRING = "STRING";
  public final static String TINYINT = "TINYINT";
  public final static String SMALLINT = "SMALLINT";
  public final static String INT = "INT";
  public final static String BIGINT = "BIGINT";
  public final static String BOOLEAN = "BOOLEAN";
  public final static String FLOAT = "FLOAT";
  public final static String DOUBLE = "DOUBLE";
  public final static String BINARY = "BINARY";
  public final static String TIMESTAMP = "TIMESTAMP";
  public final static String DECIMAL = "DECIMAL";

  // complex
  public final static String ARRAY = "ARRAY";
  public final static String MAP = "MAP";
  public final static String STRUCT = "STRUCT";
  public final static String UNIONTYPE = "UNIONTYPE";

  public final static String DEFAULT_ARRAY = " ARRAY<STRING>";
  public final static String DEFAULT = " STRING";

  /**
   * Find the hive field type
   * @param value
   * @return
   */
  @SuppressWarnings("unused")
  public static String findType(String value) {

    if (value.toLowerCase().equals("true") || value.toLowerCase().equals("false"))
      return BOOLEAN;
    try {
      byte d = Byte.parseByte(value);
    } catch (NumberFormatException nfe) {
      try {
        short d = Short.parseShort(value);
      } catch (NumberFormatException nfe2) {
        try {
          int d = Integer.parseInt(value);
          // logger.warning("="+d);
        } catch (NumberFormatException nfe3) {
          try {
            long d = Long.parseLong(value);
          } catch (NumberFormatException nfe4) {
            try {
              float d = Float.parseFloat(value);
            } catch (NumberFormatException nfe5) {
              try {
                double d = Double.parseDouble(value);
              } catch (NumberFormatException nfe6) {
                if (DATE_PATTERN.matcher(value).matches())
                  return TIMESTAMP;
                else if (HEX_PATTERN.matcher(value).matches())
                  return BINARY;
                else
                  return STRING;
              }
              return DOUBLE;
            }
            return BIGINT;
          }
          return BIGINT;
        }
        return BIGINT;
      }
      return INT;
    }
    return INT;
  }

  public static String findType(Object value) {
    if (value instanceof String) return STRING;
    if (value instanceof Byte) return INT;
    if (value instanceof Short) return INT;
    if (value instanceof Integer) return INT;
    if (value instanceof Long) return BIGINT;
    if (value instanceof Double) return BIGINT;
    if (value instanceof Float) return BIGINT;
    if (value instanceof Boolean) return BOOLEAN;
    if (DATE_PATTERN.matcher(value.toString()).matches()) return TIMESTAMP;
    if (HEX_PATTERN.matcher(value.toString()).matches()) return BINARY;

    /** No type found ... */
    return STRING;
  }

  public static String array(String value) {

    /** Empty array or struct? .... */
    if (value.equals("[]") || value.equals("{}"))
      return STRING;

    if (value.startsWith("[")) {
      JsonParser parser = new JsonParser();
      /** little tweek */
      JsonElement jsonElement = parser.parse("{\"tmp\":" + value + "}");
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
        JsonElement valu = entry.getValue();
        value = valu.getAsJsonArray().get(0).toString();
        break;
      }
    }
    if (value.startsWith("{")) {
      return arrayToStruct(value);
    } else {
      return findType(value);
    }
  }

  public static String arrayToStruct(String jsonObject) {
    String val = "";
    val += STRUCT + "<" + struct(jsonObject.toString(), 1) + ">";
    return val;
  }

  public static String struct(String value, int nasted) {

    if (value == null)
      return " ";
    if (value.isEmpty())
      return " ";
    if (nasted < 0)
      nasted = 0;
    JsonParser parser = new JsonParser();
    JsonElement jsonElement = parser.parse(value);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    String struct = "";
    COMMA = 0;
    for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
      String key = entry.getKey();
      JsonElement val = entry.getValue();
      // 3 Possibilities
      // 1. structures
      // 2. array
      // 3. primitive

      if (val.isJsonObject() && nasted >= 1) {
        if (COMMA > 0) {
          struct += ",";
        }
        COMMA = 0;
        struct += key + ":" + STRUCT + "<" + struct(val.toString(), 1) + ">";
      }
      // Array
      else if (val.isJsonArray()) {
        if (COMMA > 0) {
          struct += ",";
        }
        COMMA++;

        struct += key + ":" + ARRAY + "<" + array(val.toString()) + ">";
      } else { // normal field
        if (COMMA > 0) {
          struct += ",";
        }
        COMMA++;

        if (val.isJsonNull()) {
          struct += key + ":" + STRING;
        } else {
          struct += key + ":" + findType(val.toString());
        }
      }
    }
    return struct;
  }

}
