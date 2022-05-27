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
  public char getID() {
    return 'b';
  }

  @Override
  public boolean isLegalMoveShape(String start, String end, boolean flipped) {
    int horDist = (int) start.charAt(0) - (int) end.charAt(0);
    int vertDist = (int) start.charAt(1) - (int) end.charAt(1);
    if (horDist == 0 || vertDist == 0) {
      return false;
    }
    return Math.abs(horDist) - Math.abs(vertDist) == 0;
  }

  @Override
  public boolean isLegalCaptureShape(String start, String end, boolean flipped) {
    return isLegalMoveShape(start, end, flipped);
  }

  @Override
  public String getName() {
    return "Bishop";
  }
}
