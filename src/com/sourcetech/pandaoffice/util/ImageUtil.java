package com.sourcetech.pandaoffice.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;

public class ImageUtil {

	public static List<int[]> getPixelsARGB(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		List<int[]> res = new ArrayList<int[]>();
		List<Integer> t = new ArrayList<Integer>();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				t.add(image.getRGB(col, row));
			}
		}
		for (int i = 0; i < t.size(); i += 1) {
			int[] rr = new int[4];
			int argb = t.get(i);
			rr[0] = (argb >> 24) & 0xff;
			rr[1] = (argb >> 16) & 0xFF;
			rr[2] = (argb >> 8) & 0xFF;
			rr[3] = (argb) & 0xFF;
			res.add(rr);
		}
		return res;
	}
	
	// MainFrame背景生成相关
	private static BufferedImage BLANK_BACKGROUND;
	private static final int BACKGROUND_WIDTH  = 1330;
	private static final int BACKGROUND_HEIGHT  = 762;
	
	// Project View Item图片生成相关
	private static BufferedImage PROJECT_VIEW_SHADOW;
	private static final int PROJECT_VIEW_WIDTH = 240;
	private static final int PROJECT_VIEW_HEIGHT = 135;
	public static final Font PROJECT_TEXT_FONT = FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 17f);
	
	static{
		try {
			BLANK_BACKGROUND = ImageIO.read(new File(Res.Mipmap(Res.Image_Util_Blank_Background)));
			PROJECT_VIEW_SHADOW = ImageIO.read(new File(Res.Mipmap(Res.Image_Util_Project_View_Shadow)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static BufferedImage getBackgroundImage(BufferedImage img){
		
		BufferedImage result = new BufferedImage(BACKGROUND_WIDTH, BACKGROUND_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = result.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.drawImage(BLANK_BACKGROUND, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, null);
		
		img = (BufferedImage) getScaledImage(img, BACKGROUND_WIDTH-30, 1);
		img = getRoundedImage(img, 23);
		g.drawImage(img, 15, 15, BACKGROUND_WIDTH-30, BACKGROUND_HEIGHT-30, null);
		
		g.dispose();
		return result;
	}
	
	public static BufferedImage getProjectImage(BufferedImage img, String projectName){
		
		BufferedImage result = new BufferedImage(PROJECT_VIEW_WIDTH, PROJECT_VIEW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = result.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.drawImage(img, 0, 0, PROJECT_VIEW_WIDTH, PROJECT_VIEW_HEIGHT, null);
		g.drawImage(PROJECT_VIEW_SHADOW, 0, 0, PROJECT_VIEW_WIDTH, PROJECT_VIEW_HEIGHT, null);
		
		g.setFont(PROJECT_TEXT_FONT);
		g.setColor(Color.WHITE);
		int textAscent = FontUtil.getTextAscent(new JPanel(), PROJECT_TEXT_FONT);
		if(projectName.length()>10){
			projectName = projectName.substring(0, 10);
			projectName += "..";
		}
		g.drawString(projectName, 20, 15 + textAscent);
		
		
		g.dispose();
		return result;
		
	}
	
	
	public static BufferedImage getRoundedImage(BufferedImage img, int radius) {

		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = result.createGraphics();
		// 抗锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(img, 0, 0, null);// 在适当的位置画出

		// 圆角
		if (radius > 0) {
			RoundRectangle2D round = new RoundRectangle2D.Double(0, 0, img.getWidth(), img.getHeight(), radius, radius);
			Area clear = new Area(new Rectangle(0, 0, img.getWidth(), img.getHeight()));
			clear.subtract(new Area(round));
			g.setComposite(AlphaComposite.Clear);
			g.fill(clear);
			g.dispose();
		}
		return result;
	}

	public static BufferedImage getRoundedImage(BufferedImage img, int size, int radius, int type) {

		BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = result.createGraphics();

		// 先按最小宽高为size等比例绽放, 然后图像居中抠出直径为size的圆形图像
		Image fixedImg = getScaledImage(img, size, type);
		g.drawImage(fixedImg, (size - fixedImg.getWidth(null)) / 2, (size - fixedImg.getHeight(null)) / 2, null);// 在适当的位置画出

		// 圆角
		if (radius > 0) {
			RoundRectangle2D round = new RoundRectangle2D.Double(0, 0, size, size, radius * 2, radius * 2);
			Area clear = new Area(new Rectangle(0, 0, size, size));
			clear.subtract(new Area(round));
			g.setComposite(AlphaComposite.Clear);

			// 抗锯齿
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.fill(clear);
			g.dispose();
		}
		return result;
	}

	public static Image getScaledImage(BufferedImage img, int maxSize, int type) {
		int w0 = img.getWidth();
		int h0 = img.getHeight();
		int w = w0;
		int h = h0;
		if (type == 1) {
			w = w0 > h0 ? maxSize : (maxSize * w0 / h0);
			h = w0 > h0 ? (maxSize * h0 / w0) : maxSize;
		} else if (type == 2) {
			w = w0 > h0 ? (maxSize * w0 / h0) : maxSize;
			h = w0 > h0 ? maxSize : (maxSize * h0 / w0);
		}
		Image image = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = result.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(image, 0, 0, null);// 在适当的位置画出
		return result;
	}

}
