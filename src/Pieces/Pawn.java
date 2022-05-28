package Pieces;

public class Pawn implements Piece {

  char color;

  public Pawn(char color) {
    this.color = color;
  }

  public String toString() {
    return new String(new char[] { color, 'p' });
  }

  @Override
  public char getColor() {
    return color;
  }

  @Override
  public boolean isLegalMoveShape(int startRow, int startCol, int endRow, int endCol, boolean flipped) {
    if ((color == 'w' && !flipped) || (color == 'b' && flipped)) {
      System.out.println("not flipped board");
      if (startCol == endCol && startRow == 6 && endRow == 4) {
        System.out.println("double jump");
        return true;
      } else {
        return startCol == endCol && startRow == endRow + 1;
      }
    } else {
      if (startCol == endCol && startRow == 1 && endRow == 3) {
        return true;
      } else {
        return startCol == endCol && startRow == endRow - 1;
      }
    }
  }

  @Override
  public boolean isLegalCaptureShape(int startRow, int startCol, int endRow, int endCol, boolean flipped) {
    if ((color == 'w' && !flipped) || (color == 'b' && flipped)) {
      if (Math.abs(startCol - endCol) == 1) { //one column difference
        return startRow == endRow + 1;    //one row going 'up'
      }
    } else {
      if (Math.abs(startCol - endCol) == 1) { //one column difference
        return startRow == endRow - 1;    //one row going 'down'
      }
    }
    return false;
  }

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

    when it is not flipped, white pawns can move up (to smaller rows) and black down toward larger rows
    when it is flipped, the colors are reversed.
   */

  @Override
  public String getName() {
    return "Pawn";
  }
}
