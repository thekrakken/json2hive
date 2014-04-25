package io.thekrakken.tohive;

public class HiveTable {


  private String tableSchema = "";
  private Integer addComma = 0;

  public HiveTable() {
    tableSchema += hiveUtils.TABLE_CREATE + hiveUtils.HIVE_TABLE_DEFAULT_NAME + " (";
  }

  public HiveTable(String name) {

    tableSchema += hiveUtils.TABLE_CREATE + name + " (\n";
  }

  /**
   * Close hive table
   */
  public void close() {
    tableSchema += "\n)";
  }

  public String getSchema(){
    return tableSchema;
  }

  public void addPrimitive(String name, String value) {
    if (addComma > 0) {
      tableSchema += ",\n";
    }
    addComma++;
    tableSchema += name + " " + hiveUtils.findType(value);
  }

  public void AddUnknow(String name) {
    if (addComma > 0) {
      tableSchema += ",\n";
    }
    addComma++;
    tableSchema += name + " " + hiveUtils.STRING;
  }

  public void addStructure(String name, String value) {
    if (addComma > 0) {
      tableSchema += ",\n";
    }
    addComma++;
    tableSchema += name + " " + hiveUtils.STRUCT + "<\n" + hiveUtils.struct(value, 0) + "\n\t>";
  }

  public void addArray(String name, String value) {
    if (addComma > 0) {
      tableSchema += ",\n";
    }
    addComma++;
    if (value.equals("[]")) {
      tableSchema += name + " " + hiveUtils.DEFAULT_ARRAY;
    } else {
      tableSchema += name + " " + hiveUtils.ARRAY + "<\n" + hiveUtils.array(value) + "\n\t>";
    }
  }

}
