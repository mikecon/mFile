
package gr.teicm.controller;

import javax.swing.JOptionPane;

public class Message implements IMessage {
        @Override
        public boolean showMessage(String title, String message) {
        JOptionPane myPane = new JOptionPane();
        int reply = myPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        if (reply == myPane.YES_OPTION) {
            return true;
        } else {
            myPane.getRootFrame().dispose();
            return false;
        }
    }
        @Override
        public boolean showCopyFileMessage() {
        JOptionPane myPane = new JOptionPane();
        int reply = myPane.showConfirmDialog(null, "Do you want to copy the selected file?", "Copy File", JOptionPane.YES_NO_OPTION);
        if (reply == myPane.YES_OPTION) {
            return true;
        } else {
            myPane.getRootFrame().dispose();
            return false;
        }
    }
        @Override
        public boolean showOverwriteFileMessage() {
        JOptionPane myPane = new JOptionPane();
        int reply = myPane.showConfirmDialog(null, "File Already Exists! Do you want to overwrite the file?", "Overwrite File", JOptionPane.YES_NO_OPTION);
        if (reply == myPane.YES_OPTION) {
            return true;
        } else {
            myPane.getRootFrame().dispose();
            return false;
        }
    }

}
