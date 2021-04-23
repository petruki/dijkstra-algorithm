package com.github.petruki.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.github.petruki.app.utils.LoadImage;
import java.awt.Font;

@SuppressWarnings("serial")
public class DensityMatrixInputDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextArea inputText;
	
	public DensityMatrixInputDialog() {
		setTitle("Input Matrix");
		setIconImage(LoadImage.load("start.png"));
		setBounds(100, 100, 550, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		centerUI();
		buildView();
	}
	
	private void buildView() {
		inputText = new JTextArea();
		inputText.setTabSize(0);
		inputText.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		contentPanel.add(inputText, BorderLayout.CENTER);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(e -> dispose());
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
	
	private void centerUI() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - getHeight()) / 2);
		setLocation(x, y);
	}

	public String getInput() {
		return inputText.getText();
	}

}
