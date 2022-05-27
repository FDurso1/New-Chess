import Pieces.*;

import java.util.*;

     /*
  1[          ]       8[
  2[b b b     ]       7[
  3[          ]       6[
  4[          ]  flip 5[
  5[          ]  ---> 4[
  6[          ]       3[
  7[w w w     ]       2[
  8[          ]       1[
    a b c ... h         h g f e ... a
    0 1 2 ... 7
*/

public class chess {

  static Object[][] board = new Object[8][8];
  static Scanner keyIn = new Scanner(System.in);

  static boolean flipped = false;
  static int turn = 0;
  static int startRow;  //adjusted for -1 from having rows 1-8 but array rows 0-7
  static int startCol;  //adjusted for letters --> numbers
  static int endRow;
  static int endCol;

  public static void setBoard() {

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        board[i][j] = new Empty();
      }
    }

      for (int i = 0; i < 8; i++) {
      board[6][i] = new Pawn('w');
      board[1][i] = new Pawn('b');
    }
    board[0][0] = new Rook('b');
    board[0][7] = new Rook('b');
    board[7][0] = new Rook('w');
    board[7][7] = new Rook('w');
    board[0][2] = new Bishop('b');
    board[0][5] = new Bishop('b');
    board[7][2] = new Bishop('w');
    board[7][5] = new Bishop('w');
    board[0][3] = new Queen('b');
    board[7][3] = new Queen('w');
    board[0][1] = new Knight('b');
    board[0][6] = new Knight('b');
    board[7][1] = new Knight('w');
    board[7][6] = new Knight('w');
    board[0][4] = new King('b');
    board[7][4] = new King('w');
  }

  public static void display() {
    System.out.print("  ______________________________");
    for (int r = 0; r < 8; r++) {

      if (!flipped) {
        System.out.print("\n" + (r+1));
      }
      else {
        System.out.print("\n" + (8-r));
      }

      for (int c = 0; c < 8; c++) {
        System.out.print("|");
        System.out.print(board[r][c]);
        System.out.print("|");
      }
    }
    System.out.println();
    System.out.println(" --------------------------------");
    if (!flipped) {
      System.out.println("  A   B   C   D   E   F   G   H");
      System.out.println("  0   1   2   3   4   5   6   7");
    }
    else {
      System.out.println("  H   G   F   E   D   C   B   A");
      System.out.println("  7   6   5   4   3   2   1   0");
    }
  }

  public static int convertColumn(char c) {
    if ((int) c >= 65 && (int) c <= 72) { //Capital letter used
      if (!flipped) {
        return (int) c - 65;
      } else {
        return 72 - (int) c;
      }
    } else if ((int) c >= 97 && (int) c <= 104) { //Capital letter used
      if (!flipped) {
        return (int) c - 97;
      } else {
        return 104 - (int) c;
      }
    }
    return -1;
  }

  public static boolean pieceSameAsCurTurn(int r, int c) {
    Piece piece = (Piece) board[r][c];
    System.out.println("TESTING: Piece toString: " + piece);

    if (turn % 2 == 0 && piece.getColor() == 'w') {
      return true;
    } else if (turn % 2 == 1 && piece.getColor() == 'b') {
      return true;
    }
    return false;
  }

  public static String getNameOfPiece(int r, int c) {
    Piece piece = (Piece) board[r][c];
    return piece.getName();
  }

  public static boolean validateInput(String input) {
    if (input.length() != 2) {
      System.out.println("Error, illegal input length");
      return false;
    } else if (input.charAt(1) - '0' > 8 || input.charAt(1) - '0' < 1) {
      System.out.println("Error, illegal input row number");
      return false;
    } else if (convertColumn(input.charAt(0)) == -1) {
      System.out.println("Error, illegal input column letter");
      return false;
    }
    return true;
  }

  public static void startTurn() {
    System.out.println("Enter the coordinates, ex A1, or a piece you want to move");
    String input = keyIn.next();

    if (!validateInput(input)) {
      startTurn();
    } else {
      startRow = input.charAt(1) - '0';
      startRow -= 1;
      System.out.println("TESTING: startRow: " + startRow);
      startCol = convertColumn(input.charAt(0));
      System.out.println("TESTING: startCol: " + startCol);

      if (!pieceSameAsCurTurn(startRow, startCol)) {
        System.out.println("Error, you do not have a piece at that location");
        startTurn();
      } else {
        continueTurn(input);
      }
    }
  }

  public static boolean wayIsClear (int sr, int sc, int er, int ec) {
    //start row, start col, end row, end col
    if (sc == ec) {
      if (sr > er) {
        for (int i = sr-1; i > er; i--) {
          Piece piece = (Piece) board[i][sc];
          if (!(piece.getClass() == Empty.class)) {
            return false;
          }
        }
      } else {
        for (int i = er-1; i > sr; i--) {
          Piece piece = (Piece) board[i][sc];
          if (!(piece.getClass() == Empty.class)) {
            return false;
          }
        }
      }
      return true;
    } else if (sr == er) {
      if (sc > ec) {
        for (int i = sc-1; i > ec; i--) {
          Piece piece = (Piece) board[sr][i];
          if (!(piece.getClass() == Empty.class)) {
            return false;
          }
        }
      } else {
        for (int i = ec-1; i > sc; i--) {
          Piece piece = (Piece) board[sr][i];
          if (!(piece.getClass() == Empty.class)) {
            return false;
          }
        }
      }
      return true;
    } else if (Math.abs(sr - er) == Math.abs(sc - ec)){ //diagonal movement
      return checkDiagonals(sr, sc, er, sc);
    } else {  //knight
      return true;
    }
  }

  public static boolean checkDiagonals(int sr, int sc, int er, int ec) {
    if (sr > er && sc > ec) {
      for (int r = sr-1; r > er; r--) {
        for (int c = sc-1; c > ec; c--) {
          Piece piece = (Piece) board[r][c];
          if (!(piece.getClass() == Empty.class)) {
            return false;
          }
        }
      }
    }

    if (sr > er && sc < ec) {
      for (int r = sr-1; r > er; r--) {
        for (int c = ec-1; c > sc; c--) {
          Piece piece = (Piece) board[r][c];
          if (!(piece.getClass() == Empty.class)) {
            return false;
          }
        }
      }
    }

    if (sr < er && sc > ec) {
      for (int r = er-1; r > sr; r--) {
        for (int c = sc-1; c > ec; c--) {
          Piece piece = (Piece) board[r][c];
          if (!(piece.getClass() == Empty.class)) {
            return false;
          }
        }
      }
    }

    if (sr < er && sc < ec) {
      for (int r = er-1; r > sr; r--) {
        for (int c = ec-1; c > sc; c--) {
          Piece piece = (Piece) board[r][c];
          if (!(piece.getClass() == Empty.class)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  public static void continueTurn(String prevInput) {
    System.out.println("You have selected a " + getNameOfPiece(startRow, startCol) + ".");
    System.out.println("Please select a board location, ex A3, to move it to");
    String input = keyIn.next();
    if (!validateInput(input)) {
      System.out.println("Disregarding previous piece selection");
      startTurn();
    }
    endCol = convertColumn(input.charAt(0));
    endRow = input.charAt(1) - '0';
    endRow -= 1;
    System.out.println("TESTING: endRow: " + endRow);
    System.out.println("TESTING: endCol: " + endCol);

    if (pieceSameAsCurTurn(endRow, endCol)) {
      System.out.println("Error, you cannot move onto your own piece.");
      System.out.println("Disregarding previous piece selection");
      startTurn();
    } else {
      Piece curPiece = (Piece) board[startRow][startCol];
      Piece curEndPiece = (Piece) board[endRow][endCol];

      if (curEndPiece.getClass() == Empty.class) {    //not a capture
        if (!curPiece.isLegalMoveShape(prevInput, input, flipped)) {
          System.out.println("Illegal move shape. Disregarding previous piece selection");
          startTurn();
        } else if (!wayIsClear(startRow, startCol, endRow, endCol)) {
          System.out.println("There is a piece blocking that movement. Disregarding previous piece selection.");
          startTurn();
        } else {
          movePiece(startRow, startCol, endRow, endCol);
        }
      } else {      //capture
        if (!curPiece.isLegalCaptureShape(prevInput, input, flipped)) {
          System.out.println("Illegal capture shape. Disregarding previous piece selection");
          startTurn();
        } else if (!wayIsClear(startRow, startCol, endRow, endCol)) {
          System.out.println("There is a piece blocking that movement. Disregarding previous piece selection.");
          startTurn();
        } else {
          movePiece(startRow, startCol, endRow, endCol);
        }
      }
    }
    turn++;
    display();
    startTurn();
  }

  public static void movePiece(int sr, int sc, int er, int ec) {

    System.out.println("TESTING: movepiece startRow: " + sr);
    System.out.println("TESTING: movepiece startCol: " + sc);
    System.out.println("TESTING: movepiece endRow: " + er);
    System.out.println("TESTING: movepiece endCol: " + ec);

    Piece curPiece = (Piece) board[sr][sc];
    board[sr][sc] = new Empty();
    board[er][ec] = curPiece;
    if (curPiece.getClass() == Rook.class) {
      Rook curRook = (Rook) board[sr][sc];
      curRook.setMoved();   //en passant no longer available for this rook
    } else if (curPiece.getClass() == King.class) {
      King curKing = (King) board[sr][sc];
      curKing.setMoved();   //en passant no longer available for this king
    }
  }

  public static void main(String[] args) {
    setBoard();
    display();
    startTurn();
  }

}
