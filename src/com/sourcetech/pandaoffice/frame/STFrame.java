package com.sourcetech.pandaoffice.frame;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.sourcetech.pandaoffice.detail.STColor;

public class STFrame extends JFrame {

	private static final long serialVersionUID = -5095216049795248965L;

	private void init() {
		System.setProperty("sun.java2d.noddraw", "true");
		//Image img = Toolkit.getDefaultToolkit().getImage(ResourceId.APP_ICON);
		//setIconImage(img);
		
		MouseAdapter mouseAdapter = new WindowDragListener();
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
	}

	private JLayeredPane[] panes = null;
	
	public STFrame(int paneCount, int width, int height){
		init();
		setSize(new Dimension(width, height));
		setUndecorated(true);
		setBackground(STColor.ALPHA);
		
		panes = new JLayeredPane[paneCount];
		
		for(int i=panes.length-1;i>=0;i--){
			panes[i] = new JLayeredPane();
			panes[i].setBackground(STColor.ALPHA);
			panes[i].setBounds(0, 0, width, height);
			this.getContentPane().add(panes[i]);
		}
		
	}
	
	public JLayeredPane[] getPanes(){
		return panes;
	}

	public static void setStartAudio(String res) {
		try {
			File file = new File(res);
			AudioClip sound = Applet.newAudioClip(file.toURI().toURL());
			sound.play();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
		

	class WindowDragListener extends MouseAdapter {

		private Point pressPoint = new Point();

		public void mousePressed(MouseEvent e) {
			pressPoint = e.getPoint();
		}

		public void mouseReleased(MouseEvent e) {
			pressPoint = new Point();
		}

		public void mouseDragged(MouseEvent e) {
			Rectangle r = STFrame.this.getBounds();
			setLocation(r.x + (e.getX() - pressPoint.x), r.y
					+ (e.getY() - pressPoint.y));
		}
	}
	

}
