package Pieces;

public class Empty implements Piece {

  public Empty() {

  }

  public String toString() {
    return "__";
  }

  @Override
  public char getColor() {
    return 0;
  }

  @Override
  public char getID() {
    return 0;
  }

  @Override
  public boolean isLegalMoveShape(String start, String end, boolean flipped) {
    return false;
  }

  @Override
  public boolean isLegalCaptureShape(String start, String end, boolean flipped) {
    return false;
  }

  @Override
  public String getName() {
    return null;
  }
}
