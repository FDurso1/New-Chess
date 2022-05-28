package Pieces;

public class Bishop implements Piece{

  char color;

  public Bishop(char color) {
    this.color = color;
  }

  public String toString() {
    return new String(new char[] { color, 'b' });
  }

  @Override
  public char getColor() {
    return color;
  }

  @Override
  public boolean isLegalMoveShape(int startRow, int startCol, int endRow, int endCol, boolean flipped) {
    return Math.abs(startRow - endRow) - Math.abs(startCol - endCol) == 0;
  }

  @Override
  public boolean isLegalCaptureShape(int startRow, int startCol, int endRow, int endCol, boolean flipped) {
    return isLegalMoveShape(startRow, startCol, endRow, endCol, flipped);
  }

  @Override
  public String getName() {
    return "Bishop";
  }
}
