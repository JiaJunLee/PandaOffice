package com.sourcetech.pandaoffice.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.util.FontUtil;
import com.sourcetech.pandaoffice.util.ImageUtil;
import com.sourcetech.pandaoffice.widget.STSwitchButton.TestFrame;

public class STImageButton extends STPanel {

	public static final Color DEFAULT_TEXT_COLOR = STColor.GRAY_IMAGE_BUTTON_TEXT;
	public static final Font DEFAULT_TEXT_FONT = FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 12.3f);
	public static final String NONE_TEXT = "";
	public static final int DEFAULT_TEXT_MARGIN = 6;
	public static final int DEFAULT_BUTTON_PADDING = 0;
	public static final int NONE_OPACITY_MIN_ALPHA = 45;
	public static final int SHADOW_ALPHA = 30;
	public static final boolean DEFAULT_SHOW_SHADOW = false;
	
	public static final int VERTICAL = 0x00;
	public static final int HORIZONAL = 0x01;
	
	public static class Builder {

		private BufferedImage image = null;
		private String text = NONE_TEXT;
		private Color textColor = DEFAULT_TEXT_COLOR;
		private Font textFont = DEFAULT_TEXT_FONT;
		private Rectangle bounds = new Rectangle(0, 0, 0, 0);
		private int layout = VERTICAL;
		private int textMargin = DEFAULT_TEXT_MARGIN;
		private int buttonPadding = DEFAULT_BUTTON_PADDING;
		private boolean showShadow = DEFAULT_SHOW_SHADOW;

		private Builder() {
		}

		public Builder setIcon(String res) {
			try {
				image = ImageIO.read(new File(res));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return this;
		}
		
		public Builder setIcon(BufferedImage image){
			this.image = image;
			return this;
		}
		
		public Builder autoSize(int x, int y){
			int textHeight = FontUtil.getTextHeight(new JPanel(), textFont);
			int textWidth = FontUtil.getTextWidth(new JPanel(), textFont, text);
			int width = 0;
			int height = 0;
			switch (layout) {
			case HORIZONAL:
				width = text.equals("")?image.getWidth():image.getWidth() + textWidth + textMargin;
				height = image.getHeight();
				break;
			case VERTICAL:
				width = image.getWidth();
				height = text.equals("")?image.getHeight():image.getHeight()+textHeight + textMargin;
				break;
			}
			bounds = new Rectangle(x, y, width + 2*buttonPadding , height + 2*buttonPadding);
			return this;
		}

		
		public Builder setText(String text) {
			this.text = text;
			return this;
		}
		
		public Builder setTextColor(Color textColor){
			this.textColor = textColor;
			return this;
		}
		
		public Builder setTextFont(Font textFont){
			this.textFont = textFont;
			return this;
		}
		
		public Builder setLayout(int layout){
			this.layout = layout;
			return this;
		}
		
		public Builder setTextMargin(int textMargin){
			this.textMargin = textMargin;
			return this;
		}
		
		public Builder setShowShadow(boolean showShadow){
			this.showShadow = showShadow;
			return this;
		}
		
		public Builder setButtonPadding(int buttonPadding){
			this.buttonPadding = buttonPadding;
			return this;
		}

		public STImageButton build() {
			return new STImageButton(image, text, textColor, textFont, bounds, layout, textMargin, buttonPadding, showShadow);
		}

	}

	public static void main(String[] args) {
		TestFrame frame = new TestFrame();
		STImageButton button = null;
		
		button = STImageButton
				.newBuilder()
				.setIcon(Res.Mipmap(Res.Nav_Message_Btn))
				.setText("？？？？")
				.setLayout(VERTICAL).setShowShadow(true)
				.setButtonPadding(10)
				.autoSize(50, 50)
				.build();
		
		frame.getContentPane().add(button);
		frame.setVisible(true);
		System.out.println(button.getBounds());
	}
	

	private boolean isFocus = false;
	private boolean isClick = false;

	private BufferedImage image;
	private MouseListener buttonListener;
	private String text;
	private Color textColor;
	private Font textFont;
	private int layout;
	private int textMargin;
	private int buttonPadding;
	private boolean showShadow;

	private ArrayList<Point> points;
	private ArrayList<int[]> pixels;
	private Color focusColor;
	private Color clickColor;

	public static Builder newBuilder() {
		return new Builder();
	}
	
	public MouseListener getButtonListener(){
		return buttonListener;
	}

	public void addButtonListener(MouseListener buttonListener) {
		this.buttonListener = buttonListener;
	}

	public STImageButton(BufferedImage image,
			String text, Color textColor, Font textFont, Rectangle bounds, int layout, int textMargin, int buttonPadding, boolean showShadow) {
		super(true);
		this.image = image;
		this.text = text;
		this.textColor = textColor;
		this.textFont = textFont;
		this.layout = layout;
		this.textMargin = textMargin;
		this.buttonPadding = buttonPadding;
		this.showShadow = showShadow;
		init();
		addListener();
		setBounds(bounds);
		setPreferredSize(new Dimension(bounds.width, bounds.height));
	}

	private void init() {
		points = getPoints(image);
		pixels = (ArrayList<int[]>) ImageUtil.getPixelsARGB(image);
		focusColor = STColor.alpha(Color.WHITE, SHADOW_ALPHA);
		clickColor = STColor.alpha(Color.BLACK, SHADOW_ALPHA);
	}

	private void addListener() {
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("点击");
				isFocus = true;
				isClick = false;
				if (buttonListener != null)
					buttonListener.mouseClicked(e);
				repaint();
			}

			public void mouseEntered(MouseEvent e) {
				System.out.println("路过");
				isFocus = true;
				if (buttonListener != null)
					buttonListener.mouseEntered(e);
				repaint();
			}

			public void mouseExited(MouseEvent e) {
				System.out.println("离开");
				isFocus = false;
				if (buttonListener != null)
					buttonListener.mouseExited(e);
				repaint();
			}

			public void mousePressed(MouseEvent e) {
				System.out.println("按压");
				isFocus = false;
				isClick = true;
				if (buttonListener != null)
					buttonListener.mousePressed(e);
				repaint();
			}

			public void mouseReleased(MouseEvent e) {
				isFocus = false;
				isClick = false;
				if (buttonListener != null)
					buttonListener.mouseReleased(e);
				repaint();
			}
		});
	}

	public void repaint() {
		super.repaint();
		if (getRootPane() != null)
			getRootPane().repaint();
	}
	
	public void autoSize(int x, int y){
		int textHeight = FontUtil.getTextHeight(new JPanel(), textFont);
		int textWidth = FontUtil.getTextWidth(new JPanel(), textFont, text);
		int width = 0;
		int height = 0;
		switch (layout) {
		case HORIZONAL:
			width = text.equals("")?image.getWidth():image.getWidth() + textWidth + textMargin;
			height = image.getHeight();
			break;
		case VERTICAL:
			width = image.getWidth();
			height = text.equals("")?image.getHeight():image.getHeight()+textHeight + textMargin;
			break;
		}
		setBounds(x, y, width + 2*buttonPadding, height + 2*buttonPadding);
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2D = (Graphics2D) g;

		// 去锯齿
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// 绘制button image
		g2D.drawImage(image, buttonPadding, buttonPadding, null);

		if ( isFocus || isClick && !showShadow ) {

			// 高亮层颜色
			if (isFocus)
				g2D.setColor(focusColor);
			if (isClick)
				g2D.setColor(clickColor);

			// 绘制图形区域的高亮层
			for (int i = 0; i < pixels.size(); i++) {
				int alpha = pixels.get(i)[0];
				// 判断区域是否存在非透明内容
				if (alpha > NONE_OPACITY_MIN_ALPHA) {
					Point p = points.get(i);
					g2D.drawLine(p.x + buttonPadding, p.y + buttonPadding, p.x + buttonPadding, p.y + buttonPadding);
				}
			}

		}
		
		if(isFocus || isClick && showShadow){
			RoundRectangle2D roundRectangle2D = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10);
			if (isFocus)
				g2D.setColor(focusColor);
			if (isClick)
				g2D.setColor(clickColor);
			g2D.fill(roundRectangle2D);
		}
		
		// 绘制文字
		if(!text.equals("")){
			g2D.setColor(textColor);
			g2D.setFont(textFont);
			int textAscent = FontUtil.getTextAscent(this, textFont);
			int textHeight = FontUtil.getTextHeight(this, textFont);
			int textWidth = FontUtil.getTextWidth(this, textFont, text);
			int x = 0;
			int y = 0;
			switch (layout) {
			case HORIZONAL:
				x = image.getWidth() + textMargin;
				y = (image.getHeight() - textHeight)/2 + textAscent;
				break;
			case VERTICAL:
				x = (image.getWidth()-textWidth)/2;
				y = image.getHeight()+textMargin+textAscent;
				break;
			}
			g2D.drawString(text, x + buttonPadding, y + buttonPadding);
		}

	}

	private class Point {
		public int x;
		public int y;
	}

	private ArrayList<Point> getPoints(BufferedImage image) {
		ArrayList<Point> points = new ArrayList<Point>();
		int width = image.getWidth();
		int height = image.getHeight();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				Point p = new Point();
				p.x = col;
				p.y = row;
				points.add(p);
			}
		}
		return points;
	}

}
