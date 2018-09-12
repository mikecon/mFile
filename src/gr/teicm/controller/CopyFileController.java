package gr.teicm.controller;

import java.io.File;

public class CopyFileController implements ICopyFileController {
    
    @Override
    public int copyFile(File selectedFileToCopy) {
        //IMessage myPane = new Message();
        //boolean copyAnswYes;
        //copyAnswYes = myPane.showCopyFileMessage();
        int exitCode=0;
        try{
            if(selectedFileToCopy.isDirectory())
                exitCode = 1;
            else if (selectedFileToCopy.isFile())
                exitCode = 2;
        }catch(Exception e){
            exitCode = 0;
            e.printStackTrace();
        }
        return exitCode;
    }
}
