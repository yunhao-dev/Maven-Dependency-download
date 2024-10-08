package com.wild.maven;

import com.intellij.openapi.diagnostic.Logger;
import com.wild.maven.model.Dependencie;
import com.wild.maven.util.UrlUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.wild.maven.util.SystemConstants.DOWNLOAD_PATH;

/**
 * @description: 解析URL
 * @Author: yunhao_dev
 * @Date: 2024/8/13 0:31
 */
public class UrlAnalyzer {
    private static final String MAVEN_BASE_URL = "https://repo1.maven.org/maven2";
    private static final Logger log = Logger.getInstance(UrlAnalyzer.class);

    public static void parse(Dependencie dependencie) {
        List<String> stringList = new ArrayList<>();
        stringList.addAll(UrlUtils.splitBySeparator(dependencie.getGroupId(), "\\."));
        stringList.add(dependencie.getArtifactId());
        stringList.add(dependencie.getVersion());

        String baseUrl = UrlUtils.buildUrlPath(MAVEN_BASE_URL, stringList);
        List<String> fileExtensions = List.of(".jar", ".jar.sha1", ".pom", ".pom.sha1");
        List<String> downloadUrls = new ArrayList<>();

        for (String extension : fileExtensions) {
            downloadUrls.add(baseUrl + "/" + dependencie.getArtifactId() + "-" + dependencie.getVersion() + extension);
        }

        // download files
        String downloadBasePath = UrlUtils.buildUrlPath(DOWNLOAD_PATH, stringList);
        for (String downloadUrl : downloadUrls) {
            downloadFile(downloadUrl, downloadBasePath);
        }
    }

    private static void downloadFile(String fileUrl, String destinationPath) {
        try {
            Path destinationDir = Paths.get(destinationPath);
            if (Files.notExists(destinationDir)) {
                Files.createDirectories(destinationDir);
            }

            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
            Path filePath = destinationDir.resolve(fileName);

            if (Files.exists(filePath)) {
                System.out.println("File already exists: " + filePath);
                return;
            }

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fileUrl))
                    .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                    .header("accept-encoding", "gzip, deflate, br, zstd")
                    .header("accept-language", "zh-CN,zh;q=0.9,ru;q=0.8,en;q=0.7")
                    .header("priority", "u=0, i")
                    .header("sec-ch-ua", "\"Not)A;Brand\";v=\"99\", \"Google Chrome\";v=\"127\", \"Chromium\";v=\"127\"")
                    .header("sec-ch-ua-mobile", "?0")
                    .header("sec-ch-ua-platform", "\"Windows\"")
                    .header("sec-fetch-dest", "document")
                    .header("sec-fetch-mode", "navigate")
                    .header("sec-fetch-site", "cross-site")
                    .header("sec-fetch-user", "?1")
                    .header("upgrade-insecure-requests", "1")
                    .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36")
                    .build();

            HttpResponse<Path> response = client.send(request,
                    HttpResponse.BodyHandlers.ofFile(filePath));

            if (response.statusCode() == 200) {
                System.out.println("File downloaded to: " + filePath);
            } else {
                System.err.println("Failed to download file: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
