import javax.swing.*;

public class MainWindow {
    final public int WindowWidth =800;
    final public int WindowHeight =800;
    public static JFrame window = new JFrame();

    public MainWindow(){
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(WindowWidth,WindowHeight);
        window.repaint();
        Board gameBoard = new Board();
        window.add(gameBoard);
        System.out.println("Added board");
        window.repaint();
        window.setVisible(true);
        window.repaint();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        window.repaint();

    }
}
