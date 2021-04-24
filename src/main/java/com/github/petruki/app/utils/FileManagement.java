package com.github.petruki.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.github.petruki.app.model.MatrixSettings;

public class FileManagement {
	
	static FileFilter matrixFileFilter = new FileFilter() {
		@Override
		public String getDescription() {
			return "Density Matrix File (.dmf)";
		}
		
		@Override
		public boolean accept(File f) {
			if (f.isDirectory())
				return true;
			return f.getName().toLowerCase().endsWith(".dmf");
		}
	};
	
	static FileFilter imageFileFilter = new FileFilter() {
		@Override
		public String getDescription() {
			return "Images only (png, jpg)";
		}
		
		@Override
		public boolean accept(File f) {
			if (f.isDirectory())
				return true;
			return f.getName().toLowerCase().endsWith(".png") ||
					f.getName().toLowerCase().endsWith(".jpg");
		}
	};
	
	public static MatrixSettings openReadWork() {
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setDialogTitle("Open saved work");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setCurrentDirectory(new File("./savedwork"));
		fileChooser.setFileFilter(matrixFileFilter);
		int result = fileChooser.showOpenDialog(fileChooser);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			return readWork(fileChooser.getSelectedFile().getAbsolutePath());
		}
		
		return null;
	}
	
	public static MatrixSettings readWork(String destination) {
		try (FileInputStream fin = new FileInputStream(destination);
				ObjectInputStream ois = new ObjectInputStream(fin);) {
			return (MatrixSettings) ois.readObject();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		return null;
	}
	
	public static void openSaveWork(MatrixSettings matrixSettings) {
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setDialogTitle("Save your work");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setCurrentDirectory(new File("./savedwork"));
		fileChooser.setFileFilter(matrixFileFilter);
		int result = fileChooser.showSaveDialog(fileChooser);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			saveWork(matrixSettings, fileChooser.getSelectedFile().getAbsolutePath() + ".dmf");
		}
	}
	
	public static void saveWork(MatrixSettings matrixSettings, String destination) {
		try (FileOutputStream fout = new FileOutputStream(destination);
				ObjectOutputStream oos = new ObjectOutputStream(fout);) {
			oos.writeObject(matrixSettings);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static File openImage() {
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setDialogTitle("Open image");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setCurrentDirectory(new File("./sample"));
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(imageFileFilter);
		int result = fileChooser.showOpenDialog(fileChooser);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		
		return null;
	}

}
