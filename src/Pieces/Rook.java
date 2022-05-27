package Pieces;

public class Rook implements Piece {

  char color;
  boolean hasMoved;

  public Rook(char color) {
    this.color = color;
    hasMoved = false;
  }

  public String toString() {
    return new String(new char[] { color, 'r' });
  }

  @Override
  public char getColor() {
    return color;
  }

  @Override
  public char getID() {
    return 'r';
  }

  @Override
  public boolean isLegalMoveShape(String start, String end, boolean flipped) {
    return (start.charAt(0) == end.charAt(0) ^ start.charAt(1) == end.charAt(1));
  }

  @Override
  public boolean isLegalCaptureShape(String start, String end, boolean flipped) {
    return isLegalMoveShape(start, end, flipped);
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
