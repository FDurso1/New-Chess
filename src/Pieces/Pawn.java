package Pieces;

public class Pawn implements Piece {

  char color;

  public Pawn(char color) {
    this.color = color;
  }

  public String toString() {
    return new String(new char[] { color, 'p' });
  }

  @Override
  public char getColor() {
    return color;
  }

  @Override
  public char getID() {
    return 'p';
  }

     /*
  1[          ]       8[
  2[b b b     ]       7[
  3[          ]       6[
  4[          ]  flip 5[
  5[          ]  ---> 4[
  6[          ]       3[
  7[w w w     ]       2[
  8[          ]       1[
    a b c ... h         h g f e ... a

    when it is not flipped, white pawns can move up (to smaller rows) and black down toward larger rows
    when it is flipped, the colors are reversed.
   */

  @Override
  public boolean isLegalMoveShape(String start, String end, boolean flipped) {
  System.out.println("Start: " + start);
  System.out.println("End: " + end);

    if ((color == 'w' && !flipped) || (color == 'b' && flipped)) {
      System.out.println("here 1");
      if (start.charAt(0) == end.charAt(0) && start.charAt(1) == '7' && end.charAt(1) == '5') {
        System.out.println("double jump");
        return true;
      } else {
        return start.charAt(0) == end.charAt(0) && (int) start.charAt(1) == ((int) end.charAt(1) + 1);
      }
    } else {
      if (start.charAt(0) == end.charAt(0) && start.charAt(1) == '2' && end.charAt(1) == '4') {
        return true;
      } else {
        return start.charAt(0) == end.charAt(0) && (int) start.charAt(1) == ((int) end.charAt(1) - 1);
      }
    }
  }

  @Override
  public boolean isLegalCaptureShape(String start, String end, boolean flipped) {
    if ((color == 'w' && !flipped) || (color == 'b' && flipped)) {
      if (Math.abs((int) start.charAt(0) - (int) end.charAt(0)) == 1) { //one column difference
        return (int) start.charAt(1) == ((int) end.charAt(1) + 1);    //one row going 'up'
      }
    } else {
      if (Math.abs((int) start.charAt(0) - (int) end.charAt(0)) == 1) { //one column difference
        return (int) start.charAt(1) == ((int) end.charAt(1) - 1);    //one row going 'down'
      }
    }
    return false;
  }

  @Override
  public String getName() {
    return "Pawn";
  }
}
