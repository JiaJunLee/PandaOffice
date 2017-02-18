package com.sourcetech.pandaoffice.detail;

import java.awt.Color;
import java.awt.color.ColorSpace;

public class STColor extends Color {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3196066348625023889L;


	public static final Color ALPHA = new Color(0,0,0,0);
	public static final Color DEFAULT_BLUE = Color.decode("#0080cc");
	public static final Color DEEP_BLUE = Color.decode("#0073b7");
	public static final Color GRAY = Color.decode("#868787");
	public static final Color GRAY_LIGHT = Color.decode("#d2d2d2");
	public static final Color GRAY_DEEP = Color.decode("#858585");
	public static final Color GRAY_STANDARD = Color.decode("#ababab");
	public static final Color GRAY_IMAGE_BUTTON_TEXT = Color.decode("#a4a4a4");
	public static final Color GRAY_USER_ICON_CIRCLE = Color.decode("#a3a3a3");
	

	public STColor(ColorSpace cspace, float[] components, float alpha) {
		super(cspace, components, alpha);
		// TODO Auto-generated constructor stub
	}

	public STColor(float r, float g, float b, float a) {
		super(r, g, b, a);
		// TODO Auto-generated constructor stub
	}

	public STColor(float r, float g, float b) {
		super(r, g, b);
		// TODO Auto-generated constructor stub
	}

	public STColor(int rgba, boolean hasalpha) {
		super(rgba, hasalpha);
		// TODO Auto-generated constructor stub
	}

	public STColor(int r, int g, int b, int a) {
		super(r, g, b, a);
		// TODO Auto-generated constructor stub
	}

	public STColor(int r, int g, int b) {
		super(r, g, b);
		// TODO Auto-generated constructor stub
	}

	public STColor(int rgb) {
		super(rgb);
		// TODO Auto-generated constructor stub
	}
	
	public static Color alpha(Color c, int alpha){
		int red = c.getRed();
		int green = c.getGreen();
		int blue = c.getBlue();
		return new Color(red,green,blue,alpha);
	}
	
	public static boolean isDeepColor(int[] a){
		double grayLevel = a[0] * 0.299 + a[1] * 0.587 + a[2] * 0.114;
		if (grayLevel <= 170) {
			return true;
		}
		return false;
	}

	
	
}
