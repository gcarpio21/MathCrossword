import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crossword {
  private static final int WIDTH = 9;
  private static final int HEIGHT = 11;
  private String[][] crossword = new String[HEIGHT][WIDTH];
  private List<String> equations = new ArrayList<>();

  public Crossword(String crossword) {
    formatCrossword(crossword);
  }

  public void parseCrossword() {
    String regex =
        "([0-9]|x_[1-9][0-9]*)(\\+|\\-|\\*|\\/)([0-9]|x_[1-9][0-9]*)=([0-9]|x_[1-9][0-9]*)";
    Pattern pattern = Pattern.compile(regex);
    replaceUnderscores();
    parseHorizontalEquations(pattern);
    parseVerticalEquations(pattern);

  }

  private void parseVerticalEquations(Pattern regex) {

    StringBuilder column;
    Matcher matcher;
    String equation;

    for (int colNo = 0; colNo < WIDTH; colNo++) {
      column = new StringBuilder();
      for (String[] symbol : this.crossword) {
        column.append(symbol[colNo]);
      }
      matcher = regex.matcher(column.toString());
      while (matcher.find()) {
        equation = matcher.group();
        this.equations.add(equation);
      }
    }
  }

  private void parseHorizontalEquations(Pattern regex) {

    StringBuilder row;
    Matcher matcher;
    String equation;

    for (int rowNo = 0; rowNo < HEIGHT; rowNo++) {
      row = new StringBuilder();
      for (String symbol : this.crossword[rowNo]) {
        row.append(symbol);
      }
      matcher = regex.matcher(row.toString());
      while (matcher.find()) {
        equation = matcher.group();
        this.equations.add(equation);
      }
    }
  }

  private void replaceUnderscores() {
    int variables = 1;
    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        if (this.crossword[i][j].equals("_")) {
          this.crossword[i][j] = "x_" + variables;
          variables++;
        }
      }
    }
  }

  private void formatCrossword(String crossword) {
    char[] crosswordChars = crossword.toCharArray();

    int charIndex = 0;
    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++, charIndex++) {
        this.crossword[i][j] = String.valueOf(crosswordChars[charIndex]);
      }
    }
  }

  public void print() {
    StringBuilder sb;
    String[] rows = new String[HEIGHT];
    for (int i = 0; i < HEIGHT; i++) {
      sb = new StringBuilder();
      for (int j = 0; j < WIDTH; j++) {
        sb.append(crossword[i][j]);
      }
      rows[i] = sb.toString();
      System.out.println(rows[i]);
    }
  }

  public String[][] getCrossword() {
    String[][] crosswordCopy = new String[HEIGHT][WIDTH];
    for (int i = 0; i < HEIGHT; i++) {
      System.arraycopy(this.crossword[i], 0, crosswordCopy[i], 0, WIDTH);
    }
    return crosswordCopy;
  }

  public List<String> getEquations() {
    return new ArrayList<>(this.equations);
  }
}
