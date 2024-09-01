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
    private static final String DEPENDENCY_REGEX = "<dependency>.*?</dependency>";
    private static final String GROUP_ID_REGEX = "<groupId>(.*?)</groupId>";
    private static final String ARTIFACT_ID_REGEX = "<artifactId>(.*?)</artifactId>";
    private static final String VERSION_REGEX = "<version>(.*?)</version>";
    private static final String PARENT_REGEX = "<parent>.*?</parent>";
    private static final String BUILD_REGEX = "<build>.*?</build>";
    private static final String PLUGIN_REGEX = "<plugin>.*?</plugin>";
    private static final String PLACEHOLDER_PATTERN = "\\$\\{(.*?)\\}";
    private static final String PROPERTIES_REGEX = "<properties>(.*?)</properties>";
    private static final String NOTES_REGEX = "<!--.*?-->";
    private static final String NULL_CHAR = "";
    private static final String PROPERTY_NAME = "<(.*?)>(.*?)<.*?>";

    private static final Map<String, String> propertyNameMap = new HashMap<>();

    public PomFileAnalyzer() {}

    public static List<Dependencie> getAllDependencies(VirtualFile file) {
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

        // 去除注释并替换占位符
        String fileContent = content.toString().replaceAll(NOTES_REGEX, NULL_CHAR);
        resolvePlaceholders(fileContent);

        // 存储所有依赖的列表
        List<Dependencie> allDependencies = new ArrayList<>();

        // 解析 <parent> 中的依赖
        allDependencies.addAll(parseDependencies(fileContent, PARENT_REGEX));

        // 解析 <dependencies> 中的依赖
        allDependencies.addAll(parseDependencies(fileContent, DEPENDENCY_REGEX));

        // 解析 <build> 中的插件依赖
        Pattern buildPattern = Pattern.compile(BUILD_REGEX, Pattern.DOTALL);
        Matcher buildMatcher = buildPattern.matcher(fileContent);

        if (buildMatcher.find()) {
            String buildContent = buildMatcher.group();
            allDependencies.addAll(parseDependencies(buildContent, PLUGIN_REGEX));
        }

        return allDependencies;
    }

    /**
     * 解析给定内容中的依赖项
     * @param content 需要解析的内容
     * @param regex 匹配依赖的正则表达式
     * @return 解析出的依赖项列表
     */
    private static List<Dependencie> parseDependencies(String content, String regex) {
        List<Dependencie> dependencies = new ArrayList<>();

        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher dependencyMatcher = pattern.matcher(content);

        while (dependencyMatcher.find()) {
            String dependencyContent = dependencyMatcher.group();

            Matcher groupIdMatcher = Pattern.compile(GROUP_ID_REGEX).matcher(dependencyContent);
            Matcher artifactIdMatcher = Pattern.compile(ARTIFACT_ID_REGEX).matcher(dependencyContent);
            Matcher versionMatcher = Pattern.compile(VERSION_REGEX).matcher(dependencyContent);

            if (groupIdMatcher.find() && artifactIdMatcher.find()) {
                Dependencie dependencie = new Dependencie();
                dependencie.setGroupId(groupIdMatcher.group(1));
                dependencie.setArtifactId(artifactIdMatcher.group(1));

                if (versionMatcher.find()) {
                    String version = versionMatcher.group(1);
                    version = resolvePlaceholdersInText(version);  // 替换版本中的占位符
                    dependencie.setVersion(version);
                }

                dependencies.add(dependencie);
            }
        }

        return dependencies;
    }

    /**
     * 解析<properties>标签并保存属性到Map
     * @param content 需要解析的内容
     */
    private static void resolvePlaceholders(String content) {
        Pattern pattern = Pattern.compile(PROPERTIES_REGEX, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String propertiesContent = matcher.group(1);
            Matcher propertyMatcher = Pattern.compile(PROPERTY_NAME).matcher(propertiesContent);

            while (propertyMatcher.find()) {
                String key = propertyMatcher.group(1);
                String value = propertyMatcher.group(2);
                propertyNameMap.put(key, value);
            }
        }
    }

    /**
     * 替换文本中的占位符
     * @param text 需要替换的文本
     * @return 替换后的文本
     */
    private static String resolvePlaceholdersInText(String text) {
        Matcher matcher = Pattern.compile(PLACEHOLDER_PATTERN).matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String replacement = propertyNameMap.getOrDefault(placeholder, matcher.group(0));
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
