package com.sourcetech.pandaoffice.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.widget.STSwitchButton.TestFrame;

import sun.awt.windows.WWindowPeer;

public class STCutline extends STPanel {
	
	private int finish;
	private int recent;
	private int willExpire;
	private int overdue;
	
	private Rectangle bounds;
	
	private int[] weights = {0,0,0,0};
	
	private static final Color[] colors = {
			STColor.decode("#75c940"),
			STColor.decode("#0080cc"),
			STColor.decode("#ed8b00"),
			STColor.decode("#ed2700")
	};
	
	public STCutline(int finish, int recent, int willExpire, int overdue, Rectangle bounds) {
		super(true);
		
		this.finish = finish;
		this.recent = recent;
		this.willExpire = willExpire;
		this.overdue = overdue;
		this.bounds = bounds;
		
		init();
		setBounds(bounds);
	}
	
	public static void main(String[] args) {
		TestFrame frame = new TestFrame();
		STCutline cutline = new STCutline(20, 20, 20, 20, new Rectangle(0, 0, 600, 20));
		frame.getContentPane().add(cutline);
		frame.setVisible(true);
	}
	
	private void init() {
		int size = finish + recent + willExpire + overdue;
		if(finish!=0)
		weights[0] = finish * 100 / size;
		if(recent!=0)
		weights[1] = recent * 100 / size;
		if(willExpire!=0)
		weights[2] = willExpire * 100 / size;
		weights[3] = 100 - weights[0] - weights[1] - weights[2];
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D graphics2d = (Graphics2D) g;
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int width = bounds.width;
		int height = bounds.height;
		
		int x = 0;
		
		for(int i=0;i<weights.length;i++){
			if(weights[i]==0)
				break;
			int rw = width * weights[i] / 100 ;
			Rectangle tempRectangle = new Rectangle(x, 0, rw, height);
			graphics2d.setColor(colors[i]);
			graphics2d.fill(tempRectangle);
			x += rw;
		}
		
	}

}
