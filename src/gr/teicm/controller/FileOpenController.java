package gr.teicm.controller;

import java.awt.*;

public class FileOpenController implements IFileOpenController {

    @Override
    public int fileOpen(){
        int exitCode=0;
        try{
            if(Desktop.isDesktopSupported()){
                exitCode = 1;
            }
        }catch(Exception e){
            exitCode = 0;
            e.printStackTrace();
        }
        return exitCode;
    }
}
