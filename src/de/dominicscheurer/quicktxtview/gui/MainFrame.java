/* This file is part of F3TextViewer.
 * 
 * F3TextViewer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * F3TextViewer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with F3TextViewer.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2015 by Dominic Scheurer <dscheurer@dominic-scheurer.de>.
 */

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
