package com.sourcetech.pandaoffice.widget;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.detail.Res.MipmapRes;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.util.ImageUtil;

import javafx.scene.image.Image;

public class STUserIcon extends STPanel {

	private int diameter;
	private BufferedImage image;
	
	public STUserIcon(int diameter, File iconFile) {
		super(true);
		this.diameter = diameter;
		try {
			image = ImageIO.read(iconFile);
			image = ImageUtil.getRoundedImage(image, diameter-12, diameter-12, 2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D graphics2d = (Graphics2D) g;
		// 去锯齿
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setColor(STColor.GRAY_USER_ICON_CIRCLE);
		graphics2d.setStroke(new BasicStroke(2f));
		Ellipse2D circle = new Ellipse2D.Double(1.5, 1.5, diameter-3, diameter-3);
		graphics2d.draw(circle); 
		
		graphics2d.drawImage(image, null, 6, 6);
	}

}
