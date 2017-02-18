package com.sourcetech.pandaoffice.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.print.attribute.standard.MediaSize.Other;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class STSwitchButton extends JPanel {

	public static final float DEFAULT_REDIUS = 40f;
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(220, 220,
			220);
	public static final Color DEFAULT_FOREGROUND_COLOR = Color.WHITE;
	public static final Color DEFAULT_SELECT_COLOR = new Color(90, 200, 255);
	public static final int DEFAULT_PADDING = 3;
	public static final int DEFAULT_SPEED = 6;

	public static final int CLOSE_STATE = 0;
	public static final int OPEN_STATE = 1;

	private float radius = 0f;
	private Color backgroundColor = null;
	private Color foregroundColor = null;
	private Color selectColor = null;
	private int state = 0;
	private int padding = 0;
	private int diameter = 0;
	private int currentX = 0;
	private int maxX = 0;
	private int speed = 0;
	private boolean doInit = true;
	private SwitchButtonListener buttonListener;
	
	public interface SwitchButtonListener{
		public void ButtonOpen(MouseEvent event);
		public void ButtonClose(MouseEvent event);
	}
	
	public void addSwitchButtonListener(SwitchButtonListener buttonListener){
		this.buttonListener = buttonListener;
	}
	
	
	//  构造器
	public static class Builder {

		private float radius = DEFAULT_REDIUS;
		private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
		private Color foregroundColor = DEFAULT_FOREGROUND_COLOR;
		private Color selectColor = DEFAULT_SELECT_COLOR;
		private int state = CLOSE_STATE;
		private int padding = DEFAULT_PADDING;
		private int speed = DEFAULT_SPEED;

		public Builder setBackgroundColor(Color color) {
			this.backgroundColor = color;
			return this;
		}

		public Builder setForegroundColor(Color color) {
			this.foregroundColor = color;
			return this;
		}

		public Builder setSelectColor(Color color) {
			this.selectColor = color;
			return this;
		}

		public Builder setState(int state) {
			this.state = state;
			return this;
		}

		public Builder setPadding(int padding) {
			this.padding = padding;
			return this;
		}

		public Builder setSpeed(int speed) {
			this.speed = speed;
			return this;
		}

		public Builder setRadius(float radius) {
			this.radius = radius;
			return this;
		}

		private Builder() {
		}

		public STSwitchButton build() {
			return new STSwitchButton(backgroundColor, foregroundColor,
					selectColor, radius, state, padding, speed);
		}
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	private STSwitchButton(Color backgroundColor, Color foregroundColor,
			Color selectColor, float radius, int state, int padding, int speed) {
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
		this.selectColor = selectColor;
		this.radius = radius;
		this.state = state;
		this.padding = padding;
		this.speed = speed;

		this.setBackground(new Color(0, 0, 0, 0));
		addListener(this);

	}

	private void addListener(STSwitchButton button) {
		button.addMouseListener(new MouseAdapter() {
			// 记录鼠标按压的X坐标
			private int beforeX = 0;

			public void mousePressed(MouseEvent arg0) {
				beforeX = arg0.getX();
			}

			public void mouseReleased(MouseEvent arg0) {
				int afterX = arg0.getX();
				int beforeState = state; // 记录之前的状态
				state = judgeState(beforeX, afterX); // 获取改变状态
				if (beforeState != state) {
					// 当状态发生改变
					switch (state) {
					case OPEN_STATE:
						new AnimationThread(STSwitchButton.this, maxX, speed)
								.start();
						buttonListener.ButtonOpen(arg0);
						break;
					case CLOSE_STATE:
						new AnimationThread(STSwitchButton.this, padding, speed)
								.start();
						buttonListener.ButtonClose(arg0);
						break;
					}
				}
			}
		});
	}

	protected void setCurrentX(int currentX) {
		this.currentX = currentX;
	}

	protected int getCurrentX() {
		return currentX;
	}

	class AnimationThread extends Thread {
		private STSwitchButton button = null;
		private int toX = 0;
		private int speed = 0;

		public AnimationThread(STSwitchButton button, int toX, int speed) {
			this.button = button;
			this.toX = toX;
			this.speed = speed;
		}

		public void run() {
			if (button != null) {
				int temp = button.getCurrentX();
				while (toX != temp) {
					try {
						if (temp < toX) {
							temp += speed;
							if (temp > toX)
								temp = toX;
						} else {
							temp -= speed;
							if (temp < toX)
								temp = toX;
						}
						button.setCurrentX(temp);
						button.repaint();
						if(button.getRootPane()!=null)
							button.getRootPane().repaint();
						sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private int judgeState(int beforeX, int afterX) {
		if (afterX > beforeX)
			return OPEN_STATE;
		if (afterX < beforeX)
			return CLOSE_STATE;
		return state;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D graphics2d = (Graphics2D) g;
		
		graphics2d.setColor((state == CLOSE_STATE) ? backgroundColor
				: selectColor);

		// 去锯齿
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Rectangle bounds = getBounds();
		RoundRectangle2D roundRectangle2D = new RoundRectangle2D.Float(0, 0,
				bounds.width, bounds.height, radius, radius);

		graphics2d.fill(roundRectangle2D);
		
		graphics2d.setColor(foregroundColor);

		diameter = (bounds.height - (padding * 2)); // 直径
		maxX = bounds.width - diameter - padding;
		if (doInit) {
			currentX = (state == CLOSE_STATE) ? padding : maxX;
			doInit = false;
		}
		Ellipse2D ellipse2d = new Ellipse2D.Float(currentX, DEFAULT_PADDING,
				diameter, diameter);

		graphics2d.fill(ellipse2d);
	}

	public static void main(String[] args) {
		TestFrame frame = new TestFrame();

		// SwitchButton button = new SwitchButton();
		// button.setBounds(100, 100, 200, 40);
		// frame.getContentPane().add(button);
		STSwitchButton button = STSwitchButton.newBuilder().setSpeed(5).setState(
				STSwitchButton.OPEN_STATE).build();
		button.setBounds(100, 100, 100, 40);
		button.addSwitchButtonListener(new SwitchButtonListener() {
			public void ButtonOpen(MouseEvent event) {
				System.out.println("打开");
			}
			
			public void ButtonClose(MouseEvent event) {
				System.out.println("关闭");
			}
		});
		frame.getContentPane().add(button);

		frame.setVisible(true);
	}
	
	public static class TestFrame extends JFrame {

		public TestFrame() {
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(new Dimension(500, 500));
			setLocationRelativeTo(null);
			getContentPane().setBackground(Color.white);
			setLayout(null);
		}
	}

}




