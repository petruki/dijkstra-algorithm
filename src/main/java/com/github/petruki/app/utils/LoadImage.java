package com.github.petruki.app.utils;

import java.awt.image.BufferedImage;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class LoadImage {
	
	public static final String IMAGE_ERROR = "Failed to load image %s";

	private LoadImage() {
		// Prevent instantiation
	}
	
	/**
	 * Load image from src/main/resources
	 */
	public static BufferedImage load(String image) {
		try {
			return ImageIO.read(Objects.requireNonNull(LoadImage.class.getClassLoader().getResource(image)));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, String.format(IMAGE_ERROR, image), "Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

}
