package de.dominicscheurer.quicktxtview.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileSystemView;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -5884481485293710785L;

	final FileTreePanel ftPanel;
	
	public MainFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		final DisplayPanel displayPanel = new DisplayPanel();

		ftPanel = new FileTreePanel(FileSystemView
				.getFileSystemView().getRoots()[0].toString());
		ftPanel.setPreferredSize(300, 800);
		
		ftPanel.addDirectoryChangeListener(new DirectoryListener() {
			@Override
			public void directorySelectionChanged(File directory) {
				displayPanel.displayFileNamesInDirectory(directory);
			}
		});
		
		ftPanel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F3) {
					displayPanel.displayFileContentsInDirectory(ftPanel.getChosenDirectory());
				}
			}
		});

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				ftPanel, displayPanel);

		getContentPane().add(splitPane);
	}
	
	public void expandToFile(File file) {
		ftPanel.expandToFile(file);
	}
}
