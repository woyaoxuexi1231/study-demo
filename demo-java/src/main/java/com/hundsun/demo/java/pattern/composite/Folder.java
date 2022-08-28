package com.hundsun.demo.java.pattern.composite;

import java.util.ArrayList;
import java.util.List;

public class Folder extends File {

    private List<File> files = new ArrayList<>();

    public Folder() {
        this.setFileType("folder");
    }

    public void addFile(File file) {
        this.files.add(file);
    }

    public File getFile(Integer index) {
        return this.files.get(index);
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
