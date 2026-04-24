import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public interface Pieces {
    abstract class Piece {
        protected int x;
        protected int y;
        boolean isBlack;
        boolean isEmpty;
        boolean isKing;
        boolean isQueen;
        boolean isBishop;
        boolean isKnight;
        boolean isRook;
        boolean isPawn;
        boolean moved; //for castling
        Image image;

        abstract int[] canMoveTo(Piece[][] pieces);

        public void setX(int x) {
            this.x = x;
        }
        public void setY(int y) {
            this.y = y;
        }

        private boolean isOpponent(Piece otherPiece) {
            if (this.isEmpty || otherPiece.isEmpty)
                return false;
            if (this.isBlack && !otherPiece.isBlack)
                return true;
            if (otherPiece.isBlack && !this.isBlack)
                return true;
            return false;
        }
    }

    static Piece newCopyOfPiece(Piece piece){
        Piece piece1;
        if (piece.isPawn){
            if (piece.isBlack)
                return new BlackPawn(piece.x,piece.y);
            else return new WhitePawn(piece.x,piece.y);
        }
        if (piece.isRook){
            if (piece.isBlack){
                piece1 = new BlackRook(piece.x,piece.y);
                piece1.moved = piece.moved;
                return piece1;
            }
            else{
                piece1= new WhiteRook(piece.x,piece.y);
                piece1.moved = piece.moved;
                return piece1;
            }
        }
        if (piece.isKnight){
            if (piece.isBlack)
                return new BlackKnight(piece.x,piece.y);
            else return new WhiteKnight(piece.x,piece.y);
        }
        if (piece.isBishop){
            if (piece.isBlack)
                return new BlackBishop(piece.x,piece.y);
            else return new WhiteBishop(piece.x,piece.y);
        }
        if (piece.isKing){
            if (piece.isBlack){
                piece1 = new BlackKing(piece.x,piece.y);
                piece1.moved =piece.moved;
                return piece1;
            }
            else{
                piece1 = new WhiteKing(piece.x,piece.y);
                piece1.moved =piece.moved;
                return piece1;
            }
        }
        if (piece.isQueen){
            if (piece.isBlack)
                return new BlackQueen(piece.x,piece.y);
            else return new WhiteQueen(piece.x,piece.y);
        }
        return new Empty();
    }
    static Piece[][] newCopyOfPieces(Piece[][] pieces){
        Piece[][] temp = new Piece[pieces.length][pieces.length];
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp.length; j++) {
                temp[j][i]=newCopyOfPiece(pieces[j][i]);
            }
        }return temp;
    }

    private static boolean kingCanShortCastle(Piece[][] pieces, Piece king) {
        if (king.moved)
            return false;
        if (!king.isKing)
            return false;
        if (king.x!=4)//dont know why its needed but it would give error out of bounds without it
            return false;
        if (pieces[king.y][king.x + 1].isEmpty && pieces[king.y][king.x + 2].isEmpty)//short castle, o-o
            if (pieces[king.y][king.x + 3].isBlack == king.isBlack && pieces[king.y][king.x + 3].isRook && !pieces[king.y][king.x + 3].moved) {
                return true;
            }
        return false;
    }
    private static boolean kingCanLongCastle(Piece[][] pieces, Piece king) {
        if (king.moved)
            return false;
        if (!king.isKing)
            return false;
        if (king.x!=4)//dont know why its needed, but it would give error out of bounds without it
            return false;
        for (int i = 1; i <4 ; i++) {
            if (!pieces[king.y][king.x-i].isEmpty)
                return false;
        }
        if (pieces[king.y][king.x -4].isBlack == king.isBlack && pieces[king.y][king.x -4].isRook && !pieces[king.y][king.x -4].moved) {
            return true;
        }
        return false;
    }

    private static Piece checkSquare(Piece[][] board, int x, int y) {
        return board[y][x];
    }

    private static int[] rookMove(Piece[][] pieces, Piece piece, boolean isBlack) {
        int count = 0;
        for (int i = 1; i < 8; i++) {
            if (piece.x + i > 7)
                break;
            if (checkSquare(pieces, piece.x + i, piece.y).isEmpty)
                count++;
            else if (checkSquare(pieces, piece.x + i, piece.y).isOpponent(piece)) {
                count++;
                break;
            } else break;
        }
        for (int i = 1; i < 8; i++) {
            if (piece.y + i > 7)
                break;
            if (checkSquare(pieces, piece.x, piece.y + i).isEmpty)
                count++;
            else if (checkSquare(pieces, piece.x, piece.y + i).isOpponent(piece)) {
                count++;
                break;
            } else break;
        }
        for (int i = 1; i < 8; i++) {
            if (piece.x - i < 0)
                break;
            if (checkSquare(pieces, piece.x - i, piece.y).isEmpty)
                count++;
            else if (checkSquare(pieces, piece.x - i, piece.y).isOpponent(piece)) {
                count++;
                break;
            } else break;
        }
        for (int i = 1; i < 8; i++) {
            if (piece.y - i < 0)
                break;
            if (checkSquare(pieces, piece.x, piece.y - i).isEmpty)
                count++;
            else if (checkSquare(pieces, piece.x, piece.y - i).isOpponent(piece)) {
                count++;
                break;
            } else break;
        }
        int[] canMoveTo = new int[count];
        int index = 0;
        for (int i = 1; i < 8; i++) {
            if (piece.x + i > 7)
                break;
            if (checkSquare(pieces, piece.x + i, piece.y).isEmpty) {
                canMoveTo[index] = (piece.x + i) * 10 + piece.y;
                index++;
            } else if (checkSquare(pieces, piece.x + i, piece.y).isOpponent(piece)) {
                canMoveTo[index] = (piece.x + i) * 10 + piece.y;
                index++;
                break;
            } else break;
        }
        for (int i = 1; i < 8; i++) {
            if (piece.y + i > 7)
                break;
            if (checkSquare(pieces, piece.x, piece.y + i).isEmpty) {
                canMoveTo[index] = piece.x * 10 + piece.y + i;
                index++;
            } else if (checkSquare(pieces, piece.x, piece.y + i).isOpponent(piece)) {
                canMoveTo[index] = (piece.x) * 10 + piece.y + i;
                index++;
                break;
            } else break;
        }
        for (int i = 1; i < 8; i++) {
            if (piece.x - i < 0)
                break;
            if (checkSquare(pieces, piece.x - i, piece.y).isEmpty) {
                canMoveTo[index] = (piece.x - i) * 10 + piece.y;
                index++;
            } else if (checkSquare(pieces, piece.x - i, piece.y).isOpponent(piece)) {
                canMoveTo[index] = (piece.x - i) * 10 + piece.y;
                index++;
                break;
            } else break;
        }
        for (int i = 1; i < 8; i++) {
            if (piece.y - i < 0)
                break;
            if (checkSquare(pieces, piece.x, piece.y - i).isEmpty) {
                canMoveTo[index] = piece.x * 10 + piece.y - i;
                index++;
            } else if (checkSquare(pieces, piece.x, piece.y - i).isOpponent(piece)) {
                canMoveTo[index] = piece.x * 10 + piece.y - i;
                index++;
                break;
            } else break;
        }

        return canMoveTo;
    }

    private static int[] knightMove(Piece[][] pieces, Piece piece, boolean isBlack) {
        int x = piece.x;
        int y = piece.y;
        int count = 0;
        if (x - 2 >= 0 && y - 1 >= 0) {
            if (checkSquare(pieces, x - 2, y - 1).isEmpty || checkSquare(pieces, x - 2, y - 1).isOpponent(piece))
                count++;
        }

        if (x - 2 >= 0 && y + 1 < 8) {
            if (checkSquare(pieces, x - 2, y + 1).isEmpty || checkSquare(pieces, x - 2, y + 1).isOpponent(piece))
                count++;
        }
        if (x - 1 >= 0 && y + 2 < 8) {
            if (checkSquare(pieces, x - 1, y + 2).isEmpty || checkSquare(pieces, x - 1, y + 2).isOpponent(piece))
                count++;
        }
        if (x + 1 < 8 && y + 2 < 8) {
            if (checkSquare(pieces, x + 1, y + 2).isEmpty || checkSquare(pieces, x + 1, y + 2).isOpponent(piece))
                count++;
        }
        if (x + 2 < 8 && y - 1 >= 0) {
            if (checkSquare(pieces, x + 2, y - 1).isEmpty || checkSquare(pieces, x + 2, y - 1).isOpponent(piece))
                count++;
        }
        if (x + 2 < 8 && y + 1 < 8) {
            if (checkSquare(pieces, x + 2, y + 1).isEmpty || checkSquare(pieces, x + 2, y + 1).isOpponent(piece))
                count++;
        }
        if (x - 1 >= 0 && y - 2 >= 0) {
            if (checkSquare(pieces, x - 1, y - 2).isEmpty || checkSquare(pieces, x - 1, y - 2).isOpponent(piece))
                count++;
        }
        if (x + 1 < 8 && y - 2 >= 0) {
            if (checkSquare(pieces, x + 1, y - 2).isEmpty || checkSquare(pieces, x + 1, y - 2).isOpponent(piece))
                count++;
        }
        int[] canMoveTo = new int[count];
        int index = 0;
        if (x - 2 >= 0 && y - 1 >= 0) {
            if (checkSquare(pieces, x - 2, y - 1).isEmpty || checkSquare(pieces, x - 2, y - 1).isOpponent(piece)) {
                canMoveTo[index] = (x - 2) * 10 + y - 1;
                index++;
            }
        }
        if (x - 2 >= 0 && y + 1 < 8) {
            if (checkSquare(pieces, x - 2, y + 1).isEmpty || checkSquare(pieces, x - 2, y + 1).isOpponent(piece)) {
                canMoveTo[index] = (x - 2) * 10 + y + 1;
                index++;
            }
        }
        if (x - 1 >= 0 && y + 2 < 8) {
            if (checkSquare(pieces, x - 1, y + 2).isEmpty || checkSquare(pieces, x - 1, y + 2).isOpponent(piece)) {
                canMoveTo[index] = (x - 1) * 10 + y + 2;
                index++;
            }
        }
        if (x + 1 < 8 && y + 2 < 8) {
            if (checkSquare(pieces, x + 1, y + 2).isEmpty || checkSquare(pieces, x + 1, y + 2).isOpponent(piece)) {
                canMoveTo[index] = (x + 1) * 10 + y + 2;
                index++;
            }
        }
        if (x + 2 < 8 && y - 1 >= 0) {
            if (checkSquare(pieces, x + 2, y - 1).isEmpty || checkSquare(pieces, x + 2, y - 1).isOpponent(piece)) {
                canMoveTo[index] = (x + 2) * 10 + y - 1;
                index++;
            }
        }
        if (x + 2 < 8 && y + 1 < 8) {
            if (checkSquare(pieces, x + 2, y + 1).isEmpty || checkSquare(pieces, x + 2, y + 1).isOpponent(piece)) {
                canMoveTo[index] = (x + 2) * 10 + y + 1;
                index++;
            }
        }
        if (x - 1 >= 0 && y - 2 >= 0) {
            if (checkSquare(pieces, x - 1, y - 2).isEmpty || checkSquare(pieces, x - 1, y - 2).isOpponent(piece)) {
                canMoveTo[index] = (x - 1) * 10 + y - 2;
                index++;
            }
        }
        if (x + 1 < 8 && y - 2 >= 0) {
            if (checkSquare(pieces, x + 1, y - 2).isEmpty || checkSquare(pieces, x + 1, y - 2).isOpponent(piece)) {
                canMoveTo[index] = (x + 1) * 10 + y - 2;
                index++;
            }
        }
        return canMoveTo;
    }

    private static int[] bishopMove(Piece[][] pieces, Piece piece, boolean isBlack) {
        int x = piece.x;
        int y = piece.y;
        int count = 0;
        for (int i = 1; i < 8; i++) {
            if (x + i < 8 && y + i < 8) {
                boolean isEmpty = checkSquare(pieces, x + i, y + i).isEmpty;
                if (checkSquare(pieces, x + i, y + i).isEmpty) {
                    count++;
                    continue;
                } else if (checkSquare(pieces, x + i, y + i).isOpponent(piece)) {
                    count++;
                    break;
                } else break;//if is a white piece
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 0 && y + i < 8) {
                if (checkSquare(pieces, x - i, y + i).isEmpty)
                    count++;
                else if (checkSquare(pieces, x - i, y + i).isOpponent(piece)) {
                    count++;
                    break;
                } else break;//if is a white piece
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 0 && y - i >= 0) {
                if (checkSquare(pieces, x - i, y - i).isEmpty)
                    count++;
                else if (checkSquare(pieces, x - i, y - i).isOpponent(piece)) {
                    count++;
                    break;
                } else break;//if is a white piece
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i < 8 && y - i >= 0) {
                if (checkSquare(pieces, x + i, y - i).isEmpty)
                    count++;
                else if (checkSquare(pieces, x + i, y - i).isOpponent(piece)) {
                    count++;
                    break;
                } else break;//if is a white piece
            }
        }
        int[] canMoveTo = new int[count];
        int index = 0;
        for (int i = 1; i < 8; i++) {
            if (x + i < 8 && y + i < 8) {
                if (checkSquare(pieces, x + i, y + i).isEmpty) {
                    canMoveTo[index] = (x + i) * 10 + y + i;
                    index++;
                } else if (checkSquare(pieces, x + i, y + i).isOpponent(piece)) {
                    canMoveTo[index] = (x + i) * 10 + y + i;
                    index++;
                    break;
                } else break;//if is a white piece
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 0 && y + i < 8) {
                if (checkSquare(pieces, x - i, y + i).isEmpty) {
                    canMoveTo[index] = (x - i) * 10 + y + i;
                    index++;
                } else if (checkSquare(pieces, x - i, y + i).isOpponent(piece)) {
                    canMoveTo[index] = (x - i) * 10 + y + i;
                    index++;
                    break;
                } else break;//if is a white piece
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 0 && y - i >= 0) {
                if (checkSquare(pieces, x - i, y - i).isEmpty) {
                    canMoveTo[index] = (x - i) * 10 + y - i;
                    index++;
                } else if (checkSquare(pieces, x - i, y - i).isOpponent(piece)) {
                    canMoveTo[index] = (x - i) * 10 + y - i;
                    index++;
                    break;
                } else break;//if is a white piece
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i < 8 && y - i >= 0) {
                if (checkSquare(pieces, x + i, y - i).isEmpty) {
                    canMoveTo[index] = (x + i) * 10 + y - i;
                    index++;
                } else if (checkSquare(pieces, x + i, y - i).isOpponent(piece)) {
                    canMoveTo[index] = (x + i) * 10 + y - i;
                    index++;
                    break;
                } else break;//if is a white piece
            }
        }
        return canMoveTo;
    }

    private static int[] kingMove(Piece[][] pieces, Piece piece, boolean isBlack) {
        int x = piece.x;
        int y = piece.y;
        int count = 0;
        if (y - 1 >= 0) {
            if (checkSquare(pieces, x, y - 1).isEmpty || checkSquare(pieces, x, y - 1).isOpponent(piece))
                count++;
        }
        if (y - 1 >= 0 && x + 1 < 8) {
            if (checkSquare(pieces, x + 1, y - 1).isEmpty || checkSquare(pieces, x + 1, y - 1).isOpponent(piece))
                count++;
        }
        if (x + 1 < 8) {
            if (checkSquare(pieces, x + 1, y).isEmpty || checkSquare(pieces, x + 1, y).isOpponent(piece))
                count++;
        }
        if (x + 1 < 8 && y + 1 < 8) {
            if (checkSquare(pieces, x + 1, y + 1).isEmpty || checkSquare(pieces, x + 1, y + 1).isOpponent(piece))
                count++;
        }
        if (y + 1 < 8) {
            if (checkSquare(pieces, x, y + 1).isEmpty || checkSquare(pieces, x, y + 1).isOpponent(piece))
                count++;
        }
        if (x - 1 >= 0 && y + 1 < 8) {
            if (checkSquare(pieces, x - 1, y + 1).isEmpty || checkSquare(pieces, x - 1, y + 1).isOpponent(piece))
                count++;
        }
        if (x - 1 >= 0) {
            if (checkSquare(pieces, x - 1, y).isEmpty || checkSquare(pieces, x - 1, y).isOpponent(piece))
                count++;
        }
        if (x - 1 >= 0 && y - 1 >= 0) {
            if (checkSquare(pieces, x - 1, y - 1).isEmpty || checkSquare(pieces, x - 1, y - 1).isOpponent(piece))
                count++;
        }
        if (kingCanShortCastle(pieces,piece))
            count++;
        if (kingCanLongCastle(pieces,piece))
            count++;
        int[] canMoveTo = new int[count];
        int index = 0;

        if (y - 1 >= 0) {
            if (checkSquare(pieces, x, y - 1).isEmpty || checkSquare(pieces, x, y - 1).isOpponent(piece)) {
                canMoveTo[index] = x * 10 + y - 1;
                index++;
            }
        }
        if (x + 1 < 8 && y - 1 >= 0) {
            if (checkSquare(pieces, x + 1, y - 1).isEmpty || checkSquare(pieces, x + 1, y - 1).isOpponent(piece)) {
                canMoveTo[index] = (x + 1) * 10 + y - 1;
                index++;
            }
        }
        if (x + 1 < 8) {
            if (checkSquare(pieces, x + 1, y).isEmpty || checkSquare(pieces, x + 1, y).isOpponent(piece)) {
                canMoveTo[index] = (x + 1) * 10 + y;
                index++;
            }
        }
        if (x + 1 < 8 && y + 1 < 8) {
            if (checkSquare(pieces, x + 1, y + 1).isEmpty || checkSquare(pieces, x + 1, y + 1).isOpponent(piece)) {
                canMoveTo[index] = (x + 1) * 10 + y + 1;
                index++;
            }
        }
        if (y + 1 < 8) {
            if (checkSquare(pieces, x, y + 1).isEmpty || checkSquare(pieces, x, y + 1).isOpponent(piece)) {
                canMoveTo[index] = (x) * 10 + y + 1;
                index++;
            }
        }
        if (x - 1 >= 0 && y + 1 < 8) {
            if (checkSquare(pieces, x - 1, y + 1).isEmpty || checkSquare(pieces, x - 1, y + 1).isOpponent(piece)) {
                canMoveTo[index] = (x - 1) * 10 + y + 1;
                index++;
            }
        }
        if (x - 1 >= 0) {
            if (checkSquare(pieces, x - 1, y).isEmpty || checkSquare(pieces, x - 1, y).isOpponent(piece)) {
                canMoveTo[index] = (x - 1) * 10 + y;
                index++;
            }
        }
        if (x - 1 >= 0 && y - 1 >= 0) {
            if (checkSquare(pieces, x - 1, y - 1).isEmpty || checkSquare(pieces, x - 1, y - 1).isOpponent(piece)) {
                canMoveTo[index] = (x - 1) * 10 + y - 1;
                index++;
            }
        }
        if (kingCanShortCastle(pieces,piece)) {
            canMoveTo[index] = (piece.x + 2) * 10 + piece.y;
            index++;
        }if (kingCanLongCastle(pieces,piece)) {
            canMoveTo[index] = (piece.x - 2) * 10 + piece.y;
            index++;
        }
        return canMoveTo;
    }

    private static int[] queenMove(Piece[][] pieces, Piece piece, boolean isBlack) {
        int[] rookMove = rookMove(pieces, piece, isBlack);
        int[] bishopMove = bishopMove(pieces, piece, isBlack);
        int[] queenMoves = new int[rookMove.length + bishopMove.length];
        int index = 0;
        for (int i = 0; i < rookMove.length; i++) {
            queenMoves[index] = rookMove[i];
            index++;
        }
        for (int i = 0; i < bishopMove.length; i++) {
            queenMoves[index] = bishopMove[i];
            index++;
        }
        return queenMoves;
    }



    class Empty extends Piece {
        public Empty(int x, int y) {
            this.x = x;
            this.y = y;
            this.isEmpty = true;
        }

        public Empty() {
            isEmpty = true;
            x = -1;
            y = -1;
        }

        @Override
        int[] canMoveTo(Piece[][] pieces) {
            return new int[0];
        }
    }

    class BlackPawn extends Piece {

        {
            try {
                this.image = ImageIO.read(new File("res/BlackPawn.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public BlackPawn(int x, int y) {
            this.x = x;
            this.y = y;
            isBlack = true;
            isPawn = true;
        }

        @Override
        public int[] canMoveTo(Piece[][] pieces) {
            int count = 0;
            if (y == 1) {
                if (checkSquare(pieces, x, y + 1).isEmpty) {
                    if (checkSquare(pieces, x, y + 2).isEmpty)
                        count++;
                }
            }
            if (y < 7)
                if (checkSquare(pieces, x, y + 1).isEmpty)
                    count++;
            if (x < 7 && y < 7)
                if (checkSquare(pieces, x + 1, y + 1).isOpponent(this)) {
                    count++;
                }
            if (x > 0 && y < 7)
                if (checkSquare(pieces, x - 1, y + 1).isOpponent(this)) {
                    count++;
                }
            int[] canMoveTo = new int[count];
            int index = 0;
            if (y == 1) {
                if (checkSquare(pieces, x, y + 1).isEmpty) {
                    if (checkSquare(pieces, x, y + 2).isEmpty) {
                        canMoveTo[index] = x * 10 + y + 2;
                        index++;
                    }
                }
            }
            if (y < 7)
                if (checkSquare(pieces, x, y + 1).isEmpty) {
                    canMoveTo[index] = this.x * 10 + (this.y + 1);
                    index++;
                }
            if (x < 7 && y < 7)
                if (checkSquare(pieces, x + 1, y + 1).isOpponent(this)) {
                    canMoveTo[index] = (x + 1) * 10 + (y + 1);
                    index++;
                }
            if (x > 0 && y < 7)
                if (checkSquare(pieces, x - 1, y + 1).isOpponent(this)) {
                    canMoveTo[index] = (x - 1) * 10 + (y + 1);
                    index++;
                }
            return canMoveTo;

        }
    }

    class WhitePawn extends Piece {

        {
            try {
                this.image = ImageIO.read(new File("res/WhitePawn.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public WhitePawn(int x, int y) {
            this.x = x;
            this.y = y;
            isPawn = true;
        }

        int[] canMoveTo(Piece[][] pieces) {
            int count = 0;
            if (y == 6) {
                if (checkSquare(pieces, x, y - 1).isEmpty) {
                    if (checkSquare(pieces, x, y - 2).isEmpty)
                        count++;
                }
            }
            if (y > 0)
                if (checkSquare(pieces, x, y - 1).isEmpty)
                    count++;
            if (x < 7 && y > 0)
                if (checkSquare(pieces, x + 1, y - 1).isBlack) {
                    count++;
                }
            if (x > 0 && y > 0)
                if (checkSquare(pieces, x - 1, y - 1).isBlack) {
                    count++;
                }
            int[] canMoveTo = new int[count];
            int index = 0;
            if (y == 6) {
                if (checkSquare(pieces, x, y - 1).isEmpty) {
                    if (checkSquare(pieces, x, y - 2).isEmpty) {
                        canMoveTo[index] = this.x * 10 + (this.y - 2);
                        index++;
                    }
                }
            }
            if (y > 0)
                if (checkSquare(pieces, x, y - 1).isEmpty) {
                    canMoveTo[index] = this.x * 10 + (this.y - 1);
                    index++;
                }
            if (x < 7 && y > 0)
                if (checkSquare(pieces, x + 1, y - 1).isBlack) {
                    canMoveTo[index] = (x + 1) * 10 + (y - 1);
                    index++;
                }
            if (x > 0 && y > 0)
                if (checkSquare(pieces, x - 1, y - 1).isBlack) {
                    canMoveTo[index] = (x - 1) * 10 + (y - 1);
                    index++;
                }
            return canMoveTo;
        }
    }

    class BlackRook extends Piece {

        {
            try {
                this.image = ImageIO.read(new File("res/BlackRook.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public BlackRook(int x, int y) {
            this.x = x;
            this.y = y;
            isBlack = true;
            isRook = true;
        }

        @Override
        int[] canMoveTo(Piece[][] pieces) {
            return rookMove(pieces, this, isBlack);
        }
    }

    class WhiteRook extends Piece {

        {
            try {
                this.image = ImageIO.read(new File("res/WhiteRook.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public WhiteRook(int x, int y) {
            this.x = x;
            this.y = y;
            isRook = true;

        }

        @Override
        int[] canMoveTo(Piece[][] pieces) {
            return rookMove(pieces, this, isBlack);
        }
    }

    class BlackKnight extends Piece {

        {
            try {
                this.image = ImageIO.read(new File("res/BlackKnight.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public BlackKnight(int x, int y) {
            this.x = x;
            this.y = y;
            isBlack = true;
            isKnight =true;
        }

        @Override
        int[] canMoveTo(Piece[][] pieces) {
            return knightMove(pieces, this, isBlack);
        }
    }

    class WhiteKnight extends Piece {

        {
            try {
                this.image = ImageIO.read(new File("res/WhiteKnight.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public WhiteKnight(int x, int y) {
            this.x = x;
            this.y = y;
            isKnight =true;
        }

        @Override
        int[] canMoveTo(Piece[][] pieces) {
            return knightMove(pieces, this, isBlack);
        }

    }

    class BlackBishop extends Piece {

        {
            try {
                this.image = ImageIO.read(new File("res/BlackBishop.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public BlackBishop(int x, int y) {
            this.x = x;
            this.y = y;
            isBlack = true;
            isBishop = true;
        }

        @Override
        int[] canMoveTo(Piece[][] pieces) {
            return bishopMove(pieces, this, isBlack);
        }
    }

    class WhiteBishop extends Piece {

        {
            try {
                this.image = ImageIO.read(new File("res/WhiteBishop.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public WhiteBishop(int x, int y) {
            this.x = x;
            this.y = y;
            isBishop = true;
        }

        int[] canMoveTo(Piece[][] pieces) {
            return bishopMove(pieces, this, isBlack);
        }
    }

    class BlackKing extends Piece {

        {
            try {
                this.image = ImageIO.read(new File("res/BlackKing.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public BlackKing(int x, int y) {
            this.x = x;
            this.y = y;
            isBlack = true;
            isKing = true;
        }

        @Override
        int[] canMoveTo(Piece[][] pieces) {
            return kingMove(pieces, this, isBlack);
        }
    }

    class WhiteKing extends Piece {

        {
            try {
                this.image = ImageIO.read(new File("res/WhiteKing.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public WhiteKing(int x, int y) {
            this.x = x;
            this.y = y;
            isKing = true;
        }

        int[] canMoveTo(Piece[][] pieces) {
            return kingMove(pieces, this, isBlack);
        }
    }

    class BlackQueen extends Piece {

        {
            try {
                this.image = ImageIO.read(new File("res/BlackQueen.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public BlackQueen(int x, int y) {
            this.x = x;
            this.y = y;
            isBlack = true;
            isQueen =true;
        }

        int[] canMoveTo(Piece[][] pieces) {
            return queenMove(pieces, this, isBlack);
        }
    }

    class WhiteQueen extends Piece {

        {
            try {
                this.image = ImageIO.read(new File("res/WhiteQueen.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public WhiteQueen(int x, int y) {
            this.x = x;
            this.y = y;
            isQueen =true;
        }

        int[] canMoveTo(Piece[][] pieces) {
            return queenMove(pieces, this, isBlack);
        }
    }
}
