import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;


public class PacMan extends JPanel implements ActionListener, KeyListener{
    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;


        int startX;
        int startY;
        char direction = 'U';
        int valocityX = 0;
        int valocityY = 0;

        Block(Image image, int x, int y, int width, int height){
            this.image = image;
            this.x= x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;

        }

        void updateDicrection(char direction){
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += valocityX;
            this.y += valocityY;
            for(Block wall : walls){
                if (collision(this, wall)) {
                    this.x -= this.valocityX;
                    this.y -= this.valocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }
        void updateVelocity(){
            if (this.direction =='U') {
                this.valocityX = 0;
                this.valocityY = -tilesize/4;
            }
            else if (this.direction =='D') {
                this.valocityX = 0;
                this.valocityY = tilesize/4;
            }
            else if (this.direction == 'L') {
                this.valocityX = -tilesize/4;
                this.valocityY = 0;
            }
            else if (this.direction == 'R') {
                this.valocityX = tilesize/4;
                this.valocityY = 0;
            }
        }
        void reset(){
            this.x = this.startX;
            this.y = this.startY;
        }
    }
    private int row = 21;
    private int col = 19;
    private int tilesize = 32;
    private int boardWidth = col * tilesize;
    private int boardLength = row * tilesize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;
    Timer gameLoop;

    char[] direction = {'U','D','L','R'};
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;

    PacMan(){
        setPreferredSize(new Dimension(boardWidth, boardLength));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //load image
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
        
        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();
    
        loadMap();
        for(Block ghost:ghosts){
            char newDirction = direction[random.nextInt(4)];
            ghost.updateDicrection(newDirction);
        }
        gameLoop = new Timer(50, this);
        gameLoop.start();
    }



    public void loadMap(){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for(int r  = 0; r < row; r++){
            for(int c=0; c<col; c++){
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tilesize;
                int y = r*tilesize;

                if (tileMapChar == 'X') {//walls
                    Block wall  = new Block(wallImage, x, y, tilesize, tilesize);
                    walls.add(wall);
                }
                else if (tileMapChar =='b') {//blue
                    Block ghost = new Block(blueGhostImage, x, y, tilesize, tilesize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar =='o') {//orange
                    Block ghost = new Block(orangeGhostImage, x, y, tilesize, tilesize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar =='p') {//pink
                    Block ghost = new Block(pinkGhostImage, x, y, tilesize, tilesize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar =='r') {//red
                    Block ghost = new Block(redGhostImage, x, y, tilesize, tilesize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'P') {//pacman
                    pacman = new Block(pacmanRightImage, x, y, tilesize, tilesize);
                }
                else if (tileMapChar ==' ') {//food
                    Block food = new Block(null, x+14, y+14,4,4);
                    foods.add(food);
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height,null);

        for(Block ghost : ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for(Block wall : walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }
        g.setColor(Color.WHITE);
        for(Block food : foods){
            g.fillRect(food.x, food.y, food.width, food.height);
        }
        //score
        g.setFont(new Font("Arial",Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over : "+ String.valueOf(score), tilesize/2, tilesize/2);
        }
        else{
            g.drawString("by Ali Akbar Khan, Thanks Kenny Yip Coding "+ "Lives: "+ String.valueOf(lives) + " Score : "+ String.valueOf(score), tilesize/2, tilesize/2);
        }
    }

    public void move(){
        pacman.x += pacman.valocityX;
        pacman.y += pacman.valocityY;
        
        for( Block wall : walls){
            if (collision(pacman, wall)) {
                pacman.x -= pacman.valocityX;
                pacman.y -= pacman.valocityY;
                break;
            }
        }

        for(Block ghost : ghosts){
            if (collision(ghost, pacman)) {
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }
            if (ghost.y == tilesize*9 && ghost.direction !='U' && ghost.direction!='D') {
                ghost.updateDicrection('U');
            }
            ghost.x += ghost.valocityX;
            ghost.y += ghost.valocityY;
            for(Block wall : walls){
                if (collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth) {
                    ghost.x -= ghost.valocityX;
                    ghost.y -= ghost.valocityY;
                    char newDirction = direction[random.nextInt(4)];
                    ghost.updateDicrection(newDirction);
                }
            }
        }
        //eat food
        Block foodEaten = null;
        for(Block food : foods){
            if (collision(pacman, food)) {
                foodEaten = food;
                score +=10;
            }
        }
        foods.remove(foodEaten);
    }

    public boolean collision(Block a, Block b){
        return a.x < b.x + b.width && a.x + a.width > b.x &&
        a.y < b.y + b.height && a.y + a.height > b.y;
    }
    public void resetPositions(){
        pacman.reset();
        pacman.valocityX = 0;
        pacman.valocityY = 0;

        for(Block ghost: ghosts){
            ghost.reset();
            char newDirection = direction[random.nextInt(4)];
            ghost.updateDicrection(newDirection);
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }



    @Override
    public void keyTyped(KeyEvent e) {
        
    }



    @Override
    public void keyPressed(KeyEvent e) {}



    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }
        // System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDicrection('U');   
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDicrection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDicrection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDicrection('R');
        }

        if (pacman.direction == 'U') {
            pacman.image = pacmanUpImage;
        }
        else if (pacman.direction == 'D') {
            pacman.image = pacmanDownImage;
        }
        else if (pacman.direction == 'L') {
            pacman.image = pacmanLeftImage;
        }
        else if (pacman.direction == 'R') {
            pacman.image = pacmanRightImage;
        }
    }
    

}
