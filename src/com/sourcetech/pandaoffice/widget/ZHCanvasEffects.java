package com.sourcetech.pandaoffice.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sourcetech.pandaoffice.detail.Point;

/**
 * 仿知乎动画背景
 * 
 * @author 李佳骏
 *
 */
public class ZHCanvasEffects extends JPanel {
	
	class Circle{
		private Point leftPoint = null;
		private int radius = 0;
		private int speedX = 0;
		private int speedY = 0;
		private int orientation = 0;
		public Circle(Point leftPoint, int radius){
			this.leftPoint = leftPoint;
			this.radius = radius;
		}
		public Point getLeftPoint() {
			return leftPoint;
		}
		public void setLeftPoint(Point leftPoint) {
			this.leftPoint = leftPoint;
		}
		public int getRadius() {
			return radius;
		}
		public void setRadius(int radius) {
			this.radius = radius;
		}
		public Point getCenterPoint(){
			Ellipse2D circle2D = new Ellipse2D.Float(getLeftPoint().getPoint_X(), getLeftPoint().getPoint_Y(), getRadius(), getRadius());
			return new Point((float)circle2D.getCenterX(), (float)circle2D.getCenterY());
		}
		
		public void setSpeed(int x, int y){
			this.speedX = x;
			this.speedY = y;
		}
		
		public void setOrientation(int orientation){
			this.orientation = orientation;
		}
	}
	
	/**
	 * 点的个数
	 */
	public int DEFAULT_POINT = 15;
	
	/**
	 * 绘制颜色
	 */
	public static final Color color = Color.BLACK;
	
	/**
	 * 用于存储点的链表
	 */
	private ArrayList<Circle> circles = new ArrayList<>();
	
	/**
	 * 点的最小半径和最大半径
	 * 
	 */
	private int pointMinSize = 20;
	private int pointMaxSize = 35;
	
	/**
	 * 点和线绘制的透明度
	 */
	private float pointAlpha = 0.03f;
	private float lineAlpha = 0.015f;
	
	/**
	 * 速度的最大值和最小值
	 */
	private int minSpeed = 1;
	private int maxSpeed = 2;
	
	/**
	 * 两个点距离小于minLineLength则绘制直线
	 */
	private int minLineLength = 240;
	
	
	public static Color getAlphaColor(Color c, float alpha){
		Color color = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
		return color;
	}
	
	public static int getRandom(int min, int max){
		return (int)(min + Math.random()*(max-min+1));
	}

	private int panelWidth = 0;
	private int panelHeight = 0;
	
	public ZHCanvasEffects(Rectangle rectangle) {
		
		// 初始化Panel信息
		panelWidth = rectangle.width;
		panelHeight = rectangle.height;
		
		// 设置边界信息
		setBounds(rectangle.x, rectangle.y, panelWidth, panelHeight);
		
		// 初始化点
		for(int i=0;i<DEFAULT_POINT;i++){
			int radius = 0;
			Point leftPoint = null;
			do{
				// 从取值范围中随机生成点的半径
				radius = getRandom(pointMinSize, pointMaxSize);
				leftPoint = new Point(getRandom(0+radius, panelWidth-radius), getRandom(0+radius, panelHeight-radius));
			}while(isErrorPoint(leftPoint, radius));
			// 如果所有点都没有重合就添加一个新生成的点
			circles.add(new Circle(leftPoint, radius));
		}
		
		// 从取值范围中为每个点生成自己的X和Y轴移动速度以及移动方向
		for(int i=0;i<circles.size();i++){
			Circle circle = circles.get(i);
			circle.setSpeed(getRandom(minSpeed, maxSpeed), getRandom(minSpeed, maxSpeed));
			circle.setOrientation(getRandom(0, 1));
		}
	}
	
	/**
	 * 获取两个点之间直线的距离
	 * @param point1
	 * @param point2
	 * @return
	 */
	private int getPointLength(Point point1, Point point2){
		double t1 = Math.pow((point1.getPoint_X()-point2.getPoint_X()), 2);
		double t2 = Math.pow((point1.getPoint_Y()-point2.getPoint_Y()), 2);
		return (int)Math.sqrt(t1+t2);
	}
	
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D graphics2d = (Graphics2D) g;
		// 去锯齿
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
		
		for(int i=0;i<circles.size();i++){
			Circle circle = circles.get(i);
			graphics2d.setColor(getAlphaColor(color, pointAlpha));
			graphics2d.fillOval((int)circle.getLeftPoint().getPoint_X(), (int)circle.getLeftPoint().getPoint_Y(), circle.getRadius(), circle.getRadius());
			for(int j=0;j<circles.size();j++){
				if(j!=i){
					Circle c = circles.get(j);
					if(getPointLength(circle.getCenterPoint(), c.getCenterPoint())<minLineLength){
						graphics2d.setColor(getAlphaColor(color, lineAlpha));
						graphics2d.drawLine((int)circle.getCenterPoint().getPoint_X(), (int)circle.getCenterPoint().getPoint_Y(), (int)c.getCenterPoint().getPoint_X(), (int)c.getCenterPoint().getPoint_Y());
					}
				}
			}
		}
	}
	
	/**
	 * 更新点
	 */
	public void update(){
		
		for(int i=0;i<circles.size();i++){
			Circle circle = circles.get(i);
			Point leftPoint = circle.getLeftPoint();
			switch(circle.orientation){
			case 0:
				leftPoint.setPoint_X(leftPoint.getPoint_X()+circle.speedX);
				leftPoint.setPoint_Y(leftPoint.getPoint_Y()+circle.speedY);
				break;
			case 1:
				leftPoint.setPoint_X(leftPoint.getPoint_X()-circle.speedX);
				leftPoint.setPoint_Y(leftPoint.getPoint_Y()-circle.speedY);
				break;
			}
			circle.setLeftPoint(leftPoint);
			boolean isVisible = new Ellipse2D.Float(circle.getLeftPoint().getPoint_X(),circle.getLeftPoint().getPoint_Y(),circle.getRadius(),circle.getRadius()).getBounds().intersects(getBounds());
			if(!isVisible){
				int radius = 0;
				Point newLeftPoint = null;
				do{
					radius = getRandom(pointMinSize, pointMaxSize);
					newLeftPoint = new Point(getRandom(0+radius, panelWidth-radius), getRandom(0+radius, panelHeight-radius));
				}while(isErrorPoint(newLeftPoint, radius));
				circle.setLeftPoint(newLeftPoint);
				circle.setSpeed(getRandom(minSpeed, maxSpeed), getRandom(minSpeed, maxSpeed));
				circle.setOrientation(getRandom(0, 1));
			}
		}
		// 刷新动画
		repaint();
	}
	
	
	/**
	 * 判断是否是正确生成的点，即两个点没有重叠
	 * @param leftPoint
	 * @param radius
	 * @return 重叠返回true  不重叠返回false
	 */
	private boolean isErrorPoint(Point leftPoint, int radius){
		for(int i=0;i<circles.size();i++){
			Circle circle = circles.get(i);
			Ellipse2D circle2D = new Ellipse2D.Float(circle.getLeftPoint().getPoint_X(), circle.getLeftPoint().getPoint_Y(), circle.getRadius(), circle.getRadius());
			Ellipse2D newCircle2D = new Ellipse2D.Float(leftPoint.getPoint_X(), leftPoint.getPoint_Y(), radius, radius);
			if(circle2D.getBounds().intersects(newCircle2D.getBounds()))
					return true;
		}
		return false;
	}
	

}


