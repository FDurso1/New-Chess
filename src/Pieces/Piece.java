package Pieces;

public interface Piece {

  char color = ' ';
  char ID = ' ';

  String toString();
  char getColor();
  char getID();
  boolean isLegalMoveShape(String start, String end, boolean flipped);
  boolean isLegalCaptureShape(String start, String end, boolean flipped);
  String getName();

}
