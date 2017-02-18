package com.sourcetech.pandaoffice.frame;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.JPanel;

import com.android.ninepatch.NinePatch;
import com.sourcetech.pandaoffice.detail.STColor;

public class STPanel extends JPanel {

	private static final long serialVersionUID = 1946716108277691078L;

	public static final int DEFAULT_PANEL = 0;
	public static final int NINE_PATCH_PANEL = 1;

	private int type = DEFAULT_PANEL;
	private NinePatch ninePatch;
	private float alpha = 1.0f;

	public STPanel(File bgFile) {
		try {
			type = NINE_PATCH_PANEL;
			setBackground(STColor.ALPHA);
			InputStream stream = new FileInputStream(bgFile);
			ninePatch = NinePatch
					.load(stream, true /* is9Patch */, false /* convert */);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public STPanel(boolean opaque) {
		setLayout(null);
		if (opaque)
			setBackground(STColor.ALPHA);
	}
	
	public void repaint() {
		super.repaint();
		if(getRootPane()!=null)
			getRootPane().repaint();
	}
	
	public void setAlpha(float alpha){
		if(alpha>=0.0f && alpha<=1.0f){
			this.alpha = alpha;
			repaint();
			if(getRootPane()!=null)
				getRootPane().repaint();
		}
	}

	protected void paintComponent(Graphics g) {
		Graphics2D graphics2d = (Graphics2D) g.create();  
        graphics2d.setComposite(AlphaComposite.SrcOver.derive(alpha));  
		if(type==NINE_PATCH_PANEL){
			Rectangle clip = graphics2d.getClipBounds();
			ninePatch.draw(graphics2d, clip.x, clip.y, clip.width, clip.height);
		}
		super.paintComponent(graphics2d);
	}

}
