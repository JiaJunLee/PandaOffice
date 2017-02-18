package com.sourcetech.pandaoffice.widget;

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
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.util.FontUtil;
import com.sourcetech.pandaoffice.widget.STSwitchButton.TestFrame;

public class STTextField extends JPanel {

	public static final Color DEFAULT_TEXT_COLOR = STColor.DEFAULT_BLUE;
	public static final Color DEFAULT_LINE_COLOR = STColor.GRAY_LIGHT;
	public static final Color DEFAULT_HINT_TEXT_COLOR = STColor.GRAY_LIGHT;
	public static final Font DEFAULT_FONT = FontUtil.getFont(Res.Font(Res.FZ_LTH),
			0, 15);

	public static final int DEFAULT = 0;
	public static final int PASSWORD = 1;
	
	// 用来限制字符长度
	public static class InputDocument extends PlainDocument{

		private static final long serialVersionUID = 1L;
		
		private int length;
		
		public InputDocument(int length) {
			super();
			this.length = length;
		}
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			if(str==null)
				return;
			if((getLength()+str.length())<=length){
				char upper[] = str.toCharArray();
				super.insertString(offs, new String(upper), a);
			}
		}

	}

	public static class Builder {
		private String[] texts = new String[] {};
		private String[] hints = new String[] {};
		private int[] types = new int[] {};
		private boolean[] enables = new boolean[] {};
		private int x = 0;
		private int y = 0;
		private Dimension dimension = new Dimension(0, 0);
		private Insets padding = new Insets(0, 0, 0, 0);
		private double radius = 10;
		private Font font = DEFAULT_FONT;
		private Color textColor = DEFAULT_TEXT_COLOR;
		private Color lineColor = DEFAULT_LINE_COLOR;
		private Color hintTextColor = DEFAULT_HINT_TEXT_COLOR;
		private Builder() {
		}

		public Builder setTexts(String[] texts) {
			this.texts = texts;
			return this;
		}

		public Builder setHints(String[] hints) {
			this.hints = hints;
			return this;
		}

		public Builder setTypes(int[] types) {
			this.types = types;
			return this;
		}

		public Builder setEnables(boolean[] enables) {
			this.enables = enables;
			return this;
		}

		public Builder setTextColor(Color textColor) {
			this.textColor = textColor;
			return this;
		}
		
		public Builder setHintTextColor(Color hintTextColor){
			this.hintTextColor = hintTextColor;
			return this;
		}

		public Builder setFont(Font font) {
			this.font = font;
			return this;
		}

		public Builder setPadding(Insets padding) {
			this.padding = padding;
			return this;
		}

		public Builder setRadius(double radius) {
			this.radius = radius;
			return this;
		}

		public Builder setLineColor(Color lineColor) {
			this.lineColor = lineColor;
			return this;
		}

		public Builder autoSize(int x, int y, Dimension dimension) {
			this.x = x;
			this.y = y;
			this.dimension = dimension;
			return this;
		}

		public STTextField build() {
			return new STTextField(texts, hints, types, enables, x, y,
					dimension, padding, font, textColor, radius, lineColor);
		}
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	private JTextComponent[] textFields;
	private String[] hints;
	private Dimension dimension;
	private Insets padding;
	private double radius;
	private Color lineColor;
	private Font font;


	public JTextComponent[] getTextFields(){
		return textFields;
	}
	
	private STTextField(String[] texts, String[] hints, int[] types,
			boolean[] enables, int x, int y, Dimension dimension,
			Insets padding, Font font, Color textColor, double radius,
			Color lineColor) {

		setLayout(null);
		setBackground(STColor.ALPHA);

		this.hints = hints;
		this.dimension = dimension;
		this.padding = padding;
		this.radius = radius;
		this.lineColor = lineColor;
		this.font = font;

		textFields = new JTextField[texts.length];
		for (int i = 0; i < texts.length; i++) {
			// 初始化
			switch (types[i]) {
			case DEFAULT:
				textFields[i] = new JTextField();
				break;
			case PASSWORD:
				textFields[i] = new JPasswordField();
				((JPasswordField) textFields[i]).setEchoChar('*');
				break;
			}
			// 样式
			textFields[i].setText(texts[i]);
			textFields[i].setBorder(null);
			textFields[i].setBackground(null);
			textFields[i].setFont(font);
			textFields[i].setEnabled(enables[i]);
//			textFields[i].setFocusable(false);
			if (enables[i])
				textFields[i].setForeground(textColor);
			else
				textFields[i].setDisabledTextColor(textColor);
			// 设置边界信息
			int textFieldHeight = dimension.height - padding.top
					- padding.bottom;
			int textFieldWidth = dimension.width - padding.left - padding.right;
			textFields[i].setBounds(padding.left, textFieldHeight * i + (i + 1)
					* padding.top + i * padding.bottom, textFieldWidth,
					textFieldHeight);
			// 添加到panel
			add(textFields[i]);
		}
		// 设置panel边界信息
		setBounds(x, y, dimension.width, dimension.height * texts.length);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (getRootPane() != null)
			getRootPane().repaint();

		Graphics2D graphics2d = (Graphics2D) g;
		// 去锯齿
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// 绘制背景
		Rectangle bounds = getBounds();
		graphics2d.setColor(STColor.WHITE);
		RoundRectangle2D rectangle2d = new RoundRectangle2D.Double(0, 0,
				bounds.width - 1, bounds.height - 1, radius, radius);
		graphics2d.fill(rectangle2d);

		// 绘制边框
		graphics2d.setColor(lineColor);
		graphics2d.draw(rectangle2d);

		// 绘制分割线
		for (int i = 1; i < textFields.length; i++) {
			int y = (dimension.height - padding.top - padding.bottom) * i
					+ padding.top + (i - 1) * (padding.top + padding.bottom)
					+ (padding.top + padding.bottom) / 2;
			graphics2d.drawLine(0, y, bounds.width, y);
		}
		
		// 绘制hint
		graphics2d.setFont(font);
		for(int i=0;i<textFields.length;i++){
			if(textFields[i].getText().equals("")){
				int textHeight = FontUtil.getTextHeight(this, font);
				int textAscent = FontUtil.getTextAscent(this, font);
				int y = (dimension.height-textHeight)/2 + textAscent + i*dimension.height;
				graphics2d.drawString(hints[i], padding.left, y);
			}
		}

	}

	public static void main(String[] args) {
		TestFrame frame = new TestFrame();
		frame.getContentPane().setBackground(STColor.alpha(STColor.GRAY, 50));

		STTextField textField = STTextField.newBuilder()
				.setTexts(new String[] { "", "" })
				.setHints(new String[] { "手机号或邮箱", "密码" })
				.setEnables(new boolean[] { true, true})
				.setTypes(new int[] { DEFAULT, PASSWORD })
				.autoSize(50, 50, new Dimension(360, 61))
				.setPadding(new Insets(5, 30, 5, 30)).build();
		
		
		textField.getTextFields()[1].setDocument(new InputDocument(5));

		frame.getContentPane().add(textField);
		frame.setVisible(true);
	}

}
