package com.wild.maven.analyzer;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import com.wild.maven.analyzer.gui.MavenDependencyDownloadUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * @description:
 * @Author: yunhao_dev
 * @Date: 2024/8/11 16:13
 */
public class UIFormEditor extends UserDataHolderBase implements FileEditor {
    private static final String TABS_NAME = "Maven Dependencies";
    private Project project;
    private VirtualFile file;
    private MavenDependencyDownloadUI gui;


    public UIFormEditor(@NotNull Project project, final VirtualFile file) {
        this.file = file;
        final MavenProject mavenProject = MavenProjectsManager.getInstance(project).findProject(file);
        if(mavenProject == null){
            throw new RuntimeException("Report this bug please. MavenProject not found for file " + file.getPath());
        }
        gui = new MavenDependencyDownloadUI(project,file,mavenProject);
    }

    @Override
    public @NotNull JComponent getComponent() {
        return gui.getRootComponent();
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return null;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) @NotNull String getName() {
        return TABS_NAME;
    }

    @Override
    public void setState(@NotNull FileEditorState state) {

    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {

    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {

    }

    @Override
    public @Nullable FileEditorLocation getCurrentLocation() {
        return null;
    }

    @Override
    public void dispose() {
        Disposer.dispose(this);
    }

    @Nullable
    @Override
    public VirtualFile getFile() {
        return this.file;
    }
}
