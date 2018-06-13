package astar;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Main {

	// ��ʼ�㡢�յ������
	final static int START_Y = 7;
	final static int START_X = 0;
	final static int GOAL_Y = 7;
	final static int GOAL_X = 7;

	final static int[][] DIR = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }; // ��������

	public static void main(String[] args) throws IOException {

		long start_time = System.currentTimeMillis();

		char[][] map = inite("map.txt"); // ��ʼ���ַ���ͼ
		Point[][] points = initePoints(map.length, map.length); // ��ʼ��Point��ά��
		display(map);
		Queue<Point> openSet = new PriorityQueue<>(); // �����б�ʹ�����ȶ��б��
		Set<Point> closeSet = new HashSet<>(); // �ر��б�

		Point start = points[START_Y][START_X]; // ��ÿ�ʼ���Ŀ���
		Point goal = points[GOAL_Y][GOAL_X];

		openSet.offer(start); // ����ʼ����뿪���б�

		while (!openSet.isEmpty()) {
			Point current = openSet.remove(); // �������б��о�����С��f�ĵ�����ر��б���
			closeSet.add(current);

			int y = current.getY();
			int x = current.getX();

			for (int i = 0; i < DIR.length; i++) {
				int y_t = y + DIR[i][0]; // ����������ҵĵ������
				int x_t = x + DIR[i][1];

				if (y_t < 0 || y_t >= map.length || x_t < 0 || x_t >= map.length) // �ж��Ƿ�Խ��
					continue;

				Point temp = points[y_t][x_t];

				if (closeSet.contains(temp)) // ����õ��Ѿ������ڹر��б��ڣ�����
					continue;
				else if (isObstacle(map[y_t][x_t])) // ����õ㲻��Խ��������
					continue;
				else if (openSet.contains(temp)) { // ����õ����ڿ����б��ڣ����Ƿ����ֵ
					if (current.getG_score() <= temp.getG_score()) {
						temp.setG_scoreAndF_score(current.getG_score() + 1, distance(temp, goal));
						temp.setFather(current);
					}
				}
				else { // ���û�ڿ����б��ڣ����뿪���б�
					temp.setG_scoreAndF_score(current.getG_score() + 1, distance(temp, goal));
					temp.setFather(current);
					openSet.offer(temp);
				}
			}

			if (openSet.contains(goal))
				break;
		}

		if (openSet.isEmpty()) {
			System.out.println("û��·��");
			return;
		}

		initePath(map, goal);

		System.out.println();
		System.out.println();
		System.out.println();

		display(map);

		System.out.println(System.currentTimeMillis() - start_time);
	}

	// ���ļ�����ʼ����ͼ���飬������
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

	// ��ӡ��ͼ
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

	// �ж��Ƿ����ϰ�
	public static boolean isObstacle(char c) {
		return c == '#' || c == '@';
	}

	public static int distance(Point p1, Point p2) {
		return distancce(p1.getY(), p1.getX(), p2.getY(), p2.getX());
	}

	// ���ؽ�������
	public static int distancce(int y_t, int x_t, int y_g, int x_g) {
		return Math.abs(y_t - y_g) + Math.abs(x_t - x_g);
	}

	// ����·��
	public static void initePath(char[][] map, Point goal) {
		Point current = goal.getFather();
		while (current.getFather() != null) {
			map[current.getY()][current.getX()] = ' ';
			current = current.getFather();
		}
	}
}
