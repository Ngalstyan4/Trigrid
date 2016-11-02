  float lineWidth = 30;
  float lineHeight = (float)(2 * lineWidth / Math.sqrt(3));
  float side = lineHeight;

void setup() {
  //size(400, 400);
  fullScreen();
 background(255);

  for(int i = 0; i < width; i += lineWidth) {
      line(i, 0, i, height);

      for(float j = i/lineWidth % 2 == 0 ? 0 : side/2; j < height + lineHeight; j += lineHeight) {
        triangle(i, j, i + lineWidth, j - side/2, i + lineWidth, j + side/2);
        triangle(i, j, i, j + side, i + lineWidth, j + side/2);
      }
  }
  
}

void draw() {

}

void mousePressed() {
    for(int i = 0; i < width; i += lineWidth) {
      line(i, 0, i, height);

      for(float j = i/lineWidth % 2 == 0 ? 0 : side/2; j < height; j += lineHeight) {
        if(i < mouseX && i + lineWidth > mouseX)
        {
          fill((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
          triangle(i, j, i + lineWidth, j - side/2, i + lineWidth, j + side/2);
          triangle(i, j, i, j + side, i + lineWidth, j + side/2);
          noFill();
        }
            }
  }
}