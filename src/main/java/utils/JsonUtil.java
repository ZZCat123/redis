package utils;

import com.google.gson.Gson;

/**
 * @Class JsonUtil
 * @Description: TODO
 * @Author: luozhen
 * @Create: 2018/09/27 16:08
 */
public class JsonUtil {

    static final Gson gson = new Gson();

    public static String ObjectToJson(Object object) {
        return gson.toJson(object);
    }

    public static Object JsonToObject(String json, Class clazz) {
        return gson.fromJson(json, clazz);
    }

}
