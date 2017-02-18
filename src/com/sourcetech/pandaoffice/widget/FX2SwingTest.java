package com.sourcetech.pandaoffice.widget;

import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.widget.STSwitchButton.TestFrame;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

public class FX2SwingTest {

	public FX2SwingTest() {
		
		
	}

	public static void main(String[] args){
		TestFrame frame = new TestFrame();
		final JFXPanel jfxPanel = new JFXPanel();
		Platform.runLater(new Runnable() {
			public void run() {
				ImageView imageView = null;
//				Button button = null;
				try {
					imageView = new ImageView(new Image(new FileInputStream(new File("C:/Users/李佳骏/Desktop/1.gif"))));
//					button = new Button();
//					button.setBorder(null);
//					button.setBackground(null);
//					button.setGraphic(imageView);
//					GaussianBlur blur = new GaussianBlur(20.0);
//					imageView.setEffect(blur);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				WebView view = new WebView();
//				view.getEngine().load("http://www.baidu.com");
				StackPane stackPane = new StackPane();
				Scene scene = new Scene(stackPane,400, 400);
				stackPane.getChildren().add(imageView);
				
				jfxPanel.setScene(scene);
				jfxPanel.setBounds(0, 0, 400, 400);
			
		}});
		
		
		frame.setLayout(new FlowLayout());
//		frame.getContentPane().add(jfxPanel);
//		Mp3File mp3file = new Mp3File("D:/乱/music/Daniel Powter - Bad Day.mp3");
//		if (mp3file.hasId3v2Tag()) {
//		  ID3v2 id3v2Tag = mp3file.getId3v2Tag();
//		  byte[] imageData = id3v2Tag.getAlbumImage();
//		  if(imageData!=null)
//			  frame.getContentPane().add(new JLabel(new ImageIcon(imageData)));
//		}
//		frame.getContentPane().add(new JLabel(new ImageIcon(Res.Mipmap(Res.Nav_Item_Btn_Default))));
//		frame.getContentPane().add(new JLabel(new ImageIcon("C:/Users/李佳骏/Desktop/1.gif")));
		frame.setVisible(true);

	}

}
