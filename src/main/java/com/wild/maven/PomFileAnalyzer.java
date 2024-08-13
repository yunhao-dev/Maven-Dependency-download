package com.wild.maven;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.wild.maven.model.Dependencie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 解析Pom.xml
 * @Author: yunhao_dev
 * @Date: 2024/8/12 2:39
 */
public class PomFileAnalyzer {
    private static final Logger log = Logger.getInstance(PomFileAnalyzer.class);

    /**
     * 正则表达式
     */
    private static final String regex = "<dependency>.*?</dependency>";
    private static final String groupIdRegex = "<groupId>(.*?)</groupId>";
    private static final String artifactIdRegex = "<artifactId>(.*?)</artifactId>";
    private static final String versionRegex = "<version>(.*?)</version>";
    private static final String placeholderPattern = "^\\$\\{(.*)\\}$";
    private static final String PROPERTIES = "<properties>(.*?)</properties>";
    private static final String NOTES = "<!--.*?-->";
    private static final String NULL_CHAR = "";
    private static final String PROPERTY_NAME = "<(.*?)>(.*?)<.*?>";

    private static final Map<String,String> propertyNameMap = new HashMap<>();

    public PomFileAnalyzer() {}

    public static List<Dependencie> getDependencies(VirtualFile file) {
        String filePath = file.getPath();
        StringBuilder content = new StringBuilder();

        // 读取文件内容
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 替换占位符号
        String replaceAll = content.toString().replaceAll(NOTES, NULL_CHAR);
        propertyNameMap.clear();
        resolvePlaceholders(replaceAll);
        // 正则表达式匹配所有<dependency>标签
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher dependencyMatcher = pattern.matcher(replaceAll);

        List<Dependencie> dependencies = new ArrayList<>();

        while (dependencyMatcher.find()) {
            String dependencyContent = dependencyMatcher.group();

            Matcher groupIdMatcher = Pattern.compile(groupIdRegex).matcher(dependencyContent);
            Matcher artifactIdMatcher = Pattern.compile(artifactIdRegex).matcher(dependencyContent);
            Matcher versionMatcher = Pattern.compile(versionRegex).matcher(dependencyContent);

            if (groupIdMatcher.find() && artifactIdMatcher.find() && versionMatcher.find()) {
                Dependencie dependencie = new Dependencie();
                dependencie.setGroupId(groupIdMatcher.group(1));
                dependencie.setArtifactId(artifactIdMatcher.group(1));
                String version = versionMatcher.group(1);
                Pattern compileVersion = Pattern.compile(placeholderPattern);
                Matcher versionPlaceMatcher = compileVersion.matcher(version);
                if(versionPlaceMatcher.find()){
                    String property = versionPlaceMatcher.group(1);
                    if(propertyNameMap.containsKey(property)){
                        version = propertyNameMap.get(property);
                    }
                }
                dependencie.setVersion(version);
                dependencies.add(dependencie);
            }
        }
        return dependencies;
    }
    /**
     * 替换字符串中的占位符为实际值
     * @param content 需要替换的内容
     * @return 替换后的内容
     */
    private static void resolvePlaceholders(String content) {

        Pattern pattern = Pattern.compile(PROPERTIES, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String placeholder = matcher.group(1);
            Matcher propertyMatch = Pattern.compile(PROPERTY_NAME,Pattern.DOTALL).matcher(placeholder);

            while (propertyMatch.find()){
                String key = propertyMatch.group(1);
                String value = propertyMatch.group(2);
                propertyNameMap.put(key,value);
            }
        }
    }

}
