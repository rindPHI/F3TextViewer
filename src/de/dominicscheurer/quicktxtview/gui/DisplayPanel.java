package de.dominicscheurer.quicktxtview.gui;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class DisplayPanel extends JPanel {
	private static final long serialVersionUID = 3448497585770186687L;
	
	JTextPane textPane = null;
	
	public DisplayPanel() {
		textPane = new JTextPane();
		javax.swing.text.html.HTMLEditorKit eKit = new javax.swing.text.html.HTMLEditorKit();
		textPane.setEditorKit(eKit);
		textPane.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void displayFileNamesInDirectory(File directory) {
		File[] files = listFiles(directory);
		
		if (files == null) {
			textPane.setText("");
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("<HTML><BODY>");
		for (File file : files) {
			sb.append(file.getName());
			sb.append("<br/>");
		}
		sb.append("</BODY></HTML>");
		
		textPane.setText(sb.toString());
	}
	
	public void displayFileContentsInDirectory(File directory) {
		File[] files = listFiles(directory);
		
		if (files == null) {
			textPane.setText("");
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("<HTML><BODY>");
		for (File file : files) {
			if (file.length() > 1024) {
				continue;
			}
			
			try {
				byte[] encoded = Files.readAllBytes(file.toPath());
				String contentsString = new String(encoded, Charset.defaultCharset());
				contentsString = contentsString.replace("<", "&lt;");
				contentsString = contentsString.replace(">", "&gt;");
				contentsString = contentsString.replace("\n", "<br/>");
				
				sb.append("<h3>");
				sb.append(file.getName());
				sb.append("</h3>");
				sb.append(contentsString);
				sb.append("<br/><hr/><br/>");
			} catch (IOException e) {}
		}
		sb.append("</BODY></HTML>");
		
		textPane.setText(sb.toString());
	}

	private File[] listFiles(File directory) {
		if (directory == null) {
			return new File[0];
		}
		
		File[] files = directory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
		
		if (files == null) {
			return new File[0];
		}
		
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return Long.compare(o1.lastModified(), o2.lastModified());
			}
		});
		
		return files;
	}
	
}
