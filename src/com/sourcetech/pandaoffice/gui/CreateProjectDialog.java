package com.sourcetech.pandaoffice.gui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STDialog;
import com.sourcetech.pandaoffice.frame.STFrame;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.util.FontUtil;
import com.sourcetech.pandaoffice.widget.STImageView;
import com.sourcetech.pandaoffice.widget.STTextButton;
import com.sourcetech.pandaoffice.widget.STTextField;

public class CreateProjectDialog extends STDialog{
	
	private JLayeredPane[] panes;
	private STTextField textField;
	private String[] inputText = new String[2];

	private CreateProjectDialog() {
		super(3, 560, 670, null, true);
		panes = getPanes();
		for(JLayeredPane pane : panes)
			pane.setLayout(null);
		setLocationRelativeTo(null);
		
		init();
	}
	
	public static void main(String[] args) {
		showCreateProjectDialog();
	}

	private void init() {
		
		STPanel bgPanel = new STPanel(new File(Res.Mipmap(Res.STPanel_Default_Background)));
		bgPanel.setBounds(0, 0, 560, 670);
		panes[0].add(bgPanel);
		
		STImageView imageView = new STImageView(Res.Create_Project_Dialog_Img);
		imageView.setBounds(15, 15, 530, 640);
		panes[1].add(imageView);
		
		textField = STTextField.newBuilder()
				.setTexts(new String[] { "",""})
				.setHints(new String[] { "项目名称","项目概述"})
				.setEnables(new boolean[] { true,true})
				.setTypes(new int[] { STTextField.DEFAULT,STTextField.DEFAULT})
				.autoSize(77, 375, new Dimension(408, 47))
				.setPadding(new Insets(5, 30, 5, 30)).build();
		panes[2].add(textField);
		
		STTextButton submit = STTextButton.newBuilder().setText("创 建")
				.setFont(FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 20))
				.setShowShadow(true)
				.setRadius(10f)
				.autoSize(77, 495, new Insets(10, 181, 10, 181))
				.build();
		submit.addButtonListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				inputText[0] = textField.getTextFields()[0].getText();
				inputText[1] = textField.getTextFields()[1].getText();
				if(inputText.equals("")){
					JOptionPane.showMessageDialog(CreateProjectDialog.this, "项目名不能为空");
					return;
				}
				dispose();
			}
		});
		STTextButton cancel = STTextButton.newBuilder().setText("取 消")
				.setFont(FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 20))
				.setBackgroundColor(STColor.GRAY_LIGHT)
				.setForegroundColor(STColor.WHITE)
				.setShowShadow(true)
				.setRadius(10f)
				.autoSize(77, 555, new Insets(10, 181, 10, 181))
				.build();
		cancel.addButtonListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		panes[2].add(submit);
		panes[2].add(cancel);
	}
	
	public String[] getText(){
		return inputText;
	}
	
	public static String[] showCreateProjectDialog(){
		final CreateProjectDialog createProject = new CreateProjectDialog();
		createProject.setVisible(true);
		return createProject.getText();
	}
	
}
