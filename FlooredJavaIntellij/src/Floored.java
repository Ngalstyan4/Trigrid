import processing.core.PApplet;
import processing.core.PVector;

import java.util.HashMap;


/**
 * Created by ngalstyan on 11/22/16.
 */
public class Floored extends PApplet {

    private final int[] colors = {color(255), color(217, 0, 0), color(0, 51, 160), color(239, 107, 0)};
    private float lineWidth = 30;
    private float lineHeight = (float) (2 * lineWidth / Math.sqrt(3));
    private float side = lineHeight;
    private HashMap<PVector, Integer> state;
    private boolean RIGHT = false, LEFT = true;

    public static void main(String... args) {
        PApplet.main("Floored");
    }

    public void settings() {
        size(400, 400);
    }

    public void setup() {
        //size(400, 400);
        state = new HashMap<PVector, Integer>();
        //fullScreen();
        background(255);
        stroke(200);

        for (int i = 0; i < width; i += lineWidth) {
            for (float j = i / lineWidth % 2 == 0 ? 0 : side / 2;
                 j < height + lineHeight;
                 j += lineHeight) {

                Triangle rightTriangle = createTriangle(i, j, RIGHT);
                Triangle leftTriangle = createTriangle(i, j, LEFT);
                rightTriangle.initDrawTriangle();
                leftTriangle.initDrawTriangle();
                state.put(rightTriangle.getCenter(), 0);
                state.put(leftTriangle.getCenter(), 0);

            }
        }

    }

    public void draw() {
        for (int i = 0; i < width; i += lineWidth) {

            for (float j = i / lineWidth % 2 == 0 ? 0 : side / 2;
                 j < height + lineHeight;
                 j += lineHeight) {

                Triangle leftTriangle = createTriangle(i, j, LEFT);
                Triangle rightTriangle = createTriangle(i, j, RIGHT);

                // left
                PVector triangleCenter = leftTriangle.getCenter();
                int currentState = state.get(triangleCenter);
                int triangleColor = currentState % colors.length;
                fill(colors[triangleColor]);
                leftTriangle.drawTriangle(false);

                //right
                triangleCenter = rightTriangle.getCenter();
                currentState = state.get(triangleCenter);
                triangleColor = currentState % colors.length;
                fill(colors[triangleColor]);
                rightTriangle.drawTriangle(false);
            }
        }
    }

    public void mousePressed() {
        for (int i = 0; i < width; i += lineWidth) {
            // I initially thought this might be need but then realised that the
            // three sides of a triangle suffice to draw every needed line
            // line(i, 0, i, height);

            for (float j = i / lineWidth % 2 == 0 ? 0 : side / 2; j < height; j += lineHeight) {
                Triangle leftTriangle = createTriangle(i, j, LEFT);
                Triangle rightTriangle = createTriangle(i, j, RIGHT);

                if (checkCollision(mouseX, mouseY, leftTriangle)) {
                    leftTriangle.drawTriangle();
                } else if (checkCollision(mouseX, mouseY, rightTriangle)) {
                    rightTriangle.drawTriangle();
                }
            }
        }
    }


    // HELPER FUNCTIONS //

    private boolean checkCollision(float x, float y, Triangle t) {
        float tArea, t1Area, t2Area, t3Area;
        float err = 0.01f;
        tArea = triangleArea(t.point1x, t.point1y, t.point3x, t.point3y, t.point2x, t.point2y);
        t1Area = triangleArea(x, y, t.point2x, t.point2y, t.point3x, t.point3y);
        t2Area = triangleArea(x, y, t.point3x, t.point3y, t.point1x, t.point1y);
        t3Area = triangleArea(x, y, t.point2x, t.point2y, t.point1x, t.point1y);
//######## some debugging code
//        noStroke();
//        fill(#ff6600,50);
//        triangle(t.point1x, t.point1y, t.point3x, t.point3y, t.point2x, t.point2y);
//        fill(#ffff00,50);
//        triangle(x,y, t.point2x, t.point2y, t.point3x, t.point3y);
//        fill(#00ffff,50);
//        triangle(x,y, t.point3x, t.point3y, t.point1x, t.point1y);
//        fill(#ff0000,50);
//        triangle(x,y, t.point2x, t.point2y, t.point1x, t.point1y);

        float totalArea = t1Area + t2Area + t3Area;
        if (abs(totalArea - tArea) < err) println(totalArea == tArea);
        return (abs(totalArea - tArea) < err);
    }

    private float triangleArea(float p1, float p2, float p3, float p4, float p5, float p6) {
        float a, b, c, d;
        a = p1 - p5;
        b = p2 - p6;
        c = p3 - p5;
        d = p4 - p6;
        return (0.5f * abs((a * d) - (b * c)));
    }

    private Triangle createTriangle(float i, float j, boolean left) {
        if (left)
            return new Triangle(i, j, i, j + side, i + lineWidth, j + side / 2);
        else
            return new Triangle(i, j, i + lineWidth, j - side / 2, i + lineWidth, j + side / 2);

    }

    private class Triangle {
        // According to encapsulation rules, these must be private,
        // but it will not change anything at this point as this is an inner class
        float point1x;
        float point1y;
        float point2x;
        float point2y;
        float point3x;
        float point3y;

        Triangle(float point1x, float point1y,
                 float point2x, float point2y,
                 float point3x, float point3y) {
            this.point1x = point1x;
            this.point1y = point1y;
            this.point2x = point2x;
            this.point2y = point2y;
            this.point3x = point3x;
            this.point3y = point3y;
        }
        // not allowed this way!
//        Triangle(float i, float j, boolean left) {
//            if (left) this(i, j, i + lineWidth, j - side / 2, i + lineWidth, j + side / 2);
//        }

        PVector getCenter() {
            float centerX = this.point1x + this.point2x + this.point3x;
            centerX /= 3;
            float centerY = this.point1y + this.point2y + this.point3y;
            centerY /= 3;
            return new PVector(centerX, centerY);
        }

        void initDrawTriangle() {
            triangle(point1x, point1y, point2x, point2y, point3x, point3y);

        }

        void drawTriangle() {
        drawTriangle(true);
        }

        void drawTriangle(boolean modifyState) {
            PVector triangleCenter = this.getCenter();
            int currentState = state.get(triangleCenter);
            if (modifyState) {
                state.put(triangleCenter, ++currentState);
            }

            stroke(200);
            int triangleColor = (currentState) % colors.length;
            if (triangleColor != 0) {
                noStroke();
            }
            fill(colors[triangleColor]);
            triangle(point1x, point1y, point2x, point2y, point3x, point3y);
            noFill();
        }

    }
}
