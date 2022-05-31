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
  static Object[][] showBoard = new Object[24][8];
  static Object[][] saveBoard = new Object[8][8];

  static Scanner keyIn = new Scanner(System.in);

  static boolean flipped = false;
  static boolean flipActive = true;
  static int turn = 0;
  static int startRow;  //adjusted for -1 from having rows 1-8 but array rows 0-7
  static int startCol;  //adjusted for letters --> numbers
  static int endRow;
  static int endCol;
  static int attackRow;
  static int attackCol;
  static int pawnVal = 10, knightVal = 30, bishopVal = 30, queenVal = 90, rookVal = 50;
  static int lines = 0;
  static boolean turnSuccess = true;

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
    board[0][0] = new Rook('b', false);
    board[0][7] = new Rook('b', false);
    board[7][0] = new Rook('w', false);
    board[7][7] = new Rook('w', false);
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
    board[0][4] = new King('b', false);
    board[7][4] = new King('w', false);
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
    System.out.println();
    showDisplay();
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
    } else {
      return turn % 2 == 1 && piece.getColor() == 'b';
    }
  }

  public static String getNameOfPiece(int r, int c) {
    Piece piece = (Piece) board[r][c];
    return piece.getName();
  }

  public static boolean isInvalidInput(String input) {
    if (input.length() != 2) {
      System.out.println("Error, illegal input length");
      return true;
    } else if (input.charAt(1) - '0' > 8 || input.charAt(1) - '0' < 1) {
      System.out.println("Error, illegal input row number");
      return true;
    } else if (convertColumn(input.charAt(0)) == -1) {
      System.out.println("Error, illegal input column letter");
      return true;
    }
    return false;
  }

  public static void cleanEnPawns() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        lines++;

        Piece piece = (Piece) board[i][j];
        if (piece.getClass() == enPawn.class) {
          if (turn % 2 == 0 && piece.getColor() == 'w') {
            board[i][j] = new Empty();
          } else if (turn % 2 == 1 && piece.getColor() == 'b') {
            board[i][j] = new Empty();
          }
        }
        else if (piece.getClass() == Empty.class && piece.getName().equals("xx")) {
          board[i][j] = new Empty();
        }
      }
    }
  }

  public static void startTurn() {

    //cleanEnPawns();

    if (turn % 2 == 1) {
      AITurn(false);
    } else {

      System.out.println("Enter the coordinates, ex A1, or a piece you want to move");
      String input = keyIn.next();
      if (input.equalsIgnoreCase("flip")) {
        flipBoard();
        display();
        turnSuccess = false;
      }
      else if (isInvalidInput(input)) {
        turnSuccess = false;
      } else {
        startRow = input.charAt(1) - '0';
        startRow -= 1;
        if (flipped) {
          startRow = 7 - startRow;
        }
        System.out.println("TESTING: startRow: " + startRow);
        startCol = convertColumn(input.charAt(0));
        System.out.println("TESTING: startCol: " + startCol);

        if (!pieceSameAsCurTurn(startRow, startCol)) {
          System.out.println("Error, you do not have a piece at that location");
          turnSuccess = false;
        } else {
          continueTurn();
        }
      }
    }
  }

  public static boolean wayIsClear (int sr, int sc, int er, int ec) {
    //start row, start col, end row, end col
    if (sc == ec) {
      if (sr > er) {
        for (int i = sr-1; i > er; i--) {
          lines++;
          Piece piece = (Piece) board[i][sc];
          if (!(piece.getClass() == Empty.class)) {
            return false;
          }
        }
      } else {
        for (int i = er-1; i > sr; i--) {
          lines++;
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
          lines++;
          Piece piece = (Piece) board[sr][i];
          if (!(piece.getClass() == Empty.class)) {
            return false;
          }
        }
      } else {
        for (int i = ec-1; i > sc; i--) {
          lines++;
          Piece piece = (Piece) board[sr][i];
          if (!(piece.getClass() == Empty.class)) {
            return false;
          }
        }
      }
      return true;
    } else if (Math.abs(sr - er) == Math.abs(sc - ec)){ //diagonal movement
      return checkDiagonals(sr, sc, er, ec);
    } else {  //knight
      return true;
    }
  }

  public static boolean checkDiagonals(int sr, int sc, int er, int ec) {
  //  System.out.println("Checking diagonals");
  //  System.out.println("Going from " + sc + ", " + sr + " to " + ec + ", " + er);

    if (sr > er && sc > ec) {
   //   System.out.println("Start rows and cols greater");
      for (int i = 1; i < sr - er; i++) {
        lines++;
        Piece piece = (Piece) board[sr-i][sc-i];
        if (piece.getClass() != Empty.class) {
          //System.out.println("Piece blocking at " + c + ", " + r);
          return false;
        }
      }
    }

    if (sr > er && sc < ec) {
   //   System.out.println("Start rows greater");
      for (int i = 1; i < sr - er; i++) {
        lines++;

        Piece piece = (Piece) board[sr-i][sc+i];
        if (piece.getClass() != Empty.class) {
          //System.out.println("Piece blocking at " + c + ", " + r);
          return false;
        }
      }
    }

    if (sr < er && sc > ec) {
  //    System.out.println("Start cols greater");
      for (int i = 1; i < er - sr; i++) {
        lines++;

        Piece piece = (Piece) board[sr+i][sc-i];
        if (piece.getClass() != Empty.class) {
          //System.out.println("Piece blocking at " + c + ", " + r);
          return false;
        }
      }
    }

    if (sr < er && sc < ec) {
  //    System.out.println("End rows and cols greater");
      for (int i = 1; i < er - sr; i++) {
        lines++;

        Piece piece = (Piece) board[sr+i][sc+i];
        if (piece.getClass() != Empty.class) {
          //System.out.println("Piece blocking at " + c + ", " + r);
          return false;
        }
      }
    }
    return true;
  }

  public static boolean tryCastle() {
    Piece piece = (Piece) board[startRow][startCol];
    if (piece.getClass() != King.class) {
      return false;
    }
    return Math.abs(startCol - endCol) == 2 && startRow == endRow;
  }

  public static boolean legalCastle() {

//    System.out.println("1");
//    display();

  King king = (King) board[startRow][startCol];

  int direction = -1;
  if (startCol == endCol+2) {
    direction = 1;
  }

  Piece rPiece;

  if (direction == 1) {
    rPiece = (Piece) board[startRow][0];
  } else {
    rPiece = (Piece) board[startRow][7];
  }

  if (rPiece.getClass() != Rook.class) {
    System.out.println("Rook not available for castle");
    return false;
  }
  Rook rook = (Rook) rPiece;
  if (king.hasMoved() || rook.hasMoved()) {
    System.out.println("Either King or Rook has already moved");
    return false;
  }
  if (getCheckingPiece() != null) {
    System.out.println("Cannot castle out of check");
   return false;
  }
//      System.out.println("2");
//      display();

  if (!wayIsClear(startRow, startCol, endRow, endCol)) {
    System.out.println("There are pieces in the way of your castle");
    return false;
  }
//      System.out.println("3");
//      display();

  board[startRow][startCol] = new Empty();
  board[startRow][startCol - direction] = new King(king.getColor(), false);
  Piece piece = getCheckingPiece();

//      System.out.println("4");
//      display();

  if (piece != null) {
    System.out.println("An enemy " + piece.getName() + " is preventing your castle");
    board[startRow][startCol] = new King(king.getColor(), false);
    board[startRow][startCol - direction] = new Empty();
    System.out.println("You cannot castle through a check");

//        System.out.println("5");
//        display();
      return false;
    }
    board[startRow][startCol - direction] = new Empty();
    board[startRow][startCol - 2 * direction] = new King(king.getColor(), true);
    if (getCheckingPiece() != null) {
      board[startRow][startCol] = new King(king.getColor(), false);
      board[startRow][startCol - 2 * direction] = new Empty();
      System.out.println("You cannot castle into a check");
      return false;
    }

    if (direction == 1) {
      board[startRow][0] = new Empty();
    } else {
      board[startRow][7] = new Empty();
    }
    board[startRow][startCol - direction] = new Rook(rook.getColor(), true);
    return true;
  }

  public static void continueTurn() {
    System.out.println("You have selected a " + getNameOfPiece(startRow, startCol) + ".");
    System.out.println("Please select a board location, ex A3, to move it to");
    String input = keyIn.next();
    if (isInvalidInput(input)) {
      System.out.println("Disregarding previous piece selection");
      turnSuccess = false;
    }
    endCol = convertColumn(input.charAt(0));
    endRow = input.charAt(1) - '0';
    endRow -= 1;
    if (flipped) {
      endRow = 7 - endRow;
    }
    System.out.println("TESTING: endRow: " + endRow);
    System.out.println("TESTING: endCol: " + endCol);

    if (pieceSameAsCurTurn(endRow, endCol)) {
      System.out.println("Error, you cannot move onto your own piece.");
      System.out.println("Disregarding previous piece selection");
      turnSuccess = false;
    } else if (tryCastle()) {
      if (!legalCastle()) {
        turnSuccess = false;
      } //else, legal castle, skip ahead to end-turn calls
    } else {
      Piece curPiece = (Piece) board[startRow][startCol];
      Piece curEndPiece = (Piece) board[endRow][endCol];

      if (!wayIsClear(startRow, startCol, endRow, endCol)) {
        System.out.println("There is a piece blocking that movement. Disregarding previous piece selection.");
        turnSuccess = false;
      } else {
        if (curEndPiece.getClass() == Empty.class) {    //not a capture
          if (!curPiece.isLegalMoveShape(startRow, startCol, endRow, endCol, flipped)) {
            System.out.println("Illegal move shape. Disregarding previous piece selection");
            turnSuccess = false;
          }
        } else {      //capture
          if (!curPiece.isLegalCaptureShape(startRow, startCol, endRow, endCol, flipped)) {
            System.out.println("Illegal capture shape. Disregarding previous piece selection");
            turnSuccess = false;
          }
        }
        if (turnSuccess) {
          movePiece(startRow, startCol, endRow, endCol);
          Piece dangerPiece = getCheckingPiece();
          if (dangerPiece != null) {
            System.out.println("This move leaves your king in threatened by an enemy " + dangerPiece.getName() + ". " +
                    "Disregarding previous piece selection");
            board[startRow][startCol] = curPiece;
            board[endRow][endCol] = curEndPiece;
            turnSuccess = false;
          }
        }
      }
    }
  }

  /*
  Create an invisible allied piece when en-passant is possible for your opponent's next turn.
  These pieces will be cleaned up at the start of your next turn.
   */
  public static void prepEnPassant(int sr, int sc, int er, int ec) {
    if (Math.abs(sr-er) == 2 && sc == ec) {
      Piece piece = (Piece) board[er][ec];
      if (piece.getClass() == Pawn.class) {
        if (ec > 0) {
          Piece toLeft = (Piece) board[er][ec-1];
          if (toLeft.getClass() == Pawn.class && !pieceSameAsCurTurn(er, ec-1)) {
            if (er < sr) {
              board[er+1][sc] = new enPawn(piece.getColor());
            } else {
              board[er-1][sc] = new enPawn(piece.getColor());
            }
          } //else, there is no enemy pawn to the left which can take advantage
        }
        if (ec < 7) {
          Piece toRight = (Piece) board[er][ec+1];
          if (toRight.getClass() == Pawn.class && !pieceSameAsCurTurn(er, ec+1)) {
            if (er < sr) {
              board[er+1][sc] = new enPawn(piece.getColor());
            } else {
              board[er-1][sc] = new enPawn(piece.getColor());
            }
          } //else, there is no enemy pawn to the right which can take advantage
        }
      } //else, a pawn was not moved in this way
    } //else, a not the right move shape for a pawn double jump
  }

  public static void movePiece(int sr, int sc, int er, int ec) {

//    System.out.println("TESTING: movepiece startRow: " + sr);
//    System.out.println("TESTING: movepiece startCol: " + sc);
//    System.out.println("TESTING: movepiece endRow: " + er);
//    System.out.println("TESTING: movepiece endCol: " + ec);

    Piece curPiece = (Piece) board[sr][sc];
    if (curPiece.getClass() == Rook.class) {
      Rook curRook = (Rook) board[sr][sc];
      curRook.setMoved();   //castling no longer available for this rook
      board[er][ec] = curRook;

    } else if (curPiece.getClass() == King.class) {
      King curKing = (King) board[sr][sc];
      curKing.setMoved();   //castling no longer available for this king
      board[er][ec] = curKing;
    } else {
      Piece piece = (Piece) board[er][ec];
      if (piece.getClass() == enPawn.class) { //when an enPawn is captured, remove the actual pawn
        board[sr][ec] = new Empty();
      }
      board[er][ec] = curPiece;
    }
    board[sr][sc] = new Empty();
  }

  public static int[] findCurKing() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        lines++;

        Piece piece = (Piece) board[i][j];
        if (piece.getClass() == King.class && piece.getColor() == 'w' && turn%2 == 0) {
          return new int[]{i, j};
        } else if (piece.getClass() == King.class && piece.getColor() == 'b' && turn%2 == 1) {
          return new int[]{i, j};
        }
      }
    }
    throw new RuntimeException();
  }

  public static Piece getCheckingPiece() {
    int[] coords = findCurKing();
    int kingRow = coords[0];
    int kingCol = coords[1];
  //  System.out.println("King coords: " + kingCol + ", " + kingRow);
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        lines++;

        Piece piece = (Piece) board[i][j];
        if (piece.getColor() == 'b' && turn % 2 == 0 || piece.getColor() == 'w' && turn % 2 == 1) {
   //       System.out.println("Enemy " + piece.getName() + " detected");
          if (piece.isLegalCaptureShape(i, j, kingRow, kingCol, flipped) && wayIsClear(i, j, kingRow, kingCol)) {
            System.out.println("An enemy " + piece.getName() + " on " + getCoordName(i, j) + " is checking your king");
            attackRow = i;
            attackCol = j;
            return piece;
          }
        }
      }
    }
    return null;
  }

/*  public static boolean isCheckMate() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Piece piece = (Piece) board[i][j];
        if (piece.getColor() == 'w' && turn % 2 == 0 || piece.getColor() == 'b' && turn % 2 == 1) {

          //first try to capture the attacking piece
          if (piece.isLegalCaptureShape(i, j, attackRow, attackCol, flipped) &&
                  wayIsClear(i, j, attackRow, attackCol)) {
            Piece endPiece = (Piece) board[attackRow][attackCol];
            movePiece(i, j, attackRow, attackCol);
            Piece dangerPiece = getCheckingPiece();
            if (dangerPiece == null) {          //safe way out of check found, undo the move
              board[i][j] = piece;
              board[endRow][endCol] = endPiece;
              return false;
            }
            board[i][j] = piece;  //not a safe way out of check, still undo the move
            board[endRow][endCol] = endPiece;
          }
          //current piece cannot capture the checking piece
        }
      }
    }

    return checkStaleMate();
  } */

  public static boolean checkStaleMate() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        lines++;

        Piece piece = (Piece) board[i][j];
        if (piece.getColor() == 'w' && turn % 2 == 0 || piece.getColor() == 'b' && turn % 2 == 1) {

          for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
              lines++;

              Piece endPiece = (Piece) board[r][c];

              if (piece.getColor() == enemyColor(endPiece.getColor()) && piece.isLegalMoveShape(i, j, r, c, flipped) && wayIsClear(i, j, r, c)) {
                movePiece(i, j, r, c);
                Piece dangerPiece = getCheckingPiece();
                if (dangerPiece == null) {          //safe way out of check found, undo the move
                  board[i][j] = piece;
                  board[r][c] = endPiece;
                  return false;
                }
                board[i][j] = piece;  //not a safe way out of check, still undo the move
                board[r][c] = endPiece;
              }
              else if (endPiece.getClass() == Empty.class && piece.isLegalMoveShape(i, j, r, c, flipped) && wayIsClear(i, j, r, c)) {
                movePiece(i, j, r, c);
                Piece dangerPiece = getCheckingPiece();
                if (dangerPiece == null) {          //safe move found, undo the move
                  board[i][j] = piece;
                  board[r][c] = endPiece;
                  return false;
                }
                board[i][j] = piece;  //not a safe move, still undo the move
                board[r][c] = endPiece;
              }
            }
          }
        }
      }
    }
    return true;
  }

  public static void promotePawns() {

    String[] values = {"queen","rook","bishop","knight"};

    for (int i = 0; i < 8; i++) {
      lines++;

      Piece piece = (Piece) board[0][i];
      Piece oPiece = (Piece) board[7][i];
     // System.out.println("Piece class: " + piece.getClass());
     // System.out.println("oPiece class: " + oPiece.getClass());

      if (piece.getClass() == Pawn.class || oPiece.getClass() == Pawn.class) {
        System.out.println("What would you like to promote your pawn to (ex, Queen)?");
        String input = keyIn.next();
        while(!Arrays.asList(values).contains(input.toLowerCase())) {
          System.out.println("That is not a legal piece selection. Please select again");
          input = keyIn.next();
        }
        if (turn % 2 == 0) {
          if (input.equalsIgnoreCase("queen")) {
            board[0][i] = new Queen('w');
          } else if (input.equalsIgnoreCase("rook")) {
            board[0][i] = new Rook('w', true);
          } else if (input.equalsIgnoreCase("bishop")) {
            board[0][i] = new Bishop('w');
          } else {
            board[0][i] = new Knight('w');
          }
        } else {
          if (input.equalsIgnoreCase("queen")) {
            board[7][i] = new Queen('b');
          } else if (input.equalsIgnoreCase("rook")) {
            board[7][i] = new Rook('b', true);
          } else if (input.equalsIgnoreCase("bishop")) {
            board[7][i] = new Bishop('b');
          } else {
            board[7][i] = new Knight('b');
          }
        }
      }
    }
  }

  public static void flipBoard() {
    flipped = !flipped;
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 8; j++) {
        Piece pieceOne = (Piece) board[i][j];
        Piece pieceTwo = (Piece) board[7-i][7-j];
        board[i][j] = pieceTwo;
        board[7-i][7-j] = pieceOne;
      }
    }
  }

  public static String getCoordName(int i, int j) {
    return new String(new char[] { (char) (j + 'A'), (char) (i + '1'), });
  }

  public static void showDisplay() {
    updateShowBoard();
    int counter = 0;
    int showRowNumber = 0;
    System.out.print("  _______________________________________________________________________________________________________________");
    for (int r = 0; r < 24; r++) {
      counter = counter+1;
      System.out.print("\n");
      if (r % 3 == 1) {
        showRowNumber += 1;
        System.out.print(showRowNumber);
      }
      else {
        System.out.print(" ");
      }

      for (int c = 0; c < 8; c++) {
        System.out.print("|");
        System.out.print(showBoard[r][c]);
        System.out.print("|");
      }
      if (counter == 3) {
        counter = 0;
        System.out.print("\n");
        System.out.print(" ----------------------------------------------------------------------------------------------------------------");
      }
    }
    System.out.println();
    System.out.println("        A            B             C             D              E             F              G            H");
  }
  public static void updateShowBoard() {
    for (int r = 0; r < 8; r++) {
      int sr = 3*r;
      for (int c = 0; c < 8; c++) {
        lines++;

        Piece piece = (Piece) board[r][c];

        if (piece.getClass() == Empty.class && piece.getName().equals("__")) {
          showBoard[sr][c] = new Empty("            ");
          showBoard[sr+1][c] = new Empty("            ");
          showBoard[sr+2][c] = new Empty("            ");
        } else if (piece.getClass() == Empty.class && !piece.getName().equals("__")) {
          showBoard[sr][c] =   new Empty("  \\     /   ");
          showBoard[sr+1][c] = new Empty("    \\/      ");
          showBoard[sr+2][c] = new Empty("   /  \\     ");
        } else if (piece.getClass() == enPawn.class && piece.getColor() == 'w') {
          showBoard[sr][c] = new Empty("            ");
          showBoard[sr+1][c] = new Empty(" en pa here ");
          showBoard[sr+2][c] = new Empty(" |~~~~~~~~| ");
        } else if (piece.getClass() == enPawn.class && piece.getColor() == 'b') {
          showBoard[sr][c] = new Empty("            ");
          showBoard[sr+1][c] = new Empty(" en pa here ");
          showBoard[sr+2][c] = new Empty(" |########| ");
        }
        else if (piece.getClass() == Pawn.class && piece.getColor() == 'w') {
          showBoard[sr][c] = new Empty(" |''''''''| ");
          showBoard[sr+1][c] = new Empty(" |White Pw| ");
          showBoard[sr+2][c] = new Empty(" |~~~~~~~~| ");
        }
        else if (piece.getClass() == Pawn.class && piece.getColor() == 'b') {
          showBoard[sr][c] = new Empty(" |''''''''| ");
          showBoard[sr+1][c] = new Empty(" |Black Pw| ");
          showBoard[sr+2][c] = new Empty(" |########| ");
        }
        else if (piece.getClass() == Rook.class && piece.getColor() == 'w') {
          showBoard[sr][c] = new Empty(" |=| || |=| ");
          showBoard[sr+1][c] = new Empty(" |White Rk| ");
          showBoard[sr+2][c] = new Empty(" |~~~~~~~~| ");
        }
        else if (piece.getClass() == Rook.class && piece.getColor() == 'b') {
          showBoard[sr][c] = new Empty(" |=| || |=| ");
          showBoard[sr+1][c] = new Empty(" |Black Rk| ");
          showBoard[sr+2][c] = new Empty(" |########| ");
        }
        else if (piece.getClass() == Knight.class && piece.getColor() == 'w') {
          showBoard[sr][c] = new Empty("    /**/    ");
          showBoard[sr+1][c] = new Empty(" |White Nt| ");
          showBoard[sr+2][c] = new Empty(" |~~~~~~~~| ");
        }
        else if (piece.getClass() == Knight.class && piece.getColor() == 'b') {
          showBoard[sr][c] = new Empty("    /**/    ");
          showBoard[sr+1][c] = new Empty(" |Black Nt| ");
          showBoard[sr+2][c] = new Empty(" |########| ");
        }
        else if (piece.getClass() == Bishop.class && piece.getColor() == 'w') {
          showBoard[sr][c] = new Empty("     /\\     ");
          showBoard[sr+1][c] = new Empty(" |White Bi| ");
          showBoard[sr+2][c] = new Empty(" |~~~~~~~~| ");
        }
        else if (piece.getClass() == Bishop.class && piece.getColor() == 'b') {
          showBoard[sr][c] = new Empty("     /\\     ");
          showBoard[sr+1][c] = new Empty(" |Black Bi| ");
          showBoard[sr+2][c] = new Empty(" |########| ");
        }
        else if (piece.getClass() == Queen.class && piece.getColor() == 'w') {
          showBoard[sr][c] = new Empty("   /\\/\\/\\   ");
          showBoard[sr+1][c] = new Empty(" |White Qu| ");
          showBoard[sr+2][c] = new Empty(" |~~~~~~~~| ");
        }
        else if (piece.getClass() == King.class && piece.getColor() == 'w') {
          showBoard[sr][c] = new Empty("  /\\ || /\\  ");
          showBoard[sr+1][c] = new Empty(" |White Kg| ");
          showBoard[sr+2][c] = new Empty(" |~~~~~~~~| ");
        }
        else if (piece.getClass() == Queen.class && piece.getColor() == 'b') {
          showBoard[sr][c] = new Empty("   /\\/\\/\\   ");
          showBoard[sr+1][c] = new Empty(" |Black Qu| ");
          showBoard[sr+2][c] = new Empty(" |########| ");
        }
        else if (piece.getClass() == King.class && piece.getColor() == 'b') {
          showBoard[sr][c] = new Empty("  /\\ || /\\  ");
          showBoard[sr+1][c] = new Empty(" |Black Kg| ");
          showBoard[sr+2][c] = new Empty(" |########| ");
        }
        else {
          showBoard[sr+1][c] = new Empty ("    ERROR   ");
        }
      }
    }
  }
  /*
  Solution to "AI"
  Different values for all the pieces, optimize board value, play with values over time
  Adjust by avoiding captures of greater value pieces by lesser value pieces
  Allow captures of lesser's by greater's if you can capture back.
   ^ how far down the recursion hole to go?
   slight random adjustments to avoid repeating play patterns
   */

  public static int getBaseBoardValue() {

    turn++;
    if (getCheckingPiece() != null && checkStaleMate()) {
      turn--;
      System.out.println("   This move will result in checkmate!");
      return 999999; //this is a checkmate threat, avoid at all costs
    }
    turn--;

    int sum = 0;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        lines++;

        Piece piece = (Piece) board[i][j];
        int mod = 1;
        if (piece.getColor() == 'b') {
          mod = -1;
        }
        if (piece.getClass() == Pawn.class) {
          sum += pawnVal * mod;
        } else if (piece.getClass() == Bishop.class) {
          sum += bishopVal * mod;
        } else if (piece.getClass() == Rook.class) {
          sum += rookVal * mod;
        } else if (piece.getClass() == Knight.class) {
          sum += knightVal * mod;
        } else if (piece.getClass() == Queen.class) {
          sum += queenVal * mod;
        }
      }
    }
    return sum;
  }

  public static int tryAndReport(int i, int j, int r, int c, Object[][] board, boolean thinking) {
    int starting = getBaseBoardValue();

    Piece startPiece = (Piece) board[i][j];
    Piece endPiece = (Piece) board[r][c];
    board[r][c] = startPiece;
    board[i][j] = new Empty();
    int boardVal;
    if (getCheckingPiece() != null) {
      boardVal = Integer.MIN_VALUE;
    } else if (!thinking){
      boardVal = recursionNightmare();
    } else {
      boardVal = getBaseBoardValue();
    }

    if (boardVal > starting) {
      System.out.println("New best discovered here");
     // display();
    }


    board[i][j] = startPiece;
    board[r][c] = endPiece;
    return boardVal;
  }

  public static char enemyColor(char c) {
    if (c == 'w') {
      return 'b';
    }
    return 'w';
  }

  /*
  Cannot castle, will fail when attempting en passant
   */
  public static int AITurn(boolean thinking) {

    if (thinking) {
      System.out.println("Thinking ahead");
     // showDisplay();
      System.out.println();
    }


    int bestStartRow = -1, bestStartCol = -1, bestEndRow = -1, bestEndCol = -1;
    int bestBoardVal = Integer.MIN_VALUE + 5; //+5 to not have illegal moves be equal value
    int mod = 1;
    if (turn % 2 == 1) {
      mod = -1;
    }

    Random rand = new Random(10);

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        lines++;

        Piece piece = (Piece) board[i][j];
        if (piece.getColor() == 'w' && turn % 2 == 0 || piece.getColor() == 'b' && turn % 2 == 1) {
          for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
              lines++;

              Piece endPiece = (Piece) board[r][c];
              if (piece.isLegalCaptureShape(i, j, r, c, flipped) && wayIsClear(i, j, r, c) && endPiece.getColor() == enemyColor(piece.getColor())) {

                if (!thinking) {
                  System.out.println();
                  System.out.println("CONSIDERING THE CAPTURE " + piece.getColor() + " " + piece.getName() + " TO " + endPiece.getColor() + " " + endPiece.getName());
                } else {
                  System.out.println("Considering the capture " + piece.getColor() + " " + piece.getName() + " to " + endPiece.getColor() + " " + endPiece.getName());
                }

                int newSum = tryAndReport(i, j, r, c, board, thinking) * mod;
                int maybe = rand.nextInt(7);
                if (!thinking && maybe == 2) {
                  newSum += rand.nextInt(2);
                }

                if (newSum > bestBoardVal) {
                  bestStartRow = i;
                  bestStartCol = j;
                  bestEndRow = r;
                  bestEndCol = c;
                  bestBoardVal = newSum;

                  System.out.println("New best found after capture " + getCoordName(i, j) + " to " + getCoordName(r, c));

                  if (!thinking) {
                    System.out.println(piece.getName() + " " + getCoordName(i, j) + " TO " + getCoordName(r, c) + " (capture) IS THE CURRENT BEST MOVE with a board value of " + bestBoardVal);
                  } else {
                    System.out.println("Best response board value: " + bestBoardVal);
                  }

                }
              } else if (piece.isLegalMoveShape(i, j, r, c, flipped) && wayIsClear(i, j, r, c) && endPiece.getClass() == Empty.class) {

                if (!thinking) {
                  System.out.println();
                  System.out.println("CONSIDERING THE MOVE " + piece.getName() + " " + getCoordName(i, j) + " TO " + getCoordName(r, c));
                } else {
                  System.out.println("Considering the move " + piece.getName() + " " + getCoordName(i, j) + " to " + getCoordName(r, c));
                }

                int newSum = tryAndReport(i, j, r, c, board, thinking) * mod;
                int maybe = rand.nextInt(7);
                if (!thinking && maybe == 2) {
                  newSum += rand.nextInt(2);
                }

                //encourage advancing pieces a little bit
                if (!thinking && !flipped) {
                  if (turn % 2 == 0 && i > r) {
                    newSum += 1;
                  } else if (turn % 2 == 1 && i < r) {
                    System.out.println("Giving this move a bit of a boost");
                    newSum += 1;
                  }
                } else if (!thinking){
                  if (turn % 2 == 1 && i < r) {
                    newSum += 1;
                  } else if (turn % 2 == 0 && i > r) {
                    newSum += 1;
                  }
                }

                if (newSum > bestBoardVal) {
                  bestStartRow = i;
                  bestStartCol = j;
                  bestEndRow = r;
                  bestEndCol = c;
                  bestBoardVal = newSum;

                  System.out.println("New best found: " + getCoordName(i, j) + " to " + getCoordName(r, c));

                  if (!thinking) {
                    System.out.println(piece.getName() + " " + getCoordName(i, j) + " TO " + getCoordName(r, c) + " IS THE CURRENT BEST MOVE with a board value of " + bestBoardVal);
                  } else {
                    System.out.println("Best response board value: " + bestBoardVal);
                  }

                }
              }
            }
          }
        }
      }
    }
    if (bestStartCol == -1) {
      System.out.println("No legal moves found!");
      if (!thinking) {
        System.out.println("GAME OVER!");
        System.exit(0);
      }
    } else if (!thinking){
      Piece startPiece = (Piece) board[bestStartRow][bestStartCol];
      board[bestEndRow][bestEndCol] = startPiece;
      board[bestStartRow][bestStartCol] = new Empty("xx");
    //  turn++;
    //  display();
    } else {
      return bestBoardVal;
    }
    return 0; //indicates success
  }


//  public static void copyBoard() {
//    for (int i = 0; i < 8; i++) {
//      for (int j = 0; j < 8; j++) {
//        saveBoard[i][j] = board[i][j];
//      }
//    }
//  }

  /*
  For every move, will the resulting response result in a board state worse than
  the existing board state (unfavorable capture-back). In all cases, return the
  board value of the "optimal" response to the attempted move rather than the board
  value after the move itself. This "thinking ahead" will *not also* think ahead.
   */
  public static int recursionNightmare() {
    System.out.println("Recursion nightmare");
   // display();
   // copyBoard();
    turn++;
    int value = AITurn(true);
    turn--;
    return value;
  }

  public static void main(String[] args) {
    setBoard();

    while (!checkStaleMate()) {
      display();
      System.out.println();
      System.out.println("   Turn " + turn);
      System.out.println("   Lines: " + lines);
      System.out.println();
      cleanEnPawns();

      do {
        turnSuccess = true;
        startTurn();
      } while (!turnSuccess);


      promotePawns();
      prepEnPassant(startRow, startCol, endRow, endCol);
      turn++;
    }
    if (getCheckingPiece() == null) {
      System.out.println("The game is a draw!");
    } else {
      System.out.println("The game is OVER, you are in CHECKMATE!");
    }
    System.exit(0);
  }

}
