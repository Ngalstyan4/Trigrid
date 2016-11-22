import processing.core.PApplet;
import processing.core.PVector;

import java.util.HashMap;


/**
 * Created by ngalstyan on 11/22/16.
 */
public class Floored extends PApplet {
    /*
     * list of colors to iterate through
     * The first must be the ground state color
     * the list may be extended without any additional code changes
     */
    private final int[] colors = {color(255), color(217, 0, 0), color(0, 51, 160), color(239, 107, 0)};
    private float lineWidth = 30;
    private float lineHeight = (float) (2 * lineWidth / Math.sqrt(3));
    private float side = lineHeight;
    private HashMap<PVector, Integer> state;

    // some dummy-constants to avoid confusion in naming
    private boolean RIGHT = false, LEFT = true;

    /**
     * Running Processing sketch
     */
    public static void main(String... args) {
        PApplet.main("Floored");
    }

    /**
     * setting up the stage
     */
    public void settings() {
        size(400, 400);
        //fullScreen();
    }

    /**
     * The initial state of the program
     * initiating the data structures
     * drawing the grid
     */
    public void setup() {
        //size(400, 400); // for pde file
        state = new HashMap<PVector, Integer>();
        //fullScreen(); // for pde file
        background(255);
        stroke(200); // line strength
        // for each vertical line
        for (int i = 0; i < width; i += lineWidth) {
            // draw a triangle to indent by side/2 depending on the position of the
            // vertical line
            for (float j = i / lineWidth % 2 == 0 ? 0 : side / 2;
                 j < height + lineHeight;
                 j += lineHeight) {
                // construct the triangles
                Triangle rightTriangle = createTriangle(i, j, RIGHT);
                Triangle leftTriangle = createTriangle(i, j, LEFT);
                rightTriangle.initDrawTriangle();
                leftTriangle.initDrawTriangle();
            }
        }

    }

    /**
     * The processing function to draw the frames.
     * as in this program the only conceptual change happens when mousePressed
     * event is fired, the code in this block may be commented out to also boost
     * performance.
     * However, commenting it out sometimes results in slight differences in
     * positions of triangles resulting in unwanted lines between them
     */
    public void draw() {
        for (int i = 0; i < width; i += lineWidth) {

            for (float j = i / lineWidth % 2 == 0 ? 0 : side / 2;
                 j < height + lineHeight;
                 j += lineHeight) {

                Triangle leftTriangle = createTriangle(i, j, LEFT);
                Triangle rightTriangle = createTriangle(i, j, RIGHT);

                leftTriangle.drawTriangle(false);
                rightTriangle.drawTriangle(false);
            }
        }
    }

    /**
     * Processing mosePressed event;
     * checking whether the given triangle is clicked
     * I could keep all center coordinates and this code would change into simply
     * checking whether the given mouse coordinates belong to any of the triangles.
     * however,although  the complexity of this code is proportional to the number of
     * of triangles, because there practically are not supposed to have too many
     * triangles(it shall at least fit in the screen) the extra overhead of this code
     * is negligible and this way is prevferred due to visual simplicity.
     */
    public void mousePressed() {
        for (int i = 0; i < width; i += lineWidth) {
            // I initially thought this might be need but then realised that the
            // three sides of a triangle suffice to draw every needed line
            // line(i, 0, i, height);

            for (float j = i / lineWidth % 2 == 0 ? 0 : side / 2;
                 j < height;
                 j += lineHeight) {
                Triangle leftTriangle = createTriangle(i, j, LEFT);
                Triangle rightTriangle = createTriangle(i, j, RIGHT);

                if (leftTriangle.checkCollision(mouseX, mouseY)) {
                    leftTriangle.drawTriangle();
                } else if (rightTriangle.checkCollision(mouseX, mouseY)) {
                    rightTriangle.drawTriangle();
                }
            }
        }
    }


    // HELPER FUNCTIONS //

    /**
     * calculate the area of given triangle.
     * I use this to calculate areas of subtriangles of triangle object
     * I thought it was not worth the extra overhead to create triangle objects
     * and add this function to Triangle class that is why this function is here
     *
     * @param p1 X coordinate of vertex 1
     * @param p2 Y coordinate of vertex 1
     * @param p3 X coordinate of vertex 2
     * @param p4 Y coordinate of vertex 2
     * @param p5 X coordinate of vertex 3
     * @param p6 Y coordinate of vertex 3
     * @return
     */
    private float triangleArea(float p1, float p2, float p3, float p4, float p5, float p6) {
        float a, b, c, d;
        a = p1 - p5;
        b = p2 - p6;
        c = p3 - p5;
        d = p4 - p6;
        return (0.5f * abs((a * d) - (b * c)));
    }

    /**
     * a triangle factory
     * I would rather have this as a constructor or static method but both
     * are against Java philosophy.
     *
     * @param i    i coordinate when iterating through triangles
     * @param j    j coordinate when iterating through triangles
     * @param side side of the triangle, LEFT or RIGHT
     * @return
     */
    private Triangle createTriangle(float i, float j, boolean side) {
        if (side == LEFT)
            return new Triangle(i, j, i, j + this.side, i + lineWidth, j + this.side / 2);
        else /*if (side == RIGHT)*/
            return new Triangle(i, j, i + lineWidth, j - this.side / 2, i + lineWidth, j + this.side / 2);

    }

    /**
     * a Triangle abstraction to encapsulate triangle related logic from code
     * and avoid repetition
     */
    private class Triangle {
        // According to encapsulation rules, these must be private,
        // but it will not change anything at this point as this is an inner class
        float point1x;
        float point1y;
        float point2x;
        float point2y;
        float point3x;
        float point3y;

        /**
         * @param p1 X coordinate of vertex 1
         * @param p2 Y coordinate of vertex 1
         * @param p3 X coordinate of vertex 2
         * @param p4 Y coordinate of vertex 2
         * @param p5 X coordinate of vertex 3
         * @param p6 Y coordinate of vertex 3
         */
        Triangle(float p1, float p2,
                 float p3, float p4,
                 float p5, float p6) {
            this.point1x = p1;
            this.point1y = p2;
            this.point2x = p3;
            this.point2y = p4;
            this.point3x = p5;
            this.point3y = p6;
        }
        // not allowed this way!
//        Triangle(float i, float j, boolean left) {
//            if (left) this(i, j, i + lineWidth, j - side / 2, i + lineWidth, j + side / 2);
//        }

        /**
         * get the center of the triangle
         *
         * @return Vector of coordinates of the center
         */
        PVector getCenter() {
            float centerX = this.point1x + this.point2x + this.point3x;
            centerX /= 3;
            float centerY = this.point1y + this.point2y + this.point3y;
            centerY /= 3;
            return new PVector(centerX, centerY);
        }

        /**
         * Draw triangle and initiate its state
         */
        void initDrawTriangle() {
            triangle(point1x, point1y, point2x, point2y, point3x, point3y);
            state.put(this.getCenter(), 0);

        }

        /**
         * draw the triangle with the given state
         *
         * @param modifyState change the state into the next state if set to true
         */
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

        /**
         * a shortcut for drawing triangle with state and state modification
         */
        void drawTriangle() {
            drawTriangle(true);
        }

        /**
         * check whether the triangle collides with the given coordinates
         * (if the given coordinates are inside the triangle)
         * Idea: if the three triangle the point devides the triangle into have
         * comulative area equal to the area of the main triangle, then the point is
         * inside
         *
         * @param x x
         * @param y y
         * @return
         */
        private boolean checkCollision(float x, float y) {
            float tArea, t1Area, t2Area, t3Area;
            // this is needed as because of float rounding the point may me inside
            // which the calculated values would not be strictly equal
            float err = 0.01f;
            tArea = triangleArea(this.point1x, this.point1y, this.point3x, this.point3y, this.point2x, this.point2y);
            t1Area = triangleArea(x, y, this.point2x, this.point2y, this.point3x, this.point3y);
            t2Area = triangleArea(x, y, this.point3x, this.point3y, this.point1x, this.point1y);
            t3Area = triangleArea(x, y, this.point2x, this.point2y, this.point1x, this.point1y);
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

    }
}
