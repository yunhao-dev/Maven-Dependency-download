package com.wild.maven.analyzer;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

import static org.jetbrains.idea.maven.utils.MavenUtil.isPotentialPomFile;


/**
 * @description:
 * @Author: yunhao_dev
 * @Date: 2024/8/10 20:15
 */
public class PomEditorProvider implements FileEditorProvider, DumbAware {
    private static final Logger log = Logger.getInstance(PomEditorProvider.class);
    private static String EDITOR_TYPE_ID = "Maven Download";
    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return isPomFile(project,file);
    }

    private boolean isPomFile(Project project, VirtualFile file) {
        String name = file.getName();
        if(!isPotentialPomFile(name)){
            return false;
        }
        final MavenProjectsManager instance = MavenProjectsManager.getInstance(project);
        if(instance == null){
            return false;
        }
        MavenProject mavenProject = instance.findProject(file);
        if(mavenProject == null){
            return false;
        }
        return mavenProject.getFile().equals(file);

    }

    @Override
    public @NotNull FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        log.assertTrue(accept(project,file));
        return new UIFormEditor(project,file);
    }

    @Override
    public @NotNull @NonNls String getEditorTypeId() {
        return EDITOR_TYPE_ID;
    }

    @Override
    public @NotNull FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR;
    }
}
