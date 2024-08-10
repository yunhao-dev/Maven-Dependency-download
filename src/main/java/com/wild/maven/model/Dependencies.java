package com.wild.maven.model;

/**
 * @description:
 * @Author: yunhao_dev
 * @Date: 2024/8/10 16:31
 */
public class Dependencies {
    private String groupId;
    private String artifactId;
    private String version;

    public Dependencies(){

    }
    public Dependencies(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public java.lang.String toString() {
        return "Dependencies{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
