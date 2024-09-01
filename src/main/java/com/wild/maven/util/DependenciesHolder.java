package com.wild.maven.util;

import com.wild.maven.model.Dependencie;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @Author: yunhao_dev
 * @Date: 2024/8/13 0:34
 */
public class DependenciesHolder {
    public static final String[] TITLE = {"GroupId", "ArtifactId", "Version", "Action"};
    private static List<Dependencie> dependencies = new ArrayList<>();
    /**
     * 备份列表
     */
    private static List<Dependencie> backupDependencies = new ArrayList<>();

    public static void setDependenciesList(List<Dependencie> dependenciesList) {
        dependencies = dependenciesList;
    }

    public static void saveDependencie(Dependencie dependencie) {
        dependencies.add(dependencie);
    }

    public static void removeDependencie(Dependencie dependencie) {
        dependencies.remove(dependencie);
    }

    public static List<Dependencie> getDependencies() {
        return dependencies;
    }

    /**
     * 备份当前依赖列表
     */
    public static void backupDependencies() {
        backupDependencies = new ArrayList<>(dependencies);
    }

    /**
     * 恢复备份的依赖列表
     */
    public static void restoreDependenciesFromBackup() {
        dependencies = new ArrayList<>(backupDependencies);
    }
}
