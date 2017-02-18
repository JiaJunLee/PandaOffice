package com.sourcetech.pandaoffice.frame;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;

public class STGridPane extends STPanel {

	private int col = 0;
	private int hgap = 10;
	private int vgap = 10;

	public STGridPane(int col, boolean opaque) {
		super(opaque);
		this.col = col;
	}

	public STGridPane(int col, File bgFile) {
		super(bgFile);
		this.col = col;
	}
	
	public STGridPane(int col, int hgap, int vgap, boolean opaque){
		this(col, opaque);
		this.hgap = hgap;
		this.vgap = vgap;
	}
	
	public void autoSize(){
		Component[] components = getComponents();
		if(components.length==0){
			setPreferredSize(new Dimension(0, 0));
			return;
		}
		Dimension dimension = components[0].getPreferredSize();
		int row = components.length / col;
		if(components.length % col > 0)
			row++;
		for(int i=0;i<row*col;i++){
			if(i<components.length){
				int currentRow = i / col;
				int currentCol = i % col;
				components[i].setBounds(currentCol*(dimension.width + hgap), currentRow * (dimension.height + vgap), dimension.width, dimension.height);
			}
		}
		int width = col * dimension.width + (col-1) * hgap;
		int height = row * dimension.height + (row - 1) * vgap;
		System.err.println(width +"   " +height);
		setPreferredSize(new Dimension(width, height));
	}

}
