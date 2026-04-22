import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MainWindow {
    public static JFrame window = new JFrame();

    public MainWindow(){
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(800,800);
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
