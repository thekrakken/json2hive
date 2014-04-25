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

}
