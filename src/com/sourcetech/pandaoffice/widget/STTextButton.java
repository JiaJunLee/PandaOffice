package com.sourcetech.pandaoffice.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.FontUIResource;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.util.FontUtil;

public class STTextButton extends JPanel {

	private static final long serialVersionUID = 5624710595871000868L;
	
	public static final Color DEFAULT_FOREGROUND_COLOR = STColor.WHITE;
	public static final Color DEFAULT_BACKGROUND_COLOR = STColor.DEFAULT_BLUE;
	public static final Color DEFAULT_SELECT_COLOR = STColor.DEFAULT_BLUE;
	public static final Color DEFAULT_FOCUS_COLOR = STColor.DEFAULT_BLUE;
	public static final float DEFAULT_RADIUS = 8f;
	public static final Font DEFAULT_FONT = FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 21);
	public static final Rectangle DEFAULT_BOUNDS = new Rectangle(0, 0, 0, 0);
	public static final boolean DEFAULT_SHOW_SHADOW = true;
	public static final boolean DEFAULT_STYLE = true; 
	public static final boolean DEFAULT_SELECT = false;
	
	private String text;
	private Color foregroundColor;
	private Color backgroundColor;
	private Color selectColor;
	private Color focusColor;
	private float radius;
	private Font font;
	private Rectangle bounds;
	private boolean showShadow;
	private boolean style;
	private boolean onSelect;
	private MouseListener buttonListener;
	private ButtonListener selectListener;
	
	public interface ButtonListener{
		public void stateChange(MouseEvent e);
	}
	
	// 构造器
	public static class Builder{
		private Builder(){}
	
		private String text = "";
		private Color foregroundColor = DEFAULT_FOREGROUND_COLOR;
		private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
		private Color selectColor = DEFAULT_SELECT_COLOR;
		private Color focusColor = DEFAULT_FOCUS_COLOR;
		private float radius = DEFAULT_RADIUS;
		private Font font = DEFAULT_FONT;
		private Rectangle bounds = DEFAULT_BOUNDS;
		private boolean showShadow = DEFAULT_SHOW_SHADOW;
		private boolean style = DEFAULT_STYLE;
		private boolean onSelect = DEFAULT_SELECT;
		
		
		public STTextButton build(){
			return new STTextButton(text, foregroundColor, backgroundColor, selectColor, focusColor, font, radius, bounds, showShadow, style, onSelect);
		}
		
		public Builder setSelectColor(Color selectColor){
			this.selectColor = selectColor;
			return this;
		}
		
		public Builder setFocusColor(Color focusColor){
			this.focusColor = focusColor;
			return this;
		}
		
		public Builder setOnSelect(boolean onSelect){
			this.onSelect = onSelect;
			return this;
		}

		public Builder setText(String text) {
			this.text = text;
			return this;
		}

		public Builder setForegroundColor(Color foregroundColor) {
			this.foregroundColor = foregroundColor;
			return this;
		}

		public Builder setBackgroundColor(Color backgroundColor) {
			this.backgroundColor = backgroundColor;
			return this;
		}

		public Builder setRadius(float radius) {
			this.radius = radius;
			return this;
		}

		public Builder setFont(Font font) {
			this.font = font;
			return this;
		}
		
		public Builder setBounds(Rectangle bounds){
			this.bounds = bounds;
			return this;
		}
		
		public Builder setShowShadow(boolean showShadow){
			this.showShadow = showShadow;
			return this;
		}
		
		public Builder onlyText(){
			this.style = false;
			this.backgroundColor = STColor.ALPHA;
			return this;
		}
		
		public Builder autoSize(int x, int y, Insets insets){
			int textWidth = FontUtil.getTextWidth(new JPanel(), font, text);
			int textHeight = FontUtil.getTextHeight(new JPanel(), font);
			bounds = new Rectangle(x, y, textWidth + insets.left + insets.right , textHeight + insets.top + insets.bottom);
			return this;
		}
	}
	
	public void addButtonListener(MouseListener listener){
		this.buttonListener = listener;
	}
	
	public void addSelectListener(ButtonListener listener){
		this.selectListener = listener;
	}

	private STTextButton(String text, Color foregroundColor, Color backgroundColor, Color selectColor, Color focusColor, Font font, float radius, Rectangle bounds, boolean showShadow, boolean style, boolean onSelect) {
		this.text = text;
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
		this.selectColor = selectColor;
		this.focusColor = focusColor;
		this.radius = radius;
		this.font = font;
		this.bounds = bounds;
		this.showShadow = showShadow;
		this.style = style;
		this.onSelect = onSelect;
		
		// 设置边界信息
		setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
		setPreferredSize(new Dimension(bounds.width,bounds.height));
		
		setBackground(STColor.ALPHA);
		
		initListener();
		
	}
	
	// 构造器
	public static Builder newBuilder(){
		return new Builder();
	}
	
	private boolean isFocus = false;
	private boolean isClick = false;
	
	
	public void repaint() {
		super.repaint();
		if(getRootPane()!=null)
			getRootPane().repaint();
	}
	
	private void initListener(){
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("点击");	
				isFocus = true;
				isClick = false;
				if(buttonListener!=null)
					buttonListener.mouseClicked(e);
				repaint();
			}
			public void mouseEntered(MouseEvent e) {
				System.out.println("路过");
				isFocus = true;
				if(buttonListener!=null)
					buttonListener.mouseEntered(e);
				repaint();
			}
			public void mouseExited(MouseEvent e) {
				System.out.println("离开");
				isFocus = false;
				if(buttonListener!=null)
					buttonListener.mouseExited(e);
				repaint();
			}
			public void mousePressed(MouseEvent e) {
				System.out.println("按压");
				isFocus = false;
				isClick = true;
				if(!style){
					if(selectListener!=null)
						selectListener.stateChange(e);
				}
				if(buttonListener!=null)
					buttonListener.mousePressed(e);
				repaint();
			}
			public void mouseReleased(MouseEvent e) {
				isFocus = false;
				isClick = false;
				if(buttonListener!=null)
					buttonListener.mouseReleased(e);
				repaint();
			}
		});
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics2d = (Graphics2D) g;
		// 去锯齿
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON);
		
		// 绘制背景
		graphics2d.setColor(backgroundColor);
		Rectangle rectangle = getBounds();
		RoundRectangle2D roundRectangle2D = new RoundRectangle2D.Float(0, 0, rectangle.width, rectangle.height, radius, radius);
		graphics2d.fill(roundRectangle2D);
		
		if(isFocus && showShadow){
			graphics2d.setColor(STColor.alpha(STColor.WHITE, 40));
			graphics2d.fill(roundRectangle2D);
		}
		
		if(isClick && showShadow){
			graphics2d.setColor(STColor.alpha(STColor.BLACK, 40));
			graphics2d.fill(roundRectangle2D);
		}
		
		
		// 绘制文字
		if(isFocus && !style && !onSelect){
			graphics2d.setColor(focusColor);
		}else if(!style){
			graphics2d.setColor((onSelect)?selectColor:foregroundColor);
		}else{
			graphics2d.setColor(foregroundColor);
		}
		graphics2d.setFont(font);
		int textWidth = FontUtil.getTextWidth(this, font, text);
		int textHeight = FontUtil.getTextHeight(this, font);
		int textAscent = FontUtil.getTextAscent(this, font);
		graphics2d.drawString(text, (bounds.width-textWidth)/2, (bounds.height-textHeight)/2 + textAscent);
	}
	
	public boolean getOnSelect(){
		return onSelect;
	}
	
	public void setOnSelect(boolean onSelect){
		this.onSelect = onSelect;
		repaint();
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		
		STTextButton textButton = STTextButton.newBuilder().setText("登录")
		.setFont(FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 20))
		.setShowShadow(true)
		.setRadius(10f)
		.autoSize(50, 50, new Insets(10, 10, 10, 10))
		.build();
		
		//textButton.setOnSelect(true);
		
		textButton.addButtonListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println(e);
			}
		});
		
		frame.getContentPane().add(textButton);
		frame.setVisible(true);
	}

}
