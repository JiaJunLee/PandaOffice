package com.sourcetech.pandaoffice.widget;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.Res.MipmapRes;

public class STImageView extends JLabel{
	
	private static BufferedImage image;
	
	public STImageView(){}
	
	public STImageView(MipmapRes res){
		try {
			image = ImageIO.read(new File(Res.Mipmap(res)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setIcon(new ImageIcon(image));
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public void setIcon(BufferedImage image) {
		this.image = image;
		super.setIcon(new ImageIcon(image));
	}

}
