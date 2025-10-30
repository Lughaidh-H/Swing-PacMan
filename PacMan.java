

public class PacMan {
   private int x, y, a;

   public PacMan(int x, int y, int Axis) {
       this.x = x;
       this.y = y;
       this.a = Axis;
   }

   public int getX() {
       return x;
   }

   public int getY() {
       return y;
   }
   public int getAxis() {
       return a;
   }
   // updates position and axis
   public void move(int dx, int dy, int d_axis) {
       this.x += dx;
       this.y += dy;
       this.a = d_axis;
   }
}
