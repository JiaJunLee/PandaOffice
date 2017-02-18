package com.sourcetech.pandaoffice.widget;

import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JLayeredPane;

import com.sourcetech.pandaoffice.dao.TaskGroup;
import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STDialog;
import com.sourcetech.pandaoffice.frame.STGridPane;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.gui.ProjectDetailPane;
import com.sourcetech.pandaoffice.util.FontUtil;
import com.sourcetech.pandaoffice.view.ImageButtonGroup.ButtonGroupListener;
import com.sourcetech.pandaoffice.view.TabView;

public class STTaskGroupList extends STDialog {
	
	private static final int WIDTH = 270;
	private static final int HEIGHT = 300;
	
	private JLayeredPane[] panes; 
	private ProjectDetailPane detailPane;
	ArrayList<TaskGroup> groups;
	private ButtonGroupListener buttonGroupListener;

	public STTaskGroupList(ProjectDetailPane detailPane,ArrayList<TaskGroup> groups) {
		super(2, WIDTH, HEIGHT, null, false);
		this.detailPane = detailPane;
		this.groups = groups;
		panes = getPanes();
		initBackground();
		initGroupButtons();
		addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				setVisible(false);
			}
			public void focusGained(FocusEvent e) {
			}
		});
	}
	
	public void addSelectListener(ButtonGroupListener buttonGroupListener){
		this.buttonGroupListener = buttonGroupListener;
	}
	
	private void initGroupButtons() {
		panes[1].setLayout(null);
		STGridPane gridPane = new STGridPane(1, 10, 10, true);
		for(int i=0;i<groups.size();i++){
			final int index = i;
			TaskGroup group = groups.get(index);
			
			STTextButton textButton = STTextButton.newBuilder().setText(group.getName())
					.setFont(FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 16))
					.setShowShadow(true)
					.setRadius(10f)
					.setBackgroundColor(STColor.ALPHA)
					.setForegroundColor(STColor.GRAY_STANDARD)
					.setFocusColor(STColor.DEFAULT_BLUE)
					.autoSize(0, 0, new Insets(5, 10, 5, 10))
					.build();
			textButton.addButtonListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					detailPane.setSelect(index);
					if(buttonGroupListener!=null)
						buttonGroupListener.OnSelected(index);
					setVisible(false);
				}
			});
			gridPane.add(textButton);
			
		}
		
		gridPane.autoSize();
		gridPane.setBounds(20, 20, gridPane.getPreferredSize().width, gridPane.getPreferredSize().height);	
		
		panes[1].add(gridPane);
		
	}

	private void initBackground() {
		STPanel bgPanel = new STPanel(new File(Res.Mipmap(Res.ST_Skin_Chooser_Background)));
		bgPanel.setBounds(0, 0, WIDTH, HEIGHT);
		panes[0].add(bgPanel);
	}
	
}
