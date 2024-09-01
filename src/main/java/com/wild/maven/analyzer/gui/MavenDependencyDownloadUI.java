package com.wild.maven.analyzer.gui;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.wild.maven.util.DependenciesHolder.TITLE;

/**
 * @description: Maven Dependency Download UI
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
    private JRadioButton notDownloadedRadioButton;
    private JButton downloadButton;

    public MavenDependencyDownloadUI(@NotNull Project project, VirtualFile file, final MavenProject mavenProject) {
        this.project = project;
        this.file = file;
        mavenProjectsManager = MavenProjectsManager.getInstance(project);
        this.mavenProject = mavenProject;

        initUI();
        downloadAllButton.addActionListener(e -> downloadAllDependencies());
    }

    public JComponent getRootComponent() {
        return mainPanel;
    }

    private void initUI() {
        denTitleGroupPanel.setLayout(new GridLayout(1, 4));

        for (String title : TITLE) {
            JLabel jLabel = new JLabel(title, JLabel.CENTER);
            denTitleGroupPanel.add(jLabel);
        }

        DependenciesHolder.setDependenciesList(PomFileAnalyzer.getDependencies(file));
        updateDependenciesTable(DependenciesHolder.getDependencies());

        // 添加过滤器的监听器
        filterTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterDependencies();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterDependencies();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterDependencies();
            }
        });

    }

    // 过滤依赖项并更新表格显示
    private void filterDependencies() {
        String filterText = filterTextField.getText();
        List<Dependencie> filteredDependencies = finDependcie(filterText);
        updateDependenciesTable(filteredDependencies);
    }

    // 根据依赖列表更新表格
    private void updateDependenciesTable(List<Dependencie> dependencies) {
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

            // 设置 JPanel 上边框
            jPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));

            tablePanel.add(jPanel);
            download.addActionListener(e -> downloadDependencyWithNotification(dependency));
        }

        // 将 tablePanel 设置为 JScrollPane 的视图组件
        tableScrollPanel.setViewportView(tablePanel);
    }

    // 筛选符合条件的依赖项
    private List<Dependencie> finDependcie(String name) {
        List<Dependencie> list = new ArrayList<>();
        List<Dependencie> dependencies = DependenciesHolder.getDependencies();

        // 将name转换为可以匹配任意字符间隔的正则表达式
        StringBuilder regexBuilder = new StringBuilder();
        for (char c : name.toCharArray()) {
            regexBuilder.append(".*").append(Pattern.quote(String.valueOf(c)));
        }
        regexBuilder.append(".*");

        // 编译正则表达式
        Pattern pattern = Pattern.compile(regexBuilder.toString(), Pattern.CASE_INSENSITIVE);

        for (Dependencie dependency : dependencies) {
            // 如果groupId或artifactId匹配正则表达式，则添加到list中
            if (pattern.matcher(dependency.getGroupId()).matches() ||
                    pattern.matcher(dependency.getArtifactId()).matches()) {
                list.add(dependency);
            }
        }
        return list;
    }

    private void downloadAllDependencies() {
        Task.Backgroundable task = new Task.Backgroundable(project, "Downloading Dependencies", true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                List<Dependencie> dependencies = DependenciesHolder.getDependencies();
                int totalDependencies = dependencies.size();
                for (int i = 0; i < totalDependencies; i++) {
                    if (indicator.isCanceled()) {
                        break;
                    }
                    Dependencie dependency = dependencies.get(i);
                    UrlAnalyzer.parse(dependency);

                    // 更新进度条
                    indicator.setFraction((double) (i + 1) / totalDependencies);
                    indicator.setText2("Downloading " + dependency.getArtifactId());
                }
            }


        };

        ProgressManager.getInstance().run(task);
    }

    private void downloadDependencyWithNotification(Dependencie dependency) {
        Task.Backgroundable task = new Task.Backgroundable(project, "Downloading " + dependency.getArtifactId(), true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                UrlAnalyzer.parse(dependency);
            }
        };

        ProgressManager.getInstance().run(task);
    }
}
