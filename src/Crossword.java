import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crossword {
  private final int WIDTH = 9;
  private final int HEIGHT = 11;
  private String[][] crossword = new String[HEIGHT][WIDTH];
  private List<Equation> equations = new ArrayList<>();
  private Map<String, Integer> variables = new HashMap<>();

  public Crossword(String crossword) {
    formatCrossword(crossword);
  }

  public void parseCrossword() {
    String regex =
        "([0-9]|x_[1-9][0-9]*)(\\+|\\-|\\*|\\/)([0-9]|x_[1-9][0-9]*)=([0-9]|x_[1-9][0-9]*)";
    Pattern pattern = Pattern.compile(regex);
    parseVariables();
    addHorizontalEquations(pattern);
    addVerticalEquations(pattern);

  }

  private void addVerticalEquations(Pattern regex) {
    StringBuilder column;
    Matcher matcher;
    String equationString;
    Equation equation;

    for (int colNo = 0; colNo < WIDTH; colNo++) {
      column = new StringBuilder();
      for (String[] symbol : this.crossword) {
        column.append(symbol[colNo]);
      }
      matcher = regex.matcher(column.toString());
      while (matcher.find()) {
        equation = new Equation();
        equationString = matcher.group();
        equation.parseEquation(equationString);
        this.equations.add(equation);
      }
    }
  }

  private void addHorizontalEquations(Pattern regex) {

    StringBuilder row;
    Matcher matcher;
    String equationString;
    Equation equation;

    for (int rowNo = 0; rowNo < HEIGHT; rowNo++) {
      row = new StringBuilder();
      for (String symbol : this.crossword[rowNo]) {
        row.append(symbol);
      }
      matcher = regex.matcher(row.toString());
      while (matcher.find()) {
        equation = new Equation();
        equationString = matcher.group();
        equation.parseEquation(equationString);
        this.equations.add(equation);
      }
    }
  }

  private void parseVariables() {
    int variables = 1;
    String variable = "x_";
    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        if (this.crossword[i][j].equals("_")) {
          this.crossword[i][j] = "x_" + variables;
          nameVariable(variable + variables);
          variables++;
        } else if (this.crossword[i][j].matches("[0-9]")) {
          nameVariable(this.crossword[i][j]);
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

  private void nameVariable(String variable) {
    this.variables.put(variable, null);
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

  public List<Equation> getEquations() {
    return new ArrayList<>(this.equations);
  }

  public Map<String, Integer> getVariables() {
    return new HashMap<>(this.variables);
  }

  public void updateVariables(Map<String, Integer> variableValues) {
    for (Map.Entry<String, Integer> entry : variableValues.entrySet()) {
      this.variables.put(entry.getKey(), entry.getValue());
    }
  }

  public void replaceVariablesWithValues() {
    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        String cell = this.crossword[i][j];
        if (cell.startsWith("x_")) {
          Integer value = this.variables.get(cell);
          if (value != null) {
            this.crossword[i][j] = value.toString();
          }
        }
      }
    }
  }

}
