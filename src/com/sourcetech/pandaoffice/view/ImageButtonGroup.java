package com.sourcetech.pandaoffice.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JButton;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.widget.STImageButton;
import com.sourcetech.pandaoffice.widget.STSwitchButton.TestFrame;

public class ImageButtonGroup extends STPanel {

	public static void main(String[] args) {
		TestFrame frame = new TestFrame();
		ImageButtonGroup buttonGroup = new ImageButtonGroup();
		buttonGroup.setBounds(50, 50);
		buttonGroup.setButtonMargin(48);
		buttonGroup.setCircleMargin(15);
		buttonGroup.setLayout(VERTICAL);

		String[] icons = new String[] { Res.Mipmap(Res.Nav_Calendar_Btn), Res.Mipmap(Res.Nav_Company_Btn),
				Res.Mipmap(Res.Nav_Item_Btn), Res.Mipmap(Res.Nav_Message_Btn), Res.Mipmap(Res.Nav_My_Btn) };
		for (int i = 0; i < icons.length; i++) {
			STImageButton button = STImageButton.newBuilder().setIcon(icons[i]).setLayout(STImageButton.HORIZONAL)
					.setShowShadow(true).setText("项 目").setButtonPadding(10).setTextMargin(12).build();
			buttonGroup.addButton(button);
		}
		buttonGroup.setSelect(0);

//		buttonGroup.remove(0);

		new Thread() {
			public void run() {
				try {
					sleep(5000);
//					buttonGroup.remove(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();

		buttonGroup.addSelectListener(new ButtonGroupListener() {
			public void OnSelected(int index) {
				System.out.println(index);
			}

			public void OnMetaDown(int index) {

			}
		});

		frame.getContentPane().add(buttonGroup);
		frame.setVisible(true);
	}

	public static final int VERTICAL = 0;
	public static final int HORIZONAL = 1;
	public static final int DEFAULT_MARGIN = 10;
	public static final int DEFAULT_DIAMETER = 10;
	public static final int DEFAULT_CIRCLE_MARGIN = 8;
	public static final int DEFAULT_SPEED = 8;

	private ArrayList<STImageButton> buttons = new ArrayList<STImageButton>();
	private int layout = HORIZONAL;
	private int margin = DEFAULT_MARGIN;
	private int circleMargin = DEFAULT_CIRCLE_MARGIN;
	private int speed = DEFAULT_SPEED;
	private int x = 0;
	private int y = 0;
	private ButtonGroupListener buttonGroupListener;
	private int currentIndex = -1;
	private int currentX = 0;
	private int currentY = 0;

	public ImageButtonGroup() {
		super(true);
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setBounds(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setButtonMargin(int margin) {
		this.margin = margin;
	}

	public void setCircleMargin(int circleMargin) {
		this.circleMargin = circleMargin;
	}

	public void setLayout(int layout) {
		this.layout = layout;
	}

	public void addSelectListener(ButtonGroupListener buttonGroupListener) {
		this.buttonGroupListener = buttonGroupListener;
	}

	public void addButton(final STImageButton button) {
		buttons.add(button);
		add(button);
		autoBounds();
		button.addButtonListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.isMetaDown()) {
					if (buttonGroupListener != null)
						buttonGroupListener.OnMetaDown(buttons.indexOf(button));
					return;
				}
				System.out.println("====>>>" + buttons.indexOf(button));
				setSelect(indexOf(button));
			}
		});
	}

	public int indexOf(STImageButton button) {
		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i) == button)
				return i;
		}
		return -1;
	}

	public void autoBounds() {
		if (buttons.size() == 0)
			return;
		int x = 0, y = 0;
		for (int i = 0; i < buttons.size(); i++) {
			STImageButton button = buttons.get(i);
			button.autoSize(x, y);
			switch (layout) {
			case HORIZONAL:
				x += buttons.get(i).getBounds().width + margin;
				break;
			case VERTICAL:
				y += buttons.get(i).getBounds().height + margin;
				break;
			}
		}
		Rectangle buttonBounds = buttons.get(0).getBounds();
		int width = layout == HORIZONAL ? x : buttonBounds.width + circleMargin + DEFAULT_DIAMETER;
		int height = layout == HORIZONAL ? buttonBounds.height + circleMargin + DEFAULT_DIAMETER : y;
		setBounds(this.x, this.y, width, height);
	}

	public void remove(int index) {
		buttons.remove(index);
		autoBounds();
		super.remove(index);
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D graphics2d = (Graphics2D) g;
		// 去锯齿
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// 绘制圆形
		if (buttons.size() > 0 && currentIndex!=-1) {
			graphics2d.setColor(STColor.DEFAULT_BLUE);
			Ellipse2D circle = new Ellipse2D.Double(currentX, currentY, DEFAULT_DIAMETER, DEFAULT_DIAMETER);
			graphics2d.fill(circle);
		}
	}

	public int getSelect() {
		return currentIndex;
	}

	public void setSelect(int index) {
		int to = 0;
		for (int i = 0; i < buttons.size(); i++) {
			switch (layout) {
			case HORIZONAL:
				if (i < index)
					to += buttons.get(i).getBounds().width + margin;
				if (i == index)
					to += buttons.get(i).getBounds().width / 2 - DEFAULT_DIAMETER / 2;
				currentY = buttons.get(i).getBounds().height + circleMargin - DEFAULT_DIAMETER / 2;
				break;
			case VERTICAL:
				if (i < index)
					to += buttons.get(i).getBounds().height + margin;
				if (i == index)
					to += buttons.get(i).getBounds().height / 2 - DEFAULT_DIAMETER / 2;
				currentX = buttons.get(i).getBounds().width + circleMargin;
				break;
			}
		}
		switch (layout) {
		case HORIZONAL:
			new Animation(this, currentX, to, speed, layout).start();
			break;
		case VERTICAL:
			new Animation(this, currentY, to, speed, layout).start();
			break;
		}
		if (buttonGroupListener != null)
			if (currentIndex != index)
				buttonGroupListener.OnSelected(index);
		currentIndex = index;
	}

	public static interface ButtonGroupListener {
		public void OnSelected(int index);

		public void OnMetaDown(int index);
	}

	class Animation extends Thread {
		private ImageButtonGroup buttonGroup;
		private int from;
		private int to;
		private int speed;
		private int layout;

		public Animation(ImageButtonGroup buttonGroup, int from, int to, int speed, int layout) {
			this.buttonGroup = buttonGroup;
			this.from = from;
			this.to = to;
			this.speed = speed;
			this.layout = layout;
		}

		public void run() {
			switch (layout) {
			case HORIZONAL:
				while (currentX != to) {
					if (from < to) {
						currentX += speed;
						if (currentX > to)
							currentX = to;
					} else {
						currentX -= speed;
						if (currentX < to)
							currentX = to;
					}
					try {
						sleep(4);
						buttonGroup.repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
			case VERTICAL:
				while (currentY != to) {
					if (from < to) {
						currentY += speed;
						if (currentY > to)
							currentY = to;
					} else {
						currentY -= speed;
						if (currentY < to)
							currentY = to;
					}
					try {
						sleep(4);
						buttonGroup.repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
			}
		}
	}

}
