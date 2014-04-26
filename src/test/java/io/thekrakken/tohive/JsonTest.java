package io.thekrakken.tohive;

import static org.junit.Assert.*;
import io.thekrakken.json2hive.Json;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.gson.JsonSyntaxException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JsonTest {

  public static final String TBL_NAME = "TEST";

  @Test
  public void test001_simpleTable() throws JsonSyntaxException, Exception {
    String json = "{\"val\":\"stuffffff\"}";
    String expected = "CREATE EXTERNAL TABLE "+TBL_NAME+" (\nval STRING\n)";
    assertEquals(expected, Json.create(TBL_NAME, json));
  }

  @Test
  public void test002_tableWithStringAndNumber() throws JsonSyntaxException, Exception {
    String json = "{\"val\":\"stuffffff\", \"val2\":42}";
    String expected = "CREATE EXTERNAL TABLE "+TBL_NAME+" (\nval STRING,\nval2 INT\n)";
    assertEquals(expected, Json.create(TBL_NAME, json));
  }
  @Test
  public void test003_json() throws JsonSyntaxException, Exception{
      String json = "{"
              + "\"test\":\"I'm thinking of what should've been and what could've been if.......\","
              + "\"id\": 318594635540865000"
              + "}";
      assertEquals("CREATE EXTERNAL TABLE "+TBL_NAME+" (\ntest STRING,\nid BIGINT\n)", Json.create(TBL_NAME, json));
  }

  @Test
  public void test004_simpleStructure() throws JsonSyntaxException, Exception {
    String json = "{\"user\":{\"id\":5645454}}";
    String expected = "CREATE EXTERNAL TABLE "+TBL_NAME+" (\nuser STRUCT< id:BIGINT >\n)";
    assertEquals(expected, Json.create(TBL_NAME, json));
  }

  @Test
  public void test005_simpleArray() throws JsonSyntaxException, Exception {
    String json = "{\"user\":[5645454]}";
    String expected = "CREATE EXTERNAL TABLE "+TBL_NAME+" (\nuser ARRAY< BIGINT >\n)";
    assertEquals(expected, Json.create(TBL_NAME, json));
  }

  @Test
  public void test006_tweet() throws JsonSyntaxException, Exception {
    String json = "{"
        + "\"id\": 318594635540865000,"
        + "\"id_str\": \"294716126003355648\","
        + "\"text\":\"I'm thinking of what should've been and what could've been if.......\","
        + "\"truncated\": false,"
        + "\"in_reply_to_status_id\": 294715856800337900,"
        + "\"in_reply_to_status_id_str\": \"294715856800337921\","
        + "\"user\": {"
        + "\"id\": 118547315,"
        + "\"id_str\": \"118547315\","
        + "\"name\": \"Rell | T3\","
        + "\"screen_name\": \"iamRellOnDaBeat\"," +
        "\"location\": \"Texas\","
        + "\"url\": \"http://www.youtube.com/user/iamRellOnDaBeat\","
        + "\"description\": \"Prod Credits: Konverse Kali, GRON, Donye, SyAriDaKid, KCamp, Bambino Gold, Eldorado Red, Dirt Gang, Brian (Day 26), Yung Teddy, BoDeal, CartelMGM, Nino Cahootz\","
        + "\"protected\": false,"
        + "\"followers_count\": 5424,"
        + "\"friends_count\": 1812,"
        + "\"listed_count\": 18,"
        + "\"created_at\": \"Mon Mar 01 01:25:00 +0000 2010\","
        + "\"favourites_count\": 66,"
        + "\"utc_offset\": -21600,"
        + "\"time_zone\": \"Central Time (US & Canada)\","
        + "\"geo_enabled\": false,"
        + "\"verified\": false,"
        + "\"statuses_count\": 45337,"
        + "\"lang\": \"en\","
        + "\"contributors_enabled\": false,"
        + "\"is_translator\": false,"
        + "\"profile_background_color\": \"131516\","
        + "\"profile_background_image_url\": \"http://a0.twimg.com/profile_background_images/756084498/561062e5d3d96a42181349b3940b44f9.jpeg\","
        + "\"profile_background_image_url_https\": \"https://si0.twimg.com/profile_background_images/756084498/561062e5d3d96a42181349b3940b44f9.jpeg\","
        + "\"profile_background_tile\": true,"
        + "\"profile_image_url\": \"http://a0.twimg.com/profile_images/3159852094/cb297eb55ab2f8a816ce951f20c68807_normal.jpeg\","
        + "\"profile_image_url_https\": \"https://si0.twimg.com/profile_images/3159852094/cb297eb55ab2f8a816ce951f20c68807_normal.jpeg\","
        + "\"profile_banner_url\": \"https://si0.twimg.com/profile_banners/118547315/1357395328\","
        + "\"profile_link_color\": \"800099\","
        + "\"profile_sidebar_border_color\": \"000000\","
        + "\"profile_sidebar_fill_color\": \"EFEFEF\","
        + "\"profile_text_color\": \"000000\","
        + "\"profile_use_background_image\": true,"
        + "\"default_profile\": false,"
        + "\"default_profile_image\": false,"
        + "\"following\": null,"
        + "\"follow_request_sent\": null,"
        + "\"notifications\": null"
        + "}"
        + "}";

    String expected ="CREATE EXTERNAL TABLE "+TBL_NAME+" (\n" +
    		"id BIGINT,\n" +
    		"id_str STRING,\n" +
    		"text STRING,\n" +
    		"truncated BOOLEAN,\n" +
    		"in_reply_to_status_id BIGINT,\n" +
    		"in_reply_to_status_id_str STRING,\n" +
    		"user STRUCT< " +
    		"id:BIGINT," +
    		"id_str:STRING," +
    		"name:STRING," +
    		"screen_name:STRING," +
    		"location:STRING," +
    		"url:STRING," +
    		"description:STRING," +
    		"protected:BOOLEAN," +
    		"followers_count:INT," +
    		"friends_count:INT," +
    		"listed_count:INT," +
    		"created_at:STRING," +
    		"favourites_count:INT," +
    		"utc_offset:INT," +
    		"time_zone:STRING," +
    		"geo_enabled:BOOLEAN," +
    		"verified:BOOLEAN," +
    		"statuses_count:BIGINT," +
    		"lang:STRING," +
    		"contributors_enabled:BOOLEAN," +
    		"is_translator:BOOLEAN," +
    		"profile_background_color:STRING," +
    		"profile_background_image_url:STRING," +
    		"profile_background_image_url_https:STRING," +
    		"profile_background_tile:BOOLEAN," +
    		"profile_image_url:STRING," +
    		"profile_image_url_https:STRING," +
    		"profile_banner_url:STRING," +
    		"profile_link_color:STRING," +
    		"profile_sidebar_border_color:STRING," +
    		"profile_sidebar_fill_color:STRING," +
    		"profile_text_color:STRING," +
    		"profile_use_background_image:BOOLEAN," +
    		"default_profile:BOOLEAN," +
    		"default_profile_image:BOOLEAN," +
    		"following:STRING," +
    		"follow_request_sent:STRING," +
    		"notifications:STRING" +
    		" >\n" +
    		")";

    assertEquals(expected, Json.create(TBL_NAME, json));
  }

  @Test
  public void test006_tweetWithSerDe() throws JsonSyntaxException, Exception {
    Boolean withSerde = true;
    String json = "{"
        + "\"id\": 318594635540865000,"
        + "\"id_str\": \"294716126003355648\","
        + "\"text\":\"I'm thinking of what should've been and what could've been if.......\","
        + "\"truncated\": false,"
        + "\"in_reply_to_status_id\": 294715856800337900,"
        + "\"in_reply_to_status_id_str\": \"294715856800337921\","
        + "\"user\": {"
        + "\"id\": 118547315,"
        + "\"id_str\": \"118547315\","
        + "\"name\": \"Rell | T3\","
        + "\"screen_name\": \"iamRellOnDaBeat\"," +
        "\"location\": \"Texas\","
        + "\"url\": \"http://www.youtube.com/user/iamRellOnDaBeat\","
        + "\"description\": \"Prod Credits: Konverse Kali, GRON, Donye, SyAriDaKid, KCamp, Bambino Gold, Eldorado Red, Dirt Gang, Brian (Day 26), Yung Teddy, BoDeal, CartelMGM, Nino Cahootz\","
        + "\"protected\": false,"
        + "\"followers_count\": 5424,"
        + "\"friends_count\": 1812,"
        + "\"listed_count\": 18,"
        + "\"created_at\": \"Mon Mar 01 01:25:00 +0000 2010\","
        + "\"favourites_count\": 66,"
        + "\"utc_offset\": -21600,"
        + "\"time_zone\": \"Central Time (US & Canada)\","
        + "\"geo_enabled\": false,"
        + "\"verified\": false,"
        + "\"statuses_count\": 45337,"
        + "\"lang\": \"en\","
        + "\"contributors_enabled\": false,"
        + "\"is_translator\": false,"
        + "\"profile_background_color\": \"131516\","
        + "\"profile_background_image_url\": \"http://a0.twimg.com/profile_background_images/756084498/561062e5d3d96a42181349b3940b44f9.jpeg\","
        + "\"profile_background_image_url_https\": \"https://si0.twimg.com/profile_background_images/756084498/561062e5d3d96a42181349b3940b44f9.jpeg\","
        + "\"profile_background_tile\": true,"
        + "\"profile_image_url\": \"http://a0.twimg.com/profile_images/3159852094/cb297eb55ab2f8a816ce951f20c68807_normal.jpeg\","
        + "\"profile_image_url_https\": \"https://si0.twimg.com/profile_images/3159852094/cb297eb55ab2f8a816ce951f20c68807_normal.jpeg\","
        + "\"profile_banner_url\": \"https://si0.twimg.com/profile_banners/118547315/1357395328\","
        + "\"profile_link_color\": \"800099\","
        + "\"profile_sidebar_border_color\": \"000000\","
        + "\"profile_sidebar_fill_color\": \"EFEFEF\","
        + "\"profile_text_color\": \"000000\","
        + "\"profile_use_background_image\": true,"
        + "\"default_profile\": false,"
        + "\"default_profile_image\": false,"
        + "\"following\": null,"
        + "\"follow_request_sent\": null,"
        + "\"notifications\": null"
        + "}"
        + "}";

    String expected ="CREATE EXTERNAL TABLE "+TBL_NAME+" (\n" +
            "id BIGINT,\n" +
            "id_str STRING,\n" +
            "text STRING,\n" +
            "truncated BOOLEAN,\n" +
            "in_reply_to_status_id BIGINT,\n" +
            "in_reply_to_status_id_str STRING,\n" +
            "user STRUCT< " +
            "id:BIGINT," +
            "id_str:STRING," +
            "name:STRING," +
            "screen_name:STRING," +
            "location:STRING," +
            "url:STRING," +
            "description:STRING," +
            "protected:BOOLEAN," +
            "followers_count:INT," +
            "friends_count:INT," +
            "listed_count:INT," +
            "created_at:STRING," +
            "favourites_count:INT," +
            "utc_offset:INT," +
            "time_zone:STRING," +
            "geo_enabled:BOOLEAN," +
            "verified:BOOLEAN," +
            "statuses_count:BIGINT," +
            "lang:STRING," +
            "contributors_enabled:BOOLEAN," +
            "is_translator:BOOLEAN," +
            "profile_background_color:STRING," +
            "profile_background_image_url:STRING," +
            "profile_background_image_url_https:STRING," +
            "profile_background_tile:BOOLEAN," +
            "profile_image_url:STRING," +
            "profile_image_url_https:STRING," +
            "profile_banner_url:STRING," +
            "profile_link_color:STRING," +
            "profile_sidebar_border_color:STRING," +
            "profile_sidebar_fill_color:STRING," +
            "profile_text_color:STRING," +
            "profile_use_background_image:BOOLEAN," +
            "default_profile:BOOLEAN," +
            "default_profile_image:BOOLEAN," +
            "following:STRING," +
            "follow_request_sent:STRING," +
            "notifications:STRING" +
            " >\n" +
            ")\n" +
            "ROW FORMAT SERDE 'com.cloudera.hive.serde.JSONSerDe'";

    assertEquals(expected, Json.create(TBL_NAME, json, withSerde, null));
  }
}
