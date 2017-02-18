package com.sourcetech.pandaoffice.frame;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JLayeredPane;

import com.sourcetech.pandaoffice.detail.STColor;

public class STDialog extends JDialog {

	private JLayeredPane[] panes = null;

	public STDialog(int paneCount, int width, int height, Frame owner, boolean modal) {
		super(owner, modal);
		setSize(new Dimension(width, height));
		setUndecorated(true);
		setBackground(STColor.ALPHA);

		panes = new JLayeredPane[paneCount];

		for (int i = panes.length - 1; i >= 0; i--) {
			panes[i] = new JLayeredPane();
			panes[i].setBackground(STColor.ALPHA);
			panes[i].setBounds(0, 0, width, height);
			this.getContentPane().add(panes[i]);
		}

	}

	public JLayeredPane[] getPanes() {
		return panes;
	}


}
