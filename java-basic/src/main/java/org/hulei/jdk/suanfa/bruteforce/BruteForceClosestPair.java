package org.hulei.jdk.suanfa.bruteforce;

public class BruteForceClosestPair {
    static class Point {
        double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    public static double bruteForceClosestPair(Point[] points) {
        int n = points.length;
        double minDistance = Double.MAX_VALUE;

        // 遍历所有点对，计算它们之间的距离，找到最小距离
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = distance(points[i], points[j]);
                if (dist < minDistance) {
                    minDistance = dist;
                }
            }
        }

        return minDistance;
    }

    public static void main(String[] args) {
        Point[] points = { new Point(1, 2), new Point(3, 4), new Point(5, 6), new Point(7, 8) };
        double minDistance = bruteForceClosestPair(points);
        System.out.println("最近距离的两个点之间的距离为: " + minDistance);
    }
}
