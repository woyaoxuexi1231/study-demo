package com.hundsun.demo.java.pattern.composite;

import java.util.List;

public class Antivirus {

    public void killVirus(File file) {

        if (file.getClass().getSimpleName().equals(Folder.class.getSimpleName())) {
            Folder folder = (Folder) file;
            List<File> files = folder.getFiles();
            for (File file1 : files) {
                this.killVirus(file1);
            }
        } else {
            System.out.println("杀毒软件开始进行杀毒..." + file.getFileType());
        }
    }
}
