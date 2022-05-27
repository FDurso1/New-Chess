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
  public char getID() {
    return 'n';
  }

  @Override
  public boolean isLegalMoveShape(String start, String end, boolean flipped) {
    if (Math.abs((int) start.charAt(0) - (int) end.charAt(0)) == 1) {
      return (Math.abs((int) start.charAt(1) - (int) end.charAt(1)) == 2);
    } else if (Math.abs((int) start.charAt(0) - (int) end.charAt(0)) == 2) {
      return (Math.abs((int) start.charAt(1) - (int) end.charAt(1)) == 1);
    }
    return false;
  }

  @Override
  public boolean isLegalCaptureShape(String start, String end, boolean flipped) {
    return isLegalMoveShape(start, end, flipped);
  }

  @Override
  public String getName() {
    return "Knight";
  }
}
