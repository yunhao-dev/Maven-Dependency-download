package com.wild.maven.util;

import java.util.Arrays;
import java.util.List;

/**
 * @description: URL工具类
 * @Author: yunhao_dev
 * @Date: 2024/8/13 1:04
 */
public class UrlUtils {
    private static final String URL_PATH_SEPARATOR = "/";
    private static final String SPECIAL_CHAR = "*^:|.\\";

    /**
     *
     * @param str
     * @param separator
     * @return
     */
    public static List<String> splitBySeparator(String str, String separator) {
        // 使用指定的分隔符切割字符串，并将结果转换为 List
        String[] parts = str.split(separator);

        return Arrays.asList(parts);
    }

    /**
     * 拼接URL路径
     * @param baseUrl https://www.google.com
     * @param pathList  ['search']
     * @return  https://www.google.com/search
     */
    public static String buildUrlPath(String baseUrl, List<String> pathList){
        if (baseUrl == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(baseUrl);
        if(baseUrl.endsWith(URL_PATH_SEPARATOR)){
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        if(pathList != null && !pathList.isEmpty()){
            for (String path : pathList) {
                stringBuilder.append(URL_PATH_SEPARATOR).append(path);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 拼接URL路径
     * @param baseUrl https://www.google.com
     * @param path  search
     * @return  https://www.google.com/search
     */
    public static String buildUrlPath(String baseUrl, String path){
        if (baseUrl == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(baseUrl);
        stringBuilder.append(URL_PATH_SEPARATOR).append(path);
        return stringBuilder.toString();
    }

}
