package astar;

public class Point implements Comparable<Point> {
	private int y;
	private int x;
	private int g_score = 1000;
	private int f_score = 1000; // f值默认无穷大
	private Point father = null; // 父节点

	public Point(int y, int x) {
		super();
		this.x = x;
		this.y = y;
	}

	// 设置g和f的值
	public void setG_scoreAndF_score(int g_score, int distance) {
		this.g_score = g_score;
		this.f_score = g_score + distance;
	}

	public int getG_score() {
		return g_score;
	}

	public int getF_score() {
		return f_score;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setFather(Point father) {
		this.father = father;
	}

	public Point getFather() {
		return father;
	}

	@Override
	public boolean equals(Object obj) {
		return ((Point) obj).x == x && ((Point) obj).y == y;
	}

	@Override
	public int compareTo(Point o) {
		if (f_score > o.f_score)
			return 1;
		else if (f_score == o.f_score)
			return 0;
		else
			return -1;
	}
}
