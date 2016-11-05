
    final color[] colors = {color(255, 0, 0), color(0, 255, 0), color(0, 0, 255), color(255)};
    float lineWidth = 30;
    float lineHeight = (float) (2 * lineWidth / Math.sqrt(3));
    float side = lineHeight;
    int[][] state;

    void setup() {
        //size(400, 400);
        state = new int[(int) (width / lineWidth)][(int) (height * 2 / lineHeight)];
        println(width, height, state[0].length, state.length);

        fullScreen();
        background(255);
        stroke(200);

        for (int i = 0; i < width; i += lineWidth) {
            //line(i, 0, i, height);

            for (float j = i / lineWidth % 2 == 0 ? 0 : side / 2; j < height + lineHeight; j += lineHeight) {
                triangle(i, j, i + lineWidth, j - side / 2, i + lineWidth, j + side / 2);
                triangle(i, j, i, j + side, i + lineWidth, j + side / 2);
            }
        }

    }

    void draw() {
        //println(pointInTriangle(1,1, 1, 20, 30,30,2,20));
    }

    void mousePressed() {
        for (int i = 0; i < width; i += lineWidth) {
            // line(i, 0, i, height);

            for (float j = i / lineWidth % 2 == 0 ? 0 : side / 2; j < height; j += lineHeight) {
                if (checkCollision(mouseX, mouseY, new Triangle(i, j, i + lineWidth, j - side / 2, i + lineWidth, j + side / 2))) {
                    int currentState = state[(int) map(mouseX, 0, width, 0, state.length)]
                            [(int) map(mouseY, 0, height, 0, state[0].length)]++;
                    println(currentState);
                    stroke(153);
                    if (currentState != 0) {
                        noStroke();
                    }
                    fill(colors[(currentState + 1) % colors.length]);
                    triangle(i, j, i + lineWidth, j - side / 2, i + lineWidth, j + side / 2);
                    noFill();

                } else if (checkCollision(mouseX, mouseY, new Triangle(i, j, i, j + side, i + lineWidth, j + side / 2))) {
                    int currentState = state[(int) map(mouseX, 0, width, 0, state.length)]
                            [(int) map(mouseY, 0, height, 0, state[0].length) + 1]++;
                    stroke(153);

                    if (currentState != 0) {
                        noStroke();
                    }
                    fill(colors[(currentState + 1) % colors.length]);
                    triangle(i, j, i, j + side, i + lineWidth, j + side / 2);
                    noFill();

                }
            }
        }
    }

//boolean pointInTriangle(float aX, float aY,
//                          float bX, float bY, 
//                            float cX, float cY, 
//                              float pointX, float pointY) {
//  float slopeAB = (bY - aY) / (bX - aX);
//  float slopeBC = (bY - cY) / (bX - cX);
//  float slopeAC = (aY - cY) / (aX - cX);

    boolean checkCollision(float x, float y, Triangle t) {
        float tArea, t1Area, t2Area, t3Area;
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
        return (totalArea == tArea);
    }

    float triangleArea(float p1, float p2, float p3, float p4, float p5, float p6) {
        float a, b, c, d;
        a = p1 - p5;
        b = p2 - p6;
        c = p3 - p5;
        d = p4 - p6;
        return (0.5 * abs((a * d) - (b * c)));
    }

    //return signum(slopeAB * (pointX-bX) + bY- pointY) == signum(slopeBC * (pointX-bX) + bY-pointY);
//}
//int signum(float f) {
//  if (f > 0) return 1;
//  if (f < 0) return -1;
//  return 0;
//} 
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