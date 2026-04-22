import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JPanel implements Pieces {
    private Piece[][] pieces;
    private static Piece selectedPiece = new Empty(-1, -1);
    private boolean isBlackTurn;

    public Piece getPiece(int x, int y) {
        return pieces[y][x];
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public boolean isValidMove(Piece toMove, int x, int y) {
        int[] canMoveTo = toMove.canMoveTo(pieces);
        for (int i = 0; i < canMoveTo.length; i++) {
            if (canMoveTo[i] == (10 * x + y)) {
                return true;
            }
        }
        return false;
    }

    public void movePiece(Piece piece, int x, int y) {
        if (!isValidMove(piece, x, y))
            return;
        pieces[piece.y][piece.x] = new Empty(piece.x, piece.y);
        piece.setX(x);
        piece.setY(y);
        if (piece.getClass()== WhitePawn.class&&y==0)
            pieces[y][x]=new WhiteQueen(x,y);
        else if (piece.getClass()==BlackPawn.class&&y==7)
            pieces[y][x]=new BlackQueen(x,y);
        else pieces[y][x] = piece;


    }
    private void setDefaultPieces(){
        pieces = new Piece[8][8];

        pieces[0] = new Piece[]{new BlackRook(0, 0), new BlackKnight(1, 0), new BlackBishop(2, 0), new BlackQueen(3, 0), new BlackKing(4, 0), new BlackBishop(5, 0), new BlackKnight(6, 0), new BlackRook(7, 0)};
        pieces[1] = new Piece[]{new BlackPawn(0, 1), new BlackPawn(1, 1), new BlackPawn(2, 1), new BlackPawn(3, 1), new BlackPawn(4, 1), new BlackPawn(5, 1), new BlackPawn(6, 1), new BlackPawn(7, 1)};
        for (int i = 2; i < 6; i++) {//for every row = y
            for (int j = 0; j < 8; j++) {//for every line = x
                pieces[i][j] = new Empty(j, i);
            }
        }
        pieces[6] = new Piece[]{new WhitePawn(0, 6), new WhitePawn(1, 6), new WhitePawn(2, 6), new WhitePawn(3, 6), new WhitePawn(4, 6), new WhitePawn(5, 6), new WhitePawn(6, 6), new WhitePawn(7, 6)};
        pieces[7] = new Piece[]{new WhiteRook(0, 7), new WhiteKnight(1, 7), new WhiteBishop(2, 7), new WhiteQueen(3, 7), new WhiteKing(4, 7), new WhiteBishop(5, 7), new WhiteKnight(6, 7), new WhiteRook(7, 7)};

    }
    private void drawPossibleMove(Piece piece,Graphics g){
        g.setColor(Color.getHSBColor(0.1875F, 0.6732673F, 0.7921569F));
        int x=-1;
        int y=-1;
        int [] canMoveTo = piece.canMoveTo(pieces);
        if (piece.x%2==0&&piece.y%2==0)
            g.setColor(Color.getHSBColor(0.17361112F, 0.1182266F, 0.79607844F));
        else if (piece.x%2==1&&piece.y%2==1)
            g.setColor(Color.getHSBColor(0.17361112F, 0.1182266F, 0.79607844F));
        else g.setColor(Color.getHSBColor(0.1875F, 0.6732673F, 0.7921569F));
        g.fillRect(piece.x*100,piece.y*100,100,100);
        for (int i = 0; i < canMoveTo.length; i++) {
            x=canMoveTo[i]/10;
            y=canMoveTo[i]%10;
            if (x%2==0&&y%2==0)
                g.setColor(Color.getHSBColor(0.17361112F, 0.1182266F, 0.79607844F));
            else if (x%2==1&&y%2==1)
                g.setColor(Color.getHSBColor(0.17361112F, 0.1182266F, 0.79607844F));
            else g.setColor(Color.getHSBColor(0.1875F, 0.6732673F, 0.7921569F));
//            g.fillRect(100*x,100*y,100,100);
            g.fillRoundRect(100*x,100*y,20,20,30,30);
        }


    }

    public Board() {
        JPanel board = new JPanel();
        board.setBounds(0, 0, 800, 800);
        setDefaultPieces();
        board.setFocusable(true);
        board.grabFocus();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / 100;
                int y = e.getY() / 100;
                if (selectedPiece.isEmpty) {//player did not click a piece yet
                    if (pieces[y][x].isBlack==isBlackTurn) {
                        selectedPiece = pieces[y][x];
                        drawPossibleMove(selectedPiece,getGraphics());
                        getGraphics().drawImage(selectedPiece.image,selectedPiece.x*100+10,selectedPiece.y*100+10,null);

                    }
                } else if (!selectedPiece.isEmpty){
                    if (isValidMove(selectedPiece, x, y)) {//check is redundant
                        movePiece(selectedPiece, x, y);
                        selectedPiece = new Empty();
                        isBlackTurn =!isBlackTurn;
                        MainWindow.window.repaint();
                    } else{
                        selectedPiece=new Empty();
                        MainWindow.window.repaint();
                    }
                        //add later that turn continues
                }
            }
        });
        }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        System.out.println("painting board");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == 1) {
                    if (j % 2 == 1)
                        g.setColor(Color.getHSBColor(0.17261906F, 0.118644066F, 0.9254902F));
                    else g.setColor(Color.getHSBColor(0.2512438F, 0.44966444F, 0.58431375F));
                } else {
                    if (j % 2 == 1)
                        g.setColor(Color.getHSBColor(0.2512438F, 0.44966444F, 0.58431375F));
                    else g.setColor(Color.getHSBColor(0.17261906F, 0.118644066F, 0.9254902F));
                }
                g.fillRect(i * 100, j * 100, 100, 100);
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                g.drawImage(pieces[i][j].image, j * 100 + 20, i * 100 + 20, null);
            }
        }

    }
}
