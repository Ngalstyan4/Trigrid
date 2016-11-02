  float lineWidth = 30;
  float lineHeight = (float)(2 * lineWidth / Math.sqrt(3));
  float side = lineHeight;

void setup() {
  //size(400, 400);
  fullScreen();
  surface.setResizable(true);
    background(255);

  for(int i = 0; i < width; i += lineWidth) {
      line(i, 0, i, height);

      for(float j = i/lineWidth % 2 == 0 ? 0 : side/2; j < height + lineHeight; j += lineHeight) {
        triangle(i, j, i + lineWidth, j - side/2, i + lineWidth, j + side/2);ß
      }
  }
  
}

void draw() {

}