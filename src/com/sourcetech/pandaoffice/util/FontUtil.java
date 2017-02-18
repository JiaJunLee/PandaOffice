package com.sourcetech.pandaoffice.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;


public class FontUtil {

	
	public static Font getFont(String res, int style, float size){
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File(res));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return font.deriveFont(style, size);
	}
	
	public static int getTextWidth(JComponent pane, Font f, String str){
		FontMetrics fm = pane.getFontMetrics(f);
		return SwingUtilities.computeStringWidth(fm, str);
	}
	
	public static int getTextHeight(JComponent pane, Font f){
		FontMetrics fm = pane.getFontMetrics(f);
		return fm.getHeight();
	}
	
	public static int getTextAscent(JComponent pane, Font f){
		FontMetrics fm = pane.getFontMetrics(f);
		return fm.getAscent();
	}

}
