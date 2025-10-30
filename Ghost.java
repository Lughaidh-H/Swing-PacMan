
import java.awt.Point;

public class Ghost {
   private int x, y;
   private boolean eatable;  // Whether the ghost is vulnerable (eaten by Pac-Man)

   // Constructor: Initializes ghost position and eatable state
   public Ghost(Point startPosition) {
       this.x = startPosition.x;
       this.y = startPosition.y;
       this.eatable = false; // Initially, ghosts are not eatable
   }

   // Getter methods for x and y
   public int getX() {
       return x;
   }

   public int getY() {
       return y;
   }

   // Sets whether the ghost can be eaten (i.e., when power-up is active)
   public void setEatable(boolean eatable) {
       this.eatable = eatable;
   }

   // Check if the ghost can be eaten
   public boolean isEatable() {
       return eatable;
   }

   // Chase logic
   public void chasePacMan(PacMan pacMan, int[][] map) {
       int targetX = pacMan.getX();
       int targetY = pacMan.getY();

       // Temporary positions to try to move towards Pac-Man
       int[] possibleMovesX = {0, 0, 1, -1}; // Right, Left, Down, Up
       int[] possibleMovesY = {1, -1, 0, 0};

       // Variables to track the best move (closest to Pac-Man)
       int bestX = this.x;
       int bestY = this.y;
       // initially set to the largest integer to ensure any valid distance will be smaller
       int minDistance = Integer.MAX_VALUE;

       // Check all four possible directions (right, left, down, up)
       for (int i = 0; i < 4; i++) {
           int newX = this.x + possibleMovesX[i];
           int newY = this.y + possibleMovesY[i];

           // Check if the new position is within the bounds and is not a wall
           if (newX >= 0 && newY >= 0 && newX < map[0].length && newY < map.length && map[newY][newX] == 0) {
               int distance = Math.abs(newX - targetX) + Math.abs(newY - targetY);

               // If this move brings the ghost closer to Pac-Man, choose this move
               if (distance < minDistance) {
                   minDistance = distance;
                   bestX = newX;
                   bestY = newY;
               }
           }
       }

       // Move the ghost to the best position found
       this.x = bestX;
       this.y = bestY;
   }
}