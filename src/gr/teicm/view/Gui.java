
package gr.teicm.view;

import gr.teicm.controller.CommandStack;
import gr.teicm.model.Commands;
import gr.teicm.controller.History;
import gr.teicm.controller.TableFacade;
import gr.teicm.model.FileTreeCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;

public class Gui extends Commands {
    
    public Container gui() {
        if (panel==null) {
            panel = new JPanel(new BorderLayout(3,3));
            panel.setBorder(new EmptyBorder(5,5,5,5));

            fileSystemView = FileSystemView.getFileSystemView();
            
            tableFacade = new TableFacade();
            history = new History();
            final CommandStack stack = new CommandStack();
            
            JPanel detailView = new JPanel(new BorderLayout(3,3));
            
            menuBar = new JMenuBar();

            menu = new JMenu("File");
            menuBar.add(menu);
                        
            menuItemCreateF = new JMenuItem("New Folder");
            menu.add(menuItemCreateF);
            menuItemCreateF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileNewFolderActionPerformed(selectedFilePath);
            }
            });

            menuItemCreate = new JMenuItem("New Document");
            menu.add(menuItemCreate);
            menuItemCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileNewDocumentActionPerformed(selectedFilePath);
            }
            });
                        
            menuItemCopy = new JMenuItem("Copy");
            menu.add(menuItemCopy);
            menuItemCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               CopyFileMousePressed(selectedFilePath);
            }
            });

            menuItemPaste = new JMenuItem("Paste");
            menuItemPaste.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        PasteFileMousePressed(selectedFilePath,selectedFileToCopy);                        
                    }
                });
            menu.add(menuItemPaste);
            menuItemPaste.setVisible(false);
   
            menuItemDelete = new JMenuItem("Delete");
            menu.add(menuItemDelete);
            menuItemDelete.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        fileRemoveMousePressed(selectedFilePath);
                    }
                });

            menuItemRename = new JMenuItem("Rename");
            menu.add(menuItemRename);
            menuItemRename.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileRenameMousePressed(selectedFilePath);
            }
            });
            
            menu.addSeparator();

            menuItemClose = new JMenuItem("Close");
            menu.add(menuItemClose);
            menuItemClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
            });

            table = new JTable();
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setAutoCreateRowSorter(true);
            table.setShowVerticalLines(false);

            listSelectionListener = new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent lse) {
                    tableChange(lse);
                }
            };
            table.getSelectionModel().addListSelectionListener(listSelectionListener);
            
            popupMenu = new JPopupMenu();
            menuItemOpenPop = new JMenuItem("Open");
            menuItemOpenNew = new JMenuItem("Open in New Window");
            menuItemSend = new JMenuItem("Send To");
            menuItemCopy = new JMenuItem("Copy");
            menuItemPastePop = new JMenuItem("Paste");
            menuItemDeletePop = new JMenuItem("Delete");
            menuItemRenamePop = new JMenuItem("Rename");
            menuItemOpenTerm = new JMenuItem("Open Terminal Here");
            menuItemFindInFold = new JMenuItem("Find in this folder");
            menuItemCreatArch = new JMenuItem("Create Archive...");
            
            table.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        filesTableMousePressed(e,selectedFilePath);
                    }
                });
            
            menuItemCreatArch.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            fileNewDocumentActionPerformed(selectedFilePath);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            
            menuItemOpenNew.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fileMenuItemOpenActionPerformed(selectedFilePath);
                    }
                });
            
            menuItemOpenPop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            try {
                File getPath = new File(selectedFilePath);
                File[] files = fileSystemView.getFiles(getPath, true);
                setTableData(files);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
            });
            
            menuItemOpenTerm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            try {
                Process p = Runtime.getRuntime().exec("cmd /c start cmd.exe");
                p.waitFor();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
            });
            
            menuItemRenamePop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileRenameMousePressed(selectedFilePath);
            }
            });
            
            menuItemDeletePop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileRemoveMousePressed(selectedFilePath);
            }
            });
            
            menuItemSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CopyFileMousePressed(selectedFilePath);
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.showOpenDialog(null);
                File f = fileChooser.getSelectedFile();
                String newPath=f.getAbsolutePath();
                File getPath = new File(newPath);
                PasteFileMousePressed(newPath,selectedFileToCopy);
                File[] files = fileSystemView.getFiles(getPath, true);
                setTableData(files);
            }
            });
            
            menuItemCopy.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        CopyFileMousePressed(selectedFilePath);
                    }
                });
            menuItemPastePop.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        PasteFileMousePressed(selectedFilePath,selectedFileToCopy);
                    }
                });
            menuItemFindInFold.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        FindFileMousePressed(selectedFilePath);                       
                    }
                });
            
            popupMenu.add(menuItemOpenPop);
            popupMenu.add(menuItemOpenNew);
            popupMenu.addSeparator();
            popupMenu.add(menuItemSend);
            popupMenu.addSeparator();
            popupMenu.add(menuItemCopy);
            popupMenu.add(menuItemPastePop);
            menuItemPastePop.setVisible(false);
            popupMenu.add(menuItemDeletePop);
            popupMenu.addSeparator();
            popupMenu.add(menuItemRenamePop);
            popupMenu.addSeparator();
            popupMenu.add(menuItemOpenTerm);
            popupMenu.add(menuItemFindInFold);
            popupMenu.add(menuItemCreatArch);
            
            table.setComponentPopupMenu(popupMenu);
            
            JScrollPane tableScroll = new JScrollPane(table);
            Dimension d = tableScroll.getPreferredSize();
            tableScroll.setPreferredSize(new Dimension((int)d.getWidth(), (int)d.getHeight()/2));
            detailView.add(tableScroll, BorderLayout.CENTER);

            DefaultMutableTreeNode root = new DefaultMutableTreeNode();
            treeModel = new DefaultTreeModel(root);

            TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent tse){
                    treeChange(tse);
                }
            };

            File[] roots = fileSystemView.getRoots();
            for (File fileSystemRoot : roots) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
                root.add( node );
                showChildren(node);
                File[] files = fileSystemView.getFiles(fileSystemRoot, true);
                for (File file : files) {
                    if (file.isDirectory()) {
                        node.add(new DefaultMutableTreeNode(file));
                    }
                }
            }

            tree = new JTree(treeModel);
            tree.setRootVisible(false);
            tree.addTreeSelectionListener(treeSelectionListener);
            tree.setCellRenderer(new FileTreeCellRenderer());
            tree.expandRow(0);
            JScrollPane treeScroll = new JScrollPane(tree);

            tree.setVisibleRowCount(15);

            Dimension preferredSize = treeScroll.getPreferredSize();
            Dimension widePreferred = new Dimension(200,(int)preferredSize.getHeight());
            treeScroll.setPreferredSize( widePreferred );

            JToolBar toolBar = new JToolBar();
            toolBar = new JToolBar();
            toolBar.setFloatable(false); 
            JButton back = new JButton();
            back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/back-icon.png")));
            back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            try {
                int leadRow = tree.getLeadSelectionRow();
                previousButtonMouseClicked(tree, leadRow);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
            });
            toolBar.add(back);
            JButton forw = new JButton();
            forw.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/forward-icon.png")));
            forw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            try {
                int leadRow = tree.getLeadSelectionRow();
                previousButtonMouseClicked(tree, leadRow);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
            });
            toolBar.add(forw);
            JButton up = new JButton();
            up.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/up-icon.png")));
            up.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            try {
                TreePath upPath = getUpPath(tree);
                tree.setSelectionPath(upPath);
                tree.scrollPathToVisible(upPath);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
            });
            toolBar.add(up);
            JButton refr = new JButton();
            refr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reload-icon.png")));
            refr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            try {
                int leadRow = tree.getLeadSelectionRow();
                tree.setSelectionPath(tree.getPathForRow(leadRow));
                tree.scrollPathToVisible(tree.getPathForRow(leadRow));
                File getPath = new File(selectedFilePath);
                File[] files = fileSystemView.getFiles(getPath, true);
                setTableData(files);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
            });
            toolBar.add(refr);
            JButton undo = new JButton();
            undo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/undo-icon.png")));
            undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            try {
               stack.undo();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
            });
            toolBar.add(undo);
            JButton redo = new JButton();
            redo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/redo-icon.png")));
            redo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            try {
               stack.redo();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
            });
            toolBar.add(redo);
            JButton hm = new JButton();
            hm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/home-icon.png")));
            hm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            try {
               tree.setSelectionInterval(0,0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
            });
            toolBar.add(hm);
            toolBar.addSeparator();
            text= new JTextField();
            text.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    filepathTextFieldKeyPressed(e);
                }
            });
            toolBar.add(text);
            JButton chooser = new JButton();
            chooser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Search.png")));
            chooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.showOpenDialog(null);
                File f = fileChooser.getSelectedFile();
                String filename=f.getAbsolutePath();
                text.setText(filename);
                File getPath = new File(filename);
                File[] files = fileSystemView.getFiles(getPath, true);
                setTableData(files);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
            });
            toolBar.add(chooser);
            panel.add(toolBar,BorderLayout.NORTH);
            
            JToolBar toolBarS = new JToolBar();
            toolBarS = new JToolBar();
            toolBarS.setFloatable(false); 
            label = new JLabel();
            toolBarS.add(label);
            panel.add(toolBarS,BorderLayout.SOUTH);
            
            JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,treeScroll,detailView);
            panel.add(splitPane, BorderLayout.CENTER);
        }
        return panel;
    }
}
