package avis.starfield;

import avis.juikit.Juikit;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static final Random RANDOM = new Random();
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private static final Point CENTER = new Point(WIDTH / 2d, HEIGHT / 2d);
    private static final double MAX_DISTANCE = Math.sqrt(CENTER.x * CENTER.x + CENTER.y * CENTER.y);

    public static class Point {

        double x;
        double y;

        double beforeX;
        double beforeY;

        double size = 1;
        double speed = 10;

        public Point() {
        }

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public boolean isOutside() {
            return x < 0 || x > WIDTH || y < 0 || y > HEIGHT;
        }

        public void resetPosition() {
            x = RANDOM.nextInt(WIDTH);
            y = RANDOM.nextInt(HEIGHT);
            beforeX = x;
            beforeY = y;
            speed = (int) (MAX_DISTANCE / 500 + 5 - distance(CENTER) / 500);
            size = 1;
        }

        public double distance(Point point) {
            double diffX = point.x - x;
            double diffY = point.y - y;
            return Math.sqrt(diffX * diffX + diffY * diffY);
        }

        public double length() {
            return Math.sqrt(x * x + y * y);
        }

        public Point subtract(Point point) {
            double x = this.x - point.x;
            double y = this.y - point.y;

            return new Point(x, y);
        }

        public Point normalize() {
            return new Point(x / length(), y / length());
        }

    }

    private static final List<Point> POINTS = new ArrayList<>();

    public static void main(String args[]) {
        for(int i = 0; i < 1000; i++) {
            Point point = new Point();
            point.resetPosition();
            if(point.speed != 0) {
                POINTS.add(point);
            }
        }

        Juikit.createFrame()
                .size(WIDTH, HEIGHT)
                .centerAlign()
                .resizable(false)
                .visibility(true)
                .background(Color.BLACK)
                .antialiasing(true)
                .repaintInterval(10L)
                .painter((juikit, graphics) -> {
                    for(Point point : POINTS) {
                        point.size += 0.03;

                        Point trajectory = point.subtract(CENTER).normalize();

                        point.speed *= 1.01;

                        point.beforeX = point.x;
                        point.beforeY = point.y;

                        point.x += trajectory.x * point.speed / 10;
                        point.y += trajectory.y * point.speed / 10;

                        graphics.setColor(Color.GRAY);
                        graphics.fillOval((int) point.beforeX, (int) point.beforeY, (int) Math.max(1, point.size - 2), (int) Math.max(1, point.size - 2));
                        graphics.setColor(Color.WHITE);
                        graphics.fillOval((int) point.x, (int) point.y, (int) point.size, (int) point.size);
                    }

                    POINTS.stream().filter(Point::isOutside).forEach(Point::resetPosition);
                });
    }

}
