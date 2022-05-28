package Pieces;

public interface Piece {

  char color = ' ';
  char ID = ' ';

  String toString();
  char getColor();
  boolean isLegalMoveShape(int startRow, int startCol, int endRow, int endCol, boolean flipped);
  boolean isLegalCaptureShape(int startRow, int startCol, int endRow, int endCol, boolean flipped);
  String getName();

}
