package com.wild.maven.util;

import java.util.List;

/**
 * @description:
 * @Author: yunhao_dev
 * @Date: 2024/8/13 1:54
 */

public class TestUrlUtils {
    public static void main(String[] args) {
        String path = "org,aliball";
        List<String> list = UrlUtils.splitBySeparator(path, ",");
    }
}
