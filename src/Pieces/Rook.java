package Pieces;

public class Rook implements Piece {

  char color;
  boolean hasMoved;

  public Rook(char color, boolean hasMoved) {
    this.color = color;
    this.hasMoved = hasMoved;
  }

  public String toString() {
    return new String(new char[] { color, 'r' });
  }

  @Override
  public char getColor() {
    return color;
  }

  @Override
  public boolean isLegalMoveShape(int startRow, int startCol, int endRow, int endCol, boolean flipped) {
    return (startRow == endRow ^ startCol == endCol);
  }

  @Override
  public boolean isLegalCaptureShape(int startRow, int startCol, int endRow, int endCol, boolean flipped) {
    return isLegalMoveShape(startRow, startCol, endRow, endCol, flipped);
  }

  @Override
  public String getName() {
    return "Rook";
  }

  public boolean getMoved() {
    return hasMoved;
  }

  public void setMoved() {
    hasMoved = true;
  }
}
