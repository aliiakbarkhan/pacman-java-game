import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        int row = 21;
        int col = 19;
        int tilesize = 32;
        int boardWidth = col * tilesize;
        int boardLength = row * tilesize;

        JFrame frame = new JFrame("PAC MAN");
        frame.setSize(boardWidth, boardLength);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);

    }
}
