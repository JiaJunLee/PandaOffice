package com.sourcetech.pandaoffice.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STDialog;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.util.FontUtil;
import com.sourcetech.pandaoffice.widget.STTextButton;
import com.sourcetech.pandaoffice.widget.STTextField;

public class CreateTaskDialog  extends STDialog{
	
	private JLayeredPane[] panes;
	private STTextField textField;
	private String[] inputText = new String[5];

	public CreateTaskDialog() {
		super(2, 447, 380, null, true);
		panes = getPanes();
		for(JLayeredPane pane : panes)
			pane.setLayout(null);
		setLocationRelativeTo(null);
		
		init();
	}

	private void init() {
		STPanel bgPanel = new STPanel(new File(Res.Mipmap(Res.STPanel_Default_Background)));
		bgPanel.setBounds(0, 0, 447, 380);
		panes[0].add(bgPanel);
		
		textField = STTextField.newBuilder()
				.setTexts(new String[] { "","","","",""})
				.setHints(new String[] { "任务名称","任务概述","任务开始时间","任务结束时间","任务执行者"})
				.setEnables(new boolean[] { true,true,true,true,true})
				.setTypes(new int[] { STTextField.DEFAULT,STTextField.DEFAULT,STTextField.DEFAULT,STTextField.DEFAULT,STTextField.DEFAULT})
				.autoSize(20, 20, new Dimension(408, 47))
				.setPadding(new Insets(5, 30, 5, 30)).build();
		panes[1].add(textField);
		
		STTextButton submit = STTextButton.newBuilder().setText("创 建")
				.setFont(FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 20))
				.setShowShadow(true)
				.setRadius(10f)
				.autoSize(20, 265, new Insets(10, 181, 10, 181))
				.build();
		submit.addButtonListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JTextComponent[] components = textField.getTextFields();
				for(int i=0;i<components.length;i++){
					inputText[i] = components[i].getText();
					if(inputText[i].equals("")){
						JOptionPane.showMessageDialog(CreateTaskDialog.this, "项目名不能为空");
						return;
					}
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
				.autoSize(20, 315, new Insets(10, 181, 10, 181))
				.build();
		cancel.addButtonListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		panes[1].add(submit);
		panes[1].add(cancel);
	}
	
	public String[] getText(){
		return inputText;
	}
	
	public static String[] showCreateProjectDialog(){
		final CreateTaskDialog createTask = new CreateTaskDialog();
		createTask.setVisible(true);
		return createTask.getText();
	}
	
//	public static void main(String[] args) {
//		showCreateProjectDialog();
//	}

}
