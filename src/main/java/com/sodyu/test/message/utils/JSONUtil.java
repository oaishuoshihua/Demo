package com.sodyu.test.message.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * (一句话描述该类的功能)
 *
 * @version 1.0.0
 * @date 2017/1/3 17:55
 */
public class JSONUtil {

    /**
     * 获取JSONArray
     * @param json
     * @param key
     * @return
     */
    public static JSONArray getJSONArray(JSONObject json, String key) {
        if (json == null) return null;
        try {
            return json.containsKey(key) ? json.getJSONArray(key) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取JSONObject
     * @param json
     * @param key
     * @return
     */
    public static JSONObject getJSONObject(JSONObject json, String key) {
        if (json == null) return null;
        return json.containsKey(key) ? json.getJSONObject(key) : null;
    }

    /**
     * 获取String
     * @param json
     * @param key
     * @return
     */
    public static String getString(JSONObject json, String key) {
        if (json == null) return "";
        return json.containsKey(key) ? json.getString(key) : "";
    }

    /**
     * 获取int
     * @param json
     * @param key
     * @return
     */
    public static int getInt(JSONObject json, String key) {
        if (json == null) return 0;
        return json.containsKey(key) ? json.getInteger(key) : 0;
    }

    /**
     * 获取String
     * @param json
     * @param key
     * @return
     */
    public static Boolean getBoolean(JSONObject json, String key) {
        if (json == null) return false;
        return json.containsKey(key) ? json.getBoolean(key) : false;
    }

    public static JSONObject getJSONObject(String json) {
        try {
            return json != null ? JSONObject.parseObject(json) : null;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
