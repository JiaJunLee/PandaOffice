package com.sourcetech.pandaoffice.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.tools.Diagnostic;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.util.FontUtil;
import com.sourcetech.pandaoffice.widget.STSwitchButton.TestFrame;
import com.sourcetech.pandaoffice.widget.STTextButton;
import com.sourcetech.pandaoffice.widget.STTextButton.ButtonListener;

public class TabView extends JPanel{
	
	public interface TabViewListener{
		public void ButtonSelect(int select, MouseEvent e);
	}

	public static class Builder{
		private String[] texts = new String[]{};
		private Insets padding = new Insets(0, 0, 0, 0);
		private Font font = STTextButton.DEFAULT_FONT;
		private Color foregroundColor = STColor.DEFAULT_BLUE;
		private Color focusColor = STColor.DEFAULT_BLUE;
		private Color selectColor = STColor.DEFAULT_BLUE;
		private Color lineColor = STColor.DEFAULT_BLUE;
		private int onSelect = 0;
		private double lineHeight = 2.0;
		private Builder(){};
		public Builder setButtonTexts(String[] texts){
			this.texts = texts;
			return this;
		}
		public Builder setFont(Font font){
			this.font = font;
			return this;
		}
		public Builder setForegroundColor(Color foregroundColor) {
			this.foregroundColor = foregroundColor;
			return this;
		}
		public Builder setFocusColor(Color focusColor) {
			this.focusColor = focusColor;
			return this;
		}
		public Builder setSelectColor(Color selectColor) {
			this.selectColor = selectColor;
			return this;
		}
		public Builder setLineColor(Color lineColor){
			this.lineColor = lineColor;
			return this;
		}
		public Builder setLineHeight(double lineHeight){
			this.lineHeight = lineHeight;
			return this;
		}
		public Builder setOnSelect(int onSelect){
			this.onSelect = onSelect;
			return this;
		}
		public Builder setButtonPadding(Insets padding){
			this.padding = padding;
			return this;
		}
		public TabView build(){
			return new TabView(texts, foregroundColor, focusColor, selectColor, padding, font, onSelect, lineColor, lineHeight);
		}
	}
	
	public static Builder newBuilder(){
		return new Builder();
	}
	
	private ArrayList<STTextButton> buttons; 
	private int onSelect = 0;
	private TabViewListener tabViewListener;
	private HashMap<Integer, Rectangle> buttonRectangles;
	private int[] buttonTextWidth;
	private Color foregroundColor;
	private Color focusColor;
	private Color selectColor;
	private Color lineColor;
	private double lineHeight;
	private int buttonHeight;
	private Insets buttonPadding;
	
	// 控制线条动画坐标
	private int currentX;
	private int currentWidth;
	
	private TabView(String[] texts, Color foregroundColor, Color focusColor, Color selectColor, Insets buttonPadding, Font font, int onSelect, Color lineColor, double lineHeight){
		this.setBackground(STColor.ALPHA);
		this.setLayout(null);
		this.buttonPadding = buttonPadding;
		this.foregroundColor = foregroundColor;
		this.focusColor = focusColor;
		this.selectColor = selectColor;
		this.lineColor = lineColor;
		this.lineHeight = lineHeight;
		this.buttonTextWidth = new int[texts.length];
		
		buttons = new ArrayList<STTextButton>();
		buttonRectangles = new HashMap<>();
		int width = 0;
		for(int i=0;i<texts.length;i++){
			if(i!=0){
				width += buttonRectangles.get(Integer.valueOf(i-1)).width;
			}
			final STTextButton button = STTextButton.newBuilder()
											  .setText(texts[i])
											  .onlyText()
											  .setForegroundColor(this.foregroundColor)
											  .setFocusColor(this.focusColor)
											  .setSelectColor(this.selectColor)
											  .setOnSelect((onSelect==i)?true:false)
											  .setShowShadow(false)
											  .setFont(font)
											  .autoSize(width, 0, buttonPadding)
											  .build();
			
			Rectangle bounds = button.getBounds();
			buttonRectangles.put(i, bounds);
			buttonTextWidth[i] = bounds.width-buttonPadding.left-buttonPadding.right;
			
			final int count = i;
			button.addSelectListener(new STTextButton.ButtonListener() {
				public void stateChange(MouseEvent e) {
					if(tabViewListener!=null){
						int beforeSelect = TabView.this.onSelect;
						TabView.this.onSelect = count;
						if(!button.getOnSelect()){
							resetButtons();
							button.setOnSelect(true);
							tabViewListener.ButtonSelect(count, e);
							new Animation(TabView.this, buttonRectangles.get(beforeSelect).x+TabView.this.buttonPadding.left, buttonRectangles.get(count).x+TabView.this.buttonPadding.left, TabView.this.buttonTextWidth[beforeSelect], TabView.this.buttonTextWidth[count]).start();
						}
					}
				}
			});
			buttons.add(button);
			add(button);
		}

		this.currentX = buttonPadding.left;
		if(texts.length>0)
			this.currentWidth = buttonTextWidth[0];
	}
	
	private void resetButtons(){
		for(int i=0;i<buttons.size();i++)
			buttons.get(i).setOnSelect(false);
	}
	
	
	public void addTabViewListener(TabViewListener listener){
		this.tabViewListener = listener;
	}
	
	class Animation extends Thread{
		private TabView tabView;
		private int fromX;
		private int toX;
		private int fromWidth;
		private int toWidth;
		public Animation(TabView tabView, int fromX, int toX, int fromWidth, int toWidth){
			this.tabView = tabView;
			this.fromX = fromX;
			this.toX = toX;
			this.fromWidth = fromWidth;
			this.toWidth = toWidth;
		}
		private int nextLocal(int from, int to){
			if(from<to)
				from++;
			else
				from--;
			return from;
		}
		public void run() {
			currentX = fromX;
			currentWidth = fromWidth;
			while(currentX!=toX || currentWidth!=toWidth){
				if(currentX!=toX){
					currentX = nextLocal(currentX, toX);
				}
				if(currentWidth!=toWidth){
					currentWidth = nextLocal(currentWidth, toWidth);
				}
				try {
					tabView.repaint();
					tabView.getRootPane().repaint();
					sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D graphics2d = (Graphics2D) g;
		// 去锯齿
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON);
		
		graphics2d.setColor(lineColor);
		Rectangle2D line = new Rectangle2D.Double(currentX, buttonHeight, currentWidth, lineHeight);
		graphics2d.fill(line);
	
	}
	
	public void autoSize(int x,int y){
		int width = 0;
		for(int i=0;i<buttons.size();i++){
			Rectangle bounds = buttonRectangles.get(Integer.valueOf(i));
			width += bounds.width;
			buttonHeight = bounds.height;
		}
		setBounds(x, y, width, buttonHeight+(int)lineHeight);
	}


	
	public static void main(String[] args) {
		TestFrame frame = new TestFrame();
		TabView tabView = TabView.newBuilder()
								 .setButtonTexts(new String[]{"登录","注册","忘记密码"})
								 .setButtonPadding(new Insets(5, 15, 5, 15))
								 .setLineHeight(3)
								 .build();
		tabView.autoSize(50, 50);
		tabView.addTabViewListener(new TabViewListener() {
			public void ButtonSelect(int select, MouseEvent e) {
				System.out.println(select);
			}
		});
		frame.getContentPane().setBackground(STColor.ALPHA);
		frame.getContentPane().add(tabView);
		frame.setVisible(true);
	}

}
