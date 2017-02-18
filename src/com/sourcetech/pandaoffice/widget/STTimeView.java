package com.sourcetech.pandaoffice.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STFrame;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.util.FontUtil;
import com.sourcetech.pandaoffice.util.UIFactory.DefaultTextField;
import com.sourcetech.pandaoffice.widget.STSwitchButton.TestFrame;

public class STTimeView extends STPanel {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd  EEEE");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
	private static final Font[] TEXT_FONTS = { FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 21),
			FontUtil.getFont(Res.Font(Res.HAGIN_CAPS), 0, 130) };
	private static final int DEFAULT_TEXT_MARGIN = 35;
	
	

	private String dateStr;
	private String ampmStr;
	private String currentTimeStr;
	private Color textColor;
	private int x;
	private int y;

	public static void main(String[] args) {
		TestFrame frame = new TestFrame();
		STTimeView timeView = new STTimeView(Color.BLACK);
		timeView.setBounds(0, 0);
		frame.getContentPane().add(timeView);
		frame.setVisible(true);
	}

	public STTimeView(Color textColor) {
		super(true);
		this.textColor = textColor;
		new Thread() {
			public void run() {
				while (true) {
					repaint();
					try {
						sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public void setBounds(int x, int y) {
		this.x = x;
		this.y = y;
		int dateStrWidth = FontUtil.getTextWidth(this, TEXT_FONTS[0], dateStr);
		int currentTimeStrWidth = FontUtil.getTextWidth(this, TEXT_FONTS[1], currentTimeStr);
		int currentTimeStrHeight = FontUtil.getTextHeight(this, TEXT_FONTS[1]);
		super.setBounds(x, y, dateStrWidth + currentTimeStrWidth + DEFAULT_TEXT_MARGIN, currentTimeStrHeight);
	}

	public void paint(Graphics g) {
		
		Date date = new Date(System.currentTimeMillis());
		// 获取日期
		dateStr = DATE_FORMAT.format(date);
		// 获取上午/下午
		ampmStr = getAMPM();
		// 当前时间字符串
		currentTimeStr = TIME_FORMAT.format(date);

		int dateStrWidth = FontUtil.getTextWidth(this, TEXT_FONTS[0], dateStr);
		int ampmStrWidth = FontUtil.getTextWidth(this, TEXT_FONTS[0], ampmStr);
		int ampmStrHeight = FontUtil.getTextHeight(this, TEXT_FONTS[0]);
		int ampmStrAscent = FontUtil.getTextAscent(this, TEXT_FONTS[0]);
		int currentTimeStrHeight = FontUtil.getTextHeight(this, TEXT_FONTS[1]);
		int currentTimeStrAscent = FontUtil.getTextAscent(this, TEXT_FONTS[1]);

		Graphics2D graphics2d = (Graphics2D) g;
		// 去锯齿
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		graphics2d.setColor(textColor);
		graphics2d.setFont(TEXT_FONTS[0]);
		graphics2d.drawString(dateStr, 0, (currentTimeStrHeight - 2*ampmStrHeight - 28 - 6) / 2 + ampmStrAscent);
		graphics2d.drawString(ampmStr, dateStrWidth - ampmStrWidth, (currentTimeStrHeight - 2*ampmStrHeight - 28 - 6) / 2 + ampmStrAscent + ampmStrHeight + 6);
		graphics2d.setFont(TEXT_FONTS[1]);
		graphics2d.drawString(currentTimeStr, dateStrWidth + DEFAULT_TEXT_MARGIN, currentTimeStrAscent - 10);

		setBounds(x, y);
	}

	private String getAMPM() {
		GregorianCalendar ca = new GregorianCalendar();
		int ampm = ca.get(GregorianCalendar.AM_PM);
		switch (ampm) {
		case 0:
			return "上午";
		case 1:
			return "下午";
		}
		return "";
	}

}
