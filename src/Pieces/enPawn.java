package Pieces;

public class enPawn implements Piece {

  char color;

  public enPawn(char color) {
    this.color = color;
  }

  public String toString() {
    return "__";
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
