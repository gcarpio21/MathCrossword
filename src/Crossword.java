import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The `Crossword` class represents a crossword puzzle that can be solved using
 * a SMT solver supported by JavaSMT.
 * It stores the crossword grid, equations, and variables involved in the puzzle.
 * For the effective parsing it stores a list of instances of the `Equation` class.'
 * A map of variables is used to store the variables and their values.
 * This facilitates the creation of IntegerFormulas for the SMT solver
 * and keeping track of their value.
 *
 * <p>This class provides methods to:
 * <ul>
 *   <li>Format the crossword grid from a string input</li>
 *   <li>Parse the crossword to extract equations and variables</li>
 *   <li>Update variable values and replace them in the grid</li>
 *   <li>Retrieve the crossword grid, equations, and variables</li>
 * </ul>
 *
 * @see Equation
 */
public class Crossword {
  public final int width = 9;
  public final int height = 11;
  private String[][] crossword = new String[height][width];
  private List<Equation> equations = new ArrayList<>();
  private Map<String, Integer> variables = new HashMap<>();

  /**
   * Constructs a `Crossword` object from a string representation of the crossword.
   *
   * @param crossword the string representation of the crossword
   */
  public Crossword(String crossword) {
    crossword = crossword.replace("\n", "");
    formatCrossword(crossword);
  }

  /**
   * Parses the crossword to extract equations and variables.
   */
  public void parseCrossword() {
    String regex =
        "([0-9]|x_[1-9][0-9]*)(\\+|\\-|\\*|\\/)([0-9]|x_[1-9][0-9]*)=([0-9]|x_[1-9][0-9]*)";
    Pattern pattern = Pattern.compile(regex);
    parseVariables();
    addHorizontalEquations(pattern);
    addVerticalEquations(pattern);
  }

  /**
   * Adds vertical equations to the list of equations by matching the given regex pattern.
   *
   * @param regex the regex pattern to match equations
   */
  private void addVerticalEquations(Pattern regex) {
    StringBuilder column;
    Matcher matcher;
    String equationString;
    Equation equation;

    for (int colNo = 0; colNo < width; colNo++) {
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

  /**
   * Adds horizontal equations to the list of equations by matching the given regex pattern.
   *
   * @param regex the regex pattern to match equations
   */
  private void addHorizontalEquations(Pattern regex) {

    StringBuilder row;
    Matcher matcher;
    String equationString;
    Equation equation;

    for (int rowNo = 0; rowNo < height; rowNo++) {
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

  /**
   * Parses the variables in the crossword grid and assigns them unique names.
   */
  private void parseVariables() {
    int variables = 1;
    String variable = "x_";
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
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

  /**
   * Formats the crossword grid from a string representation.
   *
   * @param crossword the string representation of the crossword
   */
  private void formatCrossword(String crossword) {
    char[] crosswordChars = crossword.toCharArray();

    int charIndex = 0;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++, charIndex++) {
        this.crossword[i][j] = String.valueOf(crosswordChars[charIndex]);
      }
    }
  }

  /**
   * Adds a variable to the map of variables.
   *
   * @param variable the variable to add
   */
  private void nameVariable(String variable) {
    this.variables.put(variable, null);
  }

  /**
   * Returns a string representation of the crossword grid.
   *
   * @return the string representation of the crossword grid
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    String crossword = "";
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        sb.append(this.crossword[i][j]);
      }
      sb.append("\n");
    }
    crossword = sb.toString();
    return crossword;
  }

  /**
   * Returns a copy of the crossword grid.
   *
   * @return a copy of the crossword grid
   */
  public String[][] getCrossword() {
    String[][] crosswordCopy = new String[height][width];
    for (int i = 0; i < height; i++) {
      System.arraycopy(this.crossword[i], 0, crosswordCopy[i], 0, width);
    }
    return crosswordCopy;
  }

  /**
   * Returns a copy of the list of equations.
   *
   * @return a copy of the list of equations
   */
  public List<Equation> getEquations() {
    return new ArrayList<>(this.equations);
  }

  /**
   * Returns a copy of the map of variables.
   *
   * @return a copy of the mape of variables
   */
  public Map<String, Integer> getVariables() {
    return new HashMap<>(this.variables);
  }

  /**
   * Updates the values of the variables.
   */
  public void updateVariables(Map<String, Integer> variableValues) {
    for (Map.Entry<String, Integer> entry : variableValues.entrySet()) {
      this.variables.put(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Replaces the variables in the crossword grid with their values.
   */
  public void replaceVariablesWithValues() {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
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
