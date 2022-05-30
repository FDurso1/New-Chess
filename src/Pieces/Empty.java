package Pieces;

public class Empty implements Piece {

  char color = 0;
  String name = "__";

  public Empty() {

  }

  public Empty(char color) {
    this.color = color;
  }

  public Empty(String showName) {
    name = showName;
  }

  public String toString() {
    return name;
  }

  @Override
  public char getColor() {
    return color;
  }

  @Override
  public boolean isLegalMoveShape(int startRow, int startCol, int endRow, int endCol, boolean flipped) {
    return false;
  }

  @Override
  public boolean isLegalCaptureShape(int startRow, int startCol, int endRow, int endCol, boolean flipped) {
    return false;
  }

  @Override
  public String getName() {
    return null;
  }
}
