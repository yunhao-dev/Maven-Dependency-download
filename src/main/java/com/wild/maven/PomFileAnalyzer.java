package com.wild.maven;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.wild.maven.model.Dependencie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

        // 正则表达式匹配所有<dependency>标签
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher dependencyMatcher = pattern.matcher(content.toString());

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
                dependencie.setVersion(versionMatcher.group(1));
                dependencies.add(dependencie);
            }
        }
        return dependencies;
    }
}
