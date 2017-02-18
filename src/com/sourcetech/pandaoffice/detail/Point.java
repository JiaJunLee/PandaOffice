package com.sourcetech.pandaoffice.detail;

public class Point {

	private float point_X;
	private float point_Y;

	public Point() {
		this.point_X = 0f;
		this.point_Y = 0f;
	}

	public Point(float x, float y) {
		this.point_X = x;
		this.point_Y = y;
	}

	public float getPoint_X() {
		return point_X;
	}

	public void setPoint_X(float point_X) {
		this.point_X = point_X;
	}

	public float getPoint_Y() {
		return point_Y;
	}

	public void setPoint_Y(float point_Y) {
		this.point_Y = point_Y;
	}

}
