package com.wild.maven.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @Author: yunhao_dev
 * @Date: 2024/8/11 18:22
 */
public class DependencieTableData {
    private final String[] title = {"GroupId","ArtifactId","Version","Action"};
    private List<Dependencie> dependencies = new ArrayList<>();



    private Object action;

    public String[] getTitle() {
        return title;
    }

    public List<Dependencie> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Dependencie dependencie) {
        dependencies.add(dependencie);
    }
    public void setAction(Object action) {
        this.action = action;
    }
    /**
     * 将列表转换为二维数组
     * @return Object[][]
     */
    public Object[][] toTableData() {
        Object[][] data = new Object[dependencies.size()][title.length];
        for (int i = 0; i < dependencies.size(); i++) {
            Dependencie dependencie = dependencies.get(i);
            data[i][0] = dependencie.getGroupId();
            data[i][1] = dependencie.getArtifactId();
            data[i][2] = dependencie.getVersion();
            data[i][3] = action;
        }
        return data;
    }
}
