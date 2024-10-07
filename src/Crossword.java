import java.util.ArrayList;
import java.util.List;
import com.google.common.annotations.VisibleForTesting;

public class Crossword {
  private static final int WIDTH = 9;
  private static final int HEIGHT = 11;
  private List<String> equations;
  private String[] crossword = new String[HEIGHT];

  public Crossword(List<String> equations) {
    this.equations = equations;
  }

  public static Crossword parseCrossword(String[] crossword) {
    String regex = "([0-9_](\\+|\\-|\\*|\\/)[0-9_]=[0-9_])";
    parseHorizontalEquations(crossword);
    parseVerticalEquations(crossword);

    Crossword crosswordObj = new Crossword(null);
    return null;
  }

  private static List<String> parseVerticalEquations(String[] crossword) {
    String[] wordChars = new String[WIDTH];
    int variables = 1;
    for (String word : crossword) {
      wordChars = word.split("");
      for (String wordChar : wordChars) {
        if (wordChar == "_") {
          wordChar = "x_" + variables;
        }
      }
    }

    return null;
  }

  private static List<String> parseHorizontalEquations(String[] crossword) {
    return null;
  }


  private static String[] formatCrossword(String crossword) {
    String[] formattedCrossword = new String[HEIGHT];

    for (int i = 0; i < HEIGHT; i++) {
      int start = i * WIDTH;
      int end = start + WIDTH;
      formattedCrossword[i] = crossword.substring(start, end);
    }

    return formattedCrossword;
  }

  private void setCrossword(String crossword) {
    this.crossword = formatCrossword(crossword);
  }

}
