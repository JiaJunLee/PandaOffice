package com.sourcetech.pandaoffice.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Scrollbar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.widget.STSwitchButton.TestFrame;


public class ScrollTest {

	public ScrollTest() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		TestFrame frame = new TestFrame();
		
		JPanel p1 = new JPanel();
		p1.setBorder(null);
		p1.setLayout(null);
		p1.setBackground(Color.RED);
		
		JPanel p2 = new JPanel();
		p2.setBorder(null);
		p2.setBackground(STColor.GREEN);
		p2.setLayout(null);
		p1.setPreferredSize(new Dimension(300, 2000));
		p2.setBounds(0, 0, 300, 5000);
		p1.add(p2);
		
		
		String str = "";
		for(int i=0;i<100;i++){
			str += "TEST \n";
		}
		JTextArea textField = new JTextArea(str);
		textField.setBounds(0, 0, 100, 2000);
		p2.add(textField);
		
		
		JScrollPane panel = new JScrollPane(p1);
		panel.setBorder(null);
		panel.setBounds(0, 0, 410, 400);
		panel.getVerticalScrollBar().setUnitIncrement(50);
		panel.getVerticalScrollBar().setBackground(STColor.WHITE);
		panel.getVerticalScrollBar().setPreferredSize(new Dimension(20, 400));
		panel.getVerticalScrollBar().setUI(new MyScrollBar());
		panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		System.out.println(panel.getVerticalScrollBar().getPreferredSize());
		
		frame.getContentPane().add(panel);
		frame.setVisible(true);
	}
	
	public static class MyScrollBar extends BasicScrollBarUI{
		
		private Color defaultColor = STColor.LIGHT_GRAY;
		private Color focusColor = STColor.GRAY_STANDARD;
		
		private boolean mouseEnter = false;
		private boolean mousePress = false;
		
		@Override
		protected void installComponents() {
			super.installComponents();
			scrollbar.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					mouseEnter = true;
					scrollbar.repaint();
				}
				@Override
				public void mouseExited(MouseEvent e) {
					mouseEnter = false;
					scrollbar.repaint();
				}
				public void mouseReleased(MouseEvent e) {
					mousePress = false;
					scrollbar.repaint();
				}
				public void mousePressed(MouseEvent e) {
					mousePress = true;
					scrollbar.repaint();
				}
			});
		}
		
		public void paint(Graphics g, JComponent c) {
			super.paint(g, c);
			if(c.getRootPane()!=null)
				c.getRootPane().repaint();
			uninstallComponents();	
		}
		@Override
		protected JButton createDecreaseButton(int orientation) {
			// TODO Auto-generated method stub
			JButton button = new JButton();
			button.setVisible(false);
			button.setPreferredSize(new Dimension(0, 0));
			return button;
		}
		
		@Override
		protected JButton createIncreaseButton(int orientation) {
			JButton button = new JButton();
			button.setVisible(false);
			button.setPreferredSize(new Dimension(0, 0));
			return button;
		}
		
		protected void paintTrack(Graphics g, JComponent c,
				Rectangle trackBounds) {}
		
		public void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) 
		 { 
		 //super.paintThumb(g, c, thumbBounds); 
		int tw = thumbBounds.width; 
		 int th = thumbBounds.height; 
		 // 重定图形上下文的原点，这句一定要写，不然会出现拖动滑块时滑块不动的现象 
		 g.translate(thumbBounds.x, thumbBounds.y); 
		 
		 Graphics2D g2D = (Graphics2D) g; 
		// 去锯齿
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		 g2D.setColor((mouseEnter||mousePress)?focusColor:defaultColor); 
		 g2D.fillRoundRect(2, 0, scrollbar.getWidth()-4, th, 6, 6); 
		 
		 } 
		
		
		
	}

}
