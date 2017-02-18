package com.sourcetech.pandaoffice.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.util.FontUtil;
import com.sourcetech.pandaoffice.widget.STSwitchButton.TestFrame;

public class STWorkTitle extends STPanel{

	public static final Color DEFAULT_TEXT_COLOR = STColor.decode("#858585");
	public static final Color DEFAULT_LINE_COLOR = STColor.decode("#a3a3a3");
	public static final int DEFAULT_TEXT_MARGIN = 10;
	public static final Font DEFAULT_TEXT_FONT = FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 20f);
	public static final int DEFAULT_WIDTH = 787;
	public static final int DEFAULT_HEIGHT = 23;
	
	private String title;
	
	public STWorkTitle(String title) {
		super(true);
		this.title = title;
	}
	
	public void paint(Graphics g) {
		
		Graphics2D graphics2d = (Graphics2D) g;
		// 去锯齿
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int textWidth = FontUtil.getTextWidth(this, DEFAULT_TEXT_FONT, title);
		int textAscent = FontUtil.getTextAscent(this, DEFAULT_TEXT_FONT);
		
		graphics2d.setColor(DEFAULT_TEXT_COLOR);
		graphics2d.setFont(DEFAULT_TEXT_FONT);
		graphics2d.drawString(title, 0, textAscent);
		
		graphics2d.setColor(DEFAULT_LINE_COLOR);
		graphics2d.drawLine(textWidth + DEFAULT_TEXT_MARGIN, DEFAULT_HEIGHT/2, DEFAULT_WIDTH, DEFAULT_HEIGHT/2);

	}
	
	public void setBounds(int x, int y) {
		setBounds(x, y, DEFAULT_WIDTH+x, DEFAULT_HEIGHT);
	}
	
	public static void main(String[] args) {
		TestFrame frame = new TestFrame();
		STWorkTitle title = new STWorkTitle("个人项目");
		title.setBounds(0, 0);
		frame.getContentPane().add(title);
		frame.setVisible(true);
	}

}
