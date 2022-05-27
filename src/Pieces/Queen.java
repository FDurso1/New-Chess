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
  public char getID() {
    return 'q';
  }

  @Override
  public boolean isLegalMoveShape(String start, String end, boolean flipped) {

    boolean likeARook = (start.charAt(0) == end.charAt(0) ^ start.charAt(1) == end.charAt(1));

    int horDist = (int) start.charAt(0) - (int) end.charAt(0);
    int vertDist = (int) start.charAt(1) - (int) end.charAt(1);
    boolean likeABishop = Math.abs(horDist) - Math.abs(vertDist) == 0;

    return likeABishop || likeARook;
  }

  @Override
  public boolean isLegalCaptureShape(String start, String end, boolean flipped) {
    return isLegalMoveShape(start, end, flipped);
  }

  @Override
  public String getName() {
    return "Queen";
  }
}
