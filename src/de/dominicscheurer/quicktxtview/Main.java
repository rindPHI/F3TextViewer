package de.dominicscheurer.quicktxtview;

import java.io.File;

import de.dominicscheurer.quicktxtview.gui.MainFrame;

public class Main {
	
	public static void main(String[] args) {
		MainFrame mf = new MainFrame();
		mf.setSize(1200, 800);
		mf.setTitle("Quick F3 Text Viewer: Press F3 to see contents of all files in the directory.");
		mf.setVisible(true);
		
		File file;
		if (args.length > 0 && (file = new File(args[0]).getAbsoluteFile()).exists()) {
			mf.expandToFile(file);
		}
		else {
			System.out.println("Usage: F3TextViewer [directoryToExpand]");
		}
	}

}
