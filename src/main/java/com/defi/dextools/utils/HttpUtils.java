package com.defi.dextools.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class HttpUtils {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final int TIMEOUT = 30000;
    public static final int FILE_TIMEOUT = 300000;

    // boundary就是request头和上传文件内容的分隔符
    public static final String BOUNDARY = "---------------------------123821742118716";
    public static final String RESULT_FILE = "attachment";

    // Http Get请求
    //////////////////////////////////////////////////////////////////

    /**
     * * 发送HTTP请求
     *
     * @param serverUrl
     * @param params
     * @return
     */
    public static String getHttp(String serverUrl, Map<String, String> params) {
        return getHttp(serverUrl, params, null, TIMEOUT);
    }

    /**
     * * 发送HTTP请求
     *
     * @param serverUrl
     * @param params
     * @return
     */
    public static String getHttp(String serverUrl, Map<String, String> params, Map<String, String> headers) {
        return getHttp(serverUrl, params, headers, TIMEOUT);
    }

    /**
     * 无参数Get请求
     *
     * @param url 请求地址
     * @return String
     */
    public static String getHttp(String url) {
        return getHttp(url, null, null);
    }

    /**
     * 发送HTTP请求
     *
     * @param serverUrl
     * @param params
     * @return
     */
    public static String getHttp(String serverUrl, Map<String, String> params, Map<String, String> headers,
                                 Integer time) {

        HttpURLConnection conn = null;
        BufferedReader br = null;
        try {
            // 构建请求参数
            StringBuilder sbParam = new StringBuilder(serverUrl);
            if (mapIsNotEmpty(params)) {
                sbParam.append("?");
                if (mapIsNotEmpty(params)) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        sbParam.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                        sbParam.append("=");
                        sbParam.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                        sbParam.append("&");
                    }
                }

            }
            // 发送请求
            URL url = new URL(sbParam.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.setRequestMethod(GET);
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setReadTimeout(time);
            conn.setConnectTimeout(time);
            if (mapIsNotEmpty(headers)) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }


            // 获取状态码
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {

                return "";
            }

            // 取得返回信息
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF_8));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            // JSON处理
            return result.toString();
        } catch (Exception e) {

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {

                }
            }
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {

                }
            }
        }

        return "";
    }

    /**
     * 判断Map是否不为空
     *
     * @param m
     * @return
     */
    public static boolean mapIsNotEmpty(Map<?, ?> m) {
        return !mapIsEmpty(m);
    }

    /**
     * 判断Map是否为空
     *
     * @param m
     * @return
     */
    public static boolean mapIsEmpty(Map<?, ?> m) {
        return m == null || m.isEmpty();
    }
}
