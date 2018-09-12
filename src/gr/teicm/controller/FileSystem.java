
package gr.teicm.controller;

import java.io.File;
import javax.swing.filechooser.FileSystemView;

public class FileSystem implements IFileSystem {
    private FileSystemView fileSystemView;

    public FileSystem() {
        this.fileSystemView = FileSystemView.getFileSystemView();
    }

    @Override
    public File[] getRoots() {
        return fileSystemView.getRoots();
    }

    @Override
    public File getHomeDirectory() {
        return new File(System.getProperty("user.home"));
    }

}
