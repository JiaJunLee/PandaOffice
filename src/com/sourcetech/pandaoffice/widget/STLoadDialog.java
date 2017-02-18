package com.sourcetech.pandaoffice.widget;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import javax.swing.JLayeredPane;
import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STDialog;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.util.FontUtil;
import com.sourcetech.pandaoffice.util.UIFactory.DefaultTextField;

public class STLoadDialog extends STDialog{
	
	private static final int PANE_COUNT = 2;
	
	private static final int DIALOG_WIDTH = 220;
	private static final int DIALOG_HEIGHT = 100;
	
	private static final Font FONT = FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 16);
	
	private JLayeredPane[] panes;
	
	public STLoadDialog(final String str, Frame owner) {
		
		super(PANE_COUNT, DIALOG_WIDTH, DIALOG_HEIGHT, owner, true);

		setLocationRelativeTo(owner);
		setAlwaysOnTop(true);
		panes = getPanes();
		
		LoadPane loadPane = new LoadPane();
		panes[0].add(loadPane);
		
		final DefaultTextField textField = new DefaultTextField(str, FONT, STColor.GRAY_STANDARD, false);
		textField.autoSize();
		Dimension dimension = textField.getPreferredSize();
		textField.setBounds(90, (DIALOG_HEIGHT-dimension.height)/2, 130, dimension.height);
		panes[1].add(textField);
		
		new Thread(){public void run() {
			for(int i=0;i<4;i++){
				try {
					String temp = "";
					for(int j=0;j<i;j++)
						temp += ".";
					textField.setText(str+temp);
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(i==3)
					i=0;
			}
		};}.start();
		
	}
	
	public static void main(String[] args) {
		new STLoadDialog("正在处理",null).showDialog();
	}
	
	public void showDialog() {
		new Thread(){public void run() {
			setVisible(true);
		};}.start();
	}
	
	class LoadPane extends STPanel{
		
		public LoadPane(){
			super(new File(Res.Mipmap(Res.STPanel_Default_Background)));
			setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
			setBounds(0, 0, DIALOG_WIDTH, DIALOG_HEIGHT);
			loadingAnimation();
		}
		
		private int startAngle;
		private int arcAngle;

		private void loadingAnimation() {
			new Thread(){
				public void run() {
					while(true){
						try {
							sleep(5);
							if(arcAngle==-360)
								arcAngle = 0;
							else
								arcAngle --;
							if(startAngle==-360)
								startAngle = 0;
							else
								startAngle -=2;
							repaint();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
			}.start();
		}
		
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);   
			g2d.setStroke(new BasicStroke(2));   
			g2d.setColor(STColor.GRAY_STANDARD);
			g2d.drawArc(30, 30, 40, 40, startAngle, arcAngle);
		}
		
	}
	
	

}
