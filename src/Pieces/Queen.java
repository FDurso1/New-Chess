package Pieces;

public class Queen implements Piece {

  char color;

  public Queen(char color) {
    this.color = color;
  }

  public String toString() {
    return new String(new char[] { color, 'q' });
  }

  @Override
  public char getColor() {
    return color;
  }

  @Override
  public boolean isLegalMoveShape(int startRow, int startCol, int endRow, int endCol, boolean flipped) {
    boolean likeARook = (startRow == endRow ^ startCol == endCol);
    boolean likeABishop = Math.abs(startRow - endRow) - Math.abs(startCol - endCol) == 0;
    return likeABishop || likeARook;
  }

  @Override
  public boolean isLegalCaptureShape(int startRow, int startCol, int endRow, int endCol, boolean flipped) {
    return isLegalMoveShape(startRow, startCol, endRow, endCol, flipped);
  }

  @Override
  public String getName() {
    return "Queen";
  }
}
