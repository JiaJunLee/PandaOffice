package com.sourcetech.pandaoffice.util;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.Reader;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class UIFactory {

	public static class DefaultTextField extends JTextField {
		
		public DefaultTextField(String text, Font font, Color color, boolean enable) {
			setText(text);
			setBorder(null);
			setBackground(null);
			setOpaque(false);
			setFont(font);
			setEnabled(enable);
			if (enable)
				setForeground(color);
			else
				setDisabledTextColor(color);
		}
		
		public void autoSize(){
			int textWidth = FontUtil.getTextWidth(this,getFont(), getText());
			int textHeight = FontUtil.getTextHeight(this, getFont());
			setSize(textWidth, textHeight);
		}

		public void repaint() {
			super.repaint();
			if(getRootPane()!=null)
				getRootPane().repaint();
		}
	}

}
