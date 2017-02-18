package com.sourcetech.pandaoffice.run;

import javax.swing.SwingUtilities;

import com.sourcetech.pandaoffice.gui.PandaOfficeLoginGUI;

public class ProgramLaunch {

	public static void main(String[] args) {
		  SwingUtilities.invokeLater(new Runnable() {   
	            public void run() {   
	            	PandaOfficeLoginGUI loginGUI = new PandaOfficeLoginGUI();
	        		loginGUI.setVisible(true);
	            }   
	        });   
		
	}

}
