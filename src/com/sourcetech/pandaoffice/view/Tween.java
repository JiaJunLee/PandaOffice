package com.sourcetech.pandaoffice.view;

import java.awt.image.BufferedImage;

import javax.xml.namespace.QName;

/**
 * 缓动函数
 * 
 * @param t
 *            当前执行次数 current time
 * @param b
 *            初始值 beginning value
 * @param c
 *            变化量 change in value
 * @param d
 *            持续时间 duration
 * @author 李佳骏
 * 
 */
public class Tween {
	
	public static final int Function_Quadratic = 0x0;
	public static final int Function_Cubic = 0x1;
	public static final int Function_Quartic = 0x2;
	public static final int Function_Quintic = 0x3;
	public static final int Function_Sinusoidal = 0x4;
	public static final int Function_Exponential = 0x5;
	public static final int Function_Circular = 0x6;
	
	public static final int Type_Easy_In = 0x0;
	public static final int Type_Easy_Out = 0x1;
	public static final int Type_Easy_In_Out = 0x2;

	public static double get(int function, int type, double t, double b, double c, double d){
		switch (function) {
		
		case Function_Quadratic:
			switch (type) {
			case Type_Easy_In:
				return Quadratic.easyIn(t, b, c, d);
			case Type_Easy_Out:
				return Quadratic.easyOut(t, b, c, d);
			case Type_Easy_In_Out:
				return Quadratic.easyInOut(t, b, c, d);
			}
			break;
			
		case Function_Cubic:
			switch (type) {
			case Type_Easy_In:
				return Cubic.easyIn(t, b, c, d);
			case Type_Easy_Out:
				return Cubic.easyOut(t, b, c, d);
			case Type_Easy_In_Out:
				return Cubic.easyInOut(t, b, c, d);
			}
			break;
			
		case Function_Quartic:
			switch (type) {
			case Type_Easy_In:
				return Quartic.easyIn(t, b, c, d);
			case Type_Easy_Out:
				return Quartic.easyOut(t, b, c, d);
			case Type_Easy_In_Out:
				return Quartic.easyInOut(t, b, c, d);
			}
			break;
			
		case Function_Quintic:
			switch (type) {
			case Type_Easy_In:
				return Quintic.easyIn(t, b, c, d);
			case Type_Easy_Out:
				return Quintic.easyOut(t, b, c, d);
			case Type_Easy_In_Out:
				return Quintic.easyInOut(t, b, c, d);
			}
			break;
			
		case Function_Sinusoidal:
			switch (type) {
			case Type_Easy_In:
				return Sinusoidal.easyIn(t, b, c, d);
			case Type_Easy_Out:
				return Sinusoidal.easyOut(t, b, c, d);
			case Type_Easy_In_Out:
				return Sinusoidal.easyInOut(t, b, c, d);
			}
			break;
			
		case Function_Exponential:
			switch (type) {
			case Type_Easy_In:
				return Exponential.easyIn(t, b, c, d);
			case Type_Easy_Out:
				return Exponential.easyOut(t, b, c, d);
			case Type_Easy_In_Out:
				return Exponential.easyInOut(t, b, c, d);
			}
			break;
			
		case Function_Circular:
			switch (type) {
			case Type_Easy_In:
				return Circular.easyIn(t, b, c, d);
			case Type_Easy_Out:
				return Circular.easyOut(t, b, c, d);
			case Type_Easy_In_Out:
				return Circular.easyInOut(t, b, c, d);
			}
			break;
		}
		return 0;
	} 

	/**
	 * 无缓动效果
	 */
	public static double Linear(double t, double b, double c, double d) {
		return c * t / d + b;
	}

	/**
	 * 二次方的缓动
	 */
	public static class Quadratic {

		public static double easyIn(double t, double b, double c, double d) {
			return c * (t /= d) * t + b;
		}

		public static double easyOut(double t, double b, double c, double d) {
			return -c * (t /= d) * (t - 2) + b;
		}

		public static double easyInOut(double t, double b, double c, double d) {
			if ((t /= d / 2) < 1)
				return c / 2 * t * t + b;
			return -c / 2 * ((--t) * (t - 2) - 1) + b;
		}

	}

	/**
	 * 三次方的缓动
	 */
	public static class Cubic {

		public static double easyIn(double t, double b, double c, double d) {
			return c * (t /= d) * t * t + b;
		}

		public static double easyOut(double t, double b, double c, double d) {
			return c * ((t = t / d - 1) * t * t + 1) + b;
		}

		public static double easyInOut(double t, double b, double c, double d) {
			if ((t /= d / 2) < 1)
				return c / 2 * t * t * t + b;
			return c / 2 * ((t -= 2) * t * t + 2) + b;
		}

	}

	/**
	 * 四次方的缓动
	 */
	public static class Quartic {

		public static double easyIn(double t, double b, double c, double d) {
			return c * (t /= d) * t * t * t + b;
		}

		public static double easyOut(double t, double b, double c, double d) {
			return -c * ((t = t / d - 1) * t * t * t - 1) + b;
		}

		public static double easyInOut(double t, double b, double c, double d) {
			if ((t /= d / 2) < 1)
				return c / 2 * t * t * t * t + b;
			return -c / 2 * ((t -= 2) * t * t * t - 2) + b;
		}

	}

	/**
	 * 五次方的缓动
	 */
	public static class Quintic {

		public static double easyIn(double t, double b, double c, double d) {
			return c * (t /= d) * t * t * t * t + b;
		}

		public static double easyOut(double t, double b, double c, double d) {
			return c * ((t = t / d - 1) * t * t * t * t + 1) + b;
		}

		public static double easyInOut(double t, double b, double c, double d) {
			if ((t /= d / 2) < 1)
				return c / 2 * t * t * t * t * t + b;
			return c / 2 * ((t -= 2) * t * t * t * t + 2) + b;
		}

	}

	/**
	 * 正弦曲线的缓动
	 */
	public static class Sinusoidal {

		public static double easyIn(double t, double b, double c, double d) {
			return -c * Math.cos(t / d * (Math.PI / 2)) + c + b;
		}

		public static double easyOut(double t, double b, double c, double d) {
			return c * Math.sin(t / d * (Math.PI / 2)) + b;
		}

		public static double easyInOut(double t, double b, double c, double d) {
			return -c / 2 * (Math.cos(Math.PI * t / d) - 1) + b;
		}

	}

	/**
	 * 指数曲线的缓动
	 */
	public static class Exponential {

		public static double easyIn(double t, double b, double c, double d) {
			return (t == 0) ? b : c * Math.pow(2, 10 * (t / d - 1)) + b;
		}

		public static double easyOut(double t, double b, double c, double d) {
			return (t == d) ? b + c : c * (-Math.pow(2, -10 * t / d) + 1) + b;
		}

		public static double easyInOut(double t, double b, double c, double d) {
			if (t == 0)
				return b;
			if (t == d)
				return b + c;
			if ((t /= d / 2) < 1)
				return c / 2 * Math.pow(2, 10 * (t - 1)) + b;
			return c / 2 * (-Math.pow(2, -10 * --t) + 2) + b;
		}

	}

	/**
	 * 圆形曲线的缓动
	 */
	public static class Circular {

		public static double easyIn(double t, double b, double c, double d) {
			return -c * (Math.sqrt(1 - (t /= d) * t) - 1) + b;
		}

		public static double easyOut(double t, double b, double c, double d) {
			return c * Math.sqrt(1 - (t = t / d - 1) * t) + b;
		}

		public static double easyInOut(double t, double b, double c, double d) {
			if ((t /= d / 2) < 1)
				return -c / 2 * (Math.sqrt(1 - t * t) - 1) + b;
			return c / 2 * (Math.sqrt(1 - (t -= 2) * t) + 1) + b;
		}

	}

}
