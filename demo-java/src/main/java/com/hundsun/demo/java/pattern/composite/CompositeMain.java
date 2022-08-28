package com.hundsun.demo.java.pattern.composite;

public class CompositeMain {

    public static void main(String[] args) {
        Folder folder = new Folder();
        Folder folder1 = new Folder();
        folder1.addFile(new ImageFile());
        folder.addFile(folder1);
        folder.addFile(new TextFile());
        Antivirus antivirus = new Antivirus();
        antivirus.killVirus(folder);
    }
}
