package com.companyname.projectname.utils;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Created by ishan.bansal on 6/16/15.
 */
public class JsonUtils {

    public static String getStringValue(JSONObject jsonObject, String key) {
        if (jsonObject == null) {
            return null;
        }
        Object value = jsonObject.get(key);
        if (value == null)
            return null;
        return (String) value;
    }

    public static JSONObject jsonOf(Object... args) {
        JSONObject result = new JSONObject();
        if (args.length < 1) {
            return result;
        }
        if (args.length % 2 != 0) {
            return result;
        }
        for (int i = 0; i < args.length; i += 2) {
            Object key = args[i];
            Object value = args[i + 1];
            if ((key instanceof String)) {
                result.put(key, value);
            }
        }
        return result;
    }

    public static boolean getBooleanValue(JSONObject jsonObject, String key) {
        if (jsonObject == null) {
            return false;
        }
        Object value = jsonObject.get(key);
        if (!(value instanceof Boolean))
            return false;
        return (Boolean) value;
    }

    public static int getIntegerValue(JSONObject jsonObject, String key) {
        if (jsonObject == null) {
            return -1;
        }
        Object value = jsonObject.get(key);
        if (!(value instanceof Number))
            return -1;
        return ((Number) value).intValue();
    }

    public static long getLongValue(JSONObject jsonObject, String key) {
        if (jsonObject == null) {
            return -1;
        }
        Object value = jsonObject.get(key);
        if (!(value instanceof Number))
            return -1;
        return ((Number) value).longValue();
    }

    public static double getDoubleValue(JSONObject jsonObject, String key) {
        if (jsonObject == null) {
            return -1;
        }
        Object value = jsonObject.get(key);
        if (!(value instanceof Number))
            return -1;
        return ((Number) value).doubleValue();
    }

    public static JSONObject toJsonObject(String json)  throws Exception {
        return  (JSONObject) JSONValue.parseWithException(json);
    }

}
