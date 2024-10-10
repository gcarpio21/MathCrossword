import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The `Equation` class represents a mathematical equation with an operation and operands.
 * It provides methods to parse an equation string, extract variables, and retrieve the components
 * of the equation.
 */
public class Equation {

  private String resultSide;
  private String operation;
  private String[] operationSide;


  /**
   * Parses the given equation string and extracts the operation and operands.
   *
   * @param equation the equation string to parse
   */
  void parseEquation(String equation) {
    String[] sides = equation.split("=");
    Pattern pattern = Pattern.compile("[+\\-*/]");
    Matcher matcher;

    for (String side : sides) {

      matcher = pattern.matcher(side);
      if (matcher.find()) {
        setOperationSide(extractVariables(side));
        switch (matcher.group()) {
          case "+":
            setOperation("+");
            break;
          case "-":
            setOperation("-");
            break;
          case "*":
            setOperation("*");
            break;
          case "/":
            setOperation("/");
            break;
          default:
            break;
        }
      } else {
        setResultSide(side);
      }

    }
  }

  /**
   * Extracts variables from the given side of the equation.
   *
   * @param side the side of the equation to extract variables from
   * @return an array of variables
   */
  private String[] extractVariables(String side) {
    String[] variables = side.split("[+\\-*/]");
    return variables; //The order of arguments is the same as their order in the array
  }


  /**
   * Returns the operation of the equation.
   *
   * @return the operation of the equation
   */
  String getOperation() {

    return new String(operation);
  }

  /**
   * Sets the operation of the equation.
   *
   * @param operation the operation of the equation
   */
  void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Returns the operands of the equation.
   *
   * @return an array of operands
   */
  String[] getOperationSide() {
    return operationSide.clone();
  }

  /**
   * Sets the operands of the equation.
   *
   * @param operationSide an array of operands
   */
  void setOperationSide(String[] operationSide) {
    this.operationSide = operationSide.clone();
  }

  /**
   * Returns the result side of the equation.
   *
   * @return the result side of the equation
   */
  public String getResultSide() {
    return new String(resultSide);
  }

  /**
   * Sets the operands of the equation.
   *
   * @param resultSide an array of operands
   */
  void setResultSide(String resultSide) {
    this.resultSide = resultSide;
  }
}
