/*
 * Copyright © 文科中的技术宅
 * blog:https://www.townwang.com
 */
package com.townwang.logutils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * @author Town
 * @created at 2018/7/5 16:30
 * @Last Modified by: Town
 * @Last Email: android@townwang.com
 * @Last Modified time: 2018/7/5 16:30
 * @Remarks Log日志封装类
 */
public class Log {

    private Log() {
        throw new UnsupportedOperationException("不能实例化");
    }

    private static String TAG = "====>";
    /**
     * 是否需要打印bug  true 打印 fasle 不打印
     */
    private static boolean isDebug = true;

    private final static int SUBSTRINGLENG = 4;

    private final static String DELDATE = "}\r\n";

    private static long currentTime; //打印耗时时间

    private static JSONObject parseResponse;//最终被解析的JSON对象

    private static boolean isdelCb;//是否删除多余的JSONObject(默认的JSONArry会被嵌套了一个JSONObject)
    private static StringBuilder josnBuilder = new StringBuilder();//最终输出的数据容器

    /**
     * 设置日志配置
     *
     * @param TAG     过滤TAG
     * @param isDebug 是否打印
     */
    public static void setConfig(String TAG, boolean isDebug) {
        Log.isDebug = isDebug;
        Log.TAG = TAG + "====>";
    }

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            android.util.Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            android.util.Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            android.util.Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            android.util.Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            android.util.Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            android.util.Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            android.util.Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            android.util.Log.i(tag, msg);
    }

    /**
     * Json格式输出Log
     *
     * @param tag     标签
     * @param url     请求Url
     * @param message Json字符串
     */
    public static void json(String tag, String url, String message) {
        JSONObject myJsonObject = null;
        try {
            myJsonObject = new JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (isDebug)
            outputFormatJson(tag, url, myJsonObject);
    }


    /***
     * 功能：线程同步,输出日志
     * @param response  Json对象
     * @param TagName 输出台标签名：必传。
     * */
    public static void outputFormatJson(String TagName, String url, Object response) {
        synchronized (Log.class) {
            try {
                currentTime = System.currentTimeMillis();
                if (response instanceof JSONArray) {
                    isdelCb = true;
                    JSONObject tempJson = new JSONObject();
                    parseResponse = tempJson.putOpt("", response);
                } else {
                    isdelCb = false;
                    parseResponse = ((JSONObject) response);
                }
                synchronized (Log.class) {
                    outputFormatJson(TagName, url, parseResponse, isdelCb);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * 功能：
     * */
    private static void outputFormatJson(String TagName, String url, JSONObject response, boolean isdelCb) {
        startaparse(TagName, url, response, isdelCb);
    }

    /**
     * 功能 ：添加外层花括号
     *
     * @param isdelCb :对JSONArry去掉外层手动添加的JSONObject
     */
    private static void startaparse(String TagName, String url, JSONObject response, boolean isdelCb) {
        if (checkParams(TagName, response)) {
            return;
        }
        josnBuilder.setLength(0);
        appendSb("标签==>" + TagName, false);
        appendSb("____________________________________________________________________", false);
        appendSb("\n请求地址==>" + url, false);
        appendSb("____________________________________________________________________", false);
        appendSb("{", false);
        prinfrmatJson(TagName, response);
        appendSb("}", false);
        appendSb("_____________________________" + " 耗时" + (System.currentTimeMillis() - currentTime) + "毫秒_____________________________", false);
        if (isdelCb) {
            josnBuilder.delete(josnBuilder.indexOf("{"), josnBuilder.indexOf("["));
            josnBuilder.delete(josnBuilder.lastIndexOf(DELDATE), josnBuilder.lastIndexOf(DELDATE) + SUBSTRINGLENG);
        }
        logOut(TagName, josnBuilder.toString());
    }

    /**
     * 功能：检查参数是否异常：
     *
     * @param TagName  必填
     * @param response 必填
     */
    private static boolean checkParams(String TagName, JSONObject response) {
        if (null == response || TextUtils.isEmpty(response.toString()) || TextUtils.isEmpty(TagName)) {
            return true;
        }
        return false;
    }

    /**
     * 功能：遍历所有子json对象,并对孩子进行递归操作
     * 对JSONobject,JSONArray,Object。进行区分判断。
     */
    private static void prinfrmatJson(String TagName, JSONObject response) {
        try {
            Iterator<String> jsonobject = response.keys();
            while (jsonobject.hasNext()) {

                String key = jsonobject.next();

                if (response.get(key) instanceof JSONObject) {

                    appendSb("\"" + key + "\"" + ":{", false);

                    prinfrmatJson(TagName, (JSONObject) response.get(key));

                    boolean isendValue = jsonobject.hasNext();//判断是否还有下一个元素

                    appendSb("  }", isendValue);

                } else if (response.get(key) instanceof JSONArray) {

                    appendSb("\"" + key + "\"" + ":[", false);

                    itemrArray(TagName, (JSONArray) (response.get(key)));

                    boolean isendValue = jsonobject.hasNext();//判断是否还有下一个元素

                    appendSb(" " + " ]", isendValue);

                } else if (response.get(key) != null) {

                    boolean isendValue = jsonobject.hasNext();//判断是否还有下一个元素
                    getTypeData(response, key, !isendValue);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：对基本类型进行转换（String,其他的类型其实不用处理）
     * 说明：对null,进行非空处理，对字符串进行添加 "" 操作
     */
    private static void getTypeData(JSONObject response, String key, boolean isEndValue) {
        try {
            if (response.get(key) instanceof Integer) {
                int value = (int) response.get(key);
                appendSb("\t" + "\"" + key + "\"" + ":" + value + "", !isEndValue);

            } else if (response.get(key) instanceof String || null == response.get(key) || TextUtils.equals("null", response.get(key).toString())) {

                if (response.get(key) instanceof String) {

                    String value = (String) response.get(key);
                    if (null == value) {
                        appendSb("\t" + "\"" + key + "\"" + ":" + null, !isEndValue);
                    } else {
                        appendSb("\t" + "\"" + key + "\"" + ":" + "\"" + value + "\"", !isEndValue);
                    }
                } else if (TextUtils.equals("null", response.get(key).toString())) {
                    appendSb("\t" + "\"" + key + "\"" + ":" + null, !isEndValue);

                } else {
                    String value = (String) response.get(key);
                    if (null == value) {
                        appendSb("\t" + "\"" + key + "\"" + ":" + null, !isEndValue);

                    } else {
                        appendSb("\t" + "\"" + key + "\"" + ":" + "\"" + value + "\"", !isEndValue);
                    }
                }
            } else if (response.get(key) instanceof Float) {
                float value = (float) response.get(key);

                appendSb("\t" + "\"" + key + "\"" + ":" + "\"" + value + "\"", !isEndValue);

            } else if (response.get(key) instanceof Double) {

                double value = (double) response.get(key);

                appendSb("\t" + "\"" + key + "\"" + ":" + "\"" + value + "\"", !isEndValue);

            } else if (response.get(key) instanceof Boolean) {

                boolean value = (boolean) response.get(key);

                appendSb("\t" + "\"" + key + "\"" + ":" + "\"" + value + "\"", !isEndValue);

            } else if (response.get(key) instanceof Character) {

                char value = (char) response.get(key);

                appendSb("\t" + "\"" + key + "\"" + ":" + "\"" + value + "\"", !isEndValue);

            } else if (response.get(key) instanceof Long) {

                long value = (long) response.get(key);

                appendSb("\t" + "\"" + key + "\"" + ":" + "\"" + value + "\"", !isEndValue);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception es) {
            es.printStackTrace();
        }
    }

    /**
     * 功能：对JSONArray进行遍历
     *
     * @param TagName
     * @param response;
     */
    private static void itemrArray(String TagName, JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                if (response.get(i) instanceof JSONObject) {
                    appendSb("{", false);
                    prinfrmatJson(TagName, (JSONObject) response.get(i));
                    appendSb("  }", response.length() > i + 1);

                } else if (response.get(i) instanceof JSONArray) {
                    itemrArray(TagName, (JSONArray) response.get(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：添加数据，以及逗号，换行。
     *
     * @param addComma 逗号。
     */
    private static void appendSb(String append, boolean addComma) {
        josnBuilder.append(append);
        if (addComma) {
            josnBuilder.append(",");
        }
        josnBuilder.append("\r\n");
    }

    /**
     * 功能： LOG输出长度有限制。（需分层输出）
     *
     * @param content
     * @parac max:通过测试不建议修改数据值，修改成4000,会丢失数据。
     */
    private static void logOut(String tagName, String content) {
        int max = 3900;
        long length = content.length();
        if (length < max || length == max) {
            Log.i(TAG + tagName, content);
        } else {
            while (content.length() > max) {
                String logContent = content.substring(0, max);
                content = content.replace(logContent, "");
                Log.i(TAG + tagName, logContent);
            }
            Log.i(TAG + tagName, content);
        }
    }
}