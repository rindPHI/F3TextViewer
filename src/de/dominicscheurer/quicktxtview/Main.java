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
