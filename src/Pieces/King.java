package Pieces;

public class King implements Piece {

  char color;
  boolean hasMoved;

  public King(char color) {
    this.color = color;
    hasMoved = false;
  }

  public String toString() {
    return new String(new char[] { color, 'k' });
  }

  @Override
  public char getColor() {
    return color;
  }

  @Override
  public char getID() {
    return 'k';
  }

  @Override
  public boolean isLegalMoveShape(String start, String end, boolean flipped) {
    boolean horMove = Math.abs((int) start.charAt(0) - (int) end.charAt(0)) == 1;
    boolean vertMove = Math.abs((int) start.charAt(1) - (int) end.charAt(1)) == 1;
    return horMove || vertMove;
  }

  @Override
  public boolean isLegalCaptureShape(String start, String end, boolean flipped) {
    return isLegalMoveShape(start, end, flipped);
  }

  @Override
  public String getName() {
    return "King";
  }

  public boolean getMoved() {
    return hasMoved;
  }

  public void setMoved() {
    hasMoved = true;
  }
}
