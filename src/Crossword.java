import java.util.ArrayList;
import java.util.List;

public class Crossword {
  private static final int WIDTH = 9;
  private static final int HEIGHT = 10;
  private List<String> equations;

  public Crossword(List<String> equations) {
    this.equations = equations;
  }

  public static Crossword parseCrossword(String[] crossword) {


    Crossword crosswordObj = new Crossword(null);
    return null;
  }

  private static String[] formatCrossword(String crossword) {
    String[] formattedCrossword = new String[HEIGHT];
    char[] crosswordArray = crossword.toCharArray();
    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; i < WIDTH; j++) {
        formattedCrossword[i] = formattedCrossword[i] + crosswordArray[j];
      }
    }

    return formattedCrossword;
  }
}
