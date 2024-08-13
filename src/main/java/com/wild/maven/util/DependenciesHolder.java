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
    public static final String[] TITLE = {"GroupId","ArtifactId","Version","Action"};
    private static List<Dependencie> dependencies = new ArrayList<>();

    public static void setDependenciesList(List<Dependencie> dependenciesList){
        dependencies = dependenciesList;
    }

    public static void saveDependencie(Dependencie dependencie){
        dependencies.add(dependencie);
    }

    public static void removeDependencie(Dependencie dependencie){
        dependencies.remove(dependencie);
    }

    public static List<Dependencie> getDependencies() {
        return dependencies;
    }
}
