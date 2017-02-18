package com.sourcetech.pandaoffice.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sourcetech.pandaoffice.frame.STPanel;

public class ViewPager extends JPanel{
	
	public static final Rectangle DEFAULT_BOUNDS = new Rectangle(0,0,0,0);
	public static final int DEFAULT_SPEED = 100;
	
	public static interface OnPageChangeListener{
		/**
		 * 当页面滑动状态改变
		 * @param arg0 状态代码
		 */
		public void onPageScrollStateChanged(int arg0);
		/**
		 * 当页面改变调用
		 * @param arg0 当前页面
		 */
		public void onPageSelected(int arg0);
	}

	public static class Builder{
		
		private ArrayList<STPanel> components = new ArrayList<STPanel>();
		private Rectangle bounds = DEFAULT_BOUNDS;
		private int count = 0;
		private int speed = DEFAULT_SPEED;
		private int tweenFunction = Tween.Function_Quartic;
		private int tweenType = Tween.Type_Easy_In;
		
		private Builder(){}
		
		public Builder setBounds(Rectangle bounds){
			this.bounds = bounds;
			return this;
		}
		
		public Builder setPageCount(int count){
			for(int i=0;i<count;i++){
				STPanel panel = new STPanel(true);
				panel.setBounds(new Rectangle(0, 0, bounds.width, bounds.height));
				components.add(panel);
			}
			return this;
		}
		
		public Builder setSpeed(int speed){
			this.speed = speed;
			return this;
		}
		
		public Builder setTween(int tweenFunction, int tweenType){
			this.tweenFunction = tweenFunction;
			this.tweenType = tweenType;
			return this;
		}
		
		public ViewPager build(){
			return new ViewPager(components, bounds, speed, tweenFunction, tweenType);
		}
		
	}
	
	public static Builder newBuilder(){
		return new Builder();
	}
	
	// 变量
	
	private ArrayList<STPanel> components;
	private Rectangle bounds;
	private int speed;
	private int currentPage;
	private int tweenFunction;
	private int tweenType;
	
	private ViewPager(ArrayList<STPanel> components, Rectangle bounds, int speed, int tweenFunction, int tweenType) {
		
		this.components = components;
		this.bounds = bounds;
		this.speed = speed;
		this.currentPage = -1;
		this.tweenFunction = tweenFunction;
		this.tweenType = tweenType;
		
		setLayout(null);
		setBackground(null);
		setBounds(bounds);
		
		if(components.size()>0)
			setSelectedPage(0);

	}
	
	public ArrayList<STPanel> getPanes(){
		return components;
	}
	
	public void setSelectedPage(int page){
		if(page<0){
			currentPage = -1;
			setVisible(false);
			return;
		}
		if(page!=-1 && currentPage!=page){
			setVisible(true);
			// 添加选择的页面
			currentPage = page;
			JComponent component = components.get(page);
			add(component, 0);
			// 滑动动画线程
			if(getComponents().length>1){
				final JComponent oldComponent = (JComponent) getComponent(1);
				AnimationThread animationThread = new AnimationThread(component, oldComponent, bounds.width, 0, speed, tweenFunction, tweenType);
				animationThread.addListener(new ThreadListener() {
					public void onStop() {
						remove(1);
						oldComponent.setBounds(bounds);
					}
					public void onStart() {}
				});
				animationThread.start();
			}
		}
	}
	
	/**
	 * 演示
	 * @param args
	 */
	public static void main(String[] args) {
		
		final ViewPager vp = ViewPager.newBuilder()
								.setBounds(new Rectangle(0, 0, 500, 500))
							    .setPageCount(3)
							    .build();
		
		ArrayList<STPanel> panels = vp.getPanes();
		panels.get(0).setBackground(Color.RED);
		panels.get(1).setBackground(Color.GREEN);
		panels.get(2).setBackground(Color.BLUE);
		
		new Thread(){public void run() {
			try {
				while(true){
					sleep(2000);
					vp.setSelectedPage(0);
					sleep(2000);
					vp.setSelectedPage(1);
					sleep(2000);
					vp.setSelectedPage(2);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};}.start();
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(500, 500));
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(Color.white);
		frame.setLayout(null);
		
		frame.getContentPane().add(vp);
		frame.setVisible(true);
	}
	
	public static interface ThreadListener{
		public void onStart();
		public void onStop();
	}
	
	public static class AnimationThread extends Thread{

		private JComponent component;
		private JComponent oldComponent;
		private int fromX;
		private int toX;
		private int speed;
		private int tweenFunction;
		private int tweenType;
		private ThreadListener listener;
		
		public AnimationThread(JComponent component, JComponent oldComponent, int fromX, int toX, int speed, int tweenFunction, int tweenType){
			this.component = component;
			this.oldComponent = oldComponent;
			this.fromX = fromX;
			this.toX = toX;
			this.speed = speed;
			this.tweenFunction = tweenFunction;
			this.tweenType = tweenType;
		}
		
		public void addListener(ThreadListener listener){
			this.listener = listener;
		}
		
		public void update(int currentX){
			int width = component.getWidth();
			int height = component.getHeight();
			
			component.setBounds(currentX, 0, width, height);
			oldComponent.setBounds((-(width-currentX)), 0, width, height);
			
			if(component.getRootPane()!=null)
				component.getRootPane().repaint();
		}
		
		public void run() {
			if(listener!=null)
				listener.onStart();
			try {
				int currentX;
				for(int t=0;t<=speed;t++){
					currentX = fromX - (int) Tween.get(tweenFunction, tweenType, t, toX, fromX, speed);
					update(currentX);
					System.out.println(currentX);
					sleep(1);
				}
			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(listener!=null)
				listener.onStop();
		}
		
	}

}
