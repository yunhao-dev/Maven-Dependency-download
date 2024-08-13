package com.wild.maven.analyzer.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.wild.maven.PomFileAnalyzer;
import com.wild.maven.UrlAnalyzer;
import com.wild.maven.model.Dependencie;
import com.wild.maven.util.DependenciesHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.List;

import static com.wild.maven.util.DependenciesHolder.TITLE;

/**
 * @description:
 * @Author: yunhao_dev
 * @Date: 2024/8/11 15:13
 */
public class MavenDependencyDownloadUI {

    private final Project project;
    private final VirtualFile file;
    private MavenProject mavenProject;
    private MavenProjectsManager mavenProjectsManager;

    private JPanel mainPanel;
    private JButton downloadAllButton;
    private JTextField filterTextField;
    private JScrollPane tableScrollPanel;
    private JPanel denTitleGroupPanel;
    private JButton downloadButton;



    public MavenDependencyDownloadUI(@NotNull Project project, VirtualFile file, final MavenProject mavenProject) {
        this.project = project;
        this.file = file;
        mavenProjectsManager = MavenProjectsManager.getInstance(project);
        this.mavenProject = mavenProject;


        initUI();
        downloadAllButton.addActionListener(e -> {
            UrlAnalyzer.parseAll();
        });


    }

    public JComponent getRootComponent(){
        return mainPanel;
    }

    private void initUI() {
        denTitleGroupPanel.setLayout(new GridLayout(1, 4));

        for (String title : TITLE) {
            JLabel jLabel = new JLabel(title, JLabel.CENTER);
            denTitleGroupPanel.add(jLabel);
        }

        DependenciesHolder.setDependenciesList(PomFileAnalyzer.getDependencies(file));
        List<Dependencie> dependencies = DependenciesHolder.getDependencies();
        // 使用 BoxLayout 纵向排列依赖项
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));

        for (Dependencie dependency : dependencies) {
            JPanel jPanel = new JPanel(new GridLayout(1, 4));
            JLabel groupId = new JLabel(dependency.getGroupId(), JLabel.CENTER);
            JLabel artifactId = new JLabel(dependency.getArtifactId(), JLabel.CENTER);
            JLabel version = new JLabel(dependency.getVersion(), JLabel.CENTER);
            JPanel buttonPanel = new JPanel();
            JButton download = new JButton("Download");
            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(download);
            buttonPanel.add(Box.createHorizontalGlue());
            jPanel.add(groupId);
            jPanel.add(artifactId);
            jPanel.add(version);
            jPanel.add(buttonPanel);

            // 设置 JPanel 固定的高度
            jPanel.setPreferredSize(new Dimension(tablePanel.getWidth(), 40));
            jPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            // 设置 Jpanel 上边框
            jPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));

            tablePanel.add(jPanel);
            download.addActionListener(e ->{
                // 下载
                UrlAnalyzer.parse(dependency);
            });
        }

        // 将 tablePanel 设置为 JScrollPane 的视图组件
        tableScrollPanel.setViewportView(tablePanel);
    }
}
