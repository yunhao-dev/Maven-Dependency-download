package com.wild.maven;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @description:
 * @Author: yunhao_dev
 * @Date: 2024/8/14 0:07
 */
public class TestPomFileAnalyzer {
    private static final String PATH = "E:\\Code\\Java\\Maven-Dependency-download\\src\\main\\resources\\test\\pom.xml";
    public static void main(String[] args) {
        VirtualFile virtualFile = new VirtualFile() {
            @Override
            public @NotNull @NlsSafe String getName() {
                return null;
            }

            @Override
            public @NotNull VirtualFileSystem getFileSystem() {
                return null;
            }

            @Override
            public @NonNls @NotNull String getPath() {
                return PATH;
            }

            @Override
            public boolean isWritable() {
                return false;
            }

            @Override
            public boolean isDirectory() {
                return false;
            }

            @Override
            public boolean isValid() {
                return false;
            }

            @Override
            public VirtualFile getParent() {
                return null;
            }

            @Override
            public VirtualFile[] getChildren() {
                return new VirtualFile[0];
            }

            @Override
            public @NotNull OutputStream getOutputStream(Object requestor, long newModificationStamp, long newTimeStamp) throws IOException {
                return null;
            }

            @Override
            public byte @NotNull [] contentsToByteArray() throws IOException {
                return new byte[0];
            }

            @Override
            public long getTimeStamp() {
                return 0;
            }

            @Override
            public long getLength() {
                return 0;
            }

            @Override
            public void refresh(boolean asynchronous, boolean recursive, @Nullable Runnable postRunnable) {

            }

            @Override
            public @NotNull InputStream getInputStream() throws IOException {
                return null;
            }
        };
        PomFileAnalyzer.getAllDependencies(virtualFile);

    }
}
