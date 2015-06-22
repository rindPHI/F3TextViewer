package de.dominicscheurer.quicktxtview.gui;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EventListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * @author User "JavaPF" from
 *         http://www.javaprogrammingforums.com/java-swing-tutorials
 *         /7944-how-use-jtree-create-file-system-viewer-tree.html
 *
 */
public class FileTreePanel extends JPanel {
	private static final long serialVersionUID = 3243950254209997245L;

	private File chosenFolder = null;
	
	private ArrayList<DirectoryListener> listeners = new ArrayList<DirectoryListener>();

	private JTree fileTree;
	private FileSystemModel fileSystemModel;
	private JTextArea fileDetailsTextArea = new JTextArea();
	private JScrollPane scrollPane = null;

	public FileTreePanel(String directory) {
		fileDetailsTextArea.setEditable(false);
		fileSystemModel = new FileSystemModel(new File(directory));
		fileTree = new JTree(fileSystemModel);
		fileTree.setEditable(true);
		fileTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent event) {
				chosenFolder = (File) fileTree.getLastSelectedPathComponent();
				for (DirectoryListener listener : listeners) {
					listener.directorySelectionChanged(chosenFolder);
				}
			}
		});
		scrollPane = new JScrollPane(fileTree);
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	public File getChosenDirectory() {
		return chosenFolder;
	}
	
	public void expandToFile(File file) {
		final ArrayList<File> pathToExpand = new ArrayList<File>();
		pathToExpand.add(file.toPath().getRoot().toFile());
		Iterator<Path> it = file.toPath().iterator();
		while (it.hasNext()) {
			pathToExpand.add(new File(it.next().toString()));
		}
		
		fileTree.expandPath(new TreePath(pathToExpand.toArray(new File[pathToExpand.size()])));
	}
	
	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
		scrollPane.setPreferredSize(preferredSize);
	}
	
	@Override
	public synchronized void addKeyListener(KeyListener l) {
		super.addKeyListener(l);
		fileTree.addKeyListener(l);
	}
	
	public void setPreferredSize(int width, int height) {
		setPreferredSize(new Dimension(width, height));
	}
	
	public void addDirectoryChangeListener(DirectoryListener listener) {
		listeners.add(listener);
	}
}

interface DirectoryListener extends EventListener {
	void directorySelectionChanged(File directory);
}

class FileSystemModel implements TreeModel {
	private File root;

	private Vector<TreeModelListener> listeners = new Vector<TreeModelListener>();

	public FileSystemModel(File rootDirectory) {
		root = rootDirectory;
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public Object getChild(Object parent, int index) {
		File directory = (File) parent;
		String[] children = subDirectories(directory);
		return new TreeFile(directory, children[index]);
	}

	@Override
	public int getChildCount(Object parent) {
		File file = (File) parent;
		if (file.isDirectory()) {
			String[] fileList = subDirectories(file);
			if (fileList != null)
				return fileList.length;
		}
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		File file = (File) node;
		return file.isFile();
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		File directory = (File) parent;
		File file = (File) child;
		String[] children = subDirectories(directory);
		for (int i = 0; i < children.length; i++) {
			if (file.getName().equals(children[i])) {
				return i;
			}
		}
		return -1;

	}

	@Override
	public void valueForPathChanged(TreePath path, Object value) {
		File oldFile = (File) path.getLastPathComponent();
		String fileParentPath = oldFile.getParent();
		String newFileName = (String) value;
		File targetFile = new File(fileParentPath, newFileName);
		oldFile.renameTo(targetFile);
		File parent = new File(fileParentPath);
		int[] changedChildrenIndices = { getIndexOfChild(parent, targetFile) };
		Object[] changedChildren = { targetFile };
		fireTreeNodesChanged(path.getParentPath(), changedChildrenIndices,
				changedChildren);
	}

	private void fireTreeNodesChanged(TreePath parentPath, int[] indices,
			Object[] children) {
		TreeModelEvent event = new TreeModelEvent(this, parentPath, indices,
				children);
		Iterator<TreeModelListener> iterator = listeners.iterator();
		TreeModelListener listener = null;
		while (iterator.hasNext()) {
			listener = iterator.next();
			listener.treeNodesChanged(event);
		}
	}

	@Override
	public void addTreeModelListener(TreeModelListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener listener) {
		listeners.remove(listener);
	}

	private String[] subDirectories(File directory) {
		ArrayList<String> result = new ArrayList<String>();
		File[] files = directory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		for (File file: files) {
			result.add(file.getName());
		}
		
		return result.toArray(new String[result.size()]);
	}

	private class TreeFile extends File {
		private static final long serialVersionUID = -8206569869536313414L;

		public TreeFile(File parent, String child) {
			super(parent, child.toString());
		}

		public String toString() {
			return getName();
		}
	}
}
