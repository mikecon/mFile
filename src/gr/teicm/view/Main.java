
package gr.teicm.view;

import javax.swing.*;

public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch(Exception e) {
                }
                JFrame f = new JFrame("mFile");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Gui treeTable = new Gui();
                f.setContentPane(treeTable.gui());
                f.pack();
                f.setLocationByPlatform(true);
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
                treeTable.showRootFile();
            }
        });
    }
    
}
