package Pieces;

public class Knight implements Piece {

  char color;

  public Knight(char color) {
    this.color = color;
  }

  public String toString() {
    return new String(new char[] { color, 'n' });
  }

  @Override
  public char getColor() {
    return color;
  }

  @Override
  public boolean isLegalMoveShape(int startRow, int startCol, int endRow, int endCol, boolean flipped) {
    if (Math.abs(startRow - endRow) == 1) {
      return (Math.abs(startCol - endCol) == 2);
    } else if (Math.abs(startCol - endCol) == 1) {
      return (Math.abs(startRow - endRow) == 2);
    }
    return false;
  }

  @Override
  public boolean isLegalCaptureShape(int startRow, int startCol, int endRow, int endCol, boolean flipped) {
    return isLegalMoveShape(startRow, startCol, endRow, endCol, flipped);
  }

  @Override
  public String getName() {
    return "Knight";
  }
}
