import processing.core.PApplet;
import processing.core.PVector;

import java.util.HashMap;


/**
 * Created by ngalstyan on 11/22/16.
 */
public class Floored extends PApplet{

    final int[] colors = {color(255),color(255, 0, 0), color(0, 255, 0), color(0, 0, 255)};
    float lineWidth = 30;
    float lineHeight = (float) (2 * lineWidth / Math.sqrt(3));
    float side = lineHeight;
    HashMap<PVector,Integer> state;

    public void settings(){
        size(400, 400);
    }

    public void setup() {
        //size(400, 400);
        state = new HashMap<PVector,Integer>();
        //fullScreen();
        background(255);
        stroke(200);

        for (int i = 0; i < width; i += lineWidth) {
            //line(i, 0, i, height);

            for (float j = i / lineWidth % 2 == 0 ? 0 : side / 2; j < height + lineHeight; j += lineHeight) {
                triangle(i, j, i + lineWidth, j - side / 2, i + lineWidth, j + side / 2);
                triangle(i, j, i, j + side, i + lineWidth, j + side / 2);
                Triangle rightTriangle = new Triangle(i, j, i + lineWidth, j - side / 2, i + lineWidth, j + side / 2);
                Triangle leftTriangle =  new Triangle(i, j, i, j + side, i + lineWidth, j + side / 2);
                state.put(getTriangleCenter(rightTriangle),0);
                state.put(getTriangleCenter(leftTriangle),0);

            }
        }

    }

    public void draw() {
        //for (int i = 0; i < width; i += lineWidth) {
        //    //line(i, 0, i, height);

        //    for (float j = i / lineWidth % 2 == 0 ? 0 : side / 2; j < height + lineHeight; j += lineHeight) {
        //      Triangle  leftTriangle= new Triangle(i, j, i + lineWidth, j - side / 2, i + lineWidth, j + side / 2);
        //      Triangle rightTriangle =  new Triangle(i, j, i, j + side, i + lineWidth, j + side / 2);
        //      PVector triangleCenter = getTriangleCenter(leftTriangle);
        //      int currentState = state.get(triangleCenter);
        //      int triangleColor = (currentState) % colors.length;
        //      fill(colors[triangleColor]);
        //        triangle(i, j, i + lineWidth, j - side / 2, i + lineWidth, j + side / 2);
        //      triangleCenter = getTriangleCenter(rightTriangle);
        //      currentState = state.get(triangleCenter);
        //      triangleColor = (currentState) % colors.length;
        //      fill(colors[triangleColor]);
        //        triangle(i, j, i, j + side, i + lineWidth, j + side / 2);
        //    }
        //}
    }

    public void mousePressed() {
        for (int i = 0; i < width; i += lineWidth) {
            // line(i, 0, i, height);

            for (float j = i / lineWidth % 2 == 0 ? 0 : side / 2; j < height; j += lineHeight) {
                Triangle leftTriangle = new Triangle(i, j, i + lineWidth, j - side / 2, i + lineWidth, j + side / 2);
                Triangle rightTriangle = new Triangle(i, j, i, j + side, i + lineWidth, j + side / 2);

                if (checkCollision(mouseX, mouseY, leftTriangle)) {
                    PVector triangleCenter = getTriangleCenter(leftTriangle);
                    int currentState = state.get(triangleCenter);
                    state.put(triangleCenter,++currentState);


                    stroke(200);
                    //noStroke();
                    int triangleColor = (currentState) % colors.length;
                    if (triangleColor != 0) {
                        noStroke();
                    }
                    fill(colors[triangleColor]);
                    triangle(i, j, i + lineWidth, j - side / 2, i + lineWidth, j + side / 2);
                    noFill();

                } else if (checkCollision(mouseX, mouseY, rightTriangle )) {
                    PVector triangleCenter = getTriangleCenter(rightTriangle);
                    int currentState = state.get(triangleCenter);
                    state.put(triangleCenter,++currentState);

                    stroke(200);
                    //noStroke();
                    int triangleColor = (currentState) % colors.length;

                    if (triangleColor != 0) {
                        noStroke();
                    }
                    fill(colors[triangleColor]);
                    triangle(i, j, i, j + side, i + lineWidth, j + side / 2);
                    noFill();

                }
            }
        }
    }

    PVector getTriangleCenter(Triangle t) {
        float centerX = t.point1x + t.point2x + t.point3x;
        centerX /= 3;
        float centerY = t.point1y + t.point2y + t.point3y;
        centerY /= 3;
        return new PVector(centerX, centerY);
    }
    boolean checkCollision(float x, float y, Triangle t) {
        float tArea, t1Area, t2Area, t3Area;
        float err = 0.01f;
        tArea = triangleArea(t.point1x, t.point1y, t.point3x, t.point3y, t.point2x, t.point2y);
        t1Area = triangleArea(x, y, t.point2x, t.point2y, t.point3x, t.point3y);
        t2Area = triangleArea(x, y, t.point3x, t.point3y, t.point1x, t.point1y);
        t3Area = triangleArea(x, y, t.point2x, t.point2y, t.point1x, t.point1y);
        //noStroke();
        //fill(#ff6600,50);
        //triangle(t.point1x, t.point1y, t.point3x, t.point3y, t.point2x, t.point2y);
        //fill(#ffff00,50);
        //triangle(x,y, t.point2x, t.point2y, t.point3x, t.point3y);
        //fill(#00ffff,50);
        //triangle(x,y, t.point3x, t.point3y, t.point1x, t.point1y);
        //fill(#ff0000,50);
        //triangle(x,y, t.point2x, t.point2y, t.point1x, t.point1y);

        float totalArea = t1Area + t2Area + t3Area;
        if(abs(totalArea- tArea) < err)println(totalArea == tArea);
        return (abs(totalArea- tArea) < err);
    }

    float triangleArea(float p1, float p2, float p3, float p4, float p5, float p6) {
        float a, b, c, d;
        a = p1 - p5;
        b = p2 - p6;
        c = p3 - p5;
        d = p4 - p6;
        return (0.5f * abs((a * d) - (b * c)));
    }

    class Triangle {
        float point1x;
        float point1y;
        float point2x;
        float point2y;
        float point3x;
        float point3y;

        Triangle(float point1x, float point1y, float point2x, float point2y, float point3x, float point3y) {
            this.point1x = point1x;
            this.point1y = point1y;
            this.point2x = point2x;
            this.point2y = point2y;
            this.point3x = point3x;
            this.point3y = point3y;
        }

        void drawTriangle() {
            triangle(point1x, point1y, point2x, point2y, point3x, point3y);
        }


    }
    public static void main(String... args){
        PApplet.main("Floored");
    }
}
