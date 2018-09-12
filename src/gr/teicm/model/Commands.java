
package gr.teicm.model;

import gr.teicm.controller.CopyFileController;
import gr.teicm.controller.FileOpenController;
import gr.teicm.controller.History;
import gr.teicm.controller.ICopyFileController;
import gr.teicm.controller.IFileOpenController;
import gr.teicm.controller.TableFacade;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableColumn;
import javax.swing.tree.*;
import org.apache.commons.io.FileUtils;

public class Commands {
    protected FileSystemView fileSystemView;
    protected JPanel panel;
    public JTree tree;
    public DefaultTreeModel treeModel;
    public DefaultMutableTreeNode treeNode;
    protected JTable table;
    protected JPopupMenu popup;
    public FileTableModel fileTableModel;
    protected ListSelectionListener listSelectionListener;
    protected boolean cellSizesSet = false;
    protected int rowIconPadding = 6;
    protected JLabel label;
    protected JTextField text;
    public JPopupMenu popupMenu;
    public JMenuItem menuItemNew,menuItemOpenPop,menuItemOpenNew,menuItemSend,menuItemPastePop,menuItemCopyPop,menuItemDeletePop;
    public JMenuItem menuItemRenamePop,menuItemOpenTerm,menuItemFindInFold,menuItemCreatArch,menuItemProp,menuItem,menuItemPaste;
    public JMenuBar menuBar;
    public JMenu menu;
    public JMenuItem menuItemCreateF,menuItemCreate,menuItemOpen,menuItemClose,menuItemDelete,menuItemCopy,menuItemRename;
    public TableFacade tableFacade;
    public String selectedFilePath;
    public File fileToCopy;
    public History history;
    public File selectedTableFile;
    public File selectedFileToCopy;

    public void showRootFile() {
        tree.setSelectionInterval(0, 0);
    }

    public void showChildren(final DefaultMutableTreeNode node) {
        
        SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
            @Override
            public Void doInBackground() {
                File file = (File) node.getUserObject();
                if (file.isDirectory()) {
                    File[] files = fileSystemView.getFiles(file, true); 
                    if (node.isLeaf()) {
                        for (File child : files) {
                            if (child.isDirectory()) {
                                publish(child);
                            }
                        }
                    }
                    setTableData(files);
                }
                return null;
            }

            @Override
            protected void process(List<File> chunks) {
                for (File child : chunks) {
                    node.add(new DefaultMutableTreeNode(child));
                }
            }

            @Override
            protected void done() {
                tree.setEnabled(true);
            }
        };
        worker.execute();
    }
    
    public void setTableData(final File[] files) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (fileTableModel==null) {
                    fileTableModel = new FileTableModel();
                    table.setModel(fileTableModel);
                }
                table.getSelectionModel().removeListSelectionListener(listSelectionListener);
                fileTableModel.setFiles(files);
                table.getSelectionModel().addListSelectionListener(listSelectionListener);
                if (!cellSizesSet) {
                    Icon icon = fileSystemView.getSystemIcon(files[0]);

                    table.setRowHeight( icon.getIconHeight()+rowIconPadding );

                    setColumnWidth(0,-1);
                    table.getColumnModel().getColumn(3).setMaxWidth(120);
                    setColumnWidth(2,-1);
                    setColumnWidth(4,-1);
                 

                    cellSizesSet = true;
                }
            }
        });
    }

    protected void setColumnWidth(int column, int width) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        if (width < 0) {
            JLabel label = new JLabel((String) tableColumn.getHeaderValue());
            Dimension preferred = label.getPreferredSize();
            width = (int) preferred.getWidth() + 14;
        }
        tableColumn.setPreferredWidth(width);
        tableColumn.setMaxWidth(width);
        tableColumn.setMinWidth(width);
    }

    public void previousButtonMouseClicked(final JTree tree, final int currentRow) {
        String path = history.back();
        File getPath = new File(path);
        File[] files = fileSystemView.getFiles(getPath, true);
        text.setText(path);
        setTableData(files);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode (files);
        showChildren(node);
    }
    
    public void nextButtonMouseClicked(final JTree tree, final int currentRow) {
        String path = history.forward();
        File getPath = new File(path);
        text.setText(path);
        File[] files = fileSystemView.getFiles(getPath, true);
        setTableData(files);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode (files);
        showChildren(node);
    }

    protected TreePath getUpPath(final JTree tree) {
         Rectangle visibleRect = tree.getVisibleRect();
              TreePath result = tree.getClosestPathForLocation(visibleRect.x, visibleRect.y + 1);
              TreePath leadPath = tree.getLeadSelectionPath();
              if (result.equals(leadPath)) {
                  result = tree.getClosestPathForLocation(visibleRect.x, visibleRect.y - visibleRect.height);
              }

              return result;
          }
    public void setFileDetails(File file) {
        JFrame f = (JFrame)panel.getTopLevelAncestor();
        f.setJMenuBar(menuBar);
        selectedFilePath=file.getPath();
        tableFacade = new TableFacade(selectedFilePath);
        selectedTableFile = tableFacade.getSelectedTableFile();
        label.setText(" "+fileSystemView.getSystemDisplayName(file)+" ("+file.length()+" bytes) "+fileSystemView.getSystemTypeDescription(file)+"");
        text.setText(selectedFilePath.toString());
        history.add(selectedFilePath.toString());
        try {
                URL urlBig = this.getClass().getResource("icon-32x32.png");
                URL urlSmall = this.getClass().getResource("icon-16x16.png");
                ArrayList<Image> images = new ArrayList<Image>();
                images.add( ImageIO.read(urlBig) );
                images.add( ImageIO.read(urlSmall) );
                f.setIconImages(images);
            } catch(Exception e) {}
        if (f!=null) {
            f.setTitle(
                "mFile" +
                " :: " +
                fileSystemView.getSystemDisplayName(file) );
        }
        panel.repaint();
    }

    public void tableChange(ListSelectionEvent lse) {
        int row = table.getSelectionModel().getLeadSelectionIndex();
        setFileDetails( ((FileTableModel)table.getModel()).getFile(row) );
    }
    public void treeChange(TreeSelectionEvent tse) {
        DefaultMutableTreeNode node =(DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
        showChildren(node);
        setFileDetails((File)node.getUserObject());
    }
    public void filepathTextFieldKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == File.separatorChar) {
            String path = text.getText();
            File directory = new File(path);
            File[] files = fileSystemView.getFiles(directory, true);

            if (directory.exists()) {
                setTableData(files);
            }
        }
    }
    public void filesTableMousePressed(MouseEvent e,String filename) {
        if(e.getClickCount() ==2){    
            File getPath = new File(filename);
            if(getPath.isDirectory()){
                File[] files = fileSystemView.getFiles(getPath, true);
                setTableData(files);
                TreePath leadPath = tree.getSelectionPath();
                int leadRow = tree.getRowForPath(leadPath);
                tree.setSelectionPath(tree.getPathForRow(leadRow));
                tree.scrollPathToVisible(tree.getPathForRow(leadRow));
            }else{
                try {
                    Desktop.getDesktop().open(getPath);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,"Fail");
                }
            }
        }
    }

    public void fileRemoveMousePressed(String dir) {
        File fileToDelete = new File(dir);
        try {
            if (fileToDelete.delete()) {
                File[] files = fileSystemView.getFiles(fileToDelete.getParentFile(), true);
                setTableData(files);
                JOptionPane.showMessageDialog(null, "File Deleted Successfully", "Done", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "File Deletion Failed", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "File Deletion Failed ", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void fileRenameMousePressed(String selectedFile) {
        String nName;
        nName=JOptionPane.showInputDialog("Δώστε Όνομα");
        File newName = new File(nName);
        File getPath = new File(selectedFile);
        File newFilePath = new File(""+selectedFile+"\\"+nName+"");
        try {
        if(getPath.renameTo(newName)){
            FileUtils.moveFile(getPath, newFilePath);
            JOptionPane.showMessageDialog(null,"Success");
        }else
            JOptionPane.showMessageDialog(null,"Fail");
        File[] files = fileSystemView.getFiles(getPath.getParentFile(), true);
        setTableData(files);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Document is exist in windows directory", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void fileNewDocumentActionPerformed(String dir) {
        try {
            String createDoc;
            createDoc = JOptionPane.showInputDialog("Δώστε Όνομα και είδος(txt,pdf...) αρχείου");
            FileWriter doc = new FileWriter("" + dir + "\\" + createDoc + "");
            BufferedWriter nw = new BufferedWriter(doc);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Document is exist in windows directory", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
        File getPath = new File(dir);
        File[] files = fileSystemView.getFiles(getPath.getParentFile(), true);
        setTableData(files);
    }

    public void fileNewFolderActionPerformed(String dir) {
        String createFold;
        createFold = JOptionPane.showInputDialog("Δώστε Όνομα Φακέλου");
        String pathname = "" + dir + "\\" + createFold + "";
        File folder = new File(pathname);
        File getPath = new File(dir);
        if (folder.exists()) {
            JOptionPane.showMessageDialog(null, "Folder " + folder.getName() + " is exist in windows directory", "Error", JOptionPane.INFORMATION_MESSAGE);
        } else {
            folder.mkdir();
            JOptionPane.showMessageDialog(null, "Folder " + folder.getName() + " was created");
        }
        File[] files = fileSystemView.getFiles(getPath.getParentFile(), true);
        setTableData(files);
    }

    public void CopyFileMousePressed(String pathname) {
        ICopyFileController fileCopy = new CopyFileController();
        selectedFileToCopy = new File(pathname);
        fileCopy.copyFile(selectedFileToCopy);
        menuItemPaste.setVisible(true);
        menuItemPastePop.setVisible(true);
    }

    public void PasteFileMousePressed(String pathname,File copyToDirectory) {
        ICopyFileController fileCopy = new CopyFileController();
        File selectedFileToCopy = new File(pathname);
        int returnedCode = fileCopy.copyFile(selectedFileToCopy);
        try{
            if(selectedFileToCopy.isDirectory()){
                FileUtils.copyDirectoryToDirectory(selectedFileToCopy,copyToDirectory);
                menuItemPaste.setVisible(false);
                menuItemPastePop.setVisible(false);
            } else if(selectedFileToCopy.isFile())
                FileUtils.copyFileToDirectory(selectedFileToCopy,copyToDirectory);
                menuItemPaste.setVisible(false);
                menuItemPastePop.setVisible(false);
        } catch (IOException e){
            e.printStackTrace();
        }
        File[] files = fileSystemView.getFiles(copyToDirectory, true);
        setTableData(files);  
    }
    
    public void FindFileMousePressed(String selectedPath){
        String find;
        find = JOptionPane.showInputDialog("Δώστε Όνομα Αρχείου");
        String pathname = "" + selectedPath + "\\" + find + "";
        
        File directory = new File(pathname);

        if (directory.exists()) {
            try {
                Desktop.getDesktop().open(directory);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "There is no App for this file or Desktop is not supported");
            }
          }
        }

    public void fileMenuItemOpenActionPerformed(String selectedFile) {
        IFileOpenController fileOpen = new FileOpenController();
        int returnedCode = fileOpen.fileOpen();
        if (returnedCode == 1){
        try {
            File source = new File(selectedFile);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(source);
            } else {
                JOptionPane.showMessageDialog(null, "There is no App for this file or Desktop is not supported");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        }
    }
}
