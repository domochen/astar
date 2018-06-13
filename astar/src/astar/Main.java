package astar;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Main {

	// 起始点、终点的坐标
	final static int START_Y = 7;
	final static int START_X = 0;
	final static int GOAL_Y = 7;
	final static int GOAL_X = 7;

	final static int[][] DIR = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }; // 上下左右

	public static void main(String[] args) throws IOException {

		long start_time = System.currentTimeMillis();

		char[][] map = inite("map.txt"); // 初始化字符地图
		Point[][] points = initePoints(map.length, map.length); // 初始化Point二维表
		display(map);
		Queue<Point> openSet = new PriorityQueue<>(); // 开启列表，使用优先队列表达
		Set<Point> closeSet = new HashSet<>(); // 关闭列表

		Point start = points[START_Y][START_X]; // 获得开始点和目标点
		Point goal = points[GOAL_Y][GOAL_X];

		openSet.offer(start); // 将起始点加入开启列表

		while (!openSet.isEmpty()) {
			Point current = openSet.remove(); // 将开启列表中具有最小的f的点移入关闭列表内
			closeSet.add(current);

			int y = current.getY();
			int x = current.getX();

			for (int i = 0; i < DIR.length; i++) {
				int y_t = y + DIR[i][0]; // 获得上下左右的点的坐标
				int x_t = x + DIR[i][1];

				if (y_t < 0 || y_t >= map.length || x_t < 0 || x_t >= map.length) // 判断是否越界
					continue;

				Point temp = points[y_t][x_t];

				if (closeSet.contains(temp)) // 如果该点已经存在于关闭列表内，跳过
					continue;
				else if (isObstacle(map[y_t][x_t])) // 如果该点不可越过，跳过
					continue;
				else if (openSet.contains(temp)) { // 如果该点已在开启列表内，看是否更新值
					if (current.getG_score() <= temp.getG_score()) {
						temp.setG_scoreAndF_score(current.getG_score() + 1, distance(temp, goal));
						temp.setFather(current);
					}
				}
				else { // 如果没在开启列表内，加入开启列表
					temp.setG_scoreAndF_score(current.getG_score() + 1, distance(temp, goal));
					temp.setFather(current);
					openSet.offer(temp);
				}
			}

			if (openSet.contains(goal))
				break;
		}

		if (openSet.isEmpty()) {
			System.out.println("没有路径");
			return;
		}

		initePath(map, goal);

		System.out.println();
		System.out.println();
		System.out.println();

		display(map);

		System.out.println(System.currentTimeMillis() - start_time);
	}

	// 读文件，初始化地图数组，并返回
	public static char[][] inite(String fileName) throws IOException {
		char[][] map;
		try (Scanner input = new Scanner(new File(fileName))) {
			int height = input.nextInt();
			int width = input.nextInt();
			map = new char[height][width];
			input.nextLine();
			for (int i = 0; i < map.length; i++) {
				String temp = input.nextLine();
				for (int j = 0; j < map[i].length; j++)
					map[i][j] = temp.charAt(j);
			}
		}
		return map;
	}

	// 打印地图
	public static void display(char[][] map) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++)
				System.out.print(map[i][j]);
			System.out.println();
		}
	}

	public static Point[][] initePoints(int height, int width) {
		Point[][] points = new Point[height][width];
		for (int y = 0; y < points.length; y++)
			for (int x = 0; x < points[y].length; x++)
				points[y][x] = new Point(y, x);
		return points;
	}

	// 判断是否是障碍
	public static boolean isObstacle(char c) {
		return c == '#' || c == '@';
	}

	public static int distance(Point p1, Point p2) {
		return distancce(p1.getY(), p1.getX(), p2.getY(), p2.getX());
	}

	// 返回街区距离
	public static int distancce(int y_t, int x_t, int y_g, int x_g) {
		return Math.abs(y_t - y_g) + Math.abs(x_t - x_g);
	}

	// 生成路径
	public static void initePath(char[][] map, Point goal) {
		Point current = goal.getFather();
		while (current.getFather() != null) {
			map[current.getY()][current.getX()] = ' ';
			current = current.getFather();
		}
	}
}
