
package gr.teicm.controller;

import java.io.File;
import javax.swing.filechooser.FileSystemView;

public class TableFacade {
    private String selectedFilePath;
    private File selectedFile;
    public FileSystemView fileSystemView = FileSystemView.getFileSystemView();

    public TableFacade() {
    }

    public TableFacade(String filePath) {

        this.selectedFilePath = filePath;

    }

    public File getSelectedTableFile(){

        try{
            selectedFile = new File(selectedFilePath);
            if(selectedFile.isFile())
                return selectedFile;
            else if(selectedFile.isDirectory())
                return selectedFile;
            else
                return null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
