
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends JPanel implements ActionListener {
   private static final long serialVersionUID = 1L;
   private int coinsEaten = 0;
   private int lives = 3;
   private int movesMade = 0;
   private int powerUpMoves = 0;
   private int level = 1;
   private List<Ghost> ghosts = new ArrayList<>();
   private List<Point> coins = new ArrayList<>();
   private PacMan pacMan;
   private Timer timer;
   private int[][] map;
   private Point powerUp;

   public Main() {
       pacMan = new PacMan(1,1,0); // Starting position of Pac-Man
       // initialzing map and boarders
       map = new int[][] {
               {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
               {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
               {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
               {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
               {1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1},
               {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
               {1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
               {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1},
               {1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1},
               {1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
               {1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1},
               {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1},
               {1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
               {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
               {1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1},
               {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
               {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
               {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
               {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
       };

       // Initialize ghosts and coins.
       ghosts.add(new Ghost(generateValidPosition()));
       for (int i = 0; i < 3; i++) {
           coins.add(generateValidPosition());
       }

       timer = new Timer(100, this);
       timer.start();
       // allows interaction with GUI
       setFocusable(true);
       addKeyListener(new KeyAdapter() {
           @Override
           public void keyPressed(KeyEvent e) {
               movePacMan(e);
           }
       });
   }

   public void movePacMan(KeyEvent e) {
       int dx = 0, dy = 0;
       //for Pac-Mans mouth direction
       int d_axis = 0;
       switch (e.getKeyCode()) {
           case KeyEvent.VK_UP:
               dy = -1;
               d_axis = 135;
               break;
           case KeyEvent.VK_DOWN:
               dy = 1;
               d_axis = -45;
               break;
           case KeyEvent.VK_LEFT:
               dx = -1;
               d_axis = -135;
               break;
           case KeyEvent.VK_RIGHT:
               dx = 1;
               d_axis = 45;
               break;
           case KeyEvent.VK_Q:
        	   PauseGame(); // if user wishes to pause/quit game          
       }
       // stores new x and y pos
       int newX = pacMan.getX() + dx;
       int newY = pacMan.getY() + dy;
       // checks for blue wall collision
       if (map[newY][newX] == 0) {
           pacMan.move(dx, dy, d_axis);
           movesMade++;
           checkGhostCollision(); // Check for collision after every Pac-Man move.

           if (powerUpMoves > 0) {
               powerUpMoves--;
               if (powerUpMoves == 0) {
            	   // Ghosts are no longer eatable
                   for (Ghost ghost : ghosts) ghost.setEatable(false);
               }
           }

           for (int i = 0; i < coins.size(); i++) {
               Point coin = coins.get(i);
               if (pacMan.getX() == coin.x && pacMan.getY() == coin.y) {
                   eatCoin(i);
                   break;
               }
           }

           if (powerUp != null && pacMan.getX() == powerUp.x && pacMan.getY() == powerUp.y) {
               activatePowerUp();
               powerUp = null;
           }
           
           if (movesMade % 2 == 0) {
               moveGhosts();
           }
       }
   }

   private void eatCoin(int index) {
       coinsEaten++;
       coins.set(index, generateValidPosition());
       if (coinsEaten % 5 == 0) {
           level++;
           spawnExtraGhost();
           if (level >= 3) {
               powerUp = generateValidPosition();
           }
       }
   }

   private void activatePowerUp() {
       powerUpMoves = 20; // Increased power-up duration
       for (Ghost ghost : ghosts) { //ghosts can now be eaten
           ghost.setEatable(true);
       }
   }

   private void moveGhosts() {
       for (Ghost ghost : ghosts) {
           ghost.chasePacMan(pacMan, map);
       }
       checkGhostCollision();
   }

   private void checkGhostCollision() {
       for (Ghost ghost : ghosts) {
           if (pacMan.getX() == ghost.getX() && pacMan.getY() == ghost.getY()) {
               if (ghost.isEatable()) {
                   ghosts.remove(ghost);
                   break;
               } else {
                   lives--;
                   if (lives <= 0) {
                       gameOver();
                   } else {
                       resetPacManPosition();
                   }
               }
           }
       }
   }
   private void PauseGame() {
       int option = JOptionPane.showConfirmDialog(this, "Game Paused! would you like to continue?", "Pac Man!", JOptionPane.YES_NO_OPTION);
       if (option == JOptionPane.YES_OPTION) {
       //continues game as normal    
       }
       else {
           System.exit(0);
       }
   }
   private void gameOver() {
       timer.stop();  
       int option = JOptionPane.showConfirmDialog(this, "Game Over! Retry?", "Game Over", JOptionPane.YES_NO_OPTION);
       if (option == JOptionPane.YES_OPTION) {
           resetGame();
       } else {
           System.exit(0);
       }
   }

   private void resetGame() {
       lives = 3;
       coinsEaten = 0;
       level = 1;
       ghosts.clear();
       ghosts.add(new Ghost(generateValidPosition()));
       pacMan = new PacMan(1, 1, 0);
       coins.clear();
       for (int i = 0; i < 3; i++) {
           coins.add(generateValidPosition());
       }
       powerUp = null;
       timer.start();
   }

   private void resetPacManPosition() {
       pacMan = new PacMan(1, 1, 0);
   }

   private Point generateValidPosition() {
       Random rand = new Random();
       int x, y;
       // point is used to store x and y values rather than two sep variables
       Point position;
       do {
           x = rand.nextInt(map[0].length);
           y = rand.nextInt(map.length);
           position = new Point(x, y);
       } while (map[y][x] != 0 || isPositionOccupied(position));
       return position;
   }

   private boolean isPositionOccupied(Point position) {
	   //checks if a position is occupied before assigning valid position for coins/ghost/power-up
       if (position.equals(new Point(pacMan.getX(), pacMan.getY()))) return true;
       for (Ghost ghost : ghosts) {
           if (position.equals(new Point(ghost.getX(), ghost.getY()))) return true;
       }
       for (Point coin : coins) {
           if (position.equals(coin)) return true;
       }
       return false;
   }

   private void spawnExtraGhost() {
       ghosts.add(new Ghost(generateValidPosition()));
   }

   @Override
   public void actionPerformed(ActionEvent e) {
	   // Ensure at least one ghost is present and not eatable to increase game difficulty
	    if (ghosts.isEmpty()) {
	        spawnExtraGhost();
	    }
	   // updates GUI 
       repaint();
   }

   @Override
   protected void paintComponent(Graphics g) {
       super.paintComponent(g);
       setBackground(Color.BLACK);
       //paints boarder for map 
       for (int y = 0; y < map.length; y++) {
           for (int x = 0; x < map[0].length; x++) {
               if (map[y][x] == 1) {
                   g.setColor(Color.BLUE);
                   g.fillRect(x * 40, y * 40, 40, 40);
               }
           }
       }

       g.setColor(Color.YELLOW);
       g.fillArc(pacMan.getX() * 40, pacMan.getY() * 40, 40, 40, pacMan.getAxis(), 270);

       g.setColor(Color.ORANGE);
       for (Point coin : coins) {
           g.fillOval(coin.x * 40 + 10, coin.y * 40 + 10, 20, 20);
       }

       if (powerUp != null) {
           g.setColor(Color.CYAN);
           g.fillRect(powerUp.x * 40 + 10, powerUp.y * 40 + 10, 20, 20);
       }

       for (Ghost ghost : ghosts) {
           g.setColor(ghost.isEatable() ? Color.GREEN : Color.RED);
           g.fillOval(ghost.getX() * 40, ghost.getY() * 40, 40, 40);
       }

       g.setColor(Color.WHITE);
       g.drawString("Lives: " + lives, 10, 20);
       g.drawString("Level: " + level, 10, 35);
       g.drawString("press Q to pause/quit ", 80, 20);
   }
      
       public static void main(String[] args) {
           // Create the game frame
           JFrame frame = new JFrame("Pac-Man!");
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

           // Add the Main game panel (where everything is rendered)
           frame.add(new Main());

           // Set the window size and make it visible
           frame.setSize(814, 795);
           frame.setVisible(true);
       }
   }