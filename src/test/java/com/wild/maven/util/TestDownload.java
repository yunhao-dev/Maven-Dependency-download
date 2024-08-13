import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @description:
 * @Author: yunhao_dev
 * @Date: 2024/8/13 8:11
 */
public class TestDownload {
    private static final String fileUrl = "https://repo1.maven.org/maven2/org/springframework/spring-core/4.3.7.RELEASE/spring-core-4.3.7.RELEASE-javadoc.jar";
    private static final String path = "E:\\tools\\apache-maven\\repo/org/springframework/spring-core/4.3.7.RELEASE";

    public static void main(String[] args) {
        downloadFile(fileUrl, path);
    }

    private static void downloadFile(String fileUrl, String destinationPath) {
        try {
            Path destinationDir = Paths.get(destinationPath);
            if (Files.notExists(destinationDir)) {
                Files.createDirectories(destinationDir);
            }

            // 获取文件名
            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
            Path filePath = destinationDir.resolve(fileName);

            // 创建HttpClient实例
            HttpClient client = HttpClient.newHttpClient();

            // 创建HttpRequest实例，并设置请求头
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

            // 发送请求并下载文件
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
