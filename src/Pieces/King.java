package Pieces;

public class King implements Piece {

  char color;
  boolean hasMoved;

  public King(char color, boolean hasMoved) {
    this.color = color;
    this.hasMoved = hasMoved;
  }

  public String toString() {
    return new String(new char[] { color, 'k' });
  }

  @Override
  public char getColor() {
    return color;
  }

  @Override
  public boolean isLegalMoveShape(int startRow, int startCol, int endRow, int endCol, boolean flipped) {
    boolean horMove = Math.abs(startRow - endRow) <= 1 ;
    boolean vertMove = Math.abs(startCol - endCol) <= 1;
    return horMove && vertMove && (startRow - endRow + startCol - endCol) != 0;
  }

  @Override
  public boolean isLegalCaptureShape(int startRow, int startCol, int endRow, int endCol, boolean flipped) {
    return isLegalMoveShape(startRow, startCol, endRow, endCol, flipped);
  }

  @Override
  public String getName() {
    return "King";
  }

  public boolean hasMoved() {
    return hasMoved;
  }

  public void setMoved() {
    hasMoved = true;
  }
}
